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
package org.seasar.framework.mock.sql;

import java.sql.SQLException;

import junit.framework.TestCase;

/**
 * @author higa
 * 
 */
public class MockResultSetMetaDataTest extends TestCase {

    /**
     * Test method for
     * {@link org.seasar.framework.mock.sql.MockResultSetMetaData#getColumnCount()}.
     * 
     * @throws Exception
     */
    public void testGetColumnCount() throws Exception {
        MockResultSetMetaData meta = new MockResultSetMetaData();
        meta.addColumnMetaData(new MockColumnMetaData());
        assertEquals(1, meta.getColumnCount());
    }

    /**
     * Test method for
     * {@link org.seasar.framework.mock.sql.MockResultSetMetaData#getColumnLabel(int)}.
     * 
     * @throws Exception
     */
    public void testGetColumnLabel() throws Exception {
        MockResultSetMetaData meta = new MockResultSetMetaData();
        MockColumnMetaData columnMeta = new MockColumnMetaData();
        columnMeta.setColumnLabel("aaa");
        meta.addColumnMetaData(columnMeta);
        assertEquals("aaa", meta.getColumnLabel(1));
    }

    /**
     * 
     * @throws Exception
     */
    public void testFindColumn() throws Exception {
        MockResultSetMetaData meta = new MockResultSetMetaData();
        MockColumnMetaData columnMeta = new MockColumnMetaData();
        columnMeta.setColumnName("aaa");
        meta.addColumnMetaData(columnMeta);
        assertEquals(1, meta.findColumn("aaa"));
        try {
            meta.findColumn("bbb");
            fail();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
}
