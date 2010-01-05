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

import java.util.Enumeration;
import java.util.Vector;

import junit.framework.TestCase;

/**
 * @author shot
 * @author manhole
 */
public class EnumerationIteratorTest extends TestCase {

    /**
     * 
     */
    public void testEnumerationIterator() {
        Vector vector = new Vector();
        vector.add("a");
        EnumerationIterator itr = new EnumerationIterator(vector.elements());
        assertTrue(itr.hasNext());
        assertEquals("a", itr.next());
        assertEquals(false, itr.hasNext());
        try {
            itr.remove();
            fail();
        } catch (UnsupportedOperationException expected) {
            ExceptionAssert.assertMessageExist(expected);
        }
    }

    /**
     * @throws Exception
     */
    public void testNext() throws Exception {
        EnumerationIterator itr = new EnumerationIterator(new Vector()
                .elements());
        assertEquals(false, itr.hasNext());
    }

    /**
     * @throws Exception
     */
    public void testConstructorWithNull() throws Exception {
        try {
            new EnumerationIterator((Enumeration) null);
        } catch (NullPointerException expected) {
            ExceptionAssert.assertMessageExist(expected);
        }
    }

}
