/*
 * Copyright 2004-2015 the Seasar Foundation and the Others.
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
package org.seasar.framework.beans.util;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.Converter;
import org.seasar.framework.beans.ConverterRuntimeException;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.converter.DateConverter;
import org.seasar.framework.beans.converter.NumberConverter;
import org.seasar.framework.beans.converter.SqlDateConverter;
import org.seasar.framework.beans.converter.TimeConverter;
import org.seasar.framework.beans.converter.TimestampConverter;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.util.DateConversionUtil;
import org.seasar.framework.util.TimeConversionUtil;
import org.seasar.framework.util.TimestampConversionUtil;

/**
 * JavaBeansやMapに対して操作を行う抽象クラスです。
 * 
 * @author higa
 * @param <S>
 *            JavaBeansに対して操作を行うサブタイプです。
 * 
 */
public abstract class AbstractCopy<S extends AbstractCopy<S>> {

    /**
     * 空文字列の配列です。
     */
    protected static final String[] EMPTY_STRING_ARRAY = new String[0];

    /**
     * 日付用のデフォルトコンバータです。
     */
    protected static final Converter DEFAULT_DATE_CONVERTER = new DateConverter(
            DateConversionUtil.getY4Pattern(Locale.getDefault()));

    /**
     * 日時用のデフォルトコンバータです。
     */
    protected static final Converter DEFAULT_TIMESTAMP_CONVERTER = new DateConverter(
            TimestampConversionUtil.getPattern(Locale.getDefault()));

    /**
     * 時間用のデフォルトコンバータです。
     */
    protected static final Converter DEFAULT_TIME_CONVERTER = new DateConverter(
            TimeConversionUtil.getPattern(Locale.getDefault()));

    /**
     * 操作の対象に含めるプロパティ名の配列です。
     */
    protected String[] includePropertyNames = EMPTY_STRING_ARRAY;

    /**
     * 操作の対象に含めないプロパティ名の配列です。
     */
    protected String[] excludePropertyNames = EMPTY_STRING_ARRAY;

    /**
     * null値のプロパティを操作の対象外にするかどうかです。
     */
    protected boolean excludesNull = false;

    /**
     * 空白を操作の対象外にするかどうかです。
     */
    protected boolean excludesWhitespace = false;

    /**
     * プレフィックスです。
     */
    protected String prefix;

    /**
     * JavaBeanのデリミタです。
     */
    protected char beanDelimiter = '$';

    /**
     * Mapのデリミタです。
     */
    protected char mapDelimiter = '.';

    /**
     * 特定のプロパティに関連付けられたコンバータです。
     */
    protected Map<String, Converter> converterMap = new HashMap<String, Converter>();

    /**
     * 特定のプロパティに関連付けられていないコンバータです。
     */
    protected List<Converter> converters = new ArrayList<Converter>();

    /**
     * CharSequenceの配列をStringの配列に変換します。
     * 
     * @param charSequenceArray
     *            CharSequenceの配列
     * @return Stringの配列
     */
    protected String[] toStringArray(CharSequence[] charSequenceArray) {
        int length = charSequenceArray.length;
        String[] stringArray = new String[length];
        for (int index = 0; index < length; index++) {
            stringArray[index] = charSequenceArray[index].toString();
        }
        return stringArray;
    }

    /**
     * 操作の対象に含めるプロパティ名を指定します。
     * 
     * @param propertyNames
     *            プロパティ名の配列
     * @return このインスタンス自身
     */
    @SuppressWarnings("unchecked")
    public S includes(CharSequence... propertyNames) {
        this.includePropertyNames = toStringArray(propertyNames);
        return (S) this;
    }

    /**
     * 操作の対象に含めないプロパティ名を指定します。
     * 
     * @param propertyNames
     *            プロパティ名の配列
     * @return このインスタンス自身
     */
    @SuppressWarnings("unchecked")
    public S excludes(CharSequence... propertyNames) {
        this.excludePropertyNames = toStringArray(propertyNames);
        return (S) this;
    }

    /**
     * null値のプロパティを操作の対象外にします。
     * 
     * @return このインスタンス自身
     */
    @SuppressWarnings("unchecked")
    public S excludesNull() {
        this.excludesNull = true;
        return (S) this;
    }

    /**
     * 空白のプロパティを操作の対象外にします。
     * 
     * @return このインスタンス自身
     */
    @SuppressWarnings("unchecked")
    public S excludesWhitespace() {
        this.excludesWhitespace = true;
        return (S) this;
    }

    /**
     * プレフィックスを指定します。
     * 
     * @param prefix
     *            プレフィックス
     * @return このインスタンス自身
     * 
     */
    @SuppressWarnings("unchecked")
    public S prefix(CharSequence prefix) {
        this.prefix = prefix.toString();
        return (S) this;
    }

    /**
     * JavaBeansのデリミタを設定します。
     * 
     * @param beanDelimiter
     *            JavaBeansのデリミタ
     * @return このインスタンス自身
     */
    @SuppressWarnings("unchecked")
    public S beanDelimiter(char beanDelimiter) {
        this.beanDelimiter = beanDelimiter;
        return (S) this;
    }

    /**
     * Mapのデリミタを設定します。
     * 
     * @param mapDelimiter
     *            Mapのデリミタ
     * @return このインスタンス自身
     */
    @SuppressWarnings("unchecked")
    public S mapDelimiter(char mapDelimiter) {
        this.mapDelimiter = mapDelimiter;
        return (S) this;
    }

    /**
     * コンバータを設定します。
     * 
     * @param converter
     * @param propertyNames
     * @return このインスタンス自身
     */
    @SuppressWarnings("unchecked")
    public S converter(Converter converter, CharSequence... propertyNames) {
        if (propertyNames.length == 0) {
            converters.add(converter);
        } else {
            for (CharSequence name : propertyNames) {
                converterMap.put(name.toString(), converter);
            }
        }
        return (S) this;
    }

    /**
     * 日付のコンバータを設定します。
     * 
     * @param pattern
     *            日付のパターン
     * @param propertyNames
     *            プロパティ名の配列
     * @return このインスタンス自身
     */
    public S dateConverter(String pattern, CharSequence... propertyNames) {
        return converter(new DateConverter(pattern), propertyNames);
    }

    /**
     * SQL用日付のコンバータを設定します。
     * 
     * @param pattern
     *            日付のパターン
     * @param propertyNames
     *            プロパティ名の配列
     * @return このインスタンス自身
     */
    public S sqlDateConverter(String pattern, CharSequence... propertyNames) {
        return converter(new SqlDateConverter(pattern), propertyNames);
    }

    /**
     * 時間のコンバータを設定します。
     * 
     * @param pattern
     *            時間のパターン
     * @param propertyNames
     *            プロパティ名の配列
     * @return このインスタンス自身
     */
    public S timeConverter(String pattern, CharSequence... propertyNames) {
        return converter(new TimeConverter(pattern), propertyNames);
    }

    /**
     * 日時のコンバータを設定します。
     * 
     * @param pattern
     *            日時のパターン
     * @param propertyNames
     *            プロパティ名の配列
     * @return このインスタンス自身
     */
    public S timestampConverter(String pattern, CharSequence... propertyNames) {
        return converter(new TimestampConverter(pattern), propertyNames);
    }

    /**
     * 数値のコンバータを設定します。
     * 
     * @param pattern
     *            数値のパターン
     * @param propertyNames
     *            プロパティ名の配列
     * @return このインスタンス自身
     */
    public S numberConverter(String pattern, CharSequence... propertyNames) {
        return converter(new NumberConverter(pattern), propertyNames);
    }

    /**
     * 対象のプロパティかどうかを返します。
     * 
     * @param name
     *            プロパティ名
     * @return 対象のプロパティかどうか
     */
    protected boolean isTargetProperty(String name) {
        if (prefix != null && !name.startsWith(prefix)) {
            return false;
        }
        if (includePropertyNames.length > 0) {
            for (String s : includePropertyNames) {
                if (s.equals(name)) {
                    for (String s2 : excludePropertyNames) {
                        if (s2.equals(name)) {
                            return false;
                        }
                    }
                    return true;
                }
            }
            return false;
        }
        if (excludePropertyNames.length > 0) {
            for (String s : excludePropertyNames) {
                if (s.equals(name)) {
                    return false;
                }
            }
            return true;
        }
        return true;
    }

    /**
     * BeanからBeanにコピーを行います。
     * 
     * @param src
     *            コピー元
     * @param dest
     *            コピー先
     */
    protected void copyBeanToBean(Object src, Object dest) {
        BeanDesc srcBeanDesc = BeanDescFactory.getBeanDesc(src.getClass());
        BeanDesc destBeanDesc = BeanDescFactory.getBeanDesc(dest.getClass());
        int size = srcBeanDesc.getPropertyDescSize();
        for (int i = 0; i < size; i++) {
            PropertyDesc srcPropertyDesc = srcBeanDesc.getPropertyDesc(i);
            String srcPropertyName = srcPropertyDesc.getPropertyName();
            if (!srcPropertyDesc.isReadable()
                    || !isTargetProperty(srcPropertyName)) {
                continue;
            }
            String destPropertyName = trimPrefix(srcPropertyName);
            if (!destBeanDesc.hasPropertyDesc(destPropertyName)) {
                continue;
            }
            PropertyDesc destPropertyDesc = destBeanDesc
                    .getPropertyDesc(destPropertyName);
            if (!destPropertyDesc.isWritable()) {
                continue;
            }
            Object value = srcPropertyDesc.getValue(src);
            if (value instanceof String && excludesWhitespace
                    && ((String) value).trim().length() == 0) {
                continue;
            }
            if (value == null && excludesNull) {
                continue;
            }
            value = convertValue(value, destPropertyName, destPropertyDesc
                    .getPropertyType());
            destPropertyDesc.setValue(dest, value);
        }
    }

    /**
     * BeanからMapにコピーを行います。
     * 
     * @param src
     *            コピー元
     * @param dest
     *            コピー先
     */
    @SuppressWarnings("unchecked")
    protected void copyBeanToMap(Object src, Map dest) {
        BeanDesc srcBeanDesc = BeanDescFactory.getBeanDesc(src.getClass());
        int size = srcBeanDesc.getPropertyDescSize();
        for (int i = 0; i < size; i++) {
            PropertyDesc srcPropertyDesc = srcBeanDesc.getPropertyDesc(i);
            String srcPropertyName = srcPropertyDesc.getPropertyName();
            if (!srcPropertyDesc.isReadable()
                    || !isTargetProperty(srcPropertyName)) {
                continue;
            }
            Object value = srcPropertyDesc.getValue(src);
            if (value instanceof String && excludesWhitespace
                    && ((String) value).trim().length() == 0) {
                continue;
            }
            if (value == null && excludesNull) {
                continue;
            }
            String destPropertyName = trimPrefix(srcPropertyName.replace(
                    beanDelimiter, mapDelimiter));
            value = convertValue(value, destPropertyName, null);
            dest.put(destPropertyName, value);
        }
    }

    /**
     * MapからBeanにコピーを行います。
     * 
     * @param src
     *            コピー元
     * @param dest
     *            コピー先
     */
    protected void copyMapToBean(Map<String, Object> src, Object dest) {
        BeanDesc destBeanDesc = BeanDescFactory.getBeanDesc(dest.getClass());
        for (Iterator<String> i = src.keySet().iterator(); i.hasNext();) {
            String srcPropertyName = i.next();
            if (!isTargetProperty(srcPropertyName)) {
                continue;
            }
            String destPropertyName = trimPrefix(srcPropertyName.replace(
                    mapDelimiter, beanDelimiter));
            if (!destBeanDesc.hasPropertyDesc(destPropertyName)) {
                continue;
            }
            PropertyDesc destPropertyDesc = destBeanDesc
                    .getPropertyDesc(destPropertyName);
            if (!destPropertyDesc.isWritable()) {
                continue;
            }
            Object value = src.get(srcPropertyName);
            if (value instanceof String && excludesWhitespace
                    && ((String) value).trim().length() == 0) {
                continue;
            }
            if (value == null && excludesNull) {
                continue;
            }
            value = convertValue(value, destPropertyName, destPropertyDesc
                    .getPropertyType());
            destPropertyDesc.setValue(dest, value);
        }
    }

    /**
     * MapからMapにコピーを行います。
     * 
     * @param src
     *            コピー元
     * @param dest
     *            コピー先
     */
    protected void copyMapToMap(Map<String, Object> src,
            Map<String, Object> dest) {
        for (Iterator<String> i = src.keySet().iterator(); i.hasNext();) {
            String srcPropertyName = i.next();
            if (!isTargetProperty(srcPropertyName)) {
                continue;
            }
            String destPropertyName = trimPrefix(srcPropertyName);
            Object value = src.get(srcPropertyName);
            if (value instanceof String && excludesWhitespace
                    && ((String) value).trim().length() == 0) {
                continue;
            }
            if (value == null && excludesNull) {
                continue;
            }
            value = convertValue(value, destPropertyName, null);
            dest.put(destPropertyName, value);
        }
    }

    /**
     * プレフィックスを削ります。
     * 
     * @param propertyName
     *            プロパティ名
     * @return 削った結果
     */
    protected String trimPrefix(String propertyName) {
        if (prefix == null) {
            return propertyName;
        }
        return propertyName.substring(prefix.length());
    }

    /**
     * 値を変換します。
     * 
     * @param value
     *            値
     * @param destPropertyName
     *            コピー先のプロパティ名
     * @param destPropertyClass
     *            コピー先のプロパティクラス
     * @return 変換後の値
     */
    protected Object convertValue(Object value, String destPropertyName,
            Class<?> destPropertyClass) {
        if (value == null || value.getClass() != String.class
                && destPropertyClass != null
                && destPropertyClass != String.class) {
            return value;
        }
        Converter converter = converterMap.get(destPropertyName);
        if (converter == null) {
            Class<?> targetClass = value.getClass() != String.class ? value
                    .getClass() : destPropertyClass;
            if (targetClass == null) {
                return value;
            }
            for (Class<?> clazz = targetClass; clazz != Object.class
                    && clazz != null; clazz = clazz.getSuperclass()) {
                converter = findConverter(clazz);
                if (converter != null) {
                    break;
                }
            }
            if (converter == null && destPropertyClass != null) {
                converter = findDefaultConverter(targetClass);
            }
            if (converter == null) {
                return value;
            }
        }
        try {
            if (value.getClass() == String.class) {
                return converter.getAsObject((String) value);
            }
            return converter.getAsString(value);
        } catch (Throwable cause) {
            throw new ConverterRuntimeException(destPropertyName, value, cause);
        }
    }

    /**
     * クラスに対応するコンバータを探します。
     * 
     * @param clazz
     *            クラス
     * @return コンバータ
     */
    protected Converter findConverter(Class<?> clazz) {
        for (Converter c : converters) {
            if (c.isTarget(clazz)) {
                return c;
            }
        }
        return null;
    }

    /**
     * クラスに対応するデフォルトのコンバータを探します。
     * 
     * @param clazz
     *            クラス
     * @return コンバータ
     */
    protected Converter findDefaultConverter(Class<?> clazz) {
        if (clazz == java.sql.Date.class) {
            return DEFAULT_DATE_CONVERTER;
        }
        if (clazz == Time.class) {
            return DEFAULT_TIME_CONVERTER;
        }
        if (java.util.Date.class.isAssignableFrom(clazz)) {
            return DEFAULT_TIMESTAMP_CONVERTER;
        }
        return null;
    }
}