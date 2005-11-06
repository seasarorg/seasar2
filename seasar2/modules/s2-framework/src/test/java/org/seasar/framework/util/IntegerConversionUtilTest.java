package org.seasar.framework.util;

import junit.framework.TestCase;

public class IntegerConversionUtilTest extends TestCase {

    public void testToInteger() throws Exception {
        assertEquals("1", new Integer("1000"), IntegerConversionUtil
                .toInteger("1,000"));
    }

    public void testToPrimitiveInt() throws Exception {
        assertEquals("1", 1000, IntegerConversionUtil.toPrimitiveInt("1,000"));
    }
}