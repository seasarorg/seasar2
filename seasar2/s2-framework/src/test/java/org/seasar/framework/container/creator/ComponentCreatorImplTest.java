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
package org.seasar.framework.container.creator;

import junit.framework.TestCase;

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.creator.dao.AaaDao;
import org.seasar.framework.container.creator.dao.BbbDao;
import org.seasar.framework.container.creator.dao.impl.BbbDaoImpl;
import org.seasar.framework.convention.impl.NamingConventionImpl;
import org.seasar.framework.util.ClassUtil;

/**
 * @author higa
 * @author taichi
 */
public class ComponentCreatorImplTest extends TestCase {

    private String rootPackageName = ClassUtil.getPackageName(getClass());

    private ComponentCreatorImpl creator;

    protected void setUp() {
        NamingConventionImpl convention = new NamingConventionImpl();
        convention.addRootPackageName(rootPackageName);
        creator = new ComponentCreatorImpl(convention);
        creator.setNameSuffix("Dao");
        creator.setEnableInterface(true);
        creator.setEnableAbstract(true);
    }

    /**
     * @throws Exception
     */
    public void testCreateComponentDef() throws Exception {
        ComponentDef cd = creator.createComponentDef(AaaDao.class);
        assertNotNull(cd);
        assertEquals(AaaDao.class, cd.getComponentClass());
        assertEquals("aaaDao", cd.getComponentName());

        cd = creator.createComponentDef(BbbDao.class);
        assertNotNull(cd);
        assertEquals(BbbDaoImpl.class, cd.getComponentClass());
        assertEquals("bbbDao", cd.getComponentName());
    }
}