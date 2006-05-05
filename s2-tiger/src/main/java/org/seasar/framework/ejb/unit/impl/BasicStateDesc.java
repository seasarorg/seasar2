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

import static javax.persistence.EnumType.ORDINAL;

import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.Id;

import org.seasar.framework.ejb.unit.PersistentClassDesc;
import org.seasar.framework.ejb.unit.PersistentColumn;
import org.seasar.framework.ejb.unit.PersistentStateAccessor;
import org.seasar.framework.ejb.unit.PersistentStateType;
import org.seasar.framework.util.StringUtil;

/**
 * @author taedium
 * 
 */
public class BasicStateDesc extends AbstractPersistentStateDesc {

    public BasicStateDesc(PersistentClassDesc persistentClassDesc,
            String primaryTableName, PersistentStateAccessor accessor) {
        
        super(persistentClassDesc, primaryTableName, accessor);
        introspect();
    }
    
    protected void introspect() {
        identifier = annotatedElement.isAnnotationPresent(Id.class);
        setupPersistentColumn();
        setupPersistenceTargetClass();
    }

    private void setupPersistentColumn() {
        Column column = annotatedElement.getAnnotation(Column.class);
        if (column != null) {
            String tableName = StringUtil.isEmpty(column.table()) ? primaryTableName
                    : column.table();
            String columnName = StringUtil.isEmpty(column.name()) ? persistentStateName
                    : column.name();
            persistentColumn = new PersistentColumn(columnName, tableName);
        } else {
            persistentColumn = new PersistentColumn(persistentStateName, primaryTableName);
        }
    }
    
    private void setupPersistenceTargetClass() {
        Enumerated enumerated = annotatedElement.getAnnotation(Enumerated.class);
        if (Enum.class.isAssignableFrom(persistentStateClass)) {
            if (enumerated == null || enumerated.value() == ORDINAL) {
                persistenceTargetClass = int.class;
            } else {
                persistenceTargetClass = String.class;
            }
        }
    }
    
    @Override
    public Object getValue(Object target) {
        Object value = super.getValue(target);
        if (value instanceof Enum) {
            Enum enumValue = (Enum) value;
            if (persistenceTargetClass == int.class) {
                return enumValue.ordinal();
            } else {
                return enumValue.toString();
            }
        }
        return value;
    }

    @Override
    public PersistentStateType getPersistentStateType() {
        return PersistentStateType.BASIC;
    }

}
