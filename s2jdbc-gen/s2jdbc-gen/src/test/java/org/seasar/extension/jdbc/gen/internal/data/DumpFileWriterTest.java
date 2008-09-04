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

        ColumnDesc aaa = new ColumnDesc();
        aaa.setName("aaa");
        aaa.setSqlType(new VarcharType());

        ColumnDesc bbb = new ColumnDesc();
        bbb.setName("bbb");
        bbb.setSqlType(new VarcharType());

        ColumnDesc ccc = new ColumnDesc();
        ccc.setName("ccc");
        ccc.setSqlType(new IntegerType());

        TableDesc tableDesc = new TableDesc();
        tableDesc.addColumnDesc(aaa);
        tableDesc.addColumnDesc(bbb);
        tableDesc.addColumnDesc(ccc);

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
    public void testWriteHeader() throws Exception {
        MockColumnMetaData columnMetaData1 = new MockColumnMetaData();
        columnMetaData1.setColumnLabel("aaa");
        MockColumnMetaData columnMetaData2 = new MockColumnMetaData();
        columnMetaData2.setColumnLabel("ccc");
        MockResultSetMetaData metaData = new MockResultSetMetaData();
        metaData.addColumnMetaData(columnMetaData1);
        metaData.addColumnMetaData(columnMetaData2);

        dumpFileWriter.writeHeader(metaData);
        bufferedWriter.flush();
        assertEquals("\"aaa\",\"ccc\"" + separator, stringWriter.toString());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testWriteRowData() throws Exception {
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

        for (; rs.next();) {
            dumpFileWriter.writeRowData(rs, metaData);
        }
        bufferedWriter.flush();
        assertEquals("hoge,100" + separator + "\"f\"\"oo\",200" + separator,
                stringWriter.toString());
    }
}
