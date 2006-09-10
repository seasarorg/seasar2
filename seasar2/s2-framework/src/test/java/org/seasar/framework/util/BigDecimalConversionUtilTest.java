/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
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

import junit.framework.TestCase;

/**
 * @author koichik
 * 
 */
public class BigDecimalConversionUtilTest extends TestCase {
    private static final BigDecimal ZERO = new BigDecimal("0");

    public void testToBigDecimal() throws Exception {
        assertNull(BigDecimalConversionUtil.toBigDecimal(null));
        assertSame(ZERO, BigDecimalConversionUtil.toBigDecimal(ZERO));
        assertEquals(new BigDecimal("1"), BigDecimalConversionUtil
                .toBigDecimal(new Byte((byte) 1)));
        assertEquals(new BigDecimal("10"), BigDecimalConversionUtil
                .toBigDecimal(new Short((short) 10)));
        assertEquals(new BigDecimal("100"), BigDecimalConversionUtil
                .toBigDecimal(new Integer(100)));
        assertEquals(new BigDecimal("1000"), BigDecimalConversionUtil
                .toBigDecimal(new Long(1000L)));
        assertEquals(new BigDecimal("0.5"), BigDecimalConversionUtil
                .toBigDecimal(new Float(0.5F)));
        assertEquals(new BigDecimal("0.25"), BigDecimalConversionUtil
                .toBigDecimal(new Double(0.25D)));
        assertEquals(new BigDecimal("100.00"), BigDecimalConversionUtil
                .toBigDecimal("100.00"));
    }
}
