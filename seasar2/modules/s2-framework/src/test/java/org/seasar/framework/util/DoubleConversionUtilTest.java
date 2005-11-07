package org.seasar.framework.util;

import junit.framework.TestCase;

public class DoubleConversionUtilTest extends TestCase {

    public void testToDouble() throws Exception {
        assertEquals("1", new Double("1000.5"), DoubleConversionUtil
                .toDouble("1,000.5"));
    }

    public void testToPrimitiveDouble() throws Exception {
        assertEquals("1", 1000.5, DoubleConversionUtil
                .toPrimitiveDouble("1,000.5"), 0);
    }
}