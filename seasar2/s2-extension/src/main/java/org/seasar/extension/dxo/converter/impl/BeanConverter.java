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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

    protected static final String JAVA = "java.";

    protected static final String JAVAX = "javax.";

    protected static final Object PROPERTY_NOT_FOUND = new Object();

    public Class[] getSourceClasses() {
        return new Class[] { Object.class };
    }

    public Class getDestClass() {
        return Object.class;
    }

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
            convert(source, dest, context);
            return dest;
        } catch (final InstantiationRuntimeException ignore) {
        }
        return null;
    }

    public void convert(final Object source, final Object dest,
            final ConversionContext context) {
        context.addConvertedObject(source, dest);
        setValues(source, dest, context);
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
            setValue(sourceBeanDesc, source, destBeanDesc, dest,
                    destPropertyDesc, context);
        }
    }

    protected void setValue(final BeanDesc sourceBeanDesc, final Object source,
            final BeanDesc destBeanDesc, final Object dest,
            final PropertyDesc destPropertyDesc, final ConversionContext context) {
        final String destPropertyName = destPropertyDesc.getPropertyName();
        final Object sourcePropertyValue = getSourceValue(sourceBeanDesc,
                source, destPropertyName, context);
        if (sourcePropertyValue == PROPERTY_NOT_FOUND) {
            final Object dateValue = getDateValue(sourceBeanDesc, source,
                    destPropertyDesc);
            if (dateValue != null) {
                destPropertyDesc.setValue(dest, dateValue);
            }
            return;
        }
        if (sourcePropertyValue == null) {
            if (context.isIncludeNull()) {
                destPropertyDesc.setValue(dest, null);
            }
            return;
        }
        final Class sourcePropertyClass = sourcePropertyValue.getClass();
        final Class destPropertyClass = destPropertyDesc.getPropertyType();
        final Converter converter = getConverter(sourcePropertyClass, dest
                .getClass(), destPropertyClass, destPropertyName, context);
        final Object convertedValue = converter.convert(sourcePropertyValue,
                destPropertyClass, context);
        destPropertyDesc.setValue(dest, convertedValue);
    }

    protected Object getSourceValue(final BeanDesc sourceBeanDesc,
            final Object source, final String propertyName,
            final ConversionContext context) {
        if (context.hasEvalueatedValue(propertyName)) {
            return context.getEvaluatedValue(propertyName);
        }

        if (sourceBeanDesc.hasPropertyDesc(propertyName)) {
            final PropertyDesc sourcePropertyDesc = sourceBeanDesc
                    .getPropertyDesc(propertyName);
            if (!sourcePropertyDesc.hasReadMethod()) {
                return PROPERTY_NOT_FOUND;
            }
            return sourcePropertyDesc.getValue(source);
        }
        return resolveNestedProperty(sourceBeanDesc, source, propertyName,
                context);
    }

    protected Object resolveNestedProperty(final BeanDesc sourceBeanDesc,
            final Object source, final String propertyName,
            final ConversionContext context) {
        for (int i = 0; i < sourceBeanDesc.getPropertyDescSize(); ++i) {
            final PropertyDesc propertyDesc = sourceBeanDesc.getPropertyDesc(i);
            final Class propertyType = propertyDesc.getPropertyType();
            if (!propertyDesc.hasReadMethod() || isBasicType(propertyType)) {
                continue;
            }
            final Object sourcePropertyValue = propertyDesc.getValue(source);
            if (sourcePropertyValue == null) {
                continue;
            }

            final BeanDesc nestedBeanDesc = BeanDescFactory
                    .getBeanDesc(propertyType);
            if (nestedBeanDesc.hasPropertyDesc(propertyName)) {
                final PropertyDesc nestedPropertyDesc = nestedBeanDesc
                        .getPropertyDesc(propertyName);
                if (nestedPropertyDesc.hasReadMethod()) {
                    return nestedPropertyDesc.getValue(sourcePropertyValue);
                }
            }
        }
        return PROPERTY_NOT_FOUND;
    }

    protected boolean isBasicType(final Object object) {
        return isBasicType(object.getClass());
    }

    protected boolean isBasicType(final Class clazz) {
        if (clazz.isPrimitive() || clazz.isArray()) {
            return true;
        }
        final String className = clazz.getName();
        return className.startsWith(JAVA) || className.startsWith(JAVAX);
    }

    protected Object getDateValue(final BeanDesc sourceBeanDesc,
            final Object source, final PropertyDesc destPropertyDesc) {
        final String destPropertyName = destPropertyDesc.getPropertyName();
        final Class destPropertyType = destPropertyDesc.getPropertyType();
        if (destPropertyType == String.class) {
            return getDateValueAsString(sourceBeanDesc, source,
                    destPropertyName);
        }
        if (Date.class.isAssignableFrom(destPropertyType)) {
            final Date dateValue = getDateValueAsDate(sourceBeanDesc, source,
                    destPropertyName);
            if (dateValue != null) {
                if (destPropertyType.isAssignableFrom(Date.class)) {
                    return dateValue;
                }
                if (destPropertyType.isAssignableFrom(java.sql.Date.class)) {
                    return new java.sql.Date(dateValue.getTime());
                }
                if (destPropertyType.isAssignableFrom(java.sql.Time.class)) {
                    return new java.sql.Time(dateValue.getTime());
                }
                if (destPropertyType.isAssignableFrom(java.sql.Timestamp.class)) {
                    return new java.sql.Timestamp(dateValue.getTime());
                }
            }
        }
        if (destPropertyType.isAssignableFrom(Calendar.class)) {
            final Date dateValue = getDateValueAsDate(sourceBeanDesc, source,
                    destPropertyName);
            if (dateValue != null) {
                final Calendar calendar = Calendar.getInstance();
                calendar.setTime(dateValue);
                return calendar;
            }
        }
        return null;
    }

    protected String getDateValueAsString(final BeanDesc sourceBeanDesc,
            final Object source, final String destPropertyName) {
        final int pos = destPropertyName.lastIndexOf('_');
        if (pos == -1 || pos >= destPropertyName.length() - 1) {
            return null;
        }

        final String sourcePropertyName = destPropertyName.substring(0, pos);
        if (!sourceBeanDesc.hasPropertyDesc(sourcePropertyName)) {
            return null;
        }

        final PropertyDesc sourcePropertyDesc = sourceBeanDesc
                .getPropertyDesc(sourcePropertyName);
        final Class sourcePropertyType = sourcePropertyDesc.getPropertyType();
        if (!Date.class.isAssignableFrom(sourcePropertyType)
                && !Calendar.class.isAssignableFrom(sourcePropertyType)) {
            return null;
        }

        final Object sourceValue = sourcePropertyDesc.getValue(source);
        if (sourceValue == null) {
            return null;
        }

        final String format = destPropertyName.substring(pos + 1);
        final DateFormat formatter = new SimpleDateFormat(format);
        if (sourceValue instanceof Date) {
            return formatter.format((Date) sourceValue);
        } else if (sourceValue instanceof Calendar) {
            final Calendar calendar = (Calendar) sourceValue;
            return formatter.format(new Date(calendar.getTimeInMillis()));
        }
        return null;
    }

    protected Date getDateValueAsDate(final BeanDesc sourceBeanDesc,
            final Object source, final String destPropertyName) {
        final String prefix = destPropertyName + "_";
        final int starts = prefix.length();
        final StringBuffer formatBuffer = new StringBuffer();
        final StringBuffer valueBuffer = new StringBuffer();
        for (int i = 0; i < sourceBeanDesc.getPropertyDescSize(); ++i) {
            final PropertyDesc sourcePropertyDesc = sourceBeanDesc
                    .getPropertyDesc(i);
            if (sourcePropertyDesc.getPropertyType() != String.class) {
                continue;
            }
            final String sourcePropertyName = sourcePropertyDesc
                    .getPropertyName();
            if (!sourcePropertyName.startsWith(prefix)) {
                continue;
            }
            formatBuffer.append(sourcePropertyName.substring(starts));
            valueBuffer.append(sourcePropertyDesc.getValue(source));
        }
        final String format = new String(formatBuffer);
        final String stringValue = new String(valueBuffer);
        final DateFormat formatter = new SimpleDateFormat(format);
        try {
            return formatter.parse(stringValue);
        } catch (final ParseException ignore) {
        }
        return null;
    }

    protected Converter getConverter(final Class sourcePropertyClass,
            final Class destClass, final Class destPropertyClass,
            final String destPropertyName, final ConversionContext context) {
        final Converter converter = context.getConverter(destClass,
                destPropertyName);
        if (converter != null) {
            return converter;
        }
        return context.getConverterFactory().getConverter(sourcePropertyClass,
                destPropertyClass);
    }
}
