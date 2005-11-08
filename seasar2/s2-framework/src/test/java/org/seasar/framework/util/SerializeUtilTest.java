package org.seasar.framework.util;

import junit.framework.TestCase;

public class SerializeUtilTest extends TestCase {

    public void testSerialize() throws Exception {
        String[] a = new String[] { "1", "2" };
        String[] b = (String[]) SerializeUtil.serialize(a);
        assertEquals("1", b.length, a.length);
        assertEquals("2", "1", b[0]);
        assertEquals("3", "2", b[1]);
    }
}