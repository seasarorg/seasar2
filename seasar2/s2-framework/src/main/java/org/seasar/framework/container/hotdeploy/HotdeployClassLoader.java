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

import org.seasar.framework.util.FileUtil;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.framework.util.StringUtil;

public class HotdeployClassLoader extends ClassLoader {
    
    private String packageName;
    
    public HotdeployClassLoader(ClassLoader classLoader) {
        super(classLoader);
    }
    
    public String getPackageName() {
        return packageName;
    }
    
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Class loadClass(String className,
            boolean resolve) throws ClassNotFoundException {
        
        if (isTargetClass(className)) {
            Class clazz = findLoadedClass(className);
            if (clazz != null) {
                return clazz;
            }
            String path = StringUtil.replace(className, ".", "/") + ".class";
            File file = ResourceUtil.getResourceAsFileNoException(path);
            if (file != null) {
                clazz = defineClass(className, file);
                return resolveClassIfNecessary(clazz, resolve);
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
        return className.startsWith(packageName);
    }

    protected Class resolveClassIfNecessary(Class clazz, boolean resolve) {
        if (resolve) {
            resolveClass(clazz);
        }
        return clazz;
    }
}