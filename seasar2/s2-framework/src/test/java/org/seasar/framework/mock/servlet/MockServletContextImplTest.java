package org.seasar.framework.mock.servlet;

import java.util.Set;

import junit.framework.TestCase;

public class MockServletContextImplTest extends TestCase {

	private MockServletContextImpl context_;

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
}