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
package org.seasar.extension.httpsession;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * セッション情報をS2で管理するためのHttpServletResponseWrapperです。
 * 
 * @author higa
 * 
 */
public class S2HttpServletResponseWrapper extends HttpServletResponseWrapper {

    private HttpServletRequest request;

    /**
     * <code>S2HttpServletResponseWrapper</code>のインスタンスを構築します。
     * 
     * @param response
     * @param request
     */
    public S2HttpServletResponseWrapper(HttpServletResponse response,
            HttpServletRequest request) {
        super(response);
        this.request = request;
    }

    public String encodeRedirectUrl(String url) {
        return encodeRedirectURL(url);
    }

    public String encodeRedirectURL(String url) {
        return SessionIdUtil.rewriteURL(url, request);
    }

    public String encodeUrl(String url) {
        return super.encodeURL(url);
    }

    public String encodeURL(String url) {
        return SessionIdUtil.rewriteURL(url, request);
    }
}
