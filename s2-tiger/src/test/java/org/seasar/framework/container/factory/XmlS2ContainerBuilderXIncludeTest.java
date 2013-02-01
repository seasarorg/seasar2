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
package org.seasar.framework.container.factory;

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.unit.S2FrameworkTestCase;

/**
 * @author koichik
 * 
 */
public class XmlS2ContainerBuilderXIncludeTest extends S2FrameworkTestCase {

    private S2Container container;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        include(getClass().getName().replace('.', '/') + ".dicon");
    }

    /**
     * 
     */
    public void test() {
        S2Container child = container.getChild(0);
        assertEquals(4, child.getComponentDefSize());
        for (int i = 0; i < 4; ++i) {
            ComponentDef cd = child.getComponentDef(i);
            assertEquals("date" + (i + 1), cd.getComponentName());
        }
    }

}
