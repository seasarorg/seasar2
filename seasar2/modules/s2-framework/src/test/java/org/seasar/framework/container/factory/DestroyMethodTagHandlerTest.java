package org.seasar.framework.container.factory;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.S2ContainerFactory;

/**
 * @author higa
 *
 */
public class DestroyMethodTagHandlerTest extends TestCase {

	private static final String PATH =
		"org/seasar/framework/container/factory/DestroyMethodTagHandlerTest.dicon";

	public DestroyMethodTagHandlerTest(String arg0) {
		super(arg0);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(DestroyMethodTagHandlerTest.class);
	}

	public void testArg() throws Exception {
		S2Container container = S2ContainerFactory.create(PATH);
		container.init();
		Map map = (HashMap) container.getComponent(Map.class);
		container.destroy();
		assertEquals("1", new Integer(111), map.get("aaa"));
	}
}
