/*
 * Copyright 2004-2015 the Seasar Foundation and the Others.
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
package org.seasar.extension.jdbc.gen.internal.data;

import java.io.File;
import java.util.Arrays;

import org.junit.Test;
import org.seasar.extension.jdbc.gen.internal.exception.IllegalDumpColumnSizeRuntimeException;
import org.seasar.extension.jdbc.gen.internal.exception.IllegalDumpValueRuntimeException;
import org.seasar.framework.util.ResourceUtil;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class DumpFileReaderTest {

    private DumpFileTokenizer tokenizer = new DumpFileTokenizer(',');

    /**
     * 
     * @throws Exception
     */
    @Test
    public void test() throws Exception {
        String path = getClass().getName().replace('.', '/') + ".csv";
        File file = ResourceUtil.getResourceAsFile(path);
        DumpFileReader reader = new DumpFileReader(file, "UTF-8", tokenizer);
        assertEquals(Arrays.asList("ID", "NAME", "AGE"), reader.readLine());
        assertEquals(1, reader.getLineNumber());
        assertEquals(Arrays.asList("1", "aaa", "10"), reader.readLine());
        assertEquals(2, reader.getLineNumber());
        assertEquals(Arrays.asList("2", null, "20"), reader.readLine());
        assertEquals(3, reader.getLineNumber());
        assertEquals(Arrays.asList("3", "", "30"), reader.readLine());
        assertEquals(4, reader.getLineNumber());
        assertNull(reader.readLine());
        assertEquals(4, reader.getLineNumber());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testQuotedCRLF() throws Exception {
        String path = getClass().getName().replace('.', '/')
                + "_quotedCRLF.csv";
        File file = ResourceUtil.getResourceAsFile(path);
        DumpFileReader reader = new DumpFileReader(file, "UTF-8", tokenizer);
        assertEquals(Arrays.asList("ID", "NAME", "AGE"), reader.readLine());
        assertEquals(1, reader.getLineNumber());
        assertEquals(Arrays.asList("1", "aa\r\na", "10"), reader.readLine());
        assertEquals(2, reader.getLineNumber());
        assertEquals(Arrays.asList("2", null, "20"), reader.readLine());
        assertEquals(3, reader.getLineNumber());
        assertEquals(Arrays.asList("3", "\r\n", "30"), reader.readLine());
        assertEquals(4, reader.getLineNumber());
        assertNull(reader.readLine());
        assertEquals(4, reader.getLineNumber());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testEndWithCR() throws Exception {
        String path = getClass().getName().replace('.', '/') + "_endWithCR.csv";
        File file = ResourceUtil.getResourceAsFile(path);
        DumpFileReader reader = new DumpFileReader(file, "UTF-8", tokenizer);
        assertEquals(Arrays.asList("ID", "NAME", "AGE"), reader.readLine());
        assertEquals(1, reader.getLineNumber());
        assertEquals(Arrays.asList("1", "aaa", "10"), reader.readLine());
        assertEquals(2, reader.getLineNumber());
        assertEquals(Arrays.asList("2", null, "20"), reader.readLine());
        assertEquals(3, reader.getLineNumber());
        assertEquals(Arrays.asList("3", "ccc", "30"), reader.readLine());
        assertEquals(4, reader.getLineNumber());
        assertNull(reader.readLine());
        assertEquals(4, reader.getLineNumber());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testEndWithLF() throws Exception {
        String path = getClass().getName().replace('.', '/') + "_endWithLF.csv";
        File file = ResourceUtil.getResourceAsFile(path);
        DumpFileReader reader = new DumpFileReader(file, "UTF-8", tokenizer);
        assertEquals(Arrays.asList("ID", "NAME", "AGE"), reader.readLine());
        assertEquals(1, reader.getLineNumber());
        assertEquals(Arrays.asList("1", "aaa", "10"), reader.readLine());
        assertEquals(2, reader.getLineNumber());
        assertEquals(Arrays.asList("2", null, "20"), reader.readLine());
        assertEquals(3, reader.getLineNumber());
        assertEquals(Arrays.asList("3", "ccc", "30"), reader.readLine());
        assertEquals(4, reader.getLineNumber());
        assertNull(reader.readLine());
        assertEquals(4, reader.getLineNumber());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testEndWithCRLF() throws Exception {
        String path = getClass().getName().replace('.', '/')
                + "_endWithCRLF.csv";
        File file = ResourceUtil.getResourceAsFile(path);
        DumpFileReader reader = new DumpFileReader(file, "UTF-8", tokenizer);
        assertEquals(Arrays.asList("ID", "NAME", "AGE"), reader.readLine());
        assertEquals(1, reader.getLineNumber());
        assertEquals(Arrays.asList("1", "aaa", "10"), reader.readLine());
        assertEquals(2, reader.getLineNumber());
        assertEquals(Arrays.asList("2", null, "20"), reader.readLine());
        assertEquals(3, reader.getLineNumber());
        assertEquals(Arrays.asList("3", "ccc", "30"), reader.readLine());
        assertEquals(4, reader.getLineNumber());
        assertNull(reader.readLine());
        assertEquals(4, reader.getLineNumber());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testHeaderOnly() throws Exception {
        String path = getClass().getName().replace('.', '/')
                + "_headerOnly.csv";
        File file = ResourceUtil.getResourceAsFile(path);
        DumpFileReader reader = new DumpFileReader(file, "UTF-8", tokenizer);
        assertEquals(Arrays.asList("ID", "NAME", "AGE"), reader.readLine());
        assertEquals(1, reader.getLineNumber());
        assertNull(reader.readLine());
        assertEquals(1, reader.getLineNumber());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testIllegalColumnSize() throws Exception {
        String path = getClass().getName().replace('.', '/')
                + "_illegalColumnSize.csv";
        File file = ResourceUtil.getResourceAsFile(path);
        DumpFileReader reader = new DumpFileReader(file, "UTF-8", tokenizer);
        assertEquals(Arrays.asList("ID", "NAME", "AGE"), reader.readLine());
        assertEquals(1, reader.getLineNumber());
        try {
            reader.readLine();
            fail();
        } catch (IllegalDumpColumnSizeRuntimeException expected) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testIllegalValue() throws Exception {
        String path = getClass().getName().replace('.', '/')
                + "_illegalValue.csv";
        File file = ResourceUtil.getResourceAsFile(path);
        DumpFileReader reader = new DumpFileReader(file, "UTF-8", tokenizer);
        assertEquals(Arrays.asList("ID", "NAME", "AGE"), reader.readLine());
        assertEquals(1, reader.getLineNumber());
        try {
            reader.readLine();
            fail();
        } catch (IllegalDumpValueRuntimeException expected) {
        }
    }

}
