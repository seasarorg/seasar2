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
package org.seasar.framework.util;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.LoaderClassPath;
import javassist.NotFoundException;

import org.seasar.framework.exception.NotFoundRuntimeException;

/**
 * ClassPool用のユーティリティクラスです。
 * 
 * @author koichik
 */
public class ClassPoolUtil {

    /**
     * ClassPoolのキャッシュです。
     */
    protected static final Map classPoolMap = Collections
            .synchronizedMap(new WeakHashMap());

    /** クラスが初期化済みであることを示します。 */
    protected static boolean initialized;

    /**
     * クラスを初期化します。
     */
    public static synchronized void initialize() {
        if (!initialized) {
            DisposableUtil.add(new Disposable() {
                public void dispose() {
                    synchronized (ClassPoolUtil.class) {
                        classPoolMap.clear();
                        initialized = false;
                    }
                }
            });
            initialized = true;
        }
    }

    /**
     * ClassPoolを返します。
     * 
     * @param targetClass
     * @return ClassPool
     */
    public static ClassPool getClassPool(final Class targetClass) {
        return getClassPool(ClassLoaderUtil.getClassLoader(targetClass));
    }

    /**
     * ClassPoolを返します。
     * 
     * @param classLoader
     * @return ClassPool
     */
    public static ClassPool getClassPool(final ClassLoader classLoader) {
        initialize();
        ClassPool classPool = (ClassPool) classPoolMap.get(classLoader);
        if (classPool == null) {
            if (classLoader == null) {
                return ClassPool.getDefault();
            }
            classPool = new ClassPool();
            classPool.appendClassPath(new LoaderClassPath(classLoader));
            classPoolMap.put(classLoader, classPool);
        }
        return classPool;
    }

    /**
     * CtClassに変換します。
     * 
     * @param classPool
     * @param clazz
     * @return CtClass
     */
    public static CtClass toCtClass(final ClassPool classPool, final Class clazz) {
        return toCtClass(classPool, ClassUtil.getSimpleClassName(clazz));
    }

    /**
     * CtClassに変換します。
     * 
     * @param classPool
     * @param className
     * @return CtClass
     */
    public static CtClass toCtClass(final ClassPool classPool,
            final String className) {
        try {
            return classPool.get(className);
        } catch (final NotFoundException e) {
            throw new NotFoundRuntimeException(e);
        }
    }

    /**
     * CtClassの配列に変換します。
     * 
     * @param classPool
     * @param classNames
     * @return CtClassの配列
     */
    public static CtClass[] toCtClassArray(final ClassPool classPool,
            final String[] classNames) {
        if (classNames == null) {
            return null;
        }
        final CtClass[] result = new CtClass[classNames.length];
        for (int i = 0; i < result.length; ++i) {
            result[i] = toCtClass(classPool, classNames[i]);
        }
        return result;
    }

    /**
     * CtClassの配列に変換します。
     * 
     * @param classPool
     * @param classes
     * @return CtClassの配列
     */
    public static CtClass[] toCtClassArray(final ClassPool classPool,
            final Class[] classes) {
        if (classes == null) {
            return null;
        }
        final CtClass[] result = new CtClass[classes.length];
        for (int i = 0; i < result.length; ++i) {
            result[i] = toCtClass(classPool, classes[i]);
        }
        return result;
    }

    /**
     * CtClassを作成します。
     * 
     * @param classPool
     * @param name
     * @return CtClass
     */
    public static CtClass createCtClass(final ClassPool classPool,
            final String name) {
        return createCtClass(classPool, name, Object.class);
    }

    /**
     * CtClassを作成します。
     * 
     * @param classPool
     * @param name
     * @param superClass
     * @return CtClass
     */
    public static CtClass createCtClass(final ClassPool classPool,
            final String name, final Class superClass) {
        return createCtClass(classPool, name, toCtClass(classPool, superClass));
    }

    /**
     * CtClassを作成します。
     * 
     * @param classPool
     * @param name
     * @param superClass
     * @return CtClass
     */
    public static CtClass createCtClass(final ClassPool classPool,
            final String name, final CtClass superClass) {
        return classPool.makeClass(name, superClass);
    }
}