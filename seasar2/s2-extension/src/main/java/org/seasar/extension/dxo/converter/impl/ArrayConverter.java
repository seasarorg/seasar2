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
package org.seasar.extension.dxo.converter.impl;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;

import org.seasar.extension.dxo.converter.ConversionContext;
import org.seasar.extension.dxo.converter.Converter;
import org.seasar.extension.dxo.converter.ConverterFactory;

/**
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

        if (destClass.isAssignableFrom(source.getClass())) {
            return source;
        }
        if (source.getClass().isArray()) {
            return toArray(destClass.getComponentType(), (Object[]) source,
                    context);
        }
        if (source instanceof Collection) {
            return toArray(destClass.getComponentType(), (Collection) source,
                    context);
        }
        final Object[] result = (Object[]) Array.newInstance(destClass
                .getComponentType(), 1);
        result[0] = source;
        return result;
    }

    protected Object toArray(final Class componentType, final Object[] source,
            final ConversionContext context) {
        final int length = source.length;
        final Object[] result = (Object[]) Array.newInstance(componentType,
                length);
        if (length == 0) {
            return result;
        }

        final ConverterFactory converterFactory = context.getConverterFactory();
        for (int i = 0; i < length; i++) {
            final Object sourceElement = source[i];
            final Converter converter = converterFactory.getConverter(
                    sourceElement.getClass(), componentType);
            result[i] = converter
                    .convert(sourceElement, componentType, context);
        }
        return result;
    }

    protected Object toArray(final Class componentType,
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
