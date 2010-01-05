/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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
package org.seasar.framework.mock.servlet;

import java.util.Map;

import javax.servlet.ServletContext;

/**
 * モック用の{@link ServletContext}のインターフェースです。
 * 
 * @author Satoshi Kimura
 * @author manhole
 */
public interface MockServletContext extends ServletContext {

    /**
     * <code>mimeType</code>を追加します。
     * 
     * @param file
     * @param type
     */
    void addMimeType(String file, String type);

    /**
     * 初期化パラメータを設定します。
     * 
     * @param name
     * @param value
     */
    void setInitParameter(String name, String value);

    /**
     * リクエストを作成します。
     * 
     * @param path
     * @return {@link MockHttpServletRequest}
     */
    MockHttpServletRequest createRequest(String path);

    /**
     * サーブレットコンテキスト名を設定します。
     * 
     * @param servletContextName
     */
    void setServletContextName(String servletContextName);

    /**
     * 初期化パラメータの{@link Map}を返します。
     * 
     * @return
     */
    Map getInitParameterMap();

}
