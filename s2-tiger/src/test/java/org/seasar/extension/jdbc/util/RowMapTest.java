package org.seasar.extension.jdbc.util;

import junit.framework.TestCase;

/**
 * @author higa
 * 
 */
public class RowMapTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testGet() throws Exception {
        RowMap row = new RowMap();
        row.put("aaa", 1);
        row.put("bbb", 2);
        assertEquals(1, row.get("aaa"));
        try {
            row.get("xxx");
            fail();
        } catch (IllegalArgumentException e) {
            System.out.println(e);
        }
    }
}
