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
package org.seasar.framework.container.assembler;

import junit.framework.TestCase;

import org.seasar.framework.container.IllegalBindingTypeDefRuntimeException;

/**
 * @author higa
 */
public class BindingTypeDefFactoryTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testGetBindingTypeDef() throws Exception {
        assertEquals("1", BindingTypeDefFactory.MUST, BindingTypeDefFactory
                .getBindingTypeDef("must"));
        assertEquals("2", BindingTypeDefFactory.SHOULD, BindingTypeDefFactory
                .getBindingTypeDef("should"));
        assertEquals("3", BindingTypeDefFactory.MAY, BindingTypeDefFactory
                .getBindingTypeDef("may"));
        assertEquals("4", BindingTypeDefFactory.NONE, BindingTypeDefFactory
                .getBindingTypeDef("none"));
        try {
            BindingTypeDefFactory.getBindingTypeDef("hoge");
            fail("5");
        } catch (IllegalBindingTypeDefRuntimeException ex) {
            System.out.println(ex);
        }
    }
}