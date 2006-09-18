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
package org.seasar.framework.util;

import java.util.Iterator;

import junit.framework.TestCase;

/**
 * @author taichi
 * 
 */
public class MruTest extends TestCase {

    public void testAll() throws Exception {
        Mru mru = new Mru(3);
        mru.put("aaa", "111");
        mru.put("bbb", "222");
        mru.put("ccc", "333");
        assertEquals("111", mru.get("aaa"));
        Iterator i = mru.getKeyIterator();
        assertEquals("aaa", i.next());
        assertEquals("ccc", i.next());
        assertEquals("bbb", i.next());
        mru.put("ddd", "444");
        assertEquals(3, mru.getSize());
        assertNull(mru.get("bbb"));

    }
}