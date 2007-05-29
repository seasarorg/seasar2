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
package org.seasar.framework.mock.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import org.seasar.framework.util.EmptyEnumeration;
import org.seasar.framework.util.EnumerationAdapter;

/**
 * {@link MockHttpServletRequest}の実装クラスです。
 * 
 * @author higa
 * 
 */
public class MockHttpServletRequestImpl implements MockHttpServletRequest {

    private ServletContext servletContext;

    private String servletPath;

    private String authType;

    private List cookieList = new ArrayList();

    private Map headers = new HashMap();

    private String method = "POST";

    private String pathInfo;

    private String pathTranslated;

    private String queryString;

    private MockHttpSessionImpl session;

    private String scheme = "http";

    private int serverPort = 80;

    private String protocol = "HTTP/1.1";

    private String serverName = "localhost";

    private Map attributes = new HashMap();

    private String characterEncoding = "ISO-8859-1";

    private int contentLength;

    private String contentType;

    private Map parameters = new HashMap();

    private String remoteAddr;

    private String remoteHost;

    private int remotePort;

    private String localAddr;

    private String localName;

    private int localPort;

    private List locales = new ArrayList();

    /**
     * {@link MockHttpServletRequestImpl}を作成します。
     * 
     * @param servletContext
     * @param servletPath
     */
    public MockHttpServletRequestImpl(ServletContext servletContext,
            String servletPath) {
        this.servletContext = servletContext;
        if (servletPath.charAt(0) == '/') {
            this.servletPath = servletPath;
        } else {
            this.servletPath = "/" + servletPath;
        }
    }

    /**
     * @see javax.servlet.http.HttpServletRequest#getAuthType()
     */
    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    /**
     * @see javax.servlet.http.HttpServletRequest#getCookies()
     */
    public Cookie[] getCookies() {
        return (Cookie[]) cookieList.toArray(new Cookie[cookieList.size()]);
    }

    public void addCookie(Cookie cookie) {
        cookieList.add(cookie);
    }

    /**
     * @see javax.servlet.http.HttpServletRequest#getDateHeader(java.lang.String)
     */
    public long getDateHeader(String name) {
        String value = getHeader(name);
        return MockHeaderUtil.getDateValue(value);
    }

    /**
     * @see javax.servlet.http.HttpServletRequest#getHeader(java.lang.String)
     */
    public String getHeader(String name) {
        List values = getHeaderList(name);
        if (values != null) {
            return (String) values.get(0);
        }
        return null;
    }

    /**
     * @see javax.servlet.http.HttpServletRequest#getHeaders(java.lang.String)
     */
    public Enumeration getHeaders(String name) {
        List values = getHeaderList(name);
        if (values != null) {
            return new EnumerationAdapter(values.iterator());
        }
        return new EmptyEnumeration();
    }

    /**
     * @see javax.servlet.http.HttpServletRequest#getHeaderNames()
     */
    public Enumeration getHeaderNames() {
        return new EnumerationAdapter(headers.keySet().iterator());
    }

    /**
     * @see javax.servlet.http.HttpServletRequest#getIntHeader(java.lang.String)
     */
    public int getIntHeader(String name) {
        String value = getHeader(name);
        return MockHeaderUtil.getIntValue(value);
    }

    public void addHeader(String name, String value) {
        List values = getHeaderList(name);
        if (values == null) {
            values = new ArrayList();
        }
        values.add(value);
        headers.put(name.toLowerCase(), values);
    }

    public void addDateHeader(String name, long value) {
        addHeader(name, MockHeaderUtil.getDateValue(value));
    }

    public void addIntHeader(String name, int value) {
        addHeader(name, "" + value);
    }

    private List getHeaderList(String name) {
        name = name.toLowerCase();
        return (List) headers.get(name);
    }

    /**
     * @see javax.servlet.http.HttpServletRequest#getMethod()
     */
    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    /**
     * @see javax.servlet.http.HttpServletRequest#getPathInfo()
     */
    public String getPathInfo() {
        return pathInfo;
    }

    public void setPathInfo(String pathInfo) {
        this.pathInfo = pathInfo;
    }

    /**
     * @see javax.servlet.http.HttpServletRequest#getPathTranslated()
     */
    public String getPathTranslated() {
        return pathTranslated;
    }

    public void setPathTranslated(String pathTranslated) {
        this.pathTranslated = pathTranslated;
    }

    /**
     * @see javax.servlet.http.HttpServletRequest#getContextPath()
     */
    public String getContextPath() {
        return servletContext.getServletContextName();
    }

    /**
     * @see javax.servlet.http.HttpServletRequest#getQueryString()
     */
    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    /**
     * @see javax.servlet.http.HttpServletRequest#getRemoteUser()
     */
    public String getRemoteUser() {
        return System.getProperty("user.name");
    }

    /**
     * @see javax.servlet.http.HttpServletRequest#isUserInRole(java.lang.String)
     */
    public boolean isUserInRole(String arg0) {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.http.HttpServletRequest#getUserPrincipal()
     */
    public Principal getUserPrincipal() {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.http.HttpServletRequest#getRequestedSessionId()
     */
    public String getRequestedSessionId() {
        String sessionId = getRequestedSessionIdFromCookie();
        if (sessionId != null) {
            return sessionId;
        }
        return getRequestedSessionIdFromURL();
    }

    protected String getRequestedSessionIdFromCookie() {
        Cookie[] cookies = getCookies();
        if (cookies == null) {
            return null;
        }
        for (int i = 0; i < cookies.length; i++) {
            Cookie cookie = cookies[i];
            if (cookie.getName().endsWith("sessionid")) {
                return cookie.getValue();
            }
        }
        return null;
    }

    protected String getRequestedSessionIdFromURL() {
        String uri = getRequestURI();
        int index = uri.lastIndexOf("sessionid");
        if (index < 0) {
            return null;
        }
        return uri.substring(index + "sessionid".length());
    }

    /**
     * @see javax.servlet.http.HttpServletRequest#getRequestURI()
     */
    public String getRequestURI() {
        String contextPath = getContextPath();
        if (contextPath.equals("/")) {
            return servletPath;
        }
        return contextPath + servletPath;
    }

    /**
     * @see javax.servlet.http.HttpServletRequest#getRequestURL()
     */
    public StringBuffer getRequestURL() {
        StringBuffer url = new StringBuffer();
        url.append(scheme);
        url.append("://");
        url.append(serverName);
        if ((scheme.equals("http") && (serverPort != 80))
                || (scheme.equals("https") && (serverPort != 443))) {

            url.append(':');
            url.append(serverPort);
        }
        url.append(getRequestURI());
        return url;
    }

    /**
     * @see javax.servlet.http.HttpServletRequest#getServletPath()
     */
    public String getServletPath() {
        return servletPath;
    }

    /**
     * @see javax.servlet.http.HttpServletRequest#getSession(boolean)
     */
    public HttpSession getSession(boolean create) {
        if (session != null) {
            return session;
        }
        if (create) {
            session = new MockHttpSessionImpl(servletContext);
        }
        if (session != null) {
            session.access();
        }
        return session;
    }

    /**
     * @see javax.servlet.http.HttpServletRequest#getSession()
     */
    public HttpSession getSession() {
        return getSession(true);
    }

    /**
     * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdValid()
     */
    public boolean isRequestedSessionIdValid() {
        if (session != null) {
            return session.isValid();
        }
        return false;
    }

    /**
     * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromCookie()
     */
    public boolean isRequestedSessionIdFromCookie() {
        return getRequestedSessionIdFromCookie() != null;
    }

    /**
     * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromURL()
     */
    public boolean isRequestedSessionIdFromURL() {
        return getRequestedSessionIdFromURL() != null;
    }

    /**
     * @deprecated
     * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromUrl()
     */
    public boolean isRequestedSessionIdFromUrl() {
        return isRequestedSessionIdFromURL();
    }

    /**
     * @see javax.servlet.ServletRequest#getAttribute(java.lang.String)
     */
    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    /**
     * @see javax.servlet.ServletRequest#getAttributeNames()
     */
    public Enumeration getAttributeNames() {
        return new EnumerationAdapter(attributes.keySet().iterator());
    }

    /**
     * @see javax.servlet.ServletRequest#setAttribute(java.lang.String,
     *      java.lang.Object)
     */
    public void setAttribute(String name, Object value) {
        attributes.put(name, value);
    }

    /**
     * @see javax.servlet.ServletRequest#removeAttribute(java.lang.String)
     */
    public void removeAttribute(String name) {
        attributes.remove(name);
    }

    /**
     * @see javax.servlet.ServletRequest#getCharacterEncoding()
     */
    public String getCharacterEncoding() {
        return characterEncoding;
    }

    /**
     * @see javax.servlet.ServletRequest#setCharacterEncoding(java.lang.String)
     */
    public void setCharacterEncoding(String characterEncoding)
            throws UnsupportedEncodingException {

        this.characterEncoding = characterEncoding;
    }

    /**
     * @see javax.servlet.ServletRequest#getContentLength()
     */
    public int getContentLength() {
        return contentLength;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    /**
     * @see javax.servlet.ServletRequest#getContentType()
     */
    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     * @see javax.servlet.ServletRequest#getInputStream()
     */
    public ServletInputStream getInputStream() throws IOException {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletRequest#getParameter(java.lang.String)
     */
    public String getParameter(String name) {
        String[] values = (String[]) parameters.get(name);
        if (values == null || values.length == 0) {
            return null;
        }
        return values[0];
    }

    /**
     * @see javax.servlet.ServletRequest#getParameterNames()
     */
    public Enumeration getParameterNames() {
        return new EnumerationAdapter(parameters.keySet().iterator());
    }

    /**
     * 
     * @see javax.servlet.ServletRequest#getParameterValues(java.lang.String)
     */
    public String[] getParameterValues(String name) {
        return (String[]) parameters.get(name);
    }

    /**
     * 
     * @see javax.servlet.ServletRequest#getParameterMap()
     */
    public Map getParameterMap() {
        return parameters;
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

    /**
     * @see javax.servlet.ServletRequest#getProtocol()
     */
    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    /**
     * @see javax.servlet.ServletRequest#getScheme()
     */
    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    /**
     * @see javax.servlet.ServletRequest#getServerName()
     */
    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    /**
     * @see javax.servlet.ServletRequest#getServerPort()
     */
    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    /**
     * 
     * @see javax.servlet.ServletRequest#getReader()
     */
    public BufferedReader getReader() throws IOException {
        throw new UnsupportedOperationException();
    }

    /**
     * 
     * @see javax.servlet.ServletRequest#getRemoteAddr()
     */
    public String getRemoteAddr() {
        return remoteAddr;
    }

    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    /**
     * 
     * @see javax.servlet.ServletRequest#getRemoteHost()
     */
    public String getRemoteHost() {
        return remoteHost;
    }

    public void setRemoteHost(String remoteHost) {
        this.remoteHost = remoteHost;
    }

    /**
     * @see javax.servlet.ServletRequest#getLocalAddr()
     */
    public String getLocalAddr() {
        return localAddr;
    }

    public void setLocalAddr(String localAddr) {
        this.localAddr = localAddr;
    }

    /**
     * @see javax.servlet.ServletRequest#getLocalName()
     */
    public String getLocalName() {
        return localName;
    }

    public void setLocalName(String localName) {
        this.localName = localName;
    }

    /**
     * @see javax.servlet.ServletRequest#getLocalPort()
     */
    public int getLocalPort() {
        return localPort;
    }

    public void setLocalPort(int localPort) {
        this.localPort = localPort;
    }

    /**
     * @see javax.servlet.ServletRequest#getRemotePort()
     */
    public int getRemotePort() {
        return remotePort;
    }

    public void setRemotePort(int remotePort) {
        this.remotePort = remotePort;
    }

    /**
     * 
     * @see javax.servlet.ServletRequest#getLocale()
     */
    public Locale getLocale() {
        if (locales.isEmpty()) {
            return null;
        }
        return (Locale) locales.get(0);
    }

    public void setLocale(Locale locale) {
        locales.clear();
        locales.add(locale);
    }

    /**
     * 
     * @see javax.servlet.ServletRequest#getLocales()
     */
    public Enumeration getLocales() {
        return new EnumerationAdapter(locales.iterator());
    }

    /**
     * 
     * @see javax.servlet.ServletRequest#isSecure()
     */
    public boolean isSecure() {
        return false;
    }

    /**
     * 
     * @see javax.servlet.ServletRequest#getRequestDispatcher(java.lang.String)
     */
    public RequestDispatcher getRequestDispatcher(String path) {
        return new MockRequestDispatcherImpl();
    }

    /**
     * @deprecated
     * @see javax.servlet.ServletRequest#getRealPath(java.lang.String)
     */
    public String getRealPath(String path) {
        return path;
    }

}