/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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
package org.seasar.extension.dbsession;

import javax.servlet.http.Cookie;

import junit.framework.TestCase;

import org.seasar.framework.mock.servlet.MockHttpServletRequestImpl;
import org.seasar.framework.mock.servlet.MockServletContextImpl;

/**
 * @author higa
 * 
 */
public class SessionIdUtilTest extends TestCase {

    /**
     * Test method for
     * {@link org.seasar.extension.dbsession.SessionIdUtil#getSessionIdFromCookie(javax.servlet.http.HttpServletRequest)}.
     */
    public void testGetSessionIdFromCookie() {
        MockServletContextImpl context = new MockServletContextImpl("/example");
        MockHttpServletRequestImpl request = new MockHttpServletRequestImpl(
                context, "hello.html");
        assertNull(SessionIdUtil.getSessionIdFromCookie(request));
        request.addCookie(new Cookie("S2SESSIONID", "123"));
        assertEquals("123", SessionIdUtil.getSessionIdFromCookie(request));
    }

    /**
     * Test method for
     * {@link org.seasar.extension.dbsession.SessionIdUtil#getSessionIdFromURL(javax.servlet.http.HttpServletRequest)}.
     */
    public void testGetSessionIdFromURI() {
        MockServletContextImpl context = new MockServletContextImpl("/example");
        MockHttpServletRequestImpl request = new MockHttpServletRequestImpl(
                context, "hello.html;S2SESSIONID=123");
        assertEquals("123", SessionIdUtil.getSessionIdFromURL(request));
        request = new MockHttpServletRequestImpl(context,
                "hello.html;S2SESSIONID=123?aaa=111");
        assertEquals("123", SessionIdUtil.getSessionIdFromURL(request));
    }

    /**
     * Test method for
     * {@link org.seasar.extension.dbsession.SessionIdUtil#getSessionIdFromURL(javax.servlet.http.HttpServletRequest)}.
     */
    public void testGetSessionIdFromURI2() {
        MockServletContextImpl context = new MockServletContextImpl("/example");
        MockHttpServletRequestImpl request = new MockHttpServletRequestImpl(
                context, "hello.html");
        assertNull(SessionIdUtil.getSessionIdFromURL(request));
    }

    /**
     * Test method for
     * {@link org.seasar.extension.dbsession.SessionIdUtil#rewriteURL(String, javax.servlet.http.HttpServletRequest)}.
     */
    public void testRewriteURL() {
        MockServletContextImpl context = new MockServletContextImpl("/example");
        MockHttpServletRequestImpl request = new MockHttpServletRequestImpl(
                context, "hello.html");
        request.getSession(true);
        String url = SessionIdUtil.rewriteURL("/example/hello.html", request);
        System.out.println(url);
        assertTrue(url.indexOf(SessionIdUtil.SESSION_ID_KEY) >= 0);
        url = SessionIdUtil.rewriteURL("/example/hello.html?aaa=111", request);
        System.out.println(url);
        assertTrue(url.indexOf(SessionIdUtil.SESSION_ID_KEY) < url.indexOf('?'));
    }
}
