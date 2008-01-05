/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.seasar.framework.exception.ResourceNotFoundRuntimeException;
import org.seasar.framework.util.EnumerationAdapter;
import org.seasar.framework.util.FileUtil;
import org.seasar.framework.util.ResourceUtil;

/**
 * {@link MockServletContext}の実装クラスです。
 * 
 * @author higa
 * @author manhole
 */
public class MockServletContextImpl implements MockServletContext, Serializable {

    private static final long serialVersionUID = -5626752218858278823L;

    /**
     * Major Version
     */
    public static final int MAJOR_VERSION = 2;

    /**
     * Minor Version
     */
    public static final int MINOR_VERSION = 4;

    /**
     * Server Info
     */
    public static final String SERVER_INFO = "seasar/2.0";

    private String servletContextName;

    private final Map mimeTypes = new HashMap();

    private final Map initParameters = new HashMap();

    private final Map attributes = new HashMap();

    /**
     * {@link MockServletContextImpl}を作成します。
     * 
     * @param path
     */
    public MockServletContextImpl(String path) {
        if (path == null || path.charAt(0) != '/') {
            path = "/";
        }
        this.servletContextName = path;
    }

    /**
     * @see javax.servlet.ServletContext#getContext(java.lang.String)
     */
    public ServletContext getContext(String path) {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletContext#getMajorVersion()
     */
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

    /**
     * @see javax.servlet.ServletContext#getMinorVersion()
     */
    public int getMinorVersion() {
        return MINOR_VERSION;
    }

    /**
     * @see javax.servlet.ServletContext#getMimeType(java.lang.String)
     */
    public String getMimeType(String file) {
        return (String) mimeTypes.get(file);
    }

    public void addMimeType(String file, String type) {
        mimeTypes.put(file, type);
    }

    /**
     * @see javax.servlet.ServletContext#getResourcePaths(java.lang.String)
     */
    public Set getResourcePaths(String path) {
        path = path.endsWith("/") ? path.substring(0, path.length() - 1) : path;
        File src = ResourceUtil.getResourceAsFile(".");
        File root = src.getParentFile();
        if (root.getName().equalsIgnoreCase("WEB-INF")) {
            root = root.getParentFile();
        }
        File file = new File(root, adjustPath(path));
        if (!file.exists()) {
            int pos = path.lastIndexOf('/');
            if (pos != -1) {
                path = path.substring(pos + 1);
            }
            do {
                file = new File(root, path);
                root = root.getParentFile();
            } while (!file.exists() && root != null);
            path = "/" + path;
        }
        if (file.isDirectory()) {
            int len = file.getAbsolutePath().length();
            Set paths = new HashSet();
            File[] files = file.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length; ++i) {
                    paths.add(path
                            + files[i].getAbsolutePath().substring(len)
                                    .replace('\\', '/'));
                }
                return paths;
            }
        }
        return null;
    }

    /**
     * @see javax.servlet.ServletContext#getResource(java.lang.String)
     */
    public URL getResource(String path) throws MalformedURLException {
        if (path == null) {
            return null;
        }
        path = adjustPath(path);
        File src = ResourceUtil.getResourceAsFile(".");
        File root = src.getParentFile();
        if (root.getName().equalsIgnoreCase("WEB-INF")) {
            root = root.getParentFile();
        }
        while (root != null) {
            File file = new File(root, path);
            if (file.exists()) {
                return FileUtil.toURL(file);
            }
            root = root.getParentFile();
        }
        if (ResourceUtil.isExist(path)) {
            return ResourceUtil.getResource(path);
        }
        if (path.startsWith("WEB-INF")) {
            path = path.substring("WEB-INF".length());
            return getResource(path);
        }
        return null;
    }

    /**
     * @see javax.servlet.ServletContext#getResourceAsStream(java.lang.String)
     */
    public InputStream getResourceAsStream(String path) {
        if (path == null) {
            return null;
        }
        path = adjustPath(path);
        if (ResourceUtil.isExist(path)) {
            return ResourceUtil.getResourceAsStream(path);
        }
        if (path.startsWith("WEB-INF")) {
            path = path.substring("WEB-INF".length());
            return getResourceAsStream(path);
        }
        return null;
    }

    /**
     * パスを調整します。
     * 
     * @param path
     *            パス
     * @return 調整後のパス
     */
    protected String adjustPath(String path) {
        if (path != null && path.length() > 0 && path.charAt(0) == '/') {
            return path.substring(1);
        }
        return path;
    }

    /**
     * @see javax.servlet.ServletContext#getRequestDispatcher(java.lang.String)
     */
    public RequestDispatcher getRequestDispatcher(String path) {
        return new MockRequestDispatcherImpl();
    }

    /**
     * @see javax.servlet.ServletContext#getNamedDispatcher(java.lang.String)
     */
    public RequestDispatcher getNamedDispatcher(String name) {
        throw new UnsupportedOperationException();
    }

    /**
     * @deprecated
     * @see javax.servlet.ServletContext#getServlet(java.lang.String)
     */
    public Servlet getServlet(String name) throws ServletException {
        throw new UnsupportedOperationException();
    }

    /**
     * @deprecated
     * @see javax.servlet.ServletContext#getServlets()
     */
    public Enumeration getServlets() {
        throw new UnsupportedOperationException();
    }

    /**
     * @deprecated
     * @see javax.servlet.ServletContext#getServletNames()
     */
    public Enumeration getServletNames() {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletContext#log(java.lang.String)
     */
    public void log(String message) {
        System.out.println(message);
    }

    /**
     * @deprecated
     * @see javax.servlet.ServletContext#log(java.lang.Exception,
     *      java.lang.String)
     */
    public void log(Exception ex, String message) {
        System.out.println(message);
        ex.printStackTrace();
    }

    /**
     * @see javax.servlet.ServletContext#log(java.lang.String,
     *      java.lang.Throwable)
     */
    public void log(String message, Throwable t) {
        System.out.println(message);
        t.printStackTrace();
    }

    /**
     * @see javax.servlet.ServletContext#getRealPath(java.lang.String)
     */
    public String getRealPath(String path) {
        // Servlet APIによると、リソースが無い場合はnullを返す。
        try {
            return ResourceUtil.getResource(adjustPath(path)).getFile();
        } catch (final ResourceNotFoundRuntimeException e) {
            return null;
        }
    }

    /**
     * @see javax.servlet.ServletContext#getServerInfo()
     */
    public String getServerInfo() {
        return SERVER_INFO;
    }

    /**
     * @see javax.servlet.ServletContext#getInitParameter(java.lang.String)
     */
    public String getInitParameter(String name) {
        return (String) initParameters.get(name);
    }

    /**
     * @see javax.servlet.ServletContext#getInitParameterNames()
     */
    public Enumeration getInitParameterNames() {
        return new EnumerationAdapter(initParameters.keySet().iterator());
    }

    public void setInitParameter(String name, String value) {
        initParameters.put(name, value);
    }

    /**
     * @see javax.servlet.ServletContext#getAttribute(java.lang.String)
     */
    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    /**
     * @see javax.servlet.ServletContext#getAttributeNames()
     */
    public Enumeration getAttributeNames() {
        return new EnumerationAdapter(attributes.keySet().iterator());
    }

    /**
     * @see javax.servlet.ServletContext#setAttribute(java.lang.String,
     *      java.lang.Object)
     */
    public void setAttribute(String name, Object value) {
        attributes.put(name, value);
    }

    /**
     * @see javax.servlet.ServletContext#removeAttribute(java.lang.String)
     */
    public void removeAttribute(String name) {
        attributes.remove(name);
    }

    /**
     * @see javax.servlet.ServletContext#getServletContextName()
     */
    public String getServletContextName() {
        return servletContextName;
    }

    public void setServletContextName(final String servletContextName) {
        this.servletContextName = servletContextName;
    }

    public MockHttpServletRequest createRequest(String path) {
        String queryString = null;
        int question = path.indexOf('?');
        if (question >= 0) {
            queryString = path.substring(question + 1);
            path = path.substring(0, question);
        }
        MockHttpServletRequestImpl request = new MockHttpServletRequestImpl(
                this, path);
        request.setQueryString(queryString);
        return request;
    }

    public Map getInitParameterMap() {
        return initParameters;
    }

}
