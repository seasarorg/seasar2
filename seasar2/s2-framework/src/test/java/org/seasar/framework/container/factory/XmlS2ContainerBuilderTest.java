package org.seasar.framework.container.factory;

import junit.framework.TestCase;

import org.seasar.framework.container.factory.S2ContainerFactory;

/**
 * @author higa
 *
 */
public class XmlS2ContainerBuilderTest extends TestCase {

	private static final String PATH =
		"org/seasar/framework/container/factory/XmlS2ContainerBuilderTest.dicon";

	public void testCreate() throws Exception {
		S2ContainerFactory.create(PATH);
	}
}
