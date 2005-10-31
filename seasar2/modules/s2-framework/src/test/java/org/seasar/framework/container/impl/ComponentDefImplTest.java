package org.seasar.framework.container.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

import junit.framework.TestCase;

import org.seasar.framework.aop.interceptors.TraceInterceptor;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.InitMethodDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.deployer.InstanceDefFactory;
import org.seasar.framework.container.impl.ArgDefImpl;
import org.seasar.framework.container.impl.AspectDefImpl;
import org.seasar.framework.container.impl.ComponentDefImpl;
import org.seasar.framework.container.impl.DestroyMethodDefImpl;
import org.seasar.framework.container.impl.InitMethodDefImpl;
import org.seasar.framework.container.impl.PropertyDefImpl;
import org.seasar.framework.container.impl.S2ContainerImpl;
import org.seasar.framework.hotswap.Hotswap;

/**
 * @author higa
 *
 */
public class ComponentDefImplTest extends TestCase {
	
	public void testGetComponentForType3() throws Exception {
		S2Container container = new S2ContainerImpl();
		ComponentDefImpl cd = new ComponentDefImpl(A.class);
		container.register(cd);
		container.register(B.class);
		A a = (A) container.getComponent(A.class);
		assertEquals("1", "B", a.getHogeName());
		assertSame("2", a, container.getComponent(A.class));
	}
	
	public void testGetComponentForType2() throws Exception {
		S2Container container = new S2ContainerImpl();
		ComponentDefImpl cd = new ComponentDefImpl(A2.class);
		container.register(cd);
		container.register(B.class);
		A2 a2 = (A2) container.getComponent(A2.class);
		assertEquals("1", "B", a2.getHogeName());
	}
	
	public void testGetComponentForArgDef() throws Exception {
		S2Container container = new S2ContainerImpl();
		ComponentDefImpl cd = new ComponentDefImpl(BigDecimal.class, "num");
		cd.addArgDef(new ArgDefImpl("123"));
		container.register(cd);
		assertEquals("1", new BigDecimal(123), container.getComponent("num"));
	}
	
	public void testGetComponentForProperyDef() throws Exception {
		S2Container container = new S2ContainerImpl();
		ComponentDefImpl cd = new ComponentDefImpl(A2.class);
		cd.addPropertyDef(new PropertyDefImpl("hoge", new B()));
		container.register(cd);
		A2 a2 = (A2) container.getComponent(A2.class);
		assertEquals("1", "B", a2.getHogeName());
	}
	
	public void testGetComponentForMethodDef() throws Exception {
		S2Container container = new S2ContainerImpl();
		ComponentDefImpl cd = new ComponentDefImpl(HashMap.class, "myMap");
		InitMethodDef md = new InitMethodDefImpl("put");
		md.addArgDef(new ArgDefImpl("aaa"));
		md.addArgDef(new ArgDefImpl("hoge"));
		cd.addInitMethodDef(md);
		container.register(cd);
		HashMap myMap = (HashMap) container.getComponent("myMap");
		assertEquals("1", "hoge", myMap.get("aaa"));
	}
	
	public void testGetComponentForAspectDef() throws Exception {
		S2Container container = new S2ContainerImpl();
		ComponentDefImpl cd = new ComponentDefImpl(A.class);
		cd.addAspectDef(new AspectDefImpl(new TraceInterceptor()));
		container.register(cd);
		container.register(B.class);
		A a = (A) container.getComponent(A.class);
		assertEquals("1", "B", a.getHogeName());
	}
	
	public void testGetComponentForExpression() throws Exception {
		S2Container container = new S2ContainerImpl();
		container.register(Object.class, "obj");
		ComponentDefImpl cd = new ComponentDefImpl(null, "hash");
		cd.setExpression("obj.hashCode()");
		container.register(cd);
		assertNotNull("1", container.getComponent("hash"));
	}
	
	public void testCyclicReference() throws Exception {
		S2Container container = new S2ContainerImpl();
		container.register(A2.class);
		container.register(C.class);
		A2 a2 = (A2) container.getComponent(A2.class);
		C c = (C) container.getComponent(C.class);
		assertEquals("1", "C", a2.getHogeName());
		assertEquals("1", "C", c.getHogeName());
	}
	
	public void testInit() throws Exception {
		ComponentDef cd = new ComponentDefImpl(D.class);
		cd.addInitMethodDef(new InitMethodDefImpl("init"));
		cd.init();
		D d = (D) cd.getComponent();
		assertEquals("1", true, d.isInited());
	}
	
	public void testDestroy() throws Exception {
		ComponentDef cd = new ComponentDefImpl(D.class);
		cd.addDestroyMethodDef(new DestroyMethodDefImpl("destroy"));
		D d = (D) cd.getComponent();
		cd.destroy();
		assertEquals("1", true, d.isDestroyed());
	}
    
    public void testInitForHotswap() throws Exception {
        S2Container container = new S2ContainerImpl();
        container.setHotswapMode(true);
        ComponentDefImpl cd = new ComponentDefImpl(FooImpl.class);
        container.register(cd);
        container.init();
        assertNotNull("1", cd.getHotswap());
    }
    
    public void testGetComponentClassForHotswap() throws Exception {
        S2Container container = new S2ContainerImpl();
        container.setHotswapMode(true);
        ComponentDefImpl cd = new ComponentDefImpl(FooImpl.class);
        container.register(cd);
        container.init();
        Hotswap hotswap = cd.getHotswap();
        Thread.sleep(500);
        assertSame("1", FooImpl.class, cd.getComponentClass());
        hotswap.getFile().setLastModified(new Date().getTime());
        assertNotSame("2", FooImpl.class, cd.getComponentClass());
    }
    
    public void testGetConcreteClassForHotswap() throws Exception {
        S2Container container = new S2ContainerImpl();
        container.setHotswapMode(true);
        ComponentDefImpl cd = new ComponentDefImpl(FooImpl.class);
        cd.addAspectDef(new AspectDefImpl(new TraceInterceptor()));
        container.register(cd);
        container.init();
        Class clazz = cd.getConcreteClass();
        Hotswap hotswap = cd.getHotswap();
        Thread.sleep(500);
        assertSame("1", clazz, cd.getConcreteClass());
        hotswap.getFile().setLastModified(new Date().getTime());
        assertNotSame("2", clazz, cd.getConcreteClass());
    }
    
    public void testGetComponentForHotswap() throws Exception {
        S2Container container = new S2ContainerImpl();
        container.setHotswapMode(true);
        ComponentDefImpl cd = new ComponentDefImpl(FooImpl.class);
        cd.setInstanceDef(InstanceDefFactory.PROTOTYPE);
        container.register(cd);
        container.init();
        Hotswap hotswap = cd.getHotswap();
        Thread.sleep(500);
        Foo foo = (Foo) container.getComponent(Foo.class);
        hotswap.getFile().setLastModified(new Date().getTime());
        Foo foo2 = (Foo) container.getComponent(Foo.class);
        assertNotSame("1", foo.getClass(), foo2.getClass());
        assertFalse("1", foo2 instanceof FooImpl);
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
    
	public static class FooImpl implements Foo {
                
        public String getHogeName() {
            return "hoge";
        }
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
	
	public static class D {
	
		private boolean inited_ = false;
		private boolean destroyed_ = false;
		
		public boolean isInited() {
			return inited_;
		}
		
		public boolean isDestroyed() {
			return destroyed_;
		}
		public void init() {
			inited_ = true;
		}
		
		public void destroy() {
			destroyed_ = true;
		}
	}
}