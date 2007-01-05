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
 * @author koichik
 * 
 */
@Component
public class EntityManagerProviderImpl implements EntityManagerProvider {

    protected static final String DEFAULT_ENTITY_MANAGER_NAME = "entityManager";

    @Binding(bindingType = BindingType.MUST)
    protected S2Container container;

    @Binding(bindingType = BindingType.MUST)
    protected DataSourceFactory dataSourceFactory;

    @Binding(bindingType = BindingType.MAY)
    protected String entityManagerBaseName = DEFAULT_ENTITY_MANAGER_NAME;

    public String getSelectableEntityManagerName() {
        return dataSourceFactory.getSelectableDataSourceName();
    }

    public String getEntityManagerName(final String name) {
        if (name != null) {
            return name;
        }
        return dataSourceFactory.getSelectableDataSourceName();
    }

    public EntityManager getEntityManger(final String name) {
        return (EntityManager) container.getRoot().getComponent(
                getEntityManagerComponentName(name));
    }

    protected String getEntityManagerComponentName(final String name) {
        final String prefix = getEntityManagerName(name);
        if (prefix == null) {
            return entityManagerBaseName;
        }
        return prefix + StringUtil.capitalize(entityManagerBaseName);
    }

}
