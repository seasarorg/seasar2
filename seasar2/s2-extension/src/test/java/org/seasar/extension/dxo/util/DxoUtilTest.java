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
package org.seasar.extension.dxo.util;

import java.util.List;

import junit.framework.TestCase;

/**
 * @author higa
 * 
 */
public class DxoUtilTest extends TestCase {

    public void testGetElementTypeOfListFromDestination() throws Exception {
        assertNull(DxoUtil.getElementTypeOfList(Dxo.class.getMethod("hoge",
                new Class[] { List.class })));
        assertNull(DxoUtil.getElementTypeOfList(Dxo.class.getMethod("hoge",
                new Class[] { List.class, List.class })));
    }

    public void testAddQuote() throws Exception {
        assertEquals("'aaa':bbb,'ccc':ddd", DxoUtil
                .addQuote("aaa : bbb , ccc : ddd"));
    }

    public interface Dxo {
        List hoge(List src);

        void hoge(List src, List dest);
    }

}