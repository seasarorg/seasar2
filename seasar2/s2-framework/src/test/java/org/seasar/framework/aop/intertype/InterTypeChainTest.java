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
package org.seasar.framework.aop.intertype;

import java.io.Serializable;

import javassist.CannotCompileException;
import javassist.NotFoundException;

import org.seasar.framework.unit.S2FrameworkTestCase;
import org.seasar.framework.util.ClassUtil;

/**
 *
 */
public class InterTypeChainTest extends S2FrameworkTestCase {
    /**
     * 
     */
    public InterTypeChainTest() {
    }

    /**
     * @param name
     */
    public InterTypeChainTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        include(ClassUtil.getShortClassName(InterTypeChainTest.class)
                + ".dicon");
    }

    /**
     * @throws Exception
     */
    public void test() throws Exception {
        Object component = getComponent("test");
        assertTrue("1", component instanceof Serializable);
        assertTrue("2", component instanceof Runnable);
    }

    /**
     *
     */
    public static class InterType1 extends AbstractInterType {
        protected void introduce() throws CannotCompileException,
                NotFoundException {
            addInterface(Serializable.class);
        }
    }

    /**
     *
     */
    public static class InterType2 extends AbstractInterType {
        protected void introduce() throws CannotCompileException,
                NotFoundException {
            addInterface(Runnable.class);
        }
    }

    /**
     *
     */
    public static class TestClass {
        /**
         * 
         */
        public void run() {
        }
    }
}
