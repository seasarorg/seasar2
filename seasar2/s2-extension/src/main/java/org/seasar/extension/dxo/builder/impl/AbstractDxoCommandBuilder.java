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
package org.seasar.extension.dxo.builder.impl;

import java.lang.reflect.Method;

import org.seasar.extension.dxo.annotation.AnnotationReader;
import org.seasar.extension.dxo.annotation.AnnotationReaderFactory;
import org.seasar.extension.dxo.builder.DxoCommandBuilder;
import org.seasar.framework.util.MethodUtil;

/**
 * @author koichik
 * 
 */
public abstract class AbstractDxoCommandBuilder implements DxoCommandBuilder {

    protected static final String REFLECTION_UTIL_CLASS_NAME = "org.seasar.framework.util.tiger.ReflectionUtil";

    protected static final Method GET_ELEMENT_TYPE_FROM_PARAMETER_METHOD = getElementTypeOfListFromParameterMethod();

    protected static final Method GET_ELEMENT_TYPE_FROM_RETURN_METHOD = getElementTypeOfListFromReturnMethod();

    protected AnnotationReaderFactory annotationReaderFactory;

    public void setAnnotationReaderFactory(
            final AnnotationReaderFactory annotationReaderFactory) {
        this.annotationReaderFactory = annotationReaderFactory;
    }

    protected AnnotationReader getAnnotationReader() {
        return annotationReaderFactory.getAnnotationReader();
    }

    public static Class getElementTypeOfListFromParameterType(
            final Method method, final int position) {
        if (GET_ELEMENT_TYPE_FROM_PARAMETER_METHOD == null) {
            return null;
        }
        return (Class) MethodUtil.invoke(
                GET_ELEMENT_TYPE_FROM_PARAMETER_METHOD, null, new Object[] {
                        method, new Integer(position) });
    }

    public static Class getElementTypeOfListFromDestination(final Method method) {
        final Class[] parameterTypes = method.getParameterTypes();
        return parameterTypes.length == 1 ? getElementTypeOfListFromReturnType(method)
                : getElementTypeOfListFromParameterType(method, 1);
    }

    public static Class getElementTypeOfListFromReturnType(final Method method) {
        if (GET_ELEMENT_TYPE_FROM_RETURN_METHOD == null) {
            return null;
        }
        return (Class) MethodUtil.invoke(GET_ELEMENT_TYPE_FROM_RETURN_METHOD,
                null, new Object[] { method });
    }

    protected static Method getElementTypeOfListFromParameterMethod() {
        try {
            final Class reflectionUtilClass = Class
                    .forName(REFLECTION_UTIL_CLASS_NAME);
            return reflectionUtilClass.getMethod(
                    "getElementTypeOfListFromParameterType", new Class[] {
                            Method.class, int.class });
        } catch (final Throwable ignore) {
        }
        return null;
    }

    protected static Method getElementTypeOfListFromReturnMethod() {
        try {
            final Class reflectionUtilClass = Class
                    .forName(REFLECTION_UTIL_CLASS_NAME);
            return reflectionUtilClass.getMethod(
                    "getElementTypeOfListFromReturnType",
                    new Class[] { Method.class });
        } catch (final Throwable ignore) {
        }
        return null;
    }

}
