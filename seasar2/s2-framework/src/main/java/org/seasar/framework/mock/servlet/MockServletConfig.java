/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

/**
 * モック用の{@link ServletConfig}です。
 * 
 * @author Satoshi Kimura
 */
public interface MockServletConfig extends ServletConfig {

    /**
     * サーブレット名を設定します。
     * 
     * @param servletName
     */
    void setServletName(String servletName);

    /**
     * {@link ServletContext}を設定します。
     * 
     * @param servletContext
     */
    void setServletContext(ServletContext servletContext);

    /**
     * 初期化パラメータを設定します。
     * 
     * @param name
     * @param value
     */
    void setInitParameter(String name, final String value);
}
