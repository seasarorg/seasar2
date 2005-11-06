package org.seasar.framework.util;

import junit.framework.TestCase;

public class ShortConversionUtilTest extends TestCase {

    public void testToShort() throws Exception {
        assertEquals("1", new Short("1000"), ShortConversionUtil
                .toShort("1,000"));
    }

    public void testToPrimitiveShort() throws Exception {
        assertEquals("1", 1000, ShortConversionUtil.toPrimitiveShort("1,000"));
    }
}