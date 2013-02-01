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
package org.seasar.framework.ejb;

import java.lang.reflect.Method;

import javax.interceptor.InvocationContext;

import junit.framework.TestCase;

import org.seasar.framework.aop.Aspect;
import org.seasar.framework.aop.impl.AspectImpl;
import org.seasar.framework.aop.proxy.AopProxy;
import org.seasar.framework.ejb.impl.AroundInvokeSupportInterceptor;

/**
 * 
 */
public class AroundInvokeSupportInterceptorTest extends TestCase {

    /**
     * 
     */
    public AroundInvokeSupportInterceptorTest() {
    }

    /**
     * @param name
     */
    public AroundInvokeSupportInterceptorTest(String name) {
        super(name);
    }

    /**
     * @throws Exception
     */
    public void test() throws Exception {
        Method method = HogeImpl.class.getMethod("around", new Class<?>[] {InvocationContext.class});
        Aspect aspect = new AspectImpl(new AroundInvokeSupportInterceptor(method));
        Hoge hoge = (Hoge) new AopProxy(HogeImpl.class, new Aspect[] { aspect }).create();
        assertEquals("1", "origin-before-foo-after", hoge.foo("origin"));
        
    }
    
    /**
     * 
     */
    public interface Hoge {
        /**
         * @param param
         * @return
         */
        String foo(String param);
    }
   
    /**
     * 
     */
    public static class HogeImpl implements Hoge {
        public String foo(String param) {
            return param + "-foo";
        }

        /**
         * @param context
         * @return
         * @throws Exception
         */
        public Object around(InvocationContext context) throws Exception {
            String param = (String) context.getParameters()[0];
            context.setParameters(new Object[] {param + "-before"});
            String result = (String) context.proceed();
            return result + "-after";
        }
    }
}
