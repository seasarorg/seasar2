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

import java.io.BufferedWriter;
import java.io.File;
import java.io.StringWriter;

import org.junit.Before;
import org.junit.Test;
import org.seasar.extension.jdbc.gen.desc.ColumnDesc;
import org.seasar.extension.jdbc.gen.desc.TableDesc;
import org.seasar.extension.jdbc.gen.internal.dialect.StandardGenDialect;
import org.seasar.extension.jdbc.gen.internal.sqltype.IntegerType;
import org.seasar.extension.jdbc.gen.internal.sqltype.VarcharType;
import org.seasar.framework.mock.sql.MockColumnMetaData;
import org.seasar.framework.mock.sql.MockResultSet;
import org.seasar.framework.mock.sql.MockResultSetMetaData;
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

        ColumnDesc columnDesc1 = new ColumnDesc();
        columnDesc1.setName("aaa");
        columnDesc1.setSqlType(new VarcharType());
        ColumnDesc columnDesc2 = new ColumnDesc();
        columnDesc2.setName("bbb");
        columnDesc2.setSqlType(new VarcharType());
        ColumnDesc columnDesc3 = new ColumnDesc();
        columnDesc3.setName("ccc");
        columnDesc3.setSqlType(new IntegerType());

        TableDesc tableDesc = new TableDesc();
        tableDesc.addColumnDesc(columnDesc1);
        tableDesc.addColumnDesc(columnDesc2);
        tableDesc.addColumnDesc(columnDesc3);

        dumpFileWriter = new DumpFileWriter(new File("file"), tableDesc,
                new StandardGenDialect(), "UTF-8", ',') {

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
    public void testWriteHeaderOnly() throws Exception {
        dumpFileWriter.writeHeaderOnly();
        bufferedWriter.flush();
        assertEquals(1, dumpFileWriter.getLineNumber());
        assertEquals("\"aaa\",\"bbb\",\"ccc\"" + separator, stringWriter
                .toString());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testWrite_javaColumn_gt_dbColumn() throws Exception {
        MockColumnMetaData columnMetaData1 = new MockColumnMetaData();
        columnMetaData1.setColumnLabel("aaa");
        MockColumnMetaData columnMetaData2 = new MockColumnMetaData();
        columnMetaData2.setColumnLabel("ccc");

        MockResultSetMetaData metaData = new MockResultSetMetaData();
        metaData.addColumnMetaData(columnMetaData1);
        metaData.addColumnMetaData(columnMetaData2);

        MockResultSet rs = new MockResultSet();
        rs.setMockMetaData(metaData);
        ArrayMap row = new ArrayMap();
        row.put("aaa", "hoge");
        row.put("ccc", 100);
        rs.addRowData(row);
        row = new ArrayMap();
        row.put("aaa", "f\"oo");
        row.put("ccc", 200);
        rs.addRowData(row);

        dumpFileWriter.writeRows(rs);
        bufferedWriter.flush();

        assertEquals(3, dumpFileWriter.getLineNumber());
        assertEquals("\"aaa\",\"bbb\",\"ccc\"" + separator
                + "\"hoge\",,\"100\"" + separator + "\"f\"\"oo\",,\"200\""
                + separator, stringWriter.toString());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testWriteHeader_javaColumn_lt_dbColumn() throws Exception {
        MockColumnMetaData columnMetaData1 = new MockColumnMetaData();
        columnMetaData1.setColumnLabel("aaa");
        MockColumnMetaData columnMetaData2 = new MockColumnMetaData();
        columnMetaData2.setColumnLabel("bbb");
        MockColumnMetaData columnMetaData3 = new MockColumnMetaData();
        columnMetaData3.setColumnLabel("ccc");
        MockColumnMetaData columnMetaData4 = new MockColumnMetaData();
        columnMetaData4.setColumnLabel("ddd");
        MockResultSetMetaData metaData = new MockResultSetMetaData();

        metaData.addColumnMetaData(columnMetaData1);
        metaData.addColumnMetaData(columnMetaData2);
        metaData.addColumnMetaData(columnMetaData3);
        metaData.addColumnMetaData(columnMetaData4);

        MockResultSet rs = new MockResultSet();
        rs.setMockMetaData(metaData);
        ArrayMap row = new ArrayMap();
        row.put("aaa", "hoge");
        row.put("bbb", "bar");
        row.put("ccc", 100);
        row.put("ddd", 200);
        rs.addRowData(row);
        row = new ArrayMap();
        row.put("aaa", "f\"oo");
        row.put("bbb", "baz");
        row.put("ccc", 200);
        row.put("ddd", 300);
        rs.addRowData(row);

        dumpFileWriter.writeRows(rs);
        bufferedWriter.flush();

        assertEquals(3, dumpFileWriter.getLineNumber());
        assertEquals("\"aaa\",\"bbb\",\"ccc\"" + separator
                + "\"hoge\",\"bar\",\"100\"" + separator
                + "\"f\"\"oo\",\"baz\",\"200\"" + separator, stringWriter
                .toString());
    }

}
