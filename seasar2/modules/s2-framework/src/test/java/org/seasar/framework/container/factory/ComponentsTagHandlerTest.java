package org.seasar.framework.container.factory;

import junit.framework.TestCase;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.S2ContainerFactory;

/**
 * @author higa
 *
 */
public class ComponentsTagHandlerTest extends TestCase {

	private static final String PATH =
		"org/seasar/framework/container/factory/ComponentsTagHandlerTest.dicon";
	public ComponentsTagHandlerTest(String arg0) {
		super(arg0);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(ComponentsTagHandlerTest.class);
	}

	public void testComponent() throws Exception {
		S2Container container = S2ContainerFactory.create(PATH);
		assertEquals("1", "aaa", container.getNamespace());
		assertEquals("2", "", container.getComponent("aaa.bbb"));
		assertEquals("3", "", container.getComponent("bbb"));
		assertEquals("4", PATH, container.getPath());
	}
}
