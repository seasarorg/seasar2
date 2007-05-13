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
package org.seasar.framework.container.external;

import java.util.Map;

import org.seasar.framework.container.ContainerConstants;
import org.seasar.framework.mock.servlet.MockHttpServletRequest;
import org.seasar.framework.unit.S2FrameworkTestCase;

/**
 * @author higa
 */
public class RequestParameterValuesMapComponentDefTest extends S2FrameworkTestCase {

    /**
     * 
     */
    public void testGetComponent() {
        MockHttpServletRequest request = getRequest();
        request.setParameter("a", new String[] { "A", "B" });
        Map map = (Map) getComponent(ContainerConstants.PARAM_VALUES);
        Object o = map.get("a");
        assertNotNull(o);
        assertTrue(o instanceof String[]);
        String[] strs = (String[]) o;
        assertEquals(2, strs.length);
        assertEquals("A", strs[0]);
        assertEquals("B", strs[1]);
    }
}
