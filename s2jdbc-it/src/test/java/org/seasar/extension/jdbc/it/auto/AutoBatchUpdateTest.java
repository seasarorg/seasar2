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
package org.seasar.extension.jdbc.it.auto;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.OptimisticLockException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.exception.NoIdPropertyRuntimeException;
import org.seasar.extension.jdbc.it.entity.CompKeyDepartment;
import org.seasar.extension.jdbc.it.entity.ConcreteDepartment;
import org.seasar.extension.jdbc.it.entity.Department;
import org.seasar.extension.jdbc.it.entity.Department2;
import org.seasar.extension.jdbc.it.entity.Department3;
import org.seasar.extension.jdbc.it.entity.Department4;
import org.seasar.extension.jdbc.it.entity.Employee;
import org.seasar.extension.jdbc.it.entity.NoId;
import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.unit.Seasar2;
import org.seasar.framework.unit.annotation.Prerequisite;

import static junit.framework.Assert.*;

/**
 * @author taedium
 * 
 */
@RunWith(Seasar2.class)
public class AutoBatchUpdateTest {

    private JdbcManager jdbcManager;

    /**
     * 
     * @throws Exception
     */
    public void testExecute() throws Exception {
        List<Department> list = new ArrayList<Department>();
        Department department = new Department();
        department.departmentId = 1;
        department.departmentName = "hoge";
        department.departmentNo = 10;
        department.version = 1;
        list.add(department);
        Department department2 = new Department();
        department2.departmentId = 2;
        department2.departmentNo = 20;
        department2.departmentName = "foo";
        department2.version = 1;
        list.add(department2);

        int[] result = jdbcManager.updateBatch(list).execute();
        assertEquals(2, result.length);
        assertEquals(2, department.version);
        assertEquals(2, department2.version);

        department =
            jdbcManager.from(Department.class).where(
                new SimpleWhere().eq("departmentId", 1)).getSingleResult();
        assertEquals(1, department.departmentId);
        assertEquals(10, department.departmentNo);
        assertEquals("hoge", department.departmentName);
        assertNull(department.location);
        assertEquals(2, department.version);

        department =
            jdbcManager.from(Department.class).where(
                new SimpleWhere().eq("departmentId", 2)).getSingleResult();
        assertEquals(2, department.departmentId);
        assertEquals(20, department.departmentNo);
        assertEquals("foo", department.departmentName);
        assertNull(department.location);
        assertEquals(2, department.version);
    }

    /**
     * 
     * @throws Exception
     */
    public void testExecute_includesVersion() throws Exception {
        List<Department> list = new ArrayList<Department>();
        Department department = new Department();
        department.departmentId = 1;
        department.departmentNo = 10;
        department.departmentName = "hoge";
        department.version = 100;
        list.add(department);
        Department department2 = new Department();
        department2.departmentId = 2;
        department2.departmentNo = 20;
        department2.departmentName = "foo";
        department2.version = 200;
        list.add(department2);

        int[] result =
            jdbcManager.updateBatch(list).includesVersion().execute();
        assertEquals(2, result.length);
        assertEquals(100, department.version);
        assertEquals(200, department2.version);

        department =
            jdbcManager.from(Department.class).where(
                new SimpleWhere().eq("departmentId", 1)).getSingleResult();
        assertEquals(1, department.departmentId);
        assertEquals(10, department.departmentNo);
        assertEquals("hoge", department.departmentName);
        assertNull(department.location);
        assertEquals(100, department.version);

        department =
            jdbcManager.from(Department.class).where(
                new SimpleWhere().eq("departmentId", 2)).getSingleResult();
        assertEquals(2, department.departmentId);
        assertEquals(20, department.departmentNo);
        assertEquals("foo", department.departmentName);
        assertNull(department.location);
        assertEquals(200, department.version);
    }

    /**
     * 
     * @throws Exception
     */
    public void testExecute_includes() throws Exception {
        List<Department> list = new ArrayList<Department>();
        Department department = new Department();
        department.departmentId = 1;
        department.departmentNo = 100;
        department.departmentName = "hoge";
        department.location = "foo";
        department.version = 1;
        list.add(department);
        Department department2 = new Department();
        department2.departmentId = 2;
        department2.departmentNo = 200;
        department2.departmentName = "bar";
        department2.location = "baz";
        department2.version = 1;
        list.add(department2);

        int[] result =
            jdbcManager
                .updateBatch(list)
                .includes("departmentName", "location")
                .execute();
        assertEquals(2, result.length);
        assertEquals(2, department.version);
        assertEquals(2, department2.version);

        department =
            jdbcManager.from(Department.class).where(
                new SimpleWhere().eq("departmentId", 1)).getSingleResult();
        assertEquals(1, department.departmentId);
        assertEquals(10, department.departmentNo);
        assertEquals("hoge", department.departmentName);
        assertEquals("foo", department.location);
        assertEquals(2, department.version);

        department =
            jdbcManager.from(Department.class).where(
                new SimpleWhere().eq("departmentId", 2)).getSingleResult();
        assertEquals(2, department.departmentId);
        assertEquals(20, department.departmentNo);
        assertEquals("bar", department.departmentName);
        assertEquals("baz", department.location);
        assertEquals(2, department.version);
    }

    /**
     * 
     * @throws Exception
     */
    public void testExecute_excludes() throws Exception {
        List<Department> list = new ArrayList<Department>();
        Department department = new Department();
        department.departmentId = 1;
        department.departmentNo = 98;
        department.departmentName = "hoge";
        department.location = "foo";
        department.version = 1;
        list.add(department);
        Department department2 = new Department();
        department2.departmentId = 2;
        department2.departmentNo = 99;
        department2.departmentName = "bar";
        department2.location = "baz";
        department2.version = 1;
        list.add(department2);

        int[] result =
            jdbcManager
                .updateBatch(list)
                .excludes("departmentName", "location")
                .execute();
        assertEquals(2, result.length);
        assertEquals(2, department.version);
        assertEquals(2, department2.version);

        department =
            jdbcManager.from(Department.class).where(
                new SimpleWhere().eq("departmentId", 1)).getSingleResult();
        assertEquals(1, department.departmentId);
        assertEquals(98, department.departmentNo);
        assertEquals("ACCOUNTING", department.departmentName);
        assertEquals("NEW YORK", department.location);
        assertEquals(2, department.version);

        department =
            jdbcManager.from(Department.class).where(
                new SimpleWhere().eq("departmentId", 2)).getSingleResult();
        assertEquals(2, department.departmentId);
        assertEquals(99, department.departmentNo);
        assertEquals("RESEARCH", department.departmentName);
        assertEquals("DALLAS", department.location);
        assertEquals(2, department.version);
    }

    /**
     * 
     * @throws Exception
     */
    public void testExecute_compKey() throws Exception {
        List<CompKeyDepartment> list = new ArrayList<CompKeyDepartment>();
        CompKeyDepartment department = new CompKeyDepartment();
        department.departmentId1 = 1;
        department.departmentId2 = 1;
        department.departmentNo = 10;
        department.departmentName = "hoge";
        department.version = 1;
        list.add(department);
        CompKeyDepartment department2 = new CompKeyDepartment();
        department2.departmentId1 = 2;
        department2.departmentId2 = 2;
        department2.departmentNo = 20;
        department2.departmentName = "foo";
        department2.version = 1;
        list.add(department2);

        int[] result = jdbcManager.updateBatch(list).execute();
        assertEquals(2, result.length);
        assertEquals(2, department.version);
        assertEquals(2, department2.version);

        department =
            jdbcManager
                .from(CompKeyDepartment.class)
                .where(
                    new SimpleWhere().eq("departmentId1", 1).eq(
                        "departmentId2",
                        1))
                .getSingleResult();
        assertEquals(1, department.departmentId1);
        assertEquals(1, department.departmentId2);
        assertEquals(10, department.departmentNo);
        assertEquals("hoge", department.departmentName);
        assertNull(department.location);
        assertEquals(2, department.version);

        department =
            jdbcManager
                .from(CompKeyDepartment.class)
                .where(
                    new SimpleWhere().eq("departmentId1", 2).eq(
                        "departmentId2",
                        2))
                .getSingleResult();
        assertEquals(2, department.departmentId1);
        assertEquals(2, department.departmentId2);
        assertEquals(20, department.departmentNo);
        assertEquals("foo", department.departmentName);
        assertNull(department.location);
        assertEquals(2, department.version);
    }

    /**
     * 
     * @throws Exception
     */
    public void testExecute_mappedSuperclass() throws Exception {
        List<ConcreteDepartment> list = new ArrayList<ConcreteDepartment>();
        ConcreteDepartment department = new ConcreteDepartment();
        department.departmentId = 1;
        department.departmentName = "hoge";
        department.departmentNo = 10;
        department.version = 1;
        list.add(department);
        ConcreteDepartment department2 = new ConcreteDepartment();
        department2.departmentId = 2;
        department2.departmentNo = 20;
        department2.departmentName = "foo";
        department2.version = 1;
        list.add(department2);

        int[] result = jdbcManager.updateBatch(list).execute();
        assertEquals(2, result.length);
        assertEquals(2, department.version);
        assertEquals(2, department2.version);

        department =
            jdbcManager.from(ConcreteDepartment.class).where(
                new SimpleWhere().eq("departmentId", 1)).getSingleResult();
        assertEquals(1, department.departmentId);
        assertEquals(10, department.departmentNo);
        assertEquals("hoge", department.departmentName);
        assertNull(department.location);
        assertEquals(2, department.version);

        department =
            jdbcManager.from(ConcreteDepartment.class).where(
                new SimpleWhere().eq("departmentId", 2)).getSingleResult();
        assertEquals(2, department.departmentId);
        assertEquals(20, department.departmentNo);
        assertEquals("foo", department.departmentName);
        assertNull(department.location);
        assertEquals(2, department.version);
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
        jdbcManager.update(employee1).execute();
        try {
            jdbcManager.updateBatch(employee2, employee3).execute();
            fail();
        } catch (OptimisticLockException expected) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testOptimisticLockException_includesVersion() throws Exception {
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
        jdbcManager.update(employee1).execute();
        jdbcManager
            .updateBatch(employee2, employee3)
            .includesVersion()
            .execute();
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
        jdbcManager.update(employee1).execute();
        jdbcManager
            .updateBatch(employee2, employee3)
            .suppresOptimisticLockException()
            .execute();
    }

    /**
     * 
     * @throws Exception
     */
    public void testColumnAnnotation() throws Exception {
        List<Department2> list = new ArrayList<Department2>();
        Department2 department = new Department2();
        department.departmentId = 1;
        department.departmentNo = 10;
        department.departmentName = "hoge";
        list.add(department);
        Department2 department2 = new Department2();
        department2.departmentId = 2;
        department2.departmentNo = 20;
        department2.departmentName = "foo";
        list.add(department2);

        int[] result = jdbcManager.updateBatch(list).execute();
        assertEquals(2, result.length);
        String sql =
            "select DEPARTMENT_NAME from DEPARTMENT where DEPARTMENT_ID = ?";
        String departmentName =
            jdbcManager.selectBySql(String.class, sql, 1).getSingleResult();
        assertEquals("ACCOUNTING", departmentName);
        departmentName =
            jdbcManager.selectBySql(String.class, sql, 2).getSingleResult();
        assertEquals("RESEARCH", departmentName);
    }

    /**
     * 
     * @throws Exception
     */
    public void testTransientAnnotation() throws Exception {
        List<Department3> list = new ArrayList<Department3>();
        Department3 department = new Department3();
        department.departmentId = 1;
        department.departmentNo = 10;
        department.departmentName = "hoge";
        list.add(department);
        Department3 department2 = new Department3();
        department2.departmentId = 2;
        department2.departmentNo = 20;
        department2.departmentName = "foo";
        list.add(department2);

        int[] result = jdbcManager.updateBatch(list).execute();
        assertEquals(2, result.length);
        String sql =
            "select DEPARTMENT_NAME from DEPARTMENT where DEPARTMENT_ID = ?";
        String departmentName =
            jdbcManager.selectBySql(String.class, sql, 1).getSingleResult();
        assertEquals("ACCOUNTING", departmentName);
        departmentName =
            jdbcManager.selectBySql(String.class, sql, 2).getSingleResult();
        assertEquals("RESEARCH", departmentName);
    }

    /**
     * 
     * @throws Exception
     */
    public void testTransientModifier() throws Exception {
        List<Department4> list = new ArrayList<Department4>();
        Department4 department = new Department4();
        department.departmentId = 1;
        department.departmentNo = 10;
        department.departmentName = "hoge";
        list.add(department);
        Department4 department2 = new Department4();
        department2.departmentId = 2;
        department2.departmentNo = 20;
        department2.departmentName = "foo";
        list.add(department2);

        int[] result = jdbcManager.updateBatch(list).execute();
        assertEquals(2, result.length);
        String sql =
            "select DEPARTMENT_NAME from DEPARTMENT where DEPARTMENT_ID = ?";
        String departmentName =
            jdbcManager.selectBySql(String.class, sql, 1).getSingleResult();
        assertEquals("ACCOUNTING", departmentName);
        departmentName =
            jdbcManager.selectBySql(String.class, sql, 2).getSingleResult();
        assertEquals("RESEARCH", departmentName);
    }

    /**
     * 
     * @throws Exception
     */
    @Prerequisite("#ENV != 'hsqldb'")
    public void testEntityExistsException() throws Exception {
        Department department =
            jdbcManager
                .from(Department.class)
                .where("departmentId = ?", 1)
                .getSingleResult();
        department.departmentNo = 20;
        try {
            jdbcManager.updateBatch(department).execute();
            fail();
        } catch (EntityExistsException e) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    @Test(expected = NoIdPropertyRuntimeException.class)
    public void testNoId() throws Exception {
        NoId noId1 = new NoId();
        noId1.value1 = 1;
        noId1.value1 = 2;
        NoId noId2 = new NoId();
        noId2.value1 = 1;
        noId2.value1 = 2;
        jdbcManager.updateBatch(noId1, noId2).execute();

    }

}
