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
package org.seasar.framework.mock.servlet;

import java.util.Enumeration;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * モック用の{@link HttpServletResponse}のインターフェースです。
 * 
 * @author Satoshi Kimura
 */
public interface MockHttpServletResponse extends HttpServletResponse {

    /**
     * {@link Cookie}の配列を返します。
     * 
     * @return {@link Cookie}の配列
     */
    Cookie[] getCookies();

    /**
     * ステータスを返します。
     * 
     * @return ステータス
     */
    int getStatus();

    /**
     * メッセージを返します。
     * 
     * @return メッセージ
     */
    String getMessage();

    /**
     * ヘッダの{@link Enumeration}を返します。
     * 
     * @param name
     * @return ヘッダの{@link Enumeration}
     */
    Enumeration getHeaders(String name);

    /**
     * ヘッダを返します。
     * 
     * @param name
     * @return ヘッダ
     */
    String getHeader(String name);

    /**
     * ヘッダ名の{@link Enumeration}を返します。
     * 
     * @return ヘッダ名の{@link Enumeration}
     */
    Enumeration getHeaderNames();

    /**
     * ヘッダを<code>int</code>で返します。
     * 
     * @param name
     * @return ヘッダのintの値
     */
    int getIntHeader(String name);

    void setCharacterEncoding(String characterEncoding);

    /**
     * コンテンツの長さを返します。
     * 
     * @return コンテンツの長さ
     */
    int getContentLength();

    String getContentType();

    /**
     * <code>response</code>を文字列として返します。
     * 
     * @return <code>response</code>の文字列表現
     */
    String getResponseString();

    /**
     * <code>response</code>をバイトの配列で返します。
     * 
     * @return <code>response</code>のバイト配列表現
     */
    byte[] getResponseBytes();
}