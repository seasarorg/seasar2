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
import org.seasar.framework.ejb.unit.PersistentColumn;
import org.seasar.framework.ejb.unit.PersistentStateDesc;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.util.ClassUtil;

public class DiscriminatorStateDesc implements PersistentStateDesc {

    private final PersistentClassDesc persistentClassDesc;

    private PersistentColumn column;
    
    private String tableName;

    private final String columnName;

    private final DiscriminatorType discriminatorType;

    private final String value;

    public DiscriminatorStateDesc(PersistentClassDesc persistentClassDesc,
            String tableName, String columnName,
            DiscriminatorType discriminatorType, String value) {
        if (persistentClassDesc == null) {
            throw new EmptyRuntimeException("persistentClassDesc");
        }
        if (tableName == null) {
            throw new EmptyRuntimeException("tableName");
        }
        if (columnName == null) {
            throw new EmptyRuntimeException("columnName");
        }
        if (discriminatorType == null) {
            throw new EmptyRuntimeException("discriminatorType");
        }
        this.persistentClassDesc = persistentClassDesc;
        this.tableName = tableName;
        this.columnName = columnName;
        this.column = new PersistentColumnImpl(tableName, columnName);
        this.discriminatorType = discriminatorType;
        this.value = value;

        this.column = new PersistentColumnImpl(tableName, columnName);
    }

    public Class<?> getCollectionType() {
        throw new UnsupportedOperationException("getCollectionType");
    }

    public PersistentColumn getColumn() {
        return column;
    }
    
    public void setColumn(PersistentColumn column) {
        this.column = column;
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

    public boolean isCollection() {
        return false;
    }

    public boolean isEmbedded() {
        return false;
    }

    public PersistentClassDesc getEmbeddedClassDesc() {
        throw new UnsupportedOperationException("getEmbeddedClassDesc");
    }

    public void setRelationshipClassDesc(PersistentClassDesc persistentClassDesc) {
        throw new UnsupportedOperationException("setRelationshipClassDesc");
    }

    public PersistentClassDesc getRelationshipClassDesc() {
        throw new UnsupportedOperationException("getRelationshipClassDesc");
    }

    public void setupForeignKeyColumns() {
        throw new UnsupportedOperationException("setupRelationshipColumns");
    }

    public boolean isProperty() {
        return false;
    }

    public boolean isToOneRelationship() {
        return false;
    }

    public boolean isToManyRelationship() {
        return false;
    }

    public boolean isIdentifier() {
        return false;
    }

    public void setValue(Object target, Object value) {
        throw new UnsupportedOperationException("setValue");
    }

    public void addForeignKeyColumn(PersistentColumn column) {
        throw new UnsupportedOperationException("addForeignKeyColumn");
    }

    public boolean hasReferencedColumn(String columnName) {
        return this.column.getName().equalsIgnoreCase(columnName);
    }

    public int getForeignKeyColumnSize() {
        return 0;
    }

    public PersistentColumn getForeignKeyColumn(int index) {
        throw new UnsupportedOperationException("getForeignKeyColumn");
    }

    public String getPathName() {
        Class<?> clazz = persistentClassDesc.getPersistentClass();
        return ClassUtil.getShortClassName(clazz) + "." + getStateName();
    }
    
}
