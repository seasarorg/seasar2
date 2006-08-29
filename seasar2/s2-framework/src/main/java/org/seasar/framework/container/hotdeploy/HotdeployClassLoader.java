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
package org.seasar.framework.container.hotdeploy;

import java.io.InputStream;

import org.seasar.framework.util.ClassLoaderUtil;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.InputStreamUtil;
import org.seasar.framework.util.ResourceUtil;

public class HotdeployClassLoader extends ClassLoader {

    private String[] rootPackageNames;

    public HotdeployClassLoader(ClassLoader classLoader) {
        super(classLoader);
    }

    public void setRootPackageNames(String[] rootPackageNames) {
        this.rootPackageNames = rootPackageNames;
    }

    public Class loadClass(String className, boolean resolve)
            throws ClassNotFoundException {

        if (isTargetClass(className)) {
            Class clazz = findLoadedClass(className);
            if (clazz != null) {
                return clazz;
            }
            clazz = ClassLoaderUtil.findLoadedClass(getParent(), className);
            if (clazz != null) {
                return clazz;
            }
            String path = ClassUtil.getResourcePath(className);
            InputStream is = ResourceUtil.getResourceAsStreamNoException(path);
            if (is != null) {
                clazz = defineClass(className, is);
                if (resolve) {
                    resolveClass(clazz);
                }
                return clazz;
            }
        }
        return super.loadClass(className, resolve);
    }

    protected Class defineClass(String className, InputStream classFile) {
        return defineClass(className, InputStreamUtil.getBytes(classFile));
    }

    protected Class defineClass(String className, byte[] bytes) {
        return defineClass(className, bytes, 0, bytes.length);
    }

    protected boolean isTargetClass(String className) {
        if (rootPackageNames == null) {
            return true;
        }
        for (int i = 0; i < rootPackageNames.length; ++i) {
            if (className.startsWith(rootPackageNames[i])) {
                return true;
            }
        }
        return false;
    }
}