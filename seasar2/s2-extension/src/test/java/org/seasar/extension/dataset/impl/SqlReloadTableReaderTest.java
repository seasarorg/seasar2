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
package org.seasar.extension.dataset.impl;

import java.math.BigDecimal;

import javax.sql.DataSource;

import org.seasar.extension.dataset.DataRow;
import org.seasar.extension.dataset.DataTable;
import org.seasar.extension.dataset.states.RowStates;
import org.seasar.extension.unit.S2TestCase;

/**
 * @author higa
 * 
 */
public class SqlReloadTableReaderTest extends S2TestCase {

    private DataSource ds_;

    /**
     * @throws Exception
     */
    public void testRead() throws Exception {
        DataTable table = new DataTableImpl("emp");
        table.addColumn("empno");
        table.addColumn("ename");
        DataRow row = table.addRow();
        row.setValue("empno", new BigDecimal(7788));
        row.setValue("ename", "SCOTT");
        SqlReloadTableReader reader = new SqlReloadTableReader(ds_, table);
        DataTable ret = reader.read();
        System.out.println(ret);
        assertEquals("1", table, ret);
        assertEquals("2", RowStates.UNCHANGED, ret.getRow(0).getState());
    }

    public void setUp() {
        include("j2ee.dicon");
    }
}