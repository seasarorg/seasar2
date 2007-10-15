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
package org.seasar.extension.jdbc.it.auto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.it.entity.CompKeyEmployee;
import org.seasar.extension.jdbc.it.entity.Employee;
import org.seasar.extension.unit.S2TestCase;

/**
 * @author taedium
 * 
 */
public class AutoBatchDeleteTest extends S2TestCase {

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
    public void testTx() throws Exception {
        List<Employee> list = new ArrayList<Employee>();
        Employee employee = new Employee();
        employee.employeeId = 1;
        employee.version = 1;
        list.add(employee);
        Employee employee2 = new Employee();
        employee2.employeeId = 2;
        employee2.version = 1;
        list.add(employee2);

        int[] result = jdbcManager.deleteBatch(list).executeBatch();
        assertEquals(2, result.length);

        Map<String, Object> m = new HashMap<String, Object>();
        m.put("employeeId", 1);
        employee = jdbcManager.from(Employee.class).where(m).getSingleResult();
        assertNull(employee);

        m.put("employeeId", 2);
        employee = jdbcManager.from(Employee.class).where(m).getSingleResult();
        assertNull(employee);
    }

    /**
     * 
     * @throws Exception
     */
    public void test_ignoreVersionTx() throws Exception {
        List<Employee> list = new ArrayList<Employee>();
        Employee employee = new Employee();
        employee.employeeId = 1;
        employee.version = 99;
        list.add(employee);
        Employee employee2 = new Employee();
        employee2.employeeId = 2;
        employee2.version = 99;
        list.add(employee2);

        int[] result = jdbcManager.deleteBatch(list).ignoreVersion().executeBatch();
        assertEquals(2, result.length);

        Map<String, Object> m = new HashMap<String, Object>();
        m.put("employeeId", 1);
        employee = jdbcManager.from(Employee.class).where(m).getSingleResult();
        assertNull(employee);

        m.put("employeeId", 2);
        employee = jdbcManager.from(Employee.class).where(m).getSingleResult();
        assertNull(employee);
    }

    /**
     * 
     * @throws Exception
     */
    public void testCompKeyTx() throws Exception {
        List<CompKeyEmployee> list = new ArrayList<CompKeyEmployee>();
        CompKeyEmployee employee = new CompKeyEmployee();
        employee.employeeId1 = 1;
        employee.employeeId2 = 1;
        employee.version = 1;
        list.add(employee);
        CompKeyEmployee employee2 = new CompKeyEmployee();
        employee2.employeeId1 = 2;
        employee2.employeeId2 = 2;
        employee2.version = 1;
        list.add(employee2);

        int[] result = jdbcManager.deleteBatch(list).executeBatch();
        assertEquals(2, result.length);

        Map<String, Object> m = new HashMap<String, Object>();
        m.put("employeeId1", 1);
        m.put("employeeId2", 1);
        employee = jdbcManager.from(CompKeyEmployee.class).where(m)
                .getSingleResult();
        assertNull(employee);

        m.put("employeeId1", 2);
        m.put("employeeId2", 2);
        employee = jdbcManager.from(CompKeyEmployee.class).where(m)
                .getSingleResult();
        assertNull(employee);
    }

}
