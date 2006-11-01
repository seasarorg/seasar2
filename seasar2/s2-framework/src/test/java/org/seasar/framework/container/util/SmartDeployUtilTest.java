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
package org.seasar.framework.container.util;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.cooldeploy.S2ContainerFactoryCoolProvider;
import org.seasar.framework.container.factory.S2ContainerFactory;
import org.seasar.framework.container.hotdeploy.HotdeployBehavior;
import org.seasar.framework.container.impl.S2ContainerBehavior;
import org.seasar.framework.container.warmdeploy.WarmdeployBehavior;
import org.seasar.framework.unit.S2FrameworkTestCase;
import org.seasar.framework.util.FieldUtil;

/**
 * @author shot
 * 
 */
public class SmartDeployUtilTest extends S2FrameworkTestCase {

    public SmartDeployUtilTest() {
        setWarmDeploy(false);
    }

    public void testHotdeployMode() throws Exception {
        S2Container container = getContainer();
        assertFalse(SmartDeployUtil.isHotdeployMode(container));
        S2ContainerBehavior.setProvider(new HotdeployBehavior());
        assertTrue(SmartDeployUtil.isHotdeployMode(container));
    }

    protected void tearDownHotdeployMode() {
        S2ContainerBehavior
                .setProvider(new S2ContainerBehavior.DefaultProvider());
    }

    public void testCooldeployMode() throws Exception {
        S2Container container = getContainer();
        assertFalse(SmartDeployUtil.isCooldeployMode(container));
        BeanDesc bd = BeanDescFactory.getBeanDesc(S2ContainerFactory.class);
        FieldUtil.set(bd.getField("provider"), null,
                new S2ContainerFactoryCoolProvider());
        assertTrue(SmartDeployUtil.isCooldeployMode(container));
    }

    protected void tearDownCooldeployMode() {
        BeanDesc bd = BeanDescFactory.getBeanDesc(S2ContainerFactory.class);
        FieldUtil.set(bd.getField("provider"), null,
                new S2ContainerFactory.DefaultProvider());
    }

    public void testWarmdeployMode() throws Exception {
        S2Container container = getContainer();
        assertFalse(SmartDeployUtil.isWarmdeployMode(container));
        S2ContainerBehavior.setProvider(new WarmdeployBehavior());
        assertTrue(SmartDeployUtil.isWarmdeployMode(container));
    }

    protected void tearDownWarmdeployMode() {
        S2ContainerBehavior
                .setProvider(new S2ContainerBehavior.DefaultProvider());
    }

}
