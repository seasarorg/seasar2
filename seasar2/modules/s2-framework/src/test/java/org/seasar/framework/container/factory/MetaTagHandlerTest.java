package org.seasar.framework.container.factory;

import junit.framework.TestCase;

import org.seasar.framework.container.MetaDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.S2ContainerFactory;

/**
 * @author higa
 *
 */
public class MetaTagHandlerTest extends TestCase {

	private static final String PATH =
		"org/seasar/framework/container/factory/MetaTagHandlerTest.dicon";
	public MetaTagHandlerTest(String arg0) {
		super(arg0);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(MetaTagHandlerTest.class);
	}

	public void testMeta() throws Exception {
		S2Container container = S2ContainerFactory.create(PATH);
		assertEquals("1", 1, container.getMetaDefSize());
		MetaDef md = container.getMetaDef("aaa");
		assertEquals("2", "111", md.getValue());
		assertNotNull("3", md.getContainer());
		assertEquals("4", 1, md.getMetaDefSize());
		MetaDef md2 = md.getMetaDef(0);
		assertEquals("5", "222", md2.getValue());
	}
}
