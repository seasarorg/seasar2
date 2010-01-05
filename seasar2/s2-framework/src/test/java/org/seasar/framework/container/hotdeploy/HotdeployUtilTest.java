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
package org.seasar.framework.container.hotdeploy;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.seasar.framework.container.impl.S2ContainerBehavior;
import org.seasar.framework.convention.impl.NamingConventionImpl;
import org.seasar.framework.unit.S2FrameworkTestCase;

/**
 * @author higa
 * 
 */
public class HotdeployUtilTest extends S2FrameworkTestCase {

    protected void tearDown() {
        S2ContainerBehavior
                .setProvider(new S2ContainerBehavior.DefaultProvider());
    }

    /**
     * @throws Exception
     */
    public void testIsHotdeploy() throws Exception {
        assertFalse(HotdeployUtil.isHotdeploy());
        S2ContainerBehavior.setProvider(new HotdeployBehavior());
        assertTrue(HotdeployUtil.isHotdeploy());
    }

    /**
     * @throws Exception
     */
    public void testRebuildValue_simpleType() throws Exception {
        assertEquals("111", HotdeployUtil.rebuildValueInternal("111"));
    }

    /**
     * @throws Exception
     */
    public void testRebuildValue_array() throws Exception {
        String[] array = (String[]) HotdeployUtil
                .rebuildValueInternal(new String[] { "aaa", "bbb" });
        assertEquals(2, array.length);
        assertEquals("aaa", array[0]);
        assertEquals("bbb", array[1]);
    }

    /**
     * @throws Exception
     */
    public void testRebuildValue_array_primitive() throws Exception {
        int[] array = (int[]) HotdeployUtil.rebuildValueInternal(new int[] { 1,
                2 });
        assertEquals(2, array.length);
        assertEquals(1, array[0]);
        assertEquals(2, array[1]);
    }

    /**
     * @throws Exception
     */
    public void testRebuildValue_arrayList() throws Exception {
        ArrayList src = new ArrayList();
        src.add("aaa");
        src.add("bbb");
        ArrayList dest = (ArrayList) HotdeployUtil.rebuildValueInternal(src);
        assertEquals(2, dest.size());
        assertEquals("aaa", dest.get(0));
        assertEquals("bbb", dest.get(1));
    }

    /**
     * @throws Exception
     */
    public void testRebuildValue_collection() throws Exception {
        LinkedList src = new LinkedList();
        src.add("aaa");
        src.add("bbb");
        LinkedList dest = (LinkedList) HotdeployUtil.rebuildValueInternal(src);
        assertEquals(2, dest.size());
        assertEquals("aaa", dest.get(0));
        assertEquals("bbb", dest.get(1));
    }

    /**
     * @throws Exception
     */
    public void testRebuildValue_map() throws Exception {
        HashMap src = new HashMap();
        src.put("aaa", "111");
        HashMap dest = (HashMap) HotdeployUtil.rebuildValueInternal(src);
        assertEquals(1, dest.size());
        assertEquals("111", dest.get("aaa"));
    }

    /**
     * @throws Exception
     */
    public void testRebuildValue_bean() throws Exception {
        Hoge src = new Hoge();
        src.setAaa("111");
        Hoge hoge = new Hoge();
        hoge.setAaa("222");
        src.setHoge(hoge);
        Hoge dest = (Hoge) HotdeployUtil.rebuildValueInternal(src);
        assertNotSame(src, dest);
        assertEquals("111", dest.getAaa());
        assertNotNull(dest.getHoge());
        assertEquals("222", dest.getHoge().getAaa());
    }

    /**
     * @throws Exception
     */
    public void testRebuildValue_arrayListWithPrimitiveA() throws Exception {
        boolean defaultDeployMode = isWarmDeploy();
        setWarmDeploy(true);
        try {
            ArrayList list = new ArrayList();
            list.add("aaa");
            list.add("bbb");
            list.add("ccc");
            List value = (List) HotdeployUtil.rebuildValue(list);
            assertTrue(value.size() == 3);
        } finally {
            setWarmDeploy(defaultDeployMode);
        }
    }

    /**
     * @throws Exception
     */
    public void testRebuildValue_arrayListWithDto() throws Exception {
        boolean defaultDeployMode = isWarmDeploy();
        setWarmDeploy(true);
        try {
            ArrayList list = new ArrayList();
            {
                Hoge hoge = new Hoge();
                hoge.setAaa("A");
                list.add(hoge);
            }
            {
                Hoge hoge = new Hoge();
                hoge.setAaa("B");
                list.add(hoge);
            }
            {
                Hoge hoge = new Hoge();
                hoge.setAaa("C");
                list.add(hoge);
            }
            List value = (List) HotdeployUtil.rebuildValue(list);
            assertTrue(value.size() == 3);
            Hoge h = (Hoge) value.get(0);
            assertEquals("A", h.getAaa());
        } finally {
            setWarmDeploy(defaultDeployMode);
        }
    }

    /**
     * @throws Exception
     */
    public void testRebuildValue_arrayListWithDto2() throws Exception {
        boolean defaultDeployMode = isWarmDeploy();
        setWarmDeploy(true);
        try {
            ArrayList list = new ArrayList();
            {
                Hoge hoge = new Hoge();
                hoge.setAaa("A");
                Foo f = new Foo();
                f.setNum(1);
                hoge.setFoo(f);
                list.add(hoge);
            }
            {
                Hoge hoge = new Hoge();
                hoge.setAaa("B");
                Foo f = new Foo();
                f.setNum(2);
                hoge.setFoo(f);
                list.add(hoge);
            }
            {
                Hoge hoge = new Hoge();
                hoge.setAaa("C");
                Foo f = new Foo();
                f.setNum(3);
                hoge.setFoo(f);
                list.add(hoge);
            }
            List value = (List) HotdeployUtil.rebuildValue(list);
            assertTrue(value.size() == 3);
            Hoge h = (Hoge) value.get(0);
            assertEquals("A", h.getAaa());
            assertTrue(h.getFoo().getNum() == 1);
        } finally {
            setWarmDeploy(defaultDeployMode);
        }
    }

    /**
     * @throws Exception
     */
    public void testPerformance() throws Exception {
        int n = 1000;
        Bar[] src = new Bar[n];
        for (int i = 0; i < n; ++i) {
            src[i] = new Bar();
        }
        ClassLoader originalLoader = Thread.currentThread()
                .getContextClassLoader();
        NamingConventionImpl convention = new NamingConventionImpl();
        for (int i = 0; i < 5; ++i) {
            HotdeployClassLoader hotLoader = new HotdeployClassLoader(
                    originalLoader, convention);
            Thread.currentThread().setContextClassLoader(hotLoader);
            try {
                long t1 = System.currentTimeMillis();
                Bar[] dest = (Bar[]) HotdeployUtil.rebuildValueInternal(src);
                long t2 = System.currentTimeMillis();
                System.out.println("" + (t2 - t1) + "ms");
                assertEquals(n, dest.length);
                assertEquals("A", dest[0].s01);
            } finally {
                Thread.currentThread().setContextClassLoader(originalLoader);
            }
        }
    }

    /**
     * @throws Exception
     */
    public void testDeserialize_simpleType() throws Exception {
        assertEquals("111", HotdeployUtil.deserializeInternal(serialize("111")));
    }

    /**
     * @throws Exception
     */
    public void testDeserialize_array() throws Exception {
        String[] array = (String[]) HotdeployUtil
                .deserializeInternal(serialize(new String[] { "aaa", "bbb" }));
        assertEquals(2, array.length);
        assertEquals("aaa", array[0]);
        assertEquals("bbb", array[1]);
    }

    /**
     * @throws Exception
     */
    public void testDeserialize_array_primitive() throws Exception {
        int[] array = (int[]) HotdeployUtil
                .deserializeInternal(serialize(new int[] { 1, 2 }));
        assertEquals(2, array.length);
        assertEquals(1, array[0]);
        assertEquals(2, array[1]);
    }

    /**
     * @throws Exception
     */
    public void testDeserialize_arrayList() throws Exception {
        ArrayList src = new ArrayList();
        src.add("aaa");
        src.add("bbb");
        ArrayList dest = (ArrayList) HotdeployUtil
                .deserializeInternal(serialize(src));
        assertEquals(2, dest.size());
        assertEquals("aaa", dest.get(0));
        assertEquals("bbb", dest.get(1));
    }

    /**
     * @throws Exception
     */
    public void testDeserialize_collection() throws Exception {
        LinkedList src = new LinkedList();
        src.add("aaa");
        src.add("bbb");
        LinkedList dest = (LinkedList) HotdeployUtil
                .deserializeInternal(serialize(src));
        assertEquals(2, dest.size());
        assertEquals("aaa", dest.get(0));
        assertEquals("bbb", dest.get(1));
    }

    /**
     * @throws Exception
     */
    public void testDeserialize_map() throws Exception {
        HashMap src = new HashMap();
        src.put("aaa", "111");
        HashMap dest = (HashMap) HotdeployUtil
                .deserializeInternal(serialize(src));
        assertEquals(1, dest.size());
        assertEquals("111", dest.get("aaa"));
    }

    /**
     * @throws Exception
     */
    public void testDeserialize_bean() throws Exception {
        Hoge src = new Hoge();
        src.setAaa("111");
        Hoge hoge = new Hoge();
        hoge.setAaa("222");
        src.setHoge(hoge);
        Hoge dest = (Hoge) HotdeployUtil.deserializeInternal(serialize(src));
        assertNotSame(src, dest);
        assertEquals("111", dest.getAaa());
        assertNotNull(dest.getHoge());
        assertEquals("222", dest.getHoge().getAaa());
    }

    /**
     * @throws Exception
     */
    public void testDeserialize_arrayListWithPrimitiveA() throws Exception {
        boolean defaultDeployMode = isWarmDeploy();
        setWarmDeploy(true);
        try {
            ArrayList list = new ArrayList();
            list.add("aaa");
            list.add("bbb");
            list.add("ccc");
            List value = (List) HotdeployUtil
                    .deserializeInternal(serialize(list));
            assertTrue(value.size() == 3);
        } finally {
            setWarmDeploy(defaultDeployMode);
        }
    }

    /**
     * @throws Exception
     */
    public void testDeserialize_arrayListWithDto() throws Exception {
        boolean defaultDeployMode = isWarmDeploy();
        setWarmDeploy(true);
        try {
            ArrayList list = new ArrayList();
            {
                Hoge hoge = new Hoge();
                hoge.setAaa("A");
                list.add(hoge);
            }
            {
                Hoge hoge = new Hoge();
                hoge.setAaa("B");
                list.add(hoge);
            }
            {
                Hoge hoge = new Hoge();
                hoge.setAaa("C");
                list.add(hoge);
            }
            List value = (List) HotdeployUtil
                    .deserializeInternal(serialize(list));
            assertTrue(value.size() == 3);
            Hoge h = (Hoge) value.get(0);
            assertEquals("A", h.getAaa());
        } finally {
            setWarmDeploy(defaultDeployMode);
        }
    }

    /**
     * @throws Exception
     */
    public void testDeserialize_arrayListWithDto2() throws Exception {
        boolean defaultDeployMode = isWarmDeploy();
        setWarmDeploy(true);
        try {
            ArrayList list = new ArrayList();
            {
                Hoge hoge = new Hoge();
                hoge.setAaa("A");
                Foo f = new Foo();
                f.setNum(1);
                hoge.setFoo(f);
                list.add(hoge);
            }
            {
                Hoge hoge = new Hoge();
                hoge.setAaa("B");
                Foo f = new Foo();
                f.setNum(2);
                hoge.setFoo(f);
                list.add(hoge);
            }
            {
                Hoge hoge = new Hoge();
                hoge.setAaa("C");
                Foo f = new Foo();
                f.setNum(3);
                hoge.setFoo(f);
                list.add(hoge);
            }
            List value = (List) HotdeployUtil
                    .deserializeInternal(serialize(list));
            assertTrue(value.size() == 3);
            Hoge h = (Hoge) value.get(0);
            assertEquals("A", h.getAaa());
            assertTrue(h.getFoo().getNum() == 1);
        } finally {
            setWarmDeploy(defaultDeployMode);
        }
    }

    private byte[] serialize(Object o) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(o);
        oos.close();
        return baos.toByteArray();
    }

    /**
     * 
     */
    public static class Hoge implements Serializable {

        private static final long serialVersionUID = 1L;

        private String aaa;

        private Hoge hoge;

        private Foo foo;

        /**
         * @return
         */
        public Foo getFoo() {
            return foo;
        }

        /**
         * @param foo
         */
        public void setFoo(Foo foo) {
            this.foo = foo;
        }

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
         * @return Returns the hoge.
         */
        public Hoge getHoge() {
            return hoge;
        }

        /**
         * @param hoge
         *            The hoge to set.
         */
        public void setHoge(Hoge hoge) {
            this.hoge = hoge;
        }
    }

    /**
     * 
     */
    public static class Foo implements Serializable {

        private static final long serialVersionUID = 1L;

        private int num;

        /**
         * @return
         */
        public int getNum() {
            return num;
        }

        /**
         * @param num
         */
        public void setNum(int num) {
            this.num = num;
        }

    }

    /**
     * 
     */
    public static class Bar implements Serializable {

        private static final long serialVersionUID = 1L;

        /** */
        public String s01;

        /** */
        public String s02;

        /** */
        public String s03;

        /** */
        public String s04;

        /** */
        public String s05;

        /** */
        public String s06;

        /** */
        public String s07;

        /** */
        public String s08;

        /** */
        public String s09;

        /** */
        public String s10;

        /** */
        public String s11;

        /** */
        public String s12;

        /** */
        public String s13;

        /** */
        public String s14;

        /** */
        public String s15;

        /** */
        public String s16;

        /** */
        public String s17;

        /** */
        public String s18;

        /** */
        public String s19;

        /** */
        public String s20;

        /**
         * 
         */
        public Bar() {
            s01 = "A";
            s02 = "B";
            s03 = "C";
            s04 = "D";
            s05 = "E";
            s06 = "F";
            s07 = "G";
            s08 = "H";
            s09 = "I";
            s10 = "J";
            s11 = "K";
            s12 = "L";
            s13 = "M";
            s14 = "N";
            s15 = "O";
            s16 = "P";
            s17 = "Q";
            s18 = "R";
            s19 = "S";
            s20 = "T";
        }

    }

}
