package org.seasar.framework.beans.util;

import junit.framework.TestCase;

/**
 * @author higa
 * 
 */
public class BeanMapTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testGet() throws Exception {
        BeanMap map = new BeanMap();
        map.put("aaa", 1);
        map.put("bbb", 2);
        assertEquals(1, map.get("aaa"));
        try {
            map.get("xxx");
            fail();
        } catch (IllegalArgumentException e) {
            System.out.println(e);
        }
    }
}
