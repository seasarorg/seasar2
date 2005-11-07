package org.seasar.framework.util;

import java.util.Set;

import junit.framework.TestCase;

public class CaseInsensitiveSetTest extends TestCase {

    public void testContains() throws Exception {
        Set set = new CaseInsensitiveSet();
        set.add("one");
        assertEquals("1", true, set.contains("ONE"));
    }
}