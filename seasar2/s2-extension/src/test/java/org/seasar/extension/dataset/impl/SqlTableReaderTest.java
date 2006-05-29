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

import javax.sql.DataSource;

import org.seasar.extension.dataset.DataTable;
import org.seasar.extension.dataset.states.RowStates;
import org.seasar.extension.unit.S2TestCase;

public class SqlTableReaderTest extends S2TestCase {

    private DataSource ds_;

    public SqlTableReaderTest(String arg0) {
        super(arg0);
    }

    public void testRead() throws Exception {
        SqlTableReader reader = new SqlTableReader(ds_);
        reader.setTable("emp");
        DataTable ret = reader.read();
        System.out.println(ret);
        assertEquals("1", 14, ret.getRowSize());
        assertEquals("2", RowStates.UNCHANGED, ret.getRow(0).getState());
    }

    public void testRead4() throws Exception {
        SqlTableReader reader = new SqlTableReader(ds_);
        reader.setTable("emp", "", "ename");
        DataTable ret = reader.read();
        System.out.println(ret);
        assertEquals("1", 14, ret.getRowSize());
        assertEquals("2", "ADAMS", ret.getRow(0).getValue("ename"));
    }

    public void testRead2() throws Exception {
        SqlTableReader reader = new SqlTableReader(ds_);
        reader.setTable("emp", "empno = 7788");
        DataTable ret = reader.read();
        System.out.println(ret);
        assertEquals("1", 1, ret.getRowSize());
    }

    public void testRead3() throws Exception {
        SqlTableReader reader = new SqlTableReader(ds_);
        reader.setSql("select * from emp", "emp");
        DataTable ret = reader.read();
        System.out.println(ret);
        assertEquals("1", 14, ret.getRowSize());
    }

    public void setUp() {
        include("j2ee.dicon");
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(SqlTableReaderTest.class);
    }

}
