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
package org.seasar.framework.util.tiger;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author koichik
 * 
 */
public class GenericUtil {

    public static boolean isTypeOf(Type type, Class<?> clazz) {
        if (Class.class.isInstance(type)) {
            return clazz.isAssignableFrom(Class.class.cast(type));
        }
        if (ParameterizedType.class.isInstance(type)) {
            final ParameterizedType parameterizedType = ParameterizedType.class
                    .cast(type);
            return isTypeOf(parameterizedType.getRawType(), clazz);
        }
        return false;
    }

    public static Class<?> getRawClass(final Type type) {
        if (Class.class.isInstance(type)) {
            return Class.class.cast(type);
        }
        if (ParameterizedType.class.isInstance(type)) {
            final ParameterizedType parameterizedType = ParameterizedType.class
                    .cast(type);
            return getRawClass(parameterizedType.getRawType());
        }
        return null;
    }

    public static Type[] getGenericParameter(final Type type) {
        if (!ParameterizedType.class.isInstance(type)) {
            return null;
        }
        return ParameterizedType.class.cast(type).getActualTypeArguments();
    }

    public static Type getGenericParameter(final Type type, final int index) {
        if (!ParameterizedType.class.isInstance(type)) {
            return null;
        }
        final Type[] genericParameter = getGenericParameter(type);
        if (genericParameter == null) {
            return null;
        }
        return genericParameter[index];
    }

    public static Type getElementTypeOfArray(final Type type) {
        if (!GenericArrayType.class.isInstance(type)) {
            return null;
        }
        return GenericArrayType.class.cast(type).getGenericComponentType();
    }

    public static Type getElementTypeOfList(final Type type) {
        if (!isTypeOf(type, List.class)) {
            return null;
        }
        return getGenericParameter(type, 0);
    }

    public static Type getElementTypeOfSet(final Type type) {
        if (!isTypeOf(type, Set.class)) {
            return null;
        }
        return getGenericParameter(type, 0);
    }

    public static Type getKeyTypeOfMap(final Type type) {
        if (!isTypeOf(type, Map.class)) {
            return null;
        }
        return getGenericParameter(type, 0);
    }

    public static Type getValueTypeOfMap(final Type type) {
        if (!isTypeOf(type, Map.class)) {
            return null;
        }
        return getGenericParameter(type, 1);
    }

}
