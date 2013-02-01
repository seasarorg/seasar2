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
package org.seasar.framework.aop.annotation;

import junit.framework.TestCase;

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.deployer.InstanceDefFactory;
import org.seasar.framework.container.factory.AnnotationHandler;
import org.seasar.framework.container.factory.AnnotationHandlerFactory;
import org.seasar.framework.container.factory.S2ContainerFactory;
import org.seasar.framework.container.impl.S2ContainerImpl;

/**
 * @author koichik
 */
public class SyncTest extends TestCase {

    S2Container container;

    AnnotationHandler handler;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        container = new S2ContainerImpl();
        S2ContainerFactory.include(container, "aop.dicon");
        handler = AnnotationHandlerFactory.getAnnotationHandler();
    }

    /**
     * @throws Exception
     */
    public void test() throws Exception {
        ComponentDef cd = handler.createComponentDef(Hoge.class,
                InstanceDefFactory.SINGLETON);
        container.register(cd);
        handler.appendAspect(cd);
        cd.init();
        final Hoge hoge = Hoge.class.cast(cd.getComponent());
        Thread t1 = new Thread(new Runnable() {
            public void run() {
                hoge.foo();
            }
        });
        Thread t2 = new Thread(new Runnable() {
            public void run() {
                hoge.bar();
            }
        });

        t1.start();
        Thread.sleep(10);
        t2.start();
        t1.join();
        t2.join();
        assertEquals("foobar", hoge.buf.toString());
    }

    /**
     *
     */
    @Component
    public static class Hoge {
        StringBuffer buf = new StringBuffer();

        /**
         * 
         */
        @Sync
        public void foo() {
            try {
                buf.append("f");
                Thread.sleep(100);
                buf.append("o");
                Thread.sleep(100);
                buf.append("o");
            } catch (InterruptedException ignore) {

            }
        }

        /**
         * 
         */
        @Sync
        public void bar() {
            try {
                buf.append("b");
                Thread.sleep(100);
                buf.append("a");
                Thread.sleep(100);
                buf.append("r");
            } catch (InterruptedException ignore) {

            }
        }
    }

}
