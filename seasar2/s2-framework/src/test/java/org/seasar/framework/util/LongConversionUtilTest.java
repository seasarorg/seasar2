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
public class LongConversionUtilTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testToLong() throws Exception {
        assertEquals(new Long("1000"), LongConversionUtil.toLong("1,000"));
    }

    /**
     * @throws Exception
     */
    public void testToPrimitiveLong() throws Exception {
        assertEquals(1000, LongConversionUtil.toPrimitiveLong("1,000"));
    }

    /**
     * @throws Exception
     */
    public void testToPrimitiveLongForEmptyString() throws Exception {
        assertEquals(0, LongConversionUtil.toPrimitiveLong(""));
    }

    /**
     * @throws Exception
     */
    public void testToLongForEmptyString() throws Exception {
        assertNull(LongConversionUtil.toLong(""));
    }
}