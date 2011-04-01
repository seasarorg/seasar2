/*
 * Copyright 2004-2011 the Seasar Foundation and the Others.
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
package org.seasar.framework.container.assembler;

import junit.framework.TestCase;

import org.seasar.framework.container.IllegalAutoBindingDefRuntimeException;

/**
 * @author higa
 */
public class AutoBindingDefFactoryTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testGetAutoBindingDef() throws Exception {
        assertEquals("1", AutoBindingDefFactory.AUTO, AutoBindingDefFactory
                .getAutoBindingDef("auto"));
        assertEquals("2", AutoBindingDefFactory.CONSTRUCTOR,
                AutoBindingDefFactory.getAutoBindingDef("constructor"));
        assertEquals("3", AutoBindingDefFactory.PROPERTY, AutoBindingDefFactory
                .getAutoBindingDef("property"));
        assertEquals("4", AutoBindingDefFactory.NONE, AutoBindingDefFactory
                .getAutoBindingDef("none"));
        try {
            AutoBindingDefFactory.getAutoBindingDef("hoge");
            fail("5");
        } catch (IllegalAutoBindingDefRuntimeException ex) {
            System.out.println(ex);
        }
    }
}