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
package org.seasar.framework.unit.impl;

import java.lang.reflect.Method;
import java.util.List;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.seasar.framework.util.tiger.ReflectionUtil;

/**
 * @author taedium
 * 
 */
public class AnnotationTestIntrospectorTest extends TestCase {

    public AnnotationTestIntrospector introspector = new AnnotationTestIntrospector();

    public void testBeforeClassMethods() throws Exception {
        List<Method> methods = introspector.getBeforeClassMethods(Hoge.class);
        assertEquals(2, methods.size());
        Method method = ReflectionUtil.getDeclaredMethod(Hoge.class, "aaa");
        assertTrue(methods.contains(method));
        method = ReflectionUtil.getDeclaredMethod(Hoge.class, "bbb");
        assertTrue(methods.contains(method));
    }

    public void testAfterClassMethods() throws Exception {
        List<Method> methods = introspector.getAfterClassMethods(Hoge.class);
        assertEquals(2, methods.size());
        Method method = ReflectionUtil.getDeclaredMethod(Hoge.class, "ccc");
        assertTrue(methods.contains(method));
        method = ReflectionUtil.getDeclaredMethod(Hoge.class, "ddd");
        assertTrue(methods.contains(method));
    }

    public void testBeforeMethods() throws Exception {
        List<Method> methods = introspector.getBeforeMethods(Hoge.class);
        assertEquals(2, methods.size());
        Method method = ReflectionUtil.getDeclaredMethod(Hoge.class, "eee");
        assertTrue(methods.contains(method));
        method = ReflectionUtil.getDeclaredMethod(Hoge.class, "fff");
        assertTrue(methods.contains(method));
    }

    public void testAfterMethods() throws Exception {
        List<Method> methods = introspector.getAfterMethods(Hoge.class);
        assertEquals(2, methods.size());
        Method method = ReflectionUtil.getDeclaredMethod(Hoge.class, "ggg");
        assertTrue(methods.contains(method));
        method = ReflectionUtil.getDeclaredMethod(Hoge.class, "hhh");
        assertTrue(methods.contains(method));
    }

    public void testTestMethods() throws Exception {
        List<Method> methods = introspector.getTestMethods(Hoge.class);
        assertEquals(2, methods.size());
        Method method = ReflectionUtil.getDeclaredMethod(Hoge.class, "iii");
        assertTrue(methods.contains(method));
        method = ReflectionUtil.getDeclaredMethod(Hoge.class, "jjj");
        assertTrue(methods.contains(method));
    }

    public static class Hoge {
        @BeforeClass
        public static void aaa() {
        }

        @BeforeClass
        public static void bbb() {
        }

        @AfterClass
        public static void ccc() {
        }

        @AfterClass
        public static void ddd() {
        }

        @Before
        public void eee() {
        }

        @Before
        public void fff() {
        }

        @After
        public void ggg() {
        }

        @After
        public void hhh() {
        }

        @Test
        public void iii() {
        }

        @Test
        public void jjj() {
        }

        public void kkk() {
        }

        public void before() {
        }

        public void after() {
        }
    }
}
