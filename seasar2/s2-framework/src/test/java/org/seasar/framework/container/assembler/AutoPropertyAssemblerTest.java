/*
 * Copyright 2004-2014 the Seasar Foundation and the Others.
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
package org.seasar.framework.container.assembler;

import java.util.Date;

import junit.framework.TestCase;

import org.seasar.framework.container.ExternalContext;
import org.seasar.framework.container.PropertyAssembler;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.external.servlet.HttpServletExternalContext;
import org.seasar.framework.container.impl.ComponentDefImpl;
import org.seasar.framework.container.impl.PropertyDefImpl;
import org.seasar.framework.container.impl.S2ContainerImpl;
import org.seasar.framework.mock.servlet.MockHttpServletRequest;
import org.seasar.framework.mock.servlet.MockServletContext;
import org.seasar.framework.mock.servlet.MockServletContextImpl;

/**
 * @author higa
 * 
 */
public class AutoPropertyAssemblerTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testAssemble() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(A.class);
        container.register(cd);
        container.register(B.class);
        PropertyAssembler assembler = new AutoPropertyAssembler(cd);
        A a = new A();
        assembler.assemble(a);
        assertEquals("1", "B", a.getHogeName());
    }

    /**
     * @throws Exception
     */
    public void testAssemble2() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(A.class);
        cd.addPropertyDef(new PropertyDefImpl("message", "aaa"));
        container.register(cd);
        container.register(B.class);
        PropertyAssembler assembler = new AutoPropertyAssembler(cd);
        A a = new A();
        assembler.assemble(a);
        assertEquals("1", "B", a.getHogeName());
        assertEquals("2", "aaa", a.getMessage());
    }

    /**
     * @throws Exception
     */
    public void testAssembleNotInterface() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(Date.class);
        container.register(cd);
        PropertyAssembler assembler = new AutoPropertyAssembler(cd);
        Date d = new Date();
        assembler.assemble(d);
    }

    /**
     * @throws Exception
     */
    public void testAssembleWhenComponentNull() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(Date.class);
        container.register(cd);
        PropertyAssembler assembler = new AutoPropertyAssembler(cd);
        assembler.assemble(null);
    }

    /**
     * @throws Exception
     */
    public void testSkipIllegalProperty() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(A.class);
        container.register(cd);
        PropertyAssembler assembler = new AutoPropertyAssembler(cd);
        A a = new A();
        assembler.assemble(a);
    }

    /**
     * @throws Exception
     */
    public void testSkipWarning() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(A2.class);
        container.register(cd);
        PropertyAssembler assembler = new AutoPropertyAssembler(cd);
        A2 a2 = new A2();
        assembler.assemble(a2);
        assertEquals("1", "B", a2.getHogeName());
    }

    /**
     * @throws Exception
     */
    public void testBindExternally() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(D.class);
        cd.setAutoBindingDef(AutoBindingDefFactory.NONE);
        cd.setExternalBinding(true);
        container.register(cd);
        ExternalContext extCtx = new HttpServletExternalContext();
        MockServletContext servletContext = new MockServletContextImpl(
                "s2-example");
        MockHttpServletRequest request = servletContext
                .createRequest("/hello.html");
        request.setParameter("name", "aaa");
        extCtx.setRequest(request);
        container.setExternalContext(extCtx);
        PropertyAssembler assembler = new AutoPropertyAssembler(cd);
        D d = new D();
        assembler.assemble(d);
        assertEquals("1", "aaa", d.getName());
    }

    /**
     * @throws Exception
     */
    public void testPublicField() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(Ggg.class);
        container.register(cd);
        container.register(Eee.class, "eee");
        PropertyAssembler assembler = new AutoPropertyAssembler(cd);
        Ggg ggg = new Ggg();
        assembler.assemble(ggg);
        assertNotNull(ggg.eee);
    }

    /**
     * @throws Exception
     */
    public void testPublicFieldBindExternally() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(Ggg.class);
        cd.setAutoBindingDef(AutoBindingDefFactory.NONE);
        cd.setExternalBinding(true);
        container.register(cd);
        ExternalContext extCtx = new HttpServletExternalContext();
        MockServletContext servletContext = new MockServletContextImpl(
                "s2-example");
        MockHttpServletRequest request = servletContext
                .createRequest("/hello.html");
        request.setParameter("name", "aaa");
        extCtx.setRequest(request);
        container.setExternalContext(extCtx);
        PropertyAssembler assembler = new AutoPropertyAssembler(cd);
        Ggg ggg = new Ggg();
        assembler.assemble(ggg);
        assertEquals("aaa", ggg.name);
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
    public static class A implements Foo {

        private Hoge hoge_;

        private String message_;

        /**
         * 
         */
        public A() {
        }

        /**
         * @return
         */
        public Hoge getHoge() {
            return hoge_;
        }

        /**
         * @param hoge
         */
        public void setHoge(Hoge hoge) {
            hoge_ = hoge;
        }

        /**
         * @return
         */
        public String getMessage() {
            return message_;
        }

        /**
         * @param message
         */
        public void setMessage(String message) {
            message_ = message;
        }

        public String getHogeName() {
            return hoge_.getName();
        }
    }

    /**
     * 
     */
    public static class A2 implements Foo {

        private Hoge hoge_ = new B();

        /**
         * @return
         */
        public Hoge getHoge() {
            return hoge_;
        }

        /**
         * @param hoge
         */
        public void setHoge(Hoge hoge) {
            hoge_ = hoge;
        }

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

        public String getName() {
            return "C";
        }
    }

    /**
     * 
     */
    public static class D {
        private String name;

        /**
         * @return
         */
        public String getName() {
            return name;
        }

        /**
         * @param name
         */
        public void setName(String name) {
            this.name = name;
        }

    }

    /**
     * 
     */
    public static class Eee {
    }

    /**
     * 
     */
    public static class Ggg {
        /**
         * 
         */
        public Eee eee;

        /**
         * 
         */
        public String name;
    }
}