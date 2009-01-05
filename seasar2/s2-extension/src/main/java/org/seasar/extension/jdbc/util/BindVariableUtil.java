/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
package org.seasar.extension.jdbc.util;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.seasar.extension.jdbc.IllegalBindArgSizeRuntimeException;
import org.seasar.extension.jdbc.ValueType;
import org.seasar.framework.util.SStringBuilder;

/**
 * バインド変数用のユーティリティです。
 * 
 * @author higa
 * 
 */
public class BindVariableUtil {

    private static final String NULL = "null";

    /**
     * インスタンスを構築します。
     */
    protected BindVariableUtil() {
    }

    /**
     * バインド変数をSQLの中にリテラルで埋め込んだ完全なSQLを返します。
     * 
     * @param sql
     *            SQL
     * @param args
     *            引数
     * @return バインド変数をSQLの中にリテラルで埋め込んだ完全なSQL
     */
    public static String getCompleteSql(String sql, Object[] args) {
        if (args == null || args.length == 0) {
            return sql;
        }
        return getCompleteSql(sql, args, new ValueType[args.length]);
    }

    /**
     * バインド変数をSQLの中にリテラルで埋め込んだ完全なSQLを返します。
     * 
     * @param sql
     *            SQL
     * @param args
     *            引数
     * @param valueTypes
     *            値タイプの配列
     * @return バインド変数をSQLの中にリテラルで埋め込んだ完全なSQL
     */
    public static String getCompleteSql(String sql, Object[] args,
            ValueType[] valueTypes) {
        if (args == null || args.length == 0) {
            return sql;
        }
        StringBuffer buf = new StringBuffer(sql.length() + args.length * 15);
        int pos = 0;
        int pos2 = 0;
        int pos3 = 0;
        int pos4 = 0;
        int pos5 = 0;
        int pos6 = 0;
        int index = 0;
        while (true) {
            pos = sql.indexOf('?', pos2);
            pos3 = sql.indexOf('\'', pos2);
            pos4 = sql.indexOf('\'', pos3 + 1);
            pos5 = sql.indexOf("/*", pos2);
            pos6 = sql.indexOf("*/", pos5 + 1);
            if (pos > 0) {
                if (pos3 >= 0 && pos3 < pos && pos < pos4) {
                    buf.append(sql.substring(pos2, pos4 + 1));
                    pos2 = pos4 + 1;
                } else if (pos5 >= 0 && pos5 < pos && pos < pos6) {
                    buf.append(sql.substring(pos2, pos6 + 1));
                    pos2 = pos6 + 1;
                } else {
                    if (args.length <= index) {
                        throw new IllegalBindArgSizeRuntimeException(sql,
                                args.length);
                    }
                    buf.append(sql.substring(pos2, pos));
                    buf.append(getBindVariableText(args[index],
                            valueTypes[index]));
                    pos2 = pos + 1;
                    index++;
                }
            } else {
                buf.append(sql.substring(pos2));
                break;
            }
        }
        return buf.toString();
    }

    /**
     * バインド変数を文字列として返します。
     * 
     * @param bindVariable
     *            バインド変数
     * @return バインド変数の文字列表現
     */
    public static String getBindVariableText(Object bindVariable) {
        if (bindVariable instanceof String) {
            return quote(bindVariable.toString());
        } else if (bindVariable instanceof Number) {
            return bindVariable.toString();
        } else if (bindVariable instanceof Timestamp) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss");
            return quote(sdf.format((java.util.Date) bindVariable));
        } else if (bindVariable instanceof java.util.Date) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return quote(sdf.format((java.util.Date) bindVariable));
        } else if (bindVariable instanceof Boolean) {
            return bindVariable.toString();
        } else if (bindVariable == null) {
            return NULL;
        } else {
            return quote(bindVariable.toString());
        }
    }

    /**
     * バインド変数を文字列として返します。
     * 
     * @param bindVariable
     *            バインド変数
     * @param valueType
     *            値タイプ
     * @return バインド変数の文字列表現
     */
    public static String getBindVariableText(Object bindVariable,
            ValueType valueType) {
        if (valueType != null) {
            return valueType.toText(bindVariable);
        }
        return getBindVariableText(bindVariable);
    }

    /**
     * <code>null</code>の文字列表現を返します。
     * 
     * @return
     */
    public static String nullText() {
        return NULL;
    }

    /**
     * {@link Number}の文字列表現を返します。
     * 
     * @param value
     *            値
     * @return 文字列表現
     */
    public static String toText(Number value) {
        if (value == null) {
            return NULL;
        }
        return value.toString();
    }

    /**
     * {@link Boolean}の文字列表現を返します。
     * 
     * @param value
     *            値
     * @return 文字列表現
     */
    public static String toText(Boolean value) {
        if (value == null) {
            return NULL;
        }
        return quote(value.toString());
    }

    /**
     * {@link String}の文字列表現を返します。
     * 
     * @param value
     *            値
     * @return 文字列表現
     */
    public static String toText(String value) {
        if (value == null) {
            return NULL;
        }
        return quote(value);
    }

    /**
     * {@link Date}の文字列表現を返します。
     * 
     * @param value
     *            値
     * @return 文字列表現
     */
    public static String toText(Date value) {
        if (value == null) {
            return NULL;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(value);
        SStringBuilder buf = new SStringBuilder();
        addDate(buf, calendar);
        return quote(buf.toString());
    }

    /**
     * {@link Time}の文字列表現を返します。
     * 
     * @param value
     *            値
     * @return 文字列表現
     */
    public static String toText(Time value) {
        if (value == null) {
            return NULL;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(value);
        SStringBuilder buf = new SStringBuilder();
        addTime(buf, calendar);
        addTimeDecimalPart(buf, calendar.get(Calendar.MILLISECOND));
        return quote(buf.toString());
    }

    /**
     * {@link Timestamp}の文字列表現を返します。
     * 
     * @param value
     *            値
     * @return 文字列表現
     */
    public static String toText(Timestamp value) {
        if (value == null) {
            return NULL;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(value);
        SStringBuilder buf = new SStringBuilder(30);
        addDate(buf, calendar);
        addTime(buf, calendar);
        addTimeDecimalPart(buf, value.getNanos());
        return quote(buf.toString());
    }

    /**
     * <code>byte[]</code>の文字列表現を返します。
     * 
     * @param value
     *            値
     * @return 文字列表現
     */
    public static String toText(byte[] value) {
        if (value == null) {
            return NULL;
        }
        return quote(value.toString() + "(byteLength="
                + Integer.toString(value.length) + ")");
    }

    /**
     * {@link Object}の文字列表現を返します。
     * 
     * @param value
     *            値
     * @return 文字列表現
     */
    public static String toText(Object value) {
        if (value == null) {
            return NULL;
        }
        return quote(value.toString());
    }

    /**
     * 文字列バッファに<code>yyyy-mm-dd</code>のフォーマットで日付を設定します。
     * 
     * @param buf
     *            文字列バッファ
     * @param calendar
     *            カレンダ
     */
    protected static void addDate(SStringBuilder buf, Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        buf.append(year);
        buf.append('-');
        int month = calendar.get(Calendar.MONTH) + 1;
        if (month < 10) {
            buf.append('0');
        }
        buf.append(month);
        buf.append('-');
        int date = calendar.get(Calendar.DATE);
        if (date < 10) {
            buf.append('0');
        }
        buf.append(date);
    }

    /**
     * 文字列バッファに<code>hh:mm:ss</code>のフォーマットで値を設定します。
     * 
     * @param buf
     *            文字列バッファ
     * @param calendar
     *            カレンダ
     */
    protected static void addTime(SStringBuilder buf, Calendar calendar) {
        if (buf.length() > 0) {
            buf.append(' ');
        }
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour < 10) {
            buf.append('0');
        }
        buf.append(hour);
        buf.append(':');
        int minute = calendar.get(Calendar.MINUTE);
        if (minute < 10) {
            buf.append('0');
        }
        buf.append(minute);
        buf.append(':');
        int second = calendar.get(Calendar.SECOND);
        if (second < 10) {
            buf.append('0');
        }
        buf.append(second);
    }

    /**
     * 文字列バッファに時間の小数点以下の値を設定します。
     * 
     * @param buf
     *            文字列バッファ
     * @param decimalPart
     *            小数点以下の値
     */
    protected static void addTimeDecimalPart(SStringBuilder buf, int decimalPart) {
        if (decimalPart == 0) {
            return;
        }
        if (buf.length() > 0) {
            buf.append('.');
        }
        buf.append(decimalPart);
    }

    /**
     * 文字列をシングルクォートで囲みます。
     * 
     * @param text
     *            文字列
     * @return
     */
    protected static String quote(String text) {
        return "'" + text + "'";
    }
}