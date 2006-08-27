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

import org.seasar.framework.autodetector.ClassAutoDetector;
import org.seasar.framework.exception.IORuntimeException;
import org.seasar.framework.util.ClassTraversal;
import org.seasar.framework.util.FileUtil;
import org.seasar.framework.util.JarFileUtil;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.framework.util.StringUtil;
import org.seasar.framework.util.URLUtil;

/**
 * @author taedium
 * 
 */
public abstract class AbstractClassAutoDetector implements ClassAutoDetector {

    private Map strategies = new HashMap();

    private List targetPackageNames = new ArrayList();;

    public AbstractClassAutoDetector() {
        strategies.put("file", new FileSystemStrategy());
        strategies.put("jar", new JarFileStrategy());
        strategies.put("zip", new ZipFileStrategy());
    }

    public void addStrategy(final String protocol, final Strategy strategy) {
        strategies.put(protocol, strategy);
    }

    public void addTargetPackageName(final String targetPackageName) {
        targetPackageNames.add(targetPackageName);
    }

    public Strategy getStrategy(String protocol) {
        return (Strategy) strategies.get(protocol);
    }

    public int getTargetPackageNameSize() {
        return targetPackageNames.size();
    }

    public String getTargetPackageName(final int index) {
        return (String) targetPackageNames.get(index);
    }

    protected interface Strategy {

        String getBaseName(String packageName, URL url);

        void detect(String packageName, URL url,
                ClassTraversal.ClassHandler handler);
    }

    protected class FileSystemStrategy implements Strategy {

        public String getBaseName(final String packageName, final URL url) {
            final File rootDir = getRootDir(packageName, url);
            try {
                return FileUtil.getCanonicalPath(rootDir);
            } catch (IORuntimeException e) {
                return null;
            }
        }

        public void detect(final String packageName, final URL url,
                final ClassTraversal.ClassHandler handler) {

            final File rootDir = getRootDir(packageName, url);
            ClassTraversal.forEach(rootDir, packageName, handler);
        }

        protected File getRootDir(final String path, final URL url) {
            File file = ResourceUtil.getFile(url);
            final String[] names = StringUtil.split(path, ".");
            for (int i = 0; i < names.length; ++i) {
                file = file.getParentFile();
            }
            return file;
        }
    }

    protected class JarFileStrategy implements Strategy {

        public String getBaseName(final String packageName, final URL url) {
            try {
                return createJarFile(url).getName();
            } catch (final IORuntimeException e) {
                return null;
            }
        }

        public void detect(final String packageName, final URL url,
                final ClassTraversal.ClassHandler handler) {

            final JarFile jarFile = createJarFile(url);
            ClassTraversal.forEach(jarFile, handler);
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

        public String getBaseName(final String packageName, final URL url) {
            try {
                return createJarFile(url).getName();
            } catch (final IORuntimeException e) {
                return null;
            }
        }

        public void detect(final String packageName, final URL url,
                final ClassTraversal.ClassHandler handler) {

            final JarFile jarFile = createJarFile(url);
            ClassTraversal.forEach(jarFile, handler);
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
