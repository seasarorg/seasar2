package org.seasar.framework.util;

import junit.framework.TestCase;

public class BooleanConversionUtilTest extends TestCase {

    public void testToBoolean() throws Exception {
        assertEquals("1", Boolean.TRUE, BooleanConversionUtil
                .toBoolean(new Integer(1)));
        assertEquals("2", Boolean.FALSE, BooleanConversionUtil
                .toBoolean(new Integer(0)));
    }
}