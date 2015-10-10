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
package org.seasar.extension.jdbc.it.auto.select;

import java.util.List;

import org.junit.runner.RunWith;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.it.entity.Department;
import org.seasar.extension.jdbc.it.entity.Employee;
import org.seasar.framework.unit.Seasar2;

import static org.junit.Assert.*;
import static org.seasar.extension.jdbc.it.name.DepartmentNames.*;

/**
 * @author taedium
 * 
 */
@RunWith(Seasar2.class)
public class SingleKeyOneToManyTest {

    private JdbcManager jdbcManager;

    /**
     * 
     * @throws Exception
     */
    public void testLeftOuterJoin() throws Exception {
        List<Department> list =
            jdbcManager
                .from(Department.class)
                .leftOuterJoin("employees")
                .orderBy("departmentId")
                .getResultList();
        assertEquals(4, list.size());
        assertNotNull(list.get(0).employees);
        assertNotNull(list.get(1).employees);
        assertNotNull(list.get(2).employees);
        assertNotNull(list.get(3).employees);
        assertTrue(list.get(3).employees.isEmpty());
    }

    /**
     * 
     * @throws Exception
     */
    public void testLeftOuterJoin_names() throws Exception {
        List<Department> list =
            jdbcManager
                .from(Department.class)
                .leftOuterJoin(employees())
                .orderBy("departmentId")
                .getResultList();
        assertEquals(4, list.size());
        assertNotNull(list.get(0).employees);
        assertNotNull(list.get(1).employees);
        assertNotNull(list.get(2).employees);
        assertNotNull(list.get(3).employees);
        assertTrue(list.get(3).employees.isEmpty());
    }

    /**
     * 
     * @throws Exception
     */
    public void testLeftOuterJoin_noFetch() throws Exception {
        List<Department> list =
            jdbcManager
                .from(Department.class)
                .leftOuterJoin("employees", false)
                .orderBy("departmentId")
                .getResultList();
        assertEquals(4, list.size());
        assertNull(list.get(0).employees);
        assertNull(list.get(1).employees);
        assertNull(list.get(2).employees);
        assertNull(list.get(3).employees);
    }

    /**
     * 
     * @throws Exception
     */
    public void testInnerJoin() throws Exception {
        List<Department> list =
            jdbcManager.from(Department.class).innerJoin("employees").orderBy(
                "departmentId").getResultList();
        assertEquals(3, list.size());
        assertNotNull(list.get(0).employees);
        assertNotNull(list.get(1).employees);
        assertNotNull(list.get(2).employees);
    }

    /**
     * 
     * @throws Exception
     */
    public void testInnerJoin_noFetch() throws Exception {
        List<Department> list =
            jdbcManager
                .from(Department.class)
                .innerJoin("employees", false)
                .orderBy("departmentId")
                .getResultList();
        assertEquals(3, list.size());
        assertNull(list.get(0).employees);
        assertNull(list.get(1).employees);
        assertNull(list.get(2).employees);
    }

    /**
     * 
     * @throws Exception
     */
    public void testInnerJoin_manyToOne_oneToMany() throws Exception {
        Department d =
            jdbcManager
                .from(Department.class)
                .innerJoin("employees")
                .innerJoin("employees.department")
                .id(1)
                .getSingleResult();
        assertNotNull(d);
        List<Employee> employees = d.employees;
        assertNotNull(employees);
        assertEquals(3, employees.size());
        Employee employee = employees.get(0);
        assertNotNull(employee);
        assertEquals(d, employee.department);
    }

}
