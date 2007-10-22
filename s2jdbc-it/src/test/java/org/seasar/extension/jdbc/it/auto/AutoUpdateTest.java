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

import java.util.HashMap;
import java.util.Map;

import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.exception.SOptimisticLockException;
import org.seasar.extension.jdbc.it.entity.CompKeyDepartment;
import org.seasar.extension.jdbc.it.entity.Department;
import org.seasar.extension.jdbc.it.entity.Employee;
import org.seasar.extension.unit.S2TestCase;

/**
 * @author taedium
 * 
 */
public class AutoUpdateTest extends S2TestCase {

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
    public void testExecuteTx() throws Exception {
        Department department = new Department();
        department.departmentId = 1;
        department.departmentName = "hoge";
        department.version = 1;
        int result = jdbcManager.update(department).execute();
        assertEquals(1, result);
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("departmentId", 1);
        department = jdbcManager.from(Department.class).where(m)
                .getSingleResult();
        assertEquals(1, department.departmentId);
        assertEquals(0, department.departmentNo);
        assertEquals("hoge", department.departmentName);
        assertNull(department.location);
        assertEquals(2, department.version);
    }

    /**
     * 
     * @throws Exception
     */
    public void testExecute_includesVersionTx() throws Exception {
        Department department = new Department();
        department.departmentId = 1;
        department.departmentName = "hoge";
        department.version = 100;
        int result = jdbcManager.update(department).includesVersion().execute();
        assertEquals(1, result);
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("departmentId", 1);
        department = jdbcManager.from(Department.class).where(m)
                .getSingleResult();
        assertEquals(1, department.departmentId);
        assertEquals(0, department.departmentNo);
        assertEquals("hoge", department.departmentName);
        assertNull(department.location);
        assertEquals(100, department.version);
    }

    /**
     * 
     * @throws Exception
     */
    public void testExecute_excludesNullTx() throws Exception {
        Department department = new Department();
        department.departmentId = 1;
        department.departmentName = "hoge";
        department.version = 1;
        int result = jdbcManager.update(department).excludesNull().execute();
        assertEquals(1, result);
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("departmentId", 1);
        department = jdbcManager.from(Department.class).where(m)
                .getSingleResult();
        assertEquals(1, department.departmentId);
        assertEquals(0, department.departmentNo);
        assertEquals("hoge", department.departmentName);
        assertEquals("NEW YORK", department.location);
        assertEquals(2, department.version);
    }

    /**
     * 
     * @throws Exception
     */
    public void testExecute_includesTx() throws Exception {
        Department department = new Department();
        department.departmentId = 1;
        department.departmentNo = 99;
        department.departmentName = "hoge";
        department.location = "foo";
        department.version = 1;
        int result = jdbcManager.update(department).includes("departmentName",
                "location").execute();
        assertEquals(1, result);
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("departmentId", 1);
        department = jdbcManager.from(Department.class).where(m)
                .getSingleResult();
        assertEquals(1, department.departmentId);
        assertEquals(10, department.departmentNo);
        assertEquals("hoge", department.departmentName);
        assertEquals("foo", department.location);
        assertEquals(2, department.version);
    }

    /**
     * 
     * @throws Exception
     */
    public void testExecute_excludesTx() throws Exception {
        Department department = new Department();
        department.departmentId = 1;
        department.departmentNo = 99;
        department.departmentName = "hoge";
        department.location = "foo";
        department.version = 1;
        int result = jdbcManager.update(department).excludes("departmentName",
                "location").execute();
        assertEquals(1, result);
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("departmentId", 1);
        department = jdbcManager.from(Department.class).where(m)
                .getSingleResult();
        assertEquals(1, department.departmentId);
        assertEquals(99, department.departmentNo);
        assertEquals("ACCOUNTING", department.departmentName);
        assertEquals("NEW YORK", department.location);
        assertEquals(2, department.version);
    }

    /**
     * 
     * @throws Exception
     */
    public void testExecute_changeFromTx() throws Exception {
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("departmentId", 1);
        Department before = jdbcManager.from(Department.class).where(m)
                .getSingleResult();

        Department department = new Department();
        department.departmentId = before.departmentId;
        department.departmentNo = before.departmentNo;
        department.departmentName = "hoge";
        department.location = before.location;
        department.version = before.version;

        int result = jdbcManager.update(department).changedFrom(before)
                .execute();
        assertEquals(1, result);
        department = jdbcManager.from(Department.class).where(m)
                .getSingleResult();
        assertEquals(before.departmentId, department.departmentId);
        assertEquals(before.departmentNo, department.departmentNo);
        assertEquals("hoge", department.departmentName);
        assertEquals(before.location, department.location);
        assertEquals(before.version + 1, department.version);
    }

    /**
     * 
     * @throws Exception
     */
    public void testCompKeyTx() throws Exception {
        CompKeyDepartment department = new CompKeyDepartment();
        department.departmentId1 = 1;
        department.departmentId2 = 1;
        department.departmentName = "hoge";
        department.version = 1;
        int result = jdbcManager.update(department).execute();
        assertEquals(1, result);
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("departmentId1", 1);
        m.put("departmentId2", 1);
        department = jdbcManager.from(CompKeyDepartment.class).where(m)
                .getSingleResult();
        assertEquals(1, department.departmentId1);
        assertEquals(1, department.departmentId2);
        assertEquals(0, department.departmentNo);
        assertEquals("hoge", department.departmentName);
        assertNull(department.location);
        assertEquals(2, department.version);
    }

    /**
     * 
     * @throws Exception
     */
    public void testOptimisticLockExceptionTx() throws Exception {
        Employee employee1 = jdbcManager.from(Employee.class).where(
                "employeeId = ?", 1).getSingleResult();
        Employee employee2 = jdbcManager.from(Employee.class).where(
                "employeeId = ?", 1).getSingleResult();
        jdbcManager.update(employee1).execute();
        try {
            jdbcManager.update(employee2).execute();
            fail();
        } catch (SOptimisticLockException ignore) {
        }
    }
}
