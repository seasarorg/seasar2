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
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

import javax.persistence.Transient;

import org.seasar.framework.ejb.unit.PersistentStateAccessor;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.util.FieldUtil;

public class FieldAccessor implements PersistentStateAccessor {

    private final Field field;

    public FieldAccessor(Field field) {
        if (field == null) {
            throw new EmptyRuntimeException("field");
        }
        this.field = field;
    }

    public String getName() {
        return field.getName();
    }

    public Class<?> getPersistentStateClass() {
        return field.getType();
    }

    public Type getGenericType() {
        return field.getGenericType();
    }

    public Object getValue(Object target) {
        return FieldUtil.get(field, target);
    }

    public void setValue(Object target, Object value) {
        FieldUtil.set(field, target, value);
    }

    public boolean isPersistent() {
        return !(field.isAnnotationPresent(Transient.class)
                || field.isSynthetic()
                || Modifier.isTransient(field.getModifiers()) || Modifier
                .isStatic(field.getModifiers()));
    }

    public AnnotatedElement getAnnotatedElement() {
        return field;
    }
}
