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
public class IntegerConversionUtilTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testToInteger() throws Exception {
        assertEquals(new Integer("1000"), IntegerConversionUtil
                .toInteger("1,000"));
    }

    /**
     * @throws Exception
     */
    public void testToPrimitiveInt() throws Exception {
        assertEquals(1000, IntegerConversionUtil.toPrimitiveInt("1,000"));
    }

    /**
     * @throws Exception
     */
    public void testToPrimitiveIntForEmptyString() throws Exception {
        assertEquals(0, IntegerConversionUtil.toPrimitiveInt(""));
    }

    /**
     * @throws Exception
     */
    public void testToIntegerForEmptyString() throws Exception {
        assertNull(IntegerConversionUtil.toInteger(""));
    }
}