/*
 * Copyright 2004-2012 the Seasar Foundation and the Others.
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
import java.util.List;

import org.seasar.framework.beans.ParameterizedClassDesc;
import org.seasar.framework.beans.impl.ParameterizedClassDescImpl;

/**
 * @author Satoshi Kimura
 * @author koichik
 */
public class ListConverterTest extends AbsConverterTest {

    private AbstractParameterizedCollectionConverter converter;

    protected void setUp() throws Exception {
        super.setUp();
        converter = new ListConverter();
    }

    /**
     * @throws Exception
     */
    public void testConvertFromArray() throws Exception {
        Object[] array1 = { new Integer(1), new Integer(5) };
        List dest = (List) converter.convert(array1, null, createContext(
                "testConvertFromArray", null));
        assertEquals(2, dest.size());
        assertEquals(new Integer(1), dest.get(0));
        assertEquals(new Integer(5), dest.get(1));

        String[] array2 = { "a", "b" };
        dest = (List) converter.convert(array2, null, createContext(
                "testConvertFromArray", null));
        assertEquals(2, dest.size());
        assertEquals("a", dest.get(0));
        assertEquals("b", dest.get(1));

        int[] array3 = { 1, 5 };
        dest = (List) converter.convert(array3, null, createContext(
                "testConvertFromArray", null));
        assertEquals(2, dest.size());
        assertEquals(new Integer(1), dest.get(0));
        assertEquals(new Integer(5), dest.get(1));
    }

    /**
     * @throws Exception
     */
    public void testConvertFromList() throws Exception {
        List source = Arrays.asList(new Object[] { new Integer(1),
                new Integer(5) });
        List dest = (List) converter.convert(source, null, createContext(
                "testConvertFromList", null));
        assertEquals(2, dest.size());
        assertEquals(new Integer(1), dest.get(0));
        assertEquals(new Integer(5), dest.get(1));

        source = Arrays.asList(new String[] { "a", "b" });
        dest = (List) converter.convert(source, null, createContext(
                "testConvertFromList", null));
        assertEquals(2, dest.size());
        assertEquals("a", dest.get(0));
        assertEquals("b", dest.get(1));
    }

    /**
     * @throws Exception
     */
    public void testConvertFromArrayWithElementClass() throws Exception {
        Object[] array1 = { new Integer(1), new Integer(5), null };
        List dest = (List) converter
                .convert(
                        array1,
                        null,
                        new ParameterizedClassDescImpl(
                                List.class,
                                new ParameterizedClassDesc[] { new ParameterizedClassDescImpl(
                                        String.class) }), createContext(
                                "testConvertFromArrayWithElementClass", null));
        assertEquals(3, dest.size());
        assertEquals("1", dest.get(0));
        assertEquals("5", dest.get(1));
        assertNull(dest.get(2));
    }

    /**
     * @throws Exception
     */
    public void testConvertFromListWithElementClass() throws Exception {
        List source = Arrays.asList(new Object[] { new Integer(1),
                new Integer(5), null });
        List dest = (List) converter
                .convert(
                        source,
                        null,
                        new ParameterizedClassDescImpl(
                                List.class,
                                new ParameterizedClassDesc[] { new ParameterizedClassDescImpl(
                                        String.class) }), createContext(
                                "testConvertFromListWithElementClass", null));
        assertEquals(3, dest.size());
        assertEquals("1", dest.get(0));
        assertEquals("5", dest.get(1));
        assertNull(dest.get(2));
    }

}
