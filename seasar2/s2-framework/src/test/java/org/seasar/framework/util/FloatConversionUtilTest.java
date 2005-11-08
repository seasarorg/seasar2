package org.seasar.framework.util;

import junit.framework.TestCase;

public class FloatConversionUtilTest extends TestCase {

    public void testToFloat() throws Exception {
        assertEquals("1", new Float("1000.5"), FloatConversionUtil
                .toFloat("1,000.5"));
    }

    public void testToPrimitiveFloat() throws Exception {
        assertEquals("1", 1000.5, FloatConversionUtil
                .toPrimitiveFloat("1,000.5"), 0);
    }
}