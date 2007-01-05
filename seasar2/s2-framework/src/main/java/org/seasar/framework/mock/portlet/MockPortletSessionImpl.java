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
package org.seasar.framework.mock.portlet;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.portlet.PortletContext;
import javax.portlet.PortletSession;

import org.seasar.framework.util.EnumerationAdapter;

/**
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

    public MockPortletSessionImpl(PortletContext portletContext) {
        this.portletContext = portletContext;
        this.id = "id/" + hashCode();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.portlet.PortletSession#getAttribute(java.lang.String)
     */
    public Object getAttribute(String name) {
        return portletAttributes.get(name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.portlet.PortletSession#getAttribute(java.lang.String, int)
     */
    public Object getAttribute(String name, int scope) {
        if (scope == PortletSession.APPLICATION_SCOPE) {
            return applicationAttributes.get(name);
        }
        return portletAttributes.get(name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.portlet.PortletSession#getAttributeNames()
     */
    public Enumeration getAttributeNames() {
        return new EnumerationAdapter(portletAttributes.keySet().iterator());
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.portlet.PortletSession#getAttributeNames(int)
     */
    public Enumeration getAttributeNames(int scope) {
        if (scope == PortletSession.APPLICATION_SCOPE) {
            return new EnumerationAdapter(applicationAttributes.keySet()
                    .iterator());
        }
        return new EnumerationAdapter(portletAttributes.keySet().iterator());
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.portlet.PortletSession#getCreationTime()
     */
    public long getCreationTime() {
        return creationTime;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.portlet.PortletSession#getId()
     */
    public String getId() {
        return id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.portlet.PortletSession#getLastAccessedTime()
     */
    public long getLastAccessedTime() {
        return lastAccessedTime;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.portlet.PortletSession#getMaxInactiveInterval()
     */
    public int getMaxInactiveInterval() {
        return maxInactiveInterval;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.portlet.PortletSession#invalidate()
     */
    public void invalidate() {
        if (!valid) {
            return;
        }
        portletAttributes.clear();
        applicationAttributes.clear();
        valid = false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.portlet.PortletSession#isNew()
     */
    public boolean isNew() {
        return new_;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.portlet.PortletSession#removeAttribute(java.lang.String)
     */
    public void removeAttribute(String name) {
        portletAttributes.remove(name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.portlet.PortletSession#removeAttribute(java.lang.String, int)
     */
    public void removeAttribute(String name, int scope) {
        if (scope == PortletSession.APPLICATION_SCOPE) {
            applicationAttributes.remove(name);
        }
        portletAttributes.remove(name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.portlet.PortletSession#setAttribute(java.lang.String,
     *      java.lang.Object)
     */
    public void setAttribute(String name, Object value) {
        portletAttributes.put(name, value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.portlet.PortletSession#setAttribute(java.lang.String,
     *      java.lang.Object, int)
     */
    public void setAttribute(String name, Object value, int scope) {
        if (scope == PortletSession.APPLICATION_SCOPE) {
            applicationAttributes.put(name, value);
        }
        portletAttributes.put(name, value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.portlet.PortletSession#setMaxInactiveInterval(int)
     */
    public void setMaxInactiveInterval(int maxInactiveInterval) {
        this.maxInactiveInterval = maxInactiveInterval;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.portlet.PortletSession#getPortletContext()
     */
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
