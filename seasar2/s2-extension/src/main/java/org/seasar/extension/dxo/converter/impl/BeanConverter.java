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
package org.seasar.extension.dxo.converter.impl;

import org.seasar.extension.dxo.converter.ConversionContext;
import org.seasar.extension.dxo.converter.Converter;
import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.exception.InstantiationRuntimeException;
import org.seasar.framework.util.ClassUtil;

/**
 * @author Satoshi Kimura
 * @author koichik
 */
public class BeanConverter extends AbstractConverter {

    protected static final Object PROPERTY_NOT_FOUND = new Object();

    public Object convert(final Object source, final Class destClass,
            final ConversionContext context) {
        if (source == null) {
            return null;
        }

        final Class sourceClass = source.getClass();
        if (destClass.isAssignableFrom(sourceClass)) {
            return source;
        }

        final Object converted = context.getConvertedObject(source);
        if (converted != null) {
            return converted;
        }

        try {
            final Object dest = ClassUtil.newInstance(destClass);
            context.addConvertedObject(source, dest);
            setValues(source, dest, context);
            return dest;
        } catch (final InstantiationRuntimeException ignore) {
        }
        return null;
    }

    protected void setValues(final Object source, final Object dest,
            final ConversionContext context) {
        final BeanDesc sourceBeanDesc = BeanDescFactory.getBeanDesc(source
                .getClass());
        final BeanDesc destBeanDesc = BeanDescFactory.getBeanDesc(dest
                .getClass());
        for (int i = 0; i < destBeanDesc.getPropertyDescSize(); i++) {
            final PropertyDesc destPropertyDesc = destBeanDesc
                    .getPropertyDesc(i);
            if (!destPropertyDesc.hasWriteMethod()) {
                continue;
            }
            final String destPropertyName = destPropertyDesc.getPropertyName();
            final Object sourcePropertyValue = getSourceValue(sourceBeanDesc,
                    source, destPropertyName, context);
            if (sourcePropertyValue == PROPERTY_NOT_FOUND) {
                continue;
            }
            final Class destPropertyClass = destPropertyDesc.getPropertyType();
            final Converter converter = context.getConverterFactory()
                    .getConverter(sourcePropertyValue.getClass(),
                            destPropertyClass);
            destPropertyDesc.setValue(dest, converter.convert(
                    sourcePropertyValue, destPropertyClass, context));
        }
    }

    protected Object getSourceValue(final BeanDesc sourceBeanDesc,
            final Object source, final String destPropertyName,
            final ConversionContext context) {
        // TODO convert property name
        if (!sourceBeanDesc.hasPropertyDesc(destPropertyName)) {
            return PROPERTY_NOT_FOUND;
        }
        final PropertyDesc sourcePropertyDesc = sourceBeanDesc
                .getPropertyDesc(destPropertyName);
        if (!sourcePropertyDesc.hasReadMethod()) {
            return PROPERTY_NOT_FOUND;
        }
        return sourcePropertyDesc.getValue(source);
    }

}
