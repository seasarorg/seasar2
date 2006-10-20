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
package org.seasar.framework.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.seasar.framework.exception.IllegalAccessRuntimeException;
import org.seasar.framework.exception.InstantiationRuntimeException;
import org.seasar.framework.exception.InvocationTargetRuntimeException;

/**
 * @author higa
 * 
 */
public final class MethodUtil {

    private static final Method IS_BRIDGE_METHOD = getIsBridgeMethod();

    private static final Method IS_SYNTHETIC_METHOD = getIsSyntheticMethod();

    protected static final String REFLECTION_UTIL_CLASS_NAME = "org.seasar.framework.util.tiger.ReflectionUtil";

    protected static final Method GET_ELEMENT_TYPE_FROM_PARAMETER_METHOD = getElementTypeOfListFromParameterMethod();

    protected static final Method GET_ELEMENT_TYPE_FROM_RETURN_METHOD = getElementTypeOfListFromReturnMethod();

    private MethodUtil() {
    }

    public static Object invoke(Method method, Object target, Object[] args)
            throws InstantiationRuntimeException, IllegalAccessRuntimeException {

        try {
            return method.invoke(target, args);
        } catch (InvocationTargetException ex) {
            Throwable t = ex.getCause();
            if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            }
            if (t instanceof Error) {
                throw (Error) t;
            }
            throw new InvocationTargetRuntimeException(method
                    .getDeclaringClass(), ex);
        } catch (IllegalAccessException ex) {
            throw new IllegalAccessRuntimeException(method.getDeclaringClass(),
                    ex);
        }
    }

    public static boolean isAbstract(Method method) {
        int mod = method.getModifiers();
        return Modifier.isAbstract(mod);
    }

    public static String getSignature(String methodName, Class[] argTypes) {
        StringBuffer buf = new StringBuffer(100);
        buf.append(methodName);
        buf.append("(");
        if (argTypes != null) {
            for (int i = 0; i < argTypes.length; ++i) {
                if (i > 0) {
                    buf.append(", ");
                }
                buf.append(argTypes[i].getName());
            }
        }
        buf.append(")");
        return buf.toString();
    }

    public static String getSignature(String methodName, Object[] methodArgs) {
        StringBuffer buf = new StringBuffer(100);
        buf.append(methodName);
        buf.append("(");
        if (methodArgs != null) {
            for (int i = 0; i < methodArgs.length; ++i) {
                if (i > 0) {
                    buf.append(", ");
                }
                if (methodArgs[i] != null) {
                    buf.append(methodArgs[i].getClass().getName());
                } else {
                    buf.append("null");
                }
            }
        }
        buf.append(")");
        return buf.toString();
    }

    public static boolean isEqualsMethod(Method method) {
        return method != null && method.getName().equals("equals")
                && method.getReturnType() == boolean.class
                && method.getParameterTypes().length == 1
                && method.getParameterTypes()[0] == Object.class;
    }

    public static boolean isHashCodeMethod(Method method) {
        return method != null && method.getName().equals("hashCode")
                && method.getReturnType() == int.class
                && method.getParameterTypes().length == 0;
    }

    public static boolean isToStringMethod(Method method) {
        return method != null && method.getName().equals("toString")
                && method.getReturnType() == String.class
                && method.getParameterTypes().length == 0;
    }

    public static boolean isBridgeMethod(final Method method) {
        if (IS_BRIDGE_METHOD == null) {
            return false;
        }
        return ((Boolean) MethodUtil.invoke(IS_BRIDGE_METHOD, method, null))
                .booleanValue();
    }

    public static boolean isSyntheticMethod(final Method method) {
        if (IS_SYNTHETIC_METHOD == null) {
            return false;
        }
        return ((Boolean) MethodUtil.invoke(IS_SYNTHETIC_METHOD, method, null))
                .booleanValue();
    }

    private static Method getIsBridgeMethod() {
        try {
            return Method.class.getMethod("isBridge", null);
        } catch (final NoSuchMethodException e) {
            return null;
        }
    }

    private static Method getIsSyntheticMethod() {
        try {
            return Method.class.getMethod("isSynthetic", null);
        } catch (final NoSuchMethodException e) {
            return null;
        }
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