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

import javax.persistence.EntityManagerFactory;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.jpa.PersistenceUnitManager;
import org.seasar.framework.jpa.PersistenceUnitProvider;
import org.seasar.framework.jpa.metadata.EntityDescProvider;
import org.seasar.framework.jpa.unit.EntityReaderProvider;
import org.seasar.framework.util.StringUtil;

/**
 * 永続ユニットプロバイダの抽象クラスです。
 * 
 * @author koichik
 */
public abstract class AbstractPersistenceUnitProvider implements
        PersistenceUnitProvider {

    /** 永続ユニットマネージャ */
    protected PersistenceUnitManager persistenceUnitManager;

    /** {@link EntityDescProvider} */
    protected EntityDescProvider entityDescProvider;

    /** {@link EntityReaderProvider} */
    protected EntityReaderProvider entityReaderProvider;

    /** (具象) 永続ユニット名 */
    protected String unitName;

    /** 抽象永続ユニット名 */
    protected String abstractUnitName;

    /**
     * インスタンスを構築します。
     */
    protected AbstractPersistenceUnitProvider() {
    }

    /**
     * 永続ユニットマネージャを設定します。
     * 
     * @param persistenceUnitManager
     *            永続ユニットマネージャ
     */
    @Binding(bindingType = BindingType.MUST)
    public void setPersistenceUnitManager(
            final PersistenceUnitManager persistenceUnitManager) {
        this.persistenceUnitManager = persistenceUnitManager;
    }

    /**
     * 永続ユニット名を設定します。
     * 
     * @param unitName
     *            永続ユニット名
     */
    @Binding(bindingType = BindingType.MUST)
    public void setUnitName(final String unitName) {
        this.unitName = unitName;
    }

    /**
     * 抽象永続ユニット名を設定します。
     * 
     * @param abstractUnitName
     *            抽象永続ユニット名
     */
    @Binding(bindingType = BindingType.MAY)
    public void setAbstractUnitName(final String abstractUnitName) {
        this.abstractUnitName = abstractUnitName;
    }

    public EntityDescProvider getEntityDescProvider() {
        return entityDescProvider;
    }

    /**
     * {@link EntityDescProvider}を設定します。
     * 
     * @param entityDescProvider
     *            {@link EntityDescProvider}
     */
    @Binding(bindingType = BindingType.MUST)
    public void setEntityDescProvider(
            final EntityDescProvider entityDescProvider) {
        this.entityDescProvider = entityDescProvider;
    }

    public EntityReaderProvider getEntityReaderProvider() {
        return entityReaderProvider;
    }

    /**
     * {@link EntityReaderProvider}を設定します。
     * 
     * @param entityReaderProvider
     *            {@link EntityReaderProvider}
     */
    @Binding(bindingType = BindingType.MUST)
    public void setEntityReaderProvider(
            final EntityReaderProvider entityReaderProvider) {
        this.entityReaderProvider = entityReaderProvider;
    }

    public EntityManagerFactory getEntityManagerFactory() {
        return persistenceUnitManager.getEntityManagerFactory(StringUtil
                .isEmpty(abstractUnitName) ? unitName : abstractUnitName,
                unitName, this);
    }

    public EntityManagerFactory createEntityManagerFactory(final String unitName) {
        return createEntityManagerFactory(unitName, unitName);
    }

}
