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

import java.io.InputStream;

import javax.xml.parsers.SAXParser;

import org.seasar.framework.util.ResourceUtil;
import org.seasar.framework.util.SAXParserFactoryUtil;
import org.seasar.framework.util.SAXParserUtil;
import org.xml.sax.InputSource;

public final class SaxHandlerParser {

	private SaxHandler saxHandler;
	private SAXParser saxParser;

	public SaxHandlerParser(SaxHandler saxHandler) {
		this(saxHandler, SAXParserFactoryUtil.newSAXParser());
	}

	public SaxHandlerParser(SaxHandler saxHandler, SAXParser saxParser) {
		this.saxHandler = saxHandler;
		this.saxParser = saxParser;
	}
	
	public SaxHandler getSaxHandler() {
		return saxHandler;
	}
	
	public SAXParser getSAXParser() {
		return saxParser;
	}

	public Object parse(String path) {
		return parse(ResourceUtil.getResourceAsStream(path));
	}

	public Object parse(InputStream inputStream) {
		return parse(new InputSource(inputStream));
	}

	public Object parse(InputSource inputSource) {
		SAXParserUtil.parse(saxParser, inputSource, saxHandler);
		return saxHandler.getResult();
	}
}
