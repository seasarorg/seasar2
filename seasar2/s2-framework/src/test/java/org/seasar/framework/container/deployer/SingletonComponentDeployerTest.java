/*
 * Copyright 2004-2011 the Seasar Foundation and the Others.
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

import java.math.BigDecimal;
import java.util.HashMap;

import junit.framework.TestCase;

import org.seasar.framework.container.ComponentDeployer;
import org.seasar.framework.container.DestroyMethodDef;
import org.seasar.framework.container.InitMethodDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.assembler.AutoBindingDefFactory;
import org.seasar.framework.container.impl.ArgDefImpl;
import org.seasar.framework.container.impl.ComponentDefImpl;
import org.seasar.framework.container.impl.DestroyMethodDefImpl;
import org.seasar.framework.container.impl.InitMethodDefImpl;
import org.seasar.framework.container.impl.PropertyDefImpl;
import org.seasar.framework.container.impl.S2ContainerImpl;

/**
 * @author higa
 * 
 */
public class SingletonComponentDeployerTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testDeployAutoAutoConstructor() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(A.class);
        container.register(cd);
        container.register(B.class);
        ComponentDeployer deployer = new SingletonComponentDeployer(cd);
        A a = (A) deployer.deploy();
        assertEquals("1", "B", a.getHogeName());
        assertSame("2", a, deployer.deploy());
    }

    /**
     * @throws Exception
     */
    public void testDeployAutoAutoConstructorAndProperty() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(A.class);
        cd.addPropertyDef(new PropertyDefImpl("aaa", new Integer(1)));
        container.register(cd);
        container.register(B.class);
        ComponentDeployer deployer = new SingletonComponentDeployer(cd);
        A a = (A) deployer.deploy();
        assertEquals("1", "B", a.getHogeName());
        assertEquals("2", 1, a.getAaa());
        assertSame("3", a, deployer.deploy());
    }

    /**
     * @throws Exception
     */
    public void testDeployAutoAutoConstructorAndProperty2() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(A2.class);
        cd.addPropertyDef(new PropertyDefImpl("aaa", new Integer(1)));
        container.register(cd);
        container.register(B.class);
        ComponentDeployer deployer = new SingletonComponentDeployer(cd);
        A2 a2 = (A2) deployer.deploy();
        assertEquals("1", "B", a2.getHogeName());
        assertEquals("2", 1, a2.getAaa());
    }

    /**
     * @throws Exception
     */
    public void testDeployAutoAutoProperty() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(A2.class);
        container.register(cd);
        container.register(B.class);
        ComponentDeployer deployer = new SingletonComponentDeployer(cd);
        A2 a2 = (A2) deployer.deploy();
        assertEquals("1", "B", a2.getHogeName());
    }

    /**
     * @throws Exception
     */
    public void testDeployAutoManualConstructor() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(BigDecimal.class);
        cd.addArgDef(new ArgDefImpl("123"));
        container.register(cd);
        ComponentDeployer deployer = new SingletonComponentDeployer(cd);
        assertEquals("1", new BigDecimal(123), deployer.deploy());
    }

    /**
     * @throws Exception
     */
    public void testDeployAutoManualProperty() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(A2.class);
        cd.addPropertyDef(new PropertyDefImpl("hoge", new B()));
        container.register(cd);
        ComponentDeployer deployer = new SingletonComponentDeployer(cd);
        A2 a2 = (A2) deployer.deploy();
        assertEquals("1", "B", a2.getHogeName());
    }

    /**
     * @throws Exception
     */
    public void testDeployAutoManual() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(D.class);
        cd.addArgDef(new ArgDefImpl("abc"));
        cd.addPropertyDef(new PropertyDefImpl("aaa", new Integer(1)));
        container.register(cd);
        ComponentDeployer deployer = new SingletonComponentDeployer(cd);
        D d = (D) deployer.deploy();
        assertEquals("1", "abc", d.getName());
        assertEquals("2", 1, d.getAaa());
    }

    /**
     * @throws Exception
     */
    public void testGetComponentForInitMethodDef() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(HashMap.class);
        InitMethodDef md = new InitMethodDefImpl("put");
        md.addArgDef(new ArgDefImpl("aaa"));
        md.addArgDef(new ArgDefImpl("hoge"));
        cd.addInitMethodDef(md);
        container.register(cd);
        ComponentDeployer deployer = new SingletonComponentDeployer(cd);
        HashMap myMap = (HashMap) deployer.deploy();
        assertEquals("1", "hoge", myMap.get("aaa"));
    }

    /**
     * @throws Exception
     */
    public void testCyclicReference() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(A2.class);
        ComponentDefImpl cd2 = new ComponentDefImpl(C.class);
        container.register(cd);
        container.register(cd2);
        ComponentDeployer deployer = new SingletonComponentDeployer(cd);
        ComponentDeployer deployer2 = new SingletonComponentDeployer(cd2);
        A2 a2 = (A2) deployer.deploy();
        C c = (C) deployer2.deploy();
        assertEquals("1", "C", a2.getHogeName());
        assertEquals("2", "C", c.getHogeName());
    }

    /**
     * @throws Exception
     */
    public void testCyclicReferenceFail() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(A2.class);
        ComponentDefImpl cd2 = new ComponentDefImpl(C2.class);
        container.register(cd);
        container.register(cd2);
        ComponentDeployer deployer = new SingletonComponentDeployer(cd);
        ComponentDeployer deployer2 = new SingletonComponentDeployer(cd2);
        try {
            deployer.deploy();
            fail("1");
        } catch (Exception expected) {
        }
        deployer2.deploy();
    }

    /**
     * @throws Exception
     */
    public void testDeployConstructor() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(A.class);
        container.register(cd);
        container.register(B.class);
        cd.setAutoBindingDef(AutoBindingDefFactory.CONSTRUCTOR);
        ComponentDeployer deployer = new SingletonComponentDeployer(cd);
        A a = (A) deployer.deploy();
        assertEquals("1", "B", a.getHogeName());
    }

    /**
     * @throws Exception
     */
    public void testDeployProperty() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(A2.class);
        container.register(cd);
        container.register(B.class);
        cd.setAutoBindingDef(AutoBindingDefFactory.PROPERTY);
        ComponentDeployer deployer = new SingletonComponentDeployer(cd);
        A2 a2 = (A2) deployer.deploy();
        assertEquals("1", "B", a2.getHogeName());
    }

    /**
     * @throws Exception
     */
    public void testDeployNoneManualConstructor() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(BigDecimal.class);
        cd.addArgDef(new ArgDefImpl("123"));
        container.register(cd);
        cd.setAutoBindingDef(AutoBindingDefFactory.NONE);
        ComponentDeployer deployer = new SingletonComponentDeployer(cd);
        assertEquals("1", new BigDecimal(123), deployer.deploy());
    }

    /**
     * @throws Exception
     */
    public void testDeployNoneManualProperty() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(A2.class);
        cd.addPropertyDef(new PropertyDefImpl("hoge", new B()));
        container.register(cd);
        cd.setAutoBindingDef(AutoBindingDefFactory.NONE);
        ComponentDeployer deployer = new SingletonComponentDeployer(cd);
        A2 a2 = (A2) deployer.deploy();
        assertEquals("1", "B", a2.getHogeName());
    }

    /**
     * @throws Exception
     */
    public void testDeployNoneDefault() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(String.class);
        container.register(cd);
        cd.setAutoBindingDef(AutoBindingDefFactory.NONE);
        ComponentDeployer deployer = new SingletonComponentDeployer(cd);
        assertEquals("1", "", deployer.deploy());
    }

    /**
     * @throws Exception
     */
    public void testDestroy() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(HashMap.class);
        DestroyMethodDef md = new DestroyMethodDefImpl("put");
        md.addArgDef(new ArgDefImpl("aaa"));
        md.addArgDef(new ArgDefImpl("hoge"));
        cd.addDestroyMethodDef(md);
        container.register(cd);
        ComponentDeployer deployer = new SingletonComponentDeployer(cd);
        HashMap myMap = (HashMap) deployer.deploy();
        deployer.destroy();
        assertEquals("1", "hoge", myMap.get("aaa"));
    }

    /**
     * @throws Exception
     */
    public void testInjectDependency() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(HashMap.class);
        container.register(cd);
        ComponentDeployer deployer = new SingletonComponentDeployer(cd);
        try {
            deployer.injectDependency(new HashMap());
            fail("1");
        } catch (UnsupportedOperationException ex) {
            System.out.println(ex);
        }
    }

    /**
     *
     */
    public interface Foo {
        /**
         * @return
         */
        public String getHogeName();
    }

    /**
     *
     */
    public static class FooImpl implements Foo {

        public String getHogeName() {
            return "hoge";
        }
    }

    /**
     *
     */
    public static class A {

        private Hoge hoge_;

        private int aaa_;

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

        /**
         * @return
         */
        public int getAaa() {
            return aaa_;
        }

        /**
         * @param aaa
         */
        public void setAaa(int aaa) {
            aaa_ = aaa;
        }
    }

    /**
     *
     */
    public static class A2 implements Foo {

        private Hoge hoge_;

        private int aaa_;

        /**
         * @param hoge
         */
        public void setHoge(Hoge hoge) {
            hoge_ = hoge;
        }

        public String getHogeName() {
            return hoge_.getName();
        }

        /**
         * @return
         */
        public int getAaa() {
            return aaa_;
        }

        /**
         * @param aaa
         */
        public void setAaa(int aaa) {
            aaa_ = aaa;
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

        private Foo foo_;

        /**
         * @param foo
         */
        public void setFoo(Foo foo) {
            foo_ = foo;
        }

        public String getName() {
            return "C";
        }

        /**
         * @return
         */
        public String getHogeName() {
            return foo_.getHogeName();
        }
    }

    /**
     *
     */
    public static class C2 implements Hoge {
        private static boolean firstTime;

        /**
         * 
         */
        public C2() {
            if (!firstTime) {
                firstTime = true;
                throw new RuntimeException("C2");
            }
        }

        private Foo foo_;

        /**
         * @param foo
         */
        public void setFoo(Foo foo) {
            foo_ = foo;
        }

        public String getName() {
            return "C";
        }

        /**
         * @return
         */
        public String getHogeName() {
            return foo_.getHogeName();
        }
    }

    /**
     *
     */
    public static class D implements Hoge {

        private String name_;

        private int aaa_;

        /**
         * @param name
         */
        public D(String name) {
            name_ = name;
        }

        public String getName() {
            return name_;
        }

        /**
         * @return
         */
        public int getAaa() {
            return aaa_;
        }

        /**
         * @param aaa
         */
        public void setAaa(int aaa) {
            aaa_ = aaa;
        }
    }
}