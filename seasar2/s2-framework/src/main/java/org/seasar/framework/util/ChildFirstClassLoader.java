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
package org.seasar.framework.util;

import java.io.InputStream;

/**
 * @author taedium
 * 
 */
public class ChildFirstClassLoader extends ClassLoader {

    public ChildFirstClassLoader(final ClassLoader parent) {
        super(parent);
    }

    protected Class loadClass(final String className, final boolean resolve)
            throws ClassNotFoundException {
        Class clazz = getSystemClass(className);
        if (clazz != null) {
            if (resolve) {
                resolveClass(clazz);
            }
            return clazz;
        }
        if (isIncludedClass(className)) {
            clazz = findLoadedClass(className);
            if (clazz != null) {
                return clazz;
            }
            clazz = ClassLoaderUtil.findLoadedClass(getParent(), className);
            if (clazz != null) {
                return clazz;
            }
            clazz = findClass(className);
            if (resolve) {
                resolveClass(clazz);
            }
            return clazz;
        }
        return super.loadClass(className, resolve);
    }

    protected Class getSystemClass(final String className) {
        try {
            return Class.forName(className, true, null);
        } catch (final ClassNotFoundException e) {
            return null;
        }
    }

    protected boolean isIncludedClass(final String className) {
        return !(className.startsWith("java.") || className
                .startsWith("javax."));
    }

    protected Class findClass(final String name) throws ClassNotFoundException {
        final String path = ClassUtil.getResourcePath(name);
        final InputStream in = getResourceAsStream(path);
        if (in == null) {
            throw new ClassNotFoundException(name);
        }
        final byte[] bytes = InputStreamUtil.getBytes(in);
        return defineClass(name, bytes, 0, bytes.length);
    }
}
