/*
 * Copyright 2004-2013 the Seasar Foundation and the Others.
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

import java.security.Principal;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.portlet.PortalContext;
import javax.portlet.PortletContext;
import javax.portlet.PortletMode;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletSession;
import javax.portlet.WindowState;

import org.seasar.framework.util.EnumerationAdapter;

/**
 * {@link MockPortletRequest}の実装クラスです。
 * 
 * @author shinsuke
 * 
 */
public class MockPortletRequestImpl implements MockPortletRequest {

    private PortletContext portletContext;

    private String authType;

    private MockPortletSessionImpl session;

    private String scheme = "http";

    private String serverName = "localhost";

    private int serverPort = 80;

    private Map properties = new HashMap();

    private Map attributes = new HashMap();

    private Map parameters = new HashMap();

    private List locales = new ArrayList();

    private List contentTypes = new ArrayList();

    /**
     * {@link MockPortletRequestImpl}を作成します。
     * 
     * @param portletContext
     */
    public MockPortletRequestImpl(PortletContext portletContext) {
        this.portletContext = portletContext;
    }

    public boolean isWindowStateAllowed(WindowState arg0) {
        // TODO is MockWindowState needed?
        throw new UnsupportedOperationException();
    }

    public boolean isPortletModeAllowed(PortletMode arg0) {
        // TODO is MockPortletMode needed?
        throw new UnsupportedOperationException();
    }

    public PortletMode getPortletMode() {
        // TODO is MockPortletMode needed?
        throw new UnsupportedOperationException();
    }

    public WindowState getWindowState() {
        // TODO is MockWindowState needed?
        throw new UnsupportedOperationException();
    }

    public PortletPreferences getPreferences() {
        // TODO is MockPortletPreferences needed?
        throw new UnsupportedOperationException();
    }

    public PortletSession getPortletSession() {
        return getPortletSession(true);
    }

    public PortletSession getPortletSession(boolean create) {
        if (session != null) {
            return session;
        }
        if (create) {
            session = new MockPortletSessionImpl(portletContext);
        }
        if (session != null) {
            session.access();
        }
        return session;
    }

    public String getProperty(String name) {
        if (properties.get(name) instanceof String) {
            return (String) properties.get(name);
        } else if (properties.get(name) instanceof String[]) {
            String[] values = (String[]) parameters.get(name);
            if (values == null || values.length == 0) {
                return null;
            }
            return values[0];
        }
        return null;
    }

    public Enumeration getProperties(String name) {
        if (properties.get(name) instanceof String[]) {
            String[] values = (String[]) parameters.get(name);
            ArrayList array = new ArrayList();
            for (int i = 0; i < values.length; i++) {
                array.add(values[i]);
            }
            return new EnumerationAdapter(array.iterator());
        }
        return null;
    }

    public Enumeration getPropertyNames() {
        return new EnumerationAdapter(properties.keySet().iterator());
    }

    public PortalContext getPortalContext() {
        // TODO is MockPortalContext needed?
        throw new UnsupportedOperationException();
    }

    public String getAuthType() {
        return authType;
    }

    public String getContextPath() {
        return portletContext.getPortletContextName();
    }

    public String getRemoteUser() {
        return System.getProperty("user.name");
    }

    public Principal getUserPrincipal() {
        throw new UnsupportedOperationException();
    }

    public boolean isUserInRole(String arg0) {
        throw new UnsupportedOperationException();
    }

    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    public Enumeration getAttributeNames() {
        return new EnumerationAdapter(attributes.keySet().iterator());
    }

    public String getParameter(String name) {
        String[] values = (String[]) parameters.get(name);
        if (values == null || values.length == 0) {
            return null;
        }
        return values[0];
    }

    public Enumeration getParameterNames() {
        return new EnumerationAdapter(parameters.keySet().iterator());
    }

    public String[] getParameterValues(String name) {
        return (String[]) parameters.get(name);
    }

    public Map getParameterMap() {
        return parameters;
    }

    public boolean isSecure() {
        return false;
    }

    public void setAttribute(String name, Object value) {
        attributes.put(name, value);
    }

    public void removeAttribute(String name) {
        attributes.remove(name);
    }

    public String getRequestedSessionId() {
        if (session != null) {
            return session.getId();
        }
        return null;
    }

    public boolean isRequestedSessionIdValid() {
        if (session != null) {
            return session.isValid();
        }
        return false;
    }

    public String getResponseContentType() {
        return (String) contentTypes.get(0);
    }

    public Enumeration getResponseContentTypes() {
        return new EnumerationAdapter(contentTypes.iterator());
    }

    public Locale getLocale() {
        return (Locale) locales.get(0);
    }

    public Enumeration getLocales() {
        return new EnumerationAdapter(locales.iterator());
    }

    public String getScheme() {
        return scheme;
    }

    public String getServerName() {
        return serverName;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void addProperty(String name, Object value) {
        properties.put(name, value);
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public void addParameter(String name, String value) {
        String[] values = getParameterValues(name);
        if (values == null) {
            setParameter(name, value);
        } else {
            String[] newArray = new String[values.length + 1];
            System.arraycopy(values, 0, newArray, 0, values.length);
            newArray[newArray.length - 1] = value;
            parameters.put(name, newArray);
        }
    }

    public void addParameter(String name, String[] values) {
        if (values == null) {
            setParameter(name, (String) null);
            return;
        }
        String[] vals = getParameterValues(name);
        if (vals == null) {
            setParameter(name, values);
        } else {
            String[] newArray = new String[vals.length + values.length];
            System.arraycopy(vals, 0, newArray, 0, vals.length);
            System.arraycopy(values, 0, newArray, vals.length, values.length);
            parameters.put(name, newArray);
        }
    }

    public void setParameter(String name, String value) {
        parameters.put(name, new String[] { value });
    }

    public void setParameter(String name, String[] values) {
        parameters.put(name, values);
    }

    public void addResponseContentType(String contentType) {
        contentTypes.add(contentType);
    }

    public void removeResponseContentType(String contentType) {
        contentTypes.remove(contentType);
    }

    public void addLocale(Locale locale) {
        locales.add(locale);
    }

    public void removeLocale(Locale locale) {
        locales.remove(locale);
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }
}