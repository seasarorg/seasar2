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
package org.seasar.framework.container.assembler;

import junit.framework.TestCase;

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.impl.S2ContainerImpl;

public class AbstractBindingTypeDefTest extends TestCase {

    public AbstractBindingTypeDefTest() {
    }

    public AbstractBindingTypeDefTest(String name) {
        super(name);
    }

    public void testBindingComponentDef() throws Exception {
        S2Container container = new S2ContainerImpl();
        container.register(ComponentDefAware.class);
        ComponentDef cd = container.getComponentDef(ComponentDefAware.class);
        ComponentDefAware cdAware = (ComponentDefAware) cd.getComponent();
        assertSame("1", cd, cdAware.getComponentDef());
    }

    public static class ComponentDefAware {
        private ComponentDef componentDef;

        public ComponentDef getComponentDef() {
            return componentDef;
        }

        public void setComponentDef(ComponentDef componentDef) {
            this.componentDef = componentDef;
        }
    }
}
