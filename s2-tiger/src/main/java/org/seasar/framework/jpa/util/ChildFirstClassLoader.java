/*
 * Copyright 2004-2012 the Seasar Foundation and the Others.
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

import java.util.List;
import java.util.Set;

import org.seasar.framework.util.tiger.CollectionsUtil;

/**
 * 未ロードのクラスを親クラスローダに委譲せずに自身でロードするクラスローダです。
 * 
 * @author taedium
 */
public class ChildFirstClassLoader extends AbstractClassLoader {

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
        super(parent, includedNames);
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

    @Override
    protected Class<?> doDefineClass(String className, byte[] bytes) {
        final Class<?> definedClass = defineClass(className, bytes, 0,
                bytes.length);
        fireClassDefinedEvent(className, bytes, definedClass);
        return definedClass;
    }

}
