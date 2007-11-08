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
package org.seasar.extension.dxo.converter.impl;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.seasar.framework.beans.ParameterizedClassDesc;
import org.seasar.framework.beans.impl.ParameterizedClassDescImpl;

/**
 * @author Satoshi Kimura
 * @author koichik
 */
public class SetConverterTest extends AbsConverterTest {

    private AbstractParameterizedCollectionConverter converter;

    protected void setUp() throws Exception {
        super.setUp();
        converter = new SetConverter();
    }

    /**
     * @throws Exception
     */
    public void testConvertFromArray() throws Exception {
        Object[] array1 = { new Integer(1), new Integer(5) };
        Set dest = (Set) converter.convert(array1, null, createContext(
                "testConvertFromArray", null));
        assertEquals(2, dest.size());
        Iterator it = dest.iterator();
        assertEquals(new Integer(1), it.next());
        assertEquals(new Integer(5), it.next());

        String[] array2 = { "a", "b" };
        dest = (Set) converter.convert(array2, null, createContext(
                "testConvertFromArray", null));
        assertEquals(2, dest.size());
        it = dest.iterator();
        assertEquals("a", it.next());
        assertEquals("b", it.next());

        int[] array3 = { 1, 5 };
        dest = (Set) converter.convert(array3, null, createContext(
                "testConvertFromArray", null));
        assertEquals(2, dest.size());
        it = dest.iterator();
        assertEquals(new Integer(1), it.next());
        assertEquals(new Integer(5), it.next());
    }

    /**
     * @throws Exception
     */
    public void testConvertFromList() throws Exception {
        List source = Arrays.asList(new Object[] { new Integer(1),
                new Integer(5) });
        Set dest = (Set) converter.convert(source, null, createContext(
                "testConvertFromList", null));
        assertEquals(2, dest.size());
        Iterator it = dest.iterator();
        assertEquals(new Integer(1), it.next());
        assertEquals(new Integer(5), it.next());

        source = Arrays.asList(new String[] { "a", "b" });
        dest = (Set) converter.convert(source, null, createContext(
                "testConvertFromList", null));
        assertEquals(2, dest.size());
        it = dest.iterator();
        assertEquals("a", it.next());
        assertEquals("b", it.next());
    }

    /**
     * @throws Exception
     */
    public void testConvertFromArrayWithElementClass() throws Exception {
        Object[] array1 = { new Integer(1), new Integer(5), null };
        Set dest = (Set) converter
                .convert(
                        array1,
                        null,
                        new ParameterizedClassDescImpl(
                                Set.class,
                                new ParameterizedClassDesc[] { new ParameterizedClassDescImpl(
                                        String.class) }), createContext(
                                "testConvertFromArrayWithElementClass", null));
        assertEquals(3, dest.size());
        Iterator it = dest.iterator();
        assertEquals("1", it.next());
        assertEquals("5", it.next());
        assertNull(it.next());
    }

    /**
     * @throws Exception
     */
    public void testConvertFromListWithElementClass() throws Exception {
        List source = Arrays.asList(new Object[] { new Integer(1),
                new Integer(5), null });
        Set dest = (Set) converter
                .convert(
                        source,
                        null,
                        new ParameterizedClassDescImpl(
                                Set.class,
                                new ParameterizedClassDesc[] { new ParameterizedClassDescImpl(
                                        String.class) }), createContext(
                                "testConvertFromListWithElementClass", null));
        assertEquals(3, dest.size());
        Iterator it = dest.iterator();
        assertEquals("1", it.next());
        assertEquals("5", it.next());
        assertNull(it.next());
    }

}
