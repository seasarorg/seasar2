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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class DumpFileReaderTest {

    private DumpFileTokenizer tokenizer = new DumpFileTokenizer('\t');

    @Test
    public void test() throws Exception {
        DumpFileReader reader = new DumpFileReader(new File("file"), "UTF-8",
                tokenizer) {

            @Override
            protected BufferedReader createBufferedReader() throws IOException {
                StringBuilder buf = new StringBuilder();
                buf.append("\"aaa\"\t\"bbb\"\t\"c\"\"cc\"\n");
                buf.append("\"ddd\"\t\t\"eee\"");
                StringReader reader = new StringReader(buf.toString());
                return new BufferedReader(reader);
            }
        };

        assertEquals(Arrays.asList("aaa", "bbb", "c\"cc"), reader.readLine());
        assertEquals(Arrays.asList("ddd", null, "eee"), reader.readLine());
        assertNull(reader.readLine());
    }
}
