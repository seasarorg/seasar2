/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
package org.seasar.extension.dxo.converter.impl;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;

import org.seasar.extension.dxo.converter.ConversionContext;
import org.seasar.extension.dxo.converter.Converter;
import org.seasar.extension.dxo.converter.ConverterFactory;

/**
 * 任意のオブジェクトを配列への変換を行うコンバータです。
 * <p>
 * 変換は次のように行われます。
 * </p>
 * <ul>
 * <li>変換元のオブジェクトが変換先の型に代入可能な配列なら、変換元をそのまま変換先とします。</li>
 * <li>変換元のオブジェクトが配列なら、変換元配列と同じ長さで，各要素を変換した配列を変換先とします。</li>
 * <li>変換元のオブジェクトが{@link Collection コレクション}なら、コレクションのサイズと同じ長さで、各要素を変換した配列を変換先とします。</li>
 * <li>それ以外の場合は、変換元オブジェクトを変換したオブジェクトを唯一の要素とする長さ1の配列を変換先とします。</li>
 * </ul>
 * 
 * @author Satoshi Kimura
 * @author koichik
 */
public class ArrayConverter extends AbstractConverter {

    public Class[] getSourceClasses() {
        return new Class[] { Object.class };
    }

    public Class getDestClass() {
        return Object[].class;
    }

    public Object convert(final Object source, final Class destClass,
            final ConversionContext context) {
        if (source == null) {
            return null;
        }

        if (shallowCopy && destClass.isAssignableFrom(source.getClass())) {
            return source;
        }
        if (source.getClass().isArray()) {
            return fromArrayToArray(destClass.getComponentType(), source,
                    context);
        }
        if (source instanceof Collection) {
            return fromCollectionToArray(destClass.getComponentType(),
                    (Collection) source, context);
        }
        final Object result = Array
                .newInstance(destClass.getComponentType(), 1);
        Array.set(result, 0, source);
        return result;
    }

    /**
     * 配列を配列に変換して返します。
     * 
     * @param componentType
     *            変換先配列の要素型
     * @param source
     *            変換元の配列
     * @param context
     *            変換コンテキスト
     * @return 変換した結果の配列
     */
    protected Object fromArrayToArray(final Class componentType,
            final Object source, final ConversionContext context) {
        final int length = Array.getLength(source);
        final Object result = Array.newInstance(componentType, length);
        if (length == 0) {
            return result;
        }

        final ConverterFactory converterFactory = context.getConverterFactory();
        for (int i = 0; i < length; i++) {
            final Object sourceElement = Array.get(source, i);
            final Converter converter = converterFactory.getConverter(
                    sourceElement.getClass(), componentType);
            Array.set(result, i, converter.convert(sourceElement,
                    componentType, context));
        }
        return result;
    }

    /**
     * コレクションを配列に変換して返します。
     * 
     * @param componentType
     *            変換先配列の要素型
     * @param source
     *            変換元のコレクション
     * @param context
     *            変換コンテキスト
     * @return 変換した結果の配列
     */
    protected Object fromCollectionToArray(final Class componentType,
            final Collection source, final ConversionContext context) {
        final int length = source.size();
        final Object[] result = (Object[]) Array.newInstance(componentType,
                length);
        if (length == 0) {
            return result;
        }

        final ConverterFactory converterFactory = context.getConverterFactory();
        int i = 0;
        for (final Iterator it = source.iterator(); it.hasNext(); ++i) {
            final Object sourceElement = it.next();
            final Converter converter = converterFactory.getConverter(
                    sourceElement.getClass(), componentType);
            result[i] = converter
                    .convert(sourceElement, componentType, context);
        }
        return result;
    }

}
