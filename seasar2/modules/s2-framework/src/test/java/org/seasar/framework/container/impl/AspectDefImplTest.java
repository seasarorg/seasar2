package org.seasar.framework.container.impl;

import junit.framework.TestCase;

import org.seasar.framework.aop.interceptors.TraceInterceptor;
import org.seasar.framework.container.AspectDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.impl.AspectDefImpl;
import org.seasar.framework.container.impl.ComponentDefImpl;
import org.seasar.framework.container.impl.S2ContainerImpl;

/**
 * @author higa
 *
 */
public class AspectDefImplTest extends TestCase {

	public void testSetExpression() throws Exception {
		S2Container container = new S2ContainerImpl();
		AspectDef ad = new AspectDefImpl();
		ad.setExpression("traceAdvice");
		ad.setContainer(container);
		ComponentDefImpl cd =
			new ComponentDefImpl(TraceInterceptor.class, "traceAdvice");
		container.register(cd);
		assertEquals(
			"1",
			TraceInterceptor.class,
			ad.getAspect().getMethodInterceptor().getClass());
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

	public static class A2 {

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

		private A2 a2_;

		public void setA2(A2 a2) {
			a2_ = a2;
		}

		public String getName() {
			return "C";
		}

		public String getHogeName() {
			return a2_.getHogeName();
		}
	}
}