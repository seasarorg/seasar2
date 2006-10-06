/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
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
package org.seasar.framework.beans.util;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

/**
 * @author higa
 * 
 */
public class BeanUtilTest extends TestCase {

    public void testCopyProperties() throws Exception {
        Map src = new HashMap();
        src.put("aaa", "111");
        src.put("ccc", "333");
        MyClass dest = new MyClass();
        BeanUtil.copyProperties(src, dest);
        assertEquals("111", dest.getAaa());
        assertNull(dest.getBbb());
    }

    public void testCopyToMap() throws Exception {
        HogeDto hoge = new HogeDto();
        hoge.setA("A");
        hoge.setB(true);
        hoge.setC(3);
        Map map = new HashMap();
        BeanUtil.copyProperties(hoge, map);
        assertNotNull(map);
        assertEquals("A", map.get("a"));
        assertEquals(new Boolean(true), map.get("b"));
        assertEquals(new Integer(3), map.get("c"));
    }

    public void testCopyToBean() throws Exception {
        Map map = new HashMap();
        map.put("a", "A");
        map.put("b", new Boolean(true));
        map.put("c", new Integer(3));
        map.put("d", new Double(1.4));
        HogeDto hoge = new HogeDto();
        BeanUtil.copyProperties(map, hoge);
        assertEquals("A", hoge.getA());
        assertEquals(new Boolean(true), new Boolean(hoge.isB()));
        assertEquals(new Integer(3), new Integer(hoge.getC()));
    }

    public static class HogeDto {
        private String a;

        private boolean b;

        private int c;

        public String getA() {
            return a;
        }

        public void setA(String a) {
            this.a = a;
        }

        public boolean isB() {
            return b;
        }

        public void setB(boolean b) {
            this.b = b;
        }

        public int getC() {
            return c;
        }

        public void setC(int c) {
            this.c = c;
        }
    }

    public static class MyClass {
        private String aaa;

        private String bbb;

        /**
         * @return Returns the aaa.
         */
        public String getAaa() {
            return aaa;
        }

        /**
         * @param aaa
         *            The aaa to set.
         */
        public void setAaa(String aaa) {
            this.aaa = aaa;
        }

        /**
         * @return Returns the bbb.
         */
        public String getBbb() {
            return bbb;
        }

        /**
         * @param bbb
         *            The bbb to set.
         */
        public void setBbb(String bbb) {
            this.bbb = bbb;
        }
    }
}
