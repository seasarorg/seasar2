/*
 * Copyright 2004-2005 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.framework.xml;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public final class TagHandlerContext {

	private static final Integer ONE = new Integer(1);
	private StringBuffer body = null;
	private StringBuffer characters = new StringBuffer();
	private Stack bodyStack = new Stack();
	private StringBuffer path = new StringBuffer();
	private StringBuffer detailPath = new StringBuffer();
	private String qName = "";
	private Stack qNameStack = new Stack();
	private Object result;
	private Stack stack = new Stack();
	private Map pathCounts = new HashMap();
	private Map parameters = new HashMap();

	public void push(Object o) {
		if (stack.empty()) {
			result = o;
		}
		stack.push(o);
	}

	public Object getResult() {
		return result;
	}

	public Object pop() {
		return stack.pop();
	}

	public Object peek() {
		return stack.peek();
	}

	public Object peek(final int n) {
		return stack.get(stack.size() - n - 1);
	}

	public Object peek(final Class clazz) {
		for (int i = stack.size() - 1; i >= 0; --i) {
			Object o = stack.get(i);
			if (clazz.isInstance(o)) {
				return o;
			}
		}
		return null;
	}

	public Object peekFirst() {
		return stack.get(0);
	}
	
	public Object getParameter(String name) {
		return parameters.get(name);
	}
	
	public void addParameter(String name, Object parameter) {
		parameters.put(name, parameter);
	}

	public void startElement(String qName) {
		bodyStack.push(body);
		body = new StringBuffer();
		characters = new StringBuffer();
		qNameStack.push(this.qName);
		this.qName = qName;
		path.append("/");
		path.append(qName);
		int pathCount = incrementPathCount();
		detailPath.append("/");
		detailPath.append(qName);
		detailPath.append("[");
		detailPath.append(pathCount);
		detailPath.append("]");
	}

	public void characters(char[] buffer, int start, int length) {
		body.append(buffer, start, length);
		characters.append(buffer, start, length);
	}

	public String getCharacters() {
		return characters.toString().trim();
	}
	
	public String getBody() {
		return body.toString().trim();
	}
	
	public boolean isCharactersEol() {
		if (characters.length() == 0) {
			return false;
		}
		return characters.charAt(characters.length() - 1) == '\n';
	}

	public void clearCharacters() {
		characters = new StringBuffer();
	}

	public void endElement() {
		body = (StringBuffer) bodyStack.pop();
		remoteLastPath(path);
		remoteLastPath(detailPath);
		qName = (String) qNameStack.pop();
	}

	private static void remoteLastPath(StringBuffer path) {
		path.delete(path.lastIndexOf("/"), path.length());
	}

	public String getPath() {
		return path.toString();
	}

	public String getDetailPath() {
		return detailPath.toString();
	}
	
	public String getQName() {
		return qName;
	}

	private int incrementPathCount() {
		String path = getPath();
		Integer pathCount = (Integer) pathCounts.get(path);
		if (pathCount == null) {
			pathCount = ONE;
		} else {
			pathCount = new Integer(pathCount.intValue() + 1);
		}
		pathCounts.put(path, pathCount);
		return pathCount.intValue();
	}
}