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

import java.lang.reflect.AnnotatedElement;
import java.util.Collections;
import java.util.List;

import org.seasar.framework.ejb.unit.PersistentClassDesc;
import org.seasar.framework.ejb.unit.PersistentColumn;
import org.seasar.framework.ejb.unit.PersistentJoinColumn;
import org.seasar.framework.ejb.unit.PersistentStateAccessor;
import org.seasar.framework.ejb.unit.PersistentStateDesc;
import org.seasar.framework.exception.EmptyRuntimeException;

/**
 * @author taedium
 * 
 */
public abstract class AbstractPersistentStateDesc implements
        PersistentStateDesc {

    protected final PersistentClassDesc persistentClassDesc;

    protected final String primaryTableName;

    protected final PersistentStateAccessor accessor;

    protected final String persistentStateName;

    protected final AnnotatedElement annotatedElement;

    protected final Class<?> persistentStateClass;

    protected Class<?> persistenceTargetClass;

    protected PersistentColumn persistentColumn;

    protected boolean identifier;

    public AbstractPersistentStateDesc(PersistentClassDesc persistentClassDesc,
            String primaryTableName, PersistentStateAccessor accessor) {

        if (persistentClassDesc == null) {
            throw new EmptyRuntimeException("persistentClassDesc");
        }
        if (primaryTableName == null) {
            throw new EmptyRuntimeException("primaryTableName");
        }
        if (accessor == null) {
            throw new EmptyRuntimeException("accessor");
        }
        this.persistentClassDesc = persistentClassDesc;
        this.primaryTableName = primaryTableName;
        this.accessor = accessor;
        this.persistentStateName = accessor.getName();
        this.annotatedElement = accessor.getAnnotatedElement();
        this.persistentStateClass = accessor.getPersistentStateClass();
        this.persistenceTargetClass = persistentStateClass;
    }

    public PersistentClassDesc getPersistentClassDesc() {
        return persistentClassDesc;
    }

    public PersistentColumn getColumn() {
        return persistentColumn;
    }

    public void setColumn(PersistentColumn column) {
        if (column == null) {
            throw new EmptyRuntimeException("column");
        }
        this.persistentColumn = column;
    }

    public String getName() {
        return persistentStateName;
    }

    public Class<?> getPersistentStateClass() {
        return persistentStateClass;
    }

    public Class<?> getPersistenceTargetClass() {
        return persistenceTargetClass;
    }

    protected void setPersistenceTargetClass(Class<?> persistenceTargetClass) {
        this.persistenceTargetClass = persistenceTargetClass;
    }
    
    public boolean isIdentifier() {
        return identifier;
    }
    
    protected void setIdentifier(boolean identifier) {
        this.identifier = identifier;
    }

    public PersistentStateAccessor getAccessor() {
        return accessor;
    }

    public PersistentClassDesc getEmbeddedClassDesc() {
        return null;
    }

    public List<PersistentStateDesc> getEmbeddedStateDescs() {
        return Collections.emptyList();
    }

    public boolean hasColumn(String columnName) {
        if (getColumn().getName() == null) {
            return false;
        }
        return this.getColumn().getName().equalsIgnoreCase(columnName);
    }

    public List<PersistentJoinColumn> getJoinColumns() {
        return Collections.emptyList();
    }
    
    public void setJoinColumns(List<PersistentJoinColumn> joinColumn) {
    }
    
    public List<PersistentJoinColumn> getForeignKeyColumns() {
        return Collections.emptyList();
    }

    public Object getValue(Object target) {
        return accessor.getValue(target);
    }

    public void setupForeignKeyColumns(PersistentClassDesc relationship) {
    }

    public void setupPrimaryKeyColumns(
            List<PersistentJoinColumn> pkJoinColumns) {
        
        if (!pkJoinColumns.isEmpty()) {
            if (hasReferencedColumnName(pkJoinColumns)) {
                adjustPkColumnsByReferencedColumnName(pkJoinColumns);
            } else {
                adjustPkColumnsByIndex(pkJoinColumns);
            }
        }
    }

    protected boolean hasReferencedColumnName(List<PersistentJoinColumn> columns) {
        for (PersistentJoinColumn column : columns) {
            if (column.getReferencedColumnName() == null) {
                return false;
            }
        }
        return true;
    }
    
    protected abstract void adjustPkColumnsByReferencedColumnName(
            List<PersistentJoinColumn> pkJoinColumns);

    protected abstract void adjustPkColumnsByIndex(
            List<PersistentJoinColumn> pkJoinColumns);
    
    public final String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("name=");
        buf.append(persistentStateName);
        buf.append(",stateClass=");
        buf.append(persistentStateClass.getName());
        return buf.toString();
    }

}
