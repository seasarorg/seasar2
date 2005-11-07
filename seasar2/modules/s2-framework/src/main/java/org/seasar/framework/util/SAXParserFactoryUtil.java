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
package org.seasar.framework.util;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.seasar.framework.exception.ParserConfigurationRuntimeException;
import org.seasar.framework.exception.SAXRuntimeException;
import org.xml.sax.SAXException;

/**
 * @author higa
 *
 */
public final class SAXParserFactoryUtil {

	private SAXParserFactoryUtil() {
	}

	public static SAXParserFactory newInstance() {
		return SAXParserFactory.newInstance();
	}

	public static SAXParser newSAXParser() {
		return newSAXParser(newInstance());
	}
	
	public static SAXParser newSAXParser(SAXParserFactory factory) {
		try {
			return factory.newSAXParser();
		} catch (ParserConfigurationException e) {
			throw new ParserConfigurationRuntimeException(e);
		} catch (SAXException e) {
			throw new SAXRuntimeException(e);
		}
	}
}
