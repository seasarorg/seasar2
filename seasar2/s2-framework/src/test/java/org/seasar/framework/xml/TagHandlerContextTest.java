package org.seasar.framework.xml;

import java.util.ArrayList;
import java.util.List;

import org.seasar.framework.xml.TagHandlerContext;

import junit.framework.TestCase;

/**
 * @author higa
 *
 */
public class TagHandlerContextTest extends TestCase {

	public void testStartElementAndEndElement() throws Exception {
		TagHandlerContext ctx = new TagHandlerContext();
		ctx.startElement("aaa");
		assertEquals("1", "/aaa", ctx.getPath());
		assertEquals("2", "/aaa[1]", ctx.getDetailPath());
		assertEquals("3", "aaa", ctx.getQName());
		
		ctx.startElement("bbb");
		assertEquals("4", "/aaa/bbb", ctx.getPath());
		assertEquals("5", "/aaa[1]/bbb[1]", ctx.getDetailPath());
		assertEquals("6", "bbb", ctx.getQName());
		
		ctx.endElement();
		assertEquals("7", "/aaa", ctx.getPath());
		assertEquals("8", "/aaa[1]", ctx.getDetailPath());
		assertEquals("9", "aaa", ctx.getQName());
		
		ctx.startElement("bbb");
		assertEquals("10", "/aaa/bbb", ctx.getPath());
		assertEquals("11", "/aaa[1]/bbb[2]", ctx.getDetailPath());
		assertEquals("12", "bbb", ctx.getQName());
	}
	
	public void testPeek() throws Exception {
		TagHandlerContext ctx = new TagHandlerContext();
		ctx.push("aaa");
		ctx.push(new ArrayList());
		ctx.push("bbb");
		assertNotNull("1", ctx.peek(List.class));
		assertNull("2", ctx.peek(Integer.class));
		assertEquals("3", "bbb", ctx.peek(String.class));
	}
}
