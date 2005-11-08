package org.seasar.framework.container.assembler;

import java.util.HashMap;

import junit.framework.TestCase;

import org.seasar.framework.beans.MethodNotFoundRuntimeException;
import org.seasar.framework.container.ArgDef;
import org.seasar.framework.container.DestroyMethodDef;
import org.seasar.framework.container.MethodAssembler;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.assembler.DefaultDestroyMethodAssembler;
import org.seasar.framework.container.impl.ArgDefImpl;
import org.seasar.framework.container.impl.ComponentDefImpl;
import org.seasar.framework.container.impl.DestroyMethodDefImpl;
import org.seasar.framework.container.impl.S2ContainerImpl;

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
		md.setExpression("#self.put('aaa', '111')");
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