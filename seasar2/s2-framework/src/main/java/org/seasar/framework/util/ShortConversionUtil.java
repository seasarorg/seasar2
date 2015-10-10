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

import java.text.SimpleDateFormat;

/**
 * {@link Short}用の変換ユーティリティです。
 * 
 * @author higa
 * 
 */
public class ShortConversionUtil {

    /**
     * インスタンスを構築します。
     */
    protected ShortConversionUtil() {
    }

    /**
     * {@link Short}に変換します。
     * 
     * @param o
     *            変換するオブジェクト
     * @return 変換後のオブジェクト
     */
    public static Short toShort(Object o) {
        return toShort(o, null);
    }

    /**
     * {@link Short}に変換します。
     * 
     * @param o
     *            変換するオブジェクト
     * @param pattern
     *            パターン
     * @return 変換後のオブジェクト
     */
    public static Short toShort(Object o, String pattern) {
        if (o == null) {
            return null;
        } else if (o instanceof Short) {
            return (Short) o;
        } else if (o instanceof Number) {
            return new Short(((Number) o).shortValue());
        } else if (o instanceof String) {
            return toShort((String) o);
        } else if (o instanceof java.util.Date) {
            if (pattern != null) {
                return new Short(new SimpleDateFormat(pattern).format(o));
            }
            return new Short((short) ((java.util.Date) o).getTime());
        } else if (o instanceof Boolean) {
            return ((Boolean) o).booleanValue() ? new Short((short) 1)
                    : new Short((short) 0);
        } else {
            return toShort(o.toString());
        }
    }

    private static Short toShort(String s) {
        if (StringUtil.isEmpty(s)) {
            return null;
        }
        return new Short(DecimalFormatUtil.normalize(s));
    }

    /**
     * shortに変換します。
     * 
     * @param o
     *            変換するオブジェクト
     * @return 変換されたshort
     */
    public static short toPrimitiveShort(Object o) {
        return toPrimitiveShort(o, null);
    }

    /**
     * shortに変換します。
     * 
     * @param o
     *            変換するオブジェクト
     * @param pattern
     *            パターン
     * @return 変換されたshort
     */
    public static short toPrimitiveShort(Object o, String pattern) {
        if (o == null) {
            return 0;
        } else if (o instanceof Number) {
            return ((Number) o).shortValue();
        } else if (o instanceof String) {
            return toPrimitiveShort((String) o);
        } else if (o instanceof java.util.Date) {
            if (pattern != null) {
                return Short
                        .parseShort(new SimpleDateFormat(pattern).format(o));
            }
            return (short) ((java.util.Date) o).getTime();
        } else if (o instanceof Boolean) {
            return ((Boolean) o).booleanValue() ? (short) 1 : (short) 0;
        } else {
            return toPrimitiveShort(o.toString());
        }
    }

    private static short toPrimitiveShort(String s) {
        if (StringUtil.isEmpty(s)) {
            return 0;
        }
        return Short.parseShort(DecimalFormatUtil.normalize(s));
    }
}