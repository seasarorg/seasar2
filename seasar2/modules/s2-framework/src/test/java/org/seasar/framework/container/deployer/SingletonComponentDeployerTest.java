package org.seasar.framework.container.deployer;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

import junit.framework.TestCase;

import org.seasar.framework.container.ComponentDeployer;
import org.seasar.framework.container.DestroyMethodDef;
import org.seasar.framework.container.InitMethodDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.assembler.AutoBindingDefFactory;
import org.seasar.framework.container.deployer.SingletonComponentDeployer;
import org.seasar.framework.container.impl.ArgDefImpl;
import org.seasar.framework.container.impl.ComponentDefImpl;
import org.seasar.framework.container.impl.DestroyMethodDefImpl;
import org.seasar.framework.container.impl.InitMethodDefImpl;
import org.seasar.framework.container.impl.PropertyDefImpl;
import org.seasar.framework.container.impl.S2ContainerImpl;

/**
 * @author higa
 *
 */
public class SingletonComponentDeployerTest extends TestCase {

	public void testDeployAutoAutoConstructor() throws Exception {
		S2Container container = new S2ContainerImpl();
		ComponentDefImpl cd = new ComponentDefImpl(A.class);
		container.register(cd);
		container.register(B.class);
		ComponentDeployer deployer = new SingletonComponentDeployer(cd);
		A a = (A) deployer.deploy();
		assertEquals("1", "B", a.getHogeName());
		assertSame("2", a, deployer.deploy());
	}
	
	public void testDeployAutoAutoConstructorAndProperty() throws Exception {
		S2Container container = new S2ContainerImpl();
		ComponentDefImpl cd = new ComponentDefImpl(A.class);
		cd.addPropertyDef(new PropertyDefImpl("aaa", new Integer(1)));
		container.register(cd);
		container.register(B.class);
		ComponentDeployer deployer = new SingletonComponentDeployer(cd);
		A a = (A) deployer.deploy();
		assertEquals("1", "B", a.getHogeName());
		assertEquals("2", 1, a.getAaa());
		assertSame("3", a, deployer.deploy());
	}
	
	public void testDeployAutoAutoConstructorAndProperty2() throws Exception {
		S2Container container = new S2ContainerImpl();
		ComponentDefImpl cd = new ComponentDefImpl(A2.class);
		cd.addPropertyDef(new PropertyDefImpl("aaa", new Integer(1)));
		container.register(cd);
		container.register(B.class);
		ComponentDeployer deployer = new SingletonComponentDeployer(cd);
		A2 a2 = (A2) deployer.deploy();
		assertEquals("1", "B", a2.getHogeName());
		assertEquals("2", 1, a2.getAaa());
	}

	public void testDeployAutoAutoProperty() throws Exception {
		S2Container container = new S2ContainerImpl();
		ComponentDefImpl cd = new ComponentDefImpl(A2.class);
		container.register(cd);
		container.register(B.class);
		ComponentDeployer deployer = new SingletonComponentDeployer(cd);
		A2 a2 = (A2) deployer.deploy();
		assertEquals("1", "B", a2.getHogeName());
	}

	public void testDeployAutoManualConstructor() throws Exception {
		S2Container container = new S2ContainerImpl();
		ComponentDefImpl cd = new ComponentDefImpl(BigDecimal.class);
		cd.addArgDef(new ArgDefImpl("123"));
		container.register(cd);
		ComponentDeployer deployer = new SingletonComponentDeployer(cd);
		assertEquals("1", new BigDecimal(123), deployer.deploy());
	}

	public void testDeployAutoManualProperty() throws Exception {
		S2Container container = new S2ContainerImpl();
		ComponentDefImpl cd = new ComponentDefImpl(A2.class);
		cd.addPropertyDef(new PropertyDefImpl("hoge", new B()));
		container.register(cd);
		ComponentDeployer deployer = new SingletonComponentDeployer(cd);
		A2 a2 = (A2) deployer.deploy();
		assertEquals("1", "B", a2.getHogeName());
	}
	
	public void testDeployAutoManual() throws Exception {
		S2Container container = new S2ContainerImpl();
		ComponentDefImpl cd = new ComponentDefImpl(D.class);
		cd.addArgDef(new ArgDefImpl("abc"));
		cd.addPropertyDef(new PropertyDefImpl("aaa", new Integer(1)));
		container.register(cd);
		ComponentDeployer deployer = new SingletonComponentDeployer(cd);
		D d = (D) deployer.deploy();
		assertEquals("1", "abc", d.getName());
		assertEquals("2", 1, d.getAaa());
	}

	public void testGetComponentForInitMethodDef() throws Exception {
		S2Container container = new S2ContainerImpl();
		ComponentDefImpl cd = new ComponentDefImpl(HashMap.class);
		InitMethodDef md = new InitMethodDefImpl("put");
		md.addArgDef(new ArgDefImpl("aaa"));
		md.addArgDef(new ArgDefImpl("hoge"));
		cd.addInitMethodDef(md);
		container.register(cd);
		ComponentDeployer deployer = new SingletonComponentDeployer(cd);
		HashMap myMap = (HashMap) deployer.deploy();
		assertEquals("1", "hoge", myMap.get("aaa"));
	}

	public void testCyclicReference() throws Exception {
		S2Container container = new S2ContainerImpl();
		ComponentDefImpl cd = new ComponentDefImpl(A2.class);
		ComponentDefImpl cd2 = new ComponentDefImpl(C.class);
		container.register(cd);
		container.register(cd2);
		ComponentDeployer deployer = new SingletonComponentDeployer(cd);
		ComponentDeployer deployer2 = new SingletonComponentDeployer(cd2);
		A2 a2 = (A2) deployer.deploy();
		C c = (C) deployer2.deploy();
		assertEquals("1", "C", a2.getHogeName());
		assertEquals("2", "C", c.getHogeName());
	}
	
	public void testCyclicReferenceFail() throws Exception {
		S2Container container = new S2ContainerImpl();
		ComponentDefImpl cd = new ComponentDefImpl(A2.class);
		ComponentDefImpl cd2 = new ComponentDefImpl(C2.class);
		container.register(cd);
		container.register(cd2);
		ComponentDeployer deployer = new SingletonComponentDeployer(cd);
		ComponentDeployer deployer2 = new SingletonComponentDeployer(cd2);
		try {
			deployer.deploy();
			fail("1");
		} catch (Exception expected) {}
		deployer2.deploy();
	}

	public void testDeployConstructor() throws Exception {
		S2Container container = new S2ContainerImpl();
		ComponentDefImpl cd = new ComponentDefImpl(A.class);
		container.register(cd);
		container.register(B.class);
		cd.setAutoBindingDef(AutoBindingDefFactory.CONSTRUCTOR);
		ComponentDeployer deployer = new SingletonComponentDeployer(cd);
		A a = (A) deployer.deploy();
		assertEquals("1", "B", a.getHogeName());
	}
	
	public void testDeployProperty() throws Exception {
		S2Container container = new S2ContainerImpl();
		ComponentDefImpl cd = new ComponentDefImpl(A2.class);
		container.register(cd);
		container.register(B.class);
		cd.setAutoBindingDef(AutoBindingDefFactory.PROPERTY);
		ComponentDeployer deployer = new SingletonComponentDeployer(cd);
		A2 a2 = (A2) deployer.deploy();
		assertEquals("1", "B", a2.getHogeName());
	}
	
	public void testDeployNoneManualConstructor() throws Exception {
		S2Container container = new S2ContainerImpl();
		ComponentDefImpl cd = new ComponentDefImpl(BigDecimal.class);
		cd.addArgDef(new ArgDefImpl("123"));
		container.register(cd);
		cd.setAutoBindingDef(AutoBindingDefFactory.NONE);
		ComponentDeployer deployer = new SingletonComponentDeployer(cd);
		assertEquals("1", new BigDecimal(123), deployer.deploy());
	}
	
	public void testDeployNoneManualProperty() throws Exception {
		S2Container container = new S2ContainerImpl();
		ComponentDefImpl cd = new ComponentDefImpl(A2.class);
		cd.addPropertyDef(new PropertyDefImpl("hoge", new B()));
		container.register(cd);
		cd.setAutoBindingDef(AutoBindingDefFactory.NONE);
		ComponentDeployer deployer = new SingletonComponentDeployer(cd);
		A2 a2 = (A2) deployer.deploy();
		assertEquals("1", "B", a2.getHogeName());
	}
	
	public void testDeployNoneDefault() throws Exception {
		S2Container container = new S2ContainerImpl();
		ComponentDefImpl cd = new ComponentDefImpl(String.class);
		container.register(cd);
		cd.setAutoBindingDef(AutoBindingDefFactory.NONE);
		ComponentDeployer deployer = new SingletonComponentDeployer(cd);
		assertEquals("1", "", deployer.deploy());
	}
	
	public void testDestroy() throws Exception {
		S2Container container = new S2ContainerImpl();
		ComponentDefImpl cd = new ComponentDefImpl(HashMap.class);
		DestroyMethodDef md = new DestroyMethodDefImpl("put");
		md.addArgDef(new ArgDefImpl("aaa"));
		md.addArgDef(new ArgDefImpl("hoge"));
		cd.addDestroyMethodDef(md);
		container.register(cd);
		ComponentDeployer deployer = new SingletonComponentDeployer(cd);
		HashMap myMap = (HashMap) deployer.deploy();
		deployer.destroy();
		assertEquals("1", "hoge", myMap.get("aaa"));
	}
	
	public void testInjectDependency() throws Exception {
		S2Container container = new S2ContainerImpl();
		ComponentDefImpl cd = new ComponentDefImpl(HashMap.class);
		container.register(cd);
		ComponentDeployer deployer = new SingletonComponentDeployer(cd);
		try {
			deployer.injectDependency(new HashMap());
			fail("1");
		} catch (UnsupportedOperationException ex) {
			System.out.println(ex);
		}
	}
    
    public void testDeployHotswap() throws Exception {
        S2Container container = new S2ContainerImpl();
        container.setHotswapMode(true);
        ComponentDefImpl cd = new ComponentDefImpl(FooImpl.class);
        container.register(cd);
        container.init();
        ComponentDeployer deployer = cd.getComponentDeployer();
        Foo foo = (Foo) deployer.deploy();
        Foo foo2 = (Foo) deployer.deploy();
        assertSame("1", foo, foo2);
        Thread.sleep(500);
        cd.getHotswap().getFile().setLastModified(new Date().getTime());
        foo2 = (Foo) deployer.deploy();
        assertSame("2", foo, foo2);
    }

	public interface Foo {
		public String getHogeName();
	}
    
    public static class FooImpl implements Foo {
        
        public String getHogeName() {
            return "hoge";
        }
    }

	public static class A {

		private Hoge hoge_;
		private int aaa_;

		public A(Hoge hoge) {
			hoge_ = hoge;
		}

		public String getHogeName() {
			return hoge_.getName();
		}
		
		public int getAaa() {
			return aaa_;
		}

		public void setAaa(int aaa) {
			aaa_ = aaa;
		}
	}

	public static class A2 implements Foo {

		private Hoge hoge_;
		private int aaa_;

		public void setHoge(Hoge hoge) {
			hoge_ = hoge;
		}

		public String getHogeName() {
			return hoge_.getName();
		}
		
		public int getAaa() {
			return aaa_;
		}

		public void setAaa(int aaa) {
			aaa_ = aaa;
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

	public static class C2 implements Hoge {
		private static boolean firstTime;
		public C2() {
			if (!firstTime) {
				firstTime = true;
				throw new RuntimeException("C2");
			}
		}

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
	
	public static class D implements Hoge {
		
		private String name_;
		private int aaa_;
		
		public D(String name) {
			name_ = name;
		}
		
		public String getName() {
			return name_;
		}
		
		public int getAaa() {
			return aaa_;
		}
		
		public void setAaa(int aaa) {
			aaa_ = aaa;
		}
	}
}