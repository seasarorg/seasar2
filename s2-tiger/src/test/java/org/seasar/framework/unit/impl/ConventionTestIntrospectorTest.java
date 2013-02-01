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
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.unit.annotation.PostBindFields;
import org.seasar.framework.unit.annotation.PreUnbindFields;
import org.seasar.framework.util.tiger.ReflectionUtil;

/**
 * @author taedium
 * 
 */
public class ConventionTestIntrospectorTest extends S2TestCase {

    private ConventionTestIntrospector introspector;

    /**
     * @throws Exception
     */
    public void testGetTestMethods() throws Exception {
        introspector = new ConventionTestIntrospector();
        introspector.init();
        List<Method> methods = introspector.getTestMethods(Hoge.class);
        assertEquals(3, methods.size());
        Method method = ReflectionUtil.getDeclaredMethod(Hoge.class, "ccc");
        assertTrue(methods.contains(method));
        method = ReflectionUtil.getDeclaredMethod(Hoge.class, "ddd");
        assertTrue(methods.contains(method));
        method = ReflectionUtil.getDeclaredMethod(Hoge.class, "record");
        assertTrue(methods.contains(method));
    }

    /**
     * @throws Exception
     */
    public void testBeforeClassMethods() throws Exception {
        introspector = new ConventionTestIntrospector();
        introspector.init();
        List<Method> methods = introspector.getBeforeClassMethods(Hoge.class);
        assertEquals(2, methods.size());
        Method method = ReflectionUtil.getDeclaredMethod(Hoge.class, "iii");
        assertEquals(method, methods.get(0));
        method = ReflectionUtil.getDeclaredMethod(Hoge.class, "beforeClass");
        assertEquals(method, methods.get(1));
    }

    /**
     * @throws Exception
     */
    public void testAfterClassMethods() throws Exception {
        introspector = new ConventionTestIntrospector();
        introspector.init();
        List<Method> methods = introspector.getAfterClassMethods(Hoge.class);
        assertEquals(2, methods.size());
        Method method = ReflectionUtil.getDeclaredMethod(Hoge.class, "jjj");
        assertEquals(method, methods.get(0));
        method = ReflectionUtil.getDeclaredMethod(Hoge.class, "afterClass");
        assertEquals(method, methods.get(1));
    }

    /**
     * @throws Exception
     */
    public void testBeforeMethods() throws Exception {
        introspector = new ConventionTestIntrospector();
        introspector.init();
        List<Method> methods = introspector.getBeforeMethods(Hoge.class);
        assertEquals(2, methods.size());
        Method method = ReflectionUtil.getDeclaredMethod(Hoge.class, "aaa");
        assertEquals(method, methods.get(0));
        method = ReflectionUtil.getDeclaredMethod(Hoge.class, "before");
        assertEquals(method, methods.get(1));
    }

    /**
     * @throws Exception
     */
    public void testAfterMethods() throws Exception {
        introspector = new ConventionTestIntrospector();
        introspector.init();
        List<Method> methods = introspector.getAfterMethods(Hoge.class);
        assertEquals(2, methods.size());
        Method method = ReflectionUtil.getDeclaredMethod(Hoge.class, "bbb");
        assertEquals(method, methods.get(0));
        method = ReflectionUtil.getDeclaredMethod(Hoge.class, "after");
        assertEquals(method, methods.get(1));
    }

    /**
     * @throws Exception
     */
    public void testEachBeforeMethod() throws Exception {
        introspector = new ConventionTestIntrospector();
        introspector.init();
        Method method = ReflectionUtil.getDeclaredMethod(Hoge.class, "ddd");
        Method actual = introspector.getEachBeforeMethod(Hoge.class, method);
        assertEquals("beforeDdd", actual.getName());
    }

    /**
     * @throws Exception
     */
    public void testNonExistEachBeforeMethod() throws Exception {
        introspector = new ConventionTestIntrospector();
        introspector.init();
        Method method = ReflectionUtil.getDeclaredMethod(Hoge.class, "ccc");
        Method actual = introspector.getEachBeforeMethod(Hoge.class, method);
        assertNull(actual);
    }

    /**
     * @throws Exception
     */
    public void testEachAfterMethod() throws Exception {
        introspector = new ConventionTestIntrospector();
        introspector.init();
        Method method = ReflectionUtil.getDeclaredMethod(Hoge.class, "ddd");
        Method actual = introspector.getEachAfterMethod(Hoge.class, method);
        assertEquals("afterDdd", actual.getName());
    }

    /**
     * @throws Exception
     */
    public void testNonExistEachAfterMethod() throws Exception {
        introspector = new ConventionTestIntrospector();
        introspector.init();
        Method method = ReflectionUtil.getDeclaredMethod(Hoge.class, "ccc");
        Method actual = introspector.getEachAfterMethod(Hoge.class, method);
        assertNull(actual);
    }

    /**
     * @throws Exception
     */
    public void testEachRecordMethod() throws Exception {
        introspector = new ConventionTestIntrospector();
        introspector.init();
        Method method = ReflectionUtil.getDeclaredMethod(Hoge.class, "ddd");
        Method actual = introspector.getEachRecordMethod(Hoge.class, method);
        assertEquals("recordDdd", actual.getName());
    }

    /**
     * @throws Exception
     */
    public void testNonExistEachRecordMethod() throws Exception {
        introspector = new ConventionTestIntrospector();
        introspector.init();
        Method method = ReflectionUtil.getDeclaredMethod(Hoge.class, "ccc");
        Method actual = introspector.getEachRecordMethod(Hoge.class, method);
        assertNull(actual);
    }

    /**
     * @throws Exception
     */
    public void testSuperclassTestMethods() throws Exception {
        introspector = new ConventionTestIntrospector();
        introspector.init();
        List<Method> methods = introspector.getTestMethods(Bar.class);
        assertEquals(2, methods.size());
    }

    /**
     * @throws Exception
     */
    public void testSuperclassBeforeClassMethods() throws Exception {
        introspector = new ConventionTestIntrospector();
        introspector.init();
        List<Method> methods = introspector.getBeforeClassMethods(Bar.class);
        assertEquals(1, methods.size());
    }

    /**
     * @throws Exception
     */
    public void testSuperclassAfterClassMethods() throws Exception {
        introspector = new ConventionTestIntrospector();
        introspector.init();
        List<Method> methods = introspector.getAfterClassMethods(Bar.class);
        assertEquals(1, methods.size());
    }

    /**
     * @throws Exception
     */
    public void testSuperclassBeforeMethods() throws Exception {
        introspector = new ConventionTestIntrospector();
        introspector.init();
        List<Method> methods = introspector.getBeforeMethods(Bar.class);
        assertEquals(1, methods.size());
    }

    /**
     * @throws Exception
     */
    public void testSuperclassAfterMethods() throws Exception {
        introspector = new ConventionTestIntrospector();
        introspector.init();
        List<Method> methods = introspector.getAfterMethods(Bar.class);
        assertEquals(1, methods.size());
    }

    /**
     * @throws Exception
     */
    public void testSuperclassEachBeforeMethod() throws Exception {
        introspector = new ConventionTestIntrospector();
        introspector.init();
        Method method = ReflectionUtil.getDeclaredMethod(Foo.class, "aaa");
        Method actual = introspector.getEachBeforeMethod(Bar.class, method);
        assertEquals("beforeAaa", actual.getName());

        method = ReflectionUtil.getDeclaredMethod(Bar.class, "bbb");
        actual = introspector.getEachBeforeMethod(Bar.class, method);
        assertEquals("beforeBbb", actual.getName());
    }

    /**
     * @throws Exception
     */
    public void testSuperclassEachAfterMethod() throws Exception {
        introspector = new ConventionTestIntrospector();
        introspector.init();
        Method method = ReflectionUtil.getDeclaredMethod(Foo.class, "aaa");
        Method actual = introspector.getEachAfterMethod(Bar.class, method);
        assertEquals("afterAaa", actual.getName());

        method = ReflectionUtil.getDeclaredMethod(Bar.class, "bbb");
        actual = introspector.getEachAfterMethod(Bar.class, method);
        assertEquals("afterBbb", actual.getName());
    }

    /**
     * @throws Exception
     */
    public void testSuperclassEachRecordMethod() throws Exception {
        introspector = new ConventionTestIntrospector();
        introspector.init();
        Method method = ReflectionUtil.getDeclaredMethod(Foo.class, "aaa");
        Method actual = introspector.getEachRecordMethod(Bar.class, method);
        assertEquals("recordAaa", actual.getName());

        method = ReflectionUtil.getDeclaredMethod(Bar.class, "bbb");
        actual = introspector.getEachRecordMethod(Bar.class, method);
        assertEquals("recordBbb", actual.getName());
    }

    /**
     * @throws Exception
     */
    public void testSuperclassPostBindFieldsMethods() throws Exception {
        introspector = new ConventionTestIntrospector();
        introspector.init();
        List<Method> methods = introspector
                .getPostBindFieldsMethods(Hoge.class);
        assertEquals(2, methods.size());
        Method method = ReflectionUtil.getDeclaredMethod(Hoge.class, "kkk");
        assertEquals(method, methods.get(0));
        method = ReflectionUtil.getDeclaredMethod(Hoge.class, "postBindFields");
        assertEquals(method, methods.get(1));
    }

    /**
     * @throws Exception
     */
    public void testSuperclassPreUnbindFieldsMethods() throws Exception {
        introspector = new ConventionTestIntrospector();
        introspector.init();
        List<Method> methods = introspector
                .getPreUnbindFieldsMethods(Hoge.class);
        assertEquals(2, methods.size());
        Method method = ReflectionUtil.getDeclaredMethod(Hoge.class, "lll");
        assertEquals(method, methods.get(0));
        method = ReflectionUtil
                .getDeclaredMethod(Hoge.class, "preUnbindFields");
        assertEquals(method, methods.get(1));
    }

    /**
     * @throws Exception
     */
    public void setUpCustomize() throws Exception {
        include("ConventionTestIntrospectorTest.dicon");
    }

    /**
     * @throws Exception
     */
    public void testCustomize() throws Exception {
        List<Method> methods = introspector.getBeforeClassMethods(Baz.class);
        assertEquals(1, methods.size());
        assertEquals("setUpClass", methods.get(0).getName());

        methods = introspector.getAfterClassMethods(Baz.class);
        assertEquals(1, methods.size());
        assertEquals("tearDownClass", methods.get(0).getName());

        methods = introspector.getBeforeMethods(Baz.class);
        assertEquals(1, methods.size());
        assertEquals("setUp", methods.get(0).getName());

        methods = introspector.getAfterMethods(Baz.class);
        assertEquals(1, methods.size());
        assertEquals("tearDown", methods.get(0).getName());

        Method method = ReflectionUtil.getDeclaredMethod(Baz.class, "aaa");
        Method actual = introspector.getEachBeforeMethod(Baz.class, method);
        assertEquals("setUpAaa", actual.getName());

        actual = introspector.getEachAfterMethod(Baz.class, method);
        assertEquals("tearDownAaa", actual.getName());
    }

    /**
     * 
     */
    public static class Hoge {

        /**
         * 
         */
        public static void beforeClass() {
        }

        /**
         * 
         */
        public static void afterClass() {
        }

        /**
         * 
         */
        public void before() {
        }

        /**
         * 
         */
        public void after() {
        }

        /**
         * 
         */
        @Before
        public void aaa() {
        }

        /**
         * 
         */
        @After
        public void bbb() {
        }

        /**
         * 
         */
        @Test
        public void ccc() {
        }

        /**
         * 
         */
        public void beforeDdd() {
        }

        /**
         * 
         */
        public void ddd() {
        }

        /**
         * 
         */
        public void recordDdd() {
        }

        /**
         * 
         */
        public void afterDdd() {
        }

        /**
         * 
         */
        public void record() {
        }

        @SuppressWarnings("unused")
        private void eee() {
        }

        /**
         * 
         */
        public static void fff() {
        }

        /**
         * @return
         */
        public String ggg() {
            return null;
        }

        /**
         * @param s
         */
        @SuppressWarnings("unused")
        public void hhh(String s) {
        }

        /**
         * 
         */
        @BeforeClass
        public static void iii() {
        }

        /**
         * 
         */
        @AfterClass
        public static void jjj() {
        }

        /**
         * 
         */
        @PostBindFields
        public void kkk() {

        }

        /**
         * 
         */
        public void postBindFields() {

        }

        /**
         * 
         */
        @PreUnbindFields
        public void lll() {

        }

        /**
         * 
         */
        public void preUnbindFields() {

        }

    }

    /**
     * 
     */
    public static class Foo {

        /**
         * 
         */
        public static void beforeClass() {
        }

        /**
         * 
         */
        public static void afterClass() {
        }

        /**
         * 
         */
        public void before() {
        }

        /**
         * 
         */
        public void after() {
        }

        /**
         * 
         */
        public void beforeAaa() {
        }

        /**
         * 
         */
        public void aaa() {
        }

        /**
         * 
         */
        public void afterAaa() {
        }

        /**
         * 
         */
        public void recordAaa() {
        }

        /**
         * 
         */
        public void beforeBbb() {
        }

        /**
         * 
         */
        public void afterBbb() {
        }

        /**
         * 
         */
        public void recordBbb() {
        }

    }

    /**
     * 
     */
    public static class Bar extends Foo {

        /**
         * 
         */
        public void bbb() {
        }
    }

    /**
     * 
     */
    public static class Baz {

        /**
         * 
         */
        public static void setUpClass() {
        }

        /**
         * 
         */
        public static void tearDownClass() {
        }

        /**
         * 
         */
        public void setUp() {
        }

        /**
         * 
         */
        public void tearDown() {
        }

        /**
         * 
         */
        public void setUpAaa() {
        }

        /**
         * 
         */
        public void aaa() {
        }

        /**
         * 
         */
        public void tearDownAaa() {
        }
    }
}
