/*
 * Copyright 2004-2014 the Seasar Foundation and the Others.
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
package org.seasar.framework.container.customizer;

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.MetaDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.autoregister.Greeting;
import org.seasar.framework.unit.S2FrameworkTestCase;

/**
 * @author higa
 */
public class MetaCustomizerTest extends S2FrameworkTestCase {

    private S2Container child;

    public void setUp() throws Exception {
        include("MetaCustomizerTest.dicon");
    }

    /**
     * @throws Exception
     */
    public void testCustomize() throws Exception {
        ComponentDef cd = child.getComponentDef(Greeting.class);
        assertEquals(2, cd.getMetaDefSize());
        MetaDef md = cd.getMetaDef(0);
        assertEquals("foo", md.getName());
        md = cd.getMetaDef(1);
        assertEquals("bar", md.getName());
    }

}