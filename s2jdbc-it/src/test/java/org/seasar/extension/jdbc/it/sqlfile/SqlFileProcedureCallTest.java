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
package org.seasar.extension.jdbc.it.sqlfile;

import java.util.List;

import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.JdbcManagerImplementor;
import org.seasar.extension.jdbc.it.S2JdbcItUtil;
import org.seasar.extension.jdbc.it.entity.Department;
import org.seasar.extension.jdbc.it.entity.Employee;
import org.seasar.extension.unit.S2TestCase;

/**
 * @author taedium
 * 
 */
public class SqlFileProcedureCallTest extends S2TestCase {

    private JdbcManager jdbcManager;

    private JdbcManagerImplementor jdbcManagerImplementor;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        include("jdbc.dicon");
    }

    /**
     * 
     * @throws Exception
     */
    public void testParameter_noneTx() throws Exception {
        if (!S2JdbcItUtil.supportsProcedure(jdbcManagerImplementor)) {
            return;
        }
        String path = getClass().getName().replace(".", "/") + "_no" + ".sql";
        jdbcManager.callBySqlFile(path).call();
    }

    /**
     * 
     * @throws Exception
     */
    public void testParameter_simpleTypeTx() throws Exception {
        if (!S2JdbcItUtil.supportsProcedure(jdbcManagerImplementor)) {
            return;
        }
        String path =
            getClass().getName().replace(".", "/") + "_simpleType" + ".sql";
        jdbcManager.callBySqlFile(path, 1).call();
    }

    /**
     * 
     * @throws Exception
     */
    public void testParameter_dtoTx() throws Exception {
        if (!S2JdbcItUtil.supportsProcedure(jdbcManagerImplementor)) {
            return;
        }
        String path = getClass().getName().replace(".", "/") + "_dto" + ".sql";
        MyDto dto = new MyDto();
        dto.param1 = 3;
        dto.param2_IN_OUT = 5;
        jdbcManager.callBySqlFile(path, dto).call();
        assertEquals(new Integer(3), dto.param1);
        assertEquals(new Integer(8), dto.param2_IN_OUT);
        assertEquals(new Integer(3), dto.param3_OUT);
    }

    /**
     * 
     * @throws Exception
     */
    public void testParameter_oneResultTx() throws Exception {
        if (!S2JdbcItUtil.supportsProcedure(jdbcManagerImplementor)) {
            return;
        }
        String path = getClass().getName().replace(".", "/") + "_one_result";
        if (jdbcManagerImplementor.getDialect().needsParameterForResultSet()) {
            path += ".sql";
        } else {
            path += "2.sql";
        }
        OneResultDto dto = new OneResultDto();
        dto.employeeId = 10;
        jdbcManager.callBySqlFile(path, dto).call();
        List<Employee> employees = dto.employees_OUT;
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
    public void testParameter_twoResultsTx() throws Exception {
        if (!S2JdbcItUtil.supportsProcedure(jdbcManagerImplementor)) {
            return;
        }
        String path = getClass().getName().replace(".", "/") + "_two_results";
        if (jdbcManagerImplementor.getDialect().needsParameterForResultSet()) {
            path += ".sql";
        } else {
            path += "2.sql";
        }
        TwoResultsDto dto = new TwoResultsDto();
        dto.employeeId = 10;
        dto.departmentId = 2;
        jdbcManager.callBySqlFile(path, dto).call();
        List<Employee> employees = dto.employees_OUT;
        assertNotNull(employees);
        assertEquals(4, employees.size());
        assertEquals("ADAMS", employees.get(0).employeeName);
        assertEquals("JAMES", employees.get(1).employeeName);
        assertEquals("FORD", employees.get(2).employeeName);
        assertEquals("MILLER", employees.get(3).employeeName);
        List<Department> departments = dto.departments_OUT;
        assertNotNull(departments);
        assertEquals(2, departments.size());
        assertEquals("SALES", departments.get(0).departmentName);
        assertEquals("OPERATIONS", departments.get(1).departmentName);
    }

    /**
     * 
     * @author taedium
     * 
     */
    public class MyDto {

        /** */
        public Integer param1;

        /** */
        public Integer param2_IN_OUT;

        /** */
        public Integer param3_OUT;
    }

    /**
     * 
     * @author taedium
     * 
     */
    public class OneResultDto {

        /** */
        public List<Employee> employees_OUT;

        /** */
        public int employeeId;
    }

    /**
     * 
     * @author taedium
     * 
     */
    public class TwoResultsDto {

        /** */
        public List<Employee> employees_OUT;

        /** */
        public List<Department> departments_OUT;

        /** */
        public int employeeId;

        /** */
        public int departmentId;
    }

}
