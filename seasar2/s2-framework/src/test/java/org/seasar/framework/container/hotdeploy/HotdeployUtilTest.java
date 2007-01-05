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
package org.seasar.framework.container.hotdeploy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import org.seasar.framework.container.impl.S2ContainerBehavior;
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

    public void testIsHotdeploy() throws Exception {
        assertFalse(HotdeployUtil.isHotdeploy());
        S2ContainerBehavior.setProvider(new HotdeployBehavior());
        assertTrue(HotdeployUtil.isHotdeploy());
    }

    public void testRebuildValue_simpleType() throws Exception {
        assertEquals("111", HotdeployUtil.rebuildValueInternal("111"));
    }

    public void testRebuildValue_array() throws Exception {
        String[] array = (String[]) HotdeployUtil
                .rebuildValueInternal(new String[] { "aaa", "bbb" });
        assertEquals(2, array.length);
        assertEquals("aaa", array[0]);
        assertEquals("bbb", array[1]);
    }

    public void testRebuildValue_array_primitive() throws Exception {
        int[] array = (int[]) HotdeployUtil.rebuildValueInternal(new int[] { 1,
                2 });
        assertEquals(2, array.length);
        assertEquals(1, array[0]);
        assertEquals(2, array[1]);
    }

    public void testRebuildValue_arrayList() throws Exception {
        ArrayList src = new ArrayList();
        src.add("aaa");
        src.add("bbb");
        ArrayList dest = (ArrayList) HotdeployUtil.rebuildValueInternal(src);
        assertEquals(2, dest.size());
        assertEquals("aaa", dest.get(0));
        assertEquals("bbb", dest.get(1));
    }

    public void testRebuildValue_collection() throws Exception {
        LinkedList src = new LinkedList();
        src.add("aaa");
        src.add("bbb");
        LinkedList dest = (LinkedList) HotdeployUtil.rebuildValueInternal(src);
        assertEquals(2, dest.size());
        assertEquals("aaa", dest.get(0));
        assertEquals("bbb", dest.get(1));
    }

    public void testRebuildValue_map() throws Exception {
        HashMap src = new HashMap();
        src.put("aaa", "111");
        HashMap dest = (HashMap) HotdeployUtil.rebuildValueInternal(src);
        assertEquals(1, dest.size());
        assertEquals("111", dest.get("aaa"));
    }

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

    public static class Hoge {
        private String aaa;

        private Hoge hoge;

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
}