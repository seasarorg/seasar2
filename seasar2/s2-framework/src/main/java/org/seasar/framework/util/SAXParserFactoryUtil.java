/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
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

import java.lang.reflect.Method;

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

    public static final String XERCES_XINCLUDE_FUTURE = "http://apache.org/xml/features/xinclude";

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

    public static boolean setXIncludeAware(final SAXParserFactory spf,
            final boolean state) {
        try {
            final Method method = spf.getClass().getMethod("setXIncludeAware",
                    new Class[] { boolean.class });
            method.invoke(spf, new Object[] { Boolean.valueOf(state) });
            return true;
        } catch (final Exception ignore) {
        }
        try {
            spf.setFeature(XERCES_XINCLUDE_FUTURE, state);
            return true;
        } catch (Exception ignore) {
        }
        return false;
    }

}
