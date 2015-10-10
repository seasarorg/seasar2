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
package org.seasar.framework.jpa.util;

import java.util.List;
import java.util.Set;

import javax.persistence.spi.ClassTransformer;
import javax.persistence.spi.PersistenceUnitInfo;

import org.seasar.framework.jpa.PersistenceClassTransformer;
import org.seasar.framework.util.tiger.CollectionsUtil;

/**
 * 未ロードのクラスをトランスフォームし、トランスフォーム後のクラスのクラスを適切なクラスローダーにロードするクラスローダです。
 * 
 * @author taedium
 */
public class TransformClassLoader extends AbstractClassLoader {

    /** 永続クラスのトランスフォーマ */
    protected PersistenceClassTransformer persistenceClassTransformer;

    /** 永続ユニット情報 */
    protected PersistenceUnitInfo persistenceUnitInfo;

    /** クラストランスフォーマのリスト */
    protected List<ClassTransformer> transformers = CollectionsUtil
            .newArrayList();

    /**
     * インスタンスを構築します。
     * <p>
     * コンテキストクラスローダからロードされるクラス及び、<code>java.</code>または<code>javax.</code>で始まるクラスを除いて
     * 親クラスローダに委譲せず、{@link PersistenceClassTransformer#transform(PersistenceUnitInfo, String, byte[])}
     * を呼び出してロードするように構成します。
     * </p>
     * 
     */
    public TransformClassLoader() {
        this(Thread.currentThread().getContextClassLoader());
    }

    /**
     * インスタンスを構築します。
     * <p>
     * ブートストラップクラスローダからロードされるクラス及び、<code>java.</code>または<code>javax.</code>で始まるクラスを除いて
     * 親クラスローダに委譲せず、{@link PersistenceClassTransformer#transform(PersistenceUnitInfo, String, byte[])}
     * を呼び出してロードするように構成します。
     * </p>
     * 
     * @param parent
     *            親クラスローダ
     */
    public TransformClassLoader(ClassLoader parent) {
        super(parent);
    }

    /**
     * インスタンスを構築します。
     * <p>
     * <code>includedNames</code>に含まれる名前のクラスのみ、 親クラスローダに委譲せず、
     * {@link PersistenceClassTransformer#transform(PersistenceUnitInfo, String, byte[])}を呼び出してロードするように構成します。
     * ただし、ブートストラップクラスローダからロードされるクラス及び、 <code>java.</code>または<code>javax.</code>で始まるクラスを除きます。
     * </p>
     * 
     * @param parent
     *            親クラスローダ
     * @param includedNames
     *            親より先にロードする対象となるクラス名のセット
     */
    public TransformClassLoader(final ClassLoader parent,
            final Set<String> includedNames) {
        super(parent, includedNames);
    }

    /**
     * 永続クラスのトランスフォーマを登録します。
     * 
     * @param persistenceClassTransformer
     *            永続クラスのトランスフォーマ
     */
    public void registerPersistenceClassTransformer(
            final PersistenceClassTransformer persistenceClassTransformer) {
        this.persistenceClassTransformer = persistenceClassTransformer;
    }

    /**
     * 永続ユニット情報を登録します。
     * 
     * @param persistenceUnitInfo
     *            永続ユニット情報
     */
    public void registerPersistenceUnitInfo(
            final PersistenceUnitInfo persistenceUnitInfo) {
        this.persistenceUnitInfo = persistenceUnitInfo;
    }

    /**
     * クラストランスフォーマを登録します。
     * 
     * @param transformer
     *            クラストランスフォーマ
     */
    public void addTransformer(final ClassTransformer transformer) {
        transformers.add(transformer);
    }

    @Override
    protected Class<?> doDefineClass(String className, byte[] bytes) {
        return persistenceClassTransformer.transform(persistenceUnitInfo,
                className, bytes);
    }

}
