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
package org.seasar.framework.util;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.List;

import junit.framework.TestCase;

/**
 * @author koichik
 */
public class MethodUtilTigerTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testIsBridgeMethod() throws Exception {
        Method[] methods = Bar.class.getDeclaredMethods();
        assertEquals(2, methods.length);
        int bridge = 0;
        for (Method method : methods) {
            if (MethodUtil.isBridgeMethod(method)) {
                ++bridge;
            }
        }
        assertEquals(1, bridge);
    }

    /**
     * @throws Exception
     */
    public void testGetElementTypeOfListFromParameterType() throws Exception {
        assertEquals(Integer.class, MethodUtil
                .getElementTypeOfListFromParameterType(Baz.class.getMethod(
                        "hoge", new Class[] { List.class }), 0));
        assertEquals(Double.class, MethodUtil
                .getElementTypeOfListFromParameterType(Baz.class.getMethod(
                        "hoge", new Class[] { List.class, List.class }), 0));
        assertEquals(BigDecimal.class, MethodUtil
                .getElementTypeOfListFromParameterType(Baz.class.getMethod(
                        "hoge", new Class[] { List.class, List.class }), 1));
    }

    /**
     * @throws Exception
     */
    public void testGetElementTypeOfListFromReturnType() throws Exception {
        assertEquals(String.class, MethodUtil
                .getElementTypeOfListFromReturnType(Baz.class.getMethod("hoge",
                        new Class[] { List.class })));
    }

    /**
     * 
     */
    public class Foo {

        /**
         * @return
         */
        public Foo foo() {
            return null;
        }
    }

    /**
     * 
     */
    public class Bar extends Foo {

        @Override
        public Bar foo() {
            return null;
        }
    }

    /**
     * 
     */
    public interface Baz {

        /**
         * @param src
         * @return
         */
        List<String> hoge(List<Integer> src);

        /**
         * @param src
         * @param dest
         */
        void hoge(List<Double> src, List<BigDecimal> dest);
    }

}
