/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
package org.seasar.framework.jpa.impl;

import javax.persistence.EntityManager;

import org.seasar.extension.datasource.DataSourceFactory;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.jpa.EntityManagerProvider;
import org.seasar.framework.util.StringUtil;

/**
 * {@link EntityManager}を提供するコンポーネントのインターフェースです。
 * 
 * @author koichik
 */
@Component
public class EntityManagerProviderImpl implements EntityManagerProvider {

    /** このコンポーネントを定義しているS2コンテナ */
    @Binding(bindingType = BindingType.MUST)
    protected S2Container container;

    /** データソース・ファクトリ */
    @Binding(bindingType = BindingType.MUST)
    protected DataSourceFactory dataSourceFactory;

    /** デフォルトの{@link EntityManager}のコンポーネント名 */
    @Binding(bindingType = BindingType.MAY)
    protected String entityManagerBaseName = DEFAULT_ENTITY_MANAGER_NAME;

    /**
     * インスタンスを構築します。
     */
    public EntityManagerProviderImpl() {
    }

    public String getSelectableEntityManagerPrefix() {
        return dataSourceFactory.getSelectableDataSourceName();
    }

    public EntityManager getEntityManger(final String prefix) {
        return EntityManager.class.cast(container.getRoot().getComponent(
                getEntityManagerComponentName(prefix)));
    }

    /**
     * {@link EntityManager}のコンポーネント名を返します。
     * 
     * @param prefix
     *            プレフィックス
     * @return {@link EntityManager}のコンポーネント名
     */
    protected String getEntityManagerComponentName(final String prefix) {
        final String actualPrefix = prefix != null ? prefix
                : getSelectableEntityManagerPrefix();
        if (!StringUtil.isEmpty(actualPrefix)) {
            return actualPrefix + StringUtil.capitalize(entityManagerBaseName);
        }
        return entityManagerBaseName;
    }

}
