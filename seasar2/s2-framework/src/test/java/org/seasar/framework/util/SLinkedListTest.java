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
 * @author higa
 *
 */
public class SLinkedListTest extends TestCase {

    private SLinkedList list_;

    /**
     * @throws Exception
     */
    public void testGetFirstEntry() throws Exception {
        assertNull("1", list_.getFirstEntry());
        list_.addFirst("1");
        assertEquals("2", "1", list_.getFirstEntry().getElement());
    }

    /**
     * @throws Exception
     */
    public void testGetFirst() throws Exception {
        try {
            list_.getFirst();
            fail("1");
        } catch (NoSuchElementException ex) {
            System.out.println(ex);
        }
        list_.addFirst("1");
        assertEquals("2", "1", list_.getFirst());
    }

    /**
     * @throws Exception
     */
    public void testGetLastEntry() throws Exception {
        assertNull("1", list_.getLastEntry());
        list_.addLast("1");
        assertEquals("2", "1", list_.getLastEntry().getElement());
    }

    /**
     * @throws Exception
     */
    public void testGetLast() throws Exception {
        try {
            list_.getLast();
            fail("1");
        } catch (NoSuchElementException ex) {
            System.out.println(ex);
        }
        list_.addLast("1");
        assertEquals("2", "1", list_.getLast());
    }

    /**
     * @throws Exception
     */
    public void testRemoveFirst() throws Exception {
        list_.addLast("1");
        list_.addLast("2");
        list_.removeFirst();
        assertEquals("1", "2", list_.getFirst());
    }

    /**
     * @throws Exception
     */
    public void testRemoveLast() throws Exception {
        list_.addLast("1");
        list_.addLast("2");
        list_.removeLast();
        assertEquals("1", "1", list_.getLast());
    }

    /**
     * @throws Exception
     */
    public void testAddFirst() throws Exception {
        list_.addFirst("1");
        list_.addFirst("2");
        assertEquals("1", "2", list_.getFirst());
    }

    /**
     * @throws Exception
     */
    public void testAddLast() throws Exception {
        list_.addLast("1");
        list_.addLast("2");
        assertEquals("1", "2", list_.getLast());
    }

    /**
     * @throws Exception
     */
    public void testSize() throws Exception {
        assertEquals("1", 0, list_.size());
        list_.addLast("1");
        assertEquals("2", 1, list_.size());
        list_.removeFirst();
        assertEquals("3", 0, list_.size());
    }

    /**
     * @throws Exception
     */
    public void testIsEmpty() throws Exception {
        assertTrue("1", list_.isEmpty());
        list_.addLast("1");
        assertTrue("2", !list_.isEmpty());
    }

    /**
     * @throws Exception
     */
    public void testContaines() throws Exception {
        assertTrue("1", !list_.contains(null));
        assertTrue("2", !list_.contains("1"));
        list_.addLast("1");
        assertTrue("3", list_.contains("1"));
        assertTrue("4", !list_.contains("2"));
        assertTrue("5", !list_.contains(null));
    }

    /**
     * @throws Exception
     */
    public void testRemove() throws Exception {
        list_.addLast(null);
        list_.addLast("1");
        list_.addLast("2");
        assertTrue("1", !list_.remove("3"));
        assertTrue("2", list_.remove("1"));
        assertTrue("3", list_.remove(null));
        list_.clear();
        list_.addLast("1");
        list_.addLast("2");
        list_.addLast("3");
        list_.remove(1);
        assertEquals("1", "1", list_.get(0));
        assertEquals("2", "3", list_.get(1));

        list_.clear();
        list_.addLast("1");
        list_.addLast("2");
        list_.addLast("3");
        SLinkedList.Entry e = list_.getEntry(1);
        e.remove();
        assertEquals("3", "3", e.getNext().getElement());
    }

    /**
     * @throws Exception
     */
    public void testClear() throws Exception {
        list_.addLast(null);
        list_.addLast("1");
        list_.addLast("2");
        list_.clear();
        assertEquals("1", 0, list_.size());
        assertNull("2", list_.getFirstEntry());
        assertNull("3", list_.getLastEntry());
    }

    /**
     * @throws Exception
     */
    public void testGetEntry() throws Exception {
        list_.addLast("1");
        list_.addLast("2");
        list_.addLast("3");
        assertEquals("1", "1", list_.getEntry(0).getElement());
        assertEquals("2", "2", list_.getEntry(1).getElement());
        assertEquals("3", "3", list_.getEntry(2).getElement());
        try {
            list_.getEntry(-1);
            fail("4");
        } catch (IndexOutOfBoundsException ex) {
            System.out.println(ex);
        }
        try {
            list_.getEntry(3);
            fail("5");
        } catch (IndexOutOfBoundsException ex) {
            System.out.println(ex);
        }
    }

    /**
     * @throws Exception
     */
    public void testGet() throws Exception {
        list_.addLast("1");
        list_.addLast("2");
        list_.addLast("3");
        assertEquals("1", "1", list_.get(0));
        assertEquals("2", "2", list_.get(1));
        assertEquals("3", "3", list_.get(2));
    }

    /**
     * @throws Exception
     */
    public void testSerialize() throws Exception {
        list_.addLast("1");
        list_.addLast("2");
        list_.addLast("3");
        assertNotNull("1", SerializeUtil.serialize(list_));
    }

    /**
     * @throws Exception
     */
    public void testSet() throws Exception {
        list_.addLast("1");
        list_.addLast("2");
        list_.addLast("3");
        list_.set(1, "4");
        assertEquals("1", "4", list_.get(1));
    }

    /**
     * @throws Exception
     */
    public void testEntry() throws Exception {
        list_.addLast("1");
        list_.addLast("2");
        list_.addLast("3");
        SLinkedList.Entry e = list_.getFirstEntry();
        assertNull("1", e.getPrevious());
        assertEquals("2", "2", e.getNext().getElement());
        e = list_.getLastEntry();
        assertNull("3", e.getNext());
        assertEquals("4", "2", e.getPrevious().getElement());
        list_.getEntry(1).remove();
        assertEquals("5", "1", list_.getFirst());
        assertEquals("6", "3", list_.getLast());
        list_.getLastEntry().remove();
        assertEquals("7", "1", list_.getLast());
        list_.getLastEntry().remove();
        assertEquals("8", 0, list_.size());
    }

    /**
     * @throws Exception
     */
    public void testAdd() throws Exception {
        list_.addLast("1");
        list_.addLast("2");
        list_.addLast("3");
        list_.add(1, "4");
        assertEquals("1", "4", list_.get(1));
        assertEquals("2", "2", list_.get(2));
    }

    /**
     * @throws Exception
     */
    public void testIndexOf() throws Exception {
        list_.addLast(null);
        list_.addLast("1");
        list_.addLast("2");
        list_.addLast("3");
        assertEquals("1", 0, list_.indexOf(null));
        assertEquals("2", 1, list_.indexOf("1"));
        assertEquals("3", 2, list_.indexOf("2"));
        assertEquals("4", 3, list_.indexOf("3"));
        assertEquals("5", -1, list_.indexOf("4"));
    }

    protected void setUp() throws Exception {
        list_ = new SLinkedList();
    }

    protected void tearDown() throws Exception {
        list_.clear();
        list_ = null;
    }
}