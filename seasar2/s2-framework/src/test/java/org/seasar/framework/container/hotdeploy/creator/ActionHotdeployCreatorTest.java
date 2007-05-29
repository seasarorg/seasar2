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
package org.seasar.framework.container.hotdeploy.creator;

import org.seasar.framework.container.ComponentCreator;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.ComponentNotFoundRuntimeException;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.creator.ActionCreator;
import org.seasar.framework.container.impl.S2ContainerImpl;
import org.seasar.framework.convention.NamingConvention;

/**
 * @author higa
 * 
 */
public class ActionHotdeployCreatorTest extends HotdeployCreatorTestCase {

    protected ComponentCreator newOndemandCreator(NamingConvention convention) {
        return new ActionCreator(convention);
    }

    /**
     * @throws Exception
     */
    public void testAll() throws Exception {
        String name = "aaa_hogeAction";
        ComponentDef cd = getComponentDef(name);
        assertNotNull(cd);
        assertEquals(name, cd.getComponentName());
    }

    /**
     * @throws Exception
     */
    public void testIllegalCase() throws Exception {
        try {
            String name = "aaa_HogeAction";
            getComponentDef(name);
            fail();
        } catch (ComponentNotFoundRuntimeException expected) {
        }
    }

    /**
     * @throws Exception
     */
    public void testChild() throws Exception {
        S2Container child = new S2ContainerImpl();
        getContainer().include(child);
        String name = "aaa_hogeAction";
        assertFalse(child.hasComponentDef(name));
    }

}
