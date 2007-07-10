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
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import org.seasar.extension.dxo.converter.ConversionContext;
import org.seasar.framework.util.StringUtil;

/**
 * 任意のオブジェクトから{@link Calendar}への変換を行うコンバータです。
 * <p>
 * 変換は次のように行われます。
 * </p>
 * <ul>
 * <li>変換元のオブジェクトが{@link Calendar}なら、変換元をそのまま変換先とします。</li>
 * <li>変換元のオブジェクトが{@link Date}なら、同じ時刻を持つ{@link Calendar}を変換先とします。</li>
 * <li>変換元のオブジェクトが{@link Number 数}なら、その<code>long</code>値を時刻とする{@link Calendar}を変換先とします。</li>
 * <li>それ以外の場合は、その文字列表現をフォーマットに従って{@link Calendar}に変換した結果を変換先とします。</li>
 * </ul>
 * 
 * @author Satoshi Kimura
 * @author koichik
 */
public class CalendarConverter extends AbstractConverter {

    public Class[] getSourceClasses() {
        return new Class[] { Object.class };
    }

    public Class getDestClass() {
        return Calendar.class;
    }

    public Object convert(final Object source, final Class destClass,
            final ConversionContext context) {
        if (source == null) {
            return null;
        }
        if (source instanceof Calendar) {
            return source;
        }
        if (source instanceof Date) {
            return toCalendar((Date) source);
        }
        if (source instanceof Number) {
            return toCalendar((Number) source);
        }
        if (source instanceof String) {
            final DateFormat dateFormat = context.getDateFormat();
            if (dateFormat != null) {
                return toCalendar((String) source, dateFormat);
            }
        }
        return null;
    }

    /**
     * {@link Date}を{@link Calendar}に変換して返します。
     * 
     * @param date
     *            変換元の{@link Date}
     * @return 変換した結果の{@link Calendar}
     */
    protected Calendar toCalendar(final Date date) {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    /**
     * {@link Number}を{@link Calendar}に変換して返します。
     * 
     * @param date
     *            変換元の{@link Number}
     * @return 変換した結果の{@link Calendar}
     */
    protected Calendar toCalendar(final Number date) {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(date.longValue()));
        return cal;
    }

    /**
     * 文字列を{@link Calendar}に変換して返します。
     * 
     * @param date
     *            変換元のオブジェクトの文字列表現
     * @param dateFormat
     *            フォーマット
     * @return 変換した結果の{@link Calendar}
     */
    protected Calendar toCalendar(final String date, final DateFormat dateFormat) {
        if (StringUtil.isEmpty(date)) {
            return null;
        }
        try {
            final Calendar cal = Calendar.getInstance();
            cal.setTime(dateFormat.parse(date));
            return cal;
        } catch (final ParseException e) {
            return null;
        }
    }

}
