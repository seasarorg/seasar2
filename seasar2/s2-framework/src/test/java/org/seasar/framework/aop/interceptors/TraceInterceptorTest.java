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
package org.seasar.framework.aop.interceptors;

import java.util.Date;

import junit.framework.TestCase;

import org.seasar.framework.aop.Aspect;
import org.seasar.framework.aop.Pointcut;
import org.seasar.framework.aop.impl.AspectImpl;
import org.seasar.framework.aop.impl.PointcutImpl;
import org.seasar.framework.aop.proxy.AopProxy;

/**
 * @author higa
 * 
 */
public class TraceInterceptorTest extends TestCase {

    public void testIntercept() throws Exception {
        TraceInterceptor interceptor = new TraceInterceptor();
        Pointcut pointcut = new PointcutImpl(new String[] { "getTime" });
        Aspect aspect = new AspectImpl(interceptor, pointcut);
        AopProxy aopProxy = new AopProxy(Date.class, new Aspect[] { aspect });
        Date proxy = (Date) aopProxy.create();
        proxy.getTime();
    }

    public void testIntercept2() throws Exception {
        TraceInterceptor interceptor = new TraceInterceptor();
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

    public void testToString() throws Exception {
        assertEquals("null", TraceInterceptor.toString(null));
        assertEquals("[abc]", TraceInterceptor.toString(new Object[] { "abc" }));
        assertEquals("[abc, [1]]", TraceInterceptor.toString(new Object[] {
                "abc", new Object[] { "1" } }));
    }

    public static class ThrowError {
        public void hoge() {
            throw new RuntimeException("hoge");
        }
    }
}