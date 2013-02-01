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
package org.seasar.framework.unit.impl;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

import org.seasar.framework.container.annotation.tiger.Binding;

/**
 * @author taedium
 * 
 */
public class IntrospectorUtilTest extends TestCase {

    /**
     * 
     * @throws Exception
     */
    public void testGetSuperClasses() throws Exception {
        List<Class<?>> list = IntrospectorUtil.getSuperClasses(Ccc.class);
        assertEquals(3, list.size());
        assertEquals(Ccc.class, list.get(0));
        assertEquals(Bbb.class, list.get(1));
        assertEquals(Aaa.class, list.get(2));
    }

    /**
     * 
     * @throws Exception
     */
    public void testIsShadowed() throws Exception {
        Method m1 = Bbb.class.getMethod("foo", null);
        Method m2 = Bbb.class.getMethod("hoge", null);
        Method m3 = Aaa.class.getMethod("hoge", null);
        assertFalse(IntrospectorUtil.isShadowed(m2, Arrays.asList(m1)));
        assertTrue(IntrospectorUtil.isShadowed(m3, Arrays.asList(m1, m2)));
    }

    /**
     * 
     * @throws Exception
     */
    public void testGetAnnotatedMethods() throws Exception {
        List<Method> list = IntrospectorUtil.getAnnotatedMethods(Ccc.class,
                Binding.class);
        assertEquals(2, list.size());
        assertEquals(Ccc.class.getMethod("bar", null), list.get(0));
        assertEquals(Bbb.class.getMethod("hoge", null), list.get(1));
    }

    /**
     * 
     */
    public static class Aaa {

        /**
         * 
         */
        @Binding
        public void hoge() {
        }
    }

    /**
     * 
     */
    public static class Bbb extends Aaa {

        /**
         * 
         */
        public void foo() {
        }

        @Binding
        @Override
        public void hoge() {
        }
    }

    /**
     * 
     */
    public static class Ccc extends Bbb {

        /**
         * 
         */
        @Binding
        public void bar() {
        }
    }

}
