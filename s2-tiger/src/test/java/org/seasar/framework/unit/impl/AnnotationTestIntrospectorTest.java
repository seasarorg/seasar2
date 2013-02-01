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
import org.junit.Ignore;
import org.junit.Test;
import org.seasar.framework.aop.interceptors.MockInterceptor;
import org.seasar.framework.container.AspectDef;
import org.seasar.framework.unit.EasyMockTestCase;
import org.seasar.framework.unit.InternalTestContext;
import org.seasar.framework.unit.annotation.EasyMock;
import org.seasar.framework.unit.annotation.EasyMockType;
import org.seasar.framework.unit.annotation.Mock;
import org.seasar.framework.unit.annotation.Mocks;
import org.seasar.framework.unit.annotation.PostBindFields;
import org.seasar.framework.unit.annotation.PreUnbindFields;
import org.seasar.framework.unit.annotation.Prerequisite;
import org.seasar.framework.unit.annotation.RootDicon;
import org.seasar.framework.unit.annotation.TxBehavior;
import org.seasar.framework.unit.annotation.TxBehaviorType;
import org.seasar.framework.util.tiger.ReflectionUtil;

import static org.easymock.EasyMock.*;

/**
 * @author taedium
 * 
 */
public class AnnotationTestIntrospectorTest extends EasyMockTestCase {

    private AnnotationTestIntrospector introspector = new AnnotationTestIntrospector();

    @EasyMock(EasyMockType.STRICT)
    private InternalTestContext context;

    /**
     * @throws Exception
     */
    public void testBeforeClassMethods() throws Exception {
        List<Method> methods = introspector.getBeforeClassMethods(Hoge.class);
        assertEquals(2, methods.size());
        Method method = ReflectionUtil.getDeclaredMethod(Hoge.class, "aaa");
        assertTrue(methods.contains(method));
        method = ReflectionUtil.getDeclaredMethod(Hoge.class, "bbb");
        assertTrue(methods.contains(method));
    }

    /**
     * @throws Exception
     */
    public void testAfterClassMethods() throws Exception {
        List<Method> methods = introspector.getAfterClassMethods(Hoge.class);
        assertEquals(2, methods.size());
        Method method = ReflectionUtil.getDeclaredMethod(Hoge.class, "ccc");
        assertTrue(methods.contains(method));
        method = ReflectionUtil.getDeclaredMethod(Hoge.class, "ddd");
        assertTrue(methods.contains(method));
    }

    /**
     * @throws Exception
     */
    public void testBeforeMethods() throws Exception {
        List<Method> methods = introspector.getBeforeMethods(Hoge.class);
        assertEquals(2, methods.size());
        Method method = ReflectionUtil.getDeclaredMethod(Hoge.class, "eee");
        assertTrue(methods.contains(method));
        method = ReflectionUtil.getDeclaredMethod(Hoge.class, "fff");
        assertTrue(methods.contains(method));
    }

    /**
     * @throws Exception
     */
    public void testAfterMethods() throws Exception {
        List<Method> methods = introspector.getAfterMethods(Hoge.class);
        assertEquals(2, methods.size());
        Method method = ReflectionUtil.getDeclaredMethod(Hoge.class, "ggg");
        assertTrue(methods.contains(method));
        method = ReflectionUtil.getDeclaredMethod(Hoge.class, "hhh");
        assertTrue(methods.contains(method));
    }

    /**
     * @throws Exception
     */
    public void testPostBindFieldsMethods() throws Exception {
        List<Method> methods = introspector
                .getPostBindFieldsMethods(Hoge.class);
        assertEquals(1, methods.size());
        Method method = ReflectionUtil.getDeclaredMethod(Hoge.class, "lll");
        assertTrue(methods.contains(method));
        method = ReflectionUtil.getDeclaredMethod(Hoge.class, "postBindFields");
        assertFalse(methods.contains(method));
    }

    /**
     * @throws Exception
     */
    public void testPreUnbindFieldsMethods() throws Exception {
        List<Method> methods = introspector
                .getPreUnbindFieldsMethods(Hoge.class);
        assertEquals(1, methods.size());
        Method method = ReflectionUtil.getDeclaredMethod(Hoge.class, "mmm");
        assertTrue(methods.contains(method));
        method = ReflectionUtil
                .getDeclaredMethod(Hoge.class, "preUnbindFields");
        assertFalse(methods.contains(method));
    }

    /**
     * @throws Exception
     */
    public void testTestMethods() throws Exception {
        List<Method> methods = introspector.getTestMethods(Hoge.class);
        assertEquals(2, methods.size());
        Method method = ReflectionUtil.getDeclaredMethod(Hoge.class, "iii");
        assertTrue(methods.contains(method));
        method = ReflectionUtil.getDeclaredMethod(Hoge.class, "jjj");
        assertTrue(methods.contains(method));
    }

    /**
     * 
     */
    public void testNeedsTransaction() {
        Class<?> clazz = MethodTxBehavior.class;
        Method method = ReflectionUtil.getDeclaredMethod(clazz, "aaa");
        assertTrue(introspector.needsTransaction(clazz, method));
        method = ReflectionUtil.getDeclaredMethod(clazz, "bbb");
        assertTrue(introspector.needsTransaction(clazz, method));
        method = ReflectionUtil.getDeclaredMethod(clazz, "ccc");
        assertTrue(introspector.needsTransaction(clazz, method));
        method = ReflectionUtil.getDeclaredMethod(clazz, "ddd");
        assertFalse(introspector.needsTransaction(clazz, method));

        clazz = NoneClassTxBehavior.class;
        method = ReflectionUtil.getDeclaredMethod(clazz, "aaa");
        assertFalse(introspector.needsTransaction(clazz, method));
        method = ReflectionUtil.getDeclaredMethod(clazz, "bbb");
        assertTrue(introspector.needsTransaction(clazz, method));
        method = ReflectionUtil.getDeclaredMethod(clazz, "ccc");
        assertTrue(introspector.needsTransaction(clazz, method));
        method = ReflectionUtil.getDeclaredMethod(clazz, "ddd");
        assertFalse(introspector.needsTransaction(clazz, method));
    }

    /**
     * 
     */
    public void testRequiresTransactionCommitment() {
        Class<?> clazz = MethodTxBehavior.class;
        Method method = ReflectionUtil.getDeclaredMethod(clazz, "aaa");
        assertFalse(introspector.requiresTransactionCommitment(clazz, method));
        method = ReflectionUtil.getDeclaredMethod(clazz, "bbb");
        assertTrue(introspector.requiresTransactionCommitment(clazz, method));
        method = ReflectionUtil.getDeclaredMethod(clazz, "ccc");
        assertFalse(introspector.requiresTransactionCommitment(clazz, method));
        method = ReflectionUtil.getDeclaredMethod(clazz, "ddd");
        assertFalse(introspector.requiresTransactionCommitment(clazz, method));

        clazz = CommitClassTxBehavior.class;
        method = ReflectionUtil.getDeclaredMethod(clazz, "aaa");
        assertTrue(introspector.requiresTransactionCommitment(clazz, method));
        method = ReflectionUtil.getDeclaredMethod(clazz, "bbb");
        assertTrue(introspector.requiresTransactionCommitment(clazz, method));
        method = ReflectionUtil.getDeclaredMethod(clazz, "ccc");
        assertFalse(introspector.requiresTransactionCommitment(clazz, method));
        method = ReflectionUtil.getDeclaredMethod(clazz, "ddd");
        assertFalse(introspector.requiresTransactionCommitment(clazz, method));
    }

    /**
     * 
     */
    public void testIsFulfilled() {
        Aaa aaa = new Aaa();
        Method method = ReflectionUtil.getDeclaredMethod(aaa.getClass(), "aaa");
        boolean result = introspector.isFulfilled(aaa.getClass(), method, aaa);
        assertTrue(result);

        Bbb bbb = new Bbb();
        method = ReflectionUtil.getDeclaredMethod(bbb.getClass(), "aaa");
        result = introspector.isFulfilled(bbb.getClass(), method, bbb);
        assertFalse(result);
    }

    /**
     * 
     */
    public void testIsFulfilled_disabled() {
        introspector.setEnablePrerequisite(false);
        Bbb bbb = new Bbb();
        Method method = ReflectionUtil.getDeclaredMethod(bbb.getClass(), "aaa");
        boolean result = introspector.isFulfilled(bbb.getClass(), method, bbb);
        assertTrue(result);
    }

    /**
     * 
     */
    public void testIsIgnored() {
        Method method = ReflectionUtil.getDeclaredMethod(Ccc.class, "aaa");
        assertTrue(introspector.isIgnored(method));
    }

    /**
     * 
     */
    public void testIsIgnored_disabled() {
        introspector.setEnableIgnore(false);
        Method method = ReflectionUtil.getDeclaredMethod(Ccc.class, "aaa");
        assertFalse(introspector.isIgnored(method));
    }

    /**
     * 
     */
    public void testCreateMock() {
        Ddd ddd = new Ddd();
        Method method = ReflectionUtil.getMethod(ddd.getClass(), "aaa");
        introspector.createMock(method, ddd, context);
    }

    /**
     * 
     */
    public void recordCreateMock() {
        context.addAspecDef(same(Hello.class), isA(AspectDef.class));
        context.addMockInterceptor(isA(MockInterceptor.class));
    }

    /**
     * 
     */
    public void testCreateMultiMocks() {
        Ddd ddd = new Ddd();
        Method method = ReflectionUtil.getMethod(ddd.getClass(), "bbb");
        introspector.createMock(method, ddd, context);
    }

    /**
     * 
     */
    public void recordCreateMultiMocks() {
        context.addAspecDef(same(Hello.class), isA(AspectDef.class));
        context.addMockInterceptor(isA(MockInterceptor.class));
        context.addAspecDef(eq("hello"), isA(AspectDef.class));
        context.addMockInterceptor(isA(MockInterceptor.class));
    }

    /**
     * 
     */
    public void testGetRootDicon() {
        Method method = ReflectionUtil.getMethod(Eee.class, "aaa");
        String rootDicon = introspector.getRootDicon(Eee.class, method);
        assertEquals("hoge.dicon", rootDicon);

        method = ReflectionUtil.getMethod(Eee.class, "bbb");
        rootDicon = introspector.getRootDicon(Eee.class, method);
        assertEquals("foo.dicon", rootDicon);

    }

    /**
     * 
     */
    public static class Hoge {

        /**
         * 
         */
        @BeforeClass
        public static void aaa() {
        }

        /**
         * 
         */
        @BeforeClass
        public static void bbb() {
        }

        /**
         * 
         */
        @AfterClass
        public static void ccc() {
        }

        /**
         * 
         */
        @AfterClass
        public static void ddd() {
        }

        /**
         * 
         */
        @Before
        public void eee() {
        }

        /**
         * 
         */
        @Before
        public void fff() {
        }

        /**
         * 
         */
        @After
        public void ggg() {
        }

        /**
         * 
         */
        @After
        public void hhh() {
        }

        /**
         * 
         */
        @Test
        public void iii() {
        }

        /**
         * 
         */
        @Test
        public void jjj() {
        }

        /**
         * 
         */
        public void kkk() {
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
        @PostBindFields
        public void lll() {

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
        public void mmm() {

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
    public static class MethodTxBehavior {

        /**
         * 
         */
        public void aaa() {
        }

        /**
         * 
         */
        @TxBehavior(TxBehaviorType.COMMIT)
        public void bbb() {
        }

        /**
         * 
         */
        @TxBehavior(TxBehaviorType.ROLLBACK)
        public void ccc() {
        }

        /**
         * 
         */
        @TxBehavior(TxBehaviorType.NONE)
        public void ddd() {
        }
    }

    /**
     * 
     */
    @TxBehavior(TxBehaviorType.NONE)
    public static class NoneClassTxBehavior {

        /**
         * 
         */
        public void aaa() {
        }

        /**
         * 
         */
        @TxBehavior(TxBehaviorType.COMMIT)
        public void bbb() {
        }

        /**
         * 
         */
        @TxBehavior(TxBehaviorType.ROLLBACK)
        public void ccc() {
        }

        /**
         * 
         */
        @TxBehavior(TxBehaviorType.NONE)
        public void ddd() {
        }
    }

    /**
     * 
     */
    @TxBehavior(TxBehaviorType.COMMIT)
    public static class CommitClassTxBehavior {

        /**
         * 
         */
        public void aaa() {
        }

        /**
         * 
         */
        @TxBehavior(TxBehaviorType.COMMIT)
        public void bbb() {
        }

        /**
         * 
         */
        @TxBehavior(TxBehaviorType.ROLLBACK)
        public void ccc() {
        }

        /**
         * 
         */
        @TxBehavior(TxBehaviorType.NONE)
        public void ddd() {
        }
    }

    /**
     * 
     */
    public static class Aaa {

        /**
         * 
         */
        public void aaa() {
        }
    }

    /**
     * 
     */
    @Prerequisite("true")
    public static class Bbb {

        /**
         * 
         */
        @Prerequisite("false")
        public void aaa() {
        }
    }

    /**
     * 
     */
    public static class Ccc {

        /**
         * 
         */
        @Ignore
        public void aaa() {
        }
    }

    /**
     * 
     */
    public static class Ddd {

        /**
         * 
         */
        @Mock(target = Hello.class, pointcut = "greeting")
        public void aaa() {
        }

        /**
         * 
         */
        @Mocks( {
                @Mock(target = Hello.class, pointcut = "greeting"),
                @Mock(target = Hello.class, targetName = "hello", pointcut = "echo") })
        public void bbb() {
        }
    }

    /**
     * 
     */
    @RootDicon("hoge.dicon")
    public static class Eee {

        /**
         * 
         */
        public void aaa() {
        }

        /**
         * 
         */
        @RootDicon("foo.dicon")
        public void bbb() {
        }
    }

    /**
     * 
     */
    public interface Hello {

        /**
         * @return
         */
        String greeting();

        /**
         * @param aaa
         * @return
         */
        String echo(String aaa);
    }
}
