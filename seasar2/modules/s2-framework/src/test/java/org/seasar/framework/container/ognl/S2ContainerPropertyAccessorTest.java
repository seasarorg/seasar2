package org.seasar.framework.container.ognl;

import junit.framework.TestCase;
import ognl.Ognl;
import ognl.OgnlRuntime;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.impl.S2ContainerImpl;
import org.seasar.framework.container.ognl.S2ContainerPropertyAccessor;

/**
 * @author higa
 *
 */
public class S2ContainerPropertyAccessorTest extends TestCase {

	/**
	 * Constructor for InvocationImplTest.
	 * @param arg0
	 */
	public S2ContainerPropertyAccessorTest(String arg0) {
		super(arg0);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(S2ContainerPropertyAccessorTest.class);
	}
	
	protected void tearDown() throws Exception {
		OgnlRuntime.setPropertyAccessor(S2Container.class, null);
	}

	public void testGetProperty() throws Exception {
		S2Container container = new S2ContainerImpl();
		container.register("111", "aaa");
		OgnlRuntime.setPropertyAccessor(S2Container.class, new S2ContainerPropertyAccessor());
		assertEquals("1", "111", Ognl.getValue("aaa", container));
	}
}