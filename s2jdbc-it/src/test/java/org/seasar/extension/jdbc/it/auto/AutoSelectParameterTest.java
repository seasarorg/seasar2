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
import java.util.List;
import java.util.Map;

import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.JoinType;
import org.seasar.extension.jdbc.it.entity.Department;
import org.seasar.extension.jdbc.it.entity.Employee;
import org.seasar.extension.unit.S2TestCase;

import static java.util.Arrays.*;

/**
 * @author taedium
 * 
 */
public class AutoSelectParameterTest extends S2TestCase {

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
    public void test_eq() throws Exception {
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("employeeName", "SMITH");
        List<Employee> list = jdbcManager.from(Employee.class).where(m)
                .getResultList();
        assertEquals(1, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void test_ne() throws Exception {
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("employeeName_NE", "SMITH");
        List<Employee> list = jdbcManager.from(Employee.class).where(m)
                .getResultList();
        assertEquals(13, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void test_gt_lt() throws Exception {
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("salary_GT", 1100);
        m.put("salary_LT", 2000);
        List<Employee> list = jdbcManager.from(Employee.class).where(m)
                .getResultList();
        assertEquals(5, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void test_ge_le() throws Exception {
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("salary_GE", 1100);
        m.put("salary_LE", 2000);
        List<Employee> list = jdbcManager.from(Employee.class).where(m)
                .getResultList();
        assertEquals(6, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void test_in() throws Exception {
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("employeeNo_IN", asList(7654, 7900, 7934));
        List<Employee> list = jdbcManager.from(Employee.class).where(m)
                .getResultList();
        assertEquals(3, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void test_not_in() throws Exception {
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("employeeNo_NOT_IN", asList(7654, 7900, 7934));
        List<Employee> list = jdbcManager.from(Employee.class).where(m)
                .getResultList();
        assertEquals(11, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void test_like() throws Exception {
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("employeeName_LIKE", "S%");
        List<Employee> list = jdbcManager.from(Employee.class).where(m)
                .getResultList();
        assertEquals(2, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void test_starts() throws Exception {
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("employeeName_STARTS", "S");
        List<Employee> list = jdbcManager.from(Employee.class).where(m)
                .getResultList();
        assertEquals(2, list.size());

    }

    /**
     * 
     * @throws Exception
     */
    public void test_ends() throws Exception {
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("employeeName_ENDS", "S");
        List<Employee> list = jdbcManager.from(Employee.class).where(m)
                .getResultList();
        assertEquals(3, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void test_contains() throws Exception {
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("employeeName_CONTAINS", "LL");
        List<Employee> list = jdbcManager.from(Employee.class).where(m)
                .getResultList();
        assertEquals(2, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void test_is_null() throws Exception {
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("managerId_IS_NULL", true);
        List<Employee> list = jdbcManager.from(Employee.class).where(m)
                .getResultList();
        assertEquals(1, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void test_is_not_null() throws Exception {
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("managerId_IS_NOT_NULL", true);
        List<Employee> list = jdbcManager.from(Employee.class).where(m)
                .getResultList();
        assertEquals(13, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void test_nestjoin() throws Exception {
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("employees.addressId", 3);
        List<Department> list = jdbcManager.from(Department.class).join(
                "employees").join("employees.address").where(m).getResultList();
        assertEquals(1, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void test_starJoin() throws Exception {
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("department.departmentName", "RESEARCH");
        m.put("address.street_STARTS", "STREET");
        m.put("salary_GE", 2000);
        List<Employee> list = jdbcManager.from(Employee.class).join("manager",
                JoinType.INNER).join("department").join("address").where(m)
                .getResultList();
        assertEquals(3, list.size());
    }
}
