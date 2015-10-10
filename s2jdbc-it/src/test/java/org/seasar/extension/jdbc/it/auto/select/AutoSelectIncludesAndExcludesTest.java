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

import org.junit.runner.RunWith;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.it.entity.Department;
import org.seasar.extension.jdbc.it.entity.Employee;
import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.unit.Seasar2;

import static org.junit.Assert.*;

/**
 * @author jfut
 * 
 */
@RunWith(Seasar2.class)
public class AutoSelectIncludesAndExcludesTest {

    private JdbcManager jdbcManager;

    /**
     * 
     * @throws Exception
     */
    public void testIncludes() throws Exception {
        Employee employee =
            jdbcManager
                .from(Employee.class)
                .where(new SimpleWhere().eq("employeeId", 1))
                .includes("employeeName", "hiredate")
                .getSingleResult();
        assertNotNull(employee);
        assertNotSame(0, employee.employeeId);
        assertEquals(0, employee.employeeNo);
        assertNotNull(employee.employeeName);
        assertEquals(0, employee.managerId);
        assertNotNull(employee.hiredate);
        assertNull(employee.salary);
        assertEquals(0, employee.departmentId);
        assertEquals(0, employee.addressId);
        assertEquals(0, employee.version);
    }

    /**
     * 
     * @throws Exception
     */
    public void testExcludes() throws Exception {
        Employee employee =
            jdbcManager
                .from(Employee.class)
                .where(new SimpleWhere().eq("employeeId", 1))
                .excludes("employeeId", "employeeName", "hiredate")
                .getSingleResult();
        assertNotNull(employee);
        assertNotSame(0, employee.employeeId);
        assertNotSame(0, employee.employeeNo);
        assertNull(employee.employeeName);
        assertNotSame(0, employee.managerId);
        assertNull(employee.hiredate);
        assertNotNull(employee.salary);
        assertNotSame(0, employee.departmentId);
        assertNotSame(0, employee.addressId);
        assertNotSame(0, employee.version);
    }

    /**
     * 
     * @throws Exception
     */
    public void testIncludesAndExcludes() throws Exception {
        Employee employee =
            jdbcManager
                .from(Employee.class)
                .leftOuterJoin("address")
                .where(new SimpleWhere().eq("employeeId", 1))
                .includes("employeeName", "hiredate", "address.street")
                .excludes("employeeName", "address", "address.street")
                .getSingleResult();
        assertNotNull(employee);
        assertNotSame(0, employee.employeeId);
        assertEquals(0, employee.employeeNo);
        assertNotNull(employee.employeeName);
        assertEquals(0, employee.managerId);
        assertNotNull(employee.hiredate);
        assertNull(employee.salary);
        assertEquals(0, employee.departmentId);
        assertEquals(0, employee.addressId);
        assertEquals(0, employee.version);
        assertNotNull(employee.address);
        assertNotSame(0, employee.address.addressId);
        assertNotNull(employee.address.street);
        assertEquals(0, employee.address.version);
    }

    /**
     * 
     * @throws Exception
     */
    public void testIncludesAndExcludes_oneToMany() throws Exception {
        Department department =
            jdbcManager
                .from(Department.class)
                .leftOuterJoin("employees")
                .leftOuterJoin("employees.address")
                .where(new SimpleWhere().eq("departmentId", 1))
                .includes(
                    "departmentNo",
                    "departmentName",
                    "employees.employeeName",
                    "employees.hiredate",
                    "employees.address.street")
                .excludes(
                    "employees.employeeName",
                    "employees.address",
                    "employees.address.street")
                .getSingleResult();
        assertNotNull(department);
        assertNotSame(0, department.departmentId);
        assertNotNull(department.departmentName);
        assertNull(department.location);
        assertEquals(0, department.version);
        for (Employee employee : department.employees) {
            assertNotNull(employee);
            assertNotSame(0, employee.employeeId);
            assertEquals(0, employee.employeeNo);
            assertNotNull(employee.employeeName);
            assertEquals(0, employee.managerId);
            assertNotNull(employee.hiredate);
            assertNull(employee.salary);
            assertEquals(0, employee.departmentId);
            assertEquals(0, employee.addressId);
            assertEquals(0, employee.version);
            assertNotNull(employee.address);
            assertNotSame(0, employee.address.addressId);
            assertNotNull(employee.address.street);
            assertEquals(0, employee.address.version);
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testIncludesAndExcludes_oneToMany_joinName() throws Exception {
        Department department =
            jdbcManager
                .from(Department.class)
                .leftOuterJoin("employees")
                .leftOuterJoin("employees.address")
                .where(new SimpleWhere().eq("departmentId", 1))
                .includes("departmentNo", "departmentName", "employees.address")
                .excludes("employees", "employees.address")
                .getSingleResult();
        assertNotNull(department);
        assertNotSame(0, department.departmentId);
        assertNotNull(department.departmentName);
        assertNull(department.location);
        assertEquals(0, department.version);
        for (Employee employee : department.employees) {
            assertNotNull(employee);
            assertNotSame(0, employee.employeeId);
            assertEquals(0, employee.employeeNo);
            assertNull(employee.employeeName);
            assertEquals(0, employee.managerId);
            assertNull(employee.hiredate);
            assertNull(employee.salary);
            assertEquals(0, employee.departmentId);
            assertEquals(0, employee.addressId);
            assertEquals(0, employee.version);
            assertNotNull(employee.address);
            assertNotSame(0, employee.address.addressId);
            assertNotNull(employee.address.street);
            assertNotSame(0, employee.address.version);
        }
    }

}
