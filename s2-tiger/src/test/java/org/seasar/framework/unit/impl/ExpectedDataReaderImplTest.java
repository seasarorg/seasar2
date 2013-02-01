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

import org.seasar.extension.dataset.DataSet;
import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.unit.ExpectedDataReader;
import org.seasar.framework.unit.TestContext;

/**
 * @author taedium
 * 
 */
public class ExpectedDataReaderImplTest extends S2TestCase {

    private ExpectedDataReader reader;

    private TestContext testContext;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        SubExpectedDataReaderImpl.path = null;
    }

    /**
     * @throws Exception
     */
    public void setUpReader() throws Exception {
        include("ExpectedDataReaderImplTest.dicon");
    }

    /**
     * @throws Exception
     */
    public void testReader() throws Exception {
        reader.read(testContext);
        assertEquals(
                "org/seasar/framework/unit/impl/ExpectedDataReaderImplTest_foo.xls",
                SubExpectedDataReaderImpl.path);
    }

    /**
     * 
     */
    public static class SubExpectedDataReaderImpl extends
            ExpectedDataReaderImpl {

        private static String path;

        @Override
        protected DataSet readXls(final String path, final boolean trimString) {
            SubExpectedDataReaderImpl.path = path;
            return null;
        }
    }

}
