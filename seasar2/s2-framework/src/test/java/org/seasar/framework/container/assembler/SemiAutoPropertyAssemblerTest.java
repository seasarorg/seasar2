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
package org.seasar.framework.container.assembler;

import junit.framework.TestCase;

import org.seasar.framework.beans.IllegalPropertyRuntimeException;
import org.seasar.framework.beans.PropertyNotFoundRuntimeException;
import org.seasar.framework.container.ExternalContext;
import org.seasar.framework.container.PropertyAssembler;
import org.seasar.framework.container.PropertyDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.external.servlet.HttpServletExternalContext;
import org.seasar.framework.container.impl.ComponentDefImpl;
import org.seasar.framework.container.impl.PropertyDefImpl;
import org.seasar.framework.container.impl.S2ContainerImpl;
import org.seasar.framework.container.ognl.OgnlExpression;
import org.seasar.framework.mock.servlet.MockHttpServletRequest;
import org.seasar.framework.mock.servlet.MockServletContext;
import org.seasar.framework.mock.servlet.MockServletContextImpl;

/**
 * @author higa
 * 
 */
public class SemiAutoPropertyAssemblerTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testAssemble() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(A.class);
        ComponentDefImpl cd2 = new ComponentDefImpl(B.class, "hoge");
        PropertyDef pd = new PropertyDefImpl("hoge");
        pd.setExpression(new OgnlExpression("hoge"));
        cd.addPropertyDef(pd);
        container.register(cd);
        container.register(cd2);
        PropertyAssembler assembler = new SemiAutoPropertyAssembler(cd);
        A a = new A();
        assembler.assemble(a);
        assertEquals("1", "B", a.getHogeName());
    }

    /**
     * @throws Exception
     */
    public void testAssembleForField() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(C.class);
        PropertyDef pd = new PropertyDefImpl("aaa");
        pd.setAccessTypeDef(AccessTypeDefFactory.FIELD);
        cd.addPropertyDef(pd);
        container.register(cd);
        container.register("a", "aaa");
        PropertyAssembler assembler = new SemiAutoPropertyAssembler(cd);
        C c = new C();
        assembler.assemble(c);
        assertEquals("1", "a", c.aaa);
    }

    /**
     * @throws Exception
     */
    public void testAssembleForField2() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(D.class);
        ComponentDefImpl cd2 = new ComponentDefImpl(B.class, "hoge2");
        PropertyDef pd = new PropertyDefImpl("hoge");
        pd.setAccessTypeDef(AccessTypeDefFactory.FIELD);
        cd.addPropertyDef(pd);
        container.register(cd);
        container.register(cd2);
        PropertyAssembler assembler = new SemiAutoPropertyAssembler(cd);
        D d = new D();
        assembler.assemble(d);
        assertEquals("1", "B", d.getHogeName());
    }

    /**
     * @throws Exception
     */
    public void testAssembleIllegalProperty() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(A.class);
        PropertyDef pd = new PropertyDefImpl("hoge");
        pd.setExpression(new OgnlExpression("b"));
        cd.addPropertyDef(pd);
        container.register(cd);
        PropertyAssembler assembler = new SemiAutoPropertyAssembler(cd);
        A a = new A();
        try {
            assembler.assemble(a);
            fail("1");
        } catch (IllegalPropertyRuntimeException ex) {
            System.out.println(ex);
        }
    }

    /**
     * @throws Exception
     */
    public void testAssembleIllegalProperty2() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(A.class);
        PropertyDef pd = new PropertyDefImpl("abc", "111");
        cd.addPropertyDef(pd);
        container.register(cd);
        PropertyAssembler assembler = new SemiAutoPropertyAssembler(cd);
        A a = new A();
        try {
            assembler.assemble(a);
            fail("1");
        } catch (PropertyNotFoundRuntimeException ex) {
            System.out.println(ex);
        }
    }

    /**
     * @throws Exception
     */
    public void testAssembleIllegalProperty3() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(B.class);
        PropertyDef pd = new PropertyDefImpl("aaa", "abc");
        cd.addPropertyDef(pd);
        container.register(cd);
        PropertyAssembler assembler = new SemiAutoPropertyAssembler(cd);
        B b = new B();
        try {
            assembler.assemble(b);
            fail("1");
        } catch (IllegalPropertyRuntimeException ex) {
            System.out.println(ex);
        }
    }

    /**
     * @throws Exception
     */
    public void testAssembleWhenComponentNull() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(D.class);
        ComponentDefImpl cd2 = new ComponentDefImpl(B.class, "hoge2");
        PropertyDef pd = new PropertyDefImpl("hoge");
        cd.addPropertyDef(pd);
        container.register(cd);
        container.register(cd2);
        PropertyAssembler assembler = new SemiAutoPropertyAssembler(cd);
        assembler.assemble(null);
    }

    /**
     * @throws Exception
     */
    public void testBindExternally() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(E.class);
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
        E e = new E();
        assembler.assemble(e);
        assertEquals("1", "aaa", e.getName());
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

        /**
         * @param aaa
         */
        public void setAaa(int aaa) {
        }
    }

    /**
     *
     */
    public static class C {

        private String aaa;
    }

    /**
     *
     */
    public static class D implements Foo {

        private Hoge hoge;

        public String getHogeName() {
            return hoge.getName();
        }
    }

    /**
     *
     */
    public static class E {
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
}