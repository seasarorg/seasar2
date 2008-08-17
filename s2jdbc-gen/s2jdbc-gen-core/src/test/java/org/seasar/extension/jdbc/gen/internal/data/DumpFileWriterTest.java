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
package org.seasar.extension.jdbc.gen.internal.data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.seasar.extension.jdbc.gen.SqlType;
import org.seasar.extension.jdbc.gen.internal.data.DumpFileWriter;
import org.seasar.extension.jdbc.gen.internal.sqltype.IntegerType;
import org.seasar.extension.jdbc.gen.internal.sqltype.VarcharType;
import org.seasar.framework.mock.sql.MockResultSet;
import org.seasar.framework.util.ArrayMap;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class DumpFileWriterTest {

    private String separator;

    private StringWriter stringWriter;

    private BufferedWriter bufferedWriter;

    private DumpFileWriter dumpFileWriter;

    /**
     * 
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        separator = System.getProperty("line.separator");
        stringWriter = new StringWriter();
        bufferedWriter = new BufferedWriter(stringWriter);
        List<SqlType> sqlTypeList = Arrays.<SqlType> asList(new VarcharType(),
                new IntegerType());
        dumpFileWriter = new DumpFileWriter(new File("file"), sqlTypeList,
                "UTF-8", ',') {

            @Override
            protected BufferedWriter createBufferdWriter() {
                return bufferedWriter;
            }

        };
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testWriteHeader() throws Exception {
        dumpFileWriter.writeHeader(Arrays.asList("aaa", "bbb", "ccc"));
        bufferedWriter.flush();
        assertEquals("\"aaa\",\"bbb\",\"ccc\"" + separator, stringWriter
                .toString());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testWriteRowData() throws Exception {
        MockResultSet rs = new MockResultSet();
        ArrayMap row = new ArrayMap();
        row.put("column1", "hoge");
        row.put("column2", 100);
        rs.addRowData(row);
        row = new ArrayMap();
        row.put("column1", "f\"oo");
        row.put("column2", 200);
        rs.addRowData(row);

        for (; rs.next();) {
            dumpFileWriter.writeRowData(rs);
        }
        bufferedWriter.flush();
        assertEquals("hoge,100" + separator + "\"f\"\"oo\",200" + separator,
                stringWriter.toString());
    }
}
