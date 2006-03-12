/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
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
package org.seasar.framework.ejb.unit.impl;

import javax.persistence.DiscriminatorType;

import org.seasar.framework.ejb.unit.PersistentClassDesc;
import org.seasar.framework.ejb.unit.PersistentStateDesc;
import org.seasar.framework.exception.EmptyRuntimeException;


public class DiscriminatorStateDesc implements PersistentStateDesc {

    private final String tableName;

    private final String columnName;
    
    private final DiscriminatorType discriminatorType;

    private final String value;

    public DiscriminatorStateDesc(String tableName, String columnName, DiscriminatorType discriminatorType, String value) {
        if (tableName == null) {
            throw new EmptyRuntimeException("tableName");
        }
        if (columnName == null) {
            throw new EmptyRuntimeException("columnName");
        }
        if (discriminatorType == null) {
            throw new EmptyRuntimeException("discriminatorType");
        }
        this.tableName = tableName;
        this.columnName = columnName;
        this.discriminatorType = discriminatorType;
        this.value = value;
    }

    public PersistentClassDesc createPersistentClass() {
        throw new UnsupportedOperationException("createPersistentClass");
    }

    public Class<?> getCollectionType() {
        throw new UnsupportedOperationException("getCollectionType");
    }

    public String getColumnName() {
        return columnName;
    }

    public String getStateName() {
        return "$" + columnName;
    }

    public Class<?> getPersistentStateType() {
        switch (discriminatorType) {
        case STRING:
            return String.class;
        case CHAR:
            return Character.class;
        case INTEGER:
            return Integer.class;
        }
        return null;
    }

    public String getTableName() {
        return tableName;
    }

    public Object getValue(Object target) {
        return value;
    }

    public boolean hasColumnName() {
        return true;
    }

    public boolean hasTableName() {
        return true;
    }

    public boolean isCollection() {
        return false;
    }

    public boolean isEmbedded() {
        return false;
    }

    public boolean isPersistent() {
        return true;
    }

    public boolean isProperty() {
        return false;
    }

    public boolean isRelationship() {
        return false;
    }

    public void setValue(Object target, Object value) {
        throw new UnsupportedOperationException("setValue");
    }
}
