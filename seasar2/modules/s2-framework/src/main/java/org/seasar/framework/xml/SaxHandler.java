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

import org.seasar.framework.util.ResourceUtil;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public final class SaxHandler extends DefaultHandler {

	private TagHandlerRule tagHandlerRule_;
	private TagHandlerContext context_ = new TagHandlerContext();
	private Map dtdPaths_ = new HashMap();

	public SaxHandler(TagHandlerRule tagHandlerRule) {
		tagHandlerRule_ = tagHandlerRule;
	}
	
	public TagHandlerContext getTagHandlerContext() {
		return context_;
	}

	public void startElement(String namespaceURI, String localName,
			String qName, Attributes attributes) {

		appendBody();
		context_.startElement(qName);
		start(attributes);
	}

	public void characters(char[] buffer, int start, int length) {
		int begin = start;
		int end = start + length;
		for (int i = begin; i < end; ++i) {
			if (buffer[i] == '\n') {
				context_.characters(buffer, begin, i - begin + 1);
				appendBody();
				begin = i + 1;
			}
		}
		if (begin < end) {
			context_.characters(buffer, begin, end - begin);
		}
	}

	public void endElement(String namespaceURI, String localName, String qName) {

		appendBody();
		end();
		context_.endElement();
	}

	public InputSource resolveEntity(String publicId, String systemId)
			throws SAXException {

		String dtdPath = null;
		if (publicId != null) {
			dtdPath = (String) dtdPaths_.get(publicId);
		}
		if (dtdPath == null) {
			return null;
		}
		return new InputSource(ResourceUtil.getResourceAsStream(dtdPath));
	}

	public void error(SAXParseException e) throws SAXException {
		throw e;
	}

	public void warning(SAXParseException e) throws SAXException {
		System.err.println(e);
	}

	public void registerDtdPath(String publicId, String dtdPath) {
		dtdPaths_.put(publicId, dtdPath);
	}

	public Object getResult() {
		return context_.getResult();
	}

	private TagHandler getTagHandlerByPath() {
		return tagHandlerRule_.getTagHandler(context_.getPath());
	}

	private TagHandler getTagHandlerByQName() {
		return tagHandlerRule_.getTagHandler(context_.getQName());
	}

	private void start(Attributes attributes) {
		TagHandler th = getTagHandlerByPath();
		start(th, attributes);
		th = getTagHandlerByQName();
		start(th, attributes);
	}

	private void start(TagHandler handler, Attributes attributes) {
		if (handler != null) {
			try {
				handler.start(context_, attributes);
			} catch (RuntimeException ex) {
				reportDetailPath();
				ex.printStackTrace();
				throw ex;
			}

		}
	}

	private void appendBody() {
		String characters = context_.getCharacters();
		if (characters.length() > 0) {
			TagHandler th = getTagHandlerByPath();
			appendBody(th, characters);
			th = getTagHandlerByQName();
			appendBody(th, characters);
			context_.clearCharacters();
		}
	}

	private void appendBody(TagHandler handler, String characters) {
		if (handler != null) {
			try {
				handler.appendBody(context_, characters);
			} catch (RuntimeException ex) {
				reportDetailPath();
				ex.printStackTrace();
				throw ex;
			}

		}
	}

	private void end() {
		String body = context_.getBody();
		TagHandler th = getTagHandlerByPath();
		end(th, body);
		th = getTagHandlerByQName();
		end(th, body);
	}

	private void end(TagHandler handler, String body) {
		if (handler != null) {
			try {
				handler.end(context_, body);
			} catch (RuntimeException ex) {
				reportDetailPath();
				ex.printStackTrace();
				throw ex;
			}

		}
	}

	private void reportDetailPath() {
		System.err.println("Exception occured at " + context_.getDetailPath());
	}
}