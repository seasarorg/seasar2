/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
package org.seasar.extension.jdbc.gen.sql;

import java.io.File;
import java.util.Arrays;

import org.junit.Test;
import org.seasar.extension.jdbc.gen.exception.IllegalDumpColumnSizeRuntimeException;
import org.seasar.framework.util.ResourceUtil;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class DumpFileReaderTest {

    private DumpFileTokenizer tokenizer = new DumpFileTokenizer(',');

    @Test
    public void test() throws Exception {
        String path = getClass().getName().replace('.', '/') + ".csv";
        File file = ResourceUtil.getResourceAsFile(path);
        DumpFileReader reader = new DumpFileReader(file, "UTF-8", tokenizer);
        assertEquals(Arrays.asList("ID", "NAME", "AGE"), reader.readRow());
        assertEquals(0, reader.getRowNo());
        assertEquals(Arrays.asList("1", "aaa", "10"), reader.readRow());
        assertEquals(1, reader.getRowNo());
        assertEquals(Arrays.asList("2", null, "20"), reader.readRow());
        assertEquals(2, reader.getRowNo());
        assertEquals(Arrays.asList("3", "ccc", "30"), reader.readRow());
        assertEquals(3, reader.getRowNo());
        assertNull(reader.readRow());
    }

    @Test
    public void testHeaderOnly() throws Exception {
        String path = getClass().getName().replace('.', '/')
                + "_headerOnly.csv";
        File file = ResourceUtil.getResourceAsFile(path);
        DumpFileReader reader = new DumpFileReader(file, "UTF-8", tokenizer);
        assertEquals(Arrays.asList("ID", "NAME", "AGE"), reader.readRow());
        assertEquals(0, reader.getRowNo());
        assertNull(reader.readRow());
    }

    @Test
    public void testIllegalColumnSize() throws Exception {
        String path = getClass().getName().replace('.', '/')
                + "_illegalColumnSize.csv";
        File file = ResourceUtil.getResourceAsFile(path);
        DumpFileReader reader = new DumpFileReader(file, "UTF-8", tokenizer);
        assertEquals(Arrays.asList("ID", "NAME", "AGE"), reader.readRow());
        assertEquals(0, reader.getRowNo());
        try {
            reader.readRow();
            fail();
        } catch (IllegalDumpColumnSizeRuntimeException expected) {
        }
    }
}
