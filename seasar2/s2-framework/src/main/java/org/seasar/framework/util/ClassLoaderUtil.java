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

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Iterator;

import org.seasar.framework.exception.ClassNotFoundRuntimeException;
import org.seasar.framework.exception.IORuntimeException;
import org.seasar.framework.message.MessageFormatter;

/**
 * {@link ClassLoader}を扱うためのユーティリティ・クラスです。
 * 
 * @author koichik
 */
public abstract class ClassLoaderUtil {

    private static final Method findLoadedClassMethod = getFindLoadedClassMethod();

    private static final Method defineClassMethod = getDefineClassMethod();

    private ClassLoaderUtil() {
    }

    private static Method getFindLoadedClassMethod() {
        final Method method = ClassUtil.getDeclaredMethod(ClassLoader.class,
                "findLoadedClass", new Class[] { String.class });
        method.setAccessible(true);
        return method;
    }

    private static Method getDefineClassMethod() {
        final Method method = ClassUtil.getDeclaredMethod(ClassLoader.class,
                "defineClass", new Class[] { String.class, byte[].class,
                        int.class, int.class });
        method.setAccessible(true);
        return method;
    }

    /**
     * クラスローダを返します。
     * <p>
     * クラスローダは以下の順で検索します。
     * </p>
     * <ol>
     * <li>呼び出されたスレッドにコンテキスト・クラスローダが設定されている場合はそのコンテキスト・クラスローダ</li>
     * <li>ターゲット・クラスをロードしたクラスローダを取得できればそのクラスローダ</li>
     * <li>このクラスをロードしたクラスローダを取得できればそのクラスローダ</li>
     * <li>システムを取得できればそのクラスローダ</li>
     * </ol>
     * <p>
     * ただし、ターゲット・クラスをロードしたクラスローダとこのクラスをロードしたクラスローダの両方が取得できた場合で、
     * ターゲット・クラスをロードしたクラスローダがこのクラスをロードしたクラスローダの祖先であった場合は、
     * このクラスをロードしたクラスローダを返します。
     * </p>
     * 
     * @param targetClass
     *            ターゲット・クラス
     * @return クラスローダ
     * @throws IllegalStateException
     *             クラスローダを取得できなかった場合
     */
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

    /**
     * コンテキストクラスローダから指定された名前を持つすべてのリソースを探します。
     * 
     * @param name
     *            リソース名
     * @return リソースに対する URL
     *         オブジェクトの列挙。リソースが見つからなかった場合、列挙は空になる。クラスローダがアクセスを持たないリソースは列挙に入らない
     * @see java.lang.ClassLoader#getResources(String)
     */
    public static Iterator getResources(final String name) {
        return getResources(Thread.currentThread().getContextClassLoader(),
                name);
    }

    /**
     * {@link getClassLoader(Class)}が返すクラスローダから指定された名前を持つすべてのリソースを探します。
     * 
     * @param targetClass
     *            ターゲット・クラス
     * @param name
     *            リソース名
     * @return リソースに対する URL
     *         オブジェクトの列挙。リソースが見つからなかった場合、列挙は空になる。クラスローダがアクセスを持たないリソースは列挙に入らない
     * @see java.lang.ClassLoader#getResources(String)
     */
    public static Iterator getResources(final Class targetClass,
            final String name) {
        return getResources(getClassLoader(targetClass), name);
    }

    /**
     * 指定のクラスローダから指定された名前を持つすべてのリソースを探します。
     * 
     * @param loader
     *            クラスローダ
     * @param name
     *            リソース名
     * @return リソースに対する URL
     *         オブジェクトの列挙。リソースが見つからなかった場合、列挙は空になる。クラスローダがアクセスを持たないリソースは列挙に入らない
     * @see java.lang.ClassLoader#getResources(String)
     */
    public static Iterator getResources(final ClassLoader loader,
            final String name) {
        try {
            final Enumeration e = loader.getResources(name);
            return new EnumerationIterator(e);
        } catch (final IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * クラスローダ<code>other</code>がクラスローダ<code>cl</code>の祖先なら<code>true</code>を返します。
     * 
     * @param cl
     *            クラスローダ
     * @param other
     *            クラスローダ
     * @return クラスローダ<code>other</code>がクラスローダ<code>cl</code>の祖先なら<code>true</code>
     */
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
     * 指定のクラスローダまたはその祖先の暮らすローダが、 このバイナリ名を持つクラスの起動ローダとしてJava仮想マシンにより記録されていた場合は、
     * 指定されたバイナリ名を持つクラスを返します。 記録されていなかった場合は<code>null</code>を返します。
     * 
     * @param classLoader
     *            クラスローダ
     * @param className
     *            クラスのバイナリ名
     * @return <code>Class</code>オブジェクト。クラスがロードされていない場合は<code>null</code>
     * @see java.lang.ClassLoader#findLoadedClass(String)
     */
    public static Class findLoadedClass(final ClassLoader classLoader,
            final String className) {
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

    /**
     * バイトの配列を<code>Class</code>クラスのインスタンスに変換します。
     * 
     * @param classLoader
     *            バイナリデータから<code>Class</code>クラスのインスタンスに変換するクラスローダ
     * @param className
     *            クラスのバイナリ名
     * @param bytes
     *            クラスデータを構成するバイト列
     * @param offset
     *            クラスデータ<code>bytes</code>の開始オフセット
     * @param length
     *            クラスデータの長さ
     * @return 指定されたクラスデータから作成された<code>Class</code>オブジェクト
     * @see java.lang.ClassLoader#defineClass(String, byte[], int, int)
     */
    public static Class defineClass(final ClassLoader classLoader,
            final String className, final byte[] bytes, final int offset,
            final int length) {
        return (Class) MethodUtil.invoke(defineClassMethod, classLoader,
                new Object[] { className, bytes, new Integer(offset),
                        new Integer(length) });
    }

    /**
     * 指定されたバイナリ名を持つクラスをロードします。
     * 
     * @param loader
     *            クラスローダ
     * @param className
     *            クラスのバイナリ名
     * @return 結果の<code>Class</code>オブジェクト
     * @throws ClassNotFoundRuntimeException
     *             クラスが見つからなかった場合
     * @see java.lang.ClassLoader#loadClass(String)
     */
    public static Class loadClass(final ClassLoader loader,
            final String className) {
        try {
            return loader.loadClass(className);
        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundRuntimeException(e);
        }
    }

}
