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

import java.util.List;

import javax.persistence.NonUniqueResultException;

import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.JoinType;
import org.seasar.extension.jdbc.it.entity.Department;
import org.seasar.extension.jdbc.it.entity.Department3;
import org.seasar.extension.jdbc.it.entity.Department4;
import org.seasar.extension.jdbc.it.entity.Employee;
import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.extension.unit.S2TestCase;

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
        List<Employee> list =
            jdbcManager
                .from(Employee.class)
                .orderBy("employeeId")
                .getResultList();
        assertEquals(14, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void testPaging_offsetOnly() throws Exception {
        List<Employee> list =
            jdbcManager
                .from(Employee.class)
                .offset(3)
                .orderBy("employeeId")
                .getResultList();
        assertEquals(11, list.size());
        assertEquals(4, list.get(0).employeeId);
        assertEquals(14, list.get(10).employeeId);
    }

    /**
     * 
     * @throws Exception
     */
    public void testPaging_limitOnly() throws Exception {
        List<Employee> list =
            jdbcManager
                .from(Employee.class)
                .limit(3)
                .orderBy("employeeId")
                .getResultList();
        assertEquals(3, list.size());
        assertEquals(1, list.get(0).employeeId);
        assertEquals(3, list.get(2).employeeId);
    }

    /**
     * 
     * @throws Exception
     */
    public void testPaging_offsetZero_limitZero() throws Exception {
        List<Employee> list =
            jdbcManager.from(Employee.class).offset(0).limit(0).orderBy(
                "employeeId").getResultList();
        assertEquals(14, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void testPaging_offset_limitZero() throws Exception {
        List<Employee> list =
            jdbcManager.from(Employee.class).offset(3).limit(0).orderBy(
                "employeeId").getResultList();
        assertEquals(11, list.size());
        assertEquals(4, list.get(0).employeeId);
        assertEquals(14, list.get(10).employeeId);
    }

    /**
     * 
     * @throws Exception
     */
    public void testPaging_offsetZero_limit() throws Exception {
        List<Employee> list =
            jdbcManager.from(Employee.class).offset(0).limit(3).orderBy(
                "employeeId").getResultList();
        assertEquals(3, list.size());
        assertEquals(1, list.get(0).employeeId);
        assertEquals(3, list.get(2).employeeId);
    }

    /**
     * 
     * @throws Exception
     */
    public void testPaging_offset_limit() throws Exception {
        List<Employee> list =
            jdbcManager.from(Employee.class).offset(3).limit(5).orderBy(
                "employeeId").getResultList();
        assertEquals(5, list.size());
        assertEquals(4, list.get(0).employeeId);
        assertEquals(8, list.get(4).employeeId);
    }

    /**
     * 
     * @throws Exception
     */
    public void testWhere_eq() throws Exception {
        List<Employee> list =
            jdbcManager.from(Employee.class).where(
                new SimpleWhere().eq("employeeName", "SMITH")).getResultList();
        assertEquals(1, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void testWhere_eq_null() throws Exception {
        List<Employee> list =
            jdbcManager.from(Employee.class).where(
                new SimpleWhere().eq("employeeName", null)).getResultList();
        assertEquals(14, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void testWhere_eq_ignoreWhitespace() throws Exception {
        List<Employee> list =
            jdbcManager
                .from(Employee.class)
                .where(
                    new SimpleWhere().ignoreWhitespace().eq(
                        "employeeName",
                        "  "))
                .getResultList();
        assertEquals(14, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void testWhere_ne() throws Exception {
        List<Employee> list =
            jdbcManager.from(Employee.class).where(
                new SimpleWhere().ne("employeeName", "SMITH")).getResultList();
        assertEquals(13, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void testWhere_gt_lt() throws Exception {
        List<Employee> list =
            jdbcManager
                .from(Employee.class)
                .where(new SimpleWhere().gt("salary", 1100).lt("salary", 2000))
                .getResultList();
        assertEquals(5, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void testWhere_ge_le() throws Exception {
        List<Employee> list =
            jdbcManager
                .from(Employee.class)
                .where(new SimpleWhere().ge("salary", 1100).le("salary", 2000))
                .getResultList();
        assertEquals(6, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void testWhere_in() throws Exception {
        List<Employee> list =
            jdbcManager
                .from(Employee.class)
                .where(new SimpleWhere().in("employeeNo", 7654, 7900, 7934))
                .getResultList();
        assertEquals(3, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void testWhere_notIn() throws Exception {
        List<Employee> list =
            jdbcManager
                .from(Employee.class)
                .where(new SimpleWhere().notIn("employeeNo", 7654, 7900, 7934))
                .getResultList();
        assertEquals(11, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void testWhere_like() throws Exception {
        List<Employee> list =
            jdbcManager.from(Employee.class).where(
                new SimpleWhere().like("employeeName", "S%")).getResultList();
        assertEquals(2, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void testWhere_starts() throws Exception {
        List<Employee> list =
            jdbcManager.from(Employee.class).where(
                new SimpleWhere().starts("employeeName", "S")).getResultList();
        assertEquals(2, list.size());

    }

    /**
     * 
     * @throws Exception
     */
    public void testWhere_ends() throws Exception {
        List<Employee> list =
            jdbcManager.from(Employee.class).where(
                new SimpleWhere().ends("employeeName", "S")).getResultList();
        assertEquals(3, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void testWhere_contains() throws Exception {
        List<Employee> list =
            jdbcManager
                .from(Employee.class)
                .where(new SimpleWhere().contains("employeeName", "LL"))
                .getResultList();
        assertEquals(2, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void testWhere_isNull() throws Exception {
        List<Employee> list =
            jdbcManager.from(Employee.class).where(
                new SimpleWhere().isNull("managerId", true)).getResultList();
        assertEquals(1, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void testWhere_isNull_false() throws Exception {
        List<Employee> list =
            jdbcManager.from(Employee.class).where(
                new SimpleWhere().isNull("managerId", false)).getResultList();
        assertEquals(14, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void testWhere_isNull_null() throws Exception {
        List<Employee> list =
            jdbcManager.from(Employee.class).where(
                new SimpleWhere().isNull("managerId", null)).getResultList();
        assertEquals(14, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void testWhere_isNotNull() throws Exception {
        List<Employee> list =
            jdbcManager.from(Employee.class).where(
                new SimpleWhere().isNotNull("managerId", true)).getResultList();
        assertEquals(13, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void testWhere_isNotNull_false() throws Exception {
        List<Employee> list =
            jdbcManager
                .from(Employee.class)
                .where(new SimpleWhere().isNotNull("managerId", false))
                .getResultList();
        assertEquals(14, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void testWhere_isNotNull_null() throws Exception {
        List<Employee> list =
            jdbcManager.from(Employee.class).where(
                new SimpleWhere().isNotNull("managerId", null)).getResultList();
        assertEquals(14, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void testWhere_or() throws Exception {
        List<Employee> list =
            jdbcManager
                .from(Employee.class)
                .where(
                    new SimpleWhere().eq("departmentId", 1).or().ge(
                        "salary",
                        3000))
                .orderBy("employeeName")
                .getResultList();
        assertEquals(5, list.size());
        assertEquals("CLARK", list.get(0).employeeName);
        assertEquals("FORD", list.get(1).employeeName);
        assertEquals("KING", list.get(2).employeeName);
        assertEquals("MILLER", list.get(3).employeeName);
        assertEquals("SCOTT", list.get(4).employeeName);
    }

    /**
     * 
     * @throws Exception
     */
    public void testWhere_or_ignoreWhitespace() throws Exception {
        List<Employee> list =
            jdbcManager
                .from(Employee.class)
                .where(
                    new SimpleWhere()
                        .ignoreWhitespace()
                        .eq("employeeName", "")
                        .or()
                        .ge("salary", 3000)
                        .eq("employeeName", ""))
                .orderBy("employeeName")
                .getResultList();
        assertEquals(3, list.size());
        assertEquals("FORD", list.get(0).employeeName);
        assertEquals("KING", list.get(1).employeeName);
        assertEquals("SCOTT", list.get(2).employeeName);
    }

    /**
     * 
     * @throws Exception
     */
    public void testJoin_nest() throws Exception {
        List<Department> list =
            jdbcManager.from(Department.class).join("employees").join(
                "employees.address").where(
                new SimpleWhere().eq("employees.addressId", 3)).getResultList();
        assertEquals(1, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void testJoin_star() throws Exception {
        List<Employee> list =
            jdbcManager
                .from(Employee.class)
                .join("manager", JoinType.INNER)
                .join("department")
                .join("address")
                .where(
                    new SimpleWhere().eq(
                        "department.departmentName",
                        "RESEARCH").starts("address.street", "STREET").ge(
                        "salary",
                        2000))
                .getResultList();
        assertEquals(3, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void testGetSingleResult() throws Exception {
        Employee employee =
            jdbcManager.from(Employee.class).where(
                new SimpleWhere().eq("employeeId", 1)).getSingleResult();
        assertNotNull(employee);
    }

    /**
     * 
     * @throws Exception
     */
    public void testGetSingleResult_null() throws Exception {
        Employee employee =
            jdbcManager.from(Employee.class).where(
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

    /**
     * 
     * @throws Exception
     */
    public void testTransientAnnotationTx() throws Exception {
        Department3 department =
            jdbcManager.from(Department3.class).where(
                new SimpleWhere().eq("departmentId", 1)).getSingleResult();
        assertNull(department.departmentName);
    }

    /**
     * 
     * @throws Exception
     */
    public void testTransientModifierTx() throws Exception {
        Department4 department =
            jdbcManager.from(Department4.class).where(
                new SimpleWhere().eq("departmentId", 1)).getSingleResult();
        assertNull(department.departmentName);
    }

}
