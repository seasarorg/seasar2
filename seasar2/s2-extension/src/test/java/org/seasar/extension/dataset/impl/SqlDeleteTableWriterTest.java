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

import org.seasar.extension.dataset.DataRow;
import org.seasar.extension.dataset.DataTable;
import org.seasar.extension.dataset.TableWriter;
import org.seasar.extension.unit.S2TestCase;

/**
 * @author higa
 * 
 */
public class SqlDeleteTableWriterTest extends S2TestCase {

    /**
     * @throws Exception
     */
    public void testWriteTx() throws Exception {
        DataTable table = new DataTableImpl("emp");
        table.addColumn("empno");
        table.addColumn("ename");
        DataRow row = table.addRow();
        row.setValue("empno", new Integer(9900));
        row.setValue("ename", "hoge");
        TableWriter writer = new SqlTableWriter(getDataSource());
        writer.write(table);
        TableWriter writer2 = new SqlDeleteTableWriter(getDataSource());
        writer2.write(table);
        DataTable table2 = readDbByTable("emp", "empno = 9900");
        assertEquals("1", 0, table2.getRowSize());
    }

    public void setUp() {
        include("j2ee.dicon");
    }
}