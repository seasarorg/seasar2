package org.seasar.framework.aop.interceptors;

import org.aopalliance.intercept.MethodInvocation;
import org.seasar.framework.unit.S2FrameworkTestCase;

public class InterceptorChainTest extends S2FrameworkTestCase {

	public void setUp() {
		include("InterceptorChainTest.dicon");
	}

	public void test() {
		Counter counter = (Counter) getComponent(Counter.class);
		assertEquals(0, counter.getCount());

		Foo foo = (Foo) getComponent(Foo.class);
		foo.foo();
		assertEquals(5, counter.getCount());
	}

	public static class Foo {
		
		public void foo() {
		}
	}
	
	public static interface Counter {
		
		public int getCount();
		
		public void increase();
	}
	
	public static class CounterImpl implements Counter {
		private int count_;
		
		public int getCount() {
			return count_;
		}
		
		public void increase() {
			++count_;
		}
	}

	public static class CountInterceptor extends AbstractInterceptor {
		
        private static final long serialVersionUID = 4339376526738638703L;

		private int id_;
		private Counter counter_;
		
		public CountInterceptor(int id) {
			id_ = id;
		}
		
		public void setCounter(Counter counter) {
			counter_ = counter;
		}

		public Object invoke(MethodInvocation invocation) throws Throwable {
			counter_.increase();
			System.out.println("before(" + id_ + "):" + counter_.getCount());
			Object ret = invocation.proceed();
			System.out.println("after:" + id_);
			return ret;
		}
	}
}