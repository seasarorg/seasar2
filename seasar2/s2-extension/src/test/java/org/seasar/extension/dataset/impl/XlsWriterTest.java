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
package org.seasar.extension.dataset.impl;

import java.io.File;

import junit.framework.TestCase;

import org.seasar.extension.dataset.DataSet;
import org.seasar.extension.dataset.DataWriter;
import org.seasar.framework.util.ResourceUtil;

/**
 * @author higa
 * 
 */
public class XlsWriterTest extends TestCase {

    private static final String PATH = "org/seasar/extension/dataset/impl/XlsReaderImplTest.xls";

    private static final String PATH2 = "XlsWriterImplTest.xls";

    private DataSet dataSet_;

    private DataWriter writer_;

    /**
     * @throws Exception
     */
    public void testWrite() throws Exception {
        writer_.write(dataSet_);
    }

    protected void setUp() throws Exception {
        File readFile = ResourceUtil.getFile(ResourceUtil.getResource(PATH));
        dataSet_ = new XlsReader(readFile).read();
        File writeFile = new File(readFile.getParentFile(), PATH2);
        writer_ = new XlsWriter(writeFile);
    }
}