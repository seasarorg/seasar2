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
public class SqlSelectPagingTest {

    private static String sql = "SELECT * FROM EMPLOYEE ORDER BY EMPLOYEE_NO";

    private static String sql2 =
        "SELECT EMPLOYEE_ID, EMPLOYEE_NO FROM EMPLOYEE ORDER BY EMPLOYEE_NO";

    private JdbcManager jdbcManager;

    /**
     * 
     * @throws Exception
     */
    public void testBean_paging() throws Exception {
        List<Employee> list =
            jdbcManager.selectBySql(Employee.class, sql).getResultList();
        assertEquals(14, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void testBean_paging_limitOnly() throws Exception {
        List<Employee> list =
            jdbcManager
                .selectBySql(Employee.class, sql)
                .limit(3)
                .getResultList();
        assertEquals(3, list.size());
        assertEquals(1, list.get(0).employeeId);
        assertEquals(3, list.get(2).employeeId);
    }

    /**
     * 
     * @throws Exception
     */
    public void testBean_paging_offset_limit() throws Exception {
        List<Employee> list =
            jdbcManager
                .selectBySql(Employee.class, sql)
                .offset(3)
                .limit(5)
                .getResultList();
        assertEquals(5, list.size());
        assertEquals(4, list.get(0).employeeId);
        assertEquals(8, list.get(4).employeeId);
    }

    /**
     * 
     * @throws Exception
     */
    public void testBean_paging_offset_limitZero() throws Exception {
        List<Employee> list =
            jdbcManager
                .selectBySql(Employee.class, sql)
                .offset(3)
                .limit(0)
                .getResultList();
        assertEquals(11, list.size());
        assertEquals(4, list.get(0).employeeId);
        assertEquals(14, list.get(10).employeeId);
    }

    /**
     * 
     * @throws Exception
     */
    public void testBean_paging_offsetOnly() throws Exception {
        List<Employee> list =
            jdbcManager
                .selectBySql(Employee.class, sql)
                .offset(3)
                .getResultList();
        assertEquals(11, list.size());
        assertEquals(4, list.get(0).employeeId);
        assertEquals(14, list.get(10).employeeId);
    }

    /**
     * 
     * @throws Exception
     */
    public void testBean_paging_offsetZero_limit() throws Exception {
        List<Employee> list =
            jdbcManager
                .selectBySql(Employee.class, sql)
                .offset(0)
                .limit(3)
                .getResultList();
        assertEquals(3, list.size());
        assertEquals(1, list.get(0).employeeId);
        assertEquals(3, list.get(2).employeeId);
    }

    /**
     * 
     * @throws Exception
     */
    public void testBean_paging_offsetZero_limitZero() throws Exception {
        List<Employee> list =
            jdbcManager
                .selectBySql(Employee.class, sql)
                .offset(0)
                .limit(0)
                .getResultList();
        assertEquals(14, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void testMap_paging() throws Exception {
        @SuppressWarnings("unchecked")
        List<Map> list =
            jdbcManager.selectBySql(Map.class, sql).getResultList();
        assertEquals(14, list.size());
        assertEquals(9, list.get(0).size());
    }

    /**
     * 
     * @throws Exception
     */
    public void testMap_paging_limitOnly() throws Exception {
        @SuppressWarnings("unchecked")
        List<Map> list =
            jdbcManager.selectBySql(Map.class, sql).limit(3).getResultList();
        assertEquals(3, list.size());
        assertEquals(9, list.get(0).size());
        assertEquals(1, ((Number) list.get(0).get("employeeId")).intValue());
        assertEquals(3, ((Number) list.get(2).get("employeeId")).intValue());
    }

    /**
     * 
     * @throws Exception
     */
    public void testMap_paging_offset_limit() throws Exception {
        @SuppressWarnings("unchecked")
        List<Map> list =
            jdbcManager
                .selectBySql(Map.class, sql)
                .offset(3)
                .limit(5)
                .getResultList();
        assertEquals(5, list.size());
        assertEquals(9, list.get(0).size());
        assertEquals(4, ((Number) list.get(0).get("employeeId")).intValue());
        assertEquals(8, ((Number) list.get(4).get("employeeId")).intValue());
    }

    /**
     * 
     * @throws Exception
     */
    public void testMap_paging_offset_limitZero() throws Exception {
        @SuppressWarnings("unchecked")
        List<Map> list =
            jdbcManager
                .selectBySql(Map.class, sql)
                .offset(3)
                .limit(0)
                .getResultList();
        assertEquals(11, list.size());
        assertEquals(9, list.get(0).size());
        assertEquals(4, ((Number) list.get(0).get("employeeId")).intValue());
        assertEquals(14, ((Number) list.get(10).get("employeeId")).intValue());
    }

    /**
     * 
     * @throws Exception
     */
    public void testMap_paging_offsetOnly() throws Exception {
        @SuppressWarnings("unchecked")
        List<Map> list =
            jdbcManager.selectBySql(Map.class, sql).offset(3).getResultList();
        assertEquals(11, list.size());
        assertEquals(9, list.get(0).size());
        assertEquals(4, ((Number) list.get(0).get("employeeId")).intValue());
        assertEquals(14, ((Number) list.get(10).get("employeeId")).intValue());
    }

    /**
     * 
     * @throws Exception
     */
    public void testMap_paging_offsetZero_limit() throws Exception {
        @SuppressWarnings("unchecked")
        List<Map> list =
            jdbcManager
                .selectBySql(Map.class, sql)
                .offset(0)
                .limit(3)
                .getResultList();
        assertEquals(3, list.size());
        assertEquals(9, list.get(0).size());
        assertEquals(1, ((Number) list.get(0).get("employeeId")).intValue());
        assertEquals(3, ((Number) list.get(2).get("employeeId")).intValue());
    }

    /**
     * 
     * @throws Exception
     */
    public void testMap_paging_offsetZero_limitZero() throws Exception {
        @SuppressWarnings("unchecked")
        List<Map> list =
            jdbcManager
                .selectBySql(Map.class, sql)
                .offset(0)
                .limit(0)
                .getResultList();
        assertEquals(14, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void testObject_paging() throws Exception {
        List<Integer> list =
            jdbcManager.selectBySql(Integer.class, sql2).getResultList();
        assertEquals(14, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void testObject_paging_limitOnly() throws Exception {
        List<Integer> list =
            jdbcManager
                .selectBySql(Integer.class, sql2)
                .limit(3)
                .getResultList();
        assertEquals(3, list.size());
        assertEquals(1, list.get(0).intValue());
        assertEquals(3, list.get(2).intValue());
    }

    /**
     * 
     * @throws Exception
     */
    public void testObject_paging_offset_limit() throws Exception {
        List<Integer> list =
            jdbcManager
                .selectBySql(Integer.class, sql2)
                .offset(3)
                .limit(5)
                .getResultList();
        assertEquals(5, list.size());
        assertEquals(4, list.get(0).intValue());
        assertEquals(8, list.get(4).intValue());
    }

    /**
     * 
     * @throws Exception
     */
    public void testObject_paging_offset_limitZero() throws Exception {
        List<Integer> list =
            jdbcManager
                .selectBySql(Integer.class, sql2)
                .offset(3)
                .limit(0)
                .getResultList();
        assertEquals(11, list.size());
        assertEquals(4, list.get(0).intValue());
        assertEquals(14, list.get(10).intValue());
    }

    /**
     * 
     * @throws Exception
     */
    public void testObject_paging_offsetOnly() throws Exception {
        List<Integer> list =
            jdbcManager
                .selectBySql(Integer.class, sql2)
                .offset(3)
                .getResultList();
        assertEquals(11, list.size());
        assertEquals(4, list.get(0).intValue());
        assertEquals(14, list.get(10).intValue());
    }

    /**
     * 
     * @throws Exception
     */
    public void testObject_paging_offsetZero_limit() throws Exception {
        List<Employee> list =
            jdbcManager
                .selectBySql(Employee.class, sql2)
                .offset(0)
                .limit(3)
                .getResultList();
        assertEquals(3, list.size());
        assertEquals(1, list.get(0).employeeId);
        assertEquals(3, list.get(2).employeeId);
    }

    /**
     * 
     * @throws Exception
     */
    public void testObject_paging_offsetZero_limitZero() throws Exception {
        List<Integer> list =
            jdbcManager
                .selectBySql(Integer.class, sql2)
                .offset(0)
                .limit(0)
                .getResultList();
        assertEquals(14, list.size());
    }
}
