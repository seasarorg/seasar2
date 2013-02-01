/*
 * Copyright 2004-2013 the Seasar Foundation and the Others.
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
package org.seasar.framework.jpa.unit;

import java.util.Collection;

import org.seasar.framework.jpa.PersistenceUnitManager;
import org.seasar.framework.jpa.PersistenceUnitManagerLocater;
import org.seasar.framework.jpa.PersistenceUnitProvider;

/**
 * {@link EntityReader エンティティリーダ}のファクトリです。
 * 
 * @author taedium
 */
public class EntityReaderFactory {

    /**
     * エンティティリーダを返します。
     * 
     * @param entity
     *            エンティティのインスタンス
     * @return エンティティリーダ
     */
    public static EntityReader getEntityReader(final Object entity) {
        final Class<?> entityClass = entity.getClass();
        final PersistenceUnitManager manager = PersistenceUnitManagerLocater
                .getInstance();
        final PersistenceUnitProvider provider = manager
                .getPersistenceUnitProvider(entityClass);
        return provider.getEntityReaderProvider().createEntityReader(entity);
    }

    /**
     * エンティティリーダを返します。
     * 
     * @param entities
     *            エンティティのインスタンスのコレクション
     * @return エンティティリーダ
     */
    public static EntityReader getEntityReader(final Collection<?> entities) {
        if (entities == null || entities.isEmpty()) {
            return null;
        }
        final Class<?> entityClass = entities.iterator().next().getClass();
        final PersistenceUnitManager manager = PersistenceUnitManagerLocater
                .getInstance();
        final PersistenceUnitProvider provider = manager
                .getPersistenceUnitProvider(entityClass);
        return provider.getEntityReaderProvider().createEntityReader(entities);
    }

}
