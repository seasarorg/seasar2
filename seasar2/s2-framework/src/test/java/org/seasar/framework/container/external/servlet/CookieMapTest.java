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

import java.util.Iterator;

import javax.servlet.http.Cookie;

import org.seasar.framework.container.external.servlet.CookieMap;
import org.seasar.framework.mock.servlet.MockHttpServletRequest;
import org.seasar.framework.unit.S2FrameworkTestCase;

/**
 * @author shot
 */
public class CookieMapTest extends S2FrameworkTestCase {

    public void testCookieMap() {
        MockHttpServletRequest request = getRequest();
        Cookie cookie = new Cookie("a", "A");
        request.addCookie(cookie);
        CookieMap map = new CookieMap(request);
        assertTrue(map.containsKey("a"));
        assertTrue(map.containsValue("A"));
        assertFalse(map.isEmpty());
        assertEquals(cookie, map.getAttribute("a"));
        assertEquals(1, map.size());
        for (Iterator it = map.getAttributeNames(); it.hasNext();) {
            assertEquals(cookie.getName(), it.next());
        }
    }
}
