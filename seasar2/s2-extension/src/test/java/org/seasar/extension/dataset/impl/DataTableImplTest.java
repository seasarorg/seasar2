/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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
import java.util.ArrayList;
import java.util.List;

import org.seasar.extension.dataset.DataRow;
import org.seasar.extension.dataset.DataTable;
import org.seasar.extension.dataset.states.RowStates;
import org.seasar.extension.dataset.types.ColumnTypes;
import org.seasar.extension.unit.S2TestCase;

/**
 * @author higa
 *
 */
public class DataTableImplTest extends S2TestCase {

    /**
     * @throws Exception
     */
    public void testRemoveRows() throws Exception {
        DataTable table = new DataTableImpl("hoge");
        table.addColumn("aaa", ColumnTypes.STRING);
        DataRow row = table.addRow();
        row.setValue("aaa", "1");
        DataRow row2 = table.addRow();
        row2.setValue("aaa", "2");
        row2.remove();
        DataRow row3 = table.addRow();
        row3.setValue("aaa", "3");
        row3.remove();
        DataRow[] rows = table.removeRows();
        assertEquals("1", 2, rows.length);
        assertEquals("2", 2, table.getRemovedRowSize());
        assertEquals("3", 1, table.getRowSize());
        assertEquals("4", "1", table.getRow(0).getValue("aaa"));
        assertSame("5", row2, table.getRemovedRow(0));
        assertSame("6", row3, table.getRemovedRow(1));
    }

    /**
     * @throws Exception
     */
    public void testEquals() throws Exception {
        DataTable table = new DataTableImpl("hoge");
        DataTable table2 = new DataTableImpl("hoge");
        table.addColumn("aaa");
        table2.addColumn("aaa");
        DataRow row = table.addRow();
        row.setValue("aaa", "1");
        row = table.addRow();
        row.setValue("aaa", "2");
        row.remove();
        DataRow row2 = table2.addRow();
        row2.setValue("aaa", "1");
        row2 = table2.addRow();
        row2.setValue("aaa", "2");
        row2.remove();
        assertEquals("1", table, table2);
        table.removeRows();
        table2.removeRows();
        assertEquals("2", table, table2);
        table2.getRow(0).setValue(0, "11");
        assertEquals("3", false, table.equals(table2));
    }

    /**
     * @throws Exception
     */
    public void testEquals2() throws Exception {
        DataTable table = new DataTableImpl("hoge");
        table.addColumn("aaa");
        table.addColumn("bbb");
        DataTable table2 = new DataTableImpl("hoge2");
        table2.addColumn("ccc");
        table2.addColumn("aaa");
        table2.addColumn("bbb");
        DataRow row = table.addRow();
        DataRow row2 = table2.addRow();
        row.setValue("aaa", "111");
        row.setValue("bbb", "222");
        row2.setValue("aaa", "111");
        row2.setValue("bbb", "222");
        row2.setValue("ccc", "333");
        assertEquals("1", table, table2);
    }

    /**
     * @throws Exception
     */
    public void testEquals3() throws Exception {
        DataTable table = new DataTableImpl("hoge");
        table.addColumn("aaa");
        DataTable table2 = new DataTableImpl("hoge2");
        table2.addColumn("aaa", ColumnTypes.STRING);
        DataRow row = table.addRow();
        DataRow row2 = table2.addRow();
        row.setValue("aaa", new BigDecimal("111"));
        row2.setValue("aaa", "111");
        assertEquals("1", table, table2);
    }

    /**
     * @throws Exception
     */
    public void testSetupColumns() throws Exception {
        DataTable table = new DataTableImpl("hoge");
        table.setupColumns(MyBean.class);
        assertEquals("1", 2, table.getColumnSize());
        assertEquals("2", ColumnTypes.BIGDECIMAL, table.getColumnType("myInt"));
        assertEquals("3", ColumnTypes.STRING, table.getColumnType("myString"));
    }

    /**
     * @throws Exception
     */
    public void testCopyFromList() throws Exception {
        DataTable table = new DataTableImpl("hoge");
        table.setupColumns(MyBean.class);
        List list = new ArrayList();
        MyBean bean = new MyBean();
        bean.setMyInt(1);
        bean.setMyString("a");
        list.add(bean);
        bean = new MyBean();
        bean.setMyInt(2);
        bean.setMyString("b");
        list.add(bean);
        table.copyFrom(list);
        assertEquals("1", 2, table.getRowSize());
        assertEquals("2", new BigDecimal(1), table.getRow(0).getValue("myInt"));
        assertEquals("3", "a", table.getRow(0).getValue("myString"));
        assertEquals("4", new BigDecimal(2), table.getRow(1).getValue("myInt"));
        assertEquals("5", "b", table.getRow(1).getValue("myString"));
        assertEquals("6", RowStates.UNCHANGED, table.getRow(0).getState());
    }

    /**
     * @throws Exception
     */
    public void testCopyFromBeanOrMap() throws Exception {
        DataTable table = new DataTableImpl("hoge");
        table.setupColumns(MyBean.class);
        MyBean bean = new MyBean();
        bean.setMyInt(1);
        bean.setMyString("a");
        table.copyFrom(bean);
        assertEquals("1", 1, table.getRowSize());
        assertEquals("2", new BigDecimal(1), table.getRow(0).getValue("myInt"));
        assertEquals("3", "a", table.getRow(0).getValue("myString"));
        assertEquals("4", RowStates.UNCHANGED, table.getRow(0).getState());
    }

    /**
     * @throws Exception
     */
    public void testHasColumn() throws Exception {
        DataTable table = new DataTableImpl("hoge");
        table.addColumn("aaa_0");
        table.addColumn("bbbCcc");
        assertEquals("1", true, table.hasColumn("aaa_0"));
        assertEquals("2", true, table.hasColumn("bbb_ccc"));
    }

    /**
     * @throws Exception
     */
    public void testGetColumn() throws Exception {
        DataTable table = new DataTableImpl("hoge");
        table.addColumn("aaa_0");
        table.addColumn("bbbCcc");
        assertNotNull("1", table.getColumn("aaa_0"));
        assertNotNull("2", table.getColumn("bbb_ccc"));
    }

    /**
     * @throws Exception
     */
    public void testGetColumn2() throws Exception {
        DataTable table = new DataTableImpl("hoge");
        table.addColumn("aaa_bbb");
        assertNotNull("1", table.getColumn("aaaBbb"));
    }

    /**
     * @throws Exception
     */
    public void setUpSetupMetaData() throws Exception {
        include("j2ee.dicon");
    }

    /**
     * @throws Exception
     */
    public void testSetupMetaData() throws Exception {
        DataTable table = new DataTableImpl("emp");
        table.addColumn("empno");
        table.setupMetaData(getDatabaseMetaData());
        assertEquals("1", ColumnTypes.BIGDECIMAL, table.getColumnType("empno"));
    }

    /**
     *
     */
    public static class MyBean {

        private int myInt_;

        private String myString_;

        /**
         * @return
         */
        public int getMyInt() {
            return myInt_;
        }

        /**
         * @param myInt
         */
        public void setMyInt(int myInt) {
            myInt_ = myInt;
        }

        /**
         * @return
         */
        public String getMyString() {
            return myString_;
        }

        /**
         * @param myString
         */
        public void setMyString(String myString) {
            myString_ = myString;
        }
    }
}