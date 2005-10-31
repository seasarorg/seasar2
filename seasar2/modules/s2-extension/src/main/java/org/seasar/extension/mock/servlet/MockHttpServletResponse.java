/*
 * Copyright 2004-2005 the Seasar Foundation and the Others.
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
package org.seasar.extension.mock.servlet;

import java.util.Enumeration;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Satoshi Kimura
 */
public interface MockHttpServletResponse extends HttpServletResponse {
    Cookie[] getCookies();
    int getStatus();
    String getMessage();
    Enumeration getHeaders(String name);
    String getHeader(String name);
    Enumeration getHeaderNames();
    int getIntHeader(String name);
    void setCharacterEncoding(String characterEncoding);
    int getContentLength();
    String getContentType();
}