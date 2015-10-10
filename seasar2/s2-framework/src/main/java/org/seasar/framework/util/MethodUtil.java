/*
 * Copyright 2004-2015 the Seasar Foundation and the Others.
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
import org.seasar.framework.exception.InvocationTargetRuntimeException;

/**
 * {@link Method}用のユーティリティクラスです。
 * 
 * @author higa
 * 
 */
public class MethodUtil {

    private static final Method IS_BRIDGE_METHOD = getMethod("isBridge");

    private static final Method IS_SYNTHETIC_METHOD = getMethod("isSynthetic");

    private static final Method IS_DEFAULT_METHOD = getMethod("isDefault");

    /**
     * ReflectUtilのクラス名です。
     */
    protected static final String REFLECTION_UTIL_CLASS_NAME = "org.seasar.framework.util.tiger.ReflectionUtil";

    /**
     * {@link #getElementTypeOfCollectionFromParameterType(Method, int)}への定数参照です
     */
    protected static final Method GET_ELEMENT_TYPE_OF_COLLECTION_FROM_PARAMETER_METHOD = getElementTypeFromParameterMethod("Collection");

    /**
     * {@link #getElementTypeOfCollectionFromReturnType(Method)}への定数参照です。
     */
    protected static final Method GET_ELEMENT_TYPE_OF_COLLECTION_FROM_RETURN_METHOD = getElementTypeFromReturnMethod("Collection");

    /**
     * {@link #getElementTypeOfListFromParameterType(Method, int)}への定数参照です
     */
    protected static final Method GET_ELEMENT_TYPE_OF_LIST_FROM_PARAMETER_METHOD = getElementTypeFromParameterMethod("List");

    /**
     * {@link #getElementTypeOfListFromReturnType(Method)}への定数参照です。
     */
    protected static final Method GET_ELEMENT_TYPE_OF_LIST_FROM_RETURN_METHOD = getElementTypeFromReturnMethod("List");

    /**
     * {@link #getElementTypeOfSetFromParameterType(Method, int)}への定数参照です
     */
    protected static final Method GET_ELEMENT_TYPE_OF_SET_FROM_PARAMETER_METHOD = getElementTypeFromParameterMethod("Set");

    /**
     * {@link #getElementTypeOfSetFromReturnType(Method)}への定数参照です。
     */
    protected static final Method GET_ELEMENT_TYPE_OF_SET_FROM_RETURN_METHOD = getElementTypeFromReturnMethod("Set");

    /**
     * インスタンスを構築します。
     */
    protected MethodUtil() {
    }

    /**
     * {@link Method#invoke(Object, Object[])}の例外処理をラップします。
     * 
     * @param method
     * @param target
     * @param args
     * @return 戻り値
     * @throws InvocationTargetRuntimeException
     *             {@link InvocationTargetException}が発生した場合
     * @throws IllegalAccessRuntimeException
     *             {@link IllegalAccessException}が発生した場合
     * @see Method#invoke(Object, Object[])
     */
    public static Object invoke(Method method, Object target, Object[] args)
            throws InvocationTargetRuntimeException,
            IllegalAccessRuntimeException {

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

    /**
     * <code>abstract</code>かどうかを返します。
     * 
     * @param method
     * @return <code>abstract</code>かどうか
     */
    public static boolean isAbstract(Method method) {
        int mod = method.getModifiers();
        return Modifier.isAbstract(mod);
    }

    /**
     * シグニチャを返します。
     * 
     * @param methodName
     * @param argTypes
     * @return シグニチャ
     */
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

    /**
     * シグニチャを返します。
     * 
     * @param methodName
     * @param methodArgs
     * @return シグニチャ
     */
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

    /**
     * equalsメソッドかどうかを返します。
     * 
     * @param method
     * @return equalsメソッドかどうか
     */
    public static boolean isEqualsMethod(Method method) {
        return method != null && method.getName().equals("equals")
                && method.getReturnType() == boolean.class
                && method.getParameterTypes().length == 1
                && method.getParameterTypes()[0] == Object.class;
    }

    /**
     * hashCodeメソッドかどうか返します。
     * 
     * @param method
     * @return hashCodeメソッドかどうか
     */
    public static boolean isHashCodeMethod(Method method) {
        return method != null && method.getName().equals("hashCode")
                && method.getReturnType() == int.class
                && method.getParameterTypes().length == 0;
    }

    /**
     * toStringメソッドかどうか返します。
     * 
     * @param method
     * @return toStringメソッドかどうか
     */
    public static boolean isToStringMethod(Method method) {
        return method != null && method.getName().equals("toString")
                && method.getReturnType() == String.class
                && method.getParameterTypes().length == 0;
    }

    /**
     * ブリッジメソッドかどうか返します。
     * 
     * @param method
     * @return ブリッジメソッドかどうか
     */
    public static boolean isBridgeMethod(final Method method) {
        if (IS_BRIDGE_METHOD == null) {
            return false;
        }
        return ((Boolean) MethodUtil.invoke(IS_BRIDGE_METHOD, method, null))
                .booleanValue();
    }

    /**
     * 合成メソッドかどうかを返します。
     * 
     * @param method
     * @return 合成メソッドかどうか
     */
    public static boolean isSyntheticMethod(final Method method) {
        if (IS_SYNTHETIC_METHOD == null) {
            return false;
        }
        return ((Boolean) MethodUtil.invoke(IS_SYNTHETIC_METHOD, method, null))
                .booleanValue();
    }

    /**
     * デフォルトメソッドかどうか返します。
     * 
     * @param method
     * @return デフォルトメソッドかどうか
     */
    public static boolean isDefaultMethod(final Method method) {
        if (IS_DEFAULT_METHOD == null) {
            return false;
        }
        return ((Boolean) MethodUtil.invoke(IS_DEFAULT_METHOD, method, null))
                .booleanValue();
    }

    private static Method getMethod(final String name) {
        try {
            return Method.class.getMethod(name, null);
        } catch (final NoSuchMethodException e) {
            return null;
        }
    }

    /**
     * Java5以上の場合は、メソッドの引数型 (パラメタ化されたコレクション) の要素型を返します。
     * 
     * @param method
     *            メソッド
     * @param position
     *            パラメタ化されたコレクションが宣言されているメソッド引数の位置
     * @return 指定されたメソッドの引数型として宣言されているパラメタ化されたコレクションの要素型
     */
    public static Class getElementTypeOfCollectionFromParameterType(
            final Method method, final int position) {
        if (GET_ELEMENT_TYPE_OF_COLLECTION_FROM_PARAMETER_METHOD == null) {
            return null;
        }
        return (Class) MethodUtil.invoke(
                GET_ELEMENT_TYPE_OF_COLLECTION_FROM_PARAMETER_METHOD, null,
                new Object[] { method, new Integer(position) });
    }

    /**
     * 指定されたメソッドの戻り値型として宣言されているパラメタ化されたコレクションの要素型を返します。
     * 
     * @param method
     *            メソッド
     * @return 指定されたメソッドの戻り値型として宣言されているパラメタ化されたコレクションの要素型
     */
    public static Class getElementTypeOfCollectionFromReturnType(
            final Method method) {
        if (GET_ELEMENT_TYPE_OF_COLLECTION_FROM_RETURN_METHOD == null) {
            return null;
        }
        return (Class) MethodUtil.invoke(
                GET_ELEMENT_TYPE_OF_COLLECTION_FROM_RETURN_METHOD, null,
                new Object[] { method });
    }

    /**
     * Java5以上の場合は、メソッドの引数型 (パラメタ化されたリスト) の要素型を返します。
     * 
     * @param method
     *            メソッド
     * @param position
     *            パラメタ化されたリストが宣言されているメソッド引数の位置
     * @return 指定されたメソッドの引数型として宣言されているパラメタ化されたリストの要素型
     */
    public static Class getElementTypeOfListFromParameterType(
            final Method method, final int position) {
        if (GET_ELEMENT_TYPE_OF_LIST_FROM_PARAMETER_METHOD == null) {
            return null;
        }
        return (Class) MethodUtil.invoke(
                GET_ELEMENT_TYPE_OF_LIST_FROM_PARAMETER_METHOD, null,
                new Object[] { method, new Integer(position) });
    }

    /**
     * 指定されたメソッドの戻り値型として宣言されているパラメタ化されたリストの要素型を返します。
     * 
     * @param method
     *            メソッド
     * @return 指定されたメソッドの戻り値型として宣言されているパラメタ化されたリストの要素型
     */
    public static Class getElementTypeOfListFromReturnType(final Method method) {
        if (GET_ELEMENT_TYPE_OF_LIST_FROM_RETURN_METHOD == null) {
            return null;
        }
        return (Class) MethodUtil.invoke(
                GET_ELEMENT_TYPE_OF_LIST_FROM_RETURN_METHOD, null,
                new Object[] { method });
    }

    /**
     * Java5以上の場合は、メソッドの引数型 (パラメタ化されたセット) の要素型を返します。
     * 
     * @param method
     *            メソッド
     * @param position
     *            パラメタ化されたコレクションが宣言されているメソッド引数の位置
     * @return 指定されたメソッドの引数型として宣言されているパラメタ化されたセットの要素型
     */
    public static Class getElementTypeOfSetFromParameterType(
            final Method method, final int position) {
        if (GET_ELEMENT_TYPE_OF_SET_FROM_PARAMETER_METHOD == null) {
            return null;
        }
        return (Class) MethodUtil.invoke(
                GET_ELEMENT_TYPE_OF_SET_FROM_PARAMETER_METHOD, null,
                new Object[] { method, new Integer(position) });
    }

    /**
     * 指定されたメソッドの戻り値型として宣言されているパラメタ化されたセットの要素型を返します。
     * 
     * @param method
     *            メソッド
     * @return 指定されたメソッドの戻り値型として宣言されているパラメタ化されたセットの要素型
     */
    public static Class getElementTypeOfSetFromReturnType(final Method method) {
        if (GET_ELEMENT_TYPE_OF_SET_FROM_RETURN_METHOD == null) {
            return null;
        }
        return (Class) MethodUtil.invoke(
                GET_ELEMENT_TYPE_OF_SET_FROM_RETURN_METHOD, null,
                new Object[] { method });
    }

    /**
     * <code>ReflectionUtil#getElementTypeOf<var>Xxx</var>FromParameter</code>の
     * {@link Method}を返します。
     * 
     * @param type
     *            取得するメソッドが対象とする型名
     * 
     * @return {@link Method}
     */
    protected static Method getElementTypeFromParameterMethod(final String type) {
        try {
            final Class reflectionUtilClass = Class
                    .forName(REFLECTION_UTIL_CLASS_NAME);
            return reflectionUtilClass.getMethod("getElementTypeOf" + type
                    + "FromParameterType", new Class[] { Method.class,
                    int.class });
        } catch (final Throwable ignore) {
        }
        return null;
    }

    /**
     * <code>ReflectionUtil#getElementTypeOf<var>Xxx</var>FromReturn</code>の
     * {@link Method}を返します。
     * 
     * @param type
     *            取得するメソッドが対象とする型名
     * @return {@link Method}
     */
    protected static Method getElementTypeFromReturnMethod(final String type) {
        try {
            final Class reflectionUtilClass = Class
                    .forName(REFLECTION_UTIL_CLASS_NAME);
            return reflectionUtilClass.getMethod("getElementTypeOf" + type
                    + "FromReturnType", new Class[] { Method.class });
        } catch (final Throwable ignore) {
        }
        return null;
    }

}