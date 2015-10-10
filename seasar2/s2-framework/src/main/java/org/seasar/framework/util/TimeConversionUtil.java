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

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import org.seasar.framework.exception.ParseRuntimeException;

/**
 * タイム用の変換ユーティリティです。
 * 
 * @author higa
 * 
 */
public class TimeConversionUtil {

    /**
     * インスタンスを構築します。
     */
    protected TimeConversionUtil() {
    }

    /**
     * タイムに変換します。
     * 
     * @param o
     *            変換したいオブジェクト
     * @return タイム
     */
    public static Time toTime(Object o) {
        return toTime(o, null);
    }

    /**
     * タイムに変換します。
     * 
     * @param o
     *            変換したいオブジェクト
     * @param pattern
     *            パターン
     * @return タイム
     */
    public static Time toTime(Object o, String pattern) {
        if (o == null) {
            return null;
        } else if (o instanceof String) {
            return toTime((String) o, pattern);
        } else if (o instanceof Time) {
            return (Time) o;
        } else if (o instanceof Calendar) {
            return new Time(((Calendar) o).getTime().getTime());
        } else {
            return toTime(o.toString(), pattern);
        }
    }

    /**
     * タイムに変換します。
     * 
     * @param s
     *            文字列で表現した値
     * @param pattern
     *            パターン
     * @return 変換した値
     */
    public static Time toTime(String s, String pattern) {
        return toTime(s, pattern, Locale.getDefault());
    }

    /**
     * タイムに変換します。
     * 
     * @param s
     *            文字列で表現した値
     * @param pattern
     *            パターン
     * @param locale
     *            ロケール
     * @return 変換した値
     */
    public static Time toTime(String s, String pattern, Locale locale) {
        if (StringUtil.isEmpty(s)) {
            return null;
        }
        SimpleDateFormat sdf = getDateFormat(s, pattern, locale);
        try {
            return new Time(sdf.parse(s).getTime());
        } catch (ParseException ex) {
            throw new ParseRuntimeException(ex);
        }
    }

    /**
     * 日付フォーマットを返します。
     * 
     * @param s
     *            文字列で表現した値
     * @param pattern
     *            パターン
     * @param locale
     *            ロケール
     * @return 日付フォーマット
     */
    public static SimpleDateFormat getDateFormat(String s, String pattern,
            Locale locale) {
        if (pattern != null) {
            return new SimpleDateFormat(pattern);
        }
        return getDateFormat(s, locale);
    }

    /**
     * 日付フォーマットを返します。
     * 
     * @param s
     *            文字列で表現した値
     * @param locale
     *            ロケール
     * @return 日付フォーマット
     */
    public static SimpleDateFormat getDateFormat(String s, Locale locale) {
        String pattern = getPattern(locale);
        if (s.length() == pattern.length()) {
            return new SimpleDateFormat(pattern);
        }
        String shortPattern = convertShortPattern(pattern);
        if (s.length() == shortPattern.length()) {
            return new SimpleDateFormat(shortPattern);
        }
        return new SimpleDateFormat(pattern);
    }

    /**
     * 日付パターンを返します。
     * 
     * @param locale
     * @return 日付パターン
     */
    public static String getPattern(Locale locale) {
        return "HH:mm:ss";
    }

    /**
     * 短いパターンに変換します。
     * 
     * @param pattern
     *            パターン
     * @return 短いパターン
     */
    public static String convertShortPattern(String pattern) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < pattern.length(); ++i) {
            char c = pattern.charAt(i);
            if (c == 'h' || c == 'H' || c == 'm' || c == 's') {
                buf.append(c);
            }
        }
        return buf.toString();
    }
}
