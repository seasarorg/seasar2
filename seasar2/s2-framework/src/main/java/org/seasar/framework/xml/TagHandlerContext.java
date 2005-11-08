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
	private StringBuffer body_ = null;
	private StringBuffer characters_ = new StringBuffer();
	private Stack bodyStack_ = new Stack();
	private StringBuffer path_ = new StringBuffer();
	private StringBuffer detailPath_ = new StringBuffer();
	private String qName_ = "";
	private Stack qNameStack_ = new Stack();
	private Object result_;
	private Stack stack_ = new Stack();
	private Map pathCounts_ = new HashMap();
	private Map parameters_ = new HashMap();

	public void push(Object o) {
		if (stack_.empty()) {
			result_ = o;
		}
		stack_.push(o);
	}

	public Object getResult() {
		return result_;
	}

	public Object pop() {
		return stack_.pop();
	}

	public Object peek() {
		return stack_.peek();
	}

	public Object peek(final int n) {
		return stack_.get(stack_.size() - n - 1);
	}

	public Object peek(final Class clazz) {
		for (int i = stack_.size() - 1; i >= 0; --i) {
			Object o = stack_.get(i);
			if (clazz.isInstance(o)) {
				return o;
			}
		}
		return null;
	}

	public Object peekFirst() {
		return stack_.get(0);
	}
	
	public Object getParameter(String name) {
		return parameters_.get(name);
	}
	
	public void addParameter(String name, Object parameter) {
		parameters_.put(name, parameter);
	}

	public void startElement(String qName) {
		bodyStack_.push(body_);
		body_ = new StringBuffer();
		characters_ = new StringBuffer();
		qNameStack_.push(qName_);
		qName_ = qName;
		path_.append("/");
		path_.append(qName);
		int pathCount = incrementPathCount();
		detailPath_.append("/");
		detailPath_.append(qName);
		detailPath_.append("[");
		detailPath_.append(pathCount);
		detailPath_.append("]");
	}

	public void characters(char[] buffer, int start, int length) {
		body_.append(buffer, start, length);
		characters_.append(buffer, start, length);
	}

	public String getCharacters() {
		return characters_.toString().trim();
	}
	
	public String getBody() {
		return body_.toString().trim();
	}
	
	public boolean isCharactersEol() {
		if (characters_.length() == 0) {
			return false;
		}
		return characters_.charAt(characters_.length() - 1) == '\n';
	}

	public void clearCharacters() {
		characters_ = new StringBuffer();
	}

	public void endElement() {
		body_ = (StringBuffer) bodyStack_.pop();
		remoteLastPath(path_);
		remoteLastPath(detailPath_);
		qName_ = (String) qNameStack_.pop();
	}

	private static void remoteLastPath(StringBuffer path) {
		path.delete(path.lastIndexOf("/"), path.length());
	}

	public String getPath() {
		return path_.toString();
	}

	public String getDetailPath() {
		return detailPath_.toString();
	}
	
	public String getQName() {
		return qName_;
	}

	private int incrementPathCount() {
		String path = getPath();
		Integer pathCount = (Integer) pathCounts_.get(path);
		if (pathCount == null) {
			pathCount = ONE;
		} else {
			pathCount = new Integer(pathCount.intValue() + 1);
		}
		pathCounts_.put(path, pathCount);
		return pathCount.intValue();
	}
}