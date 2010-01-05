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
package org.seasar.extension.dxo.builder.impl;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.seasar.extension.dxo.command.DxoCommand;
import org.seasar.extension.dxo.command.impl.MapToBeanDxoCommand;
import org.seasar.extension.dxo.util.DxoUtil;
import org.seasar.framework.util.MethodUtil;

/**
 * {@link Map}からBeanに変換するDxoのメソッドに応じた{@link DxoCommand}のインスタンスを生成するビルダです。
 * 
 * @author koichik
 */
public class MapToBeanDxoCommandBuilder extends AbstractDxoCommandBuilder {

    public DxoCommand createDxoCommand(final Class dxoClass, final Method method) {
        final Class[] parameterTypes = method.getParameterTypes();
        final int parameterSize = parameterTypes.length;
        if (parameterSize != 1 && parameterSize != 2) {
            return null;
        }

        final Class sourceType = parameterTypes[0];
        final Class sourceElementClass = MethodUtil
                .getElementTypeOfListFromParameterType(method, 0);
        final Class destType = parameterSize == 1 ? method.getReturnType()
                : parameterTypes[1];
        final Class destElementClass = DxoUtil.getElementTypeOfList(method);

        if (sourceType.isArray()) {
            final Class elementType = sourceType.getComponentType();
            if (Map.class.isAssignableFrom(elementType)) {
                if (destType.isArray()) {
                    return new MapToBeanDxoCommand(dxoClass, method,
                            converterFactory, getAnnotationReader(), destType
                                    .getComponentType());
                } else if (List.class.isAssignableFrom(destType)
                        && destElementClass != null) {
                    return new MapToBeanDxoCommand(dxoClass, method,
                            converterFactory, getAnnotationReader(),
                            destElementClass);
                }
            }
        } else if (List.class.isAssignableFrom(sourceType)) {
            if (sourceElementClass != null
                    && Map.class.isAssignableFrom(sourceElementClass)) {
                if (destType.isArray()) {
                    return new MapToBeanDxoCommand(dxoClass, method,
                            converterFactory, getAnnotationReader(), destType
                                    .getComponentType());
                } else if (List.class.isAssignableFrom(destType)) {
                    return new MapToBeanDxoCommand(dxoClass, method,
                            converterFactory, getAnnotationReader(),
                            destElementClass);
                }
            }
        } else if (Map.class.isAssignableFrom(sourceType)) {
            if (!destType.isArray() && !List.class.isAssignableFrom(destType)) {
                return new MapToBeanDxoCommand(dxoClass, method,
                        converterFactory, getAnnotationReader(), destType);
            }
        }
        return null;
    }

}
