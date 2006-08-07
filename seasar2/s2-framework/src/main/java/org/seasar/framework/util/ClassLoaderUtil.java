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

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.exception.ClassNotFoundRuntimeException;
import org.seasar.framework.message.MessageFormatter;

/**
 * @author koichik
 */
public class ClassLoaderUtil {
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

    protected static boolean isAncestor(ClassLoader cl, final ClassLoader other) {
        while (cl != null) {
            if (cl == other) {
                return true;
            }
            cl = cl.getParent();
        }
        return false;
    }

    /**
     * 以下のバグに対する対応です。 <br/>
     * http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4167874
     * 
     * @param loader
     */
    public static void workaroundBugId4167874(ClassLoader loader) {
        try {
            Class url = loader.loadClass("java.net.URL");
            BeanDesc urlBD = BeanDescFactory.getBeanDesc(url);
            Object urlInstance = urlBD.newInstance(new String[] { "http://a" });
            Object connection = urlBD.invoke(urlInstance, "openConnection",
                    null);
            BeanDesc connectionBD = BeanDescFactory.getBeanDesc(connection
                    .getClass());
            connectionBD.invoke(connection, "setDefaultUseCaches",
                    new Object[] { Boolean.FALSE });
        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundRuntimeException(e);
        }
    }
}
