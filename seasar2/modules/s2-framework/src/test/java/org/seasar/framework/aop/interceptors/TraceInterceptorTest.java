package org.seasar.framework.aop.interceptors;

import java.util.Date;

import junit.framework.TestCase;

import org.seasar.framework.aop.Aspect;
import org.seasar.framework.aop.Pointcut;
import org.seasar.framework.aop.interceptors.TraceInterceptor;
import org.seasar.framework.aop.impl.AspectImpl;
import org.seasar.framework.aop.impl.PointcutImpl;
import org.seasar.framework.aop.proxy.AopProxy;

/**
 * @author higa
 *
 */
public class TraceInterceptorTest extends TestCase {

	/**
	 * Constructor for InvocationImplTest.
	 * @param arg0
	 */
	public TraceInterceptorTest(String arg0) {
		super(arg0);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(TraceInterceptorTest.class);
	}

	public void testIntercept() throws Exception {
		TraceInterceptor interceptor = new TraceInterceptor();
		Pointcut pointcut =
			new PointcutImpl(new String[]{ "getTime" });
		Aspect aspect = new AspectImpl(interceptor, pointcut);
		AopProxy aopProxy =
			new AopProxy(Date.class, new Aspect[] {aspect});
		Date proxy = (Date) aopProxy.create();
		proxy.getTime();
	}
	
	public void testIntercept2() throws Exception {
		TraceInterceptor interceptor = new TraceInterceptor();
		Pointcut pointcut =
			new PointcutImpl(new String[]{ "hoge" });
		Aspect aspect = new AspectImpl(interceptor, pointcut);
		AopProxy aopProxy =
			new AopProxy(ThrowError.class, new Aspect[] {aspect});
		ThrowError proxy = (ThrowError) aopProxy.create();
		try {
			proxy.hoge();
		} catch (Throwable ignore) {
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
	
	public static class ThrowError {
		public void hoge() {
			throw new RuntimeException("hoge");
		}
	}
}