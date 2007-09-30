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
package org.seasar.framework.beans.factory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import org.seasar.framework.beans.ParameterizedClassDesc;
import org.seasar.framework.beans.factory.ParameterizedClassDescFactory.Provider;
import org.seasar.framework.beans.impl.ParameterizedClassDescImpl;

import static org.seasar.framework.util.tiger.GenericUtil.*;

/**
 * {@link ParameterizedClassDescFactory}の機能を提供する実装クラスです。
 * 
 * @since 2.4.18
 * @author koichik
 */
public class ParameterizedClassDescFactoryProvider implements Provider {

    /**
     * フィールドの型をを表現する{@link ParameterizedClassDesc}を作成して返します。
     * 
     * @param field
     *            フィールド
     * @return フィールドの型を表現する{@link ParameterizedClassDesc}
     */
    public ParameterizedClassDesc createParameterizedClassDesc(final Field field) {
        return createParameterizedClassDesc(field.getGenericType());
    }

    /**
     * メソッドの引数型を表現する{@link ParameterizedClassDesc}を作成して返します。
     * 
     * @param method
     *            メソッド
     * @param index
     *            引数の位置
     * @return メソッドの引数型を表現する{@link ParameterizedClassDesc}
     */
    public ParameterizedClassDesc createParameterizedClassDesc(
            final Method method, final int index) {
        return createParameterizedClassDesc(method.getGenericParameterTypes()[index]);
    }

    /**
     * メソッドの戻り値型を表現する{@link ParameterizedClassDesc}を作成して返します。
     * 
     * @param method
     *            メソッド
     * @return メソッドの戻り値型を表現する{@link ParameterizedClassDesc}を作成して返します。
     */
    public ParameterizedClassDesc createParameterizedClassDesc(
            final Method method) {
        return createParameterizedClassDesc(method.getGenericReturnType());
    }

    /**
     * {@link Type}を表現する{@link ParameterizedClassDesc}を作成して返します。
     * 
     * @param type
     *            型
     * @return 型を表現する{@link ParameterizedClassDesc}
     */
    public ParameterizedClassDesc createParameterizedClassDesc(final Type type) {
        final Class<?> rowClass = getRawClass(type);
        if (rowClass == null) {
            return null;
        }
        final ParameterizedClassDescImpl desc = new ParameterizedClassDescImpl(
                rowClass);
        final Type[] parameterTypes = getGenericParameter(type);
        if (parameterTypes == null) {
            return desc;
        }
        final ParameterizedClassDesc[] parameterDescs = new ParameterizedClassDesc[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; ++i) {
            parameterDescs[i] = createParameterizedClassDesc(parameterTypes[i]);
        }
        desc.setArguments(parameterDescs);
        return desc;
    }

}
