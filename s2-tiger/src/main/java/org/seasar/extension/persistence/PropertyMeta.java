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

import org.seasar.framework.util.ArrayMap;

/**
 * Propertyのメタデータです。
 * 
 * @author higa
 * 
 */
public class PropertyMeta {

    private String name;

    private boolean aTransient = false;

    private boolean id = false;

    private boolean version = false;

    private ColumnMeta columnMeta;

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
     * 一時的かどうか返します。
     * 
     * @return transient
     */
    public boolean isTransient() {
        return aTransient;
    }

    /**
     * 一時的かどうか設定します。
     * 
     * @param aTransient
     */
    public void setTransient(boolean aTransient) {
        this.aTransient = aTransient;
    }

    /**
     * <code>ColumnMeta</code>を返します。
     * 
     * @return columnMeta
     */
    public ColumnMeta getColumnMeta() {
        return columnMeta;
    }

    /**
     * <code>ColumnMeta</code>を設定します。
     * 
     * @param columnMeta
     */
    public void setColumnMeta(ColumnMeta columnMeta) {
        this.columnMeta = columnMeta;
    }

    /**
     * Idかどうかを返します。
     * 
     * @return id
     */
    public boolean isId() {
        return id;
    }

    /**
     * Idかどうかを設定します。
     * 
     * @param id
     */
    public void setId(boolean id) {
        this.id = id;
    }

    /**
     * Versionかどうかを返します。
     * 
     * @return version
     */
    public boolean isVersion() {
        return version;
    }

    /**
     * Versionかどうかを設定します。
     * 
     * @param version
     */
    public void setVersion(boolean version) {
        this.version = version;
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