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
package org.seasar.extension.dataset.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.seasar.extension.dataset.DataRow;
import org.seasar.extension.dataset.DataTable;
import org.seasar.extension.dataset.states.RowStates;
import org.seasar.extension.dataset.types.ColumnTypes;

/**
 * @author higa
 *
 */
public class DataRowImplTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testModify() throws Exception {
        DataTable table = new DataTableImpl("hoge");
        table.addColumn("aaa", ColumnTypes.STRING);
        DataRow row = table.addRow();
        row.setValue("aaa", "hoge");
        assertEquals("1", RowStates.CREATED, row.getState());
        row.remove();
        row.setValue("aaa", "hoge");
        assertEquals("2", RowStates.REMOVED, row.getState());
        row.setState(RowStates.UNCHANGED);
        row.setValue("aaa", "hoge");
        assertEquals("3", RowStates.MODIFIED, row.getState());
    }

    /**
     * @throws Exception
     */
    public void testSetValue() throws Exception {
        DataTable table = new DataTableImpl("hoge");
        table.addColumn("aaa", ColumnTypes.STRING);
        DataRow row = table.addRow();
        row.setValue(0, "hoge");
        assertEquals("1", "hoge", row.getValue(0));
        assertEquals("2", "hoge", row.getValue("aaa"));
    }

    /**
     * @throws Exception
     */
    public void testGetValue() throws Exception {
        DataTable table = new DataTableImpl("hoge");
        table.addColumn("aaa_0");
        table.addColumn("bbbCcc");
        DataRow row = table.addRow();
        row.setValue(0, "hoge");
        row.setValue(1, "hoge2");
        assertEquals("1", "hoge", row.getValue("aaa_0"));
        assertEquals("2", "hoge2", row.getValue("bbbCcc"));
        assertEquals("3", "hoge2", row.getValue("bbb_ccc"));
    }

    /**
     * @throws Exception
     */
    public void testEquals() throws Exception {
        DataTable table = new DataTableImpl("hoge");
        table.addColumn("aaa");
        table.addColumn("bbb");
        DataRow row = table.addRow();
        DataRow row2 = table.addRow();
        assertEquals("1", row, row);
        assertEquals("2", row, row2);
        assertEquals("3", false, row.equals(null));
        row.setValue(0, "111");
        row.setValue(1, "222");
        row2.setValue(0, "111");
        row2.setValue(1, "222");
        assertEquals("4", row, row2);
        row.setValue(0, null);
        assertEquals("5", false, row.equals(row2));
        row2.setValue(0, null);
        assertEquals("6", row, row2);
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
        assertEquals("1", row, row2);
    }

    /**
     * @throws Exception
     */
    public void testEquals3() throws Exception {
        DataTable table = new DataTableImpl("hoge");
        table.addColumn("aaa_bbb");
        table.addColumn("ccc_0");
        table.addColumn("dddEee");
        DataTable table2 = new DataTableImpl("hoge2");
        table2.addColumn("aaaBbb");
        table2.addColumn("ccc_0");
        table2.addColumn("ddd_eee");
        DataRow row = table.addRow();
        DataRow row2 = table2.addRow();
        row.setValue("aaa_bbb", "111");
        row.setValue("ccc_0", "222");
        row.setValue("dddEee", "333");
        row2.setValue("aaaBbb", "111");
        row2.setValue("ccc_0", "222");
        row2.setValue("ddd_eee", "333");
        assertEquals("1", row, row2);
    }

    /**
     * @throws Exception
     */
    public void testEquals4() throws Exception {
        DataTable table = new DataTableImpl("hoge");
        table.addColumn("aaa", ColumnTypes.OBJECT);
        DataTable table2 = new DataTableImpl("hoge2");
        table2.addColumn("aaa", ColumnTypes.BINARY);
        DataRow row = table.addRow();
        DataRow row2 = table2.addRow();
        byte[] b1 = new byte[] { '1' };
        byte[] b2 = new byte[] { '1' };
        row.setValue("aaa", b1);
        row2.setValue("aaa", b2);
        assertEquals("1", row, row2);
    }

    /**
     * @throws Exception
     */
    public void testEquals5() throws Exception {
        DataTable table = new DataTableImpl("hoge");
        table.addColumn("aaa");
        table.addColumn("bbb");
        DataTable table2 = new DataTableImpl("hoge2");
        table2.addColumn("aaa");
        table2.addColumn("bbb");
        DataRow row = table.addRow();
        DataRow row2 = table2.addRow();
        row.setValue("aaa", new Timestamp(0));
        row2.setValue("aaa", new java.util.Date(0));
        assertEquals("1", row, row2);
    }

    /**
     * @throws Exception
     */
    public void testEquals6() throws Exception {
        DataTable table = new DataTableImpl("hoge");
        table.addColumn("aaa");
        DataTable table2 = new DataTableImpl("hoge2");
        table2.addColumn("aaa");
        DataRow row = table.addRow();
        DataRow row2 = table2.addRow();
        row.setValue("aaa", "111");
        row2.setValue("aaa", "111  ");
        assertEquals("1", row, row2);
    }

    /**
     * @throws Exception
     */
    public void testCopyFromMap() throws Exception {
        DataTable table = new DataTableImpl("hoge");
        table.addColumn("aaa");
        table.addColumn("bbb");
        DataRow row = table.addRow();
        Map source = new HashMap();
        source.put("AAA", "111");
        source.put("BBB", "222");
        row.copyFrom(source);
        assertEquals("1", "111", row.getValue("aaa"));
        assertEquals("2", "222", row.getValue("bbb"));
    }

    /**
     * @throws Exception
     */
    public void testCopyFromBean() throws Exception {
        DataTable table = new DataTableImpl("hoge");
        table.addColumn("aaa");
        table.addColumn("bbb");
        table.addColumn("test_ccc");
        table.addColumn("ddd");
        DataRow row = table.addRow();
        MyBean bean = new MyBean();
        bean.setAaa("111");
        bean.setBbb("222");
        bean.setTestCcc("333");
        bean.setDdd(444);
        row.copyFrom(bean);
        assertEquals("1", "111", row.getValue("aaa"));
        assertEquals("2", "222", row.getValue("bbb"));
        assertEquals("3", "333", row.getValue("test_ccc"));
        assertEquals("4", new BigDecimal(444), row.getValue("ddd"));
    }

    /**
     * @throws Exception
     */
    public void testCopyFromRow() throws Exception {
        DataTable table = new DataTableImpl("hoge");
        table.addColumn("aaa");
        table.addColumn("bbb");
        table.addColumn("test_ccc");
        DataRow row = table.addRow();
        row.setValue("aaa", "111");
        row.setValue("bbb", "222");
        row.setValue("test_ccc", "333");
        DataRow row2 = table.addRow();
        row2.copyFrom(row);
        assertEquals("1", "111", row2.getValue("aaa"));
        assertEquals("2", "222", row2.getValue("bbb"));
        assertEquals("3", "333", row2.getValue("test_ccc"));
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    /**
     *
     */
    public static class MyBean {

        private String aaa_;

        private String bbb_;

        private String testCcc_;

        private int ddd_;

        /**
         * @return
         */
        public String getAaa() {
            return aaa_;
        }

        /**
         * @param aaa
         */
        public void setAaa(String aaa) {
            aaa_ = aaa;
        }

        /**
         * @return
         */
        public String getBbb() {
            return bbb_;
        }

        /**
         * @param bbb
         */
        public void setBbb(String bbb) {
            bbb_ = bbb;
        }

        /**
         * @return
         */
        public String getTestCcc() {
            return testCcc_;
        }

        /**
         * @param testCcc
         */
        public void setTestCcc(String testCcc) {
            testCcc_ = testCcc;
        }

        /**
         * @return
         */
        public int getDdd() {
            return ddd_;
        }

        /**
         * @param ddd
         */
        public void setDdd(int ddd) {
            ddd_ = ddd;
        }
    }
}