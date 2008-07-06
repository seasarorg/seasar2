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
import org.junit.Before;
import org.junit.Test;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.container.impl.S2ContainerImpl;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class AbstractCommandTest {

    @Before
    public void before() throws Exception {
        SingletonS2ContainerFactory.destroy();
    }

    @After
    public void after() throws Exception {
        SingletonS2ContainerFactory.destroy();
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testInitAndDestroy() throws Exception {
        AbstractCommand command = new AbstractCommand() {

            protected void doExecute() {
            }
        };
        command.setDiconFile("s2jdbc-gen-core-test.dicon");
        assertFalse(SingletonS2ContainerFactory.hasContainer());
        command.init();
        assertTrue(SingletonS2ContainerFactory.hasContainer());
        command.destroy();
        assertFalse(SingletonS2ContainerFactory.hasContainer());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testInitAndDestroy_containerAlreadyExists() throws Exception {
        AbstractCommand command = new AbstractCommand() {

            protected void doExecute() {
            }
        };
        command.setDiconFile("s2jdbc-gen-core-test.dicon");
        S2Container container = new S2ContainerImpl();
        SingletonS2ContainerFactory.setContainer(container);
        command.init();
        assertSame(container, SingletonS2ContainerFactory.getContainer());
        command.destroy();
        assertSame(container, SingletonS2ContainerFactory.getContainer());
    }
}
