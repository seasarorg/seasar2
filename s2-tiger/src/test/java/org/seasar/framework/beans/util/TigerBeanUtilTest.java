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
package org.seasar.framework.beans.util;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

/**
 * @author higa
 */
public class TigerBeanUtilTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testCopyProperties_fromMapToBean() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("aaa", "1");
        Hoge hoge = new Hoge();
        TigerBeanUtil.copyProperties(map, hoge);
        assertEquals(1, hoge.aaa);
    }

    /**
     * @throws Exception
     */
    public void testCopyProperties_fromBeanToMap() throws Exception {
        Hoge hoge = new Hoge();
        hoge.aaa = 1;
        Map<String, Object> map = new HashMap<String, Object>();
        TigerBeanUtil.copyProperties(hoge, map);
        assertEquals(1, map.get("aaa"));
    }

    /**
     * @throws Exception
     */
    public void testCopyProperties_fromBeanToBean() throws Exception {
        Hoge hoge = new Hoge();
        hoge.aaa = 1;
        Hoge hoge2 = new Hoge();
        TigerBeanUtil.copyProperties(hoge, hoge2);
        assertEquals(1, hoge2.aaa);
    }

    /**
     * @throws Exception
     */
    public void testCreateProperties() throws Exception {
        Hoge hoge = new Hoge();
        hoge.aaa = 1;
        Map<String, Object> map = TigerBeanUtil.createProperties(hoge);
        assertEquals(1, map.get("aaa"));
    }

    /**
     * 
     */
    private static class Hoge {

        /**
         * 
         */
        public int aaa;
    }

}
