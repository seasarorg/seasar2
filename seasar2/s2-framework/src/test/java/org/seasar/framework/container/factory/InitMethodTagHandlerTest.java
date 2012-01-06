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
package org.seasar.framework.container.factory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.S2Container;

/**
 * @author higa
 * 
 */
public class InitMethodTagHandlerTest extends TestCase {

    private static final String PATH = "org/seasar/framework/container/factory/InitMethodTagHandlerTest.dicon";

    /**
     * @throws Exception
     */
    public void testArg() throws Exception {
        S2Container container = S2ContainerFactory.create(PATH);
        Map aaa = (HashMap) container.getComponent("aaa");
        assertEquals("1", new Integer(111), aaa.get("aaa"));
        Bbb bbb = (Bbb) container.getComponent("bbb");
        assertEquals("2", false, bbb.isEmpty());
    }

    /**
     * @throws Exception
     */
    public void testInitMethodAnnotation() throws Exception {
        S2Container container = S2ContainerFactory.create(PATH);
        container.init();
        Bbb bbb = (Bbb) container.getComponent("bbb");
        Bbb bbb2 = (Bbb) container.getComponent("bbb2");
        ComponentDef cd = container.getComponentDef("bbb2");
        assertEquals("1", 1, bbb.getInitCount());
        assertEquals("2", 1, cd.getInitMethodDefSize());
        assertEquals("3", 1, bbb2.getInitCount());
    }

    /**
     *
     */
    public static class Bbb {

        /**
         * 
         */
        public static final String INIT_METHOD = "init";

        private List value;

        private int initCount = 0;

        /**
         * @param value
         */
        public void value(List value) {
            this.value = value;
        }

        /**
         * @return
         */
        public boolean isEmpty() {
            return value == null;
        }

        /**
         * 
         */
        public void init() {
            ++initCount;
        }

        /**
         * @return
         */
        public int getInitCount() {
            return initCount;
        }
    }
}
