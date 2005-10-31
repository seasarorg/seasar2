package org.seasar.framework.container.factory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.S2ContainerFactory;

/**
 * @author higa
 *
 */
public class InitMethodTagHandlerTest extends TestCase {

	private static final String PATH =
		"org/seasar/framework/container/factory/InitMethodTagHandlerTest.dicon";

	public InitMethodTagHandlerTest(String arg0) {
		super(arg0);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(InitMethodTagHandlerTest.class);
	}

	public void testArg() throws Exception {
		S2Container container = S2ContainerFactory.create(PATH);
		Map aaa = (HashMap) container.getComponent("aaa");
		assertEquals("1", new Integer(111), aaa.get("aaa"));
		Bbb bbb = (Bbb) container.getComponent("bbb");
		assertEquals("2", false, bbb.isEmpty());
	}
	
	public static class Bbb {
		
		private List value_;
		
		public void value(List value) {
			value_ = value;
		}
		
		public boolean isEmpty() {
			return value_ == null;
		}
	}
}
