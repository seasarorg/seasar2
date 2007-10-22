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

import javax.persistence.NonUniqueResultException;

import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.JoinType;
import org.seasar.extension.jdbc.exception.NonArrayInConditionRuntimeException;
import org.seasar.extension.jdbc.exception.NonBooleanIsNullConditionRuntimeException;
import org.seasar.extension.jdbc.it.entity.Department;
import org.seasar.extension.jdbc.it.entity.Employee;
import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.extension.unit.S2TestCase;

import static java.util.Arrays.*;

/**
 * @author taedium
 * 
 */
public class AutoSelectTest extends S2TestCase {

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
    public void testPaging() throws Exception {
        List<Employee> list = jdbcManager.from(Employee.class).orderBy(
                "employeeId").getResultList();
        assertEquals(14, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void testPaging_offsetOnly() throws Exception {
        List<Employee> list = jdbcManager.from(Employee.class).offset(3)
                .orderBy("employeeId").getResultList();
        assertEquals(11, list.size());
        assertEquals(4, list.get(0).employeeId);
        assertEquals(14, list.get(10).employeeId);
    }

    /**
     * 
     * @throws Exception
     */
    public void testPaging_limitOnly() throws Exception {
        List<Employee> list = jdbcManager.from(Employee.class).limit(3)
                .orderBy("employeeId").getResultList();
        assertEquals(3, list.size());
        assertEquals(1, list.get(0).employeeId);
        assertEquals(3, list.get(2).employeeId);
    }

    /**
     * 
     * @throws Exception
     */
    public void testPaging_offsetZero_limitZero() throws Exception {
        List<Employee> list = jdbcManager.from(Employee.class).offset(0).limit(
                0).orderBy("employeeId").getResultList();
        assertEquals(14, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void testPaging_offset_limitZero() throws Exception {
        List<Employee> list = jdbcManager.from(Employee.class).offset(3).limit(
                0).orderBy("employeeId").getResultList();
        assertEquals(11, list.size());
        assertEquals(4, list.get(0).employeeId);
        assertEquals(14, list.get(10).employeeId);
    }

    /**
     * 
     * @throws Exception
     */
    public void testPaging_offsetZero_limit() throws Exception {
        List<Employee> list = jdbcManager.from(Employee.class).offset(0).limit(
                3).orderBy("employeeId").getResultList();
        assertEquals(3, list.size());
        assertEquals(1, list.get(0).employeeId);
        assertEquals(3, list.get(2).employeeId);
    }

    /**
     * 
     * @throws Exception
     */
    public void testPaging_offset_limit() throws Exception {
        List<Employee> list = jdbcManager.from(Employee.class).offset(3).limit(
                5).orderBy("employeeId").getResultList();
        assertEquals(5, list.size());
        assertEquals(4, list.get(0).employeeId);
        assertEquals(8, list.get(4).employeeId);
    }

    /**
     * 
     * @throws Exception
     */
    public void testWhere_eq() throws Exception {
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
    public void testWhere_ne() throws Exception {
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
    public void testWhere_gt_lt() throws Exception {
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
    public void testWhere_ge_le() throws Exception {
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
    public void testWhere_in() throws Exception {
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("employeeNo_IN", new Object[] { 7654, 7900, 7934 });
        List<Employee> list = jdbcManager.from(Employee.class).where(m)
                .getResultList();
        assertEquals(3, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void testWhere_in_NonArrayInConditionRuntimeException()
            throws Exception {
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("employeeNo_IN", asList(7654, 7900, 7934));
        try {
            jdbcManager.from(Employee.class).where(m).getResultList();
            fail();
        } catch (NonArrayInConditionRuntimeException e) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testWhere_notIn() throws Exception {
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("employeeNo_NOT_IN", new Object[] { 7654, 7900, 7934 });
        List<Employee> list = jdbcManager.from(Employee.class).where(m)
                .getResultList();
        assertEquals(11, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void testWhere_notIn_NonArrayInConditionRuntimeException()
            throws Exception {
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("employeeNo_NOT_IN", asList(7654, 7900, 7934));
        try {
            jdbcManager.from(Employee.class).where(m).getResultList();
            fail();
        } catch (NonArrayInConditionRuntimeException e) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testWhere_like() throws Exception {
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
    public void testWhere_starts() throws Exception {
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
    public void testWhere_ends() throws Exception {
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
    public void testWhere_contains() throws Exception {
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
    public void testWhere_isNull() throws Exception {
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
    public void testWhere_isNull_NonBooleanIsNullConditionRuntimeException()
            throws Exception {
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("managerId_IS_NULL", "true");
        try {
            jdbcManager.from(Employee.class).where(m).getResultList();
            fail();
        } catch (NonBooleanIsNullConditionRuntimeException e) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testWhere_isNotNull() throws Exception {
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
    public void testWhere_isNotNull_NonBooleanIsNullConditionRuntimeException()
            throws Exception {
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("managerId_IS_NOT_NULL", "true");
        try {
            jdbcManager.from(Employee.class).where(m).getResultList();
            fail();
        } catch (NonBooleanIsNullConditionRuntimeException e) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testJoin_nest() throws Exception {
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
    public void testJoin_star() throws Exception {
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("department.departmentName", "RESEARCH");
        m.put("address.street_STARTS", "STREET");
        m.put("salary_GE", 2000);
        List<Employee> list = jdbcManager.from(Employee.class).join("manager",
                JoinType.INNER).join("department").join("address").where(m)
                .getResultList();
        assertEquals(3, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void testGetSingleResult() throws Exception {
        Employee employee = jdbcManager.from(Employee.class).where(
                new SimpleWhere().eq("employeeId", 1)).getSingleResult();
        assertNotNull(employee);
    }

    /**
     * 
     * @throws Exception
     */
    public void testGetSingleResult_null() throws Exception {
        Employee employee = jdbcManager.from(Employee.class).where(
                new SimpleWhere().eq("employeeId", 100)).getSingleResult();
        assertNull(employee);
    }

    /**
     * 
     * @throws Exception
     */
    public void testGetSingleResult_NonUniqueResultException() throws Exception {
        try {
            jdbcManager.from(Employee.class).where(
                    new SimpleWhere().eq("departmentId", 1)).getSingleResult();
            fail();
        } catch (NonUniqueResultException e) {
        }
    }
}
