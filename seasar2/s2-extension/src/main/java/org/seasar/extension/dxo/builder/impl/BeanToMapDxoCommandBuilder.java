/*
 * Copyright 2004-2011 the Seasar Foundation and the Others.
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
import org.seasar.extension.dxo.command.impl.BeanToMapDxoCommand;
import org.seasar.extension.dxo.util.DxoUtil;

/**
 * Beanから{@link Map}に変換するDxoのメソッドに応じた{@link DxoCommand}のインスタンスを生成するビルダです。
 * 
 * @author koichik
 */
public class BeanToMapDxoCommandBuilder extends AbstractDxoCommandBuilder {

    /** 変換先として受け入れ可能なクラスの配列です。 */
    protected Class[] ACCEPTABLE_DEST_CLASSES = new Class[] { Map.class,
            Map[].class, List.class };

    public DxoCommand createDxoCommand(final Class dxoClass, final Method method) {
        final Class[] parameterTypes = method.getParameterTypes();
        final int parameterSize = parameterTypes.length;
        if (parameterSize != 1 && parameterSize != 2) {
            return null;
        }

        final Class sourceType = parameterTypes[0];
        final Class destType = parameterSize == 1 ? method.getReturnType()
                : parameterTypes[1];
        final String expression = getAnnotationReader().getConversionRule(
                dxoClass, method);

        if (destType.isArray()) {
            final Class elementType = destType.getComponentType();
            if (!Map.class.isAssignableFrom(elementType)) {
                return null;
            }
            if (!sourceType.isArray()
                    && !List.class.isAssignableFrom(sourceType)) {
                return null;
            }
        } else if (List.class.isAssignableFrom(destType)) {
            final Class elementType = DxoUtil.getElementTypeOfList(method);
            if (elementType == null || !Map.class.isAssignableFrom(elementType)) {
                return null;
            }
            if (!sourceType.isArray()
                    && !List.class.isAssignableFrom(sourceType)) {
                return null;
            }
        } else if (Map.class.isAssignableFrom(destType)) {
            if (sourceType.isArray() || List.class.isAssignableFrom(sourceType)) {
                return null;
            }
        } else {
            return null;
        }

        return new BeanToMapDxoCommand(dxoClass, method, converterFactory,
                getAnnotationReader(), expression);
    }

}
