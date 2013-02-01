/*
 * Copyright 2004-2013 the Seasar Foundation and the Others.
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
