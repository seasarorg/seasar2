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
package org.seasar.framework.beans.factory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;

import org.seasar.framework.beans.ParameterizedClassDesc;
import org.seasar.framework.beans.PropertyDesc;

/**
 * フィールの型やメソッドの引数型、戻り値型を表現する{@link ParameterizedClassDesc}を作成するファクトリです。
 * <p>
 * このクラスの機能はS2-Tigerに含まれる{@link Provider}の実装クラスによって提供されます。
 * </p>
 * 
 * @since 2.4.18
 * @author koichik
 * @see PropertyDesc#getParameterizedClassDesc()
 */
public class ParameterizedClassDescFactory {

    /** {@link Provider}実装クラスのクラス名 */
    protected static final String PROVIDER_CLASS_NAME = ParameterizedClassDescFactory.class
            .getName()
            + "Provider";

    /** {@link Provider}のインスタンス */
    protected static final Provider provider = createProvider();

    /**
     * パラメータ化された型(クラスまたはインタフェース)が持つ型変数をキー、型引数を値とする{@link Map}を返します。
     * <p>
     * S2-Tigerが利用できない場合や、型がパラメタ化されていない場合は空の{@link Map}を返します。
     * </p>
     * 
     * @param beanClass
     *            パラメータ化された型(クラスまたはインタフェース)
     * @return パラメータ化された型が持つ型変数をキー、型引数を値とする{@link Map}
     */
    public static Map getTypeVariables(Class beanClass) {
        if (provider == null) {
            return Collections.EMPTY_MAP;
        }
        return provider.getTypeVariables(beanClass);
    }

    /**
     * フィールドの型をを表現する{@link ParameterizedClassDesc}を作成して返します。
     * <p>
     * S2-Tigerが利用できない場合や、フィールドがパラメタ化されていない場合は<code>null</code>を返します。
     * </p>
     * 
     * @param field
     *            フィールド
     * @param map
     *            パラメータ化された型が持つ型変数をキー、型引数を値とする{@link Map}
     * @return フィールドの型を表現する{@link ParameterizedClassDesc}
     */
    public static ParameterizedClassDesc createParameterizedClassDesc(
            final Field field, final Map map) {
        if (provider == null) {
            return null;
        }
        return provider.createParameterizedClassDesc(field, map);
    }

    /**
     * メソッドの引数型を表現する{@link ParameterizedClassDesc}を作成して返します。
     * <p>
     * S2-Tigerが利用できない場合や、メソッドの引数がパラメタ化されていない場合は<code>null</code>を返します。
     * </p>
     * 
     * @param method
     *            メソッド
     * @param index
     *            引数の位置
     * @param map
     *            パラメータ化された型が持つ型変数をキー、型引数を値とする{@link Map}
     * @return メソッドの引数型を表現する{@link ParameterizedClassDesc}
     */
    public static ParameterizedClassDesc createParameterizedClassDesc(
            final Method method, final int index, final Map map) {
        if (provider == null) {
            return null;
        }
        return provider.createParameterizedClassDesc(method, index, map);
    }

    /**
     * メソッドの戻り値型を表現する{@link ParameterizedClassDesc}を作成して返します。
     * <p>
     * S2-Tigerが利用できない場合や、メソッドの戻り値型がパラメタ化されていない場合は<code>null</code>を返します。
     * </p>
     * 
     * @param method
     *            メソッド
     * @param map
     *            パラメータ化された型が持つ型変数をキー、型引数を値とする{@link Map}
     * @return メソッドの戻り値型を表現する{@link ParameterizedClassDesc}
     */
    public static ParameterizedClassDesc createParameterizedClassDesc(
            final Method method, final Map map) {
        if (provider == null) {
            return null;
        }
        return provider.createParameterizedClassDesc(method, map);
    }

    /**
     * {@link Provider}のインスタンスを作成して返します。
     * <p>
     * S2-Tigerが利用できない場合は<code>null</code>を返します。
     * </p>
     * 
     * @return {@link Provider}のインスタンス
     */
    protected static Provider createProvider() {
        try {
            final Class clazz = Class.forName(PROVIDER_CLASS_NAME);
            return (Provider) clazz.newInstance();
        } catch (final Exception e) {
            return null;
        }
    }

    /**
     * {@link ParameterizedClassDescFactory}の機能を提供するインターフェースです。
     * <p>
     * この実装クラスはS2-Tigerによって提供されます。
     * </p>
     * 
     * @author koichik
     * @since 2.4.18
     */
    public interface Provider {

        /**
         * パラメータ化された型(クラスまたはインタフェース)が持つ型変数をキー、型引数を値とする{@link Map}を返します。
         * 
         * @param beanClass
         *            パラメータ化された型(クラスまたはインタフェース)
         * @return パラメータ化された型が持つ型変数をキー、型引数を値とする{@link Map}
         */
        Map getTypeVariables(Class beanClass);

        /**
         * フィールドの型をを表現する{@link ParameterizedClassDesc}を作成して返します。
         * 
         * @param field
         *            フィールド
         * @param map
         *            パラメータ化された型が持つ型変数をキー、型引数を値とする{@link Map}
         * @return フィールドの型を表現する{@link ParameterizedClassDesc}
         */
        ParameterizedClassDesc createParameterizedClassDesc(Field field, Map map);

        /**
         * メソッドの引数型を表現する{@link ParameterizedClassDesc}を作成して返します。
         * 
         * @param method
         *            メソッド
         * @param index
         *            引数の位置
         * @param map
         *            パラメータ化された型が持つ型変数をキー、型引数を値とする{@link Map}
         * @return メソッドの引数型を表現する{@link ParameterizedClassDesc}
         */
        ParameterizedClassDesc createParameterizedClassDesc(Method method,
                int index, Map map);

        /**
         * メソッドの戻り値型を表現する{@link ParameterizedClassDesc}を作成して返します。
         * 
         * @param method
         *            メソッド
         * @param map
         *            パラメータ化された型が持つ型変数をキー、型引数を値とする{@link Map}
         * @return メソッドの戻り値型を表現する{@link ParameterizedClassDesc}を作成して返します。
         */
        ParameterizedClassDesc createParameterizedClassDesc(Method method,
                Map map);

    }

}
