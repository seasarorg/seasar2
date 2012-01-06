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
package org.seasar.framework.aop.proxy;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.util.Date;

import junit.framework.TestCase;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.seasar.framework.aop.Aspect;
import org.seasar.framework.aop.Pointcut;
import org.seasar.framework.aop.impl.AspectImpl;
import org.seasar.framework.aop.impl.PointcutImpl;
import org.seasar.framework.aop.interceptors.TraceInterceptor;
import org.seasar.framework.util.SerializeUtil;

/**
 * @author higa
 * 
 */
public class AopProxyTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testIntercept() throws Exception {
        TestInvocation invocation = new TestInvocation();
        Pointcut pointcut = new PointcutImpl(new String[] { "getTime" });
        Aspect aspect = new AspectImpl(invocation, pointcut);
        AopProxy aopProxy = new AopProxy(Date.class, new Aspect[] { aspect });
        Date proxy = (Date) aopProxy.create();
        System.out.println(proxy.getTime());
        assertEquals("2", true, invocation.invoked_);
    }

    /**
     * @throws Exception
     */
    public void testInterface() throws Exception {
        Pointcut pointcut = new PointcutImpl(new String[] { "greeting" });
        Aspect aspect = new AspectImpl(new HelloInterceptor(), pointcut);
        AopProxy aopProxy = new AopProxy(Hello.class, new Aspect[] { aspect });
        Hello proxy = (Hello) aopProxy.create();
        assertEquals("1", "Hello", proxy.greeting());
    }

    /**
     * @throws Exception
     */
    public void testCreateForArgs() throws Exception {
        Aspect aspect = new AspectImpl(new TraceInterceptor());
        AopProxy aopProxy = new AopProxy(HelloImpl.class,
                new Aspect[] { aspect });
        Hello proxy = (Hello) aopProxy.create(new Class[] { String.class },
                new Object[] { "Hello" });
        assertEquals("1", "Hello", proxy.greeting());
        System.out.println(proxy.hashCode());
    }

    /**
     * @throws Exception
     */
    public void testEquals() throws Exception {
        Pointcut pointcut = new PointcutImpl(new String[] { "greeting" });
        Aspect aspect = new AspectImpl(new HelloInterceptor(), pointcut);
        AopProxy aopProxy = new AopProxy(Hello.class, new Aspect[] { aspect });
        Hello proxy = (Hello) aopProxy.create();
        assertEquals("1", true, proxy.equals(proxy));
        assertEquals("2", false, proxy.equals(null));
        assertEquals("3", false, proxy.equals("hoge"));
    }

    /**
     * 
     */
    public void testEqualsByValue() {
        Pointcut pointcut = new PointcutImpl(new String[] { "toString" });
        Aspect aspect = new AspectImpl(new HelloInterceptor(), pointcut);
        AopProxy aopProxy = new AopProxy(BigDecimal.class,
                new Aspect[] { aspect });
        BigDecimal proxy = (BigDecimal) aopProxy.create(
                new Class[] { String.class }, new Object[] { "10" });
        assertTrue("1", proxy.equals(proxy));
        assertTrue("2", proxy.equals(new BigDecimal("10")));
        assertFalse("3", proxy.equals(null));
        assertFalse("3", proxy.equals(new BigDecimal("100")));
        assertFalse("3", proxy.equals("Hoge"));
    }

    /**
     * @throws Exception
     */
    public void testFinalMethod() throws Exception {
        Pointcut pointcut = new PointcutImpl(new String[] { "greeting" });
        Aspect aspect = new AspectImpl(new HelloInterceptor(), pointcut);
        AopProxy aopProxy = new AopProxy(HelloImpl3.class,
                new Aspect[] { aspect });
        Hello proxy = (Hello) aopProxy.create();
        assertEquals("1", "hoge", proxy.greeting());
    }

    /**
     * @throws Exception
     */
    public void testSerialize() throws Exception {
        Aspect aspect = new AspectImpl(new TraceInterceptor());
        AopProxy aopProxy = new AopProxy(HelloImpl.class,
                new Aspect[] { aspect });
        Hello proxy = (Hello) aopProxy.create(new Class[] { String.class },
                new Object[] { "Hello" });
        Hello copy = (Hello) SerializeUtil.serialize(proxy);
        assertEquals("1", "Hello", copy.greeting());
    }

    /**
     * @throws Exception
     */
    public void testPerformance() throws Exception {
        Date target = new Date();
        Pointcut pointcut = new PointcutImpl(new String[] { "compareTo" });
        Aspect aspect = new AspectImpl(new TestInvocation(), pointcut);
        AopProxy aopProxy = new AopProxy(Date.class, new Aspect[] { aspect });
        Date proxy = (Date) aopProxy.create();

        MyInvocationHandler handler = new MyInvocationHandler(target);
        Comparable proxy2 = (Comparable) Proxy.newProxyInstance(Date.class
                .getClassLoader(), new Class[] { Comparable.class }, handler);

        final int num = 2000000;
        long start = System.currentTimeMillis();
        for (int i = 0; i < num; ++i) {
            // target.hashCode();
            target.compareTo(target);
        }
        System.out.println("target:" + (System.currentTimeMillis() - start));

        start = System.currentTimeMillis();
        for (int i = 0; i < num; ++i) {
            // proxy.hashCode();
            proxy.compareTo(target);
        }
        System.out.println("AOP proxy:" + (System.currentTimeMillis() - start));

        start = System.currentTimeMillis();
        for (int i = 0; i < num; ++i) {
            // proxy.hashCode();
            proxy2.compareTo(target);
        }
        System.out.println("JDK proxy:" + (System.currentTimeMillis() - start));
    }

    /**
     *
     */
    public class TestInvocation implements MethodInterceptor {

        private boolean invoked_ = false;

        public Object invoke(MethodInvocation invocation) throws Throwable {
            invoked_ = true;
            return invocation.proceed();
        }

    }

    /**
     *
     */
    public class MyInvocation implements MethodInterceptor {

        public Object invoke(MethodInvocation invocation) throws Throwable {
            return invocation.proceed();
        }

    }

    /**
     *
     */
    public interface Hello extends Serializable {
        /**
         * @return
         */
        public String greeting();
    }

    /**
     *
     */
    public static class HelloImpl implements Hello {

        private static final long serialVersionUID = -6888891433122734807L;

        private String str_;

        /**
         * @param str
         */
        public HelloImpl(String str) {
            str_ = str;
        }

        public String greeting() {
            return str_;
        }
    }

    /**
     *
     */
    public static class Hello2Impl implements Hello {

        private static final long serialVersionUID = -7275896388662514561L;

        public String greeting() {
            return "Hello2";
        }
    }

    /**
     *
     */
    public static class HelloImpl3 implements Hello {

        private static final long serialVersionUID = 5992242320294594184L;

        public final String greeting() {
            return "hoge";
        }
    }

    /**
     *
     */
    public class HelloInterceptor implements MethodInterceptor {
        public Object invoke(MethodInvocation invocation) throws Throwable {
            return "Hello";
        }
    }

    /**
     *
     */
    public static class MyInvocationHandler implements InvocationHandler {
        private Object target;

        /**
         * @param target
         */
        public MyInvocationHandler(Object target) {
            this.target = target;
        }

        public Object invoke(Object proxy, Method method, Object[] args)
                throws Throwable {
            return method.invoke(target, args);
        }
    }
}