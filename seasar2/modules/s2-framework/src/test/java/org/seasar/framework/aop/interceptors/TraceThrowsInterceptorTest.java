package org.seasar.framework.aop.interceptors;

import junit.framework.TestCase;

import org.aopalliance.intercept.MethodInterceptor;
import org.seasar.framework.aop.Aspect;
import org.seasar.framework.aop.impl.AspectImpl;
import org.seasar.framework.aop.interceptors.TraceThrowsInterceptor;
import org.seasar.framework.aop.proxy.AopProxy;

/**
 * @author higa
 *
 */
public class TraceThrowsInterceptorTest extends TestCase {

	public void testHandleThrowable() throws Throwable {
		MethodInterceptor interceptor = new TraceThrowsInterceptor();
		Aspect aspect = new AspectImpl(interceptor);
		AopProxy aopProxy =
			new AopProxy(ThrowerImpl.class, new Aspect[] {aspect});
		Thrower proxy = (Thrower) aopProxy.create();
		try {
			proxy.throwThrowable();
			fail("1");
		} catch (Throwable t) {
			assertEquals("1", "hoge", t.getMessage());
		}
	}
	
	public interface Thrower {
		
		public Object throwThrowable() throws Throwable;
	}
	
	public static class ThrowerImpl implements Thrower {
		
		public Object throwThrowable() throws Throwable {
			throw new Throwable("hoge");
		}
	}
}