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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.seasar.extension.dxo.converter.ConversionContext;
import org.seasar.extension.dxo.converter.Converter;
import org.seasar.extension.dxo.converter.DatePropertyInfo;
import org.seasar.extension.dxo.converter.NestedPropertyInfo;
import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.exception.InstantiationRuntimeException;
import org.seasar.framework.util.ClassUtil;

/**
 * JavaBeansからJavaBeansへの変換を行うコンバータです。
 * 
 * @author Satoshi Kimura
 * @author koichik
 */
public class BeanConverter extends AbstractConverter {

    /** 変換元にプロパティが存在しないことを示すオブジェクトです。 */
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

    /**
     * 変換先オブジェクトのプロパティに値を設定します。
     * 
     * @param source
     *            変換元オブジェクト
     * @param dest
     *            変換先オブジェクト
     * @param context
     *            変換コンテキスト
     */
    protected void setValues(final Object source, final Object dest,
            final ConversionContext context) {
        final BeanDesc sourceBeanDesc = BeanDescFactory.getBeanDesc(source
                .getClass());
        final BeanDesc destBeanDesc = BeanDescFactory.getBeanDesc(dest
                .getClass());
        final int size = destBeanDesc.getPropertyDescSize();
        for (int i = 0; i < size; i++) {
            final PropertyDesc destPropertyDesc = destBeanDesc
                    .getPropertyDesc(i);
            if (!destPropertyDesc.isWritable()) {
                continue;
            }
            setValue(sourceBeanDesc, source, destBeanDesc, dest,
                    destPropertyDesc, context);
        }
    }

    /**
     * <code>destPropertyDesc</code>で示される変換先オブジェクトのプロパティに値を設定します。
     * 
     * @param sourceBeanDesc
     *            変換元のBean記述子
     * @param source
     *            変換元のオブジェクト
     * @param destBeanDesc
     *            変換先のBean記述子
     * @param dest
     *            変換先のオブジェクト
     * @param destPropertyDesc
     *            変換先のプロパティ記述子
     * @param context
     *            変換コンテキスト
     */
    protected void setValue(final BeanDesc sourceBeanDesc, final Object source,
            final BeanDesc destBeanDesc, final Object dest,
            final PropertyDesc destPropertyDesc, final ConversionContext context) {
        final String destPropertyName = destPropertyDesc.getPropertyName();
        final Class destPropertyClass = destPropertyDesc.getPropertyType();
        final Object sourcePropertyValue = getSourceValue(sourceBeanDesc,
                source, destPropertyName, context);
        if (sourcePropertyValue == PROPERTY_NOT_FOUND) {
            final Object dateValue = getDateValue(sourceBeanDesc, source,
                    destPropertyName, destPropertyClass, context);
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
        if (sourcePropertyClass == destPropertyClass) {
            destPropertyDesc.setValue(dest, sourcePropertyValue);
            return;
        }
        final Converter converter = getConverter(sourcePropertyClass, dest
                .getClass(), destPropertyName, destPropertyClass, context);
        final Object convertedValue = converter.convert(sourcePropertyValue,
                destPropertyClass, context);
        destPropertyDesc.setValue(dest, convertedValue);
    }

    /**
     * 変換元オブジェクトからプロパティの値を取得して返します。
     * <p>
     * 変換元オブジェクトに該当するプロパティが存在しない場合は{@link #PROPERTY_NOT_FOUND}を返します。
     * </p>
     * 
     * @param sourceBeanDesc
     *            変換元のBean記述子
     * @param source
     *            変換元のオブジェクト
     * @param destPropertyName
     *            変換対象のプロパティ名
     * @param context
     *            変換コンテキスト
     * @return 変換元オブジェクトのプロパティの値
     */
    protected Object getSourceValue(final BeanDesc sourceBeanDesc,
            final Object source, final String destPropertyName,
            final ConversionContext context) {
        final String sourcePropertyName = context
                .getSourcePropertyName(destPropertyName);
        if (context.hasEvalueatedValue(sourcePropertyName)) {
            return context.getEvaluatedValue(sourcePropertyName);
        }

        if (sourceBeanDesc.hasPropertyDesc(sourcePropertyName)) {
            final PropertyDesc sourcePropertyDesc = sourceBeanDesc
                    .getPropertyDesc(sourcePropertyName);
            if (!sourcePropertyDesc.isReadable()) {
                return PROPERTY_NOT_FOUND;
            }
            return sourcePropertyDesc.getValue(source);
        }
        return resolveNestedProperty(sourceBeanDesc, source,
                sourcePropertyName, context);
    }

    /**
     * 変換元オブジェクトからネストしたプロパティの値を取得して返します。
     * <p>
     * 変換元オブジェクトに該当するプロパティが存在しない場合は{@link #PROPERTY_NOT_FOUND}を返します。
     * </p>
     * 
     * @param sourceBeanDesc
     *            変換元のBean記述子
     * @param source
     *            変換元のオブジェクト
     * @param propertyName
     *            変換対象のプロパティ名
     * @param context
     *            変換コンテキスト
     * @return ネストしたプロパティの値
     */
    protected Object resolveNestedProperty(final BeanDesc sourceBeanDesc,
            final Object source, final String propertyName,
            final ConversionContext context) {
        final NestedPropertyInfo info = context.getNestedPropertyInfo(
                sourceBeanDesc.getBeanClass(), propertyName);
        if (info == null) {
            return PROPERTY_NOT_FOUND;
        }
        return info.getValue(source);
    }

    /**
     * 変換元オブジェクトから日時プロパティの値を取得して返します。
     * <p>
     * 変換元オブジェクトに該当するプロパティが存在しない場合は<code>null</code>を返します。
     * </p>
     * 
     * @param sourceBeanDesc
     *            変換元のBean記述子
     * @param source
     *            変換元のオブジェクト
     * @param destPropertyName
     *            変換先のプロパティ名
     * @param destPropertyType
     *            変換先のプロパティ型
     * @param context
     *            変換コンテキスト
     * @return 日時プロパティの値
     */
    protected Object getDateValue(final BeanDesc sourceBeanDesc,
            final Object source, final String destPropertyName,
            final Class destPropertyType, final ConversionContext context) {
        if (destPropertyType == String.class) {
            return getDateValueAsString(sourceBeanDesc, source,
                    destPropertyName);
        }
        if (Date.class.isAssignableFrom(destPropertyType)) {
            final Date dateValue = getDateValueAsDate(sourceBeanDesc
                    .getBeanClass(), source, destPropertyName, context);
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
            final Date dateValue = getDateValueAsDate(sourceBeanDesc
                    .getBeanClass(), source, destPropertyName, context);
            if (dateValue != null) {
                final Calendar calendar = Calendar.getInstance();
                calendar.setTime(dateValue);
                return calendar;
            }
        }
        return null;
    }

    /**
     * 変換元オブジェクトから日時プロパティの値を文字列として取得し、返します。
     * <p>
     * 変換元オブジェクトに該当するプロパティが存在しない場合は<code>null</code>を返します。
     * </p>
     * 
     * @param sourceBeanDesc
     *            変換元のBean記述子
     * @param source
     *            変換元のオブジェクト
     * @param destPropertyName
     *            変換先のプロパティ名
     * @return 日時プロパティの値
     */
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
        if (!sourcePropertyDesc.isReadable()) {
            return null;
        }

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

    /**
     * 変換元オブジェクトから日時プロパティの値を{@link java.util.Date}として取得し、返します。
     * <p>
     * 変換元オブジェクトに該当するプロパティが存在しない場合は<code>null</code>を返します。
     * </p>
     * 
     * @param sourceClass
     *            変換元のクラス
     * @param source
     *            変換元のオブジェクト
     * @param destPropertyName
     *            変換先のプロパティ名
     * @param context
     *            変換コンテキスト
     * @return 日時プロパティの値
     */
    protected Date getDateValueAsDate(final Class sourceClass,
            final Object source, final String destPropertyName,
            final ConversionContext context) {
        final DatePropertyInfo info = context.getDatePropertyInfo(sourceClass,
                destPropertyName);
        if (info == null) {
            return null;
        }
        return info.getValue(source);
    }

    /**
     * コンバータを取得して返します。
     * 
     * @param sourcePropertyClass
     *            変換元プロパティの型
     * @param destClass
     *            変換先のクラス
     * @param destPropertyName
     *            変換先のプロパティ名
     * @param destPropertyClass
     *            変換先のプロパティ型
     * @param context
     *            変換コンテキスト
     * @return コンバータ
     */
    protected Converter getConverter(final Class sourcePropertyClass,
            final Class destClass, final String destPropertyName,
            final Class destPropertyClass, final ConversionContext context) {
        final Converter converter = context.getConverter(destClass,
                destPropertyName);
        if (converter != null) {
            return converter;
        }
        return context.getConverterFactory().getConverter(sourcePropertyClass,
                destPropertyClass);
    }

}
