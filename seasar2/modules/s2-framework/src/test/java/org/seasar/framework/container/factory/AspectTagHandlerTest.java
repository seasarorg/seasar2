package org.seasar.framework.container.factory;

import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.S2ContainerFactory;

/**
 * @author higa
 *
 */
public class AspectTagHandlerTest extends TestCase {

	private static final String PATH =
		"org/seasar/framework/container/factory/AspectTagHandlerTest.dicon";

	public AspectTagHandlerTest(String arg0) {
		super(arg0);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(AspectTagHandlerTest.class);
	}

	public void testAspect() throws Exception {
		S2Container container = S2ContainerFactory.create(PATH);
		Date date = (Date) container.getComponent(Date.class);
		date.getTime();
		date.hashCode();
		date.toString();
		List list = (List) container.getComponent(List.class);
		list.size();
	}
}
