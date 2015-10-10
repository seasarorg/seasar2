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
import static org.seasar.extension.jdbc.it.name.EmployeeNames.*;

/**
 * @author taedium
 * 
 */
@RunWith(Seasar2.class)
public class SingleKeyManyToOneTest {

    private JdbcManager jdbcManager;

    /**
     * 
     * @throws Exception
     */
    public void testLeftOuterJoin() throws Exception {
        List<Employee> list =
            jdbcManager
                .from(Employee.class)
                .leftOuterJoin("department")
                .getResultList();
        assertEquals(14, list.size());
        for (Employee e : list) {
            assertNotNull(e);
            assertNotNull(e.department);
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testLeftOuterJoin_names() throws Exception {
        List<Employee> list =
            jdbcManager
                .from(Employee.class)
                .leftOuterJoin(department())
                .getResultList();
        assertEquals(14, list.size());
        for (Employee e : list) {
            assertNotNull(e);
            assertNotNull(e.department);
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testLeftOuterJoin_noFetch() throws Exception {
        List<Employee> list =
            jdbcManager
                .from(Employee.class)
                .leftOuterJoin("department", false)
                .getResultList();
        assertEquals(14, list.size());
        for (Employee e : list) {
            assertNotNull(e);
            assertNull(e.department);
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testInnerJoin() throws Exception {
        List<Employee> list =
            jdbcManager
                .from(Employee.class)
                .innerJoin("department")
                .getResultList();
        assertEquals(14, list.size());
        for (Employee e : list) {
            assertNotNull(e);
            assertNotNull(e.department);
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testInnerJoin_noFetch() throws Exception {
        List<Employee> list =
            jdbcManager
                .from(Employee.class)
                .innerJoin("department", false)
                .getResultList();
        assertEquals(14, list.size());
        for (Employee e : list) {
            assertNotNull(e);
            assertNull(e.department);
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testInnerJoin_self() throws Exception {
        List<Employee> list =
            jdbcManager
                .from(Employee.class)
                .innerJoin("manager")
                .getResultList();
        assertEquals(13, list.size());
        for (Employee e : list) {
            assertNotNull(e);
            assertNotNull(e.manager);
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testInnerJoin_oneToMany_manyToOne() throws Exception {
        Employee e =
            jdbcManager
                .from(Employee.class)
                .innerJoin("department")
                .innerJoin("department.employees")
                .id(1)
                .getSingleResult();
        assertNotNull(e);
        Department d = e.department;
        assertNotNull(d);
        List<Employee> employees = d.employees;
        assertNotNull(employees);
        assertEquals(5, employees.size());
        assertTrue(employees.contains(e));
    }

}
