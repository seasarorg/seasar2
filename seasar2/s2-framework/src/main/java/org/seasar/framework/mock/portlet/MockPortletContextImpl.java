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

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.portlet.PortletRequestDispatcher;

import org.seasar.framework.util.EnumerationAdapter;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.framework.util.StringUtil;

/**
 * {@link MockPortletContext}の実装クラスです。
 * 
 * @author shinsuke
 * 
 */
public class MockPortletContextImpl implements MockPortletContext {
    /**
     * MAJORバージョン
     */
    public static final int MAJOR_VERSION = 1;

    /**
     * MINORバージョン
     */
    public static final int MINOR_VERSION = 0;

    /**
     * SERVER情報
     */
    public static final String SERVER_INFO = "seasar/2.0";

    private String path;

    private Map attributes = new HashMap();

    private Map initParameters = new HashMap();

    private Map mimeTypes = new HashMap();

    /**
     * {@link MockPortletContextImpl}を作成します。
     * 
     * @param path
     */
    public MockPortletContextImpl(String path) {
        if (path == null || path.charAt(0) != '/') {
            path = "/";
        }
        this.path = path;
    }

    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    public Enumeration getAttributeNames() {
        return new EnumerationAdapter(attributes.keySet().iterator());
    }

    public String getInitParameter(String name) {
        return (String) initParameters.get(name);
    }

    public Enumeration getInitParameterNames() {
        return new EnumerationAdapter(initParameters.keySet().iterator());
    }

    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

    public String getMimeType(String file) {
        return (String) mimeTypes.get(file);
    }

    public int getMinorVersion() {
        return MINOR_VERSION;
    }

    public PortletRequestDispatcher getNamedDispatcher(String arg0) {
        return new MockPortletRequestDispatcherImpl();
    }

    public String getPortletContextName() {
        return path;
    }

    public String getRealPath(String path) {
        return ResourceUtil.getResource(adjustPath(path)).getFile();
    }

    public PortletRequestDispatcher getRequestDispatcher(String arg0) {
        return new MockPortletRequestDispatcherImpl();
    }

    public URL getResource(String path) throws MalformedURLException {
        path = adjustPath(path);
        if (ResourceUtil.isExist(path)) {
            return ResourceUtil.getResource(path);
        }
        return new URL(path);
    }

    public InputStream getResourceAsStream(String path) {
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

    public String getServerInfo() {
        return SERVER_INFO;
    }

    public void log(String message, Throwable ex) {
        System.out.println(message);
        ex.printStackTrace();
    }

    public void log(String message) {
        System.out.println(message);
    }

    public void removeAttribute(String name) {
        attributes.remove(name);
    }

    public void setAttribute(String name, Object value) {
        attributes.put(name, value);
    }

    public void setInitParameter(String name, String value) {
        initParameters.put(name, value);
    }

    public void addMimeType(String file, String type) {
        mimeTypes.put(file, type);
    }

    /**
     * パスを調整します。
     * 
     * @param path
     *            パス
     * @return 調整後のパス
     */
    protected String adjustPath(String path) {
        if (path != null && path.length() >= 0 && path.charAt(0) == '/') {
            return path.substring(1);
        }
        return path;
    }

    public MockPortletRequestImpl createRequest() {
        return new MockPortletRequestImpl(this);
    }
}
