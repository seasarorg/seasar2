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

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

/**
 * セッション情報をS2で管理するためのHttpSessionです。
 * 
 * @author higa
 * 
 */
public class S2HttpSession implements HttpSession {

    private String id;

    private SessionStateManager sessionStateManager;

    private ServletContext servletContext;

    private boolean isNew = false;

    private SessionState sessionState;

    private long creationTime = new Date().getTime();

    private long lastAccessedTime = creationTime;

    private int maxInactiveInterval = Integer.MAX_VALUE;

    /**
     * <code>S2HttpSession</code>を作成します。
     * 
     * @param id
     * @param sessionStateManager
     * @param servletContext
     * @param isNew
     */
    public S2HttpSession(String id, SessionStateManager sessionStateManager,
            ServletContext servletContext, boolean isNew) {
        this.id = id;
        this.sessionStateManager = sessionStateManager;
        this.isNew = isNew;
    }

    /**
     * SessionStateを返します。
     * 
     * @return
     */
    public SessionState getSessionState() {
        return sessionState;
    }

    public Object getAttribute(String name) {
        setupSessionState();
        return sessionState.getAttribute(name);
    }

    protected synchronized void setupSessionState() {
        if (sessionState == null) {
            sessionState = sessionStateManager.loadState(id);
        }
    }

    public Enumeration getAttributeNames() {
        setupSessionState();
        return sessionState.getAttributeNames();
    }

    public long getCreationTime() {
        return creationTime;
    }

    public String getId() {
        return id;
    }

    public long getLastAccessedTime() {
        return lastAccessedTime;
    }

    public int getMaxInactiveInterval() {
        return maxInactiveInterval;
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

    /**
     * @deprecated
     */
    public HttpSessionContext getSessionContext() {
        return null;
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
    }

    public boolean isNew() {
        return isNew;
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
        lastAccessedTime = new Date().getTime();
    }

    public void setMaxInactiveInterval(int interval) {
        maxInactiveInterval = interval;
    }
}