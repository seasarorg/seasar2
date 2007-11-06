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

import org.junit.runner.RunWith;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.it.entity.Employee;
import org.seasar.framework.unit.Seasar2;
import org.seasar.framework.unit.annotation.Prerequisite;

import static junit.framework.Assert.*;

/**
 * @author taedium
 * 
 */
@RunWith(Seasar2.class)
@Prerequisite("#ENV not in {'hsqldb', 'h2', 'db2'}")
public class SqlFileFunctionCallTest {

    private JdbcManager jdbcManager;

    /**
     * 
     * @throws Exception
     */
    public void testParameter_noneTx() throws Exception {
        String path = getClass().getName().replace(".", "/") + "_no" + ".sql";
        Integer result =
            jdbcManager.callBySqlFile(Integer.class, path).getSingleResult();
        assertEquals(new Integer(10), result);
    }

    /**
     * 
     * @throws Exception
     */
    public void testParameter_simpleTypeTx() throws Exception {
        String path =
            getClass().getName().replace(".", "/") + "_simpleType" + ".sql";
        Integer result =
            jdbcManager.callBySqlFile(Integer.class, path, 1).getSingleResult();
        assertEquals(new Integer(20), result);
    }

    /**
     * 
     * @throws Exception
     */
    public void testParameter_dtoTx() throws Exception {
        String path = getClass().getName().replace(".", "/") + "_dto" + ".sql";
        MyDto dto = new MyDto();
        dto.param1 = 3;
        dto.param2 = 5;
        Integer result =
            jdbcManager
                .callBySqlFile(Integer.class, path, dto)
                .getSingleResult();
        assertEquals(new Integer(3), dto.param1);
        assertEquals(new Integer(5), dto.param2);
        assertEquals(new Integer(8), result);
    }

    /**
     * 
     * @throws Exception
     */
    @Prerequisite("#ENV not in {'mssql2005', 'mysql'}")
    public void testParameter_resultSetTx() throws Exception {
        String path =
            getClass().getName().replace(".", "/") + "_resultSet" + ".sql";
        List<Employee> employees =
            jdbcManager.callBySqlFile(Employee.class, path, 10).getResultList();
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
    @Prerequisite("#ENV not in {'mssql2005', 'mysql'}")
    public void testParameter_resultSetUpdateTx() throws Exception {
        String path =
            getClass().getName().replace(".", "/") + "_resultSetUpdate"
                + ".sql";
        List<Employee> employees =
            jdbcManager.callBySqlFile(Employee.class, path, 10).getResultList();
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
                    "select department_name from Department where department_id = ?",
                    1)
                .getSingleResult();
        assertEquals("HOGE", departmentName);
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
        public Integer param2;
    }

}
