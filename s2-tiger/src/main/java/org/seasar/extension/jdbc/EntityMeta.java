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
package org.seasar.extension.jdbc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.seasar.extension.jdbc.exception.ColumnDuplicatedRuntimeException;
import org.seasar.extension.jdbc.exception.EntityColumnNotFoundRuntimeException;
import org.seasar.extension.jdbc.exception.PropertyDuplicatedRuntimeException;
import org.seasar.extension.jdbc.exception.PropertyNotFoundRuntimeException;
import org.seasar.framework.util.ArrayMap;
import org.seasar.framework.util.CaseInsensitiveMap;

/**
 * エンティティのメタデータです。
 * 
 * @author higa
 * 
 */
public class EntityMeta {

    /**
     * 名前です。
     */
    protected String name;

    /**
     * エンティティクラスです。
     */
    protected Class<?> entityClass;

    /**
     * テーブルメタデータです。
     */
    protected TableMeta tableMeta;

    /**
     * プロパティメタデータの配列マップです。
     */
    protected CaseInsensitiveMap propertyMetaMap = new CaseInsensitiveMap();

    /**
     * 追加情報の配列マップです。
     */
    protected ArrayMap additionalInfoMap = new ArrayMap();

    /**
     * 識別子になっているプロパティメタデータのリストです。
     */
    protected List<PropertyMeta> idPropertyMetaList = new ArrayList<PropertyMeta>();

    /**
     * バージョンを表すプロパティメタデータです。
     */
    protected PropertyMeta versionPropertyMeta;

    /**
     * MappedByで注釈されているプロパティメタデータのマップです。
     */
    protected CaseInsensitiveMap mappedByPropertyMetaMap = new CaseInsensitiveMap();

    /**
     * カラム名をキーにしたプロパティメタデータの配列マップです。
     */
    protected CaseInsensitiveMap columnPropertyMetaMap = new CaseInsensitiveMap();

    /**
     * 関連が解決されたかどうかです。
     */
    protected volatile boolean relationshipResolved = false;

    /**
     * {@link EntityMeta}を作成します。
     */
    public EntityMeta() {
    }

    /**
     * {@link EntityMeta}を作成します。
     * 
     * @param name
     *            名前
     */
    public EntityMeta(String name) {
        setName(name);
    }

    /**
     * 名前を返します。
     * 
     * @return 名前
     */
    public String getName() {
        return name;
    }

    /**
     * 名前を設定します。
     * 
     * @param name
     *            名前
     */
    public void setName(String name) {
        this.name = name;
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
     * エンティティクラスを設定します。
     * 
     * @param entityClass
     *            エンティティクラス
     */
    public void setEntityClass(Class<?> entityClass) {
        this.entityClass = entityClass;
    }

    /**
     * テーブルメタデータを返します。
     * 
     * @return テーブルメタデータ
     */
    public TableMeta getTableMeta() {
        return tableMeta;
    }

    /**
     * テーブルメタデータを設定します。
     * 
     * @param tableMeta
     *            テーブルメタデータ
     */
    public void setTableMeta(TableMeta tableMeta) {
        this.tableMeta = tableMeta;
    }

    /**
     * プロパティメタデータを返します。
     * 
     * @param propertyName
     *            プロパティ名
     * @return プロパティメタデータ
     * @throws PropertyNotFoundRuntimeException
     *             プロパティメタデータが見つからない場合
     * 
     */
    public PropertyMeta getPropertyMeta(String propertyName)
            throws PropertyNotFoundRuntimeException {
        PropertyMeta meta = (PropertyMeta) propertyMetaMap.get(propertyName);
        if (meta == null) {
            throw new PropertyNotFoundRuntimeException(name, propertyName);
        }
        return meta;
    }

    /**
     * カラムに結びつくプロパティメタデータを返します。
     * 
     * @param columnName
     *            カラム名
     * @return プロパティメタデータ
     * @throws EntityColumnNotFoundRuntimeException
     *             カラム用のプロパティメタデータが見つからない場合
     * 
     */
    public PropertyMeta getColumnPropertyMeta(String columnName)
            throws EntityColumnNotFoundRuntimeException {
        PropertyMeta meta = (PropertyMeta) columnPropertyMetaMap
                .get(columnName);
        if (meta == null) {
            throw new EntityColumnNotFoundRuntimeException(name, columnName);
        }
        return meta;
    }

    /**
     * プロパティメタデータがあるかどうかを返します。
     * 
     * @param propertyName
     *            プロパティ名
     * @return プロパティメタデータがあるかどうか
     */
    public boolean hasPropertyMeta(String propertyName) {
        return propertyMetaMap.containsKey(propertyName);
    }

    /**
     * カラムに結びつくプロパティメタデータがあるかどうかを返します。
     * 
     * @param columnName
     *            カラム名
     * @return プロパティメタデータがあるかどうか
     */
    public boolean hasColumnPropertyMeta(String columnName) {
        return columnPropertyMetaMap.containsKey(columnName);
    }

    /**
     * プロパティメタデータの数を返します。
     * 
     * @return プロパティメタデータの数
     */
    public int getPropertyMetaSize() {
        return propertyMetaMap.size();
    }

    /**
     * カラムに結びつくプロパティメタデータの数を返します。
     * 
     * @return カラムに結びつくプロパティメタデータの数
     */
    public int getColumnPropertyMetaSize() {
        return columnPropertyMetaMap.size();
    }

    /**
     * プロパティメタデータを返します。
     * 
     * @param index
     *            インデックス
     * @return プロパティメタデータ
     */
    public PropertyMeta getPropertyMeta(int index) {
        return (PropertyMeta) propertyMetaMap.get(index);
    }

    /**
     * 全てのプロパティメタデータの{@link Iterable}を返します。
     * 
     * @return 全てのプロパティメタデータの{@link Iterable}
     */
    public Iterable<PropertyMeta> getAllPropertyMeta() {
        return new Iterable<PropertyMeta>() {

            public Iterator<PropertyMeta> iterator() {
                return new Iterator<PropertyMeta>() {

                    private int i;

                    public boolean hasNext() {
                        return i < propertyMetaMap.size();
                    }

                    public PropertyMeta next() {
                        return PropertyMeta.class
                                .cast(propertyMetaMap.get(i++));
                    }

                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }

    /**
     * カラムに結びつくプロパティメタデータを返します。
     * 
     * @param index
     *            カラムインデックス
     * @return プロパティメタデータ
     */
    public PropertyMeta getColumnPropertyMeta(int index) {
        return (PropertyMeta) columnPropertyMetaMap.get(index);
    }

    /**
     * カラムに結びつく全てのプロパティメタデータの{@link Iterable}を返します。
     * 
     * @return カラムに結びつく全てのプロパティメタデータの{@link Iterable}
     */
    public Iterable<PropertyMeta> getAllColumnPropertyMeta() {
        return new Iterable<PropertyMeta>() {

            public Iterator<PropertyMeta> iterator() {
                return new Iterator<PropertyMeta>() {

                    private int i;

                    public boolean hasNext() {
                        return i < columnPropertyMetaMap.size();
                    }

                    public PropertyMeta next() {
                        return PropertyMeta.class.cast(columnPropertyMetaMap
                                .get(i++));
                    }

                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }

    /**
     * 識別子になっているプロパティメタデータのリストを返します。
     * 
     * @return 識別子になっているプロパティメタデータのリスト
     */
    public List<PropertyMeta> getIdPropertyMetaList() {
        return idPropertyMetaList;
    }

    /**
     * バージョンを表すプロパティメタデータがあれば<code>true</code>を返します。
     * 
     * @return バージョンを表すプロパティメタデータがあれば<code>true</code>
     */
    public boolean hasVersionPropertyMeta() {
        return versionPropertyMeta != null;
    }

    /**
     * バージョンを表すプロパティメタデータを返します。
     * 
     * @return バージョンを表すプロパティメタデータ
     */
    public PropertyMeta getVersionPropertyMeta() {
        return versionPropertyMeta;
    }

    /**
     * MappedByで注釈されているプロパティメタデータを返します。
     * 
     * @param mappedBy
     *            関連の所有者側のプロパティ名
     * @param relationshipClass
     *            関連クラス
     * @return MappedByで注釈されているプロパティメタデータ
     */
    @SuppressWarnings("unchecked")
    public PropertyMeta getMappedByPropertyMeta(String mappedBy,
            Class<?> relationshipClass) {
        Map m = (Map) mappedByPropertyMetaMap.get(mappedBy);
        if (m == null) {
            return null;
        }
        return (PropertyMeta) m.get(relationshipClass.getName());
    }

    /**
     * プロパティメタデータを追加します。
     * 
     * @param propertyMeta
     *            プロパティメタデータ
     */
    public void addPropertyMeta(PropertyMeta propertyMeta) {
        if (propertyMetaMap.put(propertyMeta.getName(), propertyMeta) != null) {
            throw new PropertyDuplicatedRuntimeException(name, propertyMeta
                    .getName());
        }
        if (propertyMeta.isId()) {
            idPropertyMetaList.add(propertyMeta);
        }
        if (propertyMeta.isVersion()) {
            versionPropertyMeta = propertyMeta;
        }
        if (propertyMeta.getMappedBy() != null) {
            CaseInsensitiveMap m = (CaseInsensitiveMap) mappedByPropertyMetaMap
                    .get(propertyMeta.getMappedBy());
            if (m == null) {
                m = new CaseInsensitiveMap();
                mappedByPropertyMetaMap.put(propertyMeta.getMappedBy(), m);
            }
            m.put(propertyMeta.getRelationshipClass().getName(), propertyMeta);
        }
        if (propertyMeta.getColumnMeta() != null) {
            String columnName = propertyMeta.getColumnMeta().getName();
            PropertyMeta pm2 = (PropertyMeta) columnPropertyMetaMap.put(
                    columnName, propertyMeta);
            if (pm2 != null) {
                throw new ColumnDuplicatedRuntimeException(name, pm2.getName(),
                        propertyMeta.getName(), columnName);
            }
        }
    }

    /**
     * 関連が解決されたかどうかを返します。
     * 
     * @return 関連が解決されたかどうか
     */
    public boolean isRelationshipResolved() {
        return relationshipResolved;
    }

    /**
     * 関連が解決されたかどうかを設定します。
     * 
     * @param relationshipResolved
     *            関連が解決されたかどうか
     */
    public void setRelationshipResolved(boolean relationshipResolved) {
        this.relationshipResolved = relationshipResolved;
    }

    /**
     * 名前に対応した追加情報を返します。
     * 
     * @param name
     *            名前
     * @return 追加情報
     */
    public Object getAdditionalInfo(String name) {
        return additionalInfoMap.get(name);
    }

    /**
     * 追加情報のサイズを返します。
     * 
     * @return 追加情報のサイズ
     */
    public int getAdditionalInfoSize() {
        return additionalInfoMap.size();
    }

    /**
     * インデックスに対応した追加情報を返します。
     * 
     * @param index
     *            インデックス
     * @return 追加情報
     */
    public Object getAdditionalInfo(int index) {
        return additionalInfoMap.get(index);
    }

    /**
     * 追加情報を追加します。
     * 
     * @param name
     *            名前
     * @param additionalInfo
     *            追加情報
     */
    public void addAdditionalInfo(String name, Object additionalInfo) {
        additionalInfoMap.put(name, additionalInfo);
    }
}