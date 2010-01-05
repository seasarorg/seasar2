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
package org.seasar.framework.mock.portlet;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.portlet.PortletContext;
import javax.portlet.PortletSession;

import org.seasar.framework.util.EnumerationAdapter;

/**
 * {@link MockPortletSession}の実装クラスです。
 * 
 * @author shinsuke
 * 
 */
public class MockPortletSessionImpl implements MockPortletSession {
    private PortletContext portletContext;

    private String id;

    private final long creationTime = System.currentTimeMillis();

    private long lastAccessedTime = creationTime;

    private int maxInactiveInterval = -1;

    private boolean valid = true;

    private boolean new_ = true;

    private Map portletAttributes = new HashMap();

    private Map applicationAttributes = new HashMap();

    /**
     * {@link MockPortletSessionImpl}を作成します。
     * 
     * @param portletContext
     */
    public MockPortletSessionImpl(PortletContext portletContext) {
        this.portletContext = portletContext;
        this.id = "id/" + hashCode();
    }

    public Object getAttribute(String name) {
        return portletAttributes.get(name);
    }

    public Object getAttribute(String name, int scope) {
        if (scope == PortletSession.APPLICATION_SCOPE) {
            return applicationAttributes.get(name);
        }
        return portletAttributes.get(name);
    }

    public Enumeration getAttributeNames() {
        return new EnumerationAdapter(portletAttributes.keySet().iterator());
    }

    public Enumeration getAttributeNames(int scope) {
        if (scope == PortletSession.APPLICATION_SCOPE) {
            return new EnumerationAdapter(applicationAttributes.keySet()
                    .iterator());
        }
        return new EnumerationAdapter(portletAttributes.keySet().iterator());
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

    public void invalidate() {
        if (!valid) {
            return;
        }
        portletAttributes.clear();
        applicationAttributes.clear();
        valid = false;
    }

    public boolean isNew() {
        return new_;
    }

    public void removeAttribute(String name) {
        portletAttributes.remove(name);
    }

    public void removeAttribute(String name, int scope) {
        if (scope == PortletSession.APPLICATION_SCOPE) {
            applicationAttributes.remove(name);
        }
        portletAttributes.remove(name);
    }

    public void setAttribute(String name, Object value) {
        portletAttributes.put(name, value);
    }

    public void setAttribute(String name, Object value, int scope) {
        if (scope == PortletSession.APPLICATION_SCOPE) {
            applicationAttributes.put(name, value);
        }
        portletAttributes.put(name, value);
    }

    public void setMaxInactiveInterval(int maxInactiveInterval) {
        this.maxInactiveInterval = maxInactiveInterval;
    }

    public PortletContext getPortletContext() {
        return portletContext;
    }

    public void access() {
        new_ = false;
        lastAccessedTime = System.currentTimeMillis();
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
}