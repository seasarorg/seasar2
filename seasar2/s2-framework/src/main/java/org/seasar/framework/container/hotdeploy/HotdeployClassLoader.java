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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.FileUtil;
import org.seasar.framework.util.ResourceUtil;

public class HotdeployClassLoader extends ClassLoader {

    private String packageName;

    private List listeners = new ArrayList();

    public HotdeployClassLoader(ClassLoader classLoader) {
        super(classLoader);
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void addHotdeployListener(HotdeployListener listener) {
        listeners.add(listener);
    }

    public HotdeployListener getHotdeployListener(int index) {
        return (HotdeployListener) listeners.get(index);
    }

    public int getHotdeployListenerSize() {
        return listeners.size();
    }

    public Class loadClass(String className, boolean resolve)
            throws ClassNotFoundException {

        if (isTargetClass(className)) {
            Class clazz = findLoadedClass(className);
            if (clazz != null) {
                return clazz;
            }
            String path = ClassUtil.getResourcePath(className);
            File file = ResourceUtil.getResourceAsFileNoException(path);
            if (file != null) {
                clazz = defineClass(className, file);
                definedClass(clazz);
                if (resolve) {
                    resolveClass(clazz);
                }
                return clazz;
            }
        }
        return super.loadClass(className, resolve);
    }

    protected Class defineClass(String className, File classFile) {
        return defineClass(className, FileUtil.getBytes(classFile));
    }

    protected Class defineClass(String className, byte[] bytes) {
        return defineClass(className, bytes, 0, bytes.length);
    }

    protected boolean isTargetClass(String className) {
        if (packageName == null) {
            return true;
        }
        return className.startsWith(packageName);
    }

    protected void definedClass(Class clazz) {
        for (int i = 0; i < getHotdeployListenerSize(); ++i) {
            HotdeployListener listener = getHotdeployListener(i);
            listener.definedClass(clazz);
        }
    }
}