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

import java.math.BigDecimal;
import java.util.List;

import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.it.entity.Employee;
import org.seasar.extension.unit.S2TestCase;

/**
 * @author taedium
 * 
 */
public class SqlFileSelectTest extends S2TestCase {

    private static String path = SqlFileSelectTest.class.getName().replace(".",
            "/")
            + "_paging.sql";

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
    public void test() throws Exception {
        List<Employee> list = jdbcManager.selectBySqlFile(Employee.class, path)
                .getResultList();
        assertEquals(14, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void test_offsetOnly() throws Exception {
        List<Employee> list = jdbcManager.selectBySqlFile(Employee.class, path)
                .offset(3).getResultList();
        assertEquals(11, list.size());
        assertEquals(4, list.get(0).employeeId);
        assertEquals(14, list.get(10).employeeId);
    }

    /**
     * 
     * @throws Exception
     */
    public void test_limitOnly() throws Exception {
        List<Employee> list = jdbcManager.selectBySqlFile(Employee.class, path)
                .limit(3).getResultList();
        assertEquals(3, list.size());
        assertEquals(1, list.get(0).employeeId);
        assertEquals(3, list.get(2).employeeId);
    }

    /**
     * 
     * @throws Exception
     */
    public void test_offsetZero_limitZero() throws Exception {
        List<Employee> list = jdbcManager.selectBySqlFile(Employee.class, path)
                .offset(0).limit(0).getResultList();
        assertEquals(14, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void test_offset_limitZero() throws Exception {
        List<Employee> list = jdbcManager.selectBySqlFile(Employee.class, path)
                .offset(3).limit(0).getResultList();
        assertEquals(11, list.size());
        assertEquals(4, list.get(0).employeeId);
        assertEquals(14, list.get(10).employeeId);
    }

    /**
     * 
     * @throws Exception
     */
    public void test_offsetZero_limit() throws Exception {
        List<Employee> list = jdbcManager.selectBySqlFile(Employee.class, path)
                .offset(0).limit(3).getResultList();
        assertEquals(3, list.size());
        assertEquals(1, list.get(0).employeeId);
        assertEquals(3, list.get(2).employeeId);
    }

    /**
     * 
     * @throws Exception
     */
    public void test_offset_limit() throws Exception {
        List<Employee> list = jdbcManager.selectBySqlFile(Employee.class, path)
                .offset(3).limit(5).getResultList();
        assertEquals(5, list.size());
        assertEquals(4, list.get(0).employeeId);
        assertEquals(8, list.get(4).employeeId);
    }

    /**
     * 
     * @throws Exception
     */
    public void test_no_parameter() throws Exception {
        String path = getClass().getName().replace(".", "/") + "_no.sql";
        List<Employee> list = jdbcManager.selectBySqlFile(Employee.class, path)
                .getResultList();
        assertEquals(14, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void test_simple_parameter() throws Exception {
        String path = getClass().getName().replace(".", "/") + "_simple.sql";
        List<Employee> list = jdbcManager.selectBySqlFile(Employee.class, path,
                3).getResultList();
        assertEquals(6, list.size());
        assertEquals("ALLEN", list.get(0).employeeName);
        assertEquals("BLAKE", list.get(1).employeeName);
        assertEquals("JAMES", list.get(2).employeeName);
        assertEquals("MARTIN", list.get(3).employeeName);
        assertEquals("TURNER", list.get(4).employeeName);
        assertEquals("WARD", list.get(5).employeeName);
    }

    /**
     * 
     * @throws Exception
     */
    public void test_dto_parameter() throws Exception {
        String path = getClass().getName().replace(".", "/") + "_dto.sql";
        Param param = new Param();
        param.departmentId = 3;
        param.salary = new BigDecimal(1000);
        param.orderBy = "employee_name";
        param.offset = 1;
        param.limit = 3;
        List<Employee> list = jdbcManager.selectBySqlFile(Employee.class, path,
                param).getResultList();
        assertEquals(3, list.size());
        assertEquals("BLAKE", list.get(0).employeeName);
        assertEquals("MARTIN", list.get(1).employeeName);
        assertEquals("TURNER", list.get(2).employeeName);
    }

    /**
     * 
     * @author taedium
     */
    public static class Param {

        /** */
        public int departmentId;

        /** */
        public BigDecimal salary;

        /** */
        public String orderBy;

        /** */
        public int offset;

        /** */
        public int limit;
    }

}
