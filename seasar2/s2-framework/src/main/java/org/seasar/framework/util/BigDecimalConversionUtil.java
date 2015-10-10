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

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

/**
 * {@link BigDecimal}用の変換ユーティリティです。
 * 
 * @author higa
 * 
 */
public class BigDecimalConversionUtil {

    /**
     * Tiger用のNormalizerのクラス名です。
     */
    protected static final String TIGER_NORMALIZER_CLASS_NAME = "org.seasar.framework.util.TigerBigDecimalConversion";

    /**
     * デフォルトのNormalizerです。
     */
    protected static BigDecimalNormalizer normalizer = new DefaultNormalizer();
    static {
        try {
            final Class clazz = Class.forName(TIGER_NORMALIZER_CLASS_NAME);
            normalizer = (BigDecimalNormalizer) clazz.newInstance();
        } catch (Exception ignore) {
        }
    }

    /**
     * インスタンスを構築します。
     */
    protected BigDecimalConversionUtil() {
    }

    /**
     * {@link BigDecimal}に変換します。
     * 
     * @param o
     * @return {@link BigDecimal}に変換されたデータ
     */
    public static BigDecimal toBigDecimal(Object o) {
        return toBigDecimal(o, null);
    }

    /**
     * {@link BigDecimal}に変換します。
     * 
     * @param o
     * @param pattern
     * @return {@link BigDecimal}に変換されたデータ
     */
    public static BigDecimal toBigDecimal(Object o, String pattern) {
        if (o == null) {
            return null;
        } else if (o instanceof BigDecimal) {
            return (BigDecimal) o;
        } else if (o instanceof java.util.Date) {
            if (pattern != null) {
                return new BigDecimal(new SimpleDateFormat(pattern).format(o));
            }
            return new BigDecimal(Long.toString(((java.util.Date) o).getTime()));
        } else if (o instanceof String) {
            String s = (String) o;
            if (StringUtil.isEmpty(s)) {
                return null;
            }
            return normalizer.normalize(new BigDecimal(s));
        } else {
            return normalizer.normalize(new BigDecimal(o.toString()));
        }
    }

    /**
     * {@link BigDecimal}を文字列に変換します。
     * 
     * @param dec
     * @return 文字列に変換されたデータ
     */
    public static String toString(BigDecimal dec) {
        return normalizer.toString(dec);
    }

    /**
     * {@link BigDecimal}を正規化するためのインターフェースです。
     * これは、Java5からtoString()のロジックが変わったことに対応するためです。
     * 
     */
    public interface BigDecimalNormalizer {

        /**
         * 正規化します。
         * 
         * @param dec
         * @return 正規化された{@link BigDecimal}
         */
        BigDecimal normalize(BigDecimal dec);

        /**
         * {@link BigDecimal}を文字列に変換します。
         * 
         * @param dec
         * @return
         */
        String toString(BigDecimal dec);
    }

    /**
     * デフォルトの{@link BigDecimalConversionUtil.BigDecimalNormalizer}の実装クラスです。
     * 
     */
    public static class DefaultNormalizer implements BigDecimalNormalizer {
        public BigDecimal normalize(final BigDecimal dec) {
            return dec;
        }

        public String toString(final BigDecimal dec) {
            return dec.toString();
        }
    }
}
