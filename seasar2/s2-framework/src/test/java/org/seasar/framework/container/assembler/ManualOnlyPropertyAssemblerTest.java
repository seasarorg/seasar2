/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
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
import org.seasar.framework.container.PropertyAssembler;
import org.seasar.framework.container.PropertyDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.impl.ComponentDefImpl;
import org.seasar.framework.container.impl.PropertyDefImpl;
import org.seasar.framework.container.impl.S2ContainerImpl;
import org.seasar.framework.container.ognl.OgnlExpression;
import org.seasar.framework.exception.OgnlRuntimeException;

/**
 * @author higa
 *
 */
public class ManualOnlyPropertyAssemblerTest extends TestCase {

	public void testAssemble() throws Exception {
		S2Container container = new S2ContainerImpl();
		ComponentDefImpl cd = new ComponentDefImpl(A.class);
        ComponentDefImpl cd2 = new ComponentDefImpl(B.class, "hoge");
		PropertyDef pd = new PropertyDefImpl("hoge");
        pd.setExpression(new OgnlExpression("hoge"));
		cd.addPropertyDef(pd);
		container.register(cd);
        container.register(cd2);
		PropertyAssembler assembler = new ManualOnlyPropertyAssembler(cd);
		A a = new A();
		assembler.assemble(a);
		assertEquals("1", "B", a.getHogeName());
	}
    
    public void testAssembleForField() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(C.class);
        PropertyDef pd = new PropertyDefImpl("aaa");
        pd.setAccessTypeDef(AccessTypeDefFactory.FIELD);
        pd.setExpression(new OgnlExpression("\"a\""));
        cd.addPropertyDef(pd);
        container.register(cd);
        PropertyAssembler assembler = new ManualOnlyPropertyAssembler(cd);
        C c = new C();
        assembler.assemble(c);
        assertEquals("1", "a", c.aaa);
    }
	
	public void testAssembleIllegalProperty() throws Exception {
		S2Container container = new S2ContainerImpl();
		ComponentDefImpl cd = new ComponentDefImpl(A.class);
		PropertyDef pd = new PropertyDefImpl("hoge");
		pd.setExpression(new OgnlExpression("b"));
		cd.addPropertyDef(pd);
		container.register(cd);
		PropertyAssembler assembler = new ManualOnlyPropertyAssembler(cd);
		A a = new A();
		try {
			assembler.assemble(a);
			fail("1");
		} catch (OgnlRuntimeException ex) {
			System.out.println(ex);
		}
	}
	
	public void testAssembleIllegalProperty2() throws Exception {
		S2Container container = new S2ContainerImpl();
		ComponentDefImpl cd = new ComponentDefImpl(A.class);
		PropertyDef pd = new PropertyDefImpl("abc", "111");
		cd.addPropertyDef(pd);
		container.register(cd);
		PropertyAssembler assembler = new ManualOnlyPropertyAssembler(cd);
		A a = new A();
		try {
			assembler.assemble(a);
			fail("1");
		} catch (PropertyNotFoundRuntimeException ex) {
			System.out.println(ex);
		}
	}
	
	public void testAssembleIllegalProperty3() throws Exception {
		S2Container container = new S2ContainerImpl();
		ComponentDefImpl cd = new ComponentDefImpl(B.class);
		PropertyDef pd = new PropertyDefImpl("aaa", "abc");
		cd.addPropertyDef(pd);
		container.register(cd);
		PropertyAssembler assembler = new ManualOnlyPropertyAssembler(cd);
		B b = new B();
		try {
			assembler.assemble(b);
			fail("1");
		} catch (IllegalPropertyRuntimeException ex) {
			System.out.println(ex);
		}
	}
    
    public void testAssembleWhenComponentNull() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(C.class);
        PropertyDef pd = new PropertyDefImpl("aaa");
        pd.setExpression(new OgnlExpression("\"a\""));
        cd.addPropertyDef(pd);
        container.register(cd);
        PropertyAssembler assembler = new ManualOnlyPropertyAssembler(cd);
        assembler.assemble(null);
    }

	public interface Foo {
		public String getHogeName();
	}

	public static class A implements Foo {

		private Hoge hoge_;

		public A() {
		}

		public Hoge getHoge() {
			return hoge_;
		}

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
		
		public void setAaa(int aaa) {
		}
	}
    
    public static class C {
        
        private String aaa;
    }
}