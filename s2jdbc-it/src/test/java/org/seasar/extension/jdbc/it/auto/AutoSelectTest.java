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

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

import org.junit.runner.RunWith;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.JoinType;
import org.seasar.extension.jdbc.it.condition.DepartmentCondition;
import org.seasar.extension.jdbc.it.condition.EmployeeCondition;
import org.seasar.extension.jdbc.it.entity.Department;
import org.seasar.extension.jdbc.it.entity.Department3;
import org.seasar.extension.jdbc.it.entity.Department4;
import org.seasar.extension.jdbc.it.entity.Employee;
import org.seasar.extension.jdbc.manager.JdbcManagerImplementor;
import org.seasar.extension.jdbc.where.ComplexWhere;
import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.unit.Seasar2;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
@RunWith(Seasar2.class)
public class AutoSelectTest {

    private JdbcManager jdbcManager;

    private JdbcManagerImplementor implementor;

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
    public void testWhere_eq_condition() throws Exception {
        List<Employee> list =
            jdbcManager
                .from(Employee.class)
                .where(new EmployeeCondition().employeeName.eq("SMITH"))
                .getResultList();
        assertEquals(1, list.size());
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
    public void testWhere_ne_condition() throws Exception {
        List<Employee> list =
            jdbcManager
                .from(Employee.class)
                .where(new EmployeeCondition().employeeName.ne("SMITH"))
                .getResultList();
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
    public void testWhere_gt_lt_condition() throws Exception {
        List<Employee> list =
            jdbcManager
                .from(Employee.class)
                .where(
                    new EmployeeCondition().salary.gt(new BigDecimal("1100")).salary
                        .lt(new BigDecimal("2000")))
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
    public void testWhere_ge_le_condition() throws Exception {
        List<Employee> list =
            jdbcManager
                .from(Employee.class)
                .where(
                    new EmployeeCondition().salary.ge(new BigDecimal("1100")).salary
                        .le(new BigDecimal("2000")))
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
    public void testWhere_in_condition() throws Exception {
        List<Employee> list =
            jdbcManager
                .from(Employee.class)
                .where(new EmployeeCondition().employeeNo.in(7654, 7900, 7934))
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
    public void testWhere_notIn_condition() throws Exception {
        List<Employee> list =
            jdbcManager
                .from(Employee.class)
                .where(
                    new EmployeeCondition().employeeNo.notIn(7654, 7900, 7934))
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
    public void testWhere_like_condition() throws Exception {
        List<Employee> list =
            jdbcManager
                .from(Employee.class)
                .where(new EmployeeCondition().employeeName.like("S%"))
                .getResultList();
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
    public void testWhere_starts_condition() throws Exception {
        List<Employee> list =
            jdbcManager
                .from(Employee.class)
                .where(new EmployeeCondition().employeeName.starts("S"))
                .getResultList();
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
    public void testWhere_ends_condition() throws Exception {
        List<Employee> list =
            jdbcManager.from(Employee.class).where(
                new EmployeeCondition().employeeName.ends("S")).getResultList();
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
    public void testWhere_contains_condition() throws Exception {
        List<Employee> list =
            jdbcManager
                .from(Employee.class)
                .where(new EmployeeCondition().employeeName.contains("LL"))
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
    public void testWhere_isNull_condition() throws Exception {
        List<Employee> list =
            jdbcManager.from(Employee.class).where(
                new EmployeeCondition().managerId.isNull()).getResultList();
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
    public void testWhere_isNotNull_condition() throws Exception {
        List<Employee> list =
            jdbcManager.from(Employee.class).where(
                new EmployeeCondition().managerId.isNotNull()).getResultList();
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
                    new ComplexWhere().eq("departmentId", 1).or().ge(
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
                    new ComplexWhere()
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
    public void testJoin_nest_condition() throws Exception {
        List<Department> list =
            jdbcManager
                .from(Department.class)
                .join("employees")
                .join("employees.address")
                .where(new DepartmentCondition().employees().addressId.eq(3))
                .getResultList();
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
    public void testJoin_star_condition() throws Exception {
        List<Employee> list =
            jdbcManager
                .from(Employee.class)
                .join("manager", JoinType.INNER)
                .join("department")
                .join("address")
                .where(
                    new EmployeeCondition().salary
                        .ge(new BigDecimal("2000"))
                        .department().departmentName.eq("RESEARCH").and(
                        new EmployeeCondition().address().street
                            .starts("STREET")))
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
    public void testGetSingleResult_oneToMany() throws Exception {
        Department department =
            jdbcManager.from(Department.class).join("employees").where(
                new SimpleWhere().eq("departmentId", 1)).getSingleResult();
        assertNotNull(department);
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
    public void testGetSingleResult_NoResultException() throws Exception {
        try {
            jdbcManager
                .from(Employee.class)
                .where(new SimpleWhere().eq("employeeId", 100))
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
    public void testTransientAnnotation() throws Exception {
        Department3 department =
            jdbcManager.from(Department3.class).where(
                new SimpleWhere().eq("departmentId", 1)).getSingleResult();
        assertNull(department.departmentName);
    }

    /**
     * 
     * @throws Exception
     */
    public void testTransientModifier() throws Exception {
        Department4 department =
            jdbcManager.from(Department4.class).where(
                new SimpleWhere().eq("departmentId", 1)).getSingleResult();
        assertNull(department.departmentName);
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdate() throws Exception {
        if (!implementor.getDialect().supportsForUpdate()) {
            return;
        }
        jdbcManager.from(Employee.class).forUpdate().getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdate_leftOuterJoin() throws Exception {
        if (!implementor.getDialect().supportsForUpdate()) {
            return;
        }
        jdbcManager
            .from(Employee.class)
            .join("department")
            .forUpdate()
            .getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdate_innerJoin() throws Exception {
        if (!implementor.getDialect().supportsForUpdate()) {
            return;
        }
        jdbcManager
            .from(Employee.class)
            .join("department", JoinType.INNER)
            .forUpdate()
            .getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdate_pagingLimmit() throws Exception {
        if (!implementor.getDialect().supportsForUpdate()) {
            return;
        }
        jdbcManager
            .from(Employee.class)
            .orderBy("employeeName")
            .limit(3)
            .forUpdate()
            .getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdate_pagingOffsetLimmit() throws Exception {
        if (!implementor.getDialect().supportsForUpdate()) {
            return;
        }
        jdbcManager
            .from(Employee.class)
            .orderBy("employeeName")
            .offset(5)
            .limit(3)
            .forUpdate()
            .getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdate_UnsupportedOperationException() throws Exception {
        if (implementor.getDialect().supportsForUpdate()) {
            return;
        }
        try {
            jdbcManager.from(Employee.class).forUpdate();
            fail();
        } catch (UnsupportedOperationException e) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateWithColumn() throws Exception {
        if (!implementor.getDialect().supportsForUpdateWithColumn()) {
            return;
        }
        jdbcManager
            .from(Employee.class)
            .forUpdate("employeeName")
            .getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateWithColumn_leftOuterJoin() throws Exception {
        if (!implementor.getDialect().supportsForUpdateWithColumn()) {
            return;
        }
        jdbcManager.from(Employee.class).join("department").forUpdate(
            "employeeName").getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateWithColumn_innerJoin() throws Exception {
        if (!implementor.getDialect().supportsForUpdateWithColumn()) {
            return;
        }
        jdbcManager
            .from(Employee.class)
            .join("department", JoinType.INNER)
            .forUpdate("employeeName")
            .getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateWithColumn_pagingLimit() throws Exception {
        if (!implementor.getDialect().supportsForUpdateWithColumn()) {
            return;
        }
        jdbcManager
            .from(Employee.class)
            .orderBy("employeeName")
            .limit(3)
            .forUpdate("employeeName")
            .getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateWithColumn_pagingOffsetLimit() throws Exception {
        if (!implementor.getDialect().supportsForUpdateWithColumn()) {
            return;
        }
        jdbcManager
            .from(Employee.class)
            .orderBy("employeeName")
            .offset(5)
            .limit(3)
            .forUpdate("employeeName")
            .getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateWithColumn_UnsupportedOperationException()
            throws Exception {
        if (implementor.getDialect().supportsForUpdateWithColumn()) {
            return;
        }
        try {
            jdbcManager.from(Employee.class).forUpdate("employeeName");
            fail();
        } catch (UnsupportedOperationException e) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateNowait() throws Exception {
        if (!implementor.getDialect().supportsForUpdateNowait()) {
            return;
        }
        jdbcManager.from(Employee.class).forUpdateNowait().getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateNowait_leftOuterJoin() throws Exception {
        if (!implementor.getDialect().supportsForUpdateNowait()) {
            return;
        }
        jdbcManager
            .from(Employee.class)
            .join("department")
            .forUpdateNowait()
            .getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateNowait_innerJoin() throws Exception {
        if (!implementor.getDialect().supportsForUpdateNowait()) {
            return;
        }
        jdbcManager
            .from(Employee.class)
            .join("department", JoinType.INNER)
            .forUpdateNowait()
            .getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateNowait_pagingLimit() throws Exception {
        if (!implementor.getDialect().supportsForUpdateNowait()) {
            return;
        }
        jdbcManager
            .from(Employee.class)
            .orderBy("employeeName")
            .limit(3)
            .forUpdateNowait()
            .getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateNowait_pagingOffsetLimit() throws Exception {
        if (!implementor.getDialect().supportsForUpdateNowait()) {
            return;
        }
        jdbcManager
            .from(Employee.class)
            .orderBy("employeeName")
            .offset(5)
            .limit(3)
            .forUpdateNowait()
            .getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateNowait_UnsupportedOperationException()
            throws Exception {
        if (implementor.getDialect().supportsForUpdateNowait()) {
            return;
        }
        try {
            jdbcManager.from(Employee.class).forUpdateNowait();
            fail();
        } catch (UnsupportedOperationException e) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateNowaitWithColumn() throws Exception {
        if (!implementor.getDialect().supportsForUpdateNowaitWithColumn()) {
            return;
        }
        jdbcManager
            .from(Employee.class)
            .forUpdateNowait("employeeName")
            .getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateNowaitWithColumn_leftOuterJoin() throws Exception {
        if (!implementor.getDialect().supportsForUpdateNowaitWithColumn()) {
            return;
        }
        jdbcManager.from(Employee.class).join("department").forUpdateNowait(
            "employeeName").getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateNowaitWithColumn_innerJoin() throws Exception {
        if (!implementor.getDialect().supportsForUpdateNowaitWithColumn()) {
            return;
        }
        jdbcManager
            .from(Employee.class)
            .join("department", JoinType.INNER)
            .forUpdateNowait("employeeName")
            .getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateNowaitWithColumn_pagingLimit() throws Exception {
        if (!implementor.getDialect().supportsForUpdateNowaitWithColumn()) {
            return;
        }
        jdbcManager
            .from(Employee.class)
            .orderBy("employeeName")
            .limit(3)
            .forUpdateNowait("employeeName")
            .getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateNowaitWithColumn_pagingOffsetLimit()
            throws Exception {
        if (!implementor.getDialect().supportsForUpdateNowaitWithColumn()) {
            return;
        }
        jdbcManager
            .from(Employee.class)
            .orderBy("employeeName")
            .offset(5)
            .limit(3)
            .forUpdateNowait("employeeName")
            .getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateNowaitWithColumn_UnsupportedOperationException()
            throws Exception {
        if (implementor.getDialect().supportsForUpdateNowaitWithColumn()) {
            return;
        }
        try {
            jdbcManager.from(Employee.class).forUpdateNowait("employeeName");
            fail();
        } catch (UnsupportedOperationException e) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateWait() throws Exception {
        if (!implementor.getDialect().supportsForUpdateWait()) {
            return;
        }
        jdbcManager.from(Employee.class).forUpdateWait(1).getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateWait_leftOuterJoin() throws Exception {
        if (!implementor.getDialect().supportsForUpdateWait()) {
            return;
        }
        jdbcManager
            .from(Employee.class)
            .join("department")
            .forUpdateWait(1)
            .getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateWait_innerJoin() throws Exception {
        if (!implementor.getDialect().supportsForUpdateWait()) {
            return;
        }
        jdbcManager
            .from(Employee.class)
            .join("department", JoinType.INNER)
            .forUpdateWait(1)
            .getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateWait_pagingOffsetLimit() throws Exception {
        if (!implementor.getDialect().supportsForUpdateWait()) {
            return;
        }
        jdbcManager
            .from(Employee.class)
            .orderBy("employeeName")
            .offset(5)
            .limit(3)
            .forUpdateWait(1)
            .getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateWait_UnsupportedOperationException()
            throws Exception {
        if (implementor.getDialect().supportsForUpdateWait()) {
            return;
        }
        try {
            jdbcManager.from(Employee.class).forUpdateWait(1);
            fail();
        } catch (UnsupportedOperationException e) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateWaitWithColumn() throws Exception {
        if (!implementor.getDialect().supportsForUpdateWaitWithColumn()) {
            return;
        }
        jdbcManager
            .from(Employee.class)
            .forUpdateWait("employeeName", 1)
            .getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateWaitWithColumn_leftOuterJoin() throws Exception {
        if (!implementor.getDialect().supportsForUpdateWaitWithColumn()) {
            return;
        }
        jdbcManager.from(Employee.class).join("department").forUpdateWait(
            "employeeName",
            1).getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateWaitWithColumn_innerJoin() throws Exception {
        if (!implementor.getDialect().supportsForUpdateWaitWithColumn()) {
            return;
        }
        jdbcManager
            .from(Employee.class)
            .join("department", JoinType.INNER)
            .forUpdateWait("employeeName", 1)
            .getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateWaitWithColumn_pagingLimit() throws Exception {
        if (!implementor.getDialect().supportsForUpdateWaitWithColumn()) {
            return;
        }
        jdbcManager
            .from(Employee.class)
            .orderBy("employeeName")
            .limit(3)
            .forUpdateWait("employeeName", 1)
            .getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateWaitWithColumn_pagingOffsetLimit()
            throws Exception {
        if (!implementor.getDialect().supportsForUpdateWaitWithColumn()) {
            return;
        }
        jdbcManager
            .from(Employee.class)
            .orderBy("employeeName")
            .offset(5)
            .limit(3)
            .forUpdateWait("employeeName", 1)
            .getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateWaitWithColumn_UnsupportedOperationException()
            throws Exception {
        if (implementor.getDialect().supportsForUpdateWaitWithColumn()) {
            return;
        }
        try {
            jdbcManager.from(Employee.class).forUpdateWait("employeeName", 1);
            fail();
        } catch (UnsupportedOperationException e) {
        }
    }
}
