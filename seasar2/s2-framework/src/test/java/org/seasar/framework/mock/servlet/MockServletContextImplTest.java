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
package org.seasar.framework.mock.servlet;

import java.net.URL;
import java.util.Set;

import junit.framework.TestCase;

/**
 * @author higa
 * 
 */
public class MockServletContextImplTest extends TestCase {

    private MockServletContextImpl context_;

    /**
     * @throws Exception
     */
    public void testCreateRequest() throws Exception {
        MockHttpServletRequest request = context_.createRequest("/hello.html");
        assertEquals("1", "/s2jsf-example", request.getContextPath());
        assertEquals("2", "/hello.html", request.getServletPath());

        request = context_.createRequest("/hello.html?aaa=hoge");
        assertEquals("3", "aaa=hoge", request.getQueryString());
    }

    /**
     * @throws Exception
     */
    public void testGetResourcePaths() throws Exception {
        Set paths = context_.getResourcePaths("/lib");
        System.out.println(paths);
        assertNotNull(paths);
        assertFalse(paths.isEmpty());

        String path = (String) paths.iterator().next();
        assertTrue(path.startsWith("/lib/"));

        URL url = context_.getResource(path);
        System.out.println(url);
        assertNotNull(url);

        paths = context_.getResourcePaths("/WEB-INF/lib/");
        System.out.println(paths);
        assertNotNull(paths);
        assertFalse(paths.isEmpty());

        path = (String) paths.iterator().next();
        assertTrue(path.startsWith("/lib/"));

        url = context_.getResource(path);
        System.out.println(url);
        assertNotNull(url);
    }

    protected void setUp() throws Exception {
        context_ = new MockServletContextImpl("/s2jsf-example");
    }

}
