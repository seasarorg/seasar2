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

import java.util.ArrayList;
import java.util.List;

import org.seasar.extension.jdbc.MappingContext;
import org.seasar.extension.jdbc.PropertyMapper;
import org.seasar.extension.jdbc.RelationshipEntityMapper;
import org.seasar.framework.util.ClassUtil;

/**
 * エンティティマッパーや関連マッパーのための抽象クラスです。
 * 
 * @author higa
 * 
 */
public abstract class AbstractEntityMapper {

    /**
     * エンティティクラスです。
     */
    protected Class<?> entityClass;

    /**
     * プロパティマッパーの配列です。
     */
    protected PropertyMapper[] propertyMappers;

    /**
     * 識別子のインデックスの配列です。
     */
    protected int[] idIndices;

    /**
     * 関連エンティティマッパーのリストです。
     */
    protected List<RelationshipEntityMapper> relationshipEntityMapperList = new ArrayList<RelationshipEntityMapper>();

    /**
     * {@link AbstractEntityMapper}を作成します。
     * 
     * @param entityClass
     *            エンティティクラス
     * @param propertyMappers
     *            プロパティマッパーの配列
     * @param idIndices
     *            識別子のインデックスの配列
     */
    public AbstractEntityMapper(Class<?> entityClass,
            PropertyMapper[] propertyMappers, int[] idIndices) {
        this.entityClass = entityClass;
        this.propertyMappers = propertyMappers;
        this.idIndices = idIndices;
    }

    /**
     * キーを返します。
     * 
     * @param values
     *            結果セットの1行分のデータ
     * @return キー
     */
    public Object getKey(Object[] values) {
        if (idIndices.length == 1) {
            return values[idIndices[0]];
        } else if (idIndices.length > 1) {
            Object[] objs = new Object[idIndices.length];
            for (int i = 0; i < idIndices.length; i++) {
                objs[i] = values[idIndices[i]];
                if (objs[i] == null) {
                    return null;
                }
            }
            return new KeyItems(objs);
        }
        return null;
    }

    /**
     * エンティティを返します。。
     * 
     * @param values
     *            結果セットの1行分のデータ
     * @param mappingContext
     *            マッピングコンテキスト
     * @param key
     *            キー
     * @return エンティティ
     */
    protected Object getEntity(Object[] values, MappingContext mappingContext,
            Object key) {
        Object entity = null;
        if (key != null) {
            entity = mappingContext.getCache(entityClass, key);
        } else if (idIndices.length > 0) {
            return null;
        }
        if (entity == null) {
            entity = createEntity(values, mappingContext, key);
        }
        return entity;
    }

    /**
     * エンティティを作成します。
     * 
     * @param values
     *            結果セットの1行分のデータ
     * @param mappingContext
     *            マッピングコンテキスト
     * @param key
     *            キー
     * @return エンティティ
     */
    protected Object createEntity(Object[] values,
            MappingContext mappingContext, Object key) {
        Object entity = ClassUtil.newInstance(entityClass);
        for (PropertyMapper propertyMapper : propertyMappers) {
            propertyMapper.map(entity, values);
        }
        if (key != null) {
            mappingContext.setCache(entityClass, key, entity);
        }
        return entity;
    }

    /**
     * 関連エンティティマッパーの処理を行ないます。
     * 
     * @param entity
     *            エンティティ
     * @param values
     *            結果セットの1行分のデータ
     * @param mappingContext
     *            マッピングコンテキスト
     */
    protected void mapRelationships(Object entity, Object[] values,
            MappingContext mappingContext) {
        if (entity == null) {
            return;
        }
        for (RelationshipEntityMapper mapper : relationshipEntityMapperList) {
            mapper.map(entity, values, mappingContext);
        }
    }

    /**
     * エンティティクラスを返します。
     * 
     * @return エンティティクラス
     */
    public Class<?> getEntityClass() {
        return entityClass;
    }

    /**
     * 識別子のインデックスの配列を返します。
     * 
     * @return 識別子のインデックスの配列
     */
    public int[] getIdIndices() {
        return idIndices;
    }

    /**
     * プロパティマッパーの配列を返します。
     * 
     * @return プロパティマッパーの配列
     */
    public PropertyMapper[] getPropertyMappers() {
        return propertyMappers;
    }

    /**
     * 関連のエンティティマッパーの配列を返します。
     * 
     * @return 関連のエンティティマッパーの配列
     */
    public RelationshipEntityMapper[] getRelationshipEntityMappers() {
        return relationshipEntityMapperList
                .toArray(new AbstractRelationshipEntityMapper[relationshipEntityMapperList
                        .size()]);
    }

    /**
     * 関連のエンティティマッパーを追加します。
     * 
     * @param relationshipEntityMapper
     *            関連のエンティティマッパー
     */
    public void addRelationshipEntityMapper(
            RelationshipEntityMapper relationshipEntityMapper) {
        relationshipEntityMapperList.add(relationshipEntityMapper);
    }
}