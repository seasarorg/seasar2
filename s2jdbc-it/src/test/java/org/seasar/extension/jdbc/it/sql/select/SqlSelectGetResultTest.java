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

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

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
public class SqlSelectGetResultTest {

    private JdbcManager jdbcManager;

    /**
     * 
     * @throws Exception
     */
    public void testBean_getResultList_NoResultException() throws Exception {
        String sql = "SELECT * FROM EMPLOYEE WHERE EMPLOYEE_ID = 100";
        try {
            jdbcManager
                .selectBySql(Employee.class, sql)
                .disallowNoResult()
                .getResultList();
            fail();
        } catch (NoResultException e) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testBean_getSingleResult() throws Exception {
        String sql = "SELECT * FROM EMPLOYEE WHERE EMPLOYEE_ID = 1";
        Employee employee =
            jdbcManager.selectBySql(Employee.class, sql).getSingleResult();
        assertNotNull(employee);
    }

    /**
     * 
     * @throws Exception
     */
    public void testBean_getSingleResult_NonUniqueResultException()
            throws Exception {
        String sql = "SELECT * FROM EMPLOYEE WHERE DEPARTMENT_ID = 1";
        try {
            jdbcManager.selectBySql(Employee.class, sql).getSingleResult();
            fail();
        } catch (NonUniqueResultException e) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testBean_getSingleResult_NoResultException() throws Exception {
        String sql = "SELECT * FROM EMPLOYEE WHERE EMPLOYEE_ID = 100";
        try {
            jdbcManager
                .selectBySql(Employee.class, sql)
                .disallowNoResult()
                .getSingleResult();
            fail();
        } catch (NoResultException e) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testBean_getSingleResult_null() throws Exception {
        String sql = "SELECT * FROM EMPLOYEE WHERE EMPLOYEE_ID = 100";
        Employee employee =
            jdbcManager.selectBySql(Employee.class, sql).getSingleResult();
        assertNull(employee);
    }

    /**
     * 
     * @throws Exception
     */
    public void testMap_getResultList_NoResultException() throws Exception {
        String sql = "SELECT * FROM EMPLOYEE WHERE EMPLOYEE_ID = 100";
        try {
            jdbcManager
                .selectBySql(Map.class, sql)
                .disallowNoResult()
                .getResultList();
            fail();
        } catch (NoResultException e) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testMap_getSingleResult() throws Exception {
        String sql = "SELECT * FROM EMPLOYEE WHERE EMPLOYEE_ID = 1";
        Map<?, ?> employee =
            jdbcManager.selectBySql(Map.class, sql).getSingleResult();
        assertNotNull(employee);
    }

    /**
     * 
     * @throws Exception
     */
    public void testMap_getSingleResult_NonUniqueResultException()
            throws Exception {
        String sql = "SELECT * FROM EMPLOYEE WHERE DEPARTMENT_ID = 1";
        try {
            jdbcManager.selectBySql(Map.class, sql).getSingleResult();
            fail();
        } catch (NonUniqueResultException e) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testMap_getSingleResult_NoResultException() throws Exception {
        String sql = "SELECT * FROM EMPLOYEE WHERE EMPLOYEE_ID = 100";
        try {
            jdbcManager
                .selectBySql(Map.class, sql)
                .disallowNoResult()
                .getSingleResult();
            fail();
        } catch (NoResultException e) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testMap_getSingleResult_null() throws Exception {
        String sql = "SELECT * FROM EMPLOYEE WHERE EMPLOYEE_ID = 100";
        Map<?, ?> employee =
            jdbcManager.selectBySql(Map.class, sql).getSingleResult();
        assertNull(employee);
    }

    /**
     * 
     * @throws Exception
     */
    public void testMap_getSingleResult_AsLinkedHashMap() throws Exception {
        String sql =
            "SELECT VERSION, EMPLOYEE_NAME, EMPLOYEE_ID FROM EMPLOYEE WHERE EMPLOYEE_ID = 1";
        Map<?, ?> employee =
            jdbcManager.selectBySql(LinkedHashMap.class, sql).getSingleResult();
        Iterator<?> it = employee.keySet().iterator();
        assertEquals("version", it.next());
        assertNotNull(employee.get("version"));
        assertEquals("employeeName", it.next());
        assertNotNull(employee.get("employeeName"));
        assertNotNull(employee.get("employeeId"));
    }

    /**
     * 
     * @throws Exception
     */
    public void testMap_getSingleResult_AsTreeMap() throws Exception {
        String sql =
            "SELECT VERSION, EMPLOYEE_NAME, EMPLOYEE_ID FROM EMPLOYEE WHERE EMPLOYEE_ID = 1";
        Map<?, ?> employee =
            jdbcManager.selectBySql(TreeMap.class, sql).getSingleResult();
        Iterator<?> it = employee.keySet().iterator();
        assertEquals("employeeId", it.next());
        assertNotNull(employee.get("employeeId"));
        assertEquals("employeeName", it.next());
        assertNotNull(employee.get("employeeName"));
        assertEquals("version", it.next());
        assertNotNull(employee.get("version"));
    }

    /**
     * 
     * @throws Exception
     */
    public void testObject_getResultList_NoResultException() throws Exception {
        String sql = "SELECT EMPLOYEE_ID FROM EMPLOYEE WHERE EMPLOYEE_ID = 100";
        try {
            jdbcManager
                .selectBySql(Integer.class, sql)
                .disallowNoResult()
                .getResultList();
            fail();
        } catch (NoResultException e) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testObject_getSingleResult() throws Exception {
        String sql = "SELECT EMPLOYEE_ID FROM EMPLOYEE WHERE EMPLOYEE_ID = 1";
        Integer employeeId =
            jdbcManager.selectBySql(Integer.class, sql).getSingleResult();
        assertNotNull(employeeId);
    }

    /**
     * 
     * @throws Exception
     */
    public void testObject_getSingleResult_NonUniqueResultException()
            throws Exception {
        String sql = "SELECT EMPLOYEE_ID FROM EMPLOYEE WHERE DEPARTMENT_ID = 1";
        try {
            jdbcManager.selectBySql(Integer.class, sql).getSingleResult();
            fail();
        } catch (NonUniqueResultException e) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testObject_getSingleResult_NoResultException() throws Exception {
        String sql = "SELECT EMPLOYEE_ID FROM EMPLOYEE WHERE EMPLOYEE_ID = 100";
        try {
            jdbcManager
                .selectBySql(Integer.class, sql)
                .disallowNoResult()
                .getSingleResult();
            fail();
        } catch (NoResultException e) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testObject_getSingleResult_null() throws Exception {
        String sql = "SELECT EMPLOYEE_ID FROM EMPLOYEE WHERE EMPLOYEE_ID = 100";
        Integer employeeId =
            jdbcManager.selectBySql(Integer.class, sql).getSingleResult();
        assertNull(employeeId);
    }

}
