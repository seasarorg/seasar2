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
package org.seasar.framework.container.cooldeploy;

import junit.framework.TestCase;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.S2ContainerFactory;

/**
 * @author koichik
 * 
 */
public class S2ContainerFactoryCoolProviderTest extends TestCase {

    private S2Container container;

    protected void setUp() throws Exception {
        super.setUp();
        S2ContainerFactory.configure("cooldeploy.dicon");
        container = S2ContainerFactory.create(getClass().getName().replace('.',
                '/')
                + ".dicon");
        S2ContainerFactory.include(container, "aop.dicon");
        container.init();
    }

    public void test() throws Exception {
        assertTrue(container.hasDescendant("cooldeploy-autoregister.dicon"));
    }

}
