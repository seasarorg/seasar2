/*
 * Copyright 2004-2005 the Seasar Foundation and the Others.
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

import junit.framework.TestSuite;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.unit.S2FrameworkTestCase;

/**
 * 
 * @author koichik
 */
public class ComponentAutoRegisterTest extends S2FrameworkTestCase {

    private S2Container child;

    protected void setUp() throws Exception {
        include("ComponentAutoRegisterTest.dicon");
    }

    public void testRegistAll() throws Exception {
        Foo foo = (Foo) child.getComponent(Foo.class);
        assertNotNull("1", foo);
        Foo2 foo2 = (Foo2) child.getComponent(Foo2.class);
        assertNotNull("2", foo2);
        assertSame("3", foo2, foo.getFoo2());
        assertNotNull("4", child.getComponent(Foo3.class));
        assertNotNull("5", child.getComponent("foo3"));
        assertFalse("6", child.hasComponentDef(Foo4Impl.class));
        assertNotNull("7", child.getComponentDef(TestSuite.class));
        assertNotNull("8", child.getComponentDef("testSuite"));
    }
}
