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
package org.seasar.extension.jdbc.it.sql.select;

import java.util.List;
import java.util.Map;

import org.junit.runner.RunWith;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.it.entity.Employee;
import org.seasar.framework.unit.Seasar2;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
@RunWith(Seasar2.class)
public class SqlSelectWhereTest {

    private JdbcManager jdbcManager;

    /**
     * 
     * @throws Exception
     */
    public void testBean_parameter() throws Exception {
        String sql =
            "SELECT * FROM EMPLOYEE WHERE DEPARTMENT_ID = ? AND SALARY = ?";
        List<Employee> list =
            jdbcManager
                .selectBySql(Employee.class, sql, 2, 3000)
                .getResultList();
        assertEquals(2, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void testBean_parameter_none() throws Exception {
        String sql = "SELECT * FROM EMPLOYEE";
        List<Employee> list =
            jdbcManager.selectBySql(Employee.class, sql).getResultList();
        assertEquals(14, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void testMap_parameter() throws Exception {
        String sql =
            "SELECT * FROM EMPLOYEE WHERE DEPARTMENT_ID = ? AND SALARY = ?";
        @SuppressWarnings("unchecked")
        List<Map> list =
            jdbcManager.selectBySql(Map.class, sql, 2, 3000).getResultList();
        assertEquals(2, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void testMap_parameter_none() throws Exception {
        String sql = "SELECT * FROM EMPLOYEE";
        @SuppressWarnings("unchecked")
        List<Map> list =
            jdbcManager.selectBySql(Map.class, sql).getResultList();
        assertEquals(14, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void testObject_parameter() throws Exception {
        String sql =
            "SELECT EMPLOYEE_ID FROM EMPLOYEE WHERE DEPARTMENT_ID = ? AND SALARY = ?";
        List<Integer> list =
            jdbcManager
                .selectBySql(Integer.class, sql, 2, 3000)
                .getResultList();
        assertEquals(2, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void testObject_parameter_none() throws Exception {
        String sql = "SELECT EMPLOYEE_ID FROM EMPLOYEE";
        List<Integer> list =
            jdbcManager.selectBySql(Integer.class, sql).getResultList();
        assertEquals(14, list.size());
    }
}
