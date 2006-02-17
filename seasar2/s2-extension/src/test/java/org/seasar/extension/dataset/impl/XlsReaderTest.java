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
package org.seasar.extension.dataset.impl;

import java.math.BigDecimal;

import junit.framework.TestCase;

import org.seasar.extension.dataset.DataRow;
import org.seasar.extension.dataset.DataSet;
import org.seasar.extension.dataset.DataTable;
import org.seasar.extension.dataset.types.ColumnTypes;
import org.seasar.framework.util.Base64Util;
import org.seasar.framework.util.TimestampConversionUtil;

public class XlsReaderTest extends TestCase {

    private static final String PATH = "org/seasar/extension/dataset/impl/XlsReaderImplTest.xls";

    private DataSet dataSet_;

    public void testCreateTable() throws Exception {
        assertEquals("1", 5, dataSet_.getTableSize());
    }

    public void testSetupColumns() throws Exception {
        DataTable table = dataSet_.getTable(2);
        assertEquals("1", 5, table.getColumnSize());
        for (int i = 0; i < table.getColumnSize(); ++i) {
            assertEquals("2", "COLUMN" + i, table.getColumnName(i));
        }
        assertEquals("3", ColumnTypes.TIMESTAMP, table.getColumnType(0));
        assertEquals("4", ColumnTypes.BIGDECIMAL, table.getColumnType(1));
        assertEquals("5", ColumnTypes.STRING, table.getColumnType(2));
        assertEquals("6", ColumnTypes.BINARY, table.getColumnType(3));
        assertEquals("7", ColumnTypes.BIGDECIMAL, table.getColumnType(4));
    }

    public void testSetupColumnsForNull() throws Exception {
        DataTable table = dataSet_.getTable(4);
        assertEquals("1", ColumnTypes.OBJECT, table.getColumnType(1));
    }

    public void testSetupRows() throws Exception {
        DataTable table = dataSet_.getTable(0);
        assertEquals("1", 12, table.getRowSize());
        for (int i = 0; i < table.getRowSize(); ++i) {
            DataRow row = table.getRow(i);
            for (int j = 0; j < table.getColumnSize(); ++j) {
                assertEquals("2", "row " + i + " col " + j, row.getValue(j));
            }
        }
        DataTable table2 = dataSet_.getTable("EMPTY_TABLE");
        assertEquals("3", 0, table2.getRowSize());
    }

    public void testGetValue() throws Exception {
        DataTable table = dataSet_.getTable(2);
        DataRow row = table.getRow(0);
        assertEquals("1", TimestampConversionUtil.toTimestamp("20040322",
                "yyyyMMdd"), row.getValue(0));
        assertEquals("2", new BigDecimal(123), row.getValue(1));
        assertEquals("3", "\u3042", row.getValue(2));
        assertEquals("4", "YWJj", Base64Util.encode((byte[]) row.getValue(3)));
        assertEquals("5", new BigDecimal("0.05"), row.getValue(4));
    }

    protected void setUp() throws Exception {
        dataSet_ = new XlsReader(PATH).read();
    }
}