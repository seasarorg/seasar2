/*
 * Copyright 2004-2012 the Seasar Foundation and the Others.
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

import java.util.Locale;

import javax.portlet.PortletRequest;

/**
 * モック用の<code>PortletRequest</code>のインターフェースです。
 * 
 * @author shinsuke
 * 
 */
public interface MockPortletRequest extends PortletRequest {

    /**
     * プロパティを追加します。
     * 
     * @param name
     * @param value
     */
    public void addProperty(String name, Object value);

    /**
     * <code>authType</code>を設定します。
     * 
     * @param authType
     */
    public void setAuthType(String authType);

    /**
     * パラメータを追加します。
     * 
     * @param name
     * @param value
     */
    public void addParameter(String name, String value);

    /**
     * 配列のパラメータを追加します。
     * 
     * @param name
     * @param values
     */
    public void addParameter(String name, String[] values);

    /**
     * パラメータを設定します。
     * 
     * @param name
     * @param value
     */
    public void setParameter(String name, String value);

    /**
     * 配列のパラメータを設定します。
     * 
     * @param name
     * @param values
     */
    public void setParameter(String name, String[] values);

    /**
     * レスポンスの<code>contentType</code>を追加します。
     * 
     * @param contentType
     */
    public void addResponseContentType(String contentType);

    /**
     * レスポンスの<code>contentType</code>を削除します。
     * 
     * @param contentType
     */
    public void removeResponseContentType(String contentType);

    /**
     * {@link Locale}を追加します。
     * 
     * @param locale
     */
    public void addLocale(Locale locale);

    /**
     * {@link Locale}を削除します。
     * 
     * @param locale
     */
    public void removeLocale(Locale locale);

    /**
     * <code>scheme</code>を設定します。
     * 
     * @param scheme
     */
    public void setScheme(String scheme);

    /**
     * サーバ名を設定します。
     * 
     * @param serverName
     */
    public void setServerName(String serverName);

    /**
     * サーバの<code>port</code>を設定します。
     * 
     * @param serverPort
     */
    public void setServerPort(int serverPort);
}
