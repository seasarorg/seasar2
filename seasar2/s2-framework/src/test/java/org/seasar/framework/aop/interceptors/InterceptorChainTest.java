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

import org.aopalliance.intercept.MethodInvocation;
import org.seasar.framework.unit.S2FrameworkTestCase;

public class InterceptorChainTest extends S2FrameworkTestCase {

    public void setUp() {
        include("InterceptorChainTest.dicon");
    }

    public void test() {
        Counter counter = (Counter) getComponent(Counter.class);
        assertEquals(0, counter.getCount());

        Foo foo = (Foo) getComponent(Foo.class);
        foo.foo();
        assertEquals(5, counter.getCount());
    }

    public static class Foo {

        public void foo() {
        }
    }

    public static interface Counter {

        public int getCount();

        public void increase();
    }

    public static class CounterImpl implements Counter {
        private int count_;

        public int getCount() {
            return count_;
        }

        public void increase() {
            ++count_;
        }
    }

    public static class CountInterceptor extends AbstractInterceptor {

        private static final long serialVersionUID = 4339376526738638703L;

        private int id_;

        private Counter counter_;

        public CountInterceptor(int id) {
            id_ = id;
        }

        public void setCounter(Counter counter) {
            counter_ = counter;
        }

        public Object invoke(MethodInvocation invocation) throws Throwable {
            counter_.increase();
            System.out.println("before(" + id_ + "):" + counter_.getCount());
            Object ret = invocation.proceed();
            System.out.println("after:" + id_);
            return ret;
        }
    }
}