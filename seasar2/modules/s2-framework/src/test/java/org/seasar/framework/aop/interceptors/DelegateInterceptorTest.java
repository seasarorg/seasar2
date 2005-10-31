package org.seasar.framework.aop.interceptors;

import java.util.Date;

import junit.framework.TestCase;

import org.seasar.framework.aop.interceptors.DelegateInterceptor;
import org.seasar.framework.exception.EmptyRuntimeException;

/**
 * @author higa
 *  
 */
public class DelegateInterceptorTest extends TestCase {

	/**
	 * Constructor for InvocationImplTest.
	 * 
	 * @param arg0
	 */
	public DelegateInterceptorTest(String arg0) {
		super(arg0);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(DelegateInterceptorTest.class);
	}

	public void testInvoke() throws Exception {
		Hello target = new HelloImpl();
		DelegateInterceptor di = new DelegateInterceptor(target);
		Hello proxy = (Hello) di.createProxy(Hello.class);
		assertEquals("1", "Hello", proxy.greeting());
	}

	public void testInvoke2() throws Exception {
		Hello2 target = new Hello2Impl();
		DelegateInterceptor di = new DelegateInterceptor(target);
		di.addMethodNameMap("greeting", "greeting2");
		Hello proxy = (Hello) di.createProxy(Hello.class);
		assertEquals("1", "Hello2", proxy.greeting());
	}
	
	public void testInvoke3() throws Exception {
		DelegateInterceptor di = new DelegateInterceptor("hoge");
		Date proxy = (Date) di.createProxy(Date.class);
		assertTrue("1", proxy.getTime() > 0);
	}
	
	public void testInvoke4() throws Exception {
		DelegateInterceptor di = new DelegateInterceptor(new Date(0));
		Date proxy = (Date) di.createProxy(Date.class);
		assertEquals("1", true, proxy.getTime() != 0);
	}
	
	public void testNullTarget() throws Exception {
		DelegateInterceptor di = new DelegateInterceptor();
		Hello proxy = (Hello) di.createProxy(Hello.class);
		try {
			proxy.greeting();
			fail("1");
		} catch (EmptyRuntimeException ex) {
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

	public interface Hello {
		public String greeting();
	}

	public class HelloImpl implements Hello {
		public String greeting() {
			return "Hello";
		}
	}

	public interface Hello2 {
		public String greeting2();
	}

	public class Hello2Impl implements Hello2 {
		public String greeting2() {
			return "Hello2";
		}
	}
}