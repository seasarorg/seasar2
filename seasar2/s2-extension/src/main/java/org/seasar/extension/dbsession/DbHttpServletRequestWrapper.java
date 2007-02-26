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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

/**
 * getSession()メソッドでDbHttpSessionWrapperを返すクラスです。
 * 
 * @author higa
 * 
 */
public class DbHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private HttpServletRequest request;

    private DbSessionStateManager sessionStateManager;

    private DbHttpSessionWrapper sessionWrapper;

    /**
     * @param request
     * @param sessionStateManager
     */
    public DbHttpServletRequestWrapper(HttpServletRequest request,
            DbSessionStateManager sessionStateManager) {
        super(request);
        this.request = request;
        this.sessionStateManager = sessionStateManager;
    }

    public HttpSession getSession() {
        return getSession(true);
    }

    public HttpSession getSession(boolean create) {
        if (sessionWrapper != null) {
            return sessionWrapper;
        }
        HttpSession session = request.getSession(create);
        if (session == null) {
            return null;
        }
        sessionWrapper = new DbHttpSessionWrapper(session, sessionStateManager);
        return sessionWrapper;
    }

    /**
     * DbHttpSessionWrapperを返します。
     * 
     * @return
     */
    public DbHttpSessionWrapper getSessionWrapper() {
        return sessionWrapper;
    }
}
