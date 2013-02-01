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
package org.seasar.framework.aop.proxy;

import junit.framework.TestCase;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.seasar.framework.aop.Aspect;
import org.seasar.framework.aop.Pointcut;
import org.seasar.framework.aop.impl.AspectImpl;
import org.seasar.framework.aop.impl.PointcutImpl;

/**
 * 
 */
public class S2TigerAopProxyTest extends TestCase {

    /**
     * 
     */
    public S2TigerAopProxyTest() {
    }

    /**
     * @param name
     */
    public S2TigerAopProxyTest(String name) {
        super(name);
    }

    /**
     * @throws Exception
     */
    public void testGeneric() throws Exception {
        TestInterceptor interceptor = new TestInterceptor();
        Pointcut pointcut = new PointcutImpl(new String[] { "get" });
        Aspect aspect = new AspectImpl(interceptor, pointcut);
        AopProxy aopProxy = new AopProxy(Bar.class, new Aspect[] { aspect });
        Bar generic = (Bar) aopProxy.create();
        String result = generic.get();
        assertEquals("1", "Hoge", result);
        assertTrue("2", interceptor.invoked);
    }

    /**
     * 
     * @param <T>
     */
    public static class Foo<T> {

        /**
         * @return
         */
        public T get() {
            return null;
        }
    }

    /**
     * 
     */
    public static class Bar extends Foo<String> {

        @Override
        public String get() {
            return "Hoge";
        }
    }

    /**
     * 
     */
    public class TestInterceptor implements MethodInterceptor {

        private boolean invoked = false;

        public Object invoke(MethodInvocation invocation) throws Throwable {
            invoked = true;
            return invocation.proceed();
        }

    }
}
