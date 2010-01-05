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

import java.lang.reflect.Array;
import java.util.List;

import junit.framework.TestCase;
import junitx.framework.ArrayAssert;

/**
 * @author higa
 * 
 */
public class ArrayUtilTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testAdd() throws Exception {
        String[] array = new String[] { "111" };
        String[] newArray = (String[]) ArrayUtil.add(array, "222");
        assertEquals("1", 2, newArray.length);
        assertEquals("2", "111", newArray[0]);
        assertEquals("3", "222", newArray[1]);
    }

    /**
     * @throws Exception
     */
    public void testAdd2() throws Exception {
        String[] a = new String[] { "1", "2" };
        String[] b = new String[] { "3" };
        a = (String[]) ArrayUtil.add(a, b);
        assertEquals("1", 3, a.length);
        assertEquals("2", "1", a[0]);
        assertEquals("3", "2", a[1]);
        assertEquals("4", "3", a[2]);
    }

    /**
     * @throws Exception
     */
    public void testAdd_int() throws Exception {
        int[] array = new int[] { 1 };
        int[] newArray = (int[]) ArrayUtil.add(array, 2);
        assertEquals(2, newArray.length);
        assertEquals(1, newArray[0]);
        assertEquals(2, newArray[1]);
    }

    /**
     * @throws Exception
     */
    public void testIndexOf() throws Exception {
        String[] array = new String[] { "111", "222", "333" };
        assertEquals("1", 1, ArrayUtil.indexOf(array, "222"));
        assertEquals("2", -1, ArrayUtil.indexOf(array, new Object()));
        assertEquals("3", -1, ArrayUtil.indexOf(array, null));
        array[1] = null;
        assertEquals("4", 1, ArrayUtil.indexOf(array, null));
    }

    /**
     * @throws Exception
     */
    public void testIndexOf_character() throws Exception {
        char[] array = new char[] { 'a', 'b', 'c' };
        assertEquals("1", 0, ArrayUtil.indexOf(array, 'a'));
        assertEquals("2", -1, ArrayUtil.indexOf(array, 'd'));
    }

    /**
     * @throws Exception
     */
    public void testRemoveFirst() throws Exception {
        String[] array = new String[] { "111", "222", "333" };
        String[] newArray = (String[]) ArrayUtil.remove(array, "111");
        assertEquals("1", 2, newArray.length);
        assertEquals("2", "222", newArray[0]);
        assertEquals("3", "333", newArray[1]);
    }

    /**
     * @throws Exception
     */
    public void testRemoveMiddle() throws Exception {
        String[] array = new String[] { "111", "222", "333" };
        String[] newArray = (String[]) ArrayUtil.remove(array, "222");
        assertEquals("1", 2, newArray.length);
        assertEquals("2", "111", newArray[0]);
        assertEquals("3", "333", newArray[1]);
    }

    /**
     * @throws Exception
     */
    public void testRemoveLast() throws Exception {
        String[] array = new String[] { "111", "222", "333" };
        String[] newArray = (String[]) ArrayUtil.remove(array, "333");
        assertEquals("1", 2, newArray.length);
        assertEquals("2", "111", newArray[0]);
        assertEquals("3", "222", newArray[1]);
    }

    /**
     * @throws Exception
     */
    public void testRemoveNothing() throws Exception {
        String[] array = new String[] { "111", "222", "333" };
        String[] newArray = (String[]) ArrayUtil.remove(array, "444");
        assertSame("1", array, newArray);
    }

    /**
     * 
     */
    public void testIsEmpty() {
        assertTrue(ArrayUtil.isEmpty(null));
        assertTrue(ArrayUtil.isEmpty(new Object[] {}));
        assertFalse(ArrayUtil.isEmpty(new Object[] { "" }));
        assertFalse(ArrayUtil.isEmpty(new Object[] { "aaa" }));
    }

    /**
     * @throws Exception
     */
    public void testContains() throws Exception {
        assertEquals(true, ArrayUtil.contains(new Object[] { "1" }, "1"));
        assertEquals(false, ArrayUtil.contains(new Object[] { "1" }, "2"));
        assertEquals(true, ArrayUtil.contains(new Object[] { "2", "1" }, "1"));
        assertEquals(false, ArrayUtil.contains((Object[]) null, "1"));
        assertEquals(false, ArrayUtil.contains((Object[]) null, null));
        assertEquals(true, ArrayUtil.contains(new Object[] { null }, null));
    }

    /**
     * @throws Exception
     */
    public void testContains_character() throws Exception {
        assertEquals(true, ArrayUtil.contains(new char[] { '1', '2' }, '1'));
        assertEquals(false, ArrayUtil.contains(new char[] { '1' }, '2'));
        assertEquals(true, ArrayUtil.contains(new char[] { '2', '1' }, '1'));
        assertEquals(false, ArrayUtil.contains((char[]) null, '1'));
    }

    /**
     * @throws Exception
     */
    public void testEqualsIgnoreSequence() throws Exception {
        assertEquals(true, ArrayUtil.equalsIgnoreSequence(new Object[] { "1" },
                new Object[] { "1" }));
        assertEquals(true, ArrayUtil.equalsIgnoreSequence(new Object[] { "1",
                "2", "3" }, new Object[] { "2", "3", "1" }));
        assertEquals(false, ArrayUtil.equalsIgnoreSequence(
                new Object[] { "1" }, new Object[] { "2" }));
        assertEquals(false, ArrayUtil.equalsIgnoreSequence(
                new Object[] { "1" }, new Object[] {}));
        assertEquals(false, ArrayUtil.equalsIgnoreSequence(
                new Object[] { new Integer("1") }, new Object[] { "1" }));

        assertEquals(true, ArrayUtil.equalsIgnoreSequence(null, null));
        assertEquals(false, ArrayUtil.equalsIgnoreSequence(null,
                new Object[] {}));
    }

    /**
     * @throws Exception
     */
    public void testSetArrayValue() throws Exception {
        Object o = Array.newInstance(int.class, 3);
        ArrayUtil.setArrayValue(o, int.class, new Integer(1), 0);
        ArrayUtil.setArrayValue(o, int.class, new Integer(2), 1);
        ArrayUtil.setArrayValue(o, int.class, new Integer(3), 2);
        int[] num = (int[]) o;
        ArrayAssert.assertEquals(new int[] { 1, 2, 3 }, num);
    }

    /**
     * @throws Exception
     */
    public void testToObjectArray() throws Exception {
        final Object[] a = ArrayUtil.toObjectArray(new int[] { 1, 5, 2 });
        ArrayAssert.assertEquals(new Integer[] { new Integer(1),
                new Integer(5), new Integer(2) }, a);
    }

    /**
     * @throws Exception
     */
    public void testToObjectArray_NoArray() throws Exception {
        try {
            ArrayUtil.toObjectArray("a");
            fail();
        } catch (IllegalArgumentException e) {
        }
    }

    /**
     * @throws Exception
     */
    public void testToList() throws Exception {
        final Object a = new int[] { 1, 5 };
        List list = ArrayUtil.toList(a);
        assertEquals(new Integer(1), list.get(0));
        assertEquals(new Integer(5), list.get(1));
    }
}