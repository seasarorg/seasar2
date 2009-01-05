/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
package org.seasar.framework.container.warmdeploy;

import org.seasar.framework.container.ComponentCreator;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.creator.ComponentCreatorImpl;
import org.seasar.framework.container.impl.S2ContainerBehavior;
import org.seasar.framework.container.impl.S2ContainerImpl;
import org.seasar.framework.container.warmdeploy.dao.FooDao;
import org.seasar.framework.convention.impl.NamingConventionImpl;
import org.seasar.framework.unit.S2FrameworkTestCase;
import org.seasar.framework.util.ClassUtil;

/**
 * @author higa
 * 
 */
public class WarmdeployBehaviorTest extends S2FrameworkTestCase {

    private WarmdeployBehavior behavior;

    private String rootPackageName = ClassUtil.getPackageName(getClass());

    protected void setUp() {
        NamingConventionImpl convention = new NamingConventionImpl();
        convention.addRootPackageName(rootPackageName);
        behavior = new WarmdeployBehavior();
        behavior.setNamingConvention(convention);
        ComponentCreatorImpl creator = new ComponentCreatorImpl(convention);
        creator.setNameSuffix("Dao");
        behavior.setCreators(new ComponentCreator[] { creator });
        S2ContainerBehavior.setProvider(behavior);
    }

    protected void tearDown() {
        S2ContainerBehavior
                .setProvider(new S2ContainerBehavior.DefaultProvider());
    }

    /**
     * @throws Exception
     */
    public void testCreatComponentDef_name() throws Exception {
        assertTrue(getContainer().hasComponentDef("fooDao"));
    }

    /**
     * @throws Exception
     */
    public void testCreatComponentDef_class() throws Exception {
        assertTrue(getContainer().hasComponentDef(FooDao.class));
    }

    /**
     * @throws Exception
     */
    public void testChild() throws Exception {
        S2Container child = new S2ContainerImpl();
        getContainer().include(child);
        assertFalse(child.hasComponentDef(FooDao.class));
    }

}