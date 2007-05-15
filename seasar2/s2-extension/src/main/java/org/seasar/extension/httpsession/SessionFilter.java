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

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;

/**
 * セッションの状態をデータベースに格納するためのFilterです。
 * 
 * @author higa
 * 
 */
public class SessionFilter implements Filter {

    private SessionStateManager sessionStateManager;

    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        SessionStateManager ssm = getSessionStateManager();
        S2HttpServletRequestWrapper requestWrapper = new S2HttpServletRequestWrapper(
                (HttpServletRequest) request, ssm);
        S2HttpServletResponseWrapper responseWrapper = new S2HttpServletResponseWrapper(
                (HttpServletResponse) response, requestWrapper,
                sessionStateManager);
        SessionIdUtil.writeCookie(requestWrapper, responseWrapper,
                requestWrapper.getSessionId());
        try {
            chain.doFilter(requestWrapper, responseWrapper);
        } finally {
            S2HttpSession session = requestWrapper.getS2HttpSession();
            if (session != null) {
                SessionState sessionState = session.getSessionState();
                if (sessionState != null) {
                    ssm.updateState(session.getId(), sessionState);
                }
            }
        }
    }

    protected SessionStateManager getSessionStateManager() {
        if (sessionStateManager == null) {
            S2Container container = SingletonS2ContainerFactory.getContainer();
            sessionStateManager = (SessionStateManager) container
                    .getComponent(SessionStateManager.class);
        }
        return sessionStateManager;
    }
}