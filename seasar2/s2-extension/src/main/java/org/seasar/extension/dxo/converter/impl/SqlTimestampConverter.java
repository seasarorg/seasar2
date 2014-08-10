/*
 * Copyright 2004-2014 the Seasar Foundation and the Others.
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
import java.util.Calendar;
import java.util.Date;

import org.seasar.extension.dxo.converter.ConversionContext;
import org.seasar.framework.util.StringUtil;

/**
 * 任意のオブジェクトから{@link java.sql.Timestamp}への変換を行うコンバータです。
 * <p>
 * 変換は次のように行われます。
 * </p>
 * <ul>
 * <li>変換元のオブジェクトが{@link java.sql.Timestamp}なら、変換元をそのまま変換先とします。</li>
 * <li>変換元のオブジェクトが{@link Date}なら、同じ時刻を持つ{@link java.sql.Timestamp}を変換先とします。</li>
 * <li>変換元のオブジェクトが{@link Calendar}なら、同じ時刻を持つ{@link java.sql.Timestamp}を変換先とします。</li>
 * <li>変換元のオブジェクトが{@link Number 数}なら、その<code>long</code>値を時刻とする
 * {@link java.sql.Timestamp}を変換先とします。</li>
 * <li>それ以外の場合は、その文字列表現をフォーマットに従って{@link java.sql.Timestamp}に変換した結果を変換先とします。</li>
 * </ul>
 * 
 * @author Satoshi Kimura
 * @author koichik
 */
public class SqlTimestampConverter extends AbstractConverter {

    public Class[] getSourceClasses() {
        return new Class[] { Object.class };
    }

    public Class getDestClass() {
        return java.sql.Timestamp.class;
    }

    public Object convert(final Object source, final Class destClass,
            final ConversionContext context) {
        if (source == null) {
            return null;
        }
        if (shallowCopy && source instanceof java.sql.Timestamp) {
            return source;
        }
        if (source instanceof Date) {
            return toTimestamp((Date) source);
        }
        if (source instanceof Calendar) {
            return toTimestamp((Calendar) source);
        }
        if (source instanceof Number) {
            return toTimestamp((Number) source);
        }
        if (source instanceof String) {
            final DateFormat dateFormat = context.getTimestampFormat();
            if (dateFormat != null) {
                return toTimestamp((String) source, dateFormat);
            }
        }
        return null;
    }

    /**
     * {@link Date}を{@link java.sql.Timestamp}に変換して返します。
     * 
     * @param date
     *            変換元の{@link Date}
     * @return 変換した結果の{@link java.sql.Timestamp}
     */
    protected java.sql.Timestamp toTimestamp(final Date date) {
        java.sql.Timestamp result = new java.sql.Timestamp(date.getTime());
        if (date instanceof java.sql.Timestamp) {
            result.setNanos(((java.sql.Timestamp) date).getNanos());
        }
        return result;
    }

    /**
     * {@link Calendar}を{@link java.sql.Timestamp}に変換して返します。
     * 
     * @param calendar
     *            変換元の{@link Calendar}
     * @return 変換した結果の{@link java.sql.Timestamp}
     */
    protected java.sql.Timestamp toTimestamp(final Calendar calendar) {
        return toTimestamp(calendar.getTime());
    }

    /**
     * {@link Number}を{@link java.sql.Timestamp}に変換して返します。
     * 
     * @param date
     *            変換元の{@link Number}
     * @return 変換した結果の{@link java.sql.Timestamp}
     */
    protected java.sql.Timestamp toTimestamp(final Number date) {
        return new java.sql.Timestamp(date.longValue());
    }

    /**
     * 文字列を{@link java.sql.Timestamp}に変換して返します。
     * 
     * @param date
     *            変換元のオブジェクトの文字列表現
     * @param dateFormat
     *            フォーマット
     * @return 変換した結果の{@link java.sql.Timestamp}
     */
    protected java.sql.Timestamp toTimestamp(final String date,
            final DateFormat dateFormat) {
        if (StringUtil.isEmpty(date)) {
            return null;
        }
        try {
            return toTimestamp(dateFormat.parse(date));
        } catch (final ParseException e) {
            return null;
        }
    }

}
