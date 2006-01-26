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
package org.seasar.framework.mock.servlet;

import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Satoshi Kimura
 */
public interface MockHttpServletRequest extends HttpServletRequest {
    void addParameter(String name, String value);
    void addParameter(String name, String[] values);
    void addCookie(Cookie cookie);
    void addHeader(String name, String value);
    void setAuthType(String authType);
    void addDateHeader(String name, long value);
    void addIntHeader(String name, int value);
    void setPathInfo(String pathInfo);
    void setPathTranslated(String pathTranslated);
    void setQueryString(String queryString);
    void setContentLength(int contentLength);
    void setContentType(String contentType);
    void setParameter(String name, String value);
    void setParameter(String name, String[] values);
    void setProtocol(String protocol);
    void setScheme(String scheme);
    void setServerName(String serverName);
    void setServerPort(int serverPort);
    void setRemoteAddr(String remoteAddr);
    void setRemoteHost(String remoteHost);
    void setLocale(Locale locale);
}