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
package org.seasar.framework.tool;

import junit.framework.TestCase;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;

/**
 * @author koichik
 */
public class ParameterNamesEnhancerTest extends TestCase {
    protected void setUp() throws Exception {
        super.setUp();
        String name = getClass().getName();

        ParameterNameEnhancer enhancer = new ParameterNameEnhancer(name
                + "$Foo");
        enhancer.setMethodParameterNames("hoge", new String[] { "boolean",
                "byte[]", "java.lang.String" }, new String[] { "flag", "bytes",
                "str" });
        enhancer.save();

        enhancer = new ParameterNameEnhancer(name + "$Bar");
        enhancer.setConstructorParameterNames(new String[] { name + "$Foo",
                name + "$Bar", name + "$Baz" }, new String[] { "foo", "bar",
                "baz" });
        enhancer.setMethodParameterNames("array", new String[] { "int[]",
                "int[][]", "int[][][]" }, new String[] { "a1", "a2", "a3" });
        enhancer.save();

        enhancer = new ParameterNameEnhancer(name + "$Baz");
        enhancer.setConstructorParameterNames(new String[] { name + "$Foo",
                name + "$Bar", name + "$Baz" }, new String[] { "$foo", "$bar",
                "$baz" });
        enhancer.setMethodParameterNames("array", new String[] { "int[]",
                "int[][]", "int[][][]" }, new String[] { "array1", "array2",
                "array3" });
        enhancer.save();
    }

    public void testInterface() throws Exception {
        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(Foo.class);
        String[] names = beanDesc.getMethodParameterNames("hoge", new Class[] {
                boolean.class, byte[].class, String.class });
        assertNotNull(names);
        assertEquals(3, names.length);
        assertEquals("flag", names[0]);
        assertEquals("bytes", names[1]);
        assertEquals("str", names[2]);
    }

    public void testAbstractClass() throws Exception {
        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(Bar.class);
        String[] names = beanDesc.getConstructorParameterNames(new Class[] {
                Foo.class, Bar.class, Baz.class });
        assertEquals(3, names.length);
        assertEquals("foo", names[0]);
        assertEquals("bar", names[1]);
        assertEquals("baz", names[2]);

        names = beanDesc.getMethodParameterNames("array", new Class[] {
                int[].class, int[][].class, int[][][].class });
        assertNotNull(names);
        assertEquals(3, names.length);
        assertEquals("a1", names[0]);
        assertEquals("a2", names[1]);
        assertEquals("a3", names[2]);
    }

    public void testConcreteClass() throws Exception {
        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(Baz.class);
        String[] names = beanDesc.getConstructorParameterNames(new Class[] {
                Foo.class, Bar.class, Baz.class });
        assertEquals(3, names.length);
        assertEquals("$foo", names[0]);
        assertEquals("$bar", names[1]);
        assertEquals("$baz", names[2]);

        names = beanDesc.getMethodParameterNames("array", new Class[] {
                int[].class, int[][].class, int[][][].class });
        assertNotNull(names);
        assertEquals(3, names.length);
        assertEquals("array1", names[0]);
        assertEquals("array2", names[1]);
        assertEquals("array3", names[2]);
    }

    public interface Foo {
        void hoge(boolean flag, byte[] bytes, String str);
    }

    public static abstract class Bar {
        public Bar() {
        }

        public Bar(Foo foo, Bar bar, Baz baz) {
        }

        public abstract void array(int[] a1, int[][] a2, int[][][] a3);
    }

    public static class Baz extends Bar {

        public Baz() {
        }

        public Baz(Foo $foo, Bar $bar, Baz $baz) {
            super($foo, $bar, $baz);
        }

        public void array(int[] array1, int[][] array2, int[][][] array3) {
        }
    }
}
