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

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collection;

import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.ejb.unit.PersistentClassDesc;
import org.seasar.framework.ejb.unit.PersistentStateDesc;
import org.seasar.framework.exception.EmptyRuntimeException;

/**
 * @author taedium
 * 
 */
public class PersistentPropertyDesc extends AbstractPersistentStateDesc
        implements PersistentStateDesc {

    private final PropertyDesc propertyDesc;

    public PersistentPropertyDesc(PersistentClassDesc persistentClassDesc,
            PropertyDesc propertyDesc, String primaryTableName) {

        super(persistentClassDesc, propertyDesc.getPropertyName(), propertyDesc.getPropertyType(),
                primaryTableName);

        if (propertyDesc == null) {
            throw new EmptyRuntimeException("propertyDesc");
        }
        this.propertyDesc = propertyDesc;

        if (Collection.class.isAssignableFrom(persistentStateType)) {
            Type type;
            if (propertyDesc.hasReadMethod()) {
                Method m = propertyDesc.getReadMethod();
                type = m.getGenericReturnType();
            } else {
                Method m = propertyDesc.getWriteMethod();
                type = m.getGenericParameterTypes()[0];
            }
            this.collectionType = extractCollectionType(type);
        }

        if (propertyDesc.hasReadMethod()) {
            introspection(propertyDesc.getReadMethod());
        } else {
            this.tableName = primaryTableName;
            this.columnName = stateName;
        }
    }

    @Override
    public Object getValue(Object target) {
        return propertyDesc.getValue(target);
    }

    @Override
    public void setValue(Object target, Object value) {
        propertyDesc.setValue(target, value);
    }

    @Override
    public boolean isProperty() {
        return true;
    }

    public PropertyDesc getPropertyDesc() {
        return propertyDesc;
    }
}
