/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
package org.seasar.extension.jdbc.gen.command;

import org.junit.After;
import org.junit.Test;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.container.impl.S2ContainerImpl;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class S2ContainerFactorySupportTest {

    /**
     * 
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        SingletonS2ContainerFactory.destroy();
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testInitAndDestroy() throws Exception {
        SingletonS2ContainerFactorySupport support = new SingletonS2ContainerFactorySupport(
                "s2jdbc-gen-core-test.dicon");
        support.init();
        assertTrue(SingletonS2ContainerFactory.hasContainer());
        support.destory();
        assertFalse(SingletonS2ContainerFactory.hasContainer());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testInitAndDestroy_alreadyInitialized() throws Exception {
        S2Container container = new S2ContainerImpl();
        SingletonS2ContainerFactory.setContainer(container);
        SingletonS2ContainerFactorySupport support = new SingletonS2ContainerFactorySupport(
                "s2jdbc-gen-core-test.dicon");
        support.init();
        assertSame(container, SingletonS2ContainerFactory.getContainer());
        support.destory();
        assertSame(container, SingletonS2ContainerFactory.getContainer());
    }
}
