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

}