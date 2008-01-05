/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
package org.seasar.extension.dxo.meta.impl;

import java.util.HashMap;
import java.util.Map;

import org.seasar.extension.dxo.builder.DxoCommandBuilder;
import org.seasar.extension.dxo.meta.DxoMetadata;
import org.seasar.extension.dxo.meta.DxoMetadataFactory;
import org.seasar.framework.util.Disposable;
import org.seasar.framework.util.DisposableUtil;

/**
 * Dxoのメタデータを生成するファクトリの実装クラスです。
 * 
 * @author koichik
 */
public class DxoMetadataFactoryImpl implements DxoMetadataFactory, Disposable {

    /** インスタンスが初期化済みの状態なら<code>true</code>です。 */
    protected boolean initialized;

    /** コマンドを構築するビルダの配列です。 */
    protected DxoCommandBuilder[] builders;

    /** メタデータを保持するキャッシュです。 */
    protected final Map metadataCache = new HashMap();

    /**
     * インスタンスを構築します。
     */
    public DxoMetadataFactoryImpl() {
        initialize();
    }

    /**
     * コマンドを構築するビルダの配列を設定します。
     * 
     * @param builders
     *            コマンドを構築するビルダの配列
     */
    public void setBuilders(final DxoCommandBuilder[] builders) {
        this.builders = builders;
    }

    /**
     * インスタンスを初期化します。
     */
    public void initialize() {
        if (initialized) {
            return;
        }
        DisposableUtil.add(this);
        initialized = true;
    }

    public void dispose() {
        metadataCache.clear();
        initialized = false;
    }

    public DxoMetadata getMetadata(final Class dxoClass) {
        initialize();
        final DxoMetadata metadata = (DxoMetadata) metadataCache.get(dxoClass);
        if (metadata != null) {
            return metadata;
        }
        return createMetadata(dxoClass);
    }

    /**
     * Dxoの型に応じたメタデータを作成して返します。
     * 
     * @param dxoClass
     *            Dxoの型
     * @return Dxoの型に応じたメタデータ
     */
    protected DxoMetadata createMetadata(final Class dxoClass) {
        final DxoMetadata metadata = new DxoMetadataImpl(dxoClass, builders);
        metadataCache.put(dxoClass, metadata);
        return metadata;
    }

}
