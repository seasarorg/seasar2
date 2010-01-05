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
package org.seasar.framework.aop.interceptors;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.deployer.InstanceDefFactory;
import org.seasar.framework.container.impl.ComponentDefImpl;
import org.seasar.framework.container.impl.S2ContainerImpl;

/**
 * @author koichik
 */
public class PrototypeDelegateInterceptorTest extends TestCase {
    private static List list = new ArrayList();

    private S2Container container;

    public void setUp() {
        list.clear();
        S2ContainerImpl containerImpl = new S2ContainerImpl();

        ComponentDefImpl singleton = new ComponentDefImpl(Target.class,
                "singleton");
        containerImpl.register(singleton);

        ComponentDefImpl prototype = new ComponentDefImpl(Target.class,
                "prototype");
        prototype.setInstanceDef(InstanceDefFactory.PROTOTYPE);
        containerImpl.register(prototype);

        containerImpl.init();
        container = containerImpl;
    }

    /**
     * @throws Exception
     */
    public void testSingleton() throws Exception {
        PrototypeDelegateInterceptor pdi = new PrototypeDelegateInterceptor(
                container);
        pdi.setTargetName("singleton");
        Hello proxy = (Hello) pdi.createProxy(Hello.class);
        proxy.foo();
        proxy.foo();
        assertEquals("1", 2, list.size());
        assertSame("2", list.get(0), list.get(1));
    }

    /**
     * @throws Exception
     */
    public void testPrototype() throws Exception {
        PrototypeDelegateInterceptor pdi = new PrototypeDelegateInterceptor(
                container);
        pdi.setTargetName("prototype");
        Hello proxy = (Hello) pdi.createProxy(Hello.class);
        proxy.foo();
        proxy.foo();
        assertEquals("1", 2, list.size());
        assertNotSame("2", list.get(0), list.get(1));
    }

    /**
     * @throws Exception
     */
    public void testMethodName() throws Exception {
        PrototypeDelegateInterceptor pdi = new PrototypeDelegateInterceptor(
                container);
        pdi.setTargetName("singleton");
        pdi.addMethodNameMap("bar", "foo");
        Hello proxy = (Hello) pdi.createProxy(Hello.class);
        proxy.bar();
        assertEquals("1", 1, list.size());
    }

    /**
     * @author koichik
     *
     */
    public static interface Hello {
        /**
         * 
         */
        public void foo();

        /**
         * 
         */
        public void bar();
    }

    /**
     * @author koichik
     *
     */
    public static class Target {
        /**
         * 
         */
        public void foo() {
            list.add(this);
        }
    }
}