/*
 * Copyright 2004-2013 the Seasar Foundation and the Others.
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
package org.seasar.framework.unit.impl;

import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.unit.PreparationType;
import org.seasar.framework.unit.TestContext;
import org.seasar.framework.unit.TestDataPreparer;

/**
 * @author taedium
 * 
 */
public class TestDataPreparerImplTest extends S2TestCase {

    private TestDataPreparer testDataPreparer;

    private TestContext testContext;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        SubTestDataPreparerImpl.path = null;
    }

    /**
     * @throws Exception
     */
    public void setUpPrepare() throws Exception {
        include("TestDataPreparerImplTest.dicon");
    }

    /**
     * @throws Exception
     */
    public void testPrepare() throws Exception {
        testDataPreparer.prepare(testContext);
        assertEquals(
                "org/seasar/framework/unit/impl/TestDataPreparerImplTest_foo.xls",
                SubTestDataPreparerImpl.path);
    }

    /**
     * 
     */
    public static class SubTestDataPreparerImpl extends TestDataPreparerImpl {

        private static String path;

        @Override
        protected void prepare(PreparationType preparingType, String path,
                final boolean trimString) {
            SubTestDataPreparerImpl.path = path;
        }
    }
}
