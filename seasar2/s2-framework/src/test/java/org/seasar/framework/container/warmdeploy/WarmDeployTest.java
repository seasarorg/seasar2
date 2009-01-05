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

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.S2ContainerFactory;
import org.seasar.framework.container.impl.S2ContainerBehavior;
import org.seasar.framework.container.impl.ThreadSafeS2ContainerImpl;
import org.seasar.framework.unit.S2FrameworkTestCase;

/**
 * @author koichik
 * 
 */
public class WarmDeployTest extends S2FrameworkTestCase {

    protected void setUpContainer() throws Throwable {
        S2ContainerFactory.configure("warmdeploy.dicon");
        super.setUpContainer();
    }

    protected void setUp() throws Exception {
        super.setUp();
        include("convention.dicon");
    }

    protected void tearDown() {
        S2ContainerBehavior
                .setProvider(new S2ContainerBehavior.DefaultProvider());
    }

    /**
     * 
     */
    public void test() {
        S2Container container = getContainer()
                .getDescendant("convention.dicon");
        assertTrue(container instanceof ThreadSafeS2ContainerImpl);
        assertTrue(S2ContainerBehavior.getProvider() instanceof WarmdeployBehavior);
    }

}
