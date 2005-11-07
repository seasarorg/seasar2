package org.seasar.framework.container.factory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import junit.framework.TestCase;

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.assembler.AutoBindingDefFactory;
import org.seasar.framework.mock.servlet.MockServletContextImpl;

/**
 * @author higa
 *
 */
public class ComponentTagHandlerTest extends TestCase {

	private static final String PATH =
		"org/seasar/framework/container/factory/ComponentTagHandlerTest.dicon";

	public void testComponent() throws Exception {
		S2Container container = S2ContainerFactory.create(PATH);
		container.init();
		assertNotNull("1", container.getComponent(List.class));
		assertNotNull("2", container.getComponent("aaa"));
		assertEquals("3", new Integer(1), container.getComponent("bbb"));
		assertEquals(
			"4",
			true,
			container.getComponent("ccc") != container.getComponent("ccc"));
		ComponentDef cd =
			container.getComponentDef("ddd");
		assertEquals(
			"5",
            AutoBindingDefFactory.NONE,
			cd.getAutoBindingDef());
		Map map = new HashMap();
		container.injectDependency(map, "eee");
		assertEquals("6", "111", map.get("aaa"));
		assertNotNull("7", container.getComponent("fff"));
		assertNotNull("8", container.getComponent("ggg"));
		MockServletContextImpl ctx = new MockServletContextImpl("s2jsf-example");
		HttpServletRequest request = ctx.createRequest("/hello.html");
		container.setRequest(request);
		assertNotNull("9", container.getComponent("hhh"));
		assertNotNull("10", container.getComponent("iii"));
	}
}
