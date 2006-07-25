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
package org.seasar.framework.container.factory;

import java.util.Date;

import junit.framework.TestCase;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.env.Env;

/**
 * @author higa
 * 
 */
public class IncludeTagHandlerTest extends TestCase {

    private static final String PATH = "org/seasar/framework/container/factory/IncludeTagHandlerTest.dicon";

    private static final String PATH2 = "org/seasar/framework/container/factory/IncludeTagHandlerTest2.dicon";

    private static final String AAA_PATH = "org/seasar/framework/container/factory/aaa.dicon";

    private static final String ENV_PATH = "org/seasar/framework/container/factory/env.text";

    protected void setUp() {
        Env.setFilePath(ENV_PATH);
    }

    protected void tearDown() {
        Env.initialize();
    }

    public void testInclude() throws Exception {
        S2Container container = S2ContainerFactory.create(PATH);
        assertEquals("1", new Date(0), container.getComponent(Date.class));
    }

    public void testInclude2() throws Exception {
        S2Container container = S2ContainerFactory.create(AAA_PATH);
        assertSame("1", container.getComponent("aaa.cdate"), container
                .getComponent("bbb.cdate"));
    }

    public void testInclude3() throws Exception {
        S2Container container = S2ContainerFactory.create(PATH);
        S2Container grandChild = (S2Container) container
                .getComponent("grandChild");
        S2Container child = (S2Container) container.getComponent("child");
        S2Container grandChild2 = (S2Container) child
                .getComponent("grandChild");

        assertSame("1", grandChild, grandChild2);
    }

    public void testInclude_condition() throws Exception {
        S2Container container = S2ContainerFactory.create(PATH2);
        assertFalse(container.hasComponentDef("child"));
        assertTrue(container.hasComponentDef("grandChild"));
    }
}