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
package org.seasar.extension.persistence;

import org.seasar.extension.persistence.exception.PropertyMetaNotFoundRuntimeException;
import org.seasar.framework.util.ArrayMap;

/**
 * Entityのメタデータです。
 * 
 * @author higa
 * 
 */
public class EntityMeta {

    private String name;

    private TableMeta tableMeta;

    private ArrayMap propertyMetaMap = new ArrayMap();

    private ArrayMap additionalInfoMap = new ArrayMap();

    /**
     * 名前を返します。
     * 
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * 名前を設定します。
     * 
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * <code>TableMeta</code>を返します。
     * 
     * @return tableMeta
     */
    public TableMeta getTableMeta() {
        return tableMeta;
    }

    /**
     * <code>TableMeta</code>を設定します。
     * 
     * @param tableMeta
     */
    public void setTableMeta(TableMeta tableMeta) {
        this.tableMeta = tableMeta;
    }

    /**
     * 名前に対応した<code>PropertyMeta</code>を返します。
     * 
     * @param propertyName
     * @return <code>PropertyMeta</code>
     */
    public PropertyMeta getPropertyMeta(String propertyName) {
        PropertyMeta meta = (PropertyMeta) propertyMetaMap.get(propertyName);
        if (meta == null) {
            throw new PropertyMetaNotFoundRuntimeException(name, propertyName);
        }
        return meta;
    }

    /**
     * インデックスに対応した<code>PropertyMeta</code>を返します。
     * 
     * @param index
     * @return <code>PropertyMeta</code>
     */
    public PropertyMeta getPropertyMeta(int index) {
        return (PropertyMeta) propertyMetaMap.get(index);
    }

    /**
     * <code>PropertyMeta</code>のサイズを返します。
     * 
     * @return size
     */
    public int getPropertyMetaSize() {
        return propertyMetaMap.size();
    }

    /**
     * <code>PropertyMeta</code>を追加します。
     * 
     * @param propertyMeta
     */
    public void addPropertyMeta(PropertyMeta propertyMeta) {
        propertyMetaMap.put(propertyMeta.getName(), propertyMeta);
    }

    /**
     * 名前に対応した追加情報を返します。
     * 
     * @param propertyName
     * @return additionalInfo
     */
    public Object getAdditionalInfo(String propertyName) {
        return additionalInfoMap.get(propertyName);
    }

    /**
     * インデックスに対応した追加情報を返します。
     * 
     * @param index
     * @return additionalInfo
     */
    public Object getAdditionalInfo(int index) {
        return additionalInfoMap.get(index);
    }

    /**
     * 追加情報のサイズを返します。
     * 
     * @return size
     */
    public int getAdditionalInfoSize() {
        return additionalInfoMap.size();
    }

    /**
     * 追加情報を追加します。
     * 
     * @param name
     * @param additionalInfo
     */
    public void addAdditionalInfo(String name, Object additionalInfo) {
        additionalInfoMap.put(name, additionalInfo);
    }
}