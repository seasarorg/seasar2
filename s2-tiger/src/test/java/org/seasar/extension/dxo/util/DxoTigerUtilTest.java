/*
 * Copyright 2004-2013 the Seasar Foundation and the Others.
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

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

/**
 * @author koichik
 * 
 */
public class DxoTigerUtilTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testGetElementTypeOfListFromDestination() throws Exception {
        assertEquals(Map.class, DxoUtil
                .getElementTypeOfList(GenericListOfMapDxo.class.getMethod(
                        "convert", new Class[] { List.class })));
        assertEquals(Map.class, DxoUtil
                .getElementTypeOfList(GenericListOfMapDxo.class.getMethod(
                        "convert", new Class[] { List.class, List.class })));
    }

    /**
     * @throws Exception
     */
    public void testRawMap() throws Exception {
        Method m1 = RawMapDxo.class.getMethod("convert", Object.class);
        assertEquals(Object.class, DxoTigerUtil.getValueTypeOfTargetMap(m1));

        Method m2 = RawMapDxo.class.getMethod("convert", Object.class,
                Map.class);
        assertEquals(Object.class, DxoTigerUtil.getValueTypeOfTargetMap(m2));
    }

    /**
     * @throws Exception
     */
    public void testGenericMap() throws Exception {
        Method m1 = GenericMapDxo.class.getMethod("convert", Object.class);
        assertEquals(String.class, DxoTigerUtil.getValueTypeOfTargetMap(m1));

        Method m2 = GenericMapDxo.class.getMethod("convert", Object.class,
                Map.class);
        assertEquals(String.class, DxoTigerUtil.getValueTypeOfTargetMap(m2));
    }

    /**
     * @throws Exception
     */
    public void testGenericListOfMap() throws Exception {
        Method m1 = GenericListOfMapDxo.class.getMethod("convert", List.class);
        assertEquals(String.class, DxoTigerUtil.getValueTypeOfTargetMap(m1));

        Method m2 = GenericListOfMapDxo.class.getMethod("convert", List.class,
                List.class);
        assertEquals(String.class, DxoTigerUtil.getValueTypeOfTargetMap(m2));
    }

    /**
     * @throws Exception
     */
    public void testGenericArrayOfMap() throws Exception {
        Method m1 = GenericArrayOfMapDxo.class.getMethod("convert",
                Object[].class);
        assertEquals(String.class, DxoTigerUtil.getValueTypeOfTargetMap(m1));

        Method m2 = GenericArrayOfMapDxo.class.getMethod("convert",
                Object[].class, Map[].class);
        assertEquals(String.class, DxoTigerUtil.getValueTypeOfTargetMap(m2));
    }

    /**
     *
     */
    @SuppressWarnings("unchecked")
    public interface RawMapDxo {
        /**
         * @param src
         * @return
         */
        Map convert(Object src);

        /**
         * @param src
         * @param dest
         */
        void convert(Object src, Map dest);
    }

    /**
     *
     */
    public interface GenericMapDxo {
        /**
         * @param src
         * @return
         */
        Map<String, String> convert(Object src);

        /**
         * @param src
         * @param dest
         */
        void convert(Object src, Map<String, String> dest);
    }

    /**
     *
     */
    public interface GenericListOfMapDxo {
        /**
         * @param src
         * @return
         */
        List<Map<String, String>> convert(List<Object> src);

        /**
         * @param src
         * @param dest
         */
        void convert(List<Object> src, List<Map<String, String>> dest);
    }

    /**
     *
     */
    public interface GenericArrayOfMapDxo {
        /**
         * @param src
         * @return
         */
        Map<String, String>[] convert(Object[] src);

        /**
         * @param src
         * @param dest
         */
        void convert(Object[] src, Map<String, String>[] dest);
    }

}
