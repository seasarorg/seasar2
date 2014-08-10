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

import java.util.ArrayList;
import java.util.HashMap;

import junit.framework.TestCase;

import org.seasar.framework.beans.MethodNotFoundRuntimeException;
import org.seasar.framework.container.ArgDef;
import org.seasar.framework.container.IllegalMethodRuntimeException;
import org.seasar.framework.container.InitMethodDef;
import org.seasar.framework.container.MethodAssembler;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.impl.ArgDefImpl;
import org.seasar.framework.container.impl.ComponentDefImpl;
import org.seasar.framework.container.impl.InitMethodDefImpl;
import org.seasar.framework.container.impl.S2ContainerImpl;
import org.seasar.framework.container.ognl.OgnlExpression;

/**
 * @author higa
 * 
 */
public class DefaultInitMethodAssemblerTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testAssemble() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(HashMap.class);
        InitMethodDef md = new InitMethodDefImpl("put");
        ArgDef argDef = new ArgDefImpl("aaa");
        md.addArgDef(argDef);
        ArgDef argDef2 = new ArgDefImpl("111");
        md.addArgDef(argDef2);
        cd.addInitMethodDef(md);
        container.register(cd);
        MethodAssembler assembler = new DefaultInitMethodAssembler(cd);
        HashMap map = new HashMap();
        assembler.assemble(map);
        assertEquals("1", "111", map.get("aaa"));
    }

    /**
     * @throws Exception
     */
    public void testAssembleForMethod() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(HashMap.class);
        InitMethodDef md = new InitMethodDefImpl(HashMap.class.getMethod(
                "clear", null));
        cd.addInitMethodDef(md);
        container.register(cd);
        MethodAssembler assembler = new DefaultInitMethodAssembler(cd);
        HashMap map = new HashMap();
        map.put("aaa", "111");
        map.put("bbb", "222");
        assembler.assemble(map);
        assertEquals("1", 0, map.size());
    }

    /**
     * @throws Exception
     */
    public void testAssembleForExpression() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(HashMap.class);
        InitMethodDef md = new InitMethodDefImpl();
        md.setExpression(new OgnlExpression("#self.put('aaa', '111')"));
        cd.addInitMethodDef(md);
        container.register(cd);
        MethodAssembler assembler = new DefaultInitMethodAssembler(cd);
        HashMap map = new HashMap();
        assembler.assemble(map);
        assertEquals("1", "111", map.get("aaa"));
    }

    /**
     * @throws Exception
     */
    public void testAssembleForAuto() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(B.class);
        InitMethodDef md = new InitMethodDefImpl("bbb");
        cd.addInitMethodDef(md);
        container.register(cd);
        container.register(ArrayList.class);
        MethodAssembler assembler = new DefaultInitMethodAssembler(cd);
        B b = new B();
        assembler.assemble(b);
        assertTrue("1", b.isEmpty());
    }

    /**
     * @throws Exception
     */
    public void testAssembleIllegalArgument() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(HashMap.class);
        InitMethodDef md = new InitMethodDefImpl("put");
        cd.addInitMethodDef(md);
        container.register(cd);
        MethodAssembler assembler = new DefaultInitMethodAssembler(cd);
        HashMap map = new HashMap();
        try {
            assembler.assemble(map);
            fail("1");
        } catch (MethodNotFoundRuntimeException ex) {
            System.out.println(ex);
        }
    }

    /**
     * @throws Exception
     */
    public void testAssembleIllegalArgument2() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(B.class);
        InitMethodDef md = new InitMethodDefImpl("setAaa");
        ArgDef argDef = new ArgDefImpl("aaa");
        md.addArgDef(argDef);
        cd.addInitMethodDef(md);
        container.register(cd);
        MethodAssembler assembler = new DefaultInitMethodAssembler(cd);
        try {
            assembler.assemble(new B());
            fail("1");
        } catch (IllegalMethodRuntimeException ex) {
            System.out.println(ex);
        }
    }

    /**
     * @throws Exception
     */
    public void testAssembleIllegalArgument3() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(B.class);
        InitMethodDef md = new InitMethodDefImpl(B.class.getMethod("getName",
                null));
        ArgDef argDef = new ArgDefImpl("aaa");
        md.addArgDef(argDef);
        cd.addInitMethodDef(md);
        container.register(cd);
        MethodAssembler assembler = new DefaultInitMethodAssembler(cd);
        try {
            assembler.assemble(new B());
            fail("1");
        } catch (IllegalMethodRuntimeException ex) {
            System.out.println(ex);
        }
    }

    /**
     * @throws Exception
     */
    public void testAssembleField() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(Integer.class);
        InitMethodDef md = new InitMethodDefImpl();
        md
                .setExpression(new OgnlExpression(
                        "#out.println(@Integer@MIN_VALUE)"));
        cd.addInitMethodDef(md);
        container.register(cd);
        MethodAssembler assembler = new DefaultInitMethodAssembler(cd);
        assembler.assemble(new Integer(1));
    }

    /**
     * @throws Exception
     */
    public void testAssembleWhenComponentNull() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(Integer.class);
        InitMethodDef md = new InitMethodDefImpl();
        md
                .setExpression(new OgnlExpression(
                        "#out.println(@Integer@MIN_VALUE)"));
        cd.addInitMethodDef(md);
        container.register(cd);
        MethodAssembler assembler = new DefaultInitMethodAssembler(cd);
        assembler.assemble(null);
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

        private Foo foo;

        public String getName() {
            return "B";
        }

        /**
         * @param aaa
         */
        public void setAaa(int aaa) {
        }

        /**
         * @param foo
         */
        public void bbb(Foo foo) {
            this.foo = foo;
        }

        /**
         * @return
         */
        public boolean isEmpty() {
            return foo == null;
        }
    }
}