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
package org.seasar.framework.container.customizer;

import java.lang.reflect.Method;

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.autoregister.Greeting;
import org.seasar.framework.unit.S2FrameworkTestCase;

/**
 * @author koichik
 */
public class InterTypeCustomizerTest extends S2FrameworkTestCase {

    private S2Container child;

    protected void setUp() throws Exception {
        include("InterTypeCustomizerTest.dicon");
    }

    public void testCustomize() throws Exception {
        ComponentDef cd = child.getComponentDef(Greeting.class);
        assertEquals(1, cd.getInterTypeDefSize());

        Greeting greeting = (Greeting) cd.getComponent();
        assertNotNull(greeting);
        Method m = greeting.getClass().getMethod("getFoo2", null);
        assertNotNull(m);
        assertNull(m.invoke(greeting, null));
    }

}
