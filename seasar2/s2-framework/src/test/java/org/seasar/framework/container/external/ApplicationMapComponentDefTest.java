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
package org.seasar.framework.container.external;

import java.util.Map;

import javax.servlet.ServletContext;

import org.seasar.framework.container.ContainerConstants;
import org.seasar.framework.unit.S2FrameworkTestCase;

/**
 * @author higa
 */
public class ApplicationMapComponentDefTest extends S2FrameworkTestCase {

    /**
     * 
     */
    public void testGetComponent() {
        ServletContext context = getServletContext();
        context.setAttribute("a", "A");
        Map map = (Map) getComponent(ContainerConstants.APPLICATION_SCOPE);
        assertNotNull(map);
        assertEquals("A", map.get("a"));
    }
}
