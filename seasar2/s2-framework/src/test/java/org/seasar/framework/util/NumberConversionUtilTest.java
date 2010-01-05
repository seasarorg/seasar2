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

import java.text.DecimalFormatSymbols;
import java.util.Locale;

import junit.framework.TestCase;

/**
 * @author shot
 * 
 */
public class NumberConversionUtilTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testConvertNumber_byte() throws Exception {
        assertEquals(new Byte("1"), NumberConversionUtil.convertNumber(
                Byte.class, "1"));
    }

    /**
     * @throws Exception
     */
    public void testConvertNumber_primitiveWrapper() throws Exception {
        assertEquals(new Byte("1"), NumberConversionUtil
                .convertPrimitiveWrapper(byte.class, "1"));
        assertEquals(new Byte("0"), NumberConversionUtil
                .convertPrimitiveWrapper(byte.class, null));
    }

    /**
     * @throws Exception
     */
    public void testFindFractionDelimeter() throws Exception {
        String delim = NumberConversionUtil.findDecimalSeparator(Locale.JAPAN);
        assertEquals(".", delim);
    }

    /**
     * @throws Exception
     */
    public void testFindFractionDelimeter2() throws Exception {
        String delim = NumberConversionUtil.findDecimalSeparator(Locale.FRANCE);
        assertEquals(",", delim);
    }

    /**
     * @throws Exception
     */
    public void testFindFractionDelimeter3() throws Exception {
        String delim = NumberConversionUtil.findDecimalSeparator(null);
        char c = new DecimalFormatSymbols(Locale.getDefault())
                .getDecimalSeparator();
        assertEquals(Character.toString(c), delim);
    }

    /**
     * @throws Exception
     */
    public void testFindIntegerDelimeter() throws Exception {
        String delim = NumberConversionUtil.findGroupingSeparator(Locale.JAPAN);
        assertEquals(",", delim);
    }

    /**
     * @throws Exception
     */
    public void testFindIntegerDelimeter2() throws Exception {
        String delim = NumberConversionUtil
                .findGroupingSeparator(Locale.GERMANY);
        assertEquals(".", delim);
    }

    /**
     * @throws Exception
     */
    public void testFindIntegerDelimeter3() throws Exception {
        String delim = NumberConversionUtil.findGroupingSeparator(null);
        char c = new DecimalFormatSymbols(Locale.getDefault())
                .getGroupingSeparator();
        assertEquals(Character.toString(c), delim);
    }

    /**
     * @throws Exception
     */
    public void testRemoveDelimeter() throws Exception {
        assertEquals("1000000.234", NumberConversionUtil.removeDelimeter(
                "1,000,000.234", Locale.JAPAN));
    }
}
