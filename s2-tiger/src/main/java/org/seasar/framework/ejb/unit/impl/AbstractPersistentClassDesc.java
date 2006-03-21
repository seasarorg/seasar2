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
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Transient;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.ejb.unit.PersistentClassDesc;
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

    protected List<PersistentStateDesc> identifiers = new ArrayList<PersistentStateDesc>();

    protected List<PersistentStateDesc> stateDescs = new ArrayList<PersistentStateDesc>();

    protected Map<String, PersistentStateDesc> stateDescsByPathName = new HashMap<String, PersistentStateDesc>();

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
        return stateDescs.get(index);
    }

    public boolean hasPersistentStateDesc(String pathName) {
        return stateDescsByPathName.containsKey(pathName);
    }

    public PersistentStateDesc getPersistentStateDesc(String pathName)
            throws PersistentStateNotFoundException {
        if (stateDescsByPathName.containsKey(pathName)) {
            return stateDescsByPathName.get(pathName);
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

    public boolean hasReferencedStateDesc(String columnName) {

        for (PersistentStateDesc steateDesc : stateDescs) {
            if (steateDesc.hasReferencedColumn(columnName)) {
                return true;
            }
        }
        return false;
    }

    public PersistentStateDesc getReferencedStateDesc(String columnName) {

        for (PersistentStateDesc steateDesc : stateDescs) {
            if (steateDesc.hasReferencedColumn(columnName)) {
                return steateDesc;
            }
        }
        return null;
    }

    protected void setupPersistentStateDescs() {
        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(persistentClass);
        if (propertyAccessed) {
            setupPersistentPropertyDesc(beanDesc);
        } else {
            setupPersistentFieldDesc(beanDesc);
        }
    }

    protected void setupPersistentFieldDesc(BeanDesc beanDesc) {

        for (int i = 0; i < beanDesc.getFieldSize(); i++) {
            Field field = beanDesc.getField(i);
            if (field.getDeclaringClass() == persistentClass
                    && isPersistent(field)) {
                PersistentStateDesc ps = new PersistentFieldDesc(this, field,
                        tableNames.get(0));
                setupPersistentStateDescs(ps);
            }
        }
    }

    protected boolean isPersistent(Field field) {
        if (field.isAnnotationPresent(Transient.class) || field.isSynthetic()
                || Modifier.isTransient(field.getModifiers())
                || Modifier.isStatic(field.getModifiers())) {
            return false;
        }
        return true;
    }

    protected void setupPersistentPropertyDesc(BeanDesc beanDesc) {

        for (int i = 0; i < beanDesc.getPropertyDescSize(); i++) {
            PropertyDesc propDesc = beanDesc.getPropertyDesc(i);
            if (propDesc.hasReadMethod()) {
                Method method = propDesc.getReadMethod();
                if (method.getDeclaringClass() == persistentClass
                        && isPersistent(method)) {
                    PersistentStateDesc ps = new PersistentPropertyDesc(this,
                            propDesc, tableNames.get(0));
                    setupPersistentStateDescs(ps);
                }
            }
        }
    }

    protected boolean isPersistent(Method method) {
        if (method.isAnnotationPresent(Transient.class) || method.isSynthetic()
                || method.isBridge()
                || Modifier.isStatic(method.getModifiers())) {
            return false;
        }
        return true;
    }

    protected void setupPersistentStateDescs(PersistentStateDesc ps) {
        stateDescs.add(ps);
        stateDescsByPathName.put(ps.getPathName(), ps);
        if (ps.isIdentifier()) {
            identifiers.add(ps);
        }
    }

    public List<PersistentStateDesc> getIdentifiers() {
        return identifiers;
    }
}
