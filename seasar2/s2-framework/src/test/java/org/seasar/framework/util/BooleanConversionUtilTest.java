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
public class BooleanConversionUtilTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testToBoolean() throws Exception {
        assertEquals("1", Boolean.TRUE, BooleanConversionUtil
                .toBoolean(new Integer(1)));
        assertEquals("2", Boolean.FALSE, BooleanConversionUtil
                .toBoolean(new Integer(0)));
        assertEquals("3", Boolean.FALSE, BooleanConversionUtil.toBoolean("0"));
        assertEquals("4", Boolean.TRUE, BooleanConversionUtil.toBoolean("1"));
        assertEquals("5", Boolean.TRUE, BooleanConversionUtil.toBoolean("2"));
        assertEquals("6", Boolean.TRUE, BooleanConversionUtil.toBoolean("true"));
        assertEquals("7", Boolean.FALSE, BooleanConversionUtil
                .toBoolean("false"));
        assertEquals("8", Boolean.TRUE, BooleanConversionUtil.toBoolean("fase")); // typo
    }
}