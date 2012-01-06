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
package org.seasar.extension.httpsession;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.seasar.framework.util.StringUtil;

/**
 * セッション識別子に関するユーティリティクラスです。
 * 
 * @author higa
 * 
 */
public class SessionIdUtil {

    /**
     * セッション識別子のキーをあらわします。
     */
    public static final String SESSION_ID_KEY = "S2SESSIONID";

    private static final String PART_OF_URI = ";" + SESSION_ID_KEY + "=";

    static String cookieName = SESSION_ID_KEY;

    static int cookieMaxAge = -1;

    static String cookiePath = null;

    static Boolean cookieSecure = null;

    /**
     * Cookieからセッション識別子を取り出します。
     * 
     * @param request
     *            リクエスト
     * @return セッション識別子
     */
    public static String getSessionIdFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (int i = 0; i < cookies.length; i++) {
            Cookie cookie = cookies[i];
            if (cookie.getName().equals(SESSION_ID_KEY)) {
                return cookie.getValue();
            }
        }
        return null;
    }

    /**
     * URIからセッション識別子を取り出します。
     * 
     * @param request
     *            リクエスト
     * @return セッション識別子
     */
    public static String getSessionIdFromURL(HttpServletRequest request) {
        String uri = request.getRequestURI();
        int index = uri.lastIndexOf(PART_OF_URI);
        if (index < 0) {
            return null;
        }
        uri = uri.substring(index + PART_OF_URI.length());
        int index2 = uri.indexOf('?');
        if (index2 < 0) {
            return uri;
        }
        return uri.substring(0, index2);
    }

    /**
     * URLを書き換えて必要ならセッション識別子を組み込みます。
     * 
     * @param url
     *            URL
     * @param request
     *            リクエスト
     * @return 書き換えたURL
     */
    public static String rewriteURL(String url, HttpServletRequest request) {
        if (request.isRequestedSessionIdFromCookie()) {
            return url;
        }
        HttpSession session = request.getSession(false);
        if (session == null) {
            return url;
        }
        int index = url.indexOf('?');
        if (index < 0) {
            return url + PART_OF_URI + session.getId();
        } else {
            return url.substring(0, index) + PART_OF_URI + session.getId()
                    + url.substring(index);
        }
    }

    /**
     * セッション識別子用のCookieを書き込みます。
     * 
     * @param request
     *            リクエスト
     * @param response
     *            レスポンス
     * @param sessionId
     *            セッション識別子
     */
    public static void writeCookie(HttpServletRequest request,
            HttpServletResponse response, String sessionId) {
        if (request.isRequestedSessionIdFromCookie()) {
            return;
        }
        Cookie cookie = new Cookie(cookieName, sessionId);
        if (StringUtil.isNotEmpty(cookiePath)) {
            cookie.setPath(cookiePath);
        } else {
            String path = request.getContextPath();
            cookie.setPath(StringUtil.isEmpty(path) ? "/" : path);
        }
        cookie.setMaxAge(cookieMaxAge);
        if (cookieSecure != null) {
            cookie.setSecure(cookieSecure.booleanValue());
        }
        response.addCookie(cookie);
    }
}
