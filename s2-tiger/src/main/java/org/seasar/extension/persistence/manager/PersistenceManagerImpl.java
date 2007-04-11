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
package org.seasar.extension.persistence.manager;

import java.util.List;

import org.seasar.extension.persistence.EntityMeta;
import org.seasar.extension.persistence.EntityMetaFactory;
import org.seasar.extension.persistence.PersistenceManager;
import org.seasar.extension.persistence.exception.NoEntityRuntimeException;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;

/**
 * @author higa
 * 
 */
public class PersistenceManagerImpl implements PersistenceManager {

    private EntityMetaFactory entityMetaFactory;

    /**
     * <code>EntityMetaFactory</code>を返します。
     * 
     * @return Returns entityMetaFactory
     */
    public EntityMetaFactory getEntityMetaFactory() {
        return entityMetaFactory;
    }

    /**
     * <code>EntityMetaFactory</code>を設定します。
     * 
     * @param entityMetaFactory
     */
    @Binding(bindingType = BindingType.MUST)
    public void setEntityMetaFactory(EntityMetaFactory entityMetaFactory) {
        this.entityMetaFactory = entityMetaFactory;
    }

    public <T> List<T> findAll(Class<? extends T> entityClass, Object criteria) {
        EntityMeta entityMeta = entityMetaFactory.getEntityMeta(entityClass);
        if (entityMeta == null) {
            throw new NoEntityRuntimeException(entityClass);
        }
        return null;
    }

}
