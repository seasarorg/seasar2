package org.seasar.framework.container.deployer;

import javax.servlet.http.HttpServletRequest;

import junit.framework.TestCase;

import org.seasar.extension.mock.servlet.MockServletContextImpl;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.ComponentDeployer;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.deployer.SessionComponentDeployer;
import org.seasar.framework.container.impl.ComponentDefImpl;
import org.seasar.framework.container.impl.S2ContainerImpl;

/**
 * @author higa
 *  
 */
public class SessionComponentDeployerTest extends TestCase {

	/**
	 * Constructor for InvocationImplTest.
	 * 
	 * @param arg0
	 */
	public SessionComponentDeployerTest(String arg0) {
		super(arg0);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(SessionComponentDeployerTest.class);
	}

	public void testDeployAutoAutoConstructor() throws Exception {
		MockServletContextImpl ctx = new MockServletContextImpl("s2jsf-example");
		HttpServletRequest request = ctx.createRequest("/hello.html");
		S2Container container = new S2ContainerImpl();
		container.setRequest(request);
		ComponentDef cd = new ComponentDefImpl(Foo.class, "foo");
		container.register(cd);
		ComponentDeployer deployer = new SessionComponentDeployer(cd);
		Foo foo = (Foo) deployer.deploy();
		assertSame("1", foo, request.getSession().getAttribute("foo"));
		assertSame("2", foo, deployer.deploy());
	}

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public static class Foo {

		private String message_;

		public void setMessage(String message) {
			message_ = message;
		}

		public String getMessage() {
			return message_;
		}
	}
}