/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
package org.seasar.framework.container.impl;

import junit.framework.TestCase;

import org.seasar.framework.aop.interceptors.TraceInterceptor;
import org.seasar.framework.container.AspectDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.ognl.OgnlExpression;

/**
 * @author higa
 * 
 */
public class AspectDefImplTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testSetExpression() throws Exception {
        S2Container container = new S2ContainerImpl();
        AspectDef ad = new AspectDefImpl();
        ad.setExpression(new OgnlExpression("traceAdvice"));
        ad.setContainer(container);
        ComponentDefImpl cd = new ComponentDefImpl(TraceInterceptor.class,
                "traceAdvice");
        container.register(cd);
        assertEquals("1", TraceInterceptor.class, ad.getAspect()
                .getMethodInterceptor().getClass());
    }

    /**
     *
     */
    public static class A {

        private Hoge hoge_;

        /**
         * @param hoge
         */
        public A(Hoge hoge) {
            hoge_ = hoge;
        }

        /**
         * @return
         */
        public String getHogeName() {
            return hoge_.getName();
        }
    }

    /**
     *
     */
    public static class A2 {

        private Hoge hoge_;

        /**
         * @param hoge
         */
        public void setHoge(Hoge hoge) {
            hoge_ = hoge;
        }

        /**
         * @return
         */
        public String getHogeName() {
            return hoge_.getName();
        }
    }

    /**
     *
     */
    public interface Hoge {

        /**
         * @return
         */
        public String getName();
    }

    /**
     *
     */
    public static class B implements Hoge {

        public String getName() {
            return "B";
        }
    }

    /**
     *
     */
    public static class C implements Hoge {

        private A2 a2_;

        /**
         * @param a2
         */
        public void setA2(A2 a2) {
            a2_ = a2;
        }

        public String getName() {
            return "C";
        }

        /**
         * @return
         */
        public String getHogeName() {
            return a2_.getHogeName();
        }
    }
}