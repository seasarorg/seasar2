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

import java.util.List;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.seasar.framework.unit.S2FrameworkTestCase;

/**
 * @author koichik
 *
 */
public class InterceptorLifecycleAdapterTest extends S2FrameworkTestCase {
    /**
     * 
     */
    protected List list;

    protected void setUp() throws Exception {
        include("InterceptorLifecycleAdapterTest.dicon");
    }

    /**
     * @throws Exception
     */
    public void test() throws Exception {
        Runnable runnable = (Runnable) getComponent(Runnable.class);
        runnable.run();
        runnable.run();

        assertEquals("1", 2, list.size());
        assertNotSame("2", list.get(0), list.get(1));
    }

    /**
     * @author koichik
     *
     */
    public static class PrototypeInterceptor implements MethodInterceptor {
        /**
         * 
         */
        protected List list;

        /**
         * @param list
         */
        public void setList(List list) {
            this.list = list;
        }

        public Object invoke(MethodInvocation invocation) throws Throwable {
            list.add(this);
            return null;
        }
    }
}
