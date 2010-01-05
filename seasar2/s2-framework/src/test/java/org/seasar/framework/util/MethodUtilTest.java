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

import java.lang.reflect.Method;
import java.util.List;

import junit.framework.TestCase;

/**
 * @author higa
 * 
 */
public class MethodUtilTest extends TestCase {

    /**
     * 
     */
    public void testIsEqualsMethod() {
        Method equalsMethod = ClassUtil.getMethod(getClass(), "equals",
                new Class[] { Object.class });
        assertTrue("1", MethodUtil.isEqualsMethod(equalsMethod));
        Method hashCodeMethod = ClassUtil.getMethod(getClass(), "hashCode",
                new Class[0]);
        assertFalse("2", MethodUtil.isEqualsMethod(hashCodeMethod));
    }

    /**
     * 
     */
    public void testIsHashCodeMethod() {
        Method equalsMethod = ClassUtil.getMethod(getClass(), "equals",
                new Class[] { Object.class });
        assertFalse("1", MethodUtil.isHashCodeMethod(equalsMethod));
        Method hashCodeMethod = ClassUtil.getMethod(getClass(), "hashCode",
                new Class[0]);
        assertTrue("2", MethodUtil.isHashCodeMethod(hashCodeMethod));
    }

    /**
     * 
     */
    public void testIsToStringMethod() {
        Method toStringMethod = ClassUtil.getMethod(getClass(), "toString",
                new Class[0]);
        assertTrue("1", MethodUtil.isToStringMethod(toStringMethod));
        Method hashCodeMethod = ClassUtil.getMethod(getClass(), "hashCode",
                new Class[0]);
        assertFalse("2", MethodUtil.isToStringMethod(hashCodeMethod));
    }

    /**
     * @throws Exception
     */
    public void testIsBridgeMethod() throws Exception {
        Method method = Foo.class.getMethod("foo", null);
        assertFalse(MethodUtil.isBridgeMethod(method));
    }

    /**
     * @throws Exception
     */
    public void testIsSyntheticMethod() throws Exception {
        Method method = Foo.class.getMethod("foo", null);
        assertFalse(MethodUtil.isSyntheticMethod(method));
    }

    /**
     * @throws Exception
     */
    public void testGetElementTypeOfListFromParameterType() throws Exception {
        assertNull(MethodUtil.getElementTypeOfListFromParameterType(Baz.class
                .getMethod("hoge", new Class[] { List.class }), 0));
        assertNull(MethodUtil.getElementTypeOfListFromParameterType(Baz.class
                .getMethod("hoge", new Class[] { List.class, List.class }), 0));
        assertNull(MethodUtil.getElementTypeOfListFromParameterType(Baz.class
                .getMethod("hoge", new Class[] { List.class, List.class }), 1));
    }

    /**
     * @throws Exception
     */
    public void testGetElementTypeOfListFromReturnType() throws Exception {
        assertNull(MethodUtil.getElementTypeOfListFromReturnType(Baz.class
                .getMethod("hoge", new Class[] { List.class })));
    }

    /**
     *
     */
    public static class Foo {
        /**
         * 
         */
        public void foo() {
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
        List hoge(List src);

        /**
         * @param src
         * @param dest
         */
        void hoge(List src, List dest);
    }

}
