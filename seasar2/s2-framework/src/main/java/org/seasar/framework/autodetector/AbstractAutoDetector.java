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
package org.seasar.framework.autodetector;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarFile;

import org.seasar.framework.traverser.Traverser;
import org.seasar.framework.util.JarFileUtil;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.framework.util.URLUtil;

/**
 * 
 * @author taedium
 */
public abstract class AbstractAutoDetector {

    public AbstractAutoDetector() {
        strategies.put("file", new FileSystemStrategy());
        strategies.put("jar", new JarFileStrategy());
        strategies.put("zip", new ZipFileStrategy());
    }

    private Map strategies = new HashMap();

    private List directoryNames = new ArrayList();

    private List referenceClasses = new ArrayList();

    public void addDirectoryName(String directoryName) {
        directoryNames.add(directoryName);
    }

    public void addReferenceClass(Class referenceClass) {
        referenceClasses.add(referenceClass);
    }

    public void addStrategy(final String protocol, final Strategy strategy) {
        strategies.put(protocol, strategy);
    }

    public String getDirectoryName(int index) {
        return (String) directoryNames.get(index);
    }

    public int getDirectoryNameSize() {
        return directoryNames.size();
    }

    public Class getReferenceClass(int index) {
        return (Class) referenceClasses.get(index);
    }

    public int getReferenceClassSize() {
        return referenceClasses.size();
    }

    public Strategy getStrategy(String protocol) {
        return (Strategy) strategies.get(protocol);
    }

    protected interface Strategy {

        void detect(Class referenceClass, URL url, Traverser traverser);
    }

    protected class FileSystemStrategy implements Strategy {

        public void detect(final Class referenceClass, final URL url,
                final Traverser traverser) {
            final File rootDir = getRootDir(referenceClass, url);
            for (int i = 0; i < getDirectoryNameSize(); i++) {
                final String directoryName = getDirectoryName(i);
                traverser.forEach(rootDir, directoryName);
            }
        }

        protected File getRootDir(final Class referenceClass, final URL url) {
            final String[] names = referenceClass.getName().split("\\.");
            File path = ResourceUtil.getFile(url);
            for (int i = 0; i < names.length; ++i) {
                path = path.getParentFile();
            }
            return path;
        }
    }

    protected class JarFileStrategy implements Strategy {

        public void detect(final Class referenceClass, final URL url,
                final Traverser traverser) {
            final JarFile jarFile = createJarFile(url);
            traverser.forEach(jarFile);
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

        public void detect(final Class referenceClass, final URL url,
                final Traverser traverser) {
            final JarFile jarFile = createJarFile(url);
            traverser.forEach(jarFile);
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
