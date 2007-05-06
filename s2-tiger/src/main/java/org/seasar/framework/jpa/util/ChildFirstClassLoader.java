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
package org.seasar.framework.jpa.util;

import java.io.InputStream;
import java.util.List;
import java.util.Set;

import org.seasar.framework.util.ClassLoaderUtil;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.InputStreamUtil;
import org.seasar.framework.util.tiger.CollectionsUtil;

/**
 * 未ロードのクラスを親クラスローダに委譲せずに自身でロードするクラスローダです。
 * 
 * @author taedium
 */
public class ChildFirstClassLoader extends ClassLoader {

    /** 親クラスローダに委譲せずに自身でロードする対象となるクラス名のセット */
    protected final Set<String> includedNames = CollectionsUtil.newHashSet();

    /** {@link ClassLoaderListener}のリスト */
    protected List<ClassLoaderListener> listeners = CollectionsUtil
            .newArrayList();

    /**
     * インスタンスを構築します。
     * <p>
     * コンテキストクラスローダからロードされるクラス及び、<code>java.</code>または<code>javax.</code>で始まるクラスを除いて
     * 親クラスローダに委譲せずに自身でロードするように構成します。
     * </p>
     * 
     */
    public ChildFirstClassLoader() {
        this(Thread.currentThread().getContextClassLoader());
    }

    /**
     * インスタンスを構築します。
     * <p>
     * ブートストラップクラスローダからロードされるクラス及び、<code>java.</code>または<code>javax.</code>で始まるクラスを除いて
     * 親クラスローダに委譲せずに自身でロードするように構成します。
     * </p>
     * 
     * @param parent
     *            親クラスローダ
     */
    public ChildFirstClassLoader(final ClassLoader parent) {
        super(parent);
    }

    /**
     * インスタンスを構築します。
     * <p>
     * <code>includedNames</code>に含まれる名前のクラスのみ、
     * 親クラスローダに委譲せずに自身でロードするように構成します。 ただし、ブートストラップクラスローダからロードされるクラス及び、
     * <code>java.</code>または<code>javax.</code>で始まるクラスを除きます。
     * </p>
     * 
     * @param parent
     *            親クラスローダ
     * @param includedNames
     *            親より先にロードする対象となるクラス名のセット
     */
    public ChildFirstClassLoader(final ClassLoader parent,
            final Set<String> includedNames) {
        super(parent);
        this.includedNames.addAll(includedNames);
    }

    /**
     * このクラスローダから{@link ClassLoaderEvent クラスローダイベント}を受け取るために{@link ClassLoaderListener クラスローダリスナ}を追加します。
     * 
     * @param listener
     *            リスナ
     */
    public void addClassLoaderListener(final ClassLoaderListener listener) {
        listeners.add(listener);
    }

    /**
     * このクラスローダから{@link ClassLoaderEvent クラスローダイベント}を受け取らないために{@link ClassLoaderListener クラスローダリスナ}を削除します。
     * 
     * @param listener
     *            リスナ
     */
    public void removeClassLoaderListener(final ClassLoaderListener listener) {
        listeners.remove(listener);
    }

    @Override
    protected Class<?> loadClass(final String className, final boolean resolve)
            throws ClassNotFoundException {
        Class<?> clazz = getSystemClass(className);
        if (clazz != null) {
            return resolveClass(resolve, clazz);
        }
        if (!isIncludedClass(className)) {
            return super.loadClass(className, resolve);
        }
        clazz = findLoadedClass(className);
        if (clazz != null) {
            return resolveClass(resolve, clazz);
        }
        clazz = ClassLoaderUtil.findLoadedClass(getParent(), className);
        if (clazz != null) {
            return resolveClass(resolve, clazz);
        }
        clazz = findClass(className);
        return resolveClass(resolve, clazz);
    }

    /**
     * クラス名で指定されたクラスをブートストラップクラスローダからロードできればそのクラスを返します。
     * 
     * @param className
     *            クラス名
     * @return ブートストラップクラスローダからロードしたクラス
     */
    protected Class<?> getSystemClass(final String className) {
        try {
            return Class.forName(className, true, null);
        } catch (final ClassNotFoundException e) {
            return null;
        }
    }

    /**
     * クラス名が親クラスローダに委譲せずに自身でロードする対象の場合は<code>true</code>を、 それ以外の場合は<code>false</code>を返します。
     * <p>
     * 親クラスローダに委譲せずに自身でロードする対象かどうかの判定は次の順で行われます。
     * </p>
     * <ol>
     * <li>クラス名が<code>java.</code>または<code>javax.</code>で始まるクラスは対象外</li>
     * <li>コンストラクタでロード対象クラス名のセットが与えられなかった場合は対象</li>
     * <li>コンストラクタでロード対象クラス名のセットが与えられた場合は、クラス名がセットに含まれていれば対象</li>
     * <li>それ以外の場合は対象外</li>
     * </ol>
     * 
     * @param className
     *            クラス名
     * @return クラス名が親クラスローダに委譲せずに自身でロードする対象の場合は<code>true</code>
     */
    protected boolean isIncludedClass(final String className) {
        if (className.startsWith("java.") || className.startsWith("javax.")) {
            return false;
        }
        if (className.endsWith("package-info")) {
            return false;
        }
        if (includedNames.isEmpty()) {
            return true;
        }
        return includedNames.contains(className);
    }

    /**
     * <code>resolve</code>が<code>true</code>であればクラスをリンクします。
     * 
     * @param resolve
     *            クラスをリンクする場合は<code>true</code>
     * @param clazz
     *            リンクするクラス
     * @return 結果の<code>Class</code>オブジェクト
     */
    protected Class<?> resolveClass(final boolean resolve, final Class<?> clazz) {
        if (resolve) {
            resolveClass(clazz);
        }
        return clazz;
    }

    @Override
    protected Class<?> findClass(final String className)
            throws ClassNotFoundException {
        final String path = ClassUtil.getResourcePath(className);
        final InputStream in = getResourceAsStream(path);
        if (in == null) {
            throw new ClassNotFoundException(className);
        }
        try {
            final byte[] bytes = InputStreamUtil.getBytes(in);
            final Class<?> definedClass = defineClass(className, bytes, 0,
                    bytes.length);
            fireClassDefinedEvent(className, bytes, definedClass);
            return definedClass;
        } catch (final Exception e) {
            throw new ClassNotFoundException(className, e);
        }
    }

    /**
     * クラスが定義されたことを通知します。
     * 
     * @param className
     *            見つかったクラス名
     * @param bytecode
     *            バイトコードが納められたバイト配列
     * @param definedClass
     *            定義されたクラス
     */
    protected void fireClassDefinedEvent(final String className,
            final byte[] bytecode, final Class<?> definedClass) {
        final ClassLoaderEvent event = new ClassLoaderEvent(this, className,
                bytecode, definedClass);
        for (final ClassLoaderListener listener : listeners) {
            try {
                listener.classFinded(event);
            } catch (final Exception ignore) {
            }
        }
    }

}
