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
package org.seasar.extension.tx;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.seasar.extension.unit.S2TestCase;

/**
 * @author koichik
 */
public class TxEJBConfigTest extends S2TestCase {

    private static final String PATH = "ejbtx.dicon";

    public TxEJBConfigTest(String name) {
        super(name);
    }

    public void testConfig() throws Exception {
        assertNotNull("1", getComponent("ejbtx.requiredTx"));
        assertNotNull("2", getComponent("ejbtx.requiresNewTx"));
        assertNotNull("3", getComponent("ejbtx.mandatoryTx"));
        assertNotNull("4", getComponent("ejbtx.notSupportedTx"));
    }

    protected void setUp() throws Exception {
        include(PATH);
    }

    protected void tearDown() throws Exception {
    }

    public static Test suite() {
        return new TestSuite(TxJ2EEConfigTest.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.main(new String[] { TxJ2EEConfigTest.class
                .getName() });
    }
}
