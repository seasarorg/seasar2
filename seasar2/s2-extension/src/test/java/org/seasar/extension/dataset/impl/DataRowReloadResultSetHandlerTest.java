/*
 * Copyright 2004-2014 the Seasar Foundation and the Others.
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

import javax.sql.DataSource;

import org.seasar.extension.dataset.DataRow;
import org.seasar.extension.dataset.DataTable;
import org.seasar.extension.jdbc.SelectHandler;
import org.seasar.extension.jdbc.impl.BasicSelectHandler;
import org.seasar.extension.unit.S2TestCase;

/**
 * @author higa
 * 
 */
public class DataRowReloadResultSetHandlerTest extends S2TestCase {

    private DataSource ds_;

    /**
     * @throws Exception
     */
    public void testHandle() throws Exception {
        String sql = "select empno, ename from emp where empno = ?";
        DataTable table = new DataTableImpl("emp");
        table.addColumn("empno").setPrimaryKey(true);
        table.addColumn("ename");
        DataRow row = table.addRow();
        row.setValue("empno", new BigDecimal(7788));
        row.setValue("ename", "SCOTT");
        DataTable newTable = new DataTableImpl("emp");
        newTable.addColumn("empno").setPrimaryKey(true);
        newTable.addColumn("ename");
        DataRow newRow = newTable.addRow();
        SelectHandler handler = new BasicSelectHandler(ds_, sql,
                new DataRowReloadResultSetHandler(newRow));
        handler.execute(new Object[] { new Integer(7788) });
        System.out.println(newRow);
        assertEquals("2", row, newRow);
    }

    /**
     * 
     * @throws Exception
     */
    public void testHandle2() throws Exception {
        String sql = "select dept_no, d_name from dept3 where dept_no = ?";
        DataTable table = new DataTableImpl("dept3");
        table.addColumn("dept_no").setPrimaryKey(true);
        table.addColumn("d_name");
        DataRow row = table.addRow();
        row.setValue("dept_no", new BigDecimal(20));
        row.setValue("d_name", "RESEARCH");
        DataTable newTable = new DataTableImpl("dept3");
        newTable.addColumn("dept_no").setPrimaryKey(true);
        newTable.addColumn("d_name");
        DataRow newRow = newTable.addRow();
        SelectHandler handler = new BasicSelectHandler(ds_, sql,
                new DataRowReloadResultSetHandler(newRow));
        handler.execute(new Object[] { new Integer(20) });
        System.out.println(newRow);
        assertEquals(row, newRow);
    }

    public void setUp() {
        include("j2ee.dicon");
    }
}