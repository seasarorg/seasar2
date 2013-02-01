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
package org.seasar.framework.container.assembler;

import junit.framework.TestCase;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.seasar.framework.aop.S2MethodInvocation;
import org.seasar.framework.aop.interceptors.TraceInterceptor;
import org.seasar.framework.container.ClassUnmatchRuntimeException;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.ConstructorAssembler;
import org.seasar.framework.container.ContainerConstants;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.impl.AspectDefImpl;
import org.seasar.framework.container.impl.ComponentDefImpl;
import org.seasar.framework.container.impl.S2ContainerImpl;
import org.seasar.framework.container.ognl.OgnlExpression;
import org.seasar.framework.exception.NoSuchConstructorRuntimeException;

/**
 * @author higa
 * 
 */
public class AutoConstructorAssemblerTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testAssemble() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(A.class);
        container.register(cd);
        container.register(B.class);
        ConstructorAssembler assembler = new AutoConstructorAssembler(cd);
        A a = (A) assembler.assemble();
        assertEquals("1", "B", a.getHogeName());
    }

    /**
     * @throws Exception
     */
    public void testAssembleAspect() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(A.class);
        cd.addAspectDef(new AspectDefImpl(new TraceInterceptor()));
        container.register(cd);
        container.register(B.class);
        ConstructorAssembler assembler = new AutoConstructorAssembler(cd);
        A a = (A) assembler.assemble();
        assertEquals("1", "B", a.getHogeName());
    }

    /**
     * @throws Exception
     */
    public void testAssembleArgNotFound() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(A.class);
        container.register(cd);
        ConstructorAssembler assembler = new AutoConstructorAssembler(cd);
        A a = (A) assembler.assemble();
        assertEquals("1", null, a.getHoge());
    }

    /**
     * @throws Exception
     */
    public void testAssembleDefaultConstructor() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(String.class);
        container.register(cd);
        ConstructorAssembler assembler = new AutoConstructorAssembler(cd);
        assertEquals("1", "", assembler.assemble());
    }

    /**
     * @throws Exception
     */
    public void testAssembleDefaultConstructor2() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(Hoge.class);
        cd.addAspectDef(new AspectDefImpl(new HogeInterceptor()));
        container.register(cd);
        ConstructorAssembler assembler = new AutoConstructorAssembler(cd);
        Hoge hoge = (Hoge) assembler.assemble();
        assertEquals("1", "hoge", hoge.getName());
    }

    /**
     * @throws Exception
     */
    public void testAssembleAutoNotInterfaceConstructor() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(C.class);
        container.register(cd);
        ConstructorAssembler assembler = new AutoConstructorAssembler(cd);
        try {
            assembler.assemble();
            fail("1");
        } catch (NoSuchConstructorRuntimeException ex) {
            System.out.println(ex);
        }

    }

    /**
     * @throws Exception
     */
    public void testAccessComponentDef() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(Hoge.class);
        ComponentDefInterceptor interceptor = new ComponentDefInterceptor();
        cd.addAspectDef(new AspectDefImpl(interceptor));
        container.register(cd);
        ConstructorAssembler assembler = new AutoConstructorAssembler(cd);
        Hoge hoge = (Hoge) assembler.assemble();
        assertEquals("1", "hoge", hoge.getName());
        assertSame("2", cd, interceptor.getComponentDef());
    }

    /**
     * @throws Exception
     */
    public void testAssembleExpression() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(Object.class, "obj");
        container.register(cd);
        ComponentDefImpl cd2 = new ComponentDefImpl();
        cd2.setExpression(new OgnlExpression("obj.hashCode()"));
        container.register(cd2);
        AutoConstructorAssembler assembler = new AutoConstructorAssembler(cd2);
        Integer myInt = (Integer) assembler.assemble();
        assertNotNull("1", myInt);
    }

    /**
     * @throws Exception
     */
    public void testAssembleForClassUnmatch() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(Object.class, "obj");
        cd.setExpression(new OgnlExpression("null"));
        container.register(cd);
        AutoConstructorAssembler assembler = new AutoConstructorAssembler(cd);
        try {
            assembler.assemble();
            fail("1");
        } catch (ClassUnmatchRuntimeException ex) {
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
    public static class A implements Foo {

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
        public Hoge getHoge() {
            return hoge_;
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
    public static class C {

        private String name_;

        /**
         * @param name
         */
        public C(String name) {
            name_ = name;
        }

        /**
         * @return
         */
        public String getName() {
            return name_;
        }
    }

    /**
     *
     */
    public class HogeInterceptor implements MethodInterceptor {
        public Object invoke(MethodInvocation invocation) throws Throwable {
            return "hoge";
        }
    }

    /**
     *
     */
    public class ComponentDefInterceptor implements MethodInterceptor {

        private ComponentDef componentDef_;

        /**
         * @return
         */
        public ComponentDef getComponentDef() {
            return componentDef_;
        }

        public Object invoke(MethodInvocation invocation) throws Throwable {
            S2MethodInvocation impl = (S2MethodInvocation) invocation;
            componentDef_ = (ComponentDef) impl
                    .getParameter(ContainerConstants.COMPONENT_DEF_NAME);
            return "hoge";
        }
    }
}