/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
package org.seasar.extension.jdbc.gen.internal.argtype;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.tiger.ReflectionUtil;

/**
 * {@link ArgumentType}を管理するクラスです。
 * 
 * @author taedium
 */
public class ArgumentTypeRegistry {

    /** 引数の値のクラスをキー、 {@link ArgumentType}のコンストラクタを値とするマップ */
    protected static Map<Class<?>, Constructor<? extends ArgumentType<?>>> argTypeMap = new ConcurrentHashMap<Class<?>, Constructor<? extends ArgumentType<?>>>();
    static {
        argTypeMap.put(Boolean.class, ReflectionUtil.getConstructor(
                BooleanType.class, null));
        argTypeMap.put(Character.class, ReflectionUtil.getConstructor(
                CharacterType.class, null));
        argTypeMap.put(String.class, ReflectionUtil.getConstructor(
                StringType.class, null));
        argTypeMap.put(File.class, ReflectionUtil.getConstructor(
                FileType.class, null));
        argTypeMap.put(Class.class, ReflectionUtil.getConstructor(
                ClassType.class, null));
    }

    /**
     * {@link ArgumentType}を返します。
     * 
     * @param <T>
     *            引数の値の型
     * @param propertyDesc
     *            プロパティ記述
     * @return {@link ArgumentType}
     */
    public static <T> ArgumentType<T> getArgumentType(PropertyDesc propertyDesc) {
        Class<?> propertyType = propertyDesc.getPropertyType();
        if (propertyDesc.isParameterized()
                && Collection.class.isAssignableFrom(propertyType)) {
            Class<?> elementClass = propertyDesc.getElementClassOfCollection();
            return getCollectionArgumentType(propertyType, elementClass);
        }
        return getArgumentType(propertyType);
    }

    /**
     * {@link ArgumentType}を返します。
     * 
     * @param <T>
     *            引数の値の型
     * @param clazz
     *            引数の値のクラス
     * @return {@link ArgumentType}
     */
    @SuppressWarnings("unchecked")
    protected static <T> ArgumentType<T> getArgumentType(Class<?> clazz) {
        clazz = ClassUtil.getWrapperClassIfPrimitive(clazz);
        if (argTypeMap.containsKey(clazz)) {
            Constructor<? extends ArgumentType<?>> constructor = argTypeMap
                    .get(clazz);
            return (ArgumentType<T>) ReflectionUtil.newInstance(constructor,
                    null);
        }
        if (Number.class.isAssignableFrom(clazz)) {
            return new NumberType(clazz);
        }
        if (Enum.class.isAssignableFrom(clazz)) {
            return new EnumType(clazz);
        }
        return null;
    }

    /**
     * {@link ArgumentType}を返します。
     * 
     * @param <T>
     *            引数の値の型
     * @param collectioinClass
     *            コレクションのクラス
     * @param elementClass
     *            コレクションの要素のクラス
     * @return {@link ArgumentType}を返します。
     */
    @SuppressWarnings("unchecked")
    protected static <T> ArgumentType<T> getCollectionArgumentType(
            Class<?> collectioinClass, Class<?> elementClass) {
        ArgumentType<?> argumentType = getArgumentType(elementClass);
        if (List.class.isAssignableFrom(collectioinClass)) {
            return new ListType(argumentType);
        }
        return null;
    }

    /**
     * {@link ArgumentType}を登録します。
     * 
     * @param clazz
     *            引数の値のクラス
     */
    protected static void register(Class<? extends ArgumentType<?>> clazz) {
        if (clazz == null) {
            throw new NullPointerException("clazz");
        }
        Constructor<? extends ArgumentType<?>> constructor = ReflectionUtil
                .getConstructor(clazz, null);
        argTypeMap.put(clazz, constructor);
    }

    /**
     * {@link ArgumentType}を削除します。
     * 
     * @param clazz
     *            引数の値のクラス
     */
    protected static void deregister(Class<? extends ArgumentType<?>> clazz) {
        if (clazz == null) {
            throw new NullPointerException("clazz");
        }
        argTypeMap.remove(clazz);
    }
}
