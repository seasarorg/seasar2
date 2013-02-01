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
package org.seasar.framework.aop.interceptors;

import junit.framework.TestCase;

import org.aopalliance.intercept.MethodInterceptor;
import org.seasar.framework.aop.Aspect;
import org.seasar.framework.aop.impl.AspectImpl;
import org.seasar.framework.aop.proxy.AopProxy;

/**
 * @author higa
 * 
 */
public class TraceThrowsInterceptorTest extends TestCase {

    /**
     * @throws Throwable
     */
    public void testHandleThrowable() throws Throwable {
        MethodInterceptor interceptor = new TraceThrowsInterceptor();
        Aspect aspect = new AspectImpl(interceptor);
        AopProxy aopProxy = new AopProxy(ThrowerImpl.class,
                new Aspect[] { aspect });
        Thrower proxy = (Thrower) aopProxy.create();
        try {
            proxy.throwThrowable();
            fail("1");
        } catch (Throwable t) {
            assertEquals("1", "hoge", t.getMessage());
        }
    }

    /**
     * 
     */
    public interface Thrower {

        /**
         * @return
         * @throws Throwable
         */
        public Object throwThrowable() throws Throwable;
    }

    /**
     * 
     */
    public static class ThrowerImpl implements Thrower {

        public Object throwThrowable() throws Throwable {
            throw new Throwable("hoge");
        }
    }
}