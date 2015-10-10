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
package org.seasar.framework.util;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * {@link Calendar}用の変換ユーティリティです。
 * 
 * @author higa
 * 
 */
public class CalendarConversionUtil {

    /**
     * インスタンスを構築します。
     */
    protected CalendarConversionUtil() {
    }

    /**
     * {@link Calendar}に変換します。
     * 
     * @param o
     * @return {@link Calendar}
     */
    public static Calendar toCalendar(Object o) {
        return toCalendar(o, null);
    }

    /**
     * {@link Calendar}に変換します。
     * 
     * @param o
     * @param pattern
     * @return {@link Calendar}
     */
    public static Calendar toCalendar(Object o, String pattern) {
        if (o instanceof Calendar) {
            return (Calendar) o;
        }
        java.util.Date date = DateConversionUtil.toDate(o, pattern);
        if (date != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return cal;
        }
        return null;
    }

    /**
     * ローカルの{@link TimeZone}と{@link Locale}をもつ{@link Calendar}に変換します。
     * 
     * @param calendar
     *            {@link Calendar}
     * @return
     */
    public static Calendar localize(Calendar calendar) {
        if (calendar == null) {
            throw new NullPointerException("calendar");
        }
        Calendar localCalendar = Calendar.getInstance();
        localCalendar.setTimeInMillis(calendar.getTimeInMillis());
        return localCalendar;
    }
}