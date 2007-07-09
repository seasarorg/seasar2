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
package org.seasar.extension.dxo.builder.impl;

import java.lang.reflect.Method;
import java.util.List;

import org.seasar.extension.dxo.command.DxoCommand;
import org.seasar.extension.dxo.command.impl.BeanToBeanDxoCommand;
import org.seasar.extension.dxo.util.DxoUtil;

/**
 * BeanからBeanに変換するDxoのメソッドに応じた{@link DxoCommand}のインスタンスを生成するビルダです。
 * 
 * @author koichik
 */
public class BeanToBeanDxoCommandBuilder extends AbstractDxoCommandBuilder {

    public DxoCommand createDxoCommand(final Class dxoClass, final Method method) {
        final Class[] parameterTypes = method.getParameterTypes();
        final int parameterSize = parameterTypes.length;
        if (parameterSize != 1 && parameterSize != 2) {
            return null;
        }

        final Class sourceType = parameterTypes[0];
        final Class destType = parameterSize == 1 ? method.getReturnType()
                : parameterTypes[1];
        final Class destElementClass = DxoUtil.getElementTypeOfList(method);

        if (sourceType.isArray()) {
            if (destType.isArray()) {
                return new BeanToBeanDxoCommand(dxoClass, method,
                        converterFactory, getAnnotationReader(), destType
                                .getComponentType());
            } else if (List.class.isAssignableFrom(destType)
                    && destElementClass != null) {
                return new BeanToBeanDxoCommand(dxoClass, method,
                        converterFactory, getAnnotationReader(),
                        destElementClass);
            }
        } else if (List.class.isAssignableFrom(sourceType)) {
            if (destType.isArray()) {
                return new BeanToBeanDxoCommand(dxoClass, method,
                        converterFactory, getAnnotationReader(), destType
                                .getComponentType());
            } else if (List.class.isAssignableFrom(destType)
                    && destElementClass != null) {
                return new BeanToBeanDxoCommand(dxoClass, method,
                        converterFactory, getAnnotationReader(),
                        destElementClass);
            }
        } else {
            if (!destType.isArray() && !List.class.isAssignableFrom(destType)) {
                return new BeanToBeanDxoCommand(dxoClass, method,
                        converterFactory, getAnnotationReader(), destType);
            }
        }
        return null;
    }

}
