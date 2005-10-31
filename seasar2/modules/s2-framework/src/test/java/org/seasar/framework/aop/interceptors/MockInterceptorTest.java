package org.seasar.framework.aop.interceptors;

import junit.framework.TestCase;

import org.seasar.framework.aop.Aspect;
import org.seasar.framework.aop.impl.AspectImpl;
import org.seasar.framework.aop.interceptors.MockInterceptor;
import org.seasar.framework.aop.proxy.AopProxy;

/**
 * @author higa
 *
 */
public class MockInterceptorTest extends TestCase {

	/**
	 * Constructor for InvocationImplTest.
	 * @param arg0
	 */
	public MockInterceptorTest(String arg0) {
		super(arg0);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(MockInterceptorTest.class);
	}

	public void testInvoke() throws Exception {
		MockInterceptor mi = new MockInterceptor("Hello");
		Aspect aspect = new AspectImpl(mi);
		AopProxy aopProxy =
			new AopProxy(Hello.class, new Aspect[] {aspect});
		Hello hello = (Hello) aopProxy.create();
		assertEquals("1", "Hello", hello.greeting());
	}
	
	public void testInvoke2() throws Exception {
		MockInterceptor mi = new MockInterceptor("Hello");
		Aspect aspect = new AspectImpl(mi);
		AopProxy aopProxy =
			new AopProxy(Hello2.class, new Aspect[] {aspect});
		Hello2 hello = (Hello2) aopProxy.create();
		assertEquals("1", "Hello", hello.echo("hoge"));
		assertEquals("2", true, mi.isInvoked("echo"));
		assertEquals("3", false, mi.isInvoked("greeting"));
		assertEquals("4", "hoge", mi.getArgs("echo")[0]);
	}
	
	public void testInvoke3() throws Exception {
		MockInterceptor mi = new MockInterceptor();
		mi.setReturnValue("greeting", "Hello");
		mi.setReturnValue("echo", "Hello");
		Aspect aspect = new AspectImpl(mi);
		AopProxy aopProxy =
			new AopProxy(Hello2.class, new Aspect[] {aspect});
		Hello2 hello = (Hello2) aopProxy.create();
		assertEquals("1", "Hello", hello.greeting());
		assertEquals("2", "Hello", hello.echo("hoge"));
	}
	
	public void testCreateProxy() throws Exception {
		MockInterceptor mi = new MockInterceptor("Hello");
		Hello hello = (Hello) mi.createProxy(Hello.class);
		assertEquals("1", "Hello", hello.greeting());
	}
	
	public void testThrowable() throws Exception {
		MockInterceptor mi = new MockInterceptor();
		mi.setThrowable(new NullPointerException());
		Hello hello = (Hello) mi.createProxy(Hello.class);
		try {
			hello.greeting();
			fail("1");
		} catch (NullPointerException ignore) {
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
	
	public interface Hello2 extends Hello {
		public String echo(String s);
	}
}