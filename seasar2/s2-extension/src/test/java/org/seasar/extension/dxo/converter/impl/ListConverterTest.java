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
import java.util.List;

/**
 * @author Satoshi Kimura
 * @author koichik
 */
public class ListConverterTest extends AbsConverterTest {

    private ListConverter converter;

    protected void setUp() throws Exception {
        super.setUp();
        converter = new ListConverter();
    }

    /**
     * @throws Exception
     */
    public void testConvert() throws Exception {
        Object[] array1 = { new Integer(1), new Integer(5) };
        List list = (List) converter.convert(array1, null, createContext(
                "testConvert", null));
        assertEquals(Arrays.asList(array1), list);

        String[] array2 = { "a", "b" };
        list = (List) converter.convert(array2, null, createContext(
                "testConvert", null));
        assertEquals(Arrays.asList(array2), list);
    }

}
