/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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

    /**
     * @throws Exception
     */
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

        assertEquals(0, new BigDecimal("0.1")
                .compareTo(BigDecimalConversionUtil
                        .toBigDecimal(new Float(0.1F))));

        assertEquals(0, new BigDecimal("0.1")
                .compareTo(BigDecimalConversionUtil.toBigDecimal(new Double(
                        0.1D))));
        assertEquals(0, new BigDecimal("0.01")
                .compareTo(BigDecimalConversionUtil.toBigDecimal(new Double(
                        0.01D))));
        assertEquals(0, new BigDecimal("0.001")
                .compareTo(BigDecimalConversionUtil.toBigDecimal(new Double(
                        0.001D))));
        assertEquals(0, new BigDecimal("0.0001")
                .compareTo(BigDecimalConversionUtil.toBigDecimal(new Double(
                        0.0001D))));
        assertEquals(0, new BigDecimal("0.00001")
                .compareTo(BigDecimalConversionUtil.toBigDecimal(new Double(
                        0.00001D))));
        assertEquals(0, new BigDecimal("0.0000001")
                .compareTo(BigDecimalConversionUtil.toBigDecimal(new Double(
                        0.0000001D))));

        assertEquals(0, new BigDecimal("0.123")
                .compareTo(BigDecimalConversionUtil.toBigDecimal(new Double(
                        0.123D))));
        assertEquals(0, new BigDecimal("0.0123")
                .compareTo(BigDecimalConversionUtil.toBigDecimal(new Double(
                        0.0123D))));
        assertEquals(0, new BigDecimal("0.00123")
                .compareTo(BigDecimalConversionUtil.toBigDecimal(new Double(
                        0.00123D))));
        assertEquals(0, new BigDecimal("0.000123")
                .compareTo(BigDecimalConversionUtil.toBigDecimal(new Double(
                        0.000123D))));
        assertEquals(0, new BigDecimal("0.0000123")
                .compareTo(BigDecimalConversionUtil.toBigDecimal(new Double(
                        0.0000123D))));
        assertEquals(0, new BigDecimal("0.00000123")
                .compareTo(BigDecimalConversionUtil.toBigDecimal(new Double(
                        0.00000123D))));
        assertEquals(0, new BigDecimal("0.000000123")
                .compareTo(BigDecimalConversionUtil.toBigDecimal(new Double(
                        0.000000123D))));
        assertEquals(0, new BigDecimal("0.0000000123")
                .compareTo(BigDecimalConversionUtil.toBigDecimal(new Double(
                        0.0000000123D))));
        assertEquals(0, new BigDecimal("0.00000000123")
                .compareTo(BigDecimalConversionUtil.toBigDecimal(new Double(
                        0.00000000123D))));

        assertEquals(0, new BigDecimal("0.000000123")
                .compareTo(BigDecimalConversionUtil.toBigDecimal(new Double(
                        1.23E-7))));
        assertEquals(0, new BigDecimal("1.23E-7")
                .compareTo(BigDecimalConversionUtil.toBigDecimal(new Double(
                        0.000000123))));
        assertEquals(0, new BigDecimal("1.23E-10")
                .compareTo(BigDecimalConversionUtil.toBigDecimal(new Double(
                        0.000000000123))));

        assertEquals(0, new BigDecimal("100.00")
                .compareTo(BigDecimalConversionUtil.toBigDecimal("100.00")));
        assertEquals(0, new BigDecimal("0.000000123")
                .compareTo(BigDecimalConversionUtil.toBigDecimal("1.23E-7")));
        assertEquals(0,
                new BigDecimal("1.23E-7").compareTo(BigDecimalConversionUtil
                        .toBigDecimal("0.000000123")));
    }

    /**
     * @throws Exception
     */
    public void testToBigDecimalForEmptyString() throws Exception {
        assertNull(BigDecimalConversionUtil.toBigDecimal(""));
    }
}
