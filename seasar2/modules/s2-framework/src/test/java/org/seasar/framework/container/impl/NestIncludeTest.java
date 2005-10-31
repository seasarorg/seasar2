package org.seasar.framework.container.impl;

import junit.framework.TestCase;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.S2ContainerFactory;

/**
 * @author higa
 *  
 */
public class NestIncludeTest extends TestCase {

	private static final String PATH = "org/seasar/framework/container/impl/test3.dicon";

	public NestIncludeTest(String arg0) {
		super(arg0);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(NestIncludeTest.class);
	}

	public void testInclude() throws Exception {
		S2Container container = S2ContainerFactory.create(PATH);
		container.init();
		container.destroy();
	}
}