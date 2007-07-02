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
package org.seasar.extension.jdbc.impl;

import org.seasar.extension.jdbc.PropertyType;
import org.seasar.extension.jdbc.ValueType;
import org.seasar.extension.jdbc.types.ValueTypes;
import org.seasar.framework.beans.PropertyDesc;

public class PropertyTypeImpl implements PropertyType {

    private PropertyDesc propertyDesc_;

    private String propertyName_;

    private String columnName_;

    private ValueType valueType_;

    private boolean primaryKey_ = false;

    private boolean persistent_ = true;

    public PropertyTypeImpl(PropertyDesc propertyDesc) {
        this(propertyDesc, ValueTypes.OBJECT, propertyDesc.getPropertyName());
    }

    public PropertyTypeImpl(PropertyDesc propertyDesc, ValueType valueType) {
        this(propertyDesc, valueType, propertyDesc.getPropertyName());
    }

    public PropertyTypeImpl(PropertyDesc propertyDesc, ValueType valueType,
            String columnName) {

        propertyDesc_ = propertyDesc;
        propertyName_ = propertyDesc.getPropertyName();
        valueType_ = valueType;
        columnName_ = columnName;
    }

    public PropertyTypeImpl(String propertyName, ValueType valueType) {
        this(propertyName, valueType, propertyName);
    }

    public PropertyTypeImpl(String propertyName, ValueType valueType,
            String columnName) {
        propertyName_ = propertyName;
        valueType_ = valueType;
        columnName_ = columnName;
    }

    public PropertyDesc getPropertyDesc() {
        return propertyDesc_;
    }

    public String getPropertyName() {
        return propertyName_;
    }

    public String getColumnName() {
        return columnName_;
    }

    public void setColumnName(String columnName) {
        columnName_ = columnName;
    }

    public ValueType getValueType() {
        return valueType_;
    }

    public boolean isPrimaryKey() {
        return primaryKey_;
    }

    public void setPrimaryKey(boolean primaryKey) {
        primaryKey_ = primaryKey;
    }

    public boolean isPersistent() {
        return persistent_;
    }

    public void setPersistent(boolean persistent) {
        persistent_ = persistent;
    }
}