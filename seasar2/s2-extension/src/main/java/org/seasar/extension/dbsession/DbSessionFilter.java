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

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;

/**
 * セッションの状態をデータベースに格納するためのFilterです。
 * 
 * @author higa
 * 
 */
public class DbSessionFilter implements Filter {

    private DbSessionStateManager sessionStateManager;

    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        DbSessionStateManager ssm = getSessionStateManager();
        DbHttpServletRequestWrapper requestWrapper = new DbHttpServletRequestWrapper(
                (HttpServletRequest) request, ssm);
        try {
            chain.doFilter(requestWrapper, response);
        } finally {
            DbHttpSessionWrapper sessionWrapper = requestWrapper
                    .getSessionWrapper();
            if (sessionWrapper == null) {
                return;
            }
            DbSessionState sessionState = sessionWrapper.getSessionState();
            if (sessionState != null) {
                ssm.updateState(sessionState);
            }
        }
    }

    protected DbSessionStateManager getSessionStateManager() {
        if (sessionStateManager == null) {
            S2Container container = SingletonS2ContainerFactory.getContainer();
            sessionStateManager = (DbSessionStateManager) container
                    .getComponent(DbSessionStateManager.class);
        }
        return sessionStateManager;
    }
}