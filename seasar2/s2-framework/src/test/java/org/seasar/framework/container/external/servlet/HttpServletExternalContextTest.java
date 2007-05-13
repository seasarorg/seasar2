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
package org.seasar.framework.container.external.servlet;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import org.seasar.framework.mock.servlet.MockHttpServletRequest;
import org.seasar.framework.mock.servlet.MockServletContext;
import org.seasar.framework.mock.servlet.MockServletContextImpl;
import org.seasar.framework.unit.S2FrameworkTestCase;

/**
 * @author higa
 */
public class HttpServletExternalContextTest extends S2FrameworkTestCase {

    /**
     * 
     */
    public void testGetRequestCookieMap() {
        MockHttpServletRequest request = getRequest();
        Cookie cookie = new Cookie("a", "A");
        request.addCookie(cookie);
        HttpServletExternalContext context = new HttpServletExternalContext();
        context.setRequest(request);
        Map map = context.getRequestCookieMap();
        assertTrue(map.containsKey("a"));
        assertTrue(map.containsValue("A"));
        assertFalse(map.isEmpty());
        assertEquals(1, map.size());
    }
    
    /**
     * 
     */
    public void testGetSessionMap() {
        MockHttpServletRequest request = getRequest();
        HttpSession session = request.getSession();
        session.setAttribute("aaa", "bbb");
        HttpServletExternalContext context = new HttpServletExternalContext();
        context.setRequest(request);
        Map map = context.getSessionMap();
        assertEquals("bbb", map.get("aaa"));
        assertEquals(1, map.size());
        map.remove("aaa");
        assertNull(map.get("aaa"));
        map.put("bbb", "ccc");
        assertEquals("ccc", session.getAttribute("bbb"));
    }
    
    /**
     * 
     */
    public void testGetApplicationMap() {
        ServletContext context = getServletContext();
        context.setAttribute("a", "A");
        HttpServletExternalContext extContext = new HttpServletExternalContext();
        extContext.setApplication(context);
        Map map = extContext.getApplicationMap();
        assertEquals("A", map.get("a"));
        assertEquals(1, map.size());
        map.put("a", "B");
        assertEquals("B", map.get("a"));
        
        map.clear();
        assertNull(map.get("a"));

        context.setAttribute("b", "B");
        assertEquals("B", map.get("b"));

        context.removeAttribute("b");
        assertNull(map.get("b"));
    }
    
    /**
     * 
     */
    public void testGetInitParameterMap() {
        MockServletContext context = new MockServletContextImpl(null);
        context.setInitParameter("a", "A");
        HttpServletExternalContext extContext = new HttpServletExternalContext();
        extContext.setApplication(context);
        Map map = extContext.getInitParameterMap();
        assertEquals("A", map.get("a"));
        assertEquals(1, map.size());
        try {
            map.put("b", "B");
            fail();
        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            map.remove("b");
            fail();
        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            map.clear();
            fail();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    /**
     * 
     */
    public void testGetRequestHeaderMap() {
        MockHttpServletRequest request = getRequest();
        request.addHeader("a", "A");
        HttpServletExternalContext extContext = new HttpServletExternalContext();
        extContext.setRequest(request);
        Map map = extContext.getRequestHeaderMap();
        assertEquals("A", map.get("a"));
        assertEquals(1, map.size());
        try {
            map.put("b", "B");
            fail();
        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            map.remove("b");
            fail();
        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            map.clear();
            fail();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    /**
     * 
     */
    public void testGetRequestHeaderValuesMap() {
        MockHttpServletRequest request = getRequest();
        request.addHeader("a", "A");
        request.addHeader("a", "B");
        HttpServletExternalContext extContext = new HttpServletExternalContext();
        extContext.setRequest(request);
        Map map = extContext.getRequestHeaderValuesMap();
        Object o = map.get("a");
        assertNotNull(o);
        assertTrue(o instanceof String[]);
        String[] strs = (String[]) o;
        assertEquals(2, strs.length);
        assertEquals("A", strs[0]);
        assertEquals("B", strs[1]);
    }
    
    /**
     * 
     */
    public void testGetRequestMap() {
        MockHttpServletRequest request = getRequest();
        request.setAttribute("hoge", "foo");
        HttpServletExternalContext extContext = new HttpServletExternalContext();
        extContext.setRequest(request);
        Map map = extContext.getRequestMap();
        assertEquals("foo", map.get("hoge"));
        
        map.put("bar", "baz");
        assertEquals("baz", request.getAttribute("bar"));

        map.remove("baz");
        assertNull(map.get("baz"));
    }
    
    /**
     * 
     */
    public void testGetRequestParameterMap() {
        MockHttpServletRequest request = getRequest();
        request.setParameter("a", "A");
        HttpServletExternalContext extContext = new HttpServletExternalContext();
        extContext.setRequest(request);
        Map map = extContext.getRequestParameterMap();
        assertEquals("A", map.get("a"));
        assertEquals(1, map.size());
        try {
            map.put("b", "B");
            fail();
        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            map.remove("b");
            fail();
        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            map.clear();
            fail();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    /**
     * 
     */
    public void testGetRequestParameterValuesMap() {
        MockHttpServletRequest request = getRequest();
        request.setParameter("a", new String[] { "A", "B" });
        HttpServletExternalContext extContext = new HttpServletExternalContext();
        extContext.setRequest(request);
        Map map = extContext.getRequestParameterValuesMap();
        Object o = map.get("a");
        assertNotNull(o);
        assertTrue(o instanceof String[]);
        String[] strs = (String[]) o;
        assertEquals(2, strs.length);
        assertEquals("A", strs[0]);
        assertEquals("B", strs[1]);
    }
}