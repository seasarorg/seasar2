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
package org.seasar.framework.util.tiger;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

/**
 * @author koichik
 */
public class ReflectionUtilTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testForName() throws Exception {
        Class<Foo> clazz = ReflectionUtil
                .forName(getClass().getName() + "$Foo");
        assertNotNull(clazz);
    }

    /**
     * @throws Exception
     */
    public void testGetConstructor() throws Exception {
        Constructor<Foo> ctor = ReflectionUtil.getConstructor(Foo.class);
        assertNotNull(ctor);
    }

    /**
     * @throws Exception
     */
    public void testGetDeclaredConstructor() throws Exception {
        Constructor<Foo> ctor = ReflectionUtil.getDeclaredConstructor(
                Foo.class, int.class, String.class);
        assertNotNull(ctor);
    }

    /**
     * @throws Exception
     */
    public void testGetField() throws Exception {
        Field f = ReflectionUtil.getField(Foo.class, "s");
        assertNotNull(f);
    }

    /**
     * @throws Exception
     */
    public void testGetDeclaredField() throws Exception {
        Field f = ReflectionUtil.getDeclaredField(Foo.class, "n");
        assertNotNull(f);
    }

    /**
     * @throws Exception
     */
    public void testGetMethod() throws Exception {
        Method m = ReflectionUtil.getMethod(Foo.class, "getS");
        assertNotNull(m);

        m = ReflectionUtil.getMethod(Foo.class, "setS", String.class);
        assertNotNull(m);
    }

    /**
     * @throws Exception
     */
    public void testGetDeclaredMethod() throws Exception {
        Method m = ReflectionUtil.getDeclaredMethod(Foo.class, "getN");
        assertNotNull(m);

        m = ReflectionUtil.getDeclaredMethod(Foo.class, "setN", int.class);
        assertNotNull(m);
    }

    /**
     * @throws Exception
     */
    public void testNewInstance() throws Exception {
        Foo foo = ReflectionUtil.newInstance(Foo.class);
        assertNotNull(foo);

        Constructor<Foo> ctor = ReflectionUtil.getDeclaredConstructor(
                Foo.class, int.class, String.class);
        ctor.setAccessible(true);
        foo = ReflectionUtil.newInstance(ctor, 10, "foo");
        assertNotNull(foo);
        assertEquals(10, foo.getN());
        assertEquals("foo", foo.getS());
    }

    /**
     * @throws Exception
     */
    public void testGetValue() throws Exception {
        Foo foo = new Foo(10, "foo");

        Field f = ReflectionUtil.getDeclaredField(Foo.class, "n");
        f.setAccessible(true);
        int n = Integer.class.cast(ReflectionUtil.getValue(f, foo));
        assertEquals(10, n);

        f = ReflectionUtil.getDeclaredField(Foo.class, "s");
        String s = ReflectionUtil.getValue(f, foo);
        assertEquals("foo", s);
    }

    /**
     * @throws Exception
     */
    public void testSetValue() throws Exception {
        Foo foo = new Foo();

        Field f = ReflectionUtil.getDeclaredField(Foo.class, "n");
        f.setAccessible(true);
        ReflectionUtil.setValue(f, foo, 10);
        assertEquals(10, foo.n);

        f = ReflectionUtil.getField(Foo.class, "s");
        ReflectionUtil.setValue(f, foo, "foo");
        assertEquals("foo", foo.s);
    }

    /**
     * @throws Exception
     */
    public void testInvoke() throws Exception {
        Foo foo = new Foo();

        Method m = ReflectionUtil.getDeclaredMethod(Foo.class, "setN",
                int.class);
        m.setAccessible(true);
        ReflectionUtil.invoke(m, foo, 10);
        assertEquals(10, foo.n);

        m = ReflectionUtil.getMethod(Foo.class, "setS", String.class);
        ReflectionUtil.invoke(m, foo, "foo");
        assertEquals("foo", foo.s);
    }

    /**
     * @throws Exception
     */
    public void testGetElementTypeOfCollectionFromField() throws Exception {
        Type clazz = ReflectionUtil.getField(Foo.class, "collection")
                .getGenericType();
        assertEquals(String.class, ReflectionUtil
                .getElementTypeOfCollection(clazz));
    }

    /**
     * @throws Exception
     */
    public void testGetElementTypeOfCollectionFromParameter() throws Exception {
        Method m = Foo.class.getMethod("convert", Collection.class,
                Collection.class);
        assertEquals(Object.class, ReflectionUtil
                .getElementTypeOfCollectionFromParameterType(m, 0));
        assertEquals(Integer.class, ReflectionUtil
                .getElementTypeOfCollectionFromParameterType(m, 1));
    }

    /**
     * @throws Exception
     */
    public void testGetElementTypeOfCollectionFromReturnType() throws Exception {
        Method m = Foo.class.getMethod("convert", Collection.class,
                Collection.class);
        assertEquals(String.class, ReflectionUtil
                .getElementTypeOfCollectionFromReturnType(m));
    }

    /**
     * @throws Exception
     */
    public void testGetElementTypeOfListFromField() throws Exception {
        Type clazz = ReflectionUtil.getField(Foo.class, "list")
                .getGenericType();
        assertEquals(String.class, ReflectionUtil.getElementTypeOfList(clazz));
    }

    /**
     * @throws Exception
     */
    public void testGetElementTypeOfListFromParameter() throws Exception {
        Method m = Foo.class.getMethod("convert", List.class, List.class);
        assertEquals(Object.class, ReflectionUtil
                .getElementTypeOfListFromParameterType(m, 0));
        assertEquals(Integer.class, ReflectionUtil
                .getElementTypeOfListFromParameterType(m, 1));
    }

    /**
     * @throws Exception
     */
    public void testGetElementTypeOfListFromReturnType() throws Exception {
        Method m = Foo.class.getMethod("convert", List.class, List.class);
        assertEquals(String.class, ReflectionUtil
                .getElementTypeOfListFromReturnType(m));
    }

    /**
     * @throws Exception
     */
    public void testGetElementTypeOfSetFromField() throws Exception {
        Type clazz = ReflectionUtil.getField(Foo.class, "set").getGenericType();
        assertEquals(String.class, ReflectionUtil.getElementTypeOfSet(clazz));
    }

    /**
     * @throws Exception
     */
    public void testGetElementTypeOfSetFromParameter() throws Exception {
        Method m = Foo.class.getMethod("convert", Set.class, Set.class);
        assertEquals(Object.class, ReflectionUtil
                .getElementTypeOfSetFromParameterType(m, 0));
        assertEquals(Integer.class, ReflectionUtil
                .getElementTypeOfSetFromParameterType(m, 1));
    }

    /**
     * @throws Exception
     */
    public void testGetElementTypeOfSetFromReturnType() throws Exception {
        Method m = Foo.class.getMethod("convert", Set.class, Set.class);
        assertEquals(String.class, ReflectionUtil
                .getElementTypeOfSetFromReturnType(m));
    }

    /**
     * 
     */
    public static class Foo {

        private int n;

        /** */
        public Collection<String> collection;

        /** */
        public List<String> list;

        /** */
        public Set<String> set;

        /** */
        public String s;

        /** */
        public Foo() {
        }

        private Foo(int n, String s) {
            setN(n);
            setS(s);
        }

        private int getN() {
            return n;
        }

        private void setN(int n) {
            this.n = n;
        }

        /**
         * @return
         */
        public String getS() {
            return s;
        }

        /**
         * @param s
         */
        public void setS(String s) {
            this.s = s;
        }

        /**
         * @param o
         * @param i
         * @return
         */
        @SuppressWarnings("unused")
        public Collection<String> convert(Collection<Object> o,
                Collection<Integer> i) {
            return null;
        }

        /**
         * @param o
         * @param i
         * @return
         */
        @SuppressWarnings("unused")
        public List<String> convert(List<Object> o, List<Integer> i) {
            return null;
        }

        /**
         * @param o
         * @param i
         * @return
         */
        @SuppressWarnings("unused")
        public Set<String> convert(Set<Object> o, Set<Integer> i) {
            return null;
        }
    }

}
