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
package org.seasar.extension.jdbc.mapper;

import org.seasar.extension.jdbc.EntityMapper;
import org.seasar.extension.jdbc.MappingContext;
import org.seasar.extension.jdbc.PropertyMapper;

/**
 * {@link EntityMapper}の実装クラスです。
 * 
 * @author higa
 * 
 */
public class EntityMapperImpl extends AbstractEntityMapper implements
        EntityMapper {

    /**
     * {@link EntityMapperImpl}を作成します。
     * 
     * @param entityClass
     *            エンティティクラス
     * @param propertyMappers
     *            プロパティマッパーの配列
     * @param idIndices
     *            識別子のインデックスの配列
     */
    public EntityMapperImpl(Class<?> entityClass,
            PropertyMapper[] propertyMappers, int[] idIndices) {
        super(entityClass, propertyMappers, idIndices);
    }

    public Object map(Object[] values, MappingContext mappingContext) {
        Object key = getKey(values);
        Object entity = getEntity(values, mappingContext, key);
        mapRelationships(entity, values, mappingContext);
        if (!mappingContext.checkDone(this, null, entity)) {
            return entity;
        }
        return null;
    }
}