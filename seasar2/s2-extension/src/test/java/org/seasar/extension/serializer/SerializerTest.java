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
package org.seasar.extension.serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import org.seasar.framework.util.ArrayMap;
import org.seasar.framework.util.LruHashMap;

/**
 * @author higa
 * 
 */
public class SerializerTest extends TestCase {

    private static final int TEST_COUNT = 10000;

    /**
     * @throws Exception
     */
    public void testNull() throws Exception {
        executeTest(null);
    }

    /**
     * @throws Exception
     */
    public void testString() throws Exception {
        executeTest("hoge");
    }

    /**
     * @throws Exception
     */
    public void testInteger() throws Exception {
        executeTest(new Integer(1));
    }

    /**
     * @throws Exception
     */
    public void testBoolean() throws Exception {
        executeTest(Boolean.TRUE);
    }

    /**
     * @throws Exception
     */
    public void testDate() throws Exception {
        executeTest(new Date());
    }

    /**
     * @throws Exception
     */
    public void testBigDecimal() throws Exception {
        executeTest(new BigDecimal("123"));
    }

    /**
     * @throws Exception
     */
    public void testLong() throws Exception {
        executeTest(new Long(1));
    }

    /**
     * @throws Exception
     */
    public void testShort() throws Exception {
        executeTest(new Short((short) 1));
    }

    /**
     * @throws Exception
     */
    public void testByte() throws Exception {
        executeTest(new Byte((byte) 1));
    }

    /**
     * @throws Exception
     */
    public void testFloat() throws Exception {
        executeTest(new Float(1f));
    }

    /**
     * @throws Exception
     */
    public void testDouble() throws Exception {
        executeTest(new Double(1d));
    }

    /**
     * @throws Exception
     */
    public void testBigInteger() throws Exception {
        executeTest(new BigInteger("1"));
    }

    /**
     * @throws Exception
     */
    public void testCharacter() throws Exception {
        executeTest(new Character('a'));
    }

    /**
     * @throws Exception
     */
    public void testCalendar() throws Exception {
        executeTest(Calendar.getInstance());
    }

    /**
     * @throws Exception
     */
    public void testSqlDate() throws Exception {
        executeTest(new java.sql.Date(1));
    }

    /**
     * @throws Exception
     */
    public void testTime() throws Exception {
        executeTest(new Time(1));
    }

    /**
     * @throws Exception
     */
    public void testTimestamp() throws Exception {
        executeTest(new Timestamp(1));
    }

    /**
     * @throws Exception
     */
    public void testArray() throws Exception {
        executeTest(new int[] { 1 });
        executeTest(new String[] { "1" });
        Object[] array = new Object[1];
        array[0] = array;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);
        Serializer.writeObject(out, array);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        DataInputStream in = new DataInputStream(bais);
        Object[] o = (Object[]) Serializer.readObject(in);
        assertSame(o, o[0]);
    }

    /**
     * @throws Exception
     */
    public void testArrayList() throws Exception {
        ArrayList list = new ArrayList();
        list.add("aaa");
        executeTest(list);
        ArrayList arrayList = new ArrayList();
        arrayList.add(arrayList);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);
        Serializer.writeObject(out, arrayList);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        DataInputStream in = new DataInputStream(bais);
        ArrayList o = (ArrayList) Serializer.readObject(in);
        assertSame(o, o.get(0));
    }

    /**
     * @throws Exception
     */
    public void testList() throws Exception {
        LinkedList list = new LinkedList();
        list.add("aaa");
        executeTest(list);
        list = new LinkedList();
        list.add(list);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);
        Serializer.writeObject(out, list);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        DataInputStream in = new DataInputStream(bais);
        List o = (List) Serializer.readObject(in);
        assertSame(o, o.get(0));
    }

    /**
     * @throws Exception
     */
    public void testHashMap() throws Exception {
        HashMap map = new HashMap();
        map.put("aaa", "111");
        executeTest(map);
        map = new HashMap();
        map.put("aaa", map);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);
        Serializer.writeObject(out, map);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        DataInputStream in = new DataInputStream(bais);
        Map o = (Map) Serializer.readObject(in);
        assertSame(o, o.get("aaa"));
    }

    /**
     * @throws Exception
     */
    public void testLruHashMap() throws Exception {
        LruHashMap map = new LruHashMap(2);
        map.put("aaa", "111");
        executeTest(map);
        map = new LruHashMap(2);
        map.put("aaa", map);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);
        Serializer.writeObject(out, map);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        DataInputStream in = new DataInputStream(bais);
        Map o = (Map) Serializer.readObject(in);
        assertSame(o, o.get("aaa"));
    }

    /**
     * @throws Exception
     */
    public void testArrayMap() throws Exception {
        ArrayMap map = new ArrayMap();
        map.put("aaa", "111");
        executeTest(map);
        map = new ArrayMap();
        map.put("aaa", map);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);
        Serializer.writeObject(out, map);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        DataInputStream in = new DataInputStream(bais);
        Map o = (Map) Serializer.readObject(in);
        assertSame(o, o.get("aaa"));
    }

    /**
     * @throws Exception
     */
    public void testMap() throws Exception {
        LinkedHashMap map = new LinkedHashMap();
        map.put("aaa", "111");
        executeTest(map);
        map = new LinkedHashMap();
        map.put("aaa", map);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);
        Serializer.writeObject(out, map);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        DataInputStream in = new DataInputStream(bais);
        Map o = (Map) Serializer.readObject(in);
        assertSame(o, o.get("aaa"));
    }

    /**
     * @throws Exception
     */
    public void testHashSet() throws Exception {
        HashSet set = new HashSet();
        set.add("aaa");
        executeTest(set);
        set = new HashSet();
        set.add(set);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);
        Serializer.writeObject(out, set);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        DataInputStream in = new DataInputStream(bais);
        Set o = (Set) Serializer.readObject(in);
        assertSame(o, o.iterator().next());
    }

    /**
     * @throws Exception
     */
    public void testSet() throws Exception {
        LinkedHashSet set = new LinkedHashSet();
        set.add("aaa");
        executeTest(set);
        set = new LinkedHashSet();
        set.add(set);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);
        Serializer.writeObject(out, set);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        DataInputStream in = new DataInputStream(bais);
        Set o = (Set) Serializer.readObject(in);
        assertSame(o, o.iterator().next());
    }

    /**
     * @throws Exception
     */
    public void testBean() throws Exception {
        HogeBean bean = new HogeBean();
        bean.setAaa("aaa");
        executeTest(bean);
        bean.setHogeBean(bean);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);
        Serializer.writeObject(out, bean);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        DataInputStream in = new DataInputStream(bais);
        HogeBean o = (HogeBean) Serializer.readObject(in);
        assertSame(o, o.hogeBean);
    }

    protected void executeTest(Object obj) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);
        Serializer.writeObject(out, obj);
        if (obj == null) {
            System.out.println("null test");
        } else {
            System.out.println(obj.getClass().getName() + " test");
        }
        System.out.println("byte[] size:" + baos.size());
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        DataInputStream in = new DataInputStream(bais);
        Object o = Serializer.readObject(in);
        if (obj == null) {
            assertNull(o);
        } else if (obj.getClass().isArray()) {
            int size = Array.getLength(o);
            for (int i = 0; i < size; i++) {
                assertEquals(Array.get(obj, i), Array.get(o, i));
            }
        } else {
            assertEquals(obj, o);
        }
        serializerPerformance(obj);
        javaSerializePerformance(obj);
    }

    protected void serializerPerformance(Object obj) {
        long time = System.currentTimeMillis();
        for (int i = 0; i < TEST_COUNT; i++) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(baos);
            Serializer.writeObject(out, obj);
        }
        System.out.println("Serializer performance:"
                + (System.currentTimeMillis() - time));
    }

    protected void javaSerializePerformance(Object obj) throws Exception {
        long time = System.currentTimeMillis();
        for (int i = 0; i < TEST_COUNT; i++) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(baos);
            out.writeObject(obj);
        }
        System.out.println("Java Serialize performance:"
                + (System.currentTimeMillis() - time));
    }

    public static class HogeBean implements Serializable {
        private String aaa;

        private HogeBean hogeBean;

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
         * @return Returns the hogeBean.
         */
        public HogeBean getHogeBean() {
            return hogeBean;
        }

        /**
         * @param hogeBean
         *            The hogeBean to set.
         */
        public void setHogeBean(HogeBean hogeBean) {
            this.hogeBean = hogeBean;
        }

        public String getBbb() {
            return "abc";
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (o == null) {
                return false;
            }
            if (!(o instanceof HogeBean)) {
                return false;
            }
            HogeBean other = (HogeBean) o;
            if (aaa == null && other.aaa != null) {
                return false;
            }
            if (aaa != null && !aaa.equals(other.aaa)) {
                return false;
            }
            if (hogeBean == null && other.hogeBean != null) {
                return false;
            }
            if (hogeBean != null && !hogeBean.equals(other.hogeBean)) {
                return false;
            }
            return true;
        }
    }
}