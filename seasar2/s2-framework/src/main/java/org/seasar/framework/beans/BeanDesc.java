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
package org.seasar.framework.beans;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author higa
 * 
 */
public interface BeanDesc {

    public Class getBeanClass();

    public boolean hasPropertyDesc(String propertyName);

    public PropertyDesc getPropertyDesc(String propertyName)
            throws PropertyNotFoundRuntimeException;

    public PropertyDesc getPropertyDesc(int index);

    public int getPropertyDescSize();

    public boolean hasField(String fieldName);

    public Field getField(String fieldName)
            throws FieldNotFoundRuntimeException;

    public Field getField(int index);

    public Object getFieldValue(String fieldName, Object target)
            throws FieldNotFoundRuntimeException;

    public int getFieldSize();

    public Object newInstance(Object[] args)
            throws ConstructorNotFoundRuntimeException;

    public Constructor getSuitableConstructor(Object[] args)
            throws ConstructorNotFoundRuntimeException;

    public Constructor getConstructor(Class[] paramTypes);

    public String[] getConstructorParameterNames(final Class[] paramTypes);

    public String[] getConstructorParameterNames(Constructor constructor);

    public Object invoke(Object target, String methodName, Object[] args)
            throws MethodNotFoundRuntimeException;

    public Method getMethod(String methodName);

    public Method getMethod(String methodName, Class[] paramTypes);

    public Method[] getMethods(String methodName)
            throws MethodNotFoundRuntimeException;

    public boolean hasMethod(String methodName);

    public String[] getMethodNames();

    public String[] getMethodParameterNames(String methodName,
            final Class[] paramTypes);

    public String[] getMethodParameterNames(Method method);
}
