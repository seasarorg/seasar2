/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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

import org.seasar.framework.mock.servlet.MockHttpServletRequest;
import org.seasar.framework.unit.S2FrameworkTestCase;

/**
 * @author shot
 */
public class ServletRequestMapTest extends S2FrameworkTestCase {

    /**
     * 
     */
    public void testServletRequestMap() {
        MockHttpServletRequest request = getRequest();
        request.setAttribute("hoge", "foo");
        ServletRequestMap map = new ServletRequestMap(request);
        assertEquals("foo", map.getAttribute("hoge"));
        assertEquals("hoge", map.getAttributeNames().next());

        map.setAttribute("bar", "baz");
        assertEquals("baz", request.getAttribute("bar"));
        assertEquals("baz", map.getAttribute("bar"));

        map.removeAttribute("baz");
        assertNull(map.getAttribute("baz"));
    }

}
