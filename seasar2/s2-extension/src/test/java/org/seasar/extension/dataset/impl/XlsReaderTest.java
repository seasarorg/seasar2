package org.seasar.extension.dataset.impl;

import java.math.BigDecimal;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.seasar.extension.dataset.DataRow;
import org.seasar.extension.dataset.DataSet;
import org.seasar.extension.dataset.DataTable;
import org.seasar.extension.dataset.impl.XlsReader;
import org.seasar.framework.util.Base64Util;
import org.seasar.framework.util.TimestampConversionUtil;

public class XlsReaderTest extends TestCase {

    private static final String PATH = "org/seasar/extension/dataset/impl/XlsReaderImplTest.xls";

    private DataSet dataSet_;

    public XlsReaderTest(String name) {
        super(name);
    }

    public void testCreateTable() throws Exception {
        assertEquals("1", 4, dataSet_.getTableSize());
    }

    public void testSetupColumns() throws Exception {
        DataTable table = dataSet_.getTable(0);
        assertEquals("1", 4, table.getColumnSize());
        for (int i = 0; i < table.getColumnSize(); ++i) {
            assertEquals("2", "COLUMN" + i, table.getColumnName(i));
        }
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

    protected void tearDown() throws Exception {
    }

    public static Test suite() {
        return new TestSuite(XlsReaderTest.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.main(new String[] { XlsReaderTest.class
                .getName() });
    }
}