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

import java.util.Date;

import junit.framework.TestCase;

import org.seasar.framework.container.PropertyAssembler;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.assembler.AutoPropertyAssembler;
import org.seasar.framework.container.impl.ComponentDefImpl;
import org.seasar.framework.container.impl.PropertyDefImpl;
import org.seasar.framework.container.impl.S2ContainerImpl;

/**
 * @author higa
 *
 */
public class AutoPropertyAssemblerTest extends TestCase {

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

	public void testAssembleNotInterface() throws Exception {
		S2Container container = new S2ContainerImpl();
		ComponentDefImpl cd = new ComponentDefImpl(Date.class);
		container.register(cd);
		PropertyAssembler assembler = new AutoPropertyAssembler(cd);
		Date d = new Date();
		assembler.assemble(d);
	}
    
    public void testAssembleWhenComponentNull() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(Date.class);
        container.register(cd);
        PropertyAssembler assembler = new AutoPropertyAssembler(cd);
        assembler.assemble(null);
    }
	
	public void testSkipIllegalProperty() throws Exception {
		S2Container container = new S2ContainerImpl();
		ComponentDefImpl cd = new ComponentDefImpl(A.class);
		container.register(cd);
		PropertyAssembler assembler = new AutoPropertyAssembler(cd);
		A a = new A();
		assembler.assemble(a);
	}
	
	public void testSkipWarning() throws Exception {
		S2Container container = new S2ContainerImpl();
		ComponentDefImpl cd = new ComponentDefImpl(A2.class);
		container.register(cd);
		PropertyAssembler assembler = new AutoPropertyAssembler(cd);
		A2 a2 = new A2();
		assembler.assemble(a2);
		assertEquals("1", "B", a2.getHogeName());
	}

	public interface Foo {
		public String getHogeName();
	}

	public static class A implements Foo {

		private Hoge hoge_;
		private String message_;

		public A() {
		}

		public Hoge getHoge() {
			return hoge_;
		}

		public void setHoge(Hoge hoge) {
			hoge_ = hoge;
		}
		
		public String getMessage() {
			return message_;
		}
		
		public void setMessage(String message) {
			message_ = message;
		}

		public String getHogeName() {
			return hoge_.getName();
		}
	}
	
	public static class A2 implements Foo {

		private Hoge hoge_ = new B();

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
	}
	
	public static class C implements Hoge {

		public String getName() {
			return "C";
		}
	}
}