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

import junit.framework.TestCase;

public class ArrayUtilTest extends TestCase {

    public void testAdd() throws Exception {
        String[] array = new String[] { "111" };
        String[] newArray = (String[]) ArrayUtil.add(array, "222");
        assertEquals("1", 2, newArray.length);
        assertEquals("2", "111", newArray[0]);
        assertEquals("3", "222", newArray[1]);
    }

    public void testAdd2() throws Exception {
        String[] a = new String[] { "1", "2" };
        String[] b = new String[] { "3" };
        a = (String[]) ArrayUtil.add(a, b);
        assertEquals("1", 3, a.length);
        assertEquals("2", "1", a[0]);
        assertEquals("3", "2", a[1]);
        assertEquals("4", "3", a[2]);
    }

    public void testIndexOf() throws Exception {
        String[] array = new String[] { "111", "222", "333" };
        assertEquals("1", 1, ArrayUtil.indexOf(array, "222"));
        assertEquals("2", -1, ArrayUtil.indexOf(array, new Object()));
        assertEquals("3", -1, ArrayUtil.indexOf(array, null));
        array[1] = null;
        assertEquals("4", 1, ArrayUtil.indexOf(array, null));
    }

    public void testRemoveFirst() throws Exception {
        String[] array = new String[] { "111", "222", "333" };
        String[] newArray = (String[]) ArrayUtil.remove(array, "111");
        assertEquals("1", 2, newArray.length);
        assertEquals("2", "222", newArray[0]);
        assertEquals("3", "333", newArray[1]);
    }

    public void testRemoveMiddle() throws Exception {
        String[] array = new String[] { "111", "222", "333" };
        String[] newArray = (String[]) ArrayUtil.remove(array, "222");
        assertEquals("1", 2, newArray.length);
        assertEquals("2", "111", newArray[0]);
        assertEquals("3", "333", newArray[1]);
    }

    public void testRemoveLast() throws Exception {
        String[] array = new String[] { "111", "222", "333" };
        String[] newArray = (String[]) ArrayUtil.remove(array, "333");
        assertEquals("1", 2, newArray.length);
        assertEquals("2", "111", newArray[0]);
        assertEquals("3", "222", newArray[1]);
    }

    public void testRemoveNothing() throws Exception {
        String[] array = new String[] { "111", "222", "333" };
        String[] newArray = (String[]) ArrayUtil.remove(array, "444");
        assertSame("1", array, newArray);
    }

    public void testIsEmpty() {
        assertTrue(ArrayUtil.isEmpty(null));
        assertTrue(ArrayUtil.isEmpty(new Object[] {}));
        assertFalse(ArrayUtil.isEmpty(new Object[] { "" }));
        assertFalse(ArrayUtil.isEmpty(new Object[] { "aaa" }));
    }

    public void testContains() throws Exception {
        assertEquals(true, ArrayUtil.contains(new Object[] { "1" }, "1"));
        assertEquals(false, ArrayUtil.contains(new Object[] { "1" }, "2"));
        assertEquals(true, ArrayUtil.contains(new Object[] { "2", "1" }, "1"));
        assertEquals(false, ArrayUtil.contains((Object[]) null, "1"));
        assertEquals(false, ArrayUtil.contains((Object[]) null, null));
        assertEquals(true, ArrayUtil.contains(new Object[] { null }, null));
    }

}