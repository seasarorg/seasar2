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
import org.seasar.framework.ejb.unit.PersistentClassDesc;
import org.seasar.framework.ejb.unit.PersistentStateAccessor;
import org.seasar.framework.ejb.unit.PersistentStateDesc;
import org.seasar.framework.ejb.unit.PersistentStateNotFoundException;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.util.ArrayMap;

/**
 * @author taedium
 * 
 */
public abstract class AbstractPersistentClassDesc implements
        PersistentClassDesc {

    protected final Class<?> persistentClass;

    protected boolean propertyAccessed;

    protected List<String> tableNames = new ArrayList<String>();

    protected List<PersistentStateDesc> identifiers = new ArrayList<PersistentStateDesc>();

    protected ArrayMap stateDescs = new ArrayMap();

    public AbstractPersistentClassDesc(Class<?> persistentClass) {
        if (persistentClass == null) {
            throw new EmptyRuntimeException("persistentClass");
        }
        this.persistentClass = persistentClass;
    }

    public Class<?> getPersistentClass() {
        return persistentClass;
    }

    public PersistentStateDesc getPersistentStateDesc(int index) {
        return (PersistentStateDesc)stateDescs.get(index);
    }

    public boolean hasPersistentStateDesc(String pathName) {
        return stateDescs.containsKey(pathName);
    }

    public PersistentStateDesc getPersistentStateDesc(String pathName)
            throws PersistentStateNotFoundException {

        if (stateDescs.containsKey(pathName)) {
            return (PersistentStateDesc) stateDescs.get(pathName);
        }
        throw new PersistentStateNotFoundException(persistentClass, pathName,
                propertyAccessed);
    }

    public int getPersistentStateDescSize() {
        return stateDescs.size();
    }

    public String getTableName(int index) {
        return tableNames.get(index).toUpperCase();
    }

    public int getTableSize() {
        return tableNames.size();
    }

    public boolean isPropertyAccessed() {
        return propertyAccessed;
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
                    if (accessor.isPersistent()) {
                        PersistentStateDesc ps = new PersistentStateDescImpl(
                                this, tableNames.get(0), accessor);
                        setupPersistentStateDescs(ps);
                    }
                }
            }
        } else {
            for (int i = 0; i < beanDesc.getFieldSize(); i++) {
                Field field = beanDesc.getField(i);
                if (field.getDeclaringClass() == persistentClass) {
                    PersistentStateAccessor accessor = new FieldAccessor(field);
                    if (accessor.isPersistent()) {
                        PersistentStateDesc ps = new PersistentStateDescImpl(
                                this, tableNames.get(0), accessor);
                        setupPersistentStateDescs(ps);
                    }
                }
            }
        }
    }

    protected void setupPersistentStateDescs(PersistentStateDesc ps) {
        stateDescs.put(ps.getPathName(), ps);
        if (ps.isIdentifier()) {
            identifiers.add(ps);
        }
    }

    public List<PersistentStateDesc> getIdentifiers() {
        return identifiers;
    }

}
