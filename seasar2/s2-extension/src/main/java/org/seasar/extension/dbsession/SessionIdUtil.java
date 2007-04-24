/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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
package org.seasar.extension.dbsession;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Session Idに関するユーティリティです。
 * 
 * @author higa
 * 
 */
public final class SessionIdUtil {

    /**
     * Session Idのキーをあらわします。
     */
    public static final String SESSION_ID_KEY = "s2sessionid";

    private static final String PART_OF_URI = ";" + SESSION_ID_KEY + "=";

    /**
     * CookieからSession Idを取り出します。
     * 
     * @param request
     * @return
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
     * URIからSession Idを取り出します。
     * 
     * @param request
     * @return
     */
    public static String getSessionIdFromURL(HttpServletRequest request) {
        String uri = request.getRequestURI();
        int index = uri.lastIndexOf(PART_OF_URI);
        if (index < 0) {
            return null;
        }
        return uri.substring(index + PART_OF_URI.length());
    }

    /**
     * URLを書き換えて、必要ならSession Idを組み込みます。
     * 
     * @param url
     * @param request
     * @return
     */
    public static String rewriteURL(String url, HttpServletRequest request) {
        if (request.isRequestedSessionIdFromCookie()) {
            return url;
        }
        HttpSession session = request.getSession(false);
        if (session == null) {
            return url;
        }
        return url + PART_OF_URI + session.getId();
    }
}
