/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.seasar.framework.exception.IllegalAccessRuntimeException;
import org.seasar.framework.exception.SIllegalArgumentException;

/**
 * {@link Field}用のユーティリティクラスです。
 * 
 * @author higa
 * 
 */
public final class FieldUtil {

    private FieldUtil() {
    }

    /**
     * {@link Field}の値をオブジェクトとして取得します。
     * 
     * @param field
     * @param target
     * @return {@link Object}
     * @throws IllegalAccessRuntimeException
     *             {@link IllegalAccessException}がおきた場合
     * @see Field#get(Object)
     */
    public static Object get(Field field, Object target)
            throws IllegalAccessRuntimeException {

        try {
            return field.get(target);
        } catch (IllegalAccessException ex) {
            throw new IllegalAccessRuntimeException(field.getDeclaringClass(),
                    ex);
        }
    }

    /**
     * staticな {@link Field}の値をintとして取得します。
     * 
     * @param field
     * @return intの値
     * @throws IllegalAccessRuntimeException
     *             {@link IllegalAccessException}が発生した場合
     * @see #getInt(Field, Object)
     */
    public static int getInt(Field field) throws IllegalAccessRuntimeException {
        return getInt(field, null);
    }

    /**
     * {@link Field}の値をintとして取得します。
     * 
     * @param field
     * @param target
     * @return intの値
     * @throws IllegalAccessRuntimeException
     *             {@link IllegalAccessException}が発生した場合
     * @see Field#getInt(Object)
     */
    public static int getInt(Field field, Object target)
            throws IllegalAccessRuntimeException {
        try {
            return field.getInt(target);
        } catch (IllegalAccessException ex) {
            throw new IllegalAccessRuntimeException(field.getDeclaringClass(),
                    ex);
        }
    }

    /**
     * staticな {@link Field}の値を {@link String}として取得します。
     * 
     * @param field
     * @return {@link String}の値
     * @throws IllegalAccessRuntimeException
     *             {@link IllegalAccessException}が発生した場合
     * @see #getString(Field, Object)
     */
    public static String getString(Field field)
            throws IllegalAccessRuntimeException {
        return getString(field, null);
    }

    /**
     * {@link Field}の値を {@link String}として取得します。
     * 
     * @param field
     * @param target
     * @return {@link String}の値
     * @throws IllegalAccessRuntimeException
     *             {@link IllegalAccessException}が発生した場合
     * @see Field#get(Object)
     */
    public static String getString(Field field, Object target)
            throws IllegalAccessRuntimeException {

        try {
            return (String) field.get(target);
        } catch (IllegalAccessException ex) {
            throw new IllegalAccessRuntimeException(field.getDeclaringClass(),
                    ex);
        }
    }

    /**
     * {@link Field}に値を設定します。
     * 
     * @param field
     * @param target
     * @param value
     * @throws IllegalAccessRuntimeException
     *             {@link IllegalAccessException}が発生した場合
     * @see Field#set(Object, Object)
     */
    public static void set(Field field, Object target, Object value)
            throws IllegalAccessRuntimeException {

        try {
            field.set(target, value);
        } catch (IllegalAccessException e) {
            throw new IllegalAccessRuntimeException(field.getDeclaringClass(),
                    e);
        } catch (IllegalArgumentException e) {
            throw new SIllegalArgumentException("ESSR0094",
                    new Object[] { field.getDeclaringClass().getName(),
                            field.getName(), value }, e);
        }

    }

    /**
     * インスタンスフィールドかどうか返します。
     * 
     * @param field
     * @return インスタンスフィールドかどうか
     */
    public static boolean isInstanceField(Field field) {
        int mod = field.getModifiers();
        return !Modifier.isStatic(mod) && !Modifier.isFinal(mod);
    }

    /**
     * パブリックフィールドかどうか返します。
     * 
     * @param field
     * @return パブリックフィールドかどうか
     */
    public static boolean isPublicField(Field field) {
        int mod = field.getModifiers();
        return Modifier.isPublic(mod);
    }
}
