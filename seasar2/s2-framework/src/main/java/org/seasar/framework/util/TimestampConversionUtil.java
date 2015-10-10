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

import java.sql.Timestamp;
import java.util.Date;
import java.util.Locale;

/**
 * {@link Timestamp}用の変換ユーティリティです。
 * 
 * @author higa
 * 
 */
public class TimestampConversionUtil {

    /**
     * インスタンスを構築します。
     */
    protected TimestampConversionUtil() {
    }

    /**
     * {@link Timestamp}に変換します。
     * 
     * @param o
     * @return {@link Timestamp}
     */
    public static Timestamp toTimestamp(Object o) {
        return toTimestamp(o, null);
    }

    /**
     * {@link Timestamp}に変換します。
     * 
     * @param o
     * @param pattern
     * @return {@link Timestamp}
     */
    public static Timestamp toTimestamp(Object o, String pattern) {
        if (o instanceof Timestamp) {
            return (Timestamp) o;
        }
        Date date = DateConversionUtil.toDate(o, pattern);
        if (date != null) {
            return new Timestamp(date.getTime());
        }
        return null;
    }

    /**
     * 日付パターンを返します。
     * 
     * @param locale
     * @return 日付パターン
     */
    public static String getPattern(Locale locale) {
        return DateConversionUtil.getY4Pattern(locale) + " "
                + TimeConversionUtil.getPattern(locale);
    }
}
