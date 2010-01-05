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

import junit.framework.TestCase;

/**
 * @author higa
 * 
 */
public class DoubleConversionUtilTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testToDouble() throws Exception {
        assertEquals(new Double("1000.5"), DoubleConversionUtil
                .toDouble("1,000.5"));
    }

    /**
     * @throws Exception
     */
    public void testToPrimitiveDouble() throws Exception {
        assertEquals(1000.5, DoubleConversionUtil.toPrimitiveDouble("1,000.5"),
                0);
    }

    /**
     * @throws Exception
     */
    public void testToPrimitiveDoubleForEmptyString() throws Exception {
        assertEquals(0, DoubleConversionUtil.toPrimitiveDouble(""), 0);
    }

    /**
     * @throws Exception
     */
    public void testToDoubleForEmptyString() throws Exception {
        assertNull(DoubleConversionUtil.toDouble(""));
    }
}