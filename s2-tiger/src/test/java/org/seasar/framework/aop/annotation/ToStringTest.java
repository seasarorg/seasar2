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
 * 
 */
public class ToStringTest extends TestCase {

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
        Hoge hoge = Hoge.class.cast(cd.getComponent());
        System.out.println(hoge);
    }

    /**
     * 
     */
    @Component
    public static class Hoge {

        String name = "Hoge";

        @Override
        @ToString
        public String toString() {
            return super.toString();
        }
    }

}
