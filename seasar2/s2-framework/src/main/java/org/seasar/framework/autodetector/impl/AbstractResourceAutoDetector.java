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
package org.seasar.framework.autodetector.impl;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

import org.seasar.framework.autodetector.ResourceAutoDetector;
import org.seasar.framework.exception.IORuntimeException;
import org.seasar.framework.util.FileUtil;
import org.seasar.framework.util.JarFileUtil;
import org.seasar.framework.util.ResourceTraversal;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.framework.util.StringUtil;
import org.seasar.framework.util.URLUtil;

/**
 * @author taedium
 * 
 */
public abstract class AbstractResourceAutoDetector implements
        ResourceAutoDetector {

    private Map strategies = new HashMap();

    private List targetDirPaths = new ArrayList();

    private List resourceNamePatterns = new ArrayList();

    private List ignoreResourceNamePatterns = new ArrayList();

    public AbstractResourceAutoDetector() {
        strategies.put("file", new FileSystemStrategy());
        strategies.put("jar", new JarFileStrategy());
        strategies.put("wsjar", new JarFileStrategy());
        strategies.put("zip", new ZipFileStrategy());
    }

    public void addStrategy(final String protocol, final Strategy strategy) {
        strategies.put(protocol, strategy);
    }

    public Strategy getStrategy(final String protocol) {
        return (Strategy) strategies.get(protocol);
    }

    public void addTargetDirPath(final String targetDirPath) {
        targetDirPaths.add(targetDirPath);
    }

    public int getTargetDirPathSize() {
        return targetDirPaths.size();
    }

    public String getTargetDirPath(final int index) {
        return (String) targetDirPaths.get(index);
    }

    public void addResourceNamePattern(final String resourceName) {
        resourceNamePatterns.add(Pattern.compile(resourceName));
    }

    public void addIgnoreResourceNamePattern(final String resourceName) {
        ignoreResourceNamePatterns.add(Pattern.compile(resourceName));
    }

    public Pattern getResourceNamePattern(final int index) {
        return (Pattern) resourceNamePatterns.get(index);
    }

    public int getResourceNamePatternSize() {
        return resourceNamePatterns.size();
    }

    public Pattern getIgnoreResourceNamePattern(final int index) {
        return (Pattern) ignoreResourceNamePatterns.get(index);
    }

    public int getIgnoreResourceNamePatternSize() {
        return ignoreResourceNamePatterns.size();
    }

    protected boolean isApplied(final String resourceName) {
        for (int i = 0; i < getResourceNamePatternSize(); i++) {
            final Pattern pattern = getResourceNamePattern(i);
            if (pattern.matcher(resourceName).matches()) {
                return true;
            }
        }
        return false;
    }

    protected boolean isIgnored(final String resourceName) {
        for (int i = 0; i < getIgnoreResourceNamePatternSize(); i++) {
            final Pattern pattern = getIgnoreResourceNamePattern(i);
            if (pattern.matcher(resourceName).matches()) {
                return true;
            }
        }
        return false;
    }

    protected interface Strategy {

        String getBaseName(String path, URL url);

        void detect(String path, URL url,
                ResourceTraversal.ResourceHandler handler);
    }

    protected class FileSystemStrategy implements Strategy {

        public String getBaseName(final String path, final URL url) {
            final File rootDir = getRootDir(path, url);
            try {
                return FileUtil.getCanonicalPath(rootDir);
            } catch (final IORuntimeException e) {
                return null;
            }
        }

        public void detect(final String path, final URL url,
                final ResourceTraversal.ResourceHandler handler) {

            final File rootDir = getRootDir(path, url);
            ResourceTraversal.forEach(rootDir, path, handler);
        }

        protected File getRootDir(final String path, final URL url) {
            File file = ResourceUtil.getFile(url);
            final String[] names = StringUtil.split(path, "/");
            for (int i = 0; i < names.length; ++i) {
                file = file.getParentFile();
            }
            return file;
        }
    }

    protected class JarFileStrategy implements Strategy {

        public String getBaseName(final String path, final URL url) {
            try {
                return createJarFile(url).getName();
            } catch (final IORuntimeException e) {
                return null;
            }
        }

        public void detect(final String path, final URL url,
                final ResourceTraversal.ResourceHandler handler) {

            final JarFile jarFile = createJarFile(url);
            ResourceTraversal.forEach(jarFile, handler);
        }

        protected JarFile createJarFile(final URL url) {
            final URL nestedUrl = URLUtil.create(url.getPath());
            String path = nestedUrl.getPath();
            int pos = path.lastIndexOf('!');
            String jarFileName = path.substring(0, pos);
            return JarFileUtil.create(new File(jarFileName));
        }
    }

    protected class ZipFileStrategy implements Strategy {

        public String getBaseName(final String path, final URL url) {
            try {
                return createJarFile(url).getName();
            } catch (final IORuntimeException e) {
                return null;
            }
        }

        public void detect(final String path, final URL url,
                final ResourceTraversal.ResourceHandler handler) {

            final JarFile jarFile = createJarFile(url);
            ResourceTraversal.forEach(jarFile, handler);
        }

        protected JarFile createJarFile(final URL url) {
            final String urlString = ResourceUtil.toExternalForm(url);
            final int pos = urlString.lastIndexOf('!');
            final String jarFileName = urlString
                    .substring("zip:".length(), pos);
            return JarFileUtil.create(new File(jarFileName));
        }
    }
}
