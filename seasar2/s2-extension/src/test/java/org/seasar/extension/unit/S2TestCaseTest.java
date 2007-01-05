/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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
package org.seasar.extension.unit;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.seasar.extension.dataset.DataRow;
import org.seasar.extension.dataset.DataSet;
import org.seasar.extension.dataset.DataTable;
import org.seasar.extension.dataset.impl.DataSetImpl;

public class S2TestCaseTest extends S2TestCase {

    private static final String J2EE_PATH = "j2ee.dicon";

    public void setUpGetDataSource() {
        include(J2EE_PATH);
    }

    public void testGetDataSource() {
        assertNotNull("1", getDataSource());
    }

    public void testReadXls() {
        DataSet dataSet = readXls("testdata.xls");
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

    public void setUpWriteDbTx() {
        include(J2EE_PATH);
    }

    public void testWriteDbTx() {
        DataSet dataSet = readXls("testdata.xls");
        writeDb(dataSet);
    }

    public void setUpReadDb() {
        include(J2EE_PATH);
    }

    public void testReadDb() throws Exception {
        DataSet dataSet = new DataSetImpl();
        dataSet.addTable("emp");
        dataSet.addTable("dept");
        DataSet ret = readDb(dataSet);
        System.out.println(ret);
        DataTable table1 = ret.getTable("emp");
        DataTable table2 = ret.getTable("dept");
        assertTrue("1", table1.getRowSize() > 0);
        assertTrue("2", table2.getRowSize() > 0);
    }

    public void setUpReadDbByTable() {
        include(J2EE_PATH);
    }

    public void testReadDbByTable() {
        DataTable table = readDbByTable("emp", "empno = 7788");
        System.out.println(table);
        assertEquals("1", 1, table.getRowSize());
    }

    public void setUpReadDbBySql() {
        include(J2EE_PATH);
    }

    public void testReadDbBySql() {
        DataTable table = readDbBySql("SELECT * FROM emp WHERE empno = 7788",
                "emp");
        System.out.println(table);
        assertEquals("1", 1, table.getRowSize());
    }

    public void setUpReloadOrReadDb() {
        include(J2EE_PATH);
    }

    public void testReloadOrReadDb() {
        DataSet dataSet = new DataSetImpl();
        DataTable table = dataSet.addTable("emp");
        table.addColumn("empno");
        table.addColumn("ename");
        DataRow row = table.addRow();
        row.setValue("empno", new Integer(7788));
        row.setValue("ename", "hoge");
        dataSet.addTable("dept");
        DataSet ret = reloadOrReadDb(dataSet);
        DataTable table1 = ret.getTable("emp");
        System.out.println(table1);
        DataTable table2 = ret.getTable("dept");
        System.out.println(table2);
        DataRow row1 = table1.getRow(0);
        assertTrue("1", table1.getRowSize() == 1);
        assertTrue("2", table2.getRowSize() > 0);
        assertEquals("3", false, "hoge".equals(row1.getValue("ename")));
    }

    public void testWriteXls() {
        DataSet dataSet = readXls("testdata.xls");
        writeXls("aaa.xls", dataSet);
        DataSet dataSet2 = readXls("aaa.xls");
        assertEquals("1", dataSet, dataSet2);
    }

    public void setUpReadXlsReplaceDbTx() {
        include(J2EE_PATH);
    }

    public void testReadXlsReplaceDbTx() {
        readXlsReplaceDb("testdata.xls");
    }

    public void setUpReadXlsAllReplaceDbTx() {
        include(J2EE_PATH);
    }

    public void testReadXlsAllReplaceDbTx() {
        readXlsAllReplaceDb("testdata.xls");
    }

    public void testAssertMapEquals() {
        DataSet expected = new DataSetImpl();
        DataTable table = expected.addTable("emp");
        table.addColumn("empno");
        DataRow row = table.addRow();
        row.setValue("empno", new BigDecimal(7788));
        Map map = new HashMap();
        map.put("EMPNO", new Integer(7788));
        assertEquals("1", expected, map);
    }

    public void testAssertMapListEquals() {
        DataSet expected = new DataSetImpl();
        DataTable table = expected.addTable("emp");
        table.addColumn("empno");
        DataRow row = table.addRow();
        row.setValue("empno", new BigDecimal(7788));
        Map map = new HashMap();
        map.put("EMPNO", new Integer(7788));
        List list = new ArrayList();
        list.add(map);
        assertEquals("1", expected, list);
    }

    public void testAssertBeanEquals() {
        DataSet expected = new DataSetImpl();
        DataTable table = expected.addTable("emp");
        table.addColumn("aaa");
        DataRow row = table.addRow();
        row.setValue("aaa", "111");
        Hoge bean = new Hoge();
        bean.setAaa("111");
        assertEquals("1", expected, bean);
    }

    public void testAssertBeanListEquals() {
        DataSet expected = new DataSetImpl();
        DataTable table = expected.addTable("emp");
        table.addColumn("aaa");
        DataRow row = table.addRow();
        row.setValue("aaa", "111");
        Hoge bean = new Hoge();
        bean.setAaa("111");
        List list = new ArrayList();
        list.add(bean);
        assertEquals("1", expected, list);
    }

    public static class Hoge {

        private String aaa;

        /**
         * @return Returns the aaa.
         */
        public String getAaa() {
            return aaa;
        }

        /**
         * @param aaa
         *            The aaa to set.
         */
        public void setAaa(String aaa) {
            this.aaa = aaa;
        }
    }
}
