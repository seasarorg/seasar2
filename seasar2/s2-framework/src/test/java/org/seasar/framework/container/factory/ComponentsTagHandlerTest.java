/*
 * Copyright 2004-2015 the Seasar Foundation and the Others.
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

import junit.framework.TestCase;

import org.seasar.framework.container.S2Container;

/**
 * @author higa
 * 
 */
public class ComponentsTagHandlerTest extends TestCase {

    private static final String PATH = "org/seasar/framework/container/factory/ComponentsTagHandlerTest.dicon";

    private static final String PATH2 = "org/seasar/framework/container/factory/ComponentsTagHandlerTest2.dicon";

    /**
     * @throws Exception
     */
    public void testComponent() throws Exception {
        S2Container container = S2ContainerFactory.create(PATH);
        assertEquals("aaa", container.getNamespace());
        assertEquals("", container.getComponent("aaa.bbb"));
        assertEquals("", container.getComponent("bbb"));
        assertEquals(PATH, container.getPath());
        assertFalse(container.isInitializeOnCreate());
    }

    /**
     * @throws Exception
     */
    public void testComponentInitializeOnCreate() throws Exception {
        S2Container container = S2ContainerFactory.create(PATH2);
        assertEquals("aaa", container.getNamespace());
        assertEquals("", container.getComponent("aaa.bbb"));
        assertEquals("", container.getComponent("bbb"));
        assertEquals(PATH2, container.getPath());
        assertTrue(container.isInitializeOnCreate());
    }
}
