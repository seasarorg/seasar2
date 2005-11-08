package org.seasar.framework.util;

import junit.framework.TestCase;

public class ByteConversionUtilTest extends TestCase {

    public void testToByteConversionUtilTest() throws Exception {
        assertEquals("1", new Byte("100"), ByteConversionUtil.toByte("100"));
    }

    public void testToPrimitiveByteConversionUtilTest() throws Exception {
        assertEquals("1", 100, ByteConversionUtil.toPrimitiveByte("100"));
    }
}