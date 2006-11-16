/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
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
package org.seasar.framework.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.seasar.framework.exception.IORuntimeException;
import org.seasar.framework.exception.ResourceNotFoundRuntimeException;

public final class ResourceUtil {

    private ResourceUtil() {
    }

    public static String getResourcePath(String path, String extension) {
        if (extension == null) {
            return path;
        }
        extension = "." + extension;
        if (path.endsWith(extension)) {
            return path;
        }
        return path.replace('.', '/') + extension;
    }

    public static String getResourcePath(Class clazz) {
        return clazz.getName().replace('.', '/') + ".class";
    }

    public static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    public static URL getResource(String path) {
        return getResource(path, null);
    }

    public static URL getResource(String path, String extension)
            throws ResourceNotFoundRuntimeException {

        URL url = getResourceNoException(path, extension);
        if (url != null) {
            return url;
        }
        throw new ResourceNotFoundRuntimeException(getResourcePath(path,
                extension));
    }

    public static URL getResourceNoException(String path) {
        return getResourceNoException(path, null);
    }

    public static URL getResourceNoException(String path, String extension) {
        path = getResourcePath(path, extension);
        return getClassLoader().getResource(path);
    }

    public static InputStream getResourceAsStream(String path) {
        return getResourceAsStream(path, null);
    }

    public static InputStream getResourceAsStream(String path, String extension) {
        URL url = getResource(path, extension);
        return URLUtil.openStream(url);
    }

    public static InputStream getResourceAsStreamNoException(String path) {
        return getResourceAsStreamNoException(path, null);
    }

    public static InputStream getResourceAsStreamNoException(String path,
            String extension) {
        URL url = getResourceNoException(path, extension);
        if (url == null) {
            return null;
        }
        try {
            return url.openStream();
        } catch (final IOException e) {
            return null;
        }
    }

    public static boolean isExist(String path) {
        return getResourceNoException(path) != null;
    }

    public static Properties getProperties(String path) {
        Properties props = new Properties();
        InputStream is = getResourceAsStream(path);
        try {
            props.load(is);
            return props;
        } catch (IOException ex) {
            throw new IORuntimeException(ex);
        }
    }

    public static String getExtension(String path) {
        int extPos = path.lastIndexOf(".");
        if (extPos >= 0) {
            return path.substring(extPos + 1);
        }
        return null;
    }

    public static String removeExtension(String path) {
        int extPos = path.lastIndexOf(".");
        if (extPos >= 0) {
            return path.substring(0, extPos);
        }
        return path;
    }

    public static File getBuildDir(Class clazz) {
        return getBuildDir(getResourcePath(clazz));
    }

    public static File getBuildDir(String path) {
        File dir = null;
        URL url = getResource(path);
        if ("file".equals(url.getProtocol())) {
            int num = path.split("/").length;
            dir = new File(getFileName(url));
            for (int i = 0; i < num; ++i, dir = dir.getParentFile()) {
            }
        } else {
            dir = new File(JarFileUtil.toJarFilePath(url));
        }
        return dir;
    }

    public static String toExternalForm(URL url) {
        String s = url.toExternalForm();
        return URLUtil.decode(s, "UTF8");
    }

    public static String getFileName(URL url) {
        String s = url.getFile();
        return URLUtil.decode(s, "UTF8");
    }

    public static File getFile(URL url) {
        File file = new File(getFileName(url));
        if (file != null && file.exists()) {
            return file;
        }
        return null;
    }

    public static File getResourceAsFile(String path) {
        return getResourceAsFile(path, null);
    }

    public static File getResourceAsFile(String path, String extension) {
        return getFile(getResource(path, extension));
    }

    public static File getResourceAsFileNoException(Class clazz) {
        return getResourceAsFileNoException(getResourcePath(clazz));
    }

    public static File getResourceAsFileNoException(String path) {
        URL url = getResourceNoException(path);
        if (url == null) {
            return null;
        }
        return getFile(url);
    }

    public static String convertPath(String path, Class clazz) {
        if (isExist(path)) {
            return path;
        }
        String prefix = clazz.getName().replace('.', '/').replaceFirst(
                "/[^/]+$", "");
        return prefix + "/" + path;
    }
}