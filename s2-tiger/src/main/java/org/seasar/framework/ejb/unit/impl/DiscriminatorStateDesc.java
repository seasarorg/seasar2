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

import java.lang.reflect.Constructor;
import java.util.List;

import javax.persistence.DiscriminatorType;

import org.seasar.framework.ejb.unit.PersistentClassDesc;
import org.seasar.framework.ejb.unit.PersistentColumn;
import org.seasar.framework.ejb.unit.PersistentStateAccessor;
import org.seasar.framework.ejb.unit.PersistentStateDesc;
import org.seasar.framework.ejb.unit.PersistentStateType;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.ConstructorUtil;

public class DiscriminatorStateDesc implements PersistentStateDesc {

    private final PersistentClassDesc persistentClassDesc;

    private final DiscriminatorType discriminatorType;

    private final String value;

    private PersistentColumn column;

    public DiscriminatorStateDesc(PersistentClassDesc persistentClassDesc,
            PersistentColumn column, DiscriminatorType discriminatorType,
            String value) {
        if (persistentClassDesc == null) {
            throw new EmptyRuntimeException("persistentClassDesc");
        }
        if (discriminatorType == null) {
            throw new EmptyRuntimeException("discriminatorType");
        }
        if (value == null) {
            throw new EmptyRuntimeException("value");
        }
        if (column == null) {
            throw new EmptyRuntimeException("column");
        }
        this.persistentClassDesc = persistentClassDesc;
        this.discriminatorType = discriminatorType;
        this.value = value;
        this.column = column;
    }

    public Class<?> getCollectionClass() {
        throw new UnsupportedOperationException("getCollectionType");
    }

    public PersistentColumn getColumn() {
        return column;
    }

    public void setColumn(PersistentColumn column) {
        if (column == null) {
            throw new EmptyRuntimeException("column");
        }
        this.column = column;
    }
    
    public String getName() {
        return "$" + column.getName();
    }

    public Class<?> getPersistentStateClass() {
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

    public PersistentClassDesc getPersistentClassDesc() {
        return persistentClassDesc;
    }

    public Object getValue(Object target) {
        Constructor constructor = ClassUtil.getConstructor(
                getPersistentStateClass(), new Class[] { String.class });
        return ConstructorUtil.newInstance(constructor, new Object[] { value });
    }

    public boolean isCollection() {
        return false;
    }

    public PersistentClassDesc getEmbeddedClassDesc() {
        throw new UnsupportedOperationException("getEmbeddedClassDesc");
    }
    
    public List<PersistentStateDesc> getEmbeddedStateDescs() {
        throw new UnsupportedOperationException("getEmbeddedStateDescs");
    }

    public void setupForeignKeyColumns(PersistentClassDesc relationship) {
        throw new UnsupportedOperationException("setupRelationshipColumns");
    }

    public boolean isIdentifier() {
        return false;
    }

    public void setValue(Object target, Object value) {
        throw new UnsupportedOperationException("setValue");
    }

    public boolean hasColumn(String columnName) {
        return this.column.getName().equalsIgnoreCase(columnName);
    }

    public List<PersistentColumn> getForeignKeyColumns() {
        throw new UnsupportedOperationException("getForeignKeyColumns");
    }

    public PersistentStateAccessor getAccessor() {
        throw new UnsupportedOperationException("getAccessor");
    }

    public PersistentStateType getPersistentStateType() {
        return PersistentStateType.BASIC;
    }

    public boolean isRelationOwningSide() {
        return false;
    }
}
