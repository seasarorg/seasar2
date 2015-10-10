/*
 * Copyright 2004-2015 the Seasar Foundation and the Others.
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

import java.io.IOException;

import javax.xml.parsers.SAXParser;

import org.seasar.framework.exception.IORuntimeException;
import org.seasar.framework.exception.SAXRuntimeException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 * {@link SAXParser}用のユーティリティ・クラスです。
 * 
 * @author higa
 */
public class SAXParserUtil {

    /**
     * インスタンスを構築します。
     */
    protected SAXParserUtil() {
    }

    /**
     * 指定された{@link InputSource}のコンテンツを、指定された{@link DefaultHandler}を使用してXMLとして構文解析します。
     * 
     * @param parser
     *            使用する{@link SAXParser}
     * @param inputSource
     *            構文解析されるコンテンツを含む{@link InputSource}
     * @param handler
     *            使用するSAX {@link DefaultHandler}
     */
    public static void parse(SAXParser parser, InputSource inputSource,
            DefaultHandler handler) {
        try {
            parser.parse(inputSource, handler);
        } catch (SAXException e) {
            throw new SAXRuntimeException(e);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * {@link XMLReader}の基本となる実装に特定のプロパティを設定します。
     * 
     * @param parser
     *            プロパティを設定する{@link SAXParser}
     * @param name
     *            設定されるプロパティの名前
     * @param value
     *            設定されるプロパティの値
     */
    public static void setProperty(final SAXParser parser, final String name,
            final String value) {
        try {
            parser.setProperty(name, value);
        } catch (final SAXException e) {
            throw new SAXRuntimeException(e);
        }
    }

}
