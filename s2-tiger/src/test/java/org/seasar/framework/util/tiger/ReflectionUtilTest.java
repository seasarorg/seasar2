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
package org.seasar.framework.util.tiger;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import junit.framework.TestCase;

/**
 * @author koichik
 * 
 */
public class ReflectionUtilTest extends TestCase {

    public void testForName() throws Exception {
        Class<Foo> clazz = ReflectionUtil
                .forName(getClass().getName() + "$Foo");
        assertNotNull(clazz);
    }

    public void testGetConstructor() throws Exception {
        Constructor<Foo> ctor = ReflectionUtil.getConstructor(Foo.class);
        assertNotNull(ctor);
    }

    public void testGetDeclaredConstructor() throws Exception {
        Constructor<Foo> ctor = ReflectionUtil.getDeclaredConstructor(
                Foo.class, int.class, String.class);
        assertNotNull(ctor);
    }

    public void testGetField() throws Exception {
        Field f = ReflectionUtil.getField(Foo.class, "s");
        assertNotNull(f);
    }

    public void testGetDeclaredField() throws Exception {
        Field f = ReflectionUtil.getDeclaredField(Foo.class, "n");
        assertNotNull(f);
    }

    public void testGetMethod() throws Exception {
        Method m = ReflectionUtil.getMethod(Foo.class, "getS");
        assertNotNull(m);

        m = ReflectionUtil.getMethod(Foo.class, "setS", String.class);
        assertNotNull(m);
    }

    public void testGetDeclaredMethod() throws Exception {
        Method m = ReflectionUtil.getDeclaredMethod(Foo.class, "getN");
        assertNotNull(m);

        m = ReflectionUtil.getDeclaredMethod(Foo.class, "setN", int.class);
        assertNotNull(m);
    }

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

    public static class Foo {
        private int n;

        public String s;

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

        public String getS() {
            return s;
        }

        public void setS(String s) {
            this.s = s;
        }
    }

}
