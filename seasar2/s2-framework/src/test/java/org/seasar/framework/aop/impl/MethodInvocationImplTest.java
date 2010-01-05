/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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

import java.util.Date;

import junit.framework.TestCase;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.seasar.framework.aop.Aspect;
import org.seasar.framework.aop.Pointcut;
import org.seasar.framework.aop.proxy.AopProxy;

/**
 * @author higa
 * 
 */
public class MethodInvocationImplTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testProceed() throws Exception {
        TestInterceptor interceptor = new TestInterceptor();
        TestInterceptor interceptor2 = new TestInterceptor();
        Pointcut pointcut = new PointcutImpl(new String[] { "getTime" });
        Aspect aspect = new AspectImpl(interceptor, pointcut);
        Aspect aspect2 = new AspectImpl(interceptor2, pointcut);
        AopProxy aopProxy = new AopProxy(Date.class, new Aspect[] { aspect,
                aspect2 });
        Date proxy = (Date) aopProxy.create();
        System.out.println(proxy.getTime());
        assertEquals("1", true, interceptor.invoked_);
        assertEquals("2", true, interceptor2.invoked_);
    }

    /**
     * @throws Exception
     */
    public void testProceedForAbstractMethod() throws Exception {
        HogeInterceptor interceptor = new HogeInterceptor();
        Aspect aspect = new AspectImpl(interceptor);
        AopProxy aopProxy = new AopProxy(Hoge.class, new Aspect[] { aspect });
        Hoge proxy = (Hoge) aopProxy.create();
        assertEquals("1", "Hello", proxy.foo());
    }

    /**
     * @author li0934
     * 
     */
    public class TestInterceptor implements MethodInterceptor {

        private boolean invoked_ = false;

        public Object invoke(MethodInvocation invocation) throws Throwable {
            invoked_ = true;
            return invocation.proceed();
        }

    }

    /**
     * @author higa
     * 
     */
    public interface Hoge {
        /**
         * @return
         */
        public String foo();
    }

    /**
     * @author higa
     * 
     */
    public static class HogeInterceptor implements MethodInterceptor {

        public Object invoke(MethodInvocation invocation) throws Throwable {
            return "Hello";
        }

    }
}
