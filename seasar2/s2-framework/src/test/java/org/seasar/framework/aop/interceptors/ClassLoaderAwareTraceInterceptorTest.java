/*
 * Copyright 2004-2014 the Seasar Foundation and the Others.
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

import java.util.Date;

import junit.framework.TestCase;

import org.seasar.framework.aop.Aspect;
import org.seasar.framework.aop.Pointcut;
import org.seasar.framework.aop.impl.AspectImpl;
import org.seasar.framework.aop.impl.PointcutImpl;
import org.seasar.framework.aop.proxy.AopProxy;

/**
 * @author shot
 */
public class ClassLoaderAwareTraceInterceptorTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testIntercept() throws Exception {
        ClassLoaderAwareTraceInterceptor interceptor = new ClassLoaderAwareTraceInterceptor();
        Pointcut pointcut = new PointcutImpl(new String[] { "getTime" });
        Aspect aspect = new AspectImpl(interceptor, pointcut);
        AopProxy aopProxy = new AopProxy(Date.class, new Aspect[] { aspect });
        Date proxy = (Date) aopProxy.create();
        proxy.getTime();
    }

    /**
     * @throws Exception
     */
    public void testIntercept2() throws Exception {
        ClassLoaderAwareTraceInterceptor interceptor = new ClassLoaderAwareTraceInterceptor();
        Pointcut pointcut = new PointcutImpl(new String[] { "hoge" });
        Aspect aspect = new AspectImpl(interceptor, pointcut);
        AopProxy aopProxy = new AopProxy(ThrowError.class,
                new Aspect[] { aspect });
        ThrowError proxy = (ThrowError) aopProxy.create();
        try {
            proxy.hoge();
        } catch (Throwable ignore) {
        }
    }

    /**
     * @throws Exception
     */
    public void testIntercept3() throws Exception {
        ClassLoaderAwareTraceInterceptor interceptor = new ClassLoaderAwareTraceInterceptor();
        Pointcut pointcut = new PointcutImpl(new String[] { "foo" });
        Aspect aspect = new AspectImpl(interceptor, pointcut);
        AopProxy aopProxy = new AopProxy(Hoge.class, new Aspect[] { aspect });
        Hoge hoge = (Hoge) aopProxy.create();
        hoge.foo(new Foo("a"));
    }

    /**
     * @author higa
     * 
     */
    public static class ThrowError {
        /**
         * 
         */
        public void hoge() {
            throw new RuntimeException("hoge");
        }
    }

    /**
     * @author higa
     * 
     */
    public static class Hoge {
        /**
         * @param f
         * @return
         */
        public Foo foo(Foo f) {
            return new Foo(f.getS());
        }
    }

    /**
     * 
     */
    public static class Foo {
        private String s;

        /**
         * @param s
         */
        public Foo(String s) {
            this.s = s;
        }

        /**
         * @return
         */
        public String getS() {
            return s;
        }
    }
}
