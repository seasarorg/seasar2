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
package org.seasar.framework.unit.impl;

import java.util.List;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.factory.S2ContainerFactory;
import org.seasar.framework.log.Logger;
import org.seasar.framework.unit.ConfigFileIncluder;
import org.seasar.framework.unit.TestContext;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.framework.util.tiger.CollectionsUtil;

/**
 * コンポーネント定義の設定ファイルをS2コンテナにインクルードする実装クラスです。
 * <p>
 * 設定ファイルはテストコンテキスト内のS2コンテナではなく、このオブジェクトに持つS2コンテナにインクルードします。
 * </p>
 * 
 * @author taedium
 */
public class ConfigFileIncluderImpl implements ConfigFileIncluder {

    /** ロガー */
    protected static final Logger logger = Logger
            .getLogger(ConfigFileIncluderImpl.class);

    /** S2コンテナ */
    protected S2Container container;

    /** 設定ファイルのパスのリスト */
    protected final List<String> configFiles = CollectionsUtil.newArrayList();

    /**
     * S2コンテナを設定します。
     * 
     * @param container
     *            S2コンテナ
     */
    @Binding(bindingType = BindingType.MUST)
    public void setContainer(S2Container container) {
        this.container = container;
    }

    /**
     * 設定ファイルのパスを登録します。
     * 
     * @param configFile
     *            設定ファイルのパス
     */
    public void addConfigFile(final String configFile) {
        configFiles.add(configFile);
    }

    public void include(final TestContext testContext) {
        final String dirPath = testContext.getTestClassPackagePath();
        for (final String configFile : configFiles) {
            if (ResourceUtil.isExist(configFile)) {
                include(testContext, configFile);
            } else {
                final String path = dirPath + "/" + configFile;
                if (ResourceUtil.isExist(path)) {
                    include(testContext, path);
                }
            }
        }
    }

    /**
     * 設定ファイルをS2コンテナにインクルードします。
     * 
     * @param testContext
     *            テストコンテキスト
     * @param path
     *            設定ファイルのパス
     */
    protected void include(@SuppressWarnings("unused")
    final TestContext testContext, final String path) {
        if (logger.isDebugEnabled()) {
            logger.log("DSSR0101", new Object[] { path });
        }
        S2ContainerFactory.include(container, path);
    }

}
