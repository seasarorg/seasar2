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

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

/**
 * @author higa
 * 
 */
public class DbHttpSessionWrapper implements HttpSession {

    private String id;

    private HttpSession session;

    private DbSessionStateManager sessionStateManager;

    private DbSessionState sessionState;

    /**
     * @param id
     * @param session
     * @param sessionStateManager
     */
    public DbHttpSessionWrapper(String id, HttpSession session,
            DbSessionStateManager sessionStateManager) {
        this.id = id;
        this.session = session;
        this.sessionStateManager = sessionStateManager;
    }

    /**
     * DbSessionStateを返します。
     * 
     * @return
     */
    public DbSessionState getSessionState() {
        return sessionState;
    }

    public Object getAttribute(String name) {
        setupSessionState();
        return sessionState.getAttribute(name);
    }

    protected synchronized void setupSessionState() {
        if (sessionState == null) {
            sessionState = sessionStateManager.loadState(session.getId());
        }
    }

    public Enumeration getAttributeNames() {
        setupSessionState();
        return sessionState.getAttributeNames();
    }

    public long getCreationTime() {
        return session.getCreationTime();
    }

    public String getId() {
        return id;
    }

    public long getLastAccessedTime() {
        return session.getLastAccessedTime();
    }

    public int getMaxInactiveInterval() {
        return session.getMaxInactiveInterval();
    }

    public ServletContext getServletContext() {
        return session.getServletContext();
    }

    /**
     * @deprecated
     */
    public HttpSessionContext getSessionContext() {
        return session.getSessionContext();
    }

    public Object getValue(String name) {
        return getAttribute(name);
    }

    public String[] getValueNames() {
        List list = new ArrayList();
        for (Enumeration e = getAttributeNames(); e.hasMoreElements();) {
            list.add(e.nextElement());
        }
        return (String[]) list.toArray(new String[list.size()]);
    }

    public void invalidate() {
        session.invalidate();
    }

    public boolean isNew() {
        return session.isNew();
    }

    public void putValue(String name, Object value) {
        setAttribute(name, value);
    }

    public void removeAttribute(String name) {
        setAttribute(name, null);
    }

    public void removeValue(String name) {
        removeAttribute(name);
    }

    public void setAttribute(String name, Object value) {
        setupSessionState();
        sessionState.setAttribute(name, value);
    }

    public void setMaxInactiveInterval(int interval) {
        session.setMaxInactiveInterval(interval);
    }
}