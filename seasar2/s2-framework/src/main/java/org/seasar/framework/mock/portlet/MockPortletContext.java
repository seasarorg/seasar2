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
package org.seasar.framework.mock.portlet;

import javax.portlet.PortletContext;

/**
 * モック用の<code>PortletContext</code>です。
 * 
 * @author shinsuke
 * 
 */
public interface MockPortletContext extends PortletContext {
    /**
     * 初期化パラメータを設定します。
     * 
     * @param name
     * @param value
     */
    public void setInitParameter(String name, String value);

    /**
     * mimeタイプを追加します。
     * 
     * @param file
     * @param type
     */
    public void addMimeType(String file, String type);

    /**
     * リクエストを作成します。
     * 
     * @return リクエスト
     */
    public MockPortletRequestImpl createRequest();
}
