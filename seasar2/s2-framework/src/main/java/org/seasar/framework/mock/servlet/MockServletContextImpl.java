/*
 * Copyright 2004-2005 the Seasar Foundation and the Others.
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

import org.seasar.framework.util.EnumerationAdapter;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.framework.util.StringUtil;

/**
 * @author higa
 *  
 */
public class MockServletContextImpl implements MockServletContext, Serializable {

    private static final long serialVersionUID = -5626752218858278823L;

    public static final int MAJOR_VERSION = 2;

    public static final int MINOR_VERSION = 4;

    public static final String SERVER_INFO = "seasar/2.0";

    private String path_;

    private Map mimeTypes_ = new HashMap();

    private Map initParameters_ = new HashMap();

    private Map attributes_ = new HashMap();

    public MockServletContextImpl(String path) {
        if (path == null) {
            path = "/";
        }
        if (path.charAt(0) == '/') {
            path_ = path;
        } else {
            path_ = "/" + path;
        }
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
        return (String) mimeTypes_.get(file);
    }

    public void addMimeType(String file, String type) {
        mimeTypes_.put(file, type);
    }

    /**
     * @see javax.servlet.ServletContext#getResourcePaths(java.lang.String)
     */
    public Set getResourcePaths(String path) {
        File src = ResourceUtil.getResourceAsFile(".");
        File root = src.getParentFile();
        if (root.getName().equalsIgnoreCase("WEB-INF")) {
            root = root.getParentFile();
        }
        File file = new File(root, adjustPath(path));
        if (!file.exists()) {
            String[] array = StringUtil.split(path, "/");
            if (array.length > 1) {
                path = array[array.length - 1];
            }
            do {
                file = new File(root, path);
                root = root.getParentFile();
            } while (!file.exists() && root != null);
        }
        if (file.isDirectory()) {
            Set paths = new HashSet();
            File[] files = file.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length; ++i) {
                    paths.add("file:/" + files[i].getAbsolutePath());
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
        path = adjustPath(path);
        if (ResourceUtil.isExist(path)) {
            return ResourceUtil.getResource(path);
        }
        return new URL(path);
    }

    /**
     * @see javax.servlet.ServletContext#getResourceAsStream(java.lang.String)
     */
    public InputStream getResourceAsStream(String path) {
        return ResourceUtil.getResourceAsStream(adjustPath(path));
    }

    protected String adjustPath(String path) {
        if (path != null && path.length() >= 0 && path.charAt(0) == '/') {
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
        return ResourceUtil.getResource(adjustPath(path)).getFile();
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
        return (String) initParameters_.get(name);
    }

    /**
     * @see javax.servlet.ServletContext#getInitParameterNames()
     */
    public Enumeration getInitParameterNames() {
        return new EnumerationAdapter(initParameters_.keySet().iterator());
    }

    public void setInitParameter(String name, String value) {
        initParameters_.put(name, value);
    }

    /**
     * @see javax.servlet.ServletContext#getAttribute(java.lang.String)
     */
    public Object getAttribute(String name) {
        return attributes_.get(name);
    }

    /**
     * @see javax.servlet.ServletContext#getAttributeNames()
     */
    public Enumeration getAttributeNames() {
        return new EnumerationAdapter(attributes_.keySet().iterator());
    }

    /**
     * @see javax.servlet.ServletContext#setAttribute(java.lang.String,
     *      java.lang.Object)
     */
    public void setAttribute(String name, Object value) {
        attributes_.put(name, value);
    }

    /**
     * @see javax.servlet.ServletContext#removeAttribute(java.lang.String)
     */
    public void removeAttribute(String name) {
        attributes_.remove(name);
    }

    /**
     * @see javax.servlet.ServletContext#getServletContextName()
     */
    public String getServletContextName() {
        return path_;
    }

    public MockHttpServletRequestImpl createRequest(String path) {
        String queryString = null;
        int question = path.indexOf('?');
        if (question >= 0) {
            queryString = path.substring(question + 1);
            path = path.substring(0, question);
        }
        MockHttpServletRequestImpl request = new MockHttpServletRequestImpl(this, path);
        request.setQueryString(queryString);
        return request;
    }

}
