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

import java.util.HashMap;

import junit.framework.TestCase;

import org.seasar.framework.beans.MethodNotFoundRuntimeException;
import org.seasar.framework.container.ArgDef;
import org.seasar.framework.container.DestroyMethodDef;
import org.seasar.framework.container.MethodAssembler;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.impl.ArgDefImpl;
import org.seasar.framework.container.impl.ComponentDefImpl;
import org.seasar.framework.container.impl.DestroyMethodDefImpl;
import org.seasar.framework.container.impl.S2ContainerImpl;
import org.seasar.framework.container.ognl.OgnlExpression;

/**
 * @author higa
 *
 */
public class DefaultDestroyMethodAssemblerTest extends TestCase {

	public void testAssemble() throws Exception {
		S2Container container = new S2ContainerImpl();
		ComponentDefImpl cd = new ComponentDefImpl(HashMap.class);
		DestroyMethodDef md = new DestroyMethodDefImpl("put");
		ArgDef argDef = new ArgDefImpl("aaa");
		md.addArgDef(argDef);
		ArgDef argDef2 = new ArgDefImpl("111");
		md.addArgDef(argDef2);
		cd.addDestroyMethodDef(md);
		container.register(cd);
		MethodAssembler assembler = new DefaultDestroyMethodAssembler(cd);
		HashMap map = new HashMap();
		assembler.assemble(map);
		assertEquals("1", "111", map.get("aaa"));
	}
	
	public void testAssembleForExpression() throws Exception {
		S2Container container = new S2ContainerImpl();
		ComponentDefImpl cd = new ComponentDefImpl(HashMap.class);
		DestroyMethodDef md = new DestroyMethodDefImpl();
		md.setExpression(new OgnlExpression("#self.put('aaa', '111')"));
		cd.addDestroyMethodDef(md);
		container.register(cd);
		MethodAssembler assembler = new DefaultDestroyMethodAssembler(cd);
		HashMap map = new HashMap();
		assembler.assemble(map);
		assertEquals("1", "111", map.get("aaa"));
	}

	public void testAssembleIllegalArgument() throws Exception {
		S2Container container = new S2ContainerImpl();
		ComponentDefImpl cd = new ComponentDefImpl(HashMap.class);
		DestroyMethodDef md = new DestroyMethodDefImpl("put");
		cd.addDestroyMethodDef(md);
		container.register(cd);
		MethodAssembler assembler = new DefaultDestroyMethodAssembler(cd);
		HashMap map = new HashMap();
		try {
			assembler.assemble(map);
			fail("1");
		} catch (MethodNotFoundRuntimeException ex) {
			System.out.println(ex);
		}
	}
    
    public void testAssembleWhenComponentNull() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(HashMap.class);
        DestroyMethodDef md = new DestroyMethodDefImpl();
        md.setExpression(new OgnlExpression("#self.put('aaa', '111')"));
        cd.addDestroyMethodDef(md);
        container.register(cd);
        MethodAssembler assembler = new DefaultDestroyMethodAssembler(cd);
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
	}
}