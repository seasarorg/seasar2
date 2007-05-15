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

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * セッション情報をS2で管理するためのHttpServletResponseWrapperです。
 * 
 * @author higa
 * 
 */
public class S2HttpServletResponseWrapper extends HttpServletResponseWrapper {

    private S2HttpServletRequestWrapper requestWrapper;

    private SessionStateManager sessionStateManager;

    /**
     * <code>S2HttpServletResponseWrapper</code>のインスタンスを構築します。
     * 
     * @param response
     * @param requestWrapper
     * @param sessionStateManager
     */
    public S2HttpServletResponseWrapper(HttpServletResponse response,
            S2HttpServletRequestWrapper requestWrapper,
            SessionStateManager sessionStateManager) {
        super(response);
        this.requestWrapper = requestWrapper;
        this.sessionStateManager = sessionStateManager;
    }

    public String encodeRedirectUrl(String url) {
        return encodeRedirectURL(url);
    }

    public String encodeRedirectURL(String url) {
        return SessionIdUtil.rewriteURL(url, requestWrapper);
    }

    public String encodeUrl(String url) {
        return super.encodeURL(url);
    }

    public String encodeURL(String url) {
        return SessionIdUtil.rewriteURL(url, requestWrapper);
    }

    public void flushBuffer() throws IOException {
        S2HttpSession session = requestWrapper.getS2HttpSession();
        if (session != null) {
            SessionState sessionState = session.getSessionState();
            if (sessionState != null) {
                sessionStateManager.updateState(session.getId(), sessionState);
            }
        }
        super.flushBuffer();
    }
}
