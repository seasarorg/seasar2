/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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

/**
 * {@link SaxHandler}を使って解析するためのクラスです。
 * 
 * @author higa
 * 
 */
public class SaxHandlerParser {

    private SaxHandler saxHandler;

    private SAXParser saxParser;

    /**
     * {@link SaxHandlerParser}を作成します。
     * 
     * @param saxHandler
     */
    public SaxHandlerParser(SaxHandler saxHandler) {
        this(saxHandler, SAXParserFactoryUtil.newSAXParser());
    }

    /**
     * {@link SaxHandlerParser}を作成します。
     * 
     * @param saxHandler
     * @param saxParser
     */
    public SaxHandlerParser(SaxHandler saxHandler, SAXParser saxParser) {
        this.saxHandler = saxHandler;
        this.saxParser = saxParser;
    }

    /**
     * {@link SaxHandler}を返します。
     * 
     * @return {@link SaxHandler}
     */
    public SaxHandler getSaxHandler() {
        return saxHandler;
    }

    /**
     * {@link SAXParser}を返します。
     * 
     * @return {@link SAXParser}
     */
    public SAXParser getSAXParser() {
        return saxParser;
    }

    /**
     * XMLを解析します。
     * 
     * @param path
     * @return 解析した結果
     */
    public Object parse(String path) {
        return parse(ResourceUtil.getResourceAsStream(path), path);
    }

    /**
     * XMLを解析します。
     * 
     * @param inputStream
     * @return 解析した結果
     */
    public Object parse(InputStream inputStream) {
        return parse(new InputSource(inputStream));
    }

    /**
     * XMLを解析します。
     * 
     * @param inputStream
     * @param path
     * @return 解析した結果
     */
    public Object parse(InputStream inputStream, String path) {
        InputSource is = new InputSource(inputStream);
        is.setSystemId(path);
        return parse(is);
    }

    /**
     * XMLを解析します。
     * 
     * @param inputSource
     * @return 解析した結果
     */
    public Object parse(InputSource inputSource) {
        SAXParserUtil.parse(saxParser, inputSource, saxHandler);
        return saxHandler.getResult();
    }
}
