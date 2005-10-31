package org.seasar.framework.container.factory;

import java.util.Date;

import junit.framework.TestCase;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.S2ContainerFactory;

/**
 * @author higa
 *  
 */
public class IncludeTagHandlerTest extends TestCase {

	private static final String PATH = "org/seasar/framework/container/factory/IncludeTagHandlerTest.dicon";

	private static final String PATH2 = "org/seasar/framework/container/factory/aaa.dicon";

	public IncludeTagHandlerTest(String arg0) {
		super(arg0);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(IncludeTagHandlerTest.class);
	}

	public void testInclude() throws Exception {
		S2Container container = S2ContainerFactory.create(PATH);
		assertEquals("1", new Date(0), container.getComponent(Date.class));
	}

	public void testInclude2() throws Exception {
		S2Container container = S2ContainerFactory.create(PATH2);
		assertSame("1", container.getComponent("aaa.cdate"), container
				.getComponent("bbb.cdate"));
	}
	
	public void testInclude3() throws Exception {
		S2Container container = S2ContainerFactory.create(PATH);
		S2Container grandChild = (S2Container) container.getComponent("grandChild");
		S2Container child = (S2Container) container.getComponent("child");
		S2Container grandChild2 = (S2Container) child.getComponent("grandChild");
		
		assertSame("1", grandChild, grandChild2);
	}
}