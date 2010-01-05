/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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
package org.seasar.extension.dxo.util;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import org.seasar.framework.util.tiger.GenericUtil;

/**
 * J2SE1.4でビルドされるS2-ExtensionからJava5の機能を利用するために使用されるユーティリティクラスです。
 * <p>
 * このクラスは{@code DxoUtil}からリフレクションで呼び出されます。
 * </p>
 * 
 * @author koichik
 */
public class DxoTigerUtil {

    /**
     * メソッドの戻り値型である{@link Map}の値の型変数に指定された型を返します。
     * 
     * @param method
     *            メソッド
     * @return メソッドの戻り値型である{@link Map}の値の型変数に指定された型
     */
    public static Class<?> getValueTypeOfTargetMap(final Method method) {
        final Type[] parameterTypes = method.getGenericParameterTypes();
        Type type = parameterTypes.length == 2 ? parameterTypes[1] : method
                .getGenericReturnType();
        if (GenericUtil.isTypeOf(type, List.class)) {
            type = GenericUtil.getElementTypeOfList(type);
        } else if (GenericArrayType.class.isInstance(type)) {
            type = GenericUtil.getElementTypeOfArray(type);
        }
        final Class<?> valueType = GenericUtil.getRawClass(GenericUtil
                .getValueTypeOfMap(type));
        return valueType != null ? valueType : Object.class;
    }

}
