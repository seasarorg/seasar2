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

import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityExistsException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.exception.NoIdPropertyRuntimeException;
import org.seasar.extension.jdbc.exception.SOptimisticLockException;
import org.seasar.extension.jdbc.it.entity.CompKeyDepartment;
import org.seasar.extension.jdbc.it.entity.ConcreteDepartment;
import org.seasar.extension.jdbc.it.entity.Department;
import org.seasar.extension.jdbc.it.entity.Department2;
import org.seasar.extension.jdbc.it.entity.Department3;
import org.seasar.extension.jdbc.it.entity.Department4;
import org.seasar.extension.jdbc.it.entity.Employee;
import org.seasar.extension.jdbc.it.entity.NoId;
import org.seasar.extension.jdbc.it.entity.Tense;
import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.unit.Seasar2;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
@RunWith(Seasar2.class)
public class AutoUpdateTest {

    private JdbcManager jdbcManager;

    /**
     * 
     * @throws Exception
     */
    public void testExecute() throws Exception {
        Department department = new Department();
        department.departmentId = 1;
        department.departmentName = "hoge";
        department.version = 1;
        int result = jdbcManager.update(department).execute();
        assertEquals(1, result);
        assertEquals(2, department.version);
        department =
            jdbcManager.from(Department.class).where(
                new SimpleWhere().eq("departmentId", 1)).getSingleResult();
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
    public void testExecute_includesVersion() throws Exception {
        Department department = new Department();
        department.departmentId = 1;
        department.departmentName = "hoge";
        department.version = 100;
        int result = jdbcManager.update(department).includesVersion().execute();
        assertEquals(1, result);
        assertEquals(100, department.version);
        department =
            jdbcManager.from(Department.class).where(
                new SimpleWhere().eq("departmentId", 1)).getSingleResult();
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
    public void testExecute_excludesNull() throws Exception {
        Department department = new Department();
        department.departmentId = 1;
        department.departmentName = "hoge";
        department.version = 1;
        int result = jdbcManager.update(department).excludesNull().execute();
        assertEquals(1, result);
        assertEquals(2, department.version);
        department =
            jdbcManager.from(Department.class).where(
                new SimpleWhere().eq("departmentId", 1)).getSingleResult();
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
    public void testExecute_includes() throws Exception {
        Department department = new Department();
        department.departmentId = 1;
        department.departmentNo = 99;
        department.departmentName = "hoge";
        department.location = "foo";
        department.version = 1;
        int result =
            jdbcManager.update(department).includes(
                "departmentName",
                "location").execute();
        assertEquals(1, result);
        assertEquals(2, department.version);
        department =
            jdbcManager.from(Department.class).where(
                new SimpleWhere().eq("departmentId", 1)).getSingleResult();
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
    public void testExecute_excludes() throws Exception {
        Department department = new Department();
        department.departmentId = 1;
        department.departmentNo = 99;
        department.departmentName = "hoge";
        department.location = "foo";
        department.version = 1;
        int result =
            jdbcManager.update(department).excludes(
                "departmentName",
                "location").execute();
        assertEquals(1, result);
        assertEquals(2, department.version);
        department =
            jdbcManager.from(Department.class).where(
                new SimpleWhere().eq("departmentId", 1)).getSingleResult();
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
    public void testExecute_changeFrom() throws Exception {
        Department before =
            jdbcManager.from(Department.class).where(
                new SimpleWhere().eq("departmentId", 1)).getSingleResult();

        Department department = new Department();
        department.departmentId = before.departmentId;
        department.departmentNo = before.departmentNo;
        department.departmentName = "hoge";
        department.location = before.location;
        department.version = before.version;

        int result =
            jdbcManager.update(department).changedFrom(before).execute();
        assertEquals(1, result);
        assertEquals(before.version + 1, department.version);
        department =
            jdbcManager.from(Department.class).where(
                new SimpleWhere().eq("departmentId", 1)).getSingleResult();
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
    public void testExecute_compKey() throws Exception {
        CompKeyDepartment department = new CompKeyDepartment();
        department.departmentId1 = 1;
        department.departmentId2 = 1;
        department.departmentName = "hoge";
        department.version = 1;
        int result = jdbcManager.update(department).execute();
        assertEquals(1, result);
        assertEquals(2, department.version);
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("departmentId1", 1);
        m.put("departmentId2", 1);
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
        assertEquals(0, department.departmentNo);
        assertEquals("hoge", department.departmentName);
        assertNull(department.location);
        assertEquals(2, department.version);
    }

    /**
     * 
     * @throws Exception
     */
    public void testExecute_mappedSuperclass() throws Exception {
        ConcreteDepartment department = new ConcreteDepartment();
        department.departmentId = 1;
        department.departmentName = "hoge";
        department.version = 1;
        int result = jdbcManager.update(department).execute();
        assertEquals(1, result);
        assertEquals(2, department.version);
        department =
            jdbcManager.from(ConcreteDepartment.class).where(
                new SimpleWhere().eq("departmentId", 1)).getSingleResult();
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
        jdbcManager.update(employee1).execute();
        try {
            jdbcManager.update(employee2).execute();
            fail();
        } catch (SOptimisticLockException ignore) {
        }
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
        jdbcManager.update(employee1).execute();
        int result =
            jdbcManager
                .update(employee2)
                .suppresOptimisticLockException()
                .execute();
        assertEquals(0, result);
    }

    /**
     * 
     * @throws Exception
     */
    public void testColumnAnnotation() throws Exception {
        Department2 department = new Department2();
        department.departmentId = 1;
        department.departmentName = "hoge";
        int result = jdbcManager.update(department).execute();
        assertEquals(1, result);
        String departmentName =
            jdbcManager
                .selectBySql(
                    String.class,
                    "select DEPARTMENT_NAME from DEPARTMENT where DEPARTMENT_ID = ?",
                    1)
                .getSingleResult();
        assertEquals("ACCOUNTING", departmentName);
    }

    /**
     * 
     * @throws Exception
     */
    public void testTransientAnnotation() throws Exception {
        Department3 department = new Department3();
        department.departmentId = 1;
        department.departmentName = "hoge";
        int result = jdbcManager.update(department).execute();
        assertEquals(1, result);
        String departmentName =
            jdbcManager
                .selectBySql(
                    String.class,
                    "select DEPARTMENT_NAME from DEPARTMENT where DEPARTMENT_ID = ?",
                    1)
                .getSingleResult();
        assertEquals("ACCOUNTING", departmentName);
    }

    /**
     * 
     * @throws Exception
     */
    public void testTransientModifier() throws Exception {
        Department4 department = new Department4();
        department.departmentId = 1;
        department.departmentName = "hoge";
        int result = jdbcManager.update(department).execute();
        assertEquals(1, result);
        String departmentName =
            jdbcManager
                .selectBySql(
                    String.class,
                    "select DEPARTMENT_NAME from DEPARTMENT where DEPARTMENT_ID = ?",
                    1)
                .getSingleResult();
        assertEquals("ACCOUNTING", departmentName);
    }

    /**
     * 
     * @throws Exception
     */
    public void testEntityExistsException() throws Exception {
        Department department =
            jdbcManager
                .from(Department.class)
                .where("departmentId = ?", 1)
                .getSingleResult();
        department.departmentNo = 20;
        try {
            jdbcManager.update(department).execute();
            fail();
        } catch (EntityExistsException e) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testTemporalType() throws Exception {
        Tense tense = new Tense();
        tense.id = 1;
        long date =
            new SimpleDateFormat("yyyy-MM-dd").parse("2005-03-14").getTime();
        long time =
            new SimpleDateFormat("HH:mm:ss").parse("13:11:10").getTime();
        long timestamp =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(
                "2005-03-14 13:11:10").getTime();
        tense.dateDate = new Date(date);
        tense.dateTime = new Date(time);
        tense.dateTimestamp = new Date(timestamp);
        tense.calDate = Calendar.getInstance();
        tense.calDate.setTimeInMillis(date);
        tense.calTime = Calendar.getInstance();
        tense.calTime.setTimeInMillis(time);
        tense.calTimestamp = Calendar.getInstance();
        tense.calTimestamp.setTimeInMillis(timestamp);
        tense.sqlDate = new java.sql.Date(date);
        tense.sqlTime = new Time(time);
        tense.sqlTimestamp = new Timestamp(timestamp);
        jdbcManager.update(tense).execute();
        tense = jdbcManager.from(Tense.class).id(1).getSingleResult();

        assertEquals(date, tense.calDate.getTimeInMillis());
        assertEquals(date, tense.dateDate.getTime());
        assertEquals(date, tense.sqlDate.getTime());
        assertEquals(time, tense.calTime.getTimeInMillis());
        assertEquals(time, tense.dateTime.getTime());
        assertEquals(time, tense.sqlTime.getTime());
        assertEquals(timestamp, tense.calTimestamp.getTimeInMillis());
        assertEquals(timestamp, tense.dateTimestamp.getTime());
        assertEquals(timestamp, tense.sqlTimestamp.getTime());
    }

    /**
     * 
     * @throws Exception
     */
    @Test(expected = NoIdPropertyRuntimeException.class)
    public void testNoId() throws Exception {
        NoId noId = new NoId();
        noId.value1 = 1;
        noId.value1 = 2;
        jdbcManager.update(noId).execute();
    }
}
