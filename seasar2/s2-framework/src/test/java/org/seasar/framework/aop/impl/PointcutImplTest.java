/*
 * Copyright 2004-2012 the Seasar Foundation and the Others.
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
package org.seasar.framework.aop.impl;

import junit.framework.TestCase;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.seasar.framework.util.ClassUtil;

/**
 * @author higa
 * 
 */
public class PointcutImplTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testGetMethodNames() throws Exception {
        PointcutImpl pointcut = new PointcutImpl(Hello2Impl.class);
        String[] methodNames = pointcut.getMethodNames();
        assertEquals("1", 2, methodNames.length);
        for (int i = 0; i < methodNames.length; ++i) {
            System.out.println(methodNames[i]);
        }
    }

    /**
     * @throws Exception
     */
    public void testGetMethodNames2() throws Exception {
        PointcutImpl pointcut = new PointcutImpl(Hello2.class);
        String[] methodNames = pointcut.getMethodNames();
        assertEquals("1", 2, methodNames.length);
        for (int i = 0; i < methodNames.length; ++i) {
            System.out.println(methodNames[i]);
        }
    }

    /**
     * @throws Exception
     */
    public void testGetMethodNames3() throws Exception {
        PointcutImpl pointcut = new PointcutImpl(Hello2Impl2.class);
        String[] methodNames = pointcut.getMethodNames();
        assertEquals("1", 2, methodNames.length);
        for (int i = 0; i < methodNames.length; ++i) {
            System.out.println(methodNames[i]);
        }
    }

    /**
     * @throws Exception
     */
    public void testGetMethodNames4() throws Exception {
        PointcutImpl pointcut = new PointcutImpl(Hello3.class);
        String[] methodNames = pointcut.getMethodNames();
        assertEquals(1, methodNames.length);
        assertEquals("greeting", methodNames[0]);
    }

    /**
     * @throws Exception
     */
    public void testRegex() throws Exception {
        PointcutImpl pointcut = new PointcutImpl(new String[] { "greeting.*" });
        assertTrue("1", pointcut.isApplied(ClassUtil.getMethod(
                Hello2Impl2.class, "greeting", null)));
        assertTrue("2", pointcut.isApplied(ClassUtil.getMethod(
                Hello2Impl2.class, "greeting2", null)));
        assertTrue("3", pointcut.isApplied(ClassUtil.getMethod(
                Hello2Impl2.class, "greeting2", new Class[] { String.class })));
        assertFalse("4", pointcut.isApplied(ClassUtil.getMethod(
                Hello2Impl2.class, "without", null)));
    }

    /**
     * @throws Exception
     */
    public void testMethod() throws Exception {
        PointcutImpl pointcut = new PointcutImpl(ClassUtil.getMethod(
                Hello2Impl2.class, "greeting2", null));
        assertFalse("1", pointcut.isApplied(ClassUtil.getMethod(
                Hello2Impl2.class, "greeting", null)));
        assertTrue("2", pointcut.isApplied(ClassUtil.getMethod(
                Hello2Impl2.class, "greeting2", null)));
        assertFalse("3", pointcut.isApplied(ClassUtil.getMethod(
                Hello2Impl2.class, "greeting2", new Class[] { String.class })));
        assertFalse("4", pointcut.isApplied(ClassUtil.getMethod(
                Hello2Impl2.class, "without", null)));
    }

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * @author higa
     * 
     */
    public interface Hello {
        /**
         * @return
         */
        public String greeting();
    }

    /**
     * @author higa
     * 
     */
    public static class HelloImpl implements Hello {

        public String greeting() {
            return "Hello";
        }
    }

    /**
     * @author higa
     * 
     */
    public class HelloInterceptor implements MethodInterceptor {
        public Object invoke(MethodInvocation invocation) throws Throwable {
            return "Hello";
        }
    }

    /**
     * @author higa
     * 
     */
    public interface Hello2 extends Hello {
        /**
         * @return
         */
        public String greeting2();
    }

    /**
     * @author higa
     * 
     */
    public static class Hello2Impl extends HelloImpl implements Hello2 {

        public String greeting2() {
            return "Hello2";
        }
    }

    /**
     * @author higa
     * 
     */
    public static class Hello2Impl2 implements Hello2 {

        public String greeting() {
            return "Hello";
        }

        public String greeting2() {
            return "Hello2";
        }

        /**
         * @param s
         * @return
         */
        public String greeting2(String s) {
            return s;
        }

        /**
         * @return
         */
        public String without() {
            return "Without";
        }
    }

    /**
     * @author higa
     * 
     */
    public static class Hello3 {

        /**
         * @return
         */
        public String greeting() {
            return "Hello";
        }

        /**
         * @return
         */
        public final String greeting2() {
            return "Hello";
        }
    }
}