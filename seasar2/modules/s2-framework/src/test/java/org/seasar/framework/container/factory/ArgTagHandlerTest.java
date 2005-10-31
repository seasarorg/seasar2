package org.seasar.framework.container.factory;

import junit.framework.TestCase;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.S2ContainerFactory;

/**
 * @author higa
 *
 */
public class ArgTagHandlerTest extends TestCase {

	private static final String PATH =
		"org/seasar/framework/container/factory/ArgTagHandlerTest.dicon";

	public ArgTagHandlerTest(String arg0) {
		super(arg0);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(ArgTagHandlerTest.class);
	}

	public void testArg() throws Exception {
		S2Container container = S2ContainerFactory.create(PATH);
		assertEquals(
			"1",
			new Integer(1),
			container.getComponent(Integer.class));
	}
}
