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
package org.seasar.extension.jdbc.it.sqlfile;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.junit.runner.RunWith;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.annotation.InOut;
import org.seasar.extension.jdbc.annotation.Out;
import org.seasar.extension.jdbc.annotation.ResultSet;
import org.seasar.extension.jdbc.it.entity.Department;
import org.seasar.extension.jdbc.it.entity.Employee;
import org.seasar.extension.jdbc.manager.JdbcManagerImplementor;
import org.seasar.framework.unit.Seasar2;
import org.seasar.framework.unit.annotation.Prerequisite;

import static junit.framework.Assert.*;

import static org.seasar.extension.jdbc.parameter.Parameter.*;

/**
 * @author taedium
 * 
 */
@RunWith(Seasar2.class)
@Prerequisite("#ENV not in {'hsqldb', 'h2', 'standard'}")
public class SqlFileProcedureCallTest {

    private JdbcManager jdbcManager;

    private JdbcManagerImplementor jdbcManagerImplementor;

    /**
     * 
     * @throws Exception
     */
    public void testParameter_none() throws Exception {
        String path = getClass().getName().replace(".", "/") + "_none" + ".sql";
        jdbcManager.callBySqlFile(path).execute();
    }

    /**
     * 
     * @throws Exception
     */
    public void testParameter_simpleType() throws Exception {
        String path =
            getClass().getName().replace(".", "/") + "_simpleType" + ".sql";
        jdbcManager.callBySqlFile(path, 1).execute();
    }

    /**
     * 
     * @throws Exception
     */
    public void testParameter_simpleType_time() throws Exception {
        String path =
            getClass().getName().replace(".", "/") + "_simpleType_time"
                + ".sql";
        jdbcManager.callBySqlFile(path, time(new Date())).execute();
    }

    /**
     * 
     * @throws Exception
     */
    public void testParameter_dto() throws Exception {
        String path = getClass().getName().replace(".", "/") + "_dto" + ".sql";
        MyDto dto = new MyDto();
        dto.param1 = 3;
        dto.param2 = 5;
        jdbcManager.callBySqlFile(path, dto).execute();
        assertEquals(new Integer(3), dto.param1);
        assertEquals(new Integer(8), dto.param2);
        assertEquals(new Integer(3), dto.param3);
    }

    /**
     * 
     * @throws Exception
     */
    public void testParameter_dto_time() throws Exception {
        String path =
            getClass().getName().replace(".", "/") + "_dto_time" + ".sql";
        Date date = new SimpleDateFormat("HH:mm:ss").parse("12:11:10");
        MyDto2 dto = new MyDto2();
        dto.param1 = date;
        dto.param2 = date;
        jdbcManager.callBySqlFile(path, dto).execute();
        assertEquals(date.getTime(), dto.param1.getTime());
        assertEquals(date.getTime(), dto.param2.getTime());
        assertEquals(date.getTime(), dto.param3.getTimeInMillis());
    }

    /**
     * 
     * @throws Exception
     */
    public void testParameter_resultSet() throws Exception {
        String path = getClass().getName().replace(".", "/") + "_resultSet";
        if (jdbcManagerImplementor.getDialect().needsParameterForResultSet()) {
            path += ".sql";
        } else {
            path += "2.sql";
        }
        ResultSetDto dto = new ResultSetDto();
        dto.employeeId = 10;
        jdbcManager.callBySqlFile(path, dto).execute();
        List<Employee> employees = dto.employees;
        assertNotNull(employees);
        assertEquals(4, employees.size());
        assertEquals("ADAMS", employees.get(0).employeeName);
        assertEquals("JAMES", employees.get(1).employeeName);
        assertEquals("FORD", employees.get(2).employeeName);
        assertEquals("MILLER", employees.get(3).employeeName);
    }

    /**
     * 
     * @throws Exception
     */
    public void testParameter_resultSetOut() throws Exception {
        String path = getClass().getName().replace(".", "/") + "_resultSetOut";
        if (jdbcManagerImplementor.getDialect().needsParameterForResultSet()) {
            path += ".sql";
        } else {
            path += "2.sql";
        }
        ResultSetOutDto dto = new ResultSetOutDto();
        dto.employeeId = 10;
        jdbcManager.callBySqlFile(path, dto).execute();
        List<Employee> employees = dto.employees;
        assertNotNull(employees);
        assertEquals(4, employees.size());
        assertEquals("ADAMS", employees.get(0).employeeName);
        assertEquals("JAMES", employees.get(1).employeeName);
        assertEquals("FORD", employees.get(2).employeeName);
        assertEquals("MILLER", employees.get(3).employeeName);
        assertEquals(14, dto.employeeCount);
    }

    /**
     * 
     * @throws Exception
     */
    public void testParameter_resultSetUpdate() throws Exception {
        String path =
            getClass().getName().replace(".", "/") + "_resultSetUpdate";
        if (jdbcManagerImplementor.getDialect().needsParameterForResultSet()) {
            path += ".sql";
        } else {
            path += "2.sql";
        }
        ResultSetDto dto = new ResultSetDto();
        dto.employeeId = 10;
        jdbcManager.callBySqlFile(path, dto).execute();
        List<Employee> employees = dto.employees;
        assertNotNull(employees);
        assertEquals(4, employees.size());
        assertEquals("ADAMS", employees.get(0).employeeName);
        assertEquals("JAMES", employees.get(1).employeeName);
        assertEquals("FORD", employees.get(2).employeeName);
        assertEquals("MILLER", employees.get(3).employeeName);
        String departmentName =
            jdbcManager
                .selectBySql(
                    String.class,
                    "select DEPARTMENT_NAME from DEPARTMENT where DEPARTMENT_ID = ?",
                    1)
                .getSingleResult();
        assertEquals("HOGE", departmentName);
    }

    /**
     * 
     * @throws Exception
     */
    public void testParameter_resultSets() throws Exception {
        String path = getClass().getName().replace(".", "/") + "_resultSets";
        if (jdbcManagerImplementor.getDialect().needsParameterForResultSet()) {
            path += ".sql";
        } else {
            path += "2.sql";
        }
        ResultSetsDto dto = new ResultSetsDto();
        dto.employeeId = 10;
        dto.departmentId = 2;
        jdbcManager.callBySqlFile(path, dto).execute();
        List<Employee> employees = dto.employees;
        assertNotNull(employees);
        assertEquals(4, employees.size());
        assertEquals("ADAMS", employees.get(0).employeeName);
        assertEquals("JAMES", employees.get(1).employeeName);
        assertEquals("FORD", employees.get(2).employeeName);
        assertEquals("MILLER", employees.get(3).employeeName);
        List<Department> departments = dto.departments;
        assertNotNull(departments);
        assertEquals(2, departments.size());
        assertEquals("SALES", departments.get(0).departmentName);
        assertEquals("OPERATIONS", departments.get(1).departmentName);
    }

    /**
     * 
     * @throws Exception
     */
    public void testParameter_resultSetsUpdatesOut() throws Exception {
        String path =
            getClass().getName().replace(".", "/") + "_resultSetsUpdatesOut";
        if (jdbcManagerImplementor.getDialect().needsParameterForResultSet()) {
            path += ".sql";
        } else {
            path += "2.sql";
        }
        ResultSetsUpdatesOutDto dto = new ResultSetsUpdatesOutDto();
        dto.employeeId = 10;
        dto.departmentId = 2;
        jdbcManager.callBySqlFile(path, dto).execute();
        List<Employee> employees = dto.employees;
        assertNotNull(employees);
        assertEquals(4, employees.size());
        assertEquals("ADAMS", employees.get(0).employeeName);
        assertEquals("JAMES", employees.get(1).employeeName);
        assertEquals("FORD", employees.get(2).employeeName);
        assertEquals("MILLER", employees.get(3).employeeName);
        List<Department> departments = dto.departments;
        assertNotNull(departments);
        assertEquals(2, departments.size());
        assertEquals("SALES", departments.get(0).departmentName);
        assertEquals("OPERATIONS", departments.get(1).departmentName);
        String street =
            jdbcManager.selectBySql(
                String.class,
                "select STREET from ADDRESS where ADDRESS_ID = ?",
                1).getSingleResult();
        assertEquals("HOGE", street);
        street =
            jdbcManager.selectBySql(
                String.class,
                "select STREET from ADDRESS where ADDRESS_ID = ?",
                2).getSingleResult();
        assertEquals("FOO", street);
        assertEquals(14, dto.employeeCount);
    }

    /**
     * 
     * @author taedium
     * 
     */
    public static class MyDto {

        /** */
        public Integer param1;

        /** */
        @InOut
        public Integer param2;

        /** */
        @Out
        public Integer param3;
    }

    /**
     * 
     * @author taedium
     * 
     */
    public static class MyDto2 {

        /** */
        @Temporal(TemporalType.TIME)
        public Date param1;

        /** */
        @InOut
        @Temporal(TemporalType.TIME)
        public Date param2;

        /** */
        @Out
        @Temporal(TemporalType.TIME)
        public Calendar param3;
    }

    /**
     * 
     * @author taedium
     * 
     */
    public static class ResultSetDto {

        /** */
        @ResultSet
        public List<Employee> employees;

        /** */
        public int employeeId;
    }

    /**
     * 
     * @author taedium
     * 
     */
    public static class ResultSetOutDto {

        /** */
        @ResultSet
        public List<Employee> employees;

        /** */
        public int employeeId;

        /** */
        @Out
        public int employeeCount;

    }

    /**
     * 
     * @author taedium
     * 
     */
    public static class ResultSetUpdateDto {

        /** */
        @ResultSet
        public List<Employee> employees;

        /** */
        public int employeeId;

    }

    /**
     * 
     * @author taedium
     * 
     */
    public static class ResultSetsDto {

        /** */
        @ResultSet
        public List<Employee> employees;

        /** */
        @ResultSet
        public List<Department> departments;

        /** */
        public int employeeId;

        /** */
        public int departmentId;
    }

    /**
     * 
     * @author taedium
     * 
     */
    public static class ResultSetsUpdatesOutDto {

        /** */
        @ResultSet
        public List<Employee> employees;

        /** */
        @ResultSet
        public List<Department> departments;

        /** */
        public int employeeId;

        /** */
        public int departmentId;

        /** */
        @Out
        public int employeeCount;
    }

}
