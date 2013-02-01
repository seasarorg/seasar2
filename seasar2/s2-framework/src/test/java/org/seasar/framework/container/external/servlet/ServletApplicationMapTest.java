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
package org.seasar.framework.container.external.servlet;

import javax.servlet.ServletContext;

import org.seasar.framework.unit.S2FrameworkTestCase;

/**
 * @author shot
 */
public class ServletApplicationMapTest extends S2FrameworkTestCase {

    /**
     * 
     */
    public void testServletApplicationMap() {
        ServletContext context = getServletContext();
        context.setAttribute("a", "A");
        ServletApplicationMap map = new ServletApplicationMap(context);
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

}
