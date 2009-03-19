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
package org.seasar.framework.container.hotdeploy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

/**
 * HOT deploy用の{@link HttpServletRequest}です。
 * 
 * @author koichik
 */
public class HotdeployHttpServletRequest extends HttpServletRequestWrapper {

    /** オリジナルの{@link HttpServletRequest}です。 */
    protected HttpServletRequest originalRequest;

    /** HOT deploy用の{@link HttpSession}です。 */
    protected HttpSession session;

    /**
     * インスタンスを構築します。
     * 
     * @param originalRequest
     */
    public HotdeployHttpServletRequest(HttpServletRequest originalRequest) {
        super(originalRequest);
        this.originalRequest = originalRequest;
    }

    public HttpSession getSession() {
        return getSession(true);
    }

    public HttpSession getSession(boolean create) {
        if (session != null) {
            return session;
        }
        final HttpSession originalSession = originalRequest.getSession(create);
        if (originalSession == null) {
            return originalSession;
        }
        session = new HotdeployHttpSession(originalSession);
        return session;
    }

}
