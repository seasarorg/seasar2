/*
 * Copyright 2004-2012 the Seasar Foundation and the Others.
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
package org.seasar.framework.container.cooldeploy.creator;

import org.seasar.framework.container.cooldeploy.creator.dao.AaaDao;
import org.seasar.framework.container.cooldeploy.creator.dao.BbbDao;
import org.seasar.framework.container.cooldeploy.creator.hoge.HogeDao;
import org.seasar.framework.container.hotdeploy.creatorhoge.HogeHogeDao;
import org.seasar.framework.unit.S2FrameworkTestCase;

/**
 * @author higa
 * 
 */
public class DaoCoolCreatorTest extends S2FrameworkTestCase {

    /**
     * 
     */
    public DaoCoolCreatorTest() {
        setWarmDeploy(false);
    }

    protected void setUp() {
        include("DaoCoolCreatorTest.dicon");
    }

    /**
     * @throws Exception
     */
    public void testGetTargetClass() throws Exception {
        assertTrue(getContainer().hasComponentDef("aaaDao"));
        assertTrue(getContainer().hasComponentDef(AaaDao.class));
        assertTrue(getContainer().hasComponentDef("bbbDao"));
        assertTrue(getContainer().hasComponentDef(BbbDao.class));
        assertNotNull(getContainer().getComponent("bbbDao"));

        assertFalse(getContainer().hasComponentDef("hogeDao"));
        assertFalse(getContainer().hasComponentDef("hogeHogeDao"));

        assertFalse(getContainer().hasComponentDef(HogeDao.class));
        assertFalse(getContainer().hasComponentDef(HogeHogeDao.class));
    }
}