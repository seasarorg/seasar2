/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.framework.util;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

/**
 * @author higa
 *
 */
public class CaseInsensitiveMapTest extends TestCase {

    private CaseInsensitiveMap map_;

    /**
     * @throws Exception
     */
    public void testContainsKey() throws Exception {
        assertTrue("1", map_.containsKey("ONE"));
        assertTrue("2", map_.containsKey("one"));
        assertTrue("3", !map_.containsKey("onex"));
    }

    /**
     * @throws Exception
     */
    public void testGet() throws Exception {
        assertEquals("1", "1", map_.get("ONE"));
        assertEquals("2", "1", map_.get("One"));
        assertEquals("3", null, map_.get("hoge"));
    }

    /**
     * @throws Exception
     */
    public void testPut() throws Exception {
        assertEquals("1", "1", map_.put("One", "11"));
        assertEquals("2", "11", map_.get("one"));
    }

    /**
     * @throws Exception
     */
    public void testRemove() throws Exception {
        assertEquals("1", "1", map_.remove("ONE"));
        assertEquals("2", 1, map_.size());
        assertEquals("3", null, map_.remove("dummy"));
    }

    /**
     * @throws Exception
     */
    public void testPutAll() throws Exception {
        Map m = new HashMap();
        m.put("three", "3");
        m.put("four", "4");
        map_.putAll(m);
        assertEquals("1", "3", map_.get("THREE"));
        assertEquals("2", "4", map_.get("FOUR"));
        assertEquals("3", 4, map_.size());
    }

    /**
     * @throws Exception
     */
    public void testPerformance() throws Exception {
        for (int j = 0; j < 3; ++j) {

            int num = 100000;
            Map hmap = new HashMap();
            Map cimap = new CaseInsensitiveMap();

            long start = System.currentTimeMillis();
            for (int i = 0; i < num; i++) {
                hmap.put("a" + String.valueOf(i), null);
            }
            System.out.println("HashMap.put:"
                    + (System.currentTimeMillis() - start));

            start = System.currentTimeMillis();
            for (int i = 0; i < num; i++) {
                cimap.put("a" + String.valueOf(i), null);
            }
            System.out.println("CaseInsensitiveMap.put:"
                    + (System.currentTimeMillis() - start));

            start = System.currentTimeMillis();
            for (int i = 0; i < num; i++) {
                hmap.get("a" + String.valueOf(i));
            }
            System.out.println("HashMap.get:"
                    + (System.currentTimeMillis() - start));

            start = System.currentTimeMillis();
            for (int i = 0; i < num; i++) {
                cimap.get("a" + String.valueOf(i));
            }
            System.out.println("CaseInsensitiveMap.get:"
                    + (System.currentTimeMillis() - start));

        }

    }

    protected void setUp() throws Exception {
        map_ = new CaseInsensitiveMap();
        map_.put("one", "1");
        map_.put("two", "2");
    }

    protected void tearDown() throws Exception {
        map_ = null;
    }
}
