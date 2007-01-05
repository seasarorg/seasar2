/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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
package org.seasar.framework.container.deployer;

import junit.framework.TestCase;

import org.seasar.framework.container.IllegalInstanceDefRuntimeException;

/**
 * @author higa
 */
public class InstanceDefFactoryTest extends TestCase {

    public void testGetInstanceDef() throws Exception {
        assertEquals("1", InstanceDefFactory.SESSION, InstanceDefFactory
                .getInstanceDef("session"));
        try {
            InstanceDefFactory.getInstanceDef("hoge");
            fail("2");
        } catch (IllegalInstanceDefRuntimeException ex) {
            System.out.println(ex);
        }
    }
}