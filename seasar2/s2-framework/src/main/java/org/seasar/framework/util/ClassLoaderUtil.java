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

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Iterator;

import org.seasar.framework.exception.IORuntimeException;
import org.seasar.framework.message.MessageFormatter;

/**
 * @author koichik
 */
public class ClassLoaderUtil {

    private static Method findLoadedClassMethod;

    static {
        findLoadedClassMethod = ClassUtil.getDeclaredMethod(ClassLoader.class,
                "findLoadedClass", new Class[] { String.class });
        findLoadedClassMethod.setAccessible(true);
    }

    public static ClassLoader getClassLoader(final Class targetClass) {
        final ClassLoader contextClassLoader = Thread.currentThread()
                .getContextClassLoader();
        if (contextClassLoader != null) {
            return contextClassLoader;
        }

        final ClassLoader targetClassLoader = targetClass.getClassLoader();
        final ClassLoader thisClassLoader = ClassLoaderUtil.class
                .getClassLoader();
        if (targetClassLoader != null && thisClassLoader != null) {
            if (isAncestor(thisClassLoader, targetClassLoader)) {
                return thisClassLoader;
            }
            return targetClassLoader;
        }
        if (targetClassLoader != null) {
            return targetClassLoader;
        }
        if (thisClassLoader != null) {
            return thisClassLoader;
        }

        final ClassLoader systemClassLoader = ClassLoader
                .getSystemClassLoader();
        if (systemClassLoader != null) {
            return systemClassLoader;
        }

        throw new IllegalStateException(MessageFormatter.getMessage("ESSR0001",
                new Object[] { "ClassLoader" }));
    }

    public static Iterator getResources(final String name) {
        return getResources(Thread.currentThread().getContextClassLoader(),
                name);
    }

    public static Iterator getResources(final Class targetClass,
            final String name) {
        return getResources(getClassLoader(targetClass), name);
    }

    public static Iterator getResources(final ClassLoader loader,
            final String name) {
        try {
            final Enumeration e = loader.getResources(name);
            return new EnumerationIterator(e);
        } catch (final IOException e) {
            throw new IORuntimeException(e);
        }
    }

    protected static boolean isAncestor(ClassLoader cl, final ClassLoader other) {
        while (cl != null) {
            if (cl == other) {
                return true;
            }
            cl = cl.getParent();
        }
        return false;
    }

    public static Class findLoadedClass(final ClassLoader classLoader,
            final String className) throws ClassNotFoundException {
        for (ClassLoader loader = classLoader; loader != null; loader = loader
                .getParent()) {
            final Class clazz = (Class) MethodUtil.invoke(
                    findLoadedClassMethod, loader, new Object[] { className });
            if (clazz != null) {
                return clazz;
            }
        }
        return null;
    }
}
