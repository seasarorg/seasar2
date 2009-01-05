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
package org.seasar.framework.container.impl;

import java.util.Map;

import org.seasar.framework.container.ComponentNotFoundRuntimeException;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.TooManyRegistrationComponentDef;
import org.seasar.framework.container.TooManyRegistrationRuntimeException;
import org.seasar.framework.unit.S2FrameworkTestCase;

/**
 * @author koichik
 */
public class S2ContainerBehaviorTest extends S2FrameworkTestCase {
    private S2Container container;

    public void setUp() throws Exception {
        include("S2ContainerBehaviorTest.dicon");
    }

    /**
     * @throws Exception
     */
    public void testGetComponent() throws Exception {
        assertNotNull("1", container.getComponent("foo"));
        try {
            container.getComponent("not exists");
            fail("2");
        } catch (ComponentNotFoundRuntimeException expected) {
        }
        try {
            container.getComponent("bar");
            fail("3");
        } catch (TooManyRegistrationRuntimeException expected) {
            System.out.println(expected);
        }
    }

    /**
     * @throws Exception
     */
    public void testGetComponentDef() throws Exception {
        assertNotNull("1", container.getComponentDef("foo"));
        try {
            container.getComponentDef("not exists");
            fail("2");
        } catch (ComponentNotFoundRuntimeException expected) {
        }
        assertTrue(
                "3",
                container.getComponentDef("bar") instanceof TooManyRegistrationComponentDef);
    }

    /**
     * @throws Exception
     */
    public void testHasComponentDef() throws Exception {
        assertTrue("1", container.hasComponentDef("foo"));
        assertFalse("2", container.hasComponentDef("not exists"));
        assertTrue("3", container.hasComponentDef("bar"));
    }

    /**
     * @throws Exception
     */
    public void testInjectDependency() throws Exception {
        Outer outer = new Outer();
        container.injectDependency(outer, "outerFoo");
        assertNotNull("1", outer.getMap());
        try {
            container.injectDependency(outer, "not exists");
            fail("2");
        } catch (ComponentNotFoundRuntimeException expected) {
        }
        try {
            container.injectDependency(outer, "outerBar");
            fail("3");
        } catch (UnsupportedOperationException expected) {
            System.out.println(expected);
        }
    }

    /**
     *
     */
    public static class Outer {
        Map map;

        /**
         * @return
         */
        public Map getMap() {
            return this.map;
        }

        /**
         * @param map
         */
        public void setMap(Map map) {
            this.map = map;
        }
    }
}
