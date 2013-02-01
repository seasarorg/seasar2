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

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.seasar.framework.aop.interceptors.TraceInterceptor;
import org.seasar.framework.container.ArgDef;
import org.seasar.framework.container.ConstructorAssembler;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.impl.ArgDefImpl;
import org.seasar.framework.container.impl.AspectDefImpl;
import org.seasar.framework.container.impl.ComponentDefImpl;
import org.seasar.framework.container.impl.S2ContainerImpl;
import org.seasar.framework.container.ognl.OgnlExpression;
import org.seasar.framework.exception.OgnlRuntimeException;

/**
 * @author higa
 * 
 */
public class DefaultConstructorConstructorAssemblerTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testAssemble() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(ArrayList.class);
        container.register(cd);
        ConstructorAssembler assempbler = new DefaultConstructorConstructorAssembler(
                cd);
        assertNotNull("1", assempbler.assemble());
    }

    /**
     * @throws Exception
     */
    public void testAssembleAspect() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(ArrayList.class);
        cd.addAspectDef(new AspectDefImpl(new TraceInterceptor()));
        container.register(cd);
        ConstructorAssembler assempbler = new DefaultConstructorConstructorAssembler(
                cd);
        List list = (List) assempbler.assemble();
        list.size();
    }

    /**
     * @throws Exception
     */
    public void testAssembleManual() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(A.class);
        ArgDef argDef = new ArgDefImpl(new B());
        cd.addArgDef(argDef);
        container.register(cd);
        ConstructorAssembler assembler = new DefaultConstructorConstructorAssembler(
                cd);
        A a = (A) assembler.assemble();
        assertEquals("1", "B", a.getHogeName());
    }

    /**
     * @throws Exception
     */
    public void testAssembleIllegalConstructorArgument() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(A.class);
        ArgDef argDef = new ArgDefImpl();
        argDef.setExpression(new OgnlExpression("hoge"));
        cd.addArgDef(argDef);
        container.register(cd);
        ConstructorAssembler assembler = new DefaultConstructorConstructorAssembler(
                cd);
        try {
            assembler.assemble();
            fail("1");
        } catch (OgnlRuntimeException ex) {
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
}