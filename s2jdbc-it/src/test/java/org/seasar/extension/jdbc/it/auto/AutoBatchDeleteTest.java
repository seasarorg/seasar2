/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.OptimisticLockException;

import org.junit.runner.RunWith;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.it.entity.CompKeyEmployee;
import org.seasar.extension.jdbc.it.entity.Employee;
import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.unit.Seasar2;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
@RunWith(Seasar2.class)
public class AutoBatchDeleteTest {

    private JdbcManager jdbcManager;

    /**
     * 
     * @throws Exception
     */
    public void testExecute() throws Exception {
        List<Employee> list = new ArrayList<Employee>();
        Employee employee = new Employee();
        employee.employeeId = 1;
        employee.version = 1;
        list.add(employee);
        Employee employee2 = new Employee();
        employee2.employeeId = 2;
        employee2.version = 1;
        list.add(employee2);

        int[] result = jdbcManager.deleteBatch(list).execute();
        assertEquals(2, result.length);

        employee =
            jdbcManager.from(Employee.class).where(
                new SimpleWhere().eq("employeeId", 1)).getSingleResult();
        assertNull(employee);

        employee =
            jdbcManager.from(Employee.class).where(
                new SimpleWhere().eq("employeeId", 2)).getSingleResult();
        assertNull(employee);
    }

    /**
     * 
     * @throws Exception
     */
    public void testExecute_ignoreVersion() throws Exception {
        List<Employee> list = new ArrayList<Employee>();
        Employee employee = new Employee();
        employee.employeeId = 1;
        employee.version = 99;
        list.add(employee);
        Employee employee2 = new Employee();
        employee2.employeeId = 2;
        employee2.version = 99;
        list.add(employee2);

        int[] result = jdbcManager.deleteBatch(list).ignoreVersion().execute();
        assertEquals(2, result.length);

        employee =
            jdbcManager.from(Employee.class).where(
                new SimpleWhere().eq("employeeId", 1)).getSingleResult();
        assertNull(employee);

        employee =
            jdbcManager.from(Employee.class).where(
                new SimpleWhere().eq("employeeId", 2)).getSingleResult();
        assertNull(employee);
    }

    /**
     * 
     * @throws Exception
     */
    public void testCompKey() throws Exception {
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

        int[] result = jdbcManager.deleteBatch(list).execute();
        assertEquals(2, result.length);

        employee =
            jdbcManager
                .from(CompKeyEmployee.class)
                .where(
                    new SimpleWhere().eq("employeeId1", 1).eq("employeeId2", 1))
                .getSingleResult();
        assertNull(employee);

        employee =
            jdbcManager
                .from(CompKeyEmployee.class)
                .where(
                    new SimpleWhere().eq("employeeId1", 2).eq("employeeId2", 2))
                .getSingleResult();
        assertNull(employee);
    }

    /**
     * 
     * @throws Exception
     */
    public void testOptimisticLockException() throws Exception {
        Employee employee1 =
            jdbcManager
                .from(Employee.class)
                .where("employeeId = ?", 1)
                .getSingleResult();
        Employee employee2 =
            jdbcManager
                .from(Employee.class)
                .where("employeeId = ?", 1)
                .getSingleResult();
        Employee employee3 =
            jdbcManager
                .from(Employee.class)
                .where("employeeId = ?", 2)
                .getSingleResult();
        jdbcManager.delete(employee1).execute();
        int[] result = null;
        try {
            result = jdbcManager.deleteBatch(employee2, employee3).execute();
        } catch (OptimisticLockException ignore) {
            return;
        }
        if (!containsSuccessNoInfo(result)) {
            fail();
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testOptimisticLockException_ignoreVersion() throws Exception {
        Employee employee1 =
            jdbcManager
                .from(Employee.class)
                .where("employeeId = ?", 1)
                .getSingleResult();
        Employee employee2 =
            jdbcManager
                .from(Employee.class)
                .where("employeeId = ?", 1)
                .getSingleResult();
        Employee employee3 =
            jdbcManager
                .from(Employee.class)
                .where("employeeId = ?", 2)
                .getSingleResult();
        jdbcManager.delete(employee1).execute();
        jdbcManager.deleteBatch(employee2, employee3).ignoreVersion().execute();
    }

    /**
     * 
     * @throws Exception
     */
    public void testSuppresOptimisticLockException() throws Exception {
        Employee employee1 =
            jdbcManager
                .from(Employee.class)
                .where("employeeId = ?", 1)
                .getSingleResult();
        Employee employee2 =
            jdbcManager
                .from(Employee.class)
                .where("employeeId = ?", 1)
                .getSingleResult();
        Employee employee3 =
            jdbcManager
                .from(Employee.class)
                .where("employeeId = ?", 2)
                .getSingleResult();
        jdbcManager.delete(employee1).execute();
        jdbcManager
            .deleteBatch(employee2, employee3)
            .suppresOptimisticLockException()
            .execute();
    }

    private boolean containsSuccessNoInfo(int[] batchResult) {
        for (int i : batchResult) {
            if (i == Statement.SUCCESS_NO_INFO) {
                return true;
            }
        }
        return false;
    }
}
