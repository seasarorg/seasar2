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
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

import javax.persistence.Transient;

import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.ejb.unit.PersistentStateAccessor;
import org.seasar.framework.exception.EmptyRuntimeException;

public class PropertyAccessor implements PersistentStateAccessor {

    private final PropertyDesc propDesc;

    private final Method readMethod;

    public PropertyAccessor(PropertyDesc propDesc, Method readMethod) {
        if (propDesc == null) {
            throw new EmptyRuntimeException("propDesc");
        }
        if (readMethod == null) {
            throw new EmptyRuntimeException("readMethod");
        }
        this.propDesc = propDesc;
        this.readMethod = readMethod;
    }

    public String getName() {
        return propDesc.getPropertyName();
    }

    public Class<?> getPersistentStateType() {
        return propDesc.getPropertyType();
    }

    public Type getGenericType() {
        return readMethod.getGenericReturnType();
    }

    public Object getValue(Object target) {
        return propDesc.getValue(target);
    }

    public void setValue(Object target, Object value) {
        propDesc.setValue(target, value);
    }

    public boolean isPersistent() {
        return !(readMethod.isAnnotationPresent(Transient.class)
                || readMethod.isSynthetic() || readMethod.isBridge() || Modifier
                .isStatic(readMethod.getModifiers()));
    }

    public AnnotatedElement getAnnotatedElement() {
        return readMethod;
    }
}
