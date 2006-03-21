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
import java.lang.reflect.Type;
import java.util.Collection;

import org.seasar.framework.ejb.unit.PersistentClassDesc;
import org.seasar.framework.ejb.unit.PersistentStateDesc;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.util.FieldUtil;

/**
 * @author taedium
 * 
 */
public class PersistentFieldDesc extends AbstractPersistentStateDesc implements
        PersistentStateDesc {

    private final Field field;

    public PersistentFieldDesc(PersistentClassDesc persistentClassDesc, Field field, String primaryTableName) {
        super(persistentClassDesc, field.getName(), field.getType(), primaryTableName);
        if (field == null) {
            throw new EmptyRuntimeException("field");
        }
        this.field = field;
        if (Collection.class.isAssignableFrom(field.getType())) {
            Type type = field.getGenericType();
            this.collectionType = extractCollectionType(type);
        }
        introspection(field);
    }

    @Override
    public Object getValue(Object target) {
        return FieldUtil.get(field, target);
    }

    @Override
    public void setValue(Object target, Object value) {
        FieldUtil.set(field, target, value);
    }

    @Override
    public boolean isProperty() {
        return false;
    }

    public Field getField() {
        return field;
    }
}
