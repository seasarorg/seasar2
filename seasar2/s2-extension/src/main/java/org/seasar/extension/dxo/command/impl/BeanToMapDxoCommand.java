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
package org.seasar.extension.dxo.command.impl;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.seasar.extension.dxo.annotation.AnnotationReader;
import org.seasar.extension.dxo.converter.ConversionContext;
import org.seasar.extension.dxo.converter.Converter;
import org.seasar.extension.dxo.converter.ConverterFactory;
import org.seasar.extension.dxo.util.DxoUtil;
import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.util.OgnlUtil;
import org.seasar.framework.util.StringUtil;

/**
 * @author koichik
 * 
 */
public class BeanToMapDxoCommand extends AbstractDxoCommand {

    protected Object parsedExpression;

    protected boolean excludeNull;

    protected String sourcePrefix;

    protected Class valueType;

    public BeanToMapDxoCommand(final Class dxoClass, final Method method,
            final ConverterFactory converterFactory,
            final AnnotationReader annotationReader) {
        this(dxoClass, method, converterFactory, annotationReader, null);
    }

    public BeanToMapDxoCommand(final Class dxoClass, final Method method,
            final ConverterFactory converterFactory,
            final AnnotationReader annotationReader, final String expression) {
        super(dxoClass, method, converterFactory, annotationReader);
        if (expression != null) {
            parsedExpression = DxoUtil.parseMap(expression);
        }
        excludeNull = annotationReader.isExcludeNull(dxoClass, method);
        sourcePrefix = annotationReader.getSourcePrefix(dxoClass, method);
        valueType = DxoUtil.getValueTypeOfTargetMap(method);
        if (valueType == Object.class) {
            valueType = null;
        }
    }

    protected Object convertScalar(final Object source) {
        final Map dest;
        if (parsedExpression != null) {
            dest = (Map) OgnlUtil.getValue(parsedExpression, source);
        } else {
            final String expression = createConversionRule(source.getClass());
            dest = (Map) OgnlUtil
                    .getValue(DxoUtil.parseMap(expression), source);
        }
        if (excludeNull) {
            removeNullEntry(dest);
        }
        if (valueType == null) {
            return dest;
        }
        return convertValueType(dest, createContext(source));
    }

    protected void convertScalar(final Object source, final Object dest) {
        ((Map) dest).putAll((Map) convertScalar(source));
    }

    protected void removeNullEntry(final Map map) {
        for (final Iterator it = map.entrySet().iterator(); it.hasNext();) {
            final Entry entry = (Entry) it.next();
            if (entry.getValue() == null) {
                it.remove();
            }
        }
    }

    protected Map convertValueType(final Map from,
            final ConversionContext context) {
        final Map to = new LinkedHashMap();
        for (final Iterator it = from.entrySet().iterator(); it.hasNext();) {
            final Entry entry = (Entry) it.next();
            final Object key = entry.getKey();
            final Object value = entry.getValue();
            if (value == null || valueType.isInstance(value)) {
                to.put(key, value);
            } else {
                final Converter converter = converterFactory.getConverter(value
                        .getClass(), valueType);
                to.put(key, converter.convert(value, valueType, context));
            }
        }
        return to;
    }

    protected Class getDestElementType() {
        return Map.class;
    }

    protected String createConversionRule(final Class sourceType) {
        final StringBuffer buf = new StringBuffer(100);
        final BeanDesc beanDesc = BeanDescFactory.getBeanDesc(sourceType);
        final int propertySize = beanDesc.getPropertyDescSize();
        for (int i = 0; i < propertySize; ++i) {
            final PropertyDesc propertyDesc = beanDesc.getPropertyDesc(i);
            if (propertyDesc.hasReadMethod()) {
                final String sourcePropertyName = propertyDesc
                        .getPropertyName();
                final String destPropertyName = toDestPropertyName(sourcePropertyName);
                if (destPropertyName == null) {
                    continue;
                }
                buf.append("'").append(destPropertyName).append("': ").append(
                        sourcePropertyName).append(", ");
            }
        }
        if (propertySize > 0) {
            buf.setLength(buf.length() - 2);
        }
        return new String(buf);
    }

    protected String toDestPropertyName(final String sourcePropertyName) {
        if (StringUtil.isEmpty(sourcePrefix)) {
            return sourcePropertyName;
        }
        if (!sourcePropertyName.startsWith(sourcePrefix)) {
            return null;
        }
        return StringUtil.decapitalize(sourcePropertyName
                .substring(sourcePrefix.length()));
    }
}
