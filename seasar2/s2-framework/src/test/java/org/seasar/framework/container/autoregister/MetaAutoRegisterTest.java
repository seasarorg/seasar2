/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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
package org.seasar.framework.container.autoregister;

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.MetaDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.unit.S2FrameworkTestCase;

/**
 * @author koichik
 * 
 */
public class MetaAutoRegisterTest extends S2FrameworkTestCase {

    private S2Container child;

    /**
     * @throws Exception
     */
    public void setUpRegisterAll() throws Exception {
        include("autoRegister2.dicon");
    }

    /**
     * 
     */
    public void testRegisterAll() {
        Bar bar = (Bar) child.getComponent("bar");
        assertNotNull("1", bar);
        assertEquals("2", "Hello", bar.greet());
        ComponentDef cd = child.getComponentDef("bar2");
        assertEquals("3", 2, cd.getMetaDefSize());

        MetaDef meta1 = cd.getMetaDef(0);
        assertEquals("4", "hoge", meta1.getName());
        assertNull("4", meta1.getValue());

        MetaDef meta2 = cd.getMetaDef(1);
        assertEquals("4", "fuga", meta2.getName());
        assertEquals("5", "fugafuga", meta2.getValue());
    }
}
