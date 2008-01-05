/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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

import javax.servlet.http.HttpSession;

import org.seasar.framework.mock.servlet.MockHttpServletRequest;
import org.seasar.framework.mock.servlet.MockHttpServletRequestImpl;
import org.seasar.framework.unit.S2FrameworkTestCase;

/**
 * @author shot
 */
public class HttpSessionMapTest extends S2FrameworkTestCase {

    /**
     * 
     */
    public void testHttpSessionMap() {
        MockHttpServletRequest request = getRequest();
        HttpSession session = request.getSession();
        session.setAttribute("aaa", "bbb");
        HttpSessionMap map = new HttpSessionMap(request);
        assertEquals("bbb", map.getAttribute("aaa"));
        assertEquals("aaa", map.getAttributeNames().next());

        map.removeAttribute("aaa");
        assertNull(map.getAttribute("aaa"));

        map.setAttribute("bbb", "ccc");
        assertEquals("ccc", map.getAttribute("bbb"));

        session.setAttribute("zzz", "ZZZ");
        assertEquals("ZZZ", map.getAttribute("zzz"));

        MockHttpServletRequest request2 = new MockHttpServletRequestImpl(
                getServletContext(), "/");
        HttpSessionMap map2 = new HttpSessionMap(request2);
        assertNull(map2.getAttribute("aaa"));
        map2.setAttribute("aaa", "AAA");
        assertEquals("AAA", map2.getAttribute("aaa"));
    }
}
