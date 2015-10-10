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
package org.seasar.extension.jdbc.impl;

import org.seasar.extension.jdbc.PropertyType;
import org.seasar.extension.jdbc.ValueType;
import org.seasar.extension.jdbc.types.ValueTypes;
import org.seasar.framework.beans.PropertyDesc;

/**
 * {@link PropertyType}の実装クラスです。
 * 
 * @author higa
 * 
 */
public class PropertyTypeImpl implements PropertyType {

    private PropertyDesc propertyDesc;

    private String propertyName;

    private String columnName;

    private ValueType valueType;

    private boolean primaryKey = false;

    private boolean persistent = true;

    /**
     * {@link PropertyTypeImpl}を作成します。
     * 
     * @param propertyDesc
     *            プロパティ記述
     */
    public PropertyTypeImpl(PropertyDesc propertyDesc) {
        this(propertyDesc, ValueTypes.OBJECT, propertyDesc.getPropertyName());
    }

    /**
     * {@link PropertyTypeImpl}を作成します。
     * 
     * @param propertyDesc
     *            プロパティ記述
     * @param valueType
     *            値型
     */
    public PropertyTypeImpl(PropertyDesc propertyDesc, ValueType valueType) {
        this(propertyDesc, valueType, propertyDesc.getPropertyName());
    }

    /**
     * {@link PropertyTypeImpl}を作成します。
     * 
     * @param propertyDesc
     *            プロパティ記述
     * @param valueType
     *            値型
     * @param columnName
     *            カラム名
     */
    public PropertyTypeImpl(PropertyDesc propertyDesc, ValueType valueType,
            String columnName) {

        this.propertyDesc = propertyDesc;
        this.propertyName = propertyDesc.getPropertyName();
        this.valueType = valueType;
        this.columnName = columnName;
    }

    /**
     * {@link PropertyTypeImpl}を作成します。
     * 
     * @param propertyName
     *            プロパティ名
     * @param valueType
     *            値型
     */
    public PropertyTypeImpl(String propertyName, ValueType valueType) {
        this(propertyName, valueType, propertyName);
    }

    /**
     * {@link PropertyTypeImpl}を作成します。
     * 
     * @param propertyName
     *            プロパティ名
     * @param valueType
     *            値型
     * @param columnName
     *            カラム名
     */
    public PropertyTypeImpl(String propertyName, ValueType valueType,
            String columnName) {
        this.propertyName = propertyName;
        this.valueType = valueType;
        this.columnName = columnName;
    }

    public PropertyDesc getPropertyDesc() {
        return propertyDesc;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public ValueType getValueType() {
        return valueType;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
    }

    public boolean isPersistent() {
        return persistent;
    }

    public void setPersistent(boolean persistent) {
        this.persistent = persistent;
    }
}