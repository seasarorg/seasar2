/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

import org.seasar.framework.container.servlet.S2ContainerServlet;
import org.seasar.framework.util.UUID;

/**
 * セッション情報をS2で管理するためのHttpServletRequestWrapperです。
 * 
 * @author higa
 * 
 */
public class S2HttpServletRequestWrapper extends HttpServletRequestWrapper {

    private HttpServletRequest request;

    private SessionStateManager sessionStateManager;

    private S2HttpSession session;

    private String requestedSessionIdFromCookie;

    private String requestedSessionIdFromURL;

    private String createdSessionId;

    /**
     * <code>S2HttpServletRequestWrapper</code>のインスタンスを構築します。
     * 
     * @param request
     *            リクエスト
     * @param sessionStateManager
     *            セッション状態マネージャ
     */
    public S2HttpServletRequestWrapper(HttpServletRequest request,
            SessionStateManager sessionStateManager) {
        super(request);
        this.request = request;
        this.sessionStateManager = sessionStateManager;
        setupSessionId();
    }

    /**
     * セッション識別子をセットアップします。
     */
    protected void setupSessionId() {
        requestedSessionIdFromCookie = SessionIdUtil
                .getSessionIdFromCookie(request);
        if (requestedSessionIdFromCookie == null) {
            requestedSessionIdFromURL = SessionIdUtil
                    .getSessionIdFromURL(request);
        }
        if (requestedSessionIdFromURL == null) {
            createdSessionId = UUID.create();
        }
    }

    public HttpSession getSession() {
        return getSession(true);
    }

    public HttpSession getSession(boolean create) {
        if (session != null) {
            return session;
        }
        boolean isNew = false;
        String sessionId = getRequestedSessionId();
        if (sessionId == null) {
            if (!create) {
                return null;
            }
            sessionId = createdSessionId;
            isNew = true;
        }
        session = new S2HttpSession(sessionId, sessionStateManager,
                S2ContainerServlet.getInstance().getServletContext(), isNew);
        return session;
    }

    /**
     * Seasar2用のセッションを返します。
     * 
     * @return Seasar2用のセッション
     */
    public S2HttpSession getS2HttpSession() {
        return session;
    }

    public String getRequestedSessionId() {
        if (requestedSessionIdFromCookie != null) {
            return requestedSessionIdFromCookie;
        }
        return requestedSessionIdFromURL;
    }

    /**
     * 作成されたセッション識別子を返します。
     * 
     * @return 作成されたセッション識別子
     */
    public String getCreatedSessionId() {
        return createdSessionId;
    }

    /**
     * セッション識別子を返します。
     * 
     * @return セッション識別子
     */
    public String getSessionId() {
        String sessionId = getRequestedSessionId();
        if (sessionId == null) {
            sessionId = createdSessionId;
        }
        return sessionId;
    }

    public boolean isRequestedSessionIdFromCookie() {
        return requestedSessionIdFromCookie != null;
    }

    public boolean isRequestedSessionIdFromUrl() {
        return isRequestedSessionIdFromURL();
    }

    public boolean isRequestedSessionIdFromURL() {
        return requestedSessionIdFromURL != null;
    }
}
