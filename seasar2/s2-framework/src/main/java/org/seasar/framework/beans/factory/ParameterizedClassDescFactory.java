/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
     * フィールドの型をを表現する{@link ParameterizedClassDesc}を作成して返します。
     * <p>
     * S2-Tigerが利用できない場合や、フィールドがパラメタ化されていない場合は<code>null</code>を返します。
     * </p>
     * 
     * @param field
     *            フィールド
     * @return フィールドの型を表現する{@link ParameterizedClassDesc}
     */
    public static ParameterizedClassDesc createParameterizedClassDesc(
            final Field field) {
        if (provider == null) {
            return null;
        }
        return provider.createParameterizedClassDesc(field);
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
     * @return メソッドの引数型を表現する{@link ParameterizedClassDesc}
     */
    public static ParameterizedClassDesc createParameterizedClassDesc(
            final Method method, final int index) {
        if (provider == null) {
            return null;
        }
        return provider.createParameterizedClassDesc(method, index);
    }

    /**
     * メソッドの戻り値型を表現する{@link ParameterizedClassDesc}を作成して返します。
     * <p>
     * S2-Tigerが利用できない場合や、メソッドの戻り値型がパラメタ化されていない場合は<code>null</code>を返します。
     * </p>
     * 
     * @param method
     *            メソッド
     * @return メソッドの戻り値型を表現する{@link ParameterizedClassDesc}
     */
    public static ParameterizedClassDesc createParameterizedClassDesc(
            final Method method) {
        if (provider == null) {
            return null;
        }
        return provider.createParameterizedClassDesc(method);
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
         * フィールドの型をを表現する{@link ParameterizedClassDesc}を作成して返します。
         * 
         * @param field
         *            フィールド
         * @return フィールドの型を表現する{@link ParameterizedClassDesc}
         */
        ParameterizedClassDesc createParameterizedClassDesc(Field field);

        /**
         * メソッドの引数型を表現する{@link ParameterizedClassDesc}を作成して返します。
         * 
         * @param method
         *            メソッド
         * @param index
         *            引数の位置
         * @return メソッドの引数型を表現する{@link ParameterizedClassDesc}
         */
        ParameterizedClassDesc createParameterizedClassDesc(Method method,
                int index);

        /**
         * メソッドの戻り値型を表現する{@link ParameterizedClassDesc}を作成して返します。
         * 
         * @param method
         *            メソッド
         * @return メソッドの戻り値型を表現する{@link ParameterizedClassDesc}を作成して返します。
         */
        ParameterizedClassDesc createParameterizedClassDesc(Method method);

    }

}
