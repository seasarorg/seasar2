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
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Map;

import org.seasar.framework.beans.ParameterizedClassDesc;
import org.seasar.framework.beans.factory.ParameterizedClassDescFactory.Provider;
import org.seasar.framework.beans.impl.ParameterizedClassDescImpl;

import static org.seasar.framework.util.tiger.GenericUtil.*;

/**
 * {@link Provider}の機能を提供する実装クラスです。
 * 
 * @since 2.4.18
 * @author koichik
 */
public class ParameterizedClassDescFactoryProvider implements Provider {

    @SuppressWarnings("unchecked")
    public Map<TypeVariable<?>, Type> getTypeVariables(Class beanClass) {
        return getTypeVariableMap(beanClass);
    }

    @SuppressWarnings("unchecked")
    public ParameterizedClassDesc createParameterizedClassDesc(
            final Field field, final Map map) {
        return createParameterizedClassDesc(field.getGenericType(), map);
    }

    @SuppressWarnings("unchecked")
    public ParameterizedClassDesc createParameterizedClassDesc(
            final Method method, final int index, Map map) {
        return createParameterizedClassDesc(
                method.getGenericParameterTypes()[index], map);
    }

    @SuppressWarnings("unchecked")
    public ParameterizedClassDesc createParameterizedClassDesc(
            final Method method, Map map) {
        return createParameterizedClassDesc(method.getGenericReturnType(), map);
    }

    /**
     * {@link Type}を表現する{@link ParameterizedClassDesc}を作成して返します。
     * 
     * @param type
     *            型
     * @param map
     *            パラメータ化された型が持つ型変数をキー、型引数を値とする{@link Map}
     * @return 型を表現する{@link ParameterizedClassDesc}
     */
    public ParameterizedClassDesc createParameterizedClassDesc(final Type type,
            final Map<TypeVariable<?>, Type> map) {
        final Class<?> rowClass = getActualClass(type, map);
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
            parameterDescs[i] = createParameterizedClassDesc(parameterTypes[i],
                    map);
        }
        desc.setArguments(parameterDescs);
        return desc;
    }

}
