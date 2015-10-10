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
import java.util.Collection;

import org.seasar.extension.jdbc.MappingContext;
import org.seasar.extension.jdbc.PropertyMapper;
import org.seasar.extension.jdbc.RelationshipEntityMapper;

/**
 * {@link RelationshipEntityMapper}の抽象クラスです。
 * 
 * @author higa
 * 
 */
public abstract class AbstractRelationshipEntityMapper extends
        AbstractEntityMapper implements RelationshipEntityMapper {

    /**
     * 関連エンティティのフィールドです。
     */
    protected Field field;

    /**
     * 逆側の関連エンティティのフィールドです。
     */
    protected Field inverseField;

    /**
     * {@link AbstractRelationshipEntityMapper}を作成します。
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
    public AbstractRelationshipEntityMapper(Class<?> entityClass,
            PropertyMapper[] propertyMappers, int[] idIndices, Field field,
            Field inverseField) {
        super(entityClass, propertyMappers, idIndices);
        this.field = field;
        field.setAccessible(true);
        if (inverseField != null) {
            this.inverseField = inverseField;
            inverseField.setAccessible(true);
        }

    }

    public void map(Object target, Object[] values,
            MappingContext mappingContext) {
        Object key = getKey(values);
        Object entity = getEntity(values, mappingContext, key);
        if (!mappingContext.checkDone(this, target, entity)) {
            mapRelationship(target, entity);
        }
        mapRelationships(entity, values, mappingContext);
    }

    /**
     * 関連をマッピングします。
     * 
     * @param target
     *            関連元のエンティティ
     * @param entity
     *            エンティティ
     */
    protected abstract void mapRelationship(Object target, Object entity);

    /**
     * 関連エンティティのフィールドを返します。
     * 
     * @return 関連エンティティのフィールド
     */
    public Field getField() {
        return field;
    }

    /**
     * 逆側の関連エンティティのフィールドを返します。
     * 
     * @return 逆側の関連エンティティのフィールド
     */
    public Field getInverseField() {
        return inverseField;
    }

    /**
     * 関連の要素にターゲットが含まれていれば{@literal true}を返します。
     * <p>
     * 同値性ではなく同一性で判定します。
     * </p>
     * 
     * @param relationship
     *            関連
     * @param target
     *            ターゲット
     * @return 関連の要素にターゲットが含まれていれば{@literal true}
     */
    protected boolean contains(Collection<?> relationship, Object target) {
        for (Object e : relationship) {
            if (e == target) {
                return true;
            }
        }
        return false;
    }

}
