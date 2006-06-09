/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
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
package org.seasar.framework.container.impl.servlet;

import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.mock.servlet.MockHttpServletRequest;

/**
 * @author shot
 */
public class ServletRequestParameterValuesMapTest extends S2TestCase {

    public void testServletRequestParameterValuesMap() {
        MockHttpServletRequest request = getRequest();
        request.setParameter("a", new String[] { "A", "B" });
        ServletRequestParameterValuesMap map = new ServletRequestParameterValuesMap(
                request);
        Object o = map.getAttribute("a");
        assertNotNull(o);
        assertTrue(o instanceof String[]);
        String[] strs = (String[]) o;
        assertEquals(2, strs.length);

        assertEquals("a", map.getAttributeNames().nextElement());
    }
}
