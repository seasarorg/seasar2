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

import java.util.Date;

import junit.framework.TestCase;

import org.seasar.framework.exception.EmptyRuntimeException;

/**
 * @author higa
 * 
 */
public class DelegateInterceptorTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testInvoke() throws Exception {
        Hello target = new HelloImpl();
        DelegateInterceptor di = new DelegateInterceptor(target);
        Hello proxy = (Hello) di.createProxy(Hello.class);
        assertEquals("1", "Hello", proxy.greeting());
    }

    /**
     * @throws Exception
     */
    public void testInvoke2() throws Exception {
        Hello2 target = new Hello2Impl();
        DelegateInterceptor di = new DelegateInterceptor(target);
        di.addMethodNameMap("greeting", "greeting2");
        Hello proxy = (Hello) di.createProxy(Hello.class);
        assertEquals("1", "Hello2", proxy.greeting());
    }

    /**
     * @throws Exception
     */
    public void testInvoke3() throws Exception {
        DelegateInterceptor di = new DelegateInterceptor("hoge");
        Date proxy = (Date) di.createProxy(Date.class);
        assertTrue("1", proxy.getTime() > 0);
    }

    /**
     * @throws Exception
     */
    public void testInvoke4() throws Exception {
        DelegateInterceptor di = new DelegateInterceptor(new Date(0));
        Date proxy = (Date) di.createProxy(Date.class);
        assertEquals("1", true, proxy.getTime() != 0);
    }

    /**
     * @throws Exception
     */
    public void testNullTarget() throws Exception {
        DelegateInterceptor di = new DelegateInterceptor();
        Hello proxy = (Hello) di.createProxy(Hello.class);
        try {
            proxy.greeting();
            fail("1");
        } catch (EmptyRuntimeException ex) {
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
    public class HelloImpl implements Hello {
        public String greeting() {
            return "Hello";
        }
    }

    /**
     * @author higa
     * 
     */
    public interface Hello2 {
        /**
         * @return
         */
        public String greeting2();
    }

    /**
     * @author higa
     * 
     */
    public class Hello2Impl implements Hello2 {
        public String greeting2() {
            return "Hello2";
        }
    }
}