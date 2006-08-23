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
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.InputStreamUtil;
import org.seasar.framework.util.MethodUtil;
import org.seasar.framework.util.ResourceUtil;

public class HotdeployClassLoader extends ClassLoader {

    private OndemandProject[] projects;

    private Method findLoadedClassMethod;

    private List listeners = new ArrayList();

    public HotdeployClassLoader(ClassLoader classLoader) {
        super(classLoader);
        findLoadedClassMethod = ClassUtil.getDeclaredMethod(ClassLoader.class,
                "findLoadedClass", new Class[] { String.class });
        findLoadedClassMethod.setAccessible(true);
    }

    public void setProjects(OndemandProject[] projects) {
        this.projects = projects;
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

    public void removeHotdeployListener(HotdeployListener listener) {
        listeners.remove(listener);
    }

    public Class loadClass(String className, boolean resolve)
            throws ClassNotFoundException {

        if (isTargetClass(className)) {
            Class clazz = findLoadedClassInternal(className);
            if (clazz != null) {
                return clazz;
            }
            String path = ClassUtil.getResourcePath(className);
            InputStream is = ResourceUtil.getResourceAsStreamNoException(path);
            if (is != null) {
                clazz = defineClass(className, is);
                definedClass(clazz);
                if (resolve) {
                    resolveClass(clazz);
                }
                return clazz;
            }
        }
        return super.loadClass(className, resolve);
    }

    protected Class findLoadedClassInternal(String className) {
        for (ClassLoader loader = this; loader != null; loader = loader
                .getParent()) {
            Class clazz = invokeFindLoadedClass(loader, className);
            if (clazz != null) {
                return clazz;
            }
        }
        return null;
    }

    protected Class invokeFindLoadedClass(ClassLoader loader, String className) {
        return (Class) MethodUtil.invoke(findLoadedClassMethod, loader,
                new Object[] { className });
    }

    protected Class defineClass(String className, InputStream classFile) {
        return defineClass(className, InputStreamUtil.getBytes(classFile));
    }

    protected Class defineClass(String className, byte[] bytes) {
        return defineClass(className, bytes, 0, bytes.length);
    }

    protected boolean isTargetClass(String className) {
        if (projects == null) {
            return true;
        }
        for (int i = 0; i < projects.length; ++i) {
            OndemandProject project = projects[i];
            int m = project.matchClassName(className);
            if (m == OndemandProject.MATCH) {
                return true;
            } else if (m == OndemandProject.IGNORE) {
                return false;
            }
        }
        return false;
    }

    protected void definedClass(Class clazz) {
        final int listenerSize = getHotdeployListenerSize();
        for (int i = 0; i < listenerSize; ++i) {
            HotdeployListener listener = getHotdeployListener(i);
            listener.definedClass(clazz);
        }
    }
}