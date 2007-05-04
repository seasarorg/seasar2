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
package org.seasar.framework.jpa.impl;

import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.persistence.spi.ClassTransformer;

import org.seasar.framework.exception.ClassNotFoundRuntimeException;
import org.seasar.framework.jpa.PersistenceClassTransformer;
import org.seasar.framework.jpa.util.ChildFirstClassLoader;
import org.seasar.framework.jpa.util.ClassLoaderEvent;
import org.seasar.framework.jpa.util.ClassLoaderListener;
import org.seasar.framework.jpa.util.ClassTransformerUtil;
import org.seasar.framework.util.ClassLoaderUtil;
import org.seasar.framework.util.JarFileUtil;
import org.seasar.framework.util.tiger.CollectionsUtil;
import org.seasar.framework.util.tiger.ReflectionUtil;

/**
 * 永続クラスをトランスフォームします。
 * 
 * @author taedium
 */
public class PersistenceClassTransformerImpl implements
        PersistenceClassTransformer {

    public void transformJarFiles(final List<ClassTransformer> transformers,
            final ClassLoader classLoader, final List<URL> jarFileUrls) {
        final List<String> classNames = CollectionsUtil.newArrayList();
        for (final URL jarFileUrl : jarFileUrls) {
            final JarFile jarFile = JarFileUtil.create(jarFileUrl.getPath());
            try {
                for (final Enumeration<JarEntry> e = jarFile.entries(); e
                        .hasMoreElements();) {
                    final JarEntry entry = e.nextElement();
                    final String entryName = entry.getName();
                    if (entry.isDirectory() || !entryName.endsWith(".class")) {
                        continue;
                    }
                    final String className = entryName.substring(0,
                            entryName.length() - 6).replace("/", ".");
                    classNames.add(className);
                }
            } finally {
                JarFileUtil.close(jarFile);
            }
        }
        transformClasses(transformers, classLoader, classNames);

    }

    public void transformClasses(final List<ClassTransformer> transformers,
            final ClassLoader classLoader, final List<String> classNames) {
        final ChildFirstClassLoader tempLoader = new ChildFirstClassLoader(
                classLoader);

        tempLoader.addClassLoaderListener(new ClassLoaderListener() {

            public void classFinded(final ClassLoaderEvent event) {
                final String className = event.getClassName();
                byte[] bytes = event.getBytecode();
                for (final ClassTransformer transformer : transformers) {
                    bytes = transform(transformer, classLoader, className,
                            bytes);
                }
                ClassLoaderUtil.defineClass(classLoader, className, bytes, 0,
                        bytes.length);
            }
        });

        for (final String className : classNames) {
            try {
                ReflectionUtil.forName(className, tempLoader);
            } catch (ClassNotFoundRuntimeException e) {
                ReflectionUtil.forName(className + ".package-info", tempLoader);
            }
        }
    }

    protected byte[] transform(final ClassTransformer transformer,
            final ClassLoader classLoader, final String className,
            final byte[] bytes) {
        final byte[] transformed = ClassTransformerUtil.transform(transformer,
                classLoader, className.replace(".", "/"), null, null, bytes);
        return transformed == null ? bytes : transformed;
    }

}
