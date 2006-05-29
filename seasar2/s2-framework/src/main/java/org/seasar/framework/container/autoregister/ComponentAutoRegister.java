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
package org.seasar.framework.container.autoregister;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarFile;

import org.seasar.framework.util.ClassTraversal;
import org.seasar.framework.util.JarFileUtil;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.framework.util.ClassTraversal.ClassHandler;

/**
 * 
 * @author koichik
 */
public class ComponentAutoRegister extends AbstractComponentAutoRegister
        implements ClassHandler {

    private List referenceClasses = new ArrayList();

    private Map strategies = new HashMap();

    public ComponentAutoRegister() {
        strategies.put("file", new FileSystemStrategy());
        strategies.put("jar", new JarFileStrategy());
    }

    public void addReferenceClass(final Class referenceClass) {
        referenceClasses.add(referenceClass);
    }

    public void registerAll() {
        for (int i = 0; i < referenceClasses.size(); ++i) {
            final Class referenceClass = (Class) referenceClasses.get(i);
            final String baseClassPath = ResourceUtil
                    .getResourcePath(referenceClass);
            final URL url = ResourceUtil.getResource(baseClassPath);
            final Strategy strategy = (Strategy) strategies.get(url
                    .getProtocol());
            strategy.registerAll(referenceClass, url);
        }
    }

    private interface Strategy {

        void registerAll(Class referenceClass, URL url);
    }

    private class FileSystemStrategy implements Strategy {

        public void registerAll(final Class referenceClass, final URL url) {
            final File rootDir = getRootDir(referenceClass, url);
            for (int i = 0; i < getClassPatternSize(); ++i) {
                ClassTraversal.forEach(rootDir, getClassPattern(i)
                        .getPackageName(), ComponentAutoRegister.this);
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

    private class JarFileStrategy implements Strategy {

        public void registerAll(final Class referenceClass, final URL url) {
            final JarFile jarFile = createJarFile(url);
            ClassTraversal.forEach(jarFile, ComponentAutoRegister.this);
        }

        private JarFile createJarFile(final URL url) {
            final String urlString = ResourceUtil.toExternalForm(url);
            final int pos = urlString.lastIndexOf('!');
            final String jarFileName = urlString.substring(9, pos);
            return JarFileUtil.create(new File(jarFileName));
        }
    }
}
