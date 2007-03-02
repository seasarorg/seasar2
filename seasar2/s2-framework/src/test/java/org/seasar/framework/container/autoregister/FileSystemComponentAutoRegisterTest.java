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
package org.seasar.framework.container.autoregister;

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.InstanceDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.TooManyRegistrationRuntimeException;
import org.seasar.framework.container.autoregister.sub.Bar2;
import org.seasar.framework.unit.S2FrameworkTestCase;

/**
 * @author higa
 */
public class FileSystemComponentAutoRegisterTest extends S2FrameworkTestCase {

    private S2Container child;

    public void setUpRegisterAll() throws Exception {
        include("autoRegister.dicon");
    }

    public void testRegisterAll() throws Exception {
        Foo foo = (Foo) child.getComponent(Foo.class);
        assertNotNull(foo);
        Foo2 foo2 = (Foo2) child.getComponent(Foo2.class);
        assertNotNull(foo2);
        assertNotNull(child.getComponent(Foo3.class));
        assertSame(foo2, foo.getFoo2());
        assertNotNull(child.getComponent("foo3"));
        assertNotNull(child.getComponent(Bar2.class));
        assertFalse(child.hasComponentDef(Foo4Impl.class));
    }

    public void setUpRegisterAll2() throws Exception {
        include("autoRegister3.dicon");
    }

    public void testRegisterAll2() throws Exception {
        ComponentDef cd = child.getComponentDef(Foo.class);
        assertEquals("1", InstanceDef.PROTOTYPE_NAME, cd.getInstanceDef()
                .getName());
        ComponentDef cd2 = child.getComponentDef(Foo2.class);
        assertEquals("2", InstanceDef.REQUEST_NAME, cd2.getInstanceDef()
                .getName());
        assertNotNull("3", child.getComponent(Foo5.class));
        try {
            child.getComponent("foo5");
            fail("4");
        } catch (TooManyRegistrationRuntimeException ex) {
            System.out.println(ex);
        }
    }

    public void setUpGetComponentDefForRoot() throws Exception {
        include("autoRegister.dicon");
    }

    public void testGetComponentDefForRoot() throws Exception {
        assertNotNull("1", getComponentDef(Foo.class));
    }
}