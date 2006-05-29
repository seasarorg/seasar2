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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.ejb.unit.EmbeddedStateDesc;
import org.seasar.framework.ejb.unit.MappedSuperclassDesc;
import org.seasar.framework.ejb.unit.PersistentClassDesc;
import org.seasar.framework.ejb.unit.PersistentStateAccessor;
import org.seasar.framework.ejb.unit.PersistentStateDesc;
import org.seasar.framework.ejb.unit.PersistentStateNotFoundException;
import org.seasar.framework.exception.EmptyRuntimeException;

/**
 * @author taedium
 * 
 */
public abstract class AbstractPersistentClassDesc implements
        PersistentClassDesc {

    protected final Class<?> persistentClass;

    protected boolean propertyAccessed;

    protected List<String> tableNames = new ArrayList<String>();

    protected List<PersistentStateDesc> stateDescs = new ArrayList<PersistentStateDesc>();

    public AbstractPersistentClassDesc(Class<?> persistentClass) {
        this(persistentClass, null, false);
    }

    public AbstractPersistentClassDesc(Class<?> persistentClass,
            String primayTableName, boolean propertyAccessed) {
        if (persistentClass == null) {
            throw new EmptyRuntimeException("persistentClass");
        }
        this.persistentClass = persistentClass;
        this.propertyAccessed = propertyAccessed;
        if (primayTableName != null) {
            addTableName(primayTableName);
        }
    }

    public Class<?> getPersistentClass() {
        return persistentClass;
    }

    public boolean isPropertyAccessed() {
        return propertyAccessed;
    }

    public List<PersistentStateDesc> getPersistentStateDescs() {
        return stateDescs;
    }

    public PersistentStateDesc getPersistentStateDesc(String persistentStateName)
            throws PersistentStateNotFoundException {

        return getPersistentStateDesc(persistentClass, persistentStateName);
    }

    public PersistentStateDesc getPersistentStateDesc(Class owner,
            String persistentStateName) throws PersistentStateNotFoundException {

        for (PersistentStateDesc ps : getPersistentStateDescs()) {
            Class<?> clazz = ps.getPersistentClassDesc().getPersistentClass();
            if (clazz == owner && ps.getName().equals(persistentStateName)) {
                return ps;
            }
        }
        throw new PersistentStateNotFoundException(
                propertyAccessed ? "ESSR0503" : "ESSR0502", owner,
                persistentStateName);
    }

    public List<PersistentStateDesc> getPersistentStateDescsByTableName(
            String tableName) {

        List<PersistentStateDesc> stateDescs = new ArrayList<PersistentStateDesc>();
        for (PersistentStateDesc ps : getPersistentStateDescs()) {
            String table = ps.getColumn().getTable();
            if (table.equalsIgnoreCase(tableName)) {
                stateDescs.add(ps);
            }
        }
        return stateDescs;
    }

    public List<PersistentStateDesc> getIdentifiers() {
        List<PersistentStateDesc> result = new ArrayList<PersistentStateDesc>();
        for (PersistentStateDesc stateDesc : stateDescs) {
            if (stateDesc.isIdentifier()) {
                PersistentClassDesc ownerClass = stateDesc
                        .getPersistentClassDesc();
                if (ownerClass == this
                        || ownerClass instanceof MappedSuperclassDesc) {
                    result.add(stateDesc);
                }
            }
        }
        return result;
    }

    public String getPrimaryTableName() {
        return tableNames.get(0);
    }

    public List<String> getTableNames() {
        return tableNames;
    }

    public boolean hasEmbeddedId() {
        PersistentStateDesc id = getIdentifiers().get(0);
        if (id instanceof EmbeddedStateDesc) {
            return true;
        }
        return false;
    }

    public EmbeddedStateDesc getEmbeddedId() {
        return (EmbeddedStateDesc) getIdentifiers().get(0);
    }

    protected void setupPersistentStateDescs() {
        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(persistentClass);
        if (propertyAccessed) {
            for (int i = 0; i < beanDesc.getPropertyDescSize(); i++) {
                PropertyDesc propDesc = beanDesc.getPropertyDesc(i);
                if (!propDesc.hasReadMethod()) {
                    continue;
                }
                Method readMethod = propDesc.getReadMethod();
                if (readMethod.getDeclaringClass() == persistentClass) {
                    PersistentStateAccessor accessor = new PropertyAccessor(
                            propDesc, readMethod);
                    setupPersistentStateDesc(accessor);
                }
            }
        } else {
            for (int i = 0; i < beanDesc.getFieldSize(); i++) {
                Field field = beanDesc.getField(i);
                if (field.getDeclaringClass() == persistentClass) {
                    PersistentStateAccessor accessor = new FieldAccessor(field);
                    setupPersistentStateDesc(accessor);
                }
            }
        }
    }

    private void setupPersistentStateDesc(PersistentStateAccessor accessor) {
        PersistentStateDesc ps = PersistentStateDescFactory
                .getPersistentStateDesc(this, getPrimaryTableName(), accessor);
        if (ps != null) {
            addPersistentStateDesc(ps);
        }
    }

    protected void addPersistentStateDesc(PersistentStateDesc ps) {
        stateDescs.add(ps);
    }

    protected void addTableName(String tableName) {
        if (!tableNames.contains(tableName.toLowerCase())) {
            tableNames.add(tableName.toLowerCase());
        }
    }

    protected void setPropertyAccessed(boolean propertyAccessed) {
        this.propertyAccessed = propertyAccessed;
    }

}
