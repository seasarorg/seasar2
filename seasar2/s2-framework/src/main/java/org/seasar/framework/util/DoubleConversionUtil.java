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
 * {@link Double}用の変換ユーティリティです。
 * 
 * @author higa
 * 
 */
public class DoubleConversionUtil {

    /**
     * インスタンスを構築します。
     */
    protected DoubleConversionUtil() {
    }

    /**
     * {@link Double}に変換します。
     * 
     * @param o
     * @return {@link Double}
     */
    public static Double toDouble(Object o) {
        return toDouble(o, null);
    }

    /**
     * {@link Double}に変換します。
     * 
     * @param o
     * @param pattern
     * @return {@link Double}
     */
    public static Double toDouble(Object o, String pattern) {
        if (o == null) {
            return null;
        } else if (o instanceof Double) {
            return (Double) o;
        } else if (o instanceof Number) {
            return new Double(((Number) o).doubleValue());
        } else if (o instanceof String) {
            return toDouble((String) o);
        } else if (o instanceof java.util.Date) {
            if (pattern != null) {
                return new Double(new SimpleDateFormat(pattern).format(o));
            }
            return new Double(((java.util.Date) o).getTime());
        } else {
            return toDouble(o.toString());
        }
    }

    private static Double toDouble(String s) {
        if (StringUtil.isEmpty(s)) {
            return null;
        }
        return new Double(DecimalFormatUtil.normalize(s));
    }

    /**
     * doubleに変換します。
     * 
     * @param o
     * @return double
     */
    public static double toPrimitiveDouble(Object o) {
        return toPrimitiveDouble(o, null);
    }

    /**
     * doubleに変換します。
     * 
     * @param o
     * @param pattern
     * @return double
     */
    public static double toPrimitiveDouble(Object o, String pattern) {
        if (o == null) {
            return 0;
        } else if (o instanceof Number) {
            return ((Number) o).doubleValue();
        } else if (o instanceof String) {
            return toPrimitiveDouble((String) o);
        } else if (o instanceof java.util.Date) {
            if (pattern != null) {
                return Double.parseDouble(new SimpleDateFormat(pattern)
                        .format(o));
            }
            return ((java.util.Date) o).getTime();
        } else {
            return toPrimitiveDouble(o.toString());
        }
    }

    private static double toPrimitiveDouble(String s) {
        if (StringUtil.isEmpty(s)) {
            return 0;
        }
        return Double.parseDouble(DecimalFormatUtil.normalize(s));
    }
}
