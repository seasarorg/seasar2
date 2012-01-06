/*
 * Copyright 2004-2012 the Seasar Foundation and the Others.
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
import org.seasar.extension.dxo.converter.ParameterizedClassConverter;
import org.seasar.framework.beans.ParameterizedClassDesc;

/**
 * 変換元クラスのインスタンスをパラメタ化されたコレクションクラスのインスタンスに変換するコンバータの抽象クラスです。
 * 
 * @since 2.4.18
 * @author koichik
 */
public abstract class AbstractParameterizedCollectionConverter extends
        AbstractConverter implements ParameterizedClassConverter {

    public void convert(final Object source, final Object dest,
            final ConversionContext context) {
        final Collection destCollection = (Collection) dest;
        if (source.getClass().isArray()) {
            final int length = Array.getLength(source);
            for (int i = 0; i < length; ++i) {
                destCollection.add(Array.get(source, i));
            }
        } else if (source instanceof Collection) {
            destCollection.addAll((Collection) source);
        } else {
            destCollection.add(source);
        }
    }

    public void convert(final Object source, final Object dest,
            final ParameterizedClassDesc parameterizedClassDesc,
            final ConversionContext context) {
        final Collection destCollection = (Collection) dest;
        final Class destElementClass = getElementClass(parameterizedClassDesc);
        if (destElementClass == null) {
            convert(source, dest, context);
        } else if (source.getClass().isArray()) {
            convertFromArray(source, destCollection, destElementClass, context);
        } else if (source instanceof Collection) {
            convertFromCollection((Collection) source, destCollection,
                    destElementClass, context);
        } else if (shallowCopy
                && destElementClass.isAssignableFrom(source.getClass())) {
            destCollection.add(source);
        } else {
            final Converter converter = context.getConverterFactory()
                    .getConverter(source.getClass(), destElementClass);
            destCollection.add(converter.convert(source, destElementClass,
                    context));
        }
    }

    /**
     * コレクションの要素型を返します。
     * 
     * @param parameterizedClassDesc
     *            パラメタ化された型の情報
     * @return パラメタ化されたコレクションの要素型
     */
    protected Class getElementClass(
            final ParameterizedClassDesc parameterizedClassDesc) {
        if (parameterizedClassDesc == null
                || !parameterizedClassDesc.isParameterizedClass()) {
            return null;
        }
        final ParameterizedClassDesc[] arguments = parameterizedClassDesc
                .getArguments();
        if (arguments.length != 1) {
            return null;
        }
        return arguments[0].getRawClass();
    }

    /**
     * 配列からコレクションに変換します。
     * 
     * @param source
     *            変換元の配列
     * @param dest
     *            変換先のコレクション
     * @param destElementClass
     *            変換先コレクションの要素型
     * @param context
     *            変換コンテキスト
     */
    protected void convertFromArray(final Object source, final Collection dest,
            final Class destElementClass, final ConversionContext context) {
        if (destElementClass == null) {
            convert(source, dest, context);
            return;
        }
        final Class sourceElementClass = source.getClass().getComponentType();
        final Converter converter = context.getConverterFactory().getConverter(
                sourceElementClass, destElementClass);
        final int length = Array.getLength(source);
        for (int i = 0; i < length; ++i) {
            final Object sourceElement = Array.get(source, i);
            if (sourceElement == null) {
                dest.add(null);
            } else if (shallowCopy
                    && destElementClass.isAssignableFrom(sourceElement
                            .getClass())) {
                dest.add(sourceElement);
            } else {
                dest.add(converter.convert(sourceElement, destElementClass,
                        context));
            }
        }
    }

    /**
     * コレクションからコレクションに変換します。
     * 
     * @param source
     *            変換元のコレクション
     * @param dest
     *            変換先のコレクション
     * @param destElementClass
     *            変換先コレクションの要素型
     * @param context
     *            変換コンテキスト
     */
    protected void convertFromCollection(final Collection source,
            final Collection dest, final Class destElementClass,
            final ConversionContext context) {
        if (destElementClass == null) {
            convert(source, dest, context);
            return;
        }
        for (final Iterator it = source.iterator(); it.hasNext();) {
            final Object sourceElement = it.next();
            if (sourceElement == null) {
                dest.add(null);
            } else if (shallowCopy
                    && destElementClass.isAssignableFrom(sourceElement
                            .getClass())) {
                dest.add(sourceElement);
            } else {
                final Converter converter = context.getConverterFactory()
                        .getConverter(sourceElement.getClass(),
                                destElementClass);
                dest.add(converter.convert(sourceElement, destElementClass,
                        context));
            }
        }
    }

}
