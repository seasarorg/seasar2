package org.seasar.framework.container.deployer;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.seasar.framework.container.ClassUnmatchRuntimeException;
import org.seasar.framework.container.ComponentDeployer;
import org.seasar.framework.container.InitMethodDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.deployer.OuterComponentDeployer;
import org.seasar.framework.container.impl.ArgDefImpl;
import org.seasar.framework.container.impl.ComponentDefImpl;
import org.seasar.framework.container.impl.InitMethodDefImpl;
import org.seasar.framework.container.impl.S2ContainerImpl;
import org.seasar.framework.util.ArrayMap;
import org.seasar.framework.util.CaseInsensitiveMap;

/**
 * @author higa
 *  
 */
public class OuterComponentDeployerTest extends TestCase {

	/**
	 * Constructor for InvocationImplTest.
	 * 
	 * @param arg0
	 */
	public OuterComponentDeployerTest(String arg0) {
		super(arg0);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(OuterComponentDeployerTest.class);
	}

	public void testInjectDependency() throws Exception {
		ComponentDefImpl cd = new ComponentDefImpl(HashMap.class);
		InitMethodDef md = new InitMethodDefImpl("put");
		md.addArgDef(new ArgDefImpl("aaa"));
		md.addArgDef(new ArgDefImpl("hoge"));
		cd.addInitMethodDef(md);
		ComponentDeployer deployer = new OuterComponentDeployer(cd);
		HashMap myMap = new HashMap();
		deployer.injectDependency(myMap);
		assertEquals("1", "hoge", myMap.get("aaa"));
	}

	public void testInjectDependency2() throws Exception {
		ComponentDefImpl cd = new ComponentDefImpl(ArrayMap.class);
		InitMethodDef md = new InitMethodDefImpl("put");
		md.addArgDef(new ArgDefImpl("aaa"));
		md.addArgDef(new ArgDefImpl("hoge"));
		cd.addInitMethodDef(md);
		ComponentDeployer deployer = new OuterComponentDeployer(cd);
		Map myMap = new CaseInsensitiveMap();
		deployer.injectDependency(myMap);
		assertEquals("1", "hoge", myMap.get("aaa"));
		try {
			Map myMap2 = new HashMap();
			deployer.injectDependency(myMap2);
			fail("1");
		} catch (ClassUnmatchRuntimeException ex) {
			System.out.println(ex);
		}
	}

	public void testDeploy() throws Exception {
		S2Container container = new S2ContainerImpl();
		ComponentDefImpl cd = new ComponentDefImpl(HashMap.class);
		container.register(cd);
		ComponentDeployer deployer = new OuterComponentDeployer(cd);
		try {
			deployer.deploy();
			fail("1");
		} catch (UnsupportedOperationException ex) {
			System.out.println(ex);
		}
	}

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public interface Foo {
		public String getHogeName();
	}

	public static class A {

		private Hoge hoge_;

		public A(Hoge hoge) {
			hoge_ = hoge;
		}

		public String getHogeName() {
			return hoge_.getName();
		}
	}

	public static class A2 implements Foo {

		private Hoge hoge_;

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

		private Foo foo_;

		public void setFoo(Foo foo) {
			foo_ = foo;
		}

		public String getName() {
			return "C";
		}

		public String getHogeName() {
			return foo_.getHogeName();
		}
	}
}