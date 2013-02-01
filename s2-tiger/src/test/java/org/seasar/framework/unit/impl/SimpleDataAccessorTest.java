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

import java.math.BigDecimal;

import org.seasar.extension.dataset.DataRow;
import org.seasar.extension.dataset.DataSet;
import org.seasar.extension.dataset.DataTable;
import org.seasar.extension.dataset.impl.DataSetImpl;
import org.seasar.extension.dataset.impl.SqlWriter;
import org.seasar.framework.unit.S2TigerTestCase;

/**
 * @author taedium
 * 
 */
public class SimpleDataAccessorTest extends S2TigerTestCase {

    private SimpleDataAccessor accessor;

    private SqlWriter sqlWriter;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        include(getClass().getSimpleName() + ".dicon");
    }

    /**
     * 
     * @throws Exception
     */
    public void testReadXls() throws Exception {
        DataSet dataSet = accessor.readXls("testdata.xls");
        System.out.println(dataSet);
        assertEquals("1", 2, dataSet.getTableSize());
        DataTable table = dataSet.getTable("emp");
        assertEquals("2", 2, table.getRowSize());
        assertEquals("3", 3, table.getColumnSize());
        DataRow row = table.getRow(0);
        assertEquals("4", new BigDecimal(9900), row.getValue("empno"));
        assertEquals("5", "hoge", row.getValue("ename"));
        assertEquals("6", "aaa", row.getValue("dname"));
    }

    /**
     * 
     * @throws Exception
     */
    public void testReadXlsNoTrim() throws Exception {
        DataSet dataSet = accessor.readXls("testdata_notrim.xls", false);
        System.out.println(dataSet);
        assertEquals("1", 2, dataSet.getTableSize());
        DataTable table = dataSet.getTable("emp");
        assertEquals("2", 2, table.getRowSize());
        assertEquals("3", 3, table.getColumnSize());
        DataRow row = table.getRow(0);
        assertEquals("4", new BigDecimal(9900), row.getValue("empno"));
        assertEquals("5", "hoge ", row.getValue("ename"));
        assertEquals("6", "aaa ", row.getValue("dname"));
    }

    /**
     * 
     * @throws Exception
     */
    public void testWriteDbTx() throws Exception {
        DataSet dataSet = accessor.readXls("testdata.xls");
        accessor.writeDb(dataSet);
    }

    /**
     * 
     * @throws Exception
     */
    public void testReadDb() throws Exception {
        DataSet dataSet = new DataSetImpl();
        dataSet.addTable("emp");
        dataSet.addTable("dept");
        DataSet ret = accessor.readDb(dataSet);
        System.out.println(ret);
        DataTable table1 = ret.getTable("emp");
        DataTable table2 = ret.getTable("dept");
        assertTrue("1", table1.getRowSize() > 0);
        assertTrue("2", table2.getRowSize() > 0);
    }

    /**
     * 
     * @throws Exception
     */
    public void testReadDbByTable() throws Exception {
        DataTable table = accessor.readDbByTable("emp", "empno = 7788");
        System.out.println(table);
        assertEquals("1", 1, table.getRowSize());
    }

    /**
     * 
     * @throws Exception
     */
    public void testReadDbBySql() throws Exception {
        DataTable table = accessor.readDbBySql(
                "SELECT * FROM emp WHERE empno = 7788", "emp");
        System.out.println(table);
        assertEquals("1", 1, table.getRowSize());
    }

    /**
     * 
     * @throws Exception
     */
    public void testReloadOrReadDb() throws Exception {
        DataSet dataSet = new DataSetImpl();
        DataTable table = dataSet.addTable("emp");
        table.addColumn("empno");
        table.addColumn("ename");
        DataRow row = table.addRow();
        row.setValue("empno", new Integer(7788));
        row.setValue("ename", "hoge");
        table = dataSet.addTable("dept");
        table.addColumn("loc");
        table.addColumn("dname");
        DataSet ret = accessor.reloadOrReadDb(dataSet);
        DataTable table1 = ret.getTable("emp");
        System.out.println(table1);
        DataTable table2 = ret.getTable("dept");
        System.out.println(table2);
        DataRow row1 = table1.getRow(0);
        assertTrue("1", table1.getRowSize() == 1);
        assertTrue("2", table2.getRowSize() > 0);
        assertEquals("3", false, "hoge".equals(row1.getValue("ename")));
    }

    /**
     * 
     * @throws Exception
     */
    public void testWriteXls() throws Exception {
        DataSet dataSet = accessor.readXls("testdata.xls");
        accessor.writeXls("aaa.xls", dataSet);
        DataSet dataSet2 = accessor.readXls("aaa.xls");
        assertEquals("1", dataSet, dataSet2);
    }

    /**
     * 
     * @throws Exception
     */
    public void testReadXlsReplaceDbTx() throws Exception {
        accessor.readXlsReplaceDb("testdata.xls");
    }

    /**
     * 
     * @throws Exception
     */
    public void testReadXlsAllReplaceDbTx() throws Exception {
        accessor.readXlsAllReplaceDb("testdata.xls");
    }

    /**
     * @throws Exception
     */
    public void testGetSqlWriter() throws Exception {
        assertSame(sqlWriter, accessor.getSqlWriter());
    }

}
