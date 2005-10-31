package org.seasar.extension.mock.servlet;

import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.seasar.extension.mock.servlet.MockHttpServletRequestImpl;
import org.seasar.extension.mock.servlet.MockServletContextImpl;

public class MockServletContextImplTest extends TestCase {

	private MockServletContextImpl context_;

	public MockServletContextImplTest(String name) {
		super(name);
	}

	public void testCreateRequest() throws Exception {
		MockHttpServletRequestImpl request = context_.createRequest("/hello.html");
		assertEquals("1", "/s2jsf-example", request.getContextPath());
		assertEquals("2", "/hello.html", request.getServletPath());

		request = context_.createRequest("/hello.html?aaa=hoge");
		assertEquals("3", "aaa=hoge", request.getQueryString());
	}
	
	public void testGetResourcePaths() throws Exception {
		Set paths = context_.getResourcePaths("/lib");
		System.out.println(paths);
		assertNotNull("1", paths);
	}

	protected void setUp() throws Exception {
		context_ = new MockServletContextImpl("/s2jsf-example");
	}

	protected void tearDown() throws Exception {
	}

	public static Test suite() {
		return new TestSuite(MockServletContextImplTest.class);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner
				.main(new String[] { MockServletContextImplTest.class.getName() });
	}
}