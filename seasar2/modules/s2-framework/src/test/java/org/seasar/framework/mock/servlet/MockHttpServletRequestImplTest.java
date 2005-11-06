package org.seasar.framework.mock.servlet;

import junit.framework.TestCase;

public class MockHttpServletRequestImplTest extends TestCase {

	private MockServletContextImpl context_;
	private MockHttpServletRequestImpl request_;

	public void testAddParameter() throws Exception {
		request_.addParameter("aaa", "111");
		String[] values = request_.getParameterValues("aaa");
		assertEquals("1", 1, values.length);
		assertEquals("2", "111", values[0]);
		request_.addParameter("aaa", "222");
		values = request_.getParameterValues("aaa");
		assertEquals("3", 2, values.length);
		assertEquals("4", "111", values[0]);
		assertEquals("5", "222", values[1]);
		request_.addParameter("aaa", new String[]{"333", "444"});
		values = request_.getParameterValues("aaa");
		assertEquals("6", 4, values.length);
		assertEquals("7", "111", values[0]);
		assertEquals("8", "222", values[1]);
		assertEquals("9", "333", values[2]);
		assertEquals("10", "444", values[3]);
	}

	protected void setUp() throws Exception {
		context_ = new MockServletContextImpl("/s2jsf-example");
		request_ = context_.createRequest("/hello.html");
	}
}