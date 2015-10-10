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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.seasar.extension.jdbc.PropertyMapper;
import org.seasar.framework.util.FieldUtil;

/**
 * 多対一関連のエンティティマッパーです。
 * 
 * @author higa
 * 
 */
public class ManyToOneEntityMapperImpl extends AbstractRelationshipEntityMapper {

    /**
     * {@link ManyToOneEntityMapperImpl}を作成します。
     * 
     * @param entityClass
     *            エンティティクラス
     * @param propertyMappers
     *            プロパティマッパーの配列
     * @param idIndices
     *            識別子のインデックスの配列
     * @param field
     *            関連エンティティのフィールド
     * @param inverseField
     *            逆側の関連エンティティのフィールド
     */
    public ManyToOneEntityMapperImpl(Class<?> entityClass,
            PropertyMapper[] propertyMappers, int[] idIndices, Field field,
            Field inverseField) {
        super(entityClass, propertyMappers, idIndices, field, inverseField);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void mapRelationship(Object target, Object entity) {
        if (entity != null) {
            FieldUtil.set(field, target, entity);
            if (inverseField != null) {
                List list = (List) FieldUtil.get(inverseField, entity);
                if (list == null) {
                    list = new ArrayList();
                    FieldUtil.set(inverseField, entity, list);
                }
                if (!contains(list, target)) {
                    list.add(target);
                }
            }
        }
    }

}
