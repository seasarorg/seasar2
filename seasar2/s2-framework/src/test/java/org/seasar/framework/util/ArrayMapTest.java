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
import java.util.Iterator;
import java.util.Map;

import junit.framework.TestCase;

/**
 * @author higa
 *
 */
public class ArrayMapTest extends TestCase {

    private ArrayMap _map;

    /**
     * @throws Exception
     */
    public void testSize() throws Exception {
        assertEquals("1", 3, _map.size());
        _map.put("3", "test3");
        assertEquals("2", 4, _map.size());
    }

    /**
     * @throws Exception
     */
    public void testIsEmpty() throws Exception {
        assertTrue("1", !_map.isEmpty());
        _map.clear();
        assertTrue("2", _map.isEmpty());
    }

    /**
     * @throws Exception
     */
    public void testContainsValue() throws Exception {
        assertTrue("1", _map.containsValue("test2"));
        assertTrue("2", !_map.containsValue("test3"));
    }

    /**
     * @throws Exception
     */
    public void testContainsKey() throws Exception {
        assertTrue("1", _map.containsKey("2"));
        assertTrue("2", !_map.containsKey("3"));
        _map.put("3", null);
        assertTrue("3", _map.containsKey("3"));
    }

    /**
     * @throws Exception
     */
    public void testIndexOf() throws Exception {
        assertEquals("1", 1, _map.indexOf("test"));
        assertEquals("1", 0, _map.indexOf(null));
        assertEquals("2", -1, _map.indexOf("test3"));
    }

    /**
     * @throws Exception
     */
    public void testGet() throws Exception {
        assertEquals("1", "test", _map.get("1"));
        assertEquals("2", null, _map.get(null));
        assertEquals("3", null, _map.get("test3"));
        assertEquals("4", null, _map.get(0));
    }

    /**
     * @throws Exception
     */
    public void testPut() throws Exception {
        assertEquals("1", "test", _map.put("1", "test3"));
        assertEquals("2", "test3", _map.get("1"));
        assertEquals("3", "test3", _map.get(1));
        _map.put(null, "test4");
        _map.put(null, "test5");
    }

    /**
     * @throws Exception
     */
    public void testRemove() throws Exception {
        assertEquals("1", "test", _map.remove("1"));
        assertEquals("2", 2, _map.size());
        assertEquals("3", null, _map.remove("dummy"));
        assertEquals("4", null, _map.remove(0));
    }

    /**
     * @throws Exception
     */
    public void testRemove2() throws Exception {
        Map m = new ArrayMap();
        m.put("1", "d");
        m.remove("1");
        assertEquals("1", false, m.containsKey("1"));
        m.put("1", "d");
        m.remove("1");
        assertEquals("2", false, m.containsKey("1"));
    }

    /**
     * @throws Exception
     */
    public void testRemove3() throws Exception {
        Map m = new ArrayMap();
        m.put(new MyKey("1"), "d");
        m.put(new MyKey("2"), "d");
        m.remove(new MyKey("1"));
        assertEquals("1", false, m.containsKey(new MyKey("1")));
    }

    /**
     * @throws Exception
     */
    public void testRemove4() throws Exception {
        ArrayMap m = new ArrayMap();
        m.put("1", "d");
        m.put("2", "d");
        System.out.println("remove before:" + m);
        m.remove("2");
        System.out.println("remove after:" + m);
        assertEquals("1", false, m.containsKey("2"));
        assertEquals("2", true, m.containsKey("1"));
        assertEquals("3", "d", m.get("1"));
        assertEquals("4", null, m.get("2"));
        assertEquals("5", "d", m.get(0));
    }

    /**
     * @throws Exception
     */
    public void testPutAll() throws Exception {
        Map m = new HashMap();
        m.put("3", "test3");
        m.put("4", "test4");
        _map.putAll(m);
        assertEquals("1", "test3", _map.get("3"));
        assertEquals("2", "test4", _map.get("4"));
        assertEquals("3", 5, _map.size());
    }

    /**
     * @throws Exception
     */
    public void testEqaulas() throws Exception {
        Map copy = (ArrayMap) _map.clone();
        assertTrue("1", _map.equals(copy));
        assertTrue("2", !_map.equals(null));
        _map.put("3", "test3");
        assertTrue("3", !_map.equals(copy));
    }

    /**
     * @throws Exception
     */
    public void testToString() throws Exception {
        assertNotNull("1", _map.toString());
    }

    /**
     * @throws Exception
     */
    public void testClear() throws Exception {
        _map.clear();
        assertEquals("1", 0, _map.size());
    }

    /**
     * @throws Exception
     */
    public void testEntrySet() throws Exception {
        Iterator i = _map.entrySet().iterator();
        assertEquals("1", null, ((Map.Entry) i.next()).getKey());
        assertEquals("2", "1", ((Map.Entry) i.next()).getKey());
        assertEquals("1", "2", ((Map.Entry) i.next()).getKey());
    }

    /**
     * @throws Exception
     */
    public void testSerialize() throws Exception {
        ArrayMap copy = (ArrayMap) SerializeUtil.serialize(_map);
        assertEquals("1", null, copy.get(0));
        assertEquals("2", "test", copy.get(1));
        assertEquals("3", "test2", copy.get(2));
        _map.equals(copy);
    }

    /**
     * @throws Exception
     */
    public void testPerformance() throws Exception {
        int num = 100000;
        Map hmap = new HashMap();
        Map amap = new ArrayMap();

        long start = System.currentTimeMillis();
        for (int i = 0; i < num; i++) {
            hmap.put(String.valueOf(i), null);
        }
        System.out.println("HashMap.put:"
                + (System.currentTimeMillis() - start));

        start = System.currentTimeMillis();
        for (int i = 0; i < num; i++) {
            amap.put(String.valueOf(i), null);
        }
        System.out.println("ArrayMap.put:"
                + (System.currentTimeMillis() - start));

        start = System.currentTimeMillis();
        for (int i = 0; i < num; i++) {
            hmap.get(String.valueOf(i));
        }
        System.out.println("HashMap.get:"
                + (System.currentTimeMillis() - start));

        start = System.currentTimeMillis();
        for (int i = 0; i < num; i++) {
            amap.get(String.valueOf(i));
        }
        System.out.println("ArrayMap.get:"
                + (System.currentTimeMillis() - start));

        start = System.currentTimeMillis();
        for (Iterator i = hmap.entrySet().iterator(); i.hasNext();) {
            i.next();
        }
        System.out.println("HashMap iteration:"
                + (System.currentTimeMillis() - start));

        start = System.currentTimeMillis();
        for (Iterator i = amap.entrySet().iterator(); i.hasNext();) {
            i.next();
        }
        System.out.println("ArrayMap iteration:"
                + (System.currentTimeMillis() - start));

        start = System.currentTimeMillis();
        SerializeUtil.serialize(hmap);
        System.out.println("HashMap serialize:"
                + (System.currentTimeMillis() - start));

        start = System.currentTimeMillis();
        SerializeUtil.serialize(amap);
        System.out.println("ArrayMap serialize:"
                + (System.currentTimeMillis() - start));
    }

    protected void setUp() throws Exception {
        _map = new ArrayMap();
        _map.put(null, null);
        _map.put("1", "test");
        _map.put("2", "test2");
    }

    protected void tearDown() throws Exception {
        _map = null;
    }

    private static class MyKey {
        Object _key;

        MyKey(Object key) {
            _key = key;
        }

        public int hashCode() {
            return 0;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (o == null || !(o instanceof MyKey)) {
                return false;
            }
            return _key.equals(((MyKey) o)._key);
        }
    }
}