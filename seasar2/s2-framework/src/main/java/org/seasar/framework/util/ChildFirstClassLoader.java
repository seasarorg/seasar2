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

    protected Class loadClass(final String name, final boolean resolve)
            throws ClassNotFoundException {
        if (isStystemClass(name)) {
            return super.loadClass(name, resolve);
        }
        Class clazz = findLoadedClass(name);
        if (clazz == null) {
            clazz = findClass(name);
        }
        if (resolve) {
            resolveClass(clazz);
        }
        return clazz;
    }

    protected boolean isStystemClass(final String name) {
        try {
            Class.forName(name, false, null);
        } catch (final ClassNotFoundException e) {
            return false;
        }
        return true;
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
