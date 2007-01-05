/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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
package org.seasar.framework.container.deployer;

import java.util.HashMap;

import junit.framework.TestCase;

import org.seasar.framework.container.ComponentDeployer;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.impl.ComponentDefImpl;
import org.seasar.framework.container.impl.S2ContainerImpl;

/**
 * @author higa
 * 
 */
public class PrototypeComponentDeployerTest extends TestCase {

    public void testDeployAutoAutoConstructor() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(A.class);
        container.register(cd);
        container.register(B.class);
        ComponentDeployer deployer = new PrototypeComponentDeployer(cd);
        A a = (A) deployer.deploy();
        assertEquals("1", "B", a.getHogeName());
        assertEquals("2", false, a == deployer.deploy());
    }

    public void testCyclicReference() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(A2.class);
        ComponentDefImpl cd2 = new ComponentDefImpl(C.class);
        container.register(cd);
        container.register(cd2);
        ComponentDeployer deployer = new PrototypeComponentDeployer(cd);
        ComponentDeployer deployer2 = new PrototypeComponentDeployer(cd2);
        A2 a2 = (A2) deployer.deploy();
        C c = (C) deployer2.deploy();
        assertEquals("1", "C", a2.getHogeName());
        assertEquals("2", "C", c.getHogeName());
    }

    public void testInjectDependency() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(HashMap.class);
        container.register(cd);
        ComponentDeployer deployer = new PrototypeComponentDeployer(cd);
        try {
            deployer.injectDependency(new HashMap());
            fail("1");
        } catch (UnsupportedOperationException ex) {
            System.out.println(ex);
        }
    }

    public interface Foo {
        public String getHogeName();
    }

    public static class A {

        private Hoge hoge_;

        public A(Hoge hoge) {
            hoge_ = hoge;
        }

        public String getHogeName() {
            return hoge_.getName();
        }
    }

    public static class A2 implements Foo {

        private Hoge hoge_;

        public void setHoge(Hoge hoge) {
            hoge_ = hoge;
        }

        public String getHogeName() {
            return hoge_.getName();
        }
    }

    public interface Hoge {

        public String getName();
    }

    public static class B implements Hoge {

        public String getName() {
            return "B";
        }
    }

    public static class C implements Hoge {

        private Foo foo_;

        public void setFoo(Foo foo) {
            foo_ = foo;
        }

        public String getName() {
            return "C";
        }

        public String getHogeName() {
            return foo_.getHogeName();
        }
    }
}