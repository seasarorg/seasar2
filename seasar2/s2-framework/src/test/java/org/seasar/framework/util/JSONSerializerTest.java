/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

/**
 * {@link JSONSerializer}のテストクラスです。
 * 
 * @author mopemope
 */
public class JSONSerializerTest extends TestCase {
    /**
     * @throws Exception
     */
    public void testToString_null() throws Exception {
        assertEquals("null", JSONSerializer.serialize(null));
    }

    /**
     * @throws Exception
     */
    public void testToJson_boolean() throws Exception {
        assertEquals("true", JSONSerializer.serialize(Boolean.TRUE));
        assertEquals("false", JSONSerializer.serialize(Boolean.FALSE));
    }

    /**
     * @throws Exception
     */
    public void testToJson_string() throws Exception {
        assertEquals("\"a\"", JSONSerializer.serialize("a"));
    }

    /**
     * @throws Exception
     */
    public void testToJson_float() throws Exception {
        assertEquals("1.0", JSONSerializer.serialize(new Float(1.0f)));
        try {
            JSONSerializer.serialize(new Float(Float.NaN));
            fail();
        } catch (IllegalArgumentException e) {
            System.out.println(e);
        }
    }

    /**
     * @throws Exception
     */
    public void testToJson_double() throws Exception {
        assertEquals("1.0", JSONSerializer.serialize(new Double(1.0d)));
        try {
            JSONSerializer.serialize(new Double(Double.NaN));
            fail();
        } catch (IllegalArgumentException e) {
            System.out.println(e);
        }
    }

    /**
     * @throws Exception
     */
    public void testToJson_number() throws Exception {
        assertEquals("100", JSONSerializer.serialize(new Integer(100)));
    }

    /**
     * @throws Exception
     */
    public void testToJson_array() throws Exception {
        assertEquals("[]", JSONSerializer.serialize(new Object[0]));
        assertEquals("[1]", JSONSerializer
                .serialize(new Integer[] { new Integer(1) }));
        assertEquals("[1,2]", JSONSerializer.serialize(new Integer[] {
                new Integer(1), new Integer(2) }));
    }

    /**
     * @throws Exception
     */
    public void testToJson_collection() throws Exception {
        assertEquals("[]", JSONSerializer.serialize(new ArrayList()));
        ArrayList list = new ArrayList();
        list.add(new Integer(1));
        assertEquals("[1]", JSONSerializer.serialize(list));
    }

    /**
     * @throws Exception
     */
    public void testToJson_map() throws Exception {
        assertEquals("{}", JSONSerializer.serialize(new HashMap()));
        HashMap map = new HashMap();
        map.put("aaa", new Integer(1));
        assertEquals("{aaa:1}", JSONSerializer.serialize(map));
        map.put("bbb", new Integer(2));
        assertEquals("{aaa:1,bbb:2}", JSONSerializer.serialize(map));
        map.put("bbb", new HashMap());
        assertEquals("{aaa:1,bbb:{}}", JSONSerializer.serialize(map));
    }

    /**
     * @throws Exception
     */
    public void testToJson_PrimitiveArray() throws Exception {
        assertEquals("[1]", JSONSerializer.serialize(new int[] { 1 }));
        assertEquals("[1,2,3]", JSONSerializer.serialize(new int[] { 1, 2, 3 }));
        assertEquals("[true]", JSONSerializer.serialize(new boolean[] { true }));
        assertEquals("[true,false,true]", JSONSerializer
                .serialize(new boolean[] { true, false, true }));
        assertEquals("[1.11]", JSONSerializer.serialize(new double[] { 1.11 }));
        assertEquals("[1.11,2.22,3.33]", JSONSerializer.serialize(new double[] {
                1.11, 2.22, 3.33 }));

        // assertEquals(
        // "{bbb:[11,22,33],ccc:[true,false,true],ddd:[1.11,2.22,3.33]}",
        // JSONSerializer.serialize(new Test2()));
        System.out.println(JSONSerializer.serialize(new Test2()));
    }

    /**
     * @throws Exception
     */
    public void testToJson_bean() throws Exception {
        String s = JSONSerializer.serialize(new MyBean());
        System.out.println(s);
        assertTrue(s.indexOf("aaa:null") > 0);
        assertTrue(s.indexOf("bbb:null") > 0);

        MyBean bean = new MyBean();
        bean.setBbb(new MyBean());
        s = JSONSerializer.serialize(bean);
        System.out.println(s);
    }

    /**
     * @throws Exception
     */
    public void testToJson_htmlBean1() throws Exception {
        final HtmlBean bean = new HtmlBean();
        final String s = JSONSerializer.serialize(bean);
        assertTrue(s.indexOf("status:null") > 0);
        assertTrue(s.indexOf("html:null") > 0);
    }

    /**
     * @throws Exception
     */
    public void testToJson_htmlBean2() throws Exception {
        final HtmlBean bean = new HtmlBean();
        bean.setStatus(new Integer(30));
        bean.setHtml("<div>hoge</div>");
        final String s = JSONSerializer.serialize(bean);
        assertTrue(s.indexOf("status:30") > 0);
        assertTrue(s.indexOf("html:\"<div>hoge<\\/div>\"") > 0);
    }

    /**
     * @throws Exception
     */
    public void testQuote() throws Exception {
        assertEquals("\"a\"", JSONSerializer.quote("a"));
        assertEquals("\"\\t\"", JSONSerializer.quote("\t"));
        assertEquals("\"\\n\"", JSONSerializer.quote("\n"));
        assertEquals("\"\\u0000\"", JSONSerializer.quote("\0"));
        assertEquals("\"\\\"\"", JSONSerializer.quote("\""));
        assertEquals("\"\\\\\"", JSONSerializer.quote("\\"));
        assertEquals("\"\\/\"", JSONSerializer.quote("/"));
    }

    /**
     * 
     */
    public void testIsObject1() {
        String str = "{'test1':'test2'}";
        boolean result = JSONSerializer.isObject(str);
        assertTrue(result);
    }

    /**
     * 
     */
    public void testIsObject2() {
        String str = "['test1','test2']";
        boolean result = JSONSerializer.isObject(str);
        assertFalse(result);
    }

    /**
     * 
     */
    public void testIsObject3() {
        String str = "'test2'";
        boolean result = JSONSerializer.isObject(str);
        assertFalse(result);
    }

    /**
     * 
     */
    public void testIsArray1() {
        String str = "['test1','test2']";
        boolean result = JSONSerializer.isArray(str);
        assertTrue(result);
    }

    /**
     * 
     */
    public void testIsArray2() {
        String str = "{'test1','test2'}";
        boolean result = JSONSerializer.isArray(str);
        assertFalse(result);
    }

    /**
     * 
     */
    public void testIsArray3() {
        String str = "'test2'";
        boolean result = JSONSerializer.isArray(str);
        assertFalse(result);
    }

    /**
     * 
     */
    public void testParseObject1() {
        String str = "{'test1':'test2'}";
        Map map = JSONSerializer.evalMap(str);
        assertEquals("{test1=test2}", map.toString());
    }

    /**
     * 
     */
    public void testParseObject2() {
        String str = "{'test1':test2}";
        Map map = JSONSerializer.evalMap(str);
        assertEquals("{test1=test2}", map.toString());
    }

    /**
     * 
     */
    public void testParseObject3() {
        String str = "{'test1':3}";
        Map map = JSONSerializer.evalMap(str);
        Integer i = (Integer) map.get("test1");
        assertEquals(i, new Integer(3));
    }

    /**
     * 
     */
    public void testParseObject4() {
        String str = "{'test1':'4'}";
        Map map = JSONSerializer.evalMap(str);
        String i = (String) map.get("test1");
        assertEquals(i, "4");
    }

    /**
     * 
     */
    public void testParseObject5() {
        String str = "{'test2':{'test1':'5'}}";
        Map map = JSONSerializer.evalMap(str);
        Map m = (Map) map.get("test2");
        assertNotNull(m);
        String s = (String) m.get("test1");
        assertEquals(s, "5");
    }

    /**
     * 
     */
    public void testParseObject6() {
        String str = "{'test1':{'test2':'2'},'test3':{'test4':'4'}}";
        Map map = JSONSerializer.evalMap(str);
        assertEquals("{test3={test4=4}, test1={test2=2}}", map.toString());
    }

    /**
     * 
     */
    public void testParseArray1() {
        String str = "['test1']";
        List list = JSONSerializer.evalArray(str);
        assertEquals("test1", list.get(0));
    }

    /**
     * 
     */
    public void testParseArray2() {
        String str = "['test1', 'test2']";
        List list = JSONSerializer.evalArray(str);
        assertEquals("test1", list.get(0));
        assertEquals("test2", list.get(1));
    }

    /**
     * 
     */
    public void testParseArray3() {
        String str = "[1, 2]";
        List list = JSONSerializer.evalArray(str);
        assertEquals(new Integer(1), list.get(0));
        assertEquals(new Integer(2), list.get(1));
    }

    /**
     * 
     */
    public void testParseArray4() {
        String str = "[1, [2, 3, 4, 5], 3]";
        List list = JSONSerializer.evalArray(str);
        assertEquals(new Integer(1), list.get(0));
        assertEquals(list.toString(), "[1, [2, 3, 4, 5], 3]");
    }

    /**
     * 
     */
    public void testParseArray5() {
        String str = "[{'test1':1}, {'test2':'2'}]";
        List list = JSONSerializer.evalArray(str);
        Map map1 = (Map) list.get(0);
        Integer i = (Integer) map1.get("test1");
        assertEquals(i, new Integer(1));
        Map map2 = (Map) list.get(1);
        String s = (String) map2.get("test2");
        assertEquals(s, "2");
    }

    /**
     * 
     */
    public void testParseArray6() {
        String str = "[{'test1':{'test2':'2'},'test3':{'test4':'4'}}]";
        List list = JSONSerializer.evalArray(str);
        assertEquals("[{test3={test4=4}, test1={test2=2}}]", list.toString());
    }

    /**
     * @author mopemope
     */
    public static class MyBean {
        private String aaa;

        private MyBean bbb;

        /**
         * @return
         */
        public String getAaa() {
            return aaa;
        }

        /**
         * @param aaa
         */
        public void setAaa(String aaa) {
            this.aaa = aaa;
        }

        /**
         * @return
         */
        public MyBean getBbb() {
            return bbb;
        }

        /**
         * @param bbb
         */
        public void setBbb(MyBean bbb) {
            this.bbb = bbb;
        }
    }

    /**
     * @author mopemope
     */
    public static class Test1 {
        private String aaa = "hoge";

        private int bbb = 100;

        private boolean ccc = true;

        private double ddd = 3.14;

        /**
         * @return
         */
        public String getAaa() {
            return aaa;
        }

        /**
         * @return
         */
        public int getBbb() {
            return bbb;
        }

        /**
         * @return
         */
        public boolean isCcc() {
            return ccc;
        }

        /**
         * @return
         */
        public double getDdd() {
            return ddd;
        }
    }

    /**
     * @author mopemope
     */
    public static class Test2 {

        private int[] bbb = { 11, 22, 33 };

        private boolean[] ccc = { true, false, true };

        private double[] ddd = { 1.11, 2.22, 3.33 };

        /**
         * @return
         */
        public int[] getBbb() {
            return bbb;
        }

        /**
         * @return
         */
        public boolean[] getCcc() {
            return ccc;
        }

        /**
         * @return
         */
        public double[] getDdd() {
            return ddd;
        }
    }

    /**
     * @author mopemope
     */
    public static class HtmlBean {

        private Integer status;

        private String html;

        /**
         * @return
         */
        public String getHtml() {
            return html;
        }

        /**
         * @param html
         */
        public void setHtml(final String html) {
            this.html = html;
        }

        /**
         * @return
         */
        public Integer getStatus() {
            return status;
        }

        /**
         * @param status
         */
        public void setStatus(final Integer status) {
            this.status = status;
        }

    }

}
