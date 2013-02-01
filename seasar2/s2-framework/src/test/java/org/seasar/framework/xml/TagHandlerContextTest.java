/*
 * Copyright 2004-2013 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.framework.xml;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

/**
 * @author higa
 * 
 */
public class TagHandlerContextTest extends TestCase {

    /**
     * @throws Exception
     */
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

    /**
     * @throws Exception
     */
    public void testPeek() throws Exception {
        TagHandlerContext ctx = new TagHandlerContext();
        ctx.push("aaa");
        ctx.push(new ArrayList());
        ctx.push("bbb");
        assertNotNull("1", ctx.peek(List.class));
        assertNull("2", ctx.peek(Integer.class));
        assertEquals("3", "bbb", ctx.peek(String.class));
    }

    /**
     * 
     */
    public void testIsEmpty() {
        TagHandlerContext ctx = new TagHandlerContext();
        assertTrue("1", ctx.isEmpty());
        ctx.push("aaa");
        assertFalse("2", ctx.isEmpty());
    }
}
