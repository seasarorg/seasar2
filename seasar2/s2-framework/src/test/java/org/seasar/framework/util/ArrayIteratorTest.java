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

import java.util.NoSuchElementException;

import junit.framework.TestCase;

/**
 * @author shot
 * @author manhole
 */
public class ArrayIteratorTest extends TestCase {

    /**
     * 
     */
    public void testNext() {
        ArrayIterator itr = new ArrayIterator(new Object[] { "a", "b", "c" });
        assertEquals("a", itr.next());
        assertEquals("b", itr.next());
        assertEquals("c", itr.next());
        try {
            itr.next();
            fail();
        } catch (NoSuchElementException expected) {
            ExceptionAssert.assertMessageExist(expected);
        }
    }

    /**
     * 
     */
    public void testHasNext() {
        ArrayIterator itr = new ArrayIterator(new Object[] { "A", "B" });
        assertEquals(true, itr.hasNext());
        itr.next();
        assertEquals(true, itr.hasNext());
        itr.next();
        assertEquals(false, itr.hasNext());
    }

    /**
     * @throws Exception
     */
    public void testRemove() throws Exception {
        ArrayIterator itr = new ArrayIterator(new String[] { "1", "2" });
        try {
            itr.remove();
            fail();
        } catch (UnsupportedOperationException expected) {
            ExceptionAssert.assertMessageExist(expected);
        }
    }

}
