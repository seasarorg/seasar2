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
package org.seasar.extension.dxo.converter.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import org.seasar.extension.dxo.converter.ConversionContext;
import org.seasar.framework.util.StringUtil;

/**
 * 任意のオブジェクトから{@link java.sql.Time}への変換を行うコンバータです。
 * <p>
 * 変換は次のように行われます。
 * </p>
 * <ul>
 * <li>変換元のオブジェクトが{@link java.sql.Time}なら、変換元をそのまま変換先とします。</li>
 * <li>変換元のオブジェクトが{@link Date}なら、同じ時刻を持つ{@link java.sql.Time}を変換先とします。</li>
 * <li>変換元のオブジェクトが{@link Calendar}なら、同じ時刻を持つ{@link java.sql.Time}を変換先とします。</li>
 * <li>変換元のオブジェクトが{@link Number 数}なら、その<code>long</code>値を時刻とする{@link java.sql.Time}を変換先とします。</li>
 * <li>それ以外の場合は、その文字列表現をフォーマットに従って{@link java.sql.Time}に変換した結果を変換先とします。</li>
 * </ul>
 * 
 * @author Satoshi Kimura
 * @author koichik
 */
public class SqlTimeConverter extends AbstractConverter {

    public Class[] getSourceClasses() {
        return new Class[] { Object.class };
    }

    public Class getDestClass() {
        return java.sql.Time.class;
    }

    public Object convert(final Object source, final Class destClass,
            final ConversionContext context) {
        if (source == null) {
            return null;
        }
        if (shallowCopy && source instanceof java.sql.Time) {
            return source;
        }
        if (source instanceof Date) {
            return toTime((Date) source);
        }
        if (source instanceof Calendar) {
            return toTime((Calendar) source);
        }
        if (source instanceof Number) {
            return toTime((Number) source);
        }
        if (source instanceof String) {
            final DateFormat dateFormat = context.getTimeFormat();
            if (dateFormat != null) {
                return toTime((String) source, dateFormat);
            }
        }
        return null;
    }

    /**
     * {@link Date}を{@link java.sql.Time}に変換して返します。
     * 
     * @param date
     *            変換元の{@link Date}
     * @return 変換した結果の{@link java.sql.Time}
     */
    protected java.sql.Time toTime(final Date date) {
        return new java.sql.Time(date.getTime());
    }

    /**
     * {@link Calendar}を{@link java.sql.Time}に変換して返します。
     * 
     * @param calendar
     *            変換元の{@link Calendar}
     * @return 変換した結果の{@link java.sql.Time}
     */
    protected java.sql.Time toTime(final Calendar calendar) {
        return toTime(calendar.getTime());
    }

    /**
     * {@link Number}を{@link java.sql.Time}に変換して返します。
     * 
     * @param date
     *            変換元の{@link Number}
     * @return 変換した結果の{@link java.sql.Time}
     */
    protected java.sql.Time toTime(final Number date) {
        return new java.sql.Time(date.longValue());
    }

    /**
     * 文字列を{@link java.sql.Time}に変換して返します。
     * 
     * @param date
     *            変換元のオブジェクトの文字列表現
     * @param dateFormat
     *            フォーマット
     * @return 変換した結果の{@link java.sql.Time}
     */
    protected java.sql.Time toTime(final String date,
            final DateFormat dateFormat) {
        if (StringUtil.isEmpty(date)) {
            return null;
        }
        try {
            return toTime(dateFormat.parse(date));
        } catch (final ParseException e) {
            return null;
        }
    }

}
