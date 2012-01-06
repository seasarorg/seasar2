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

import java.lang.reflect.Method;

import org.seasar.framework.unit.S2FrameworkTestCase;

/**
 * @author koichik
 * 
 */
public class PropertyInterTypeTest extends S2FrameworkTestCase {

    Target target;

    protected void setUp() throws Exception {
        super.setUp();
        include(getClass().getName().replace('.', '/') + ".dicon");
    }

    /**
     * @throws Exception
     */
    public void test() throws Exception {
        Method setter = target.getClass().getMethod("setName",
                new Class[] { String.class });
        Method getter = target.getClass().getMethod("getName", new Class[0]);
        setter.invoke(target, new Object[] { "hoge" });
        assertEquals("hoge", getter.invoke(target, null));

        setter = target.getClass().getMethod("setDuplicate",
                new Class[] { long.class });
        getter = target.getClass().getMethod("getDuplicate", new Class[0]);
        setter.invoke(target, new Object[] { new Long(100) });
        assertEquals(new Long(100), getter.invoke(target, null));
    }

    /**
     * 
     */
    public static abstract class AbstractTarget {
        /**
         * 
         */
        protected int duplicate;
    }

    /**
     * 
     */
    public static class Target extends AbstractTarget {
        /**
         * 
         */
        protected String name;

        /**
         * 
         */
        protected long duplicate;
    }

}
