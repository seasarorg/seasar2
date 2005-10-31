package org.seasar.framework.aop.impl;

import java.util.Date;

import junit.framework.TestCase;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.seasar.framework.aop.Aspect;
import org.seasar.framework.aop.Pointcut;
import org.seasar.framework.aop.impl.AspectImpl;
import org.seasar.framework.aop.impl.PointcutImpl;
import org.seasar.framework.aop.proxy.AopProxy;

/**
 * @author higa
 *
 */
public class MethodInvocationImplTest extends TestCase {

	/**
	 * Constructor for InvocationImplTest.
	 * @param arg0
	 */
	public MethodInvocationImplTest(String arg0) {
		super(arg0);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(MethodInvocationImplTest.class);
	}

	public void testProceed() throws Exception {
		TestInterceptor interceptor = new TestInterceptor();
		TestInterceptor interceptor2 = new TestInterceptor();
		Pointcut pointcut =
			new PointcutImpl(new String[]{ "getTime" });
		Aspect aspect = new AspectImpl(interceptor, pointcut);
		Aspect aspect2 = new AspectImpl(interceptor2, pointcut);
		AopProxy aopProxy =
			new AopProxy(Date.class, new Aspect[] { aspect, aspect2 });
		Date proxy = (Date) aopProxy.create();
		System.out.println(proxy.getTime());
		assertEquals("1", true, interceptor.invoked_);
		assertEquals("2", true, interceptor2.invoked_);
	}
	
	public void testProceedForAbstractMethod() throws Exception {
		HogeInterceptor interceptor = new HogeInterceptor();
		Aspect aspect = new AspectImpl(interceptor);
		AopProxy aopProxy =
			new AopProxy(Hoge.class, new Aspect[] { aspect });
		Hoge proxy = (Hoge) aopProxy.create();
		assertEquals("1", "Hello", proxy.foo());
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

	public class TestInterceptor implements MethodInterceptor {

		private boolean invoked_ = false;

		public Object invoke(MethodInvocation invocation) throws Throwable {
			invoked_ = true;
			return invocation.proceed();
		}

	}
	
	public interface Hoge {
		public String foo();
	}

	public static class HogeInterceptor implements MethodInterceptor {

		public Object invoke(MethodInvocation invocation) throws Throwable {
			return "Hello";
		}

	}
}
