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
package org.seasar.extension.jdbc.it;

import java.util.List;

import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.SqlSelect;
import org.seasar.extension.jdbc.it.entity.Employee;
import org.seasar.extension.jdbc.query.SqlSelectImpl;
import org.seasar.extension.unit.S2TestCase;

/**
 * @author taedium
 * 
 */
public class SqlSelectPagingTest extends S2TestCase {

    private static String sql = "select * from Employee order by employee_no";

    private JdbcManager jdbcManager;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        include("jdbc.dicon");
    }

    /**
     * 
     * @throws Exception
     */
    public void test() throws Exception {
        SqlSelect<Employee> query = new SqlSelectImpl<Employee>(jdbcManager,
                Employee.class, sql);
        List<Employee> list = query.offset(0).limit(0).getResultList();
        assertEquals(14, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void test_offsetOnly() throws Exception {
        SqlSelect<Employee> query = new SqlSelectImpl<Employee>(jdbcManager,
                Employee.class, sql);
        List<Employee> list = query.offset(3).limit(0).getResultList();
        assertEquals(11, list.size());
        assertEquals(4, list.get(0).employeeId.intValue());
        assertEquals(14, list.get(10).employeeId.intValue());
    }

    /**
     * 
     * @throws Exception
     */
    public void test_limitOnly() throws Exception {
        SqlSelect<Employee> query = new SqlSelectImpl<Employee>(jdbcManager,
                Employee.class, sql);
        List<Employee> list = query.offset(0).limit(3).getResultList();
        assertEquals(3, list.size());
        assertEquals(1, list.get(0).employeeId.intValue());
        assertEquals(3, list.get(2).employeeId.intValue());
    }

    /**
     * 
     * @throws Exception
     */
    public void test_offset_limit() throws Exception {
        SqlSelect<Employee> query = new SqlSelectImpl<Employee>(jdbcManager,
                Employee.class, sql);
        List<Employee> list = query.offset(3).limit(5).getResultList();
        assertEquals(5, list.size());
        assertEquals(4, list.get(0).employeeId.intValue());
        assertEquals(8, list.get(4).employeeId.intValue());
    }
}
