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
package org.seasar.framework.util.tiger;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import org.seasar.framework.exception.ClassNotFoundRuntimeException;
import org.seasar.framework.exception.IllegalAccessRuntimeException;
import org.seasar.framework.exception.InstantiationRuntimeException;
import org.seasar.framework.exception.InvocationTargetRuntimeException;
import org.seasar.framework.exception.NoSuchConstructorRuntimeException;
import org.seasar.framework.exception.NoSuchFieldRuntimeException;
import org.seasar.framework.exception.NoSuchMethodRuntimeException;

/**
 * Java5のgenericsや可変長を活用する、リフレクションのためのユーティリティです。
 * 
 * @author koichik
 */
public abstract class ReflectionUtil {

    /**
     * インスタンスを構築します。
     */
    protected ReflectionUtil() {
    }

    /**
     * 現在のスレッドのコンテキストクラスローダを使って、 指定された文字列名を持つクラスまたはインタフェースに関連付けられた、
     * {@link Class}オブジェクトを返します。
     * 
     * @param <T>
     *            {@link Class}オブジェクトが表すクラス
     * @param className
     *            要求するクラスの完全修飾名
     * @return 指定された名前を持つクラスの{@link Class}オブジェクト
     * @throws ClassNotFoundRuntimeException
     *             クラスが見つからなかった場合
     * @see Class#forName(String)
     */
    public static <T> Class<T> forName(final String className)
            throws ClassNotFoundRuntimeException {
        return forName(className, Thread.currentThread()
                .getContextClassLoader());
    }

    /**
     * 指定されたクラスローダを使って、 指定された文字列名を持つクラスまたはインタフェースに関連付けられた{@link Class}オブジェクトを返します。
     * 
     * @param <T>
     *            {@link Class}オブジェクトが表すクラス
     * @param className
     *            要求するクラスの完全修飾名
     * @param loader
     *            クラスのロード元である必要があるクラスローダ
     * @return 指定された名前を持つクラスの{@link Class}オブジェクト
     * @throws ClassNotFoundRuntimeException
     *             クラスが見つからなかった場合
     * @see Class#forName(String, boolean, ClassLoader)
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> forName(final String className,
            final ClassLoader loader) throws ClassNotFoundRuntimeException {
        try {
            return (Class<T>) Class.forName(className, true, loader);
        } catch (final ClassNotFoundException e) {
            throw new ClassNotFoundRuntimeException(e);
        }
    }

    /**
     * 現在のスレッドのコンテキストクラスローダを使って、 指定された文字列名を持つクラスまたはインタフェースに関連付けられた、
     * {@link Class}オブジェクトを返します。
     * <p>
     * クラスが見つからなかった場合は{@code null}を返します。
     * </p>
     * 
     * @param <T>
     *            {@link Class}オブジェクトが表すクラス
     * @param className
     *            要求するクラスの完全修飾名
     * @return 指定された名前を持つクラスの{@link Class}オブジェクト
     * @see Class#forName(String)
     */
    public static <T> Class<T> forNameNoException(final String className) {
        return forNameNoException(className, Thread.currentThread()
                .getContextClassLoader());
    }

    /**
     * 指定されたクラスローダを使って、 指定された文字列名を持つクラスまたはインタフェースに関連付けられた、 {@link Class}オブジェクトを返します。
     * <p>
     * クラスが見つからなかった場合は{@code null}を返します。
     * </p>
     * 
     * @param <T>
     *            {@link Class}オブジェクトが表すクラス
     * @param className
     *            要求するクラスの完全修飾名
     * @param loader
     *            クラスのロード元である必要があるクラスローダ
     * @return 指定された名前を持つクラスの{@link Class}オブジェクト
     * @see Class#forName(String)
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> forNameNoException(final String className,
            final ClassLoader loader) {
        try {
            return (Class<T>) Class.forName(className, true, loader);
        } catch (final Throwable ignore) {
            return null;
        }
    }

    /**
     * {@link Class}オブジェクトが表すクラスの指定された{@code public}コンストラクタをリフレクトする{@link Constructor}オブジェクトを返します。
     * 
     * @param <T>
     *            {@link Class}オブジェクトが表すクラス
     * @param clazz
     *            クラスの{@link Class}オブジェクト
     * @param argTypes
     *            パラメータ配列
     * @return 指定された{@code argTypes}と一致する{@code public}コンストラクタの{@link Constructor}オブジェクト
     * @throws NoSuchConstructorRuntimeException
     *             一致するコンストラクタが見つからない場合
     * @see Class#getConstructor(Class[])
     */
    public static <T> Constructor<T> getConstructor(final Class<T> clazz,
            final Class<?>... argTypes)
            throws NoSuchConstructorRuntimeException {
        try {
            return clazz.getConstructor(argTypes);
        } catch (final NoSuchMethodException e) {
            throw new NoSuchConstructorRuntimeException(clazz, argTypes, e);
        }
    }

    /**
     * {@link Class}オブジェクトが表すクラスまたはインタフェースの指定されたコンストラクタをリフレクトする{@link Constructor}オブジェクトを返します。
     * 
     * @param <T>
     *            {@link Class}オブジェクトが表すクラス
     * @param clazz
     *            クラスの{@link Class}オブジェクト
     * @param argTypes
     *            パラメータ配列
     * @return 指定された{@code argTypes}と一致するコンストラクタの{@link Constructor}オブジェクト
     * @throws NoSuchConstructorRuntimeException
     *             一致するコンストラクタが見つからない場合
     * @see Class#getDeclaredConstructor(Class[])
     */
    public static <T> Constructor<T> getDeclaredConstructor(
            final Class<T> clazz, final Class<?>... argTypes)
            throws NoSuchConstructorRuntimeException {
        try {
            return clazz.getDeclaredConstructor(argTypes);
        } catch (final NoSuchMethodException e) {
            throw new NoSuchConstructorRuntimeException(clazz, argTypes, e);
        }
    }

    /**
     * {@link Class}オブジェクトが表すクラスまたはインタフェースの指定された{@code public}メンバフィールドをリフレクトする{@link Field}オブジェクトを返します。
     * 
     * @param clazz
     *            クラスの{@link Class}オブジェクト
     * @param name
     *            フィールド名
     * @return {@code name}で指定されたこのクラスの{@link Field}オブジェクト
     * @throws NoSuchFieldRuntimeException
     *             指定された名前のフィールドが見つからない場合
     * @see Class#getField(String)
     */
    public static Field getField(final Class<?> clazz, final String name)
            throws NoSuchFieldRuntimeException {
        try {
            return clazz.getField(name);
        } catch (final NoSuchFieldException e) {
            throw new NoSuchFieldRuntimeException(clazz, name, e);
        }
    }

    /**
     * {@link Class}オブジェクトが表すクラスまたはインタフェースの指定された宣言フィールドをリフレクトする{@link Field}オブジェクトを返します。
     * 
     * @param clazz
     *            クラスの{@link Class}オブジェクト
     * @param name
     *            フィールド名
     * @return {@code name}で指定されたこのクラスの{@link Field}オブジェクト
     * @throws NoSuchFieldRuntimeException
     *             指定された名前のフィールドが見つからない場合
     * @see Class#getDeclaredField(String)
     */
    public static Field getDeclaredField(final Class<?> clazz, final String name)
            throws NoSuchFieldRuntimeException {
        try {
            return clazz.getDeclaredField(name);
        } catch (final NoSuchFieldException e) {
            throw new NoSuchFieldRuntimeException(clazz, name, e);
        }
    }

    /**
     * {@link Class}オブジェクトが表すクラスまたはインタフェースの指定された{@code public}メンバメソッドをリフレクトする{@link Method}オブジェクトを返します。
     * 
     * @param clazz
     *            クラスの{@link Class}オブジェクト
     * @param name
     *            メソッドの名前
     * @param argTypes
     *            パラメータのリスト
     * @return 指定された{@code name}および{@code argTypes}と一致する{@link Method}オブジェクト
     * @throws NoSuchMethodRuntimeException
     *             一致するメソッドが見つからない場合
     * @see Class#getMethod(String, Class[])
     */
    public static Method getMethod(final Class<?> clazz, final String name,
            final Class<?>... argTypes) throws NoSuchMethodRuntimeException {
        try {
            return clazz.getMethod(name, argTypes);
        } catch (final NoSuchMethodException e) {
            throw new NoSuchMethodRuntimeException(clazz, name, argTypes, e);
        }
    }

    /**
     * {@link Class}オブジェクトが表すクラスまたはインタフェースの指定されたメンバメソッドをリフレクトする{@link Method}オブジェクトを返します。
     * 
     * @param clazz
     *            クラスの{@link Class}オブジェクト
     * @param name
     *            メソッドの名前
     * @param argTypes
     *            パラメータのリスト
     * @return 指定された{@code name}および{@code argTypes}と一致する{@link Method}オブジェクト
     * @throws NoSuchMethodRuntimeException
     *             一致するメソッドが見つからない場合
     * @see Class#getDeclaredMethod(String, Class[])
     */
    public static Method getDeclaredMethod(final Class<?> clazz,
            final String name, final Class<?>... argTypes)
            throws NoSuchMethodRuntimeException {
        try {
            return clazz.getDeclaredMethod(name, argTypes);
        } catch (final NoSuchMethodException e) {
            throw new NoSuchMethodRuntimeException(clazz, name, argTypes, e);
        }
    }

    /**
     * 指定されたクラスのデフォルトコンストラクタで、クラスの新しいインスタンスを作成および初期化します。
     * 
     * @param <T>
     *            {@link Class}オブジェクトが表すクラス
     * @param clazz
     *            クラスを表す{@link Class}オブジェクト
     * @return このオブジェクトが表すコンストラクタを呼び出すことで作成される新規オブジェクト
     * @throws InstantiationRuntimeException
     *             基本となるコンストラクタを宣言するクラスが{@code abstract}クラスを表す場合
     * @throws IllegalAccessRuntimeException
     *             実パラメータ数と仮パラメータ数が異なる場合、 プリミティブ引数のラップ解除変換が失敗した場合、 またはラップ解除後、
     *             メソッド呼び出し変換によってパラメータ値を対応する仮パラメータ型に変換できない場合、
     *             このコンストラクタが列挙型に関連している場合
     * @see Constructor#newInstance(Object[])
     */
    public static <T> T newInstance(final Class<T> clazz)
            throws InstantiationRuntimeException, IllegalAccessRuntimeException {
        try {
            return clazz.newInstance();
        } catch (final InstantiationException e) {
            throw new InstantiationRuntimeException(clazz, e);
        } catch (final IllegalAccessException e) {
            throw new IllegalAccessRuntimeException(clazz, e);
        }
    }

    /**
     * 指定された初期化パラメータで、コンストラクタの宣言クラスの新しいインスタンスを作成および初期化します。
     * 
     * @param <T>
     *            コンストラクタの宣言クラス
     * @param constructor
     *            コンストラクタ
     * @param args
     *            コンストラクタ呼び出しに引数として渡すオブジェクトの配列
     * @return コンストラクタを呼び出すことで作成される新規オブジェクト
     * @throws InstantiationRuntimeException
     *             基本となるコンストラクタを宣言するクラスが{@code abstract}クラスを表す場合
     * @throws IllegalAccessRuntimeException
     *             実パラメータ数と仮パラメータ数が異なる場合、 プリミティブ引数のラップ解除変換が失敗した場合、 またはラップ解除後、
     *             メソッド呼び出し変換によってパラメータ値を対応する仮パラメータ型に変換できない場合、
     *             このコンストラクタが列挙型に関連している場合
     * @see Constructor#newInstance(Object[])
     */
    public static <T> T newInstance(final Constructor<T> constructor,
            final Object... args) throws InstantiationRuntimeException,
            IllegalAccessRuntimeException {
        try {
            return constructor.newInstance(args);
        } catch (final InstantiationException e) {
            throw new InstantiationRuntimeException(constructor
                    .getDeclaringClass(), e);
        } catch (final IllegalAccessException e) {
            throw new IllegalAccessRuntimeException(constructor
                    .getDeclaringClass(), e);
        } catch (final InvocationTargetException e) {
            throw new InvocationTargetRuntimeException(constructor
                    .getDeclaringClass(), e);
        }
    }

    /**
     * 指定されたオブジェクトについて、{@link Field}によって表されるフィールドの値を返します。
     * 
     * @param <T>
     *            フィールドの型
     * @param field
     *            フィールド
     * @param target
     *            表現されるフィールド値の抽出元オブジェクト
     * @return オブジェクト{@code obj}内で表現される値
     * @throws IllegalAccessRuntimeException
     *             基本となるフィールドにアクセスできない場合
     * @see Field#get(Object)
     */
    @SuppressWarnings("unchecked")
    public static <T> T getValue(final Field field, final Object target)
            throws IllegalAccessRuntimeException {
        try {
            return (T) field.get(target);
        } catch (final IllegalAccessException e) {
            throw new IllegalAccessRuntimeException(field.getDeclaringClass(),
                    e);
        }
    }

    /**
     * 指定されたオブジェクトについて、{@link Field}によって表される{@code static}フィールドの値を返します。
     * 
     * @param <T>
     *            フィールドの型
     * @param field
     *            フィールド
     * @return {@code static}フィールドで表現される値
     * @throws IllegalAccessRuntimeException
     *             基本となるフィールドにアクセスできない場合
     * @see Field#get(Object)
     */
    @SuppressWarnings("unchecked")
    public static <T> T getStaticValue(final Field field)
            throws IllegalAccessRuntimeException {
        return (T) getValue(field, null);
    }

    /**
     * {@link Field}オブジェクトによって表される指定されたオブジェクト引数のフィールドを、指定された新しい値に設定します。
     * 
     * @param field
     *            フィールド
     * @param target
     *            フィールドを変更するオブジェクト
     * @param value
     *            変更中の{@code target}の新しいフィールド値
     * @throws IllegalAccessRuntimeException
     *             基本となるフィールドにアクセスできない場合
     * @see Field#set(Object, Object)
     */
    public static void setValue(final Field field, final Object target,
            final Object value) throws IllegalAccessRuntimeException {
        try {
            field.set(target, value);
        } catch (final IllegalAccessException e) {
            throw new IllegalAccessRuntimeException(field.getDeclaringClass(),
                    e);
        }
    }

    /**
     * {@link Field}オブジェクトによって表される{@code static}フィールドを、指定された新しい値に設定します。
     * 
     * @param field
     *            フィールド
     * @param value
     *            {@code static}フィールドの新しい値
     * @throws IllegalAccessRuntimeException
     *             基本となるフィールドにアクセスできない場合
     * @see Field#set(Object, Object)
     */
    public static void setStaticValue(final Field field, final Object value)
            throws IllegalAccessRuntimeException {
        setValue(field, null, value);
    }

    /**
     * {@link Method}オブジェクトによって表される基本となるメソッドを、指定したオブジェクトに対して指定したパラメータで呼び出します。
     * 
     * @param <T>
     *            メソッドの戻り値の型
     * @param method
     *            メソッド
     * @param target
     *            基本となるメソッドの呼び出し元のオブジェクト
     * @param args
     *            メソッド呼び出しに使用される引数
     * @return このオブジェクトが表すメソッドを、パラメータ{@code args}を使用して{@code obj}にディスパッチした結果
     * @throws IllegalAccessRuntimeException
     *             この{@link Method}オブジェクトがJava言語アクセス制御を実施し、
     *             基本となるメソッドにアクセスできない場合
     * @throws InvocationTargetRuntimeException
     *             基本となるメソッドが例外をスローする場合
     * @see Method#invoke(Object, Object[])
     */
    @SuppressWarnings("unchecked")
    public static <T> T invoke(final Method method, final Object target,
            final Object... args) throws IllegalAccessRuntimeException,
            InvocationTargetRuntimeException {
        try {
            return (T) method.invoke(target, args);
        } catch (final IllegalAccessException e) {
            throw new IllegalAccessRuntimeException(method.getDeclaringClass(),
                    e);
        } catch (final InvocationTargetException e) {
            throw new InvocationTargetRuntimeException(method
                    .getDeclaringClass(), e);
        }
    }

    /**
     * {@link Method}オブジェクトによって表される基本となる{@code static}メソッドを、指定したパラメータで呼び出します。
     * 
     * @param <T>
     *            メソッドの戻り値の型
     * @param method
     *            メソッド
     * @param args
     *            メソッド呼び出しに使用される引数
     * @return このオブジェクトが表す{@code static}メソッドを、パラメータ{@code args}を使用してディスパッチした結果
     * @throws IllegalAccessRuntimeException
     *             この{@link Method}オブジェクトがJava言語アクセス制御を実施し、
     *             基本となるメソッドにアクセスできない場合
     * @throws InvocationTargetRuntimeException
     *             基本となるメソッドが例外をスローする場合
     * @see Method#invoke(Object, Object[])
     */
    @SuppressWarnings("unchecked")
    public static <T> T invokeStatic(final Method method, final Object... args)
            throws IllegalAccessRuntimeException,
            InvocationTargetRuntimeException {
        return (T) invoke(method, null, args);
    }

    /**
     * パラメタ化されたコレクションの要素型を返します。
     * 
     * @param parameterizedCollection
     *            パラメタ化されたコレクションの型
     * @return パラメタ化されたコレクションの要素型
     */
    public static Class<?> getElementTypeOfCollection(
            final Type parameterizedCollection) {
        return GenericUtil.getRawClass(GenericUtil
                .getElementTypeOfCollection(parameterizedCollection));
    }

    /**
     * 指定されたフィールドのパラメタ化されたコレクションの要素型を返します。
     * 
     * @param field
     *            フィールド
     * @return 指定されたフィールドのパラメタ化されたコレクションの要素型 since 2.4.18
     * since 2.4.18
     */
    public static Class<?> getElementTypeOfCollectionFromFieldType(
            final Field field) {
        final Type type = field.getGenericType();
        return getElementTypeOfCollection(type);
    }

    /**
     * 指定されたメソッドの引数型として宣言されているパラメタ化されたコレクションの要素型を返します。
     * 
     * @param method
     *            メソッド
     * @param parameterPosition
     *            パラメタ化されたコレクションが宣言されているメソッド引数の位置
     * @return 指定されたメソッドの引数型として宣言されているパラメタ化されたコレクションの要素型
     */
    public static Class<?> getElementTypeOfCollectionFromParameterType(
            final Method method, final int parameterPosition) {
        final Type[] parameterTypes = method.getGenericParameterTypes();
        return getElementTypeOfCollection(parameterTypes[parameterPosition]);
    }

    /**
     * 指定されたメソッドの戻り値型として宣言されているパラメタ化されたコレクションの要素型を返します。
     * 
     * @param method
     *            メソッド
     * @return 指定されたメソッドの戻り値型として宣言されているパラメタ化されたコレクションの要素型
     */
    public static Class<?> getElementTypeOfCollectionFromReturnType(
            final Method method) {
        return getElementTypeOfCollection(method.getGenericReturnType());
    }

    /**
     * パラメタ化されたリストの要素型を返します。
     * 
     * @param parameterizedList
     *            パラメタ化されたリストの型
     * @return パラメタ化されたリストの要素型
     */
    public static Class<?> getElementTypeOfList(final Type parameterizedList) {
        return GenericUtil.getRawClass(GenericUtil
                .getElementTypeOfList(parameterizedList));
    }

    /**
     * 指定されたフィールドのパラメタ化されたリストの要素型を返します。
     * 
     * @param field
     *            フィールド
     * @return 指定されたフィールドのパラメタ化されたリストの要素型 since 2.4.18
     * since 2.4.18
     */
    public static Class<?> getElementTypeOfListFromFieldType(final Field field) {
        final Type type = field.getGenericType();
        return getElementTypeOfList(type);
    }

    /**
     * 指定されたメソッドの引数型として宣言されているパラメタ化されたリストの要素型を返します。
     * 
     * @param method
     *            メソッド
     * @param parameterPosition
     *            パラメタ化されたリストが宣言されているメソッド引数の位置
     * @return 指定されたメソッドの引数型として宣言されているパラメタ化されたリストの要素型
     */
    public static Class<?> getElementTypeOfListFromParameterType(
            final Method method, final int parameterPosition) {
        final Type[] parameterTypes = method.getGenericParameterTypes();
        return getElementTypeOfList(parameterTypes[parameterPosition]);
    }

    /**
     * 指定されたメソッドの戻り値型として宣言されているパラメタ化されたリストの要素型を返します。
     * 
     * @param method
     *            メソッド
     * @return 指定されたメソッドの戻り値型として宣言されているパラメタ化されたリストの要素型
     */
    public static Class<?> getElementTypeOfListFromReturnType(
            final Method method) {
        return getElementTypeOfList(method.getGenericReturnType());
    }

    /**
     * パラメタ化されたセットの要素型を返します。
     * 
     * @param parameterizedSet
     *            パラメタ化されたセットの型
     * @return パラメタ化されたセットの要素型
     */
    public static Class<?> getElementTypeOfSet(final Type parameterizedSet) {
        return GenericUtil.getRawClass(GenericUtil
                .getElementTypeOfSet(parameterizedSet));
    }

    /**
     * 指定されたフィールドのパラメタ化されたセットの要素型を返します。
     * 
     * @param field
     *            フィールド
     * @return 指定されたフィールドのパラメタ化されたセットの要素型 since 2.4.18
     * since 2.4.18
     */
    public static Class<?> getElementTypeOfSetFromFieldType(final Field field) {
        final Type type = field.getGenericType();
        return getElementTypeOfSet(type);
    }

    /**
     * 指定されたメソッドの引数型として宣言されているパラメタ化されたセットの要素型を返します。
     * 
     * @param method
     *            メソッド
     * @param parameterPosition
     *            パラメタ化されたセットが宣言されているメソッド引数の位置
     * @return 指定されたメソッドの引数型として宣言されているパラメタ化されたセットの要素型
     */
    public static Class<?> getElementTypeOfSetFromParameterType(
            final Method method, final int parameterPosition) {
        final Type[] parameterTypes = method.getGenericParameterTypes();
        return getElementTypeOfSet(parameterTypes[parameterPosition]);
    }

    /**
     * 指定されたメソッドの戻り値型として宣言されているパラメタ化されたセットの要素型を返します。
     * 
     * @param method
     *            メソッド
     * @return 指定されたメソッドの戻り値型として宣言されているパラメタ化されたセットの要素型
     */
    public static Class<?> getElementTypeOfSetFromReturnType(final Method method) {
        return getElementTypeOfSet(method.getGenericReturnType());
    }

}
