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
package org.seasar.framework.util;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

public final class BigDecimalConversionUtil {

    protected static final String TIGER_NORMALIZER_CLASS_NAME = "org.seasar.framework.util.TigerBigDecimalConversion";

    protected static BigDecimalNormalizer normalizer = new DefaultNormalizer();
    static {
        try {
            final Class clazz = Class.forName(TIGER_NORMALIZER_CLASS_NAME);
            normalizer = (BigDecimalNormalizer) clazz.newInstance();
        } catch (Exception ignore) {
        }
    }

    private BigDecimalConversionUtil() {
    }

    public static BigDecimal toBigDecimal(Object o) {
        return toBigDecimal(o, null);
    }

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
        } else {
            return normalizer.normalize(new BigDecimal(o.toString()));
        }
    }

    public interface BigDecimalNormalizer {
        BigDecimal normalize(BigDecimal dec);
    }

    public static class DefaultNormalizer implements BigDecimalNormalizer {
        public BigDecimal normalize(final BigDecimal dec) {
            return dec;
        }
    }
}
