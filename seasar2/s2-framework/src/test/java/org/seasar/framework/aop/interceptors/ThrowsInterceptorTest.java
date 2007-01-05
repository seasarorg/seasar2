/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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
package org.seasar.framework.aop.interceptors;

import junit.framework.TestCase;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.seasar.framework.aop.Aspect;
import org.seasar.framework.aop.impl.AspectImpl;
import org.seasar.framework.aop.proxy.AopProxy;
import org.seasar.framework.beans.MethodNotFoundRuntimeException;

/**
 * @author higa
 * 
 */
public class ThrowsInterceptorTest extends TestCase {

    public void testHandleThrowable() throws Throwable {
        MethodInterceptor interceptor = new MyThrowsInterceptor();
        Aspect aspect = new AspectImpl(interceptor);
        AopProxy aopProxy = new AopProxy(ThrowerImpl.class,
                new Aspect[] { aspect });
        Thrower proxy = (Thrower) aopProxy.create();
        assertEquals("1", RuntimeException.class.getName(), proxy
                .throwRuntimeException());
        try {
            proxy.throwThrowable();
            fail("2");
        } catch (Throwable t) {
            assertEquals("1", "hoge", t.getMessage());
        }
    }

    public void testHandleThrowable2() throws Throwable {
        MethodInterceptor interceptor = new MyThrowsInterceptor2();
        Aspect aspect = new AspectImpl(interceptor);
        AopProxy aopProxy = new AopProxy(ThrowerImpl.class,
                new Aspect[] { aspect });
        Thrower proxy = (Thrower) aopProxy.create();
        try {
            proxy.throwException();
            fail("1");
        } catch (Exception ex) {
            assertEquals("1", "hoge", ex.getMessage());
        }
    }

    public void testHandleThrowable3() throws Throwable {
        MethodInterceptor interceptor = new MyThrowsInterceptor3();
        Aspect aspect = new AspectImpl(interceptor);
        AopProxy aopProxy = new AopProxy(ThrowerImpl.class,
                new Aspect[] { aspect });
        Thrower proxy = (Thrower) aopProxy.create();
        assertEquals("1", "aaa", proxy.throwException());
    }

    public void testHandleThrowable4() throws Throwable {
        try {
            new MyThrowsInterceptor4();
            fail("1");
        } catch (MethodNotFoundRuntimeException ex) {
            System.out.println(ex);
        }
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

    public interface Thrower {

        public Object throwThrowable() throws Throwable;

        public Object throwException() throws Throwable;

        public Object throwRuntimeException() throws Throwable;
    }

    public static class ThrowerImpl implements Thrower {

        private int num_;

        public Object throwThrowable() throws Throwable {
            throw new Throwable("hoge");
        }

        public Object throwException() throws Throwable {
            if (num_ == 0) {
                num_++;
                throw new Exception("hoge");
            }
            return "aaa";
        }

        public Object throwRuntimeException() throws Throwable {
            throw new RuntimeException("hoge");
        }
    }

    public class MyThrowsInterceptor extends ThrowsInterceptor {

        private static final long serialVersionUID = 850322067660303954L;

        public String handleThrowable(Exception ex, MethodInvocation invocation) {
            return ex.getClass().getName();
        }
    }

    public class MyThrowsInterceptor2 extends ThrowsInterceptor {

        private static final long serialVersionUID = -2523692002595965341L;

        public String handleThrowable(Exception ex, MethodInvocation invocation)
                throws Throwable {

            System.out.println("handleThrowable");
            throw ex;
        }
    }

    public class MyThrowsInterceptor3 extends ThrowsInterceptor {

        private static final long serialVersionUID = -5725852748409700279L;

        public String handleThrowable(Exception ex, MethodInvocation invocation)
                throws Throwable {

            return (String) invocation.proceed();
        }
    }

    public class MyThrowsInterceptor4 extends ThrowsInterceptor {
        private static final long serialVersionUID = 2583097886643107941L;
    }
}