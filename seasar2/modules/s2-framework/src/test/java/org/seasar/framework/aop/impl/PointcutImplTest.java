package org.seasar.framework.aop.impl;

import junit.framework.TestCase;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.seasar.framework.aop.impl.PointcutImpl;
import org.seasar.framework.util.ClassUtil;

/**
 * @author higa
 *
 */
public class PointcutImplTest extends TestCase {

	/**
	 * Constructor for InvocationImplTest.
	 * @param arg0
	 */
	public PointcutImplTest(String arg0) {
		super(arg0);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(PointcutImplTest.class);
	}

	public void testGetMethodNames() throws Exception {
		PointcutImpl pointcut = new PointcutImpl(Hello2Impl.class);
		String[] methodNames = pointcut.getMethodNames();
		assertEquals("1", 2, methodNames.length);
		for (int i = 0; i < methodNames.length; ++i) {
			System.out.println(methodNames[i]);
		}
	}
	
	public void testGetMethodNames2() throws Exception {
		PointcutImpl pointcut = new PointcutImpl(Hello2.class);
		String[] methodNames = pointcut.getMethodNames();
		assertEquals("1", 2, methodNames.length);
		for (int i = 0; i < methodNames.length; ++i) {
			System.out.println(methodNames[i]);
		}
	}
	
	public void testGetMethodNames3() throws Exception {
		PointcutImpl pointcut = new PointcutImpl(Hello2Impl2.class);
		String[] methodNames = pointcut.getMethodNames();
		assertEquals("1", 2, methodNames.length);
		for (int i = 0; i < methodNames.length; ++i) {
			System.out.println(methodNames[i]);
		}
	}

	public void testRegex() throws Exception {
		PointcutImpl pointcut = new PointcutImpl(new String[] { "greeting.*" });
		assertTrue("1", pointcut.isApplied(ClassUtil.getMethod(Hello2Impl2.class, "greeting", null)));
		assertTrue("2", pointcut.isApplied(ClassUtil.getMethod(Hello2Impl2.class, "greeting2", null)));
        assertTrue("3", pointcut.isApplied(ClassUtil.getMethod(Hello2Impl2.class, "greeting2", new Class[] {String.class})));
		assertFalse("4", pointcut.isApplied(ClassUtil.getMethod(Hello2Impl2.class, "without", null)));
	}

    public void testMethod() throws Exception {
        PointcutImpl pointcut = new PointcutImpl(ClassUtil.getMethod(Hello2Impl2.class, "greeting2", null));
        assertFalse("1", pointcut.isApplied(ClassUtil.getMethod(Hello2Impl2.class, "greeting", null)));
        assertTrue("2", pointcut.isApplied(ClassUtil.getMethod(Hello2Impl2.class, "greeting2", null)));
        assertFalse("3", pointcut.isApplied(ClassUtil.getMethod(Hello2Impl2.class, "greeting2", new Class[] {String.class})));
        assertFalse("4", pointcut.isApplied(ClassUtil.getMethod(Hello2Impl2.class, "without", null)));
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

	public static class HelloImpl implements Hello {

		public String greeting() {
			return "Hello";
		}
	}

	public class HelloInterceptor implements MethodInterceptor {
		public Object invoke(MethodInvocation invocation) throws Throwable {
			return "Hello";
		}
	}

	public interface Hello2 extends Hello {
		public String greeting2();
	}

	public static class Hello2Impl extends HelloImpl implements Hello2 {

		public String greeting2() {
			return "Hello2";
		}
	}
	
	public static class Hello2Impl2 implements Hello2 {
		
		public String greeting() {
			return "Hello";
		}
		
		public String greeting2() {
			return "Hello2";
		}
        
        public String greeting2(String s) {
            return s;
        }
        
        public String without() {
            return "Without";
        }
	}
}