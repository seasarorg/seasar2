package org.seasar.framework.util;

import junit.framework.TestCase;

public class LongConversionUtilTest extends TestCase {

    public void testToLong() throws Exception {
        assertEquals("1", new Long("1000"), LongConversionUtil.toLong("1,000"));
    }

    public void testToPrimitiveLong() throws Exception {
        assertEquals("1", 1000, LongConversionUtil.toPrimitiveLong("1,000"));
    }
}