/*
 * Copyright 2004-2015 the Seasar Foundation and the Others.
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

import org.seasar.framework.convention.NamingConvention;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.ClassLoaderUtil;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.InputStreamUtil;
import org.seasar.framework.util.ResourceUtil;

/**
 * HOT deploy用の {@link ClassLoader}です。
 * 
 * @author higa
 */
public class HotdeployClassLoader extends ClassLoader {

    private static final Logger logger = Logger
            .getLogger(HotdeployClassLoader.class);

    private NamingConvention namingConvention;

    /**
     * {@link HotdeployClassLoader}を作成します。
     * 
     * @param classLoader
     * @param namingConvention
     */
    public HotdeployClassLoader(ClassLoader classLoader,
            NamingConvention namingConvention) {
        super(classLoader);
        this.namingConvention = namingConvention;
    }

    public Class loadClass(String className, boolean resolve)
            throws ClassNotFoundException {

        if (HotdeployUtil.REBUILDER_CLASS_NAME.equals(className)) {
            Class clazz = findLoadedClass(className);
            if (clazz != null) {
                return clazz;
            }
            return defineClass(className, resolve);
        }
        if (isTargetClass(className)) {
            Class clazz = findLoadedClass(className);
            if (clazz != null) {
                return clazz;
            }
            clazz = ClassLoaderUtil.findLoadedClass(getParent(), className);
            if (clazz != null) {
                logger.log("WSSR0015", new Object[] { className });
                return clazz;
            }
            clazz = defineClass(className, resolve);
            if (clazz != null) {
                return clazz;
            }
        }
        return super.loadClass(className, resolve);
    }

    /**
     * {@link Class}を定義します。
     * 
     * @param className
     * @return {@link Class}
     */
    private Class defineClass(String className, boolean resolve) {
        Class clazz;
        String path = ClassUtil.getResourcePath(className);
        InputStream is = ResourceUtil.getResourceAsStreamNoException(path);
        if (is != null) {
            clazz = defineClass(className, is);
            if (resolve) {
                resolveClass(clazz);
            }
            return clazz;
        }
        return null;
    }

    /**
     * {@link Class}を定義します。
     * 
     * @param className
     * @param classFile
     * @return {@link Class}
     */
    protected Class defineClass(String className, InputStream classFile) {
        return defineClass(className, InputStreamUtil.getBytes(classFile));
    }

    /**
     * {@link Class}を定義します。
     * 
     * @param className
     * @param bytes
     * @return {@link Class}
     */
    protected Class defineClass(String className, byte[] bytes) {
        return defineClass(className, bytes, 0, bytes.length);
    }

    /**
     * HOT deployの対象のクラスかどうか返します。
     * 
     * @param className
     * @return HOT deployの対象のクラスかどうか
     */
    protected boolean isTargetClass(String className) {
        return namingConvention.isHotdeployTargetClassName(className);
    }
}