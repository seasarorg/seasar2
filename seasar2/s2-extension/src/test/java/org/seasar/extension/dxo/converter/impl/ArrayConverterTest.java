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
package org.seasar.extension.dxo.converter.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Satoshi Kimura
 * @author koichik
 */
public class ArrayConverterTest extends AbsConverterTest {

    private ArrayConverter converter;

    protected void setUp() throws Exception {
        super.setUp();
        converter = new ArrayConverter();
    }

    public void testConvert() throws Exception {
        List list = new ArrayList();
        list.add("1");
        list.add("2");

        Object[] array1 = (Object[]) converter.convert(list, Object[].class,
                createContext("testConvert", null));
        assertEquals(list, Arrays.asList(array1));

        String[] array2 = (String[]) converter.convert(list, String[].class,
                createContext("testConvert", null));
        assertEquals(list, Arrays.asList(array2));

        Integer[] array3 = (Integer[]) converter.convert(list, Integer[].class,
                createContext("testConvert", null));
        List intList = new ArrayList();
        intList.add(new Integer(1));
        intList.add(new Integer(2));
        assertEquals(intList, Arrays.asList(array3));
    }

}
