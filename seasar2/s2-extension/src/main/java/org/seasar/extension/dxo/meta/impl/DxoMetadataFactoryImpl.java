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
package org.seasar.extension.dxo.meta.impl;

import java.util.HashMap;
import java.util.Map;

import org.seasar.extension.dxo.builder.DxoCommandBuilder;
import org.seasar.extension.dxo.meta.DxoMetadata;
import org.seasar.extension.dxo.meta.DxoMetadataFactory;
import org.seasar.framework.util.Disposable;
import org.seasar.framework.util.DisposableUtil;

/**
 * @author koichik
 * 
 */
public class DxoMetadataFactoryImpl implements DxoMetadataFactory, Disposable {

    protected boolean initialized;

    protected DxoCommandBuilder[] builders;

    protected final Map metadataCache = new HashMap();

    public DxoMetadataFactoryImpl() {
        initialize();
    }

    public void setBuilders(final DxoCommandBuilder[] builders) {
        this.builders = builders;
    }

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

    protected DxoMetadata createMetadata(final Class dxoClass) {
        final DxoMetadata metadata = new DxoMetadataImpl(dxoClass, builders);
        metadataCache.put(dxoClass, metadata);
        return metadata;
    }
}
