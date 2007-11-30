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
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

import org.junit.runner.RunWith;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.JoinType;
import org.seasar.extension.jdbc.exception.BaseJoinNotFoundRuntimeException;
import org.seasar.extension.jdbc.exception.IllegalIdPropertySizeRuntimeException;
import org.seasar.extension.jdbc.exception.PropertyNotFoundRuntimeException;
import org.seasar.extension.jdbc.it.condition.DepartmentCondition;
import org.seasar.extension.jdbc.it.condition.EmployeeCondition;
import org.seasar.extension.jdbc.it.entity.CompKeyEmployee;
import org.seasar.extension.jdbc.it.entity.Department;
import org.seasar.extension.jdbc.it.entity.Department3;
import org.seasar.extension.jdbc.it.entity.Department4;
import org.seasar.extension.jdbc.it.entity.Employee;
import org.seasar.extension.jdbc.it.entity.Tense;
import org.seasar.extension.jdbc.manager.JdbcManagerImplementor;
import org.seasar.extension.jdbc.where.ComplexWhere;
import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.unit.Seasar2;

import static org.junit.Assert.*;
import static org.seasar.extension.jdbc.SelectForUpdateType.*;
import static org.seasar.extension.jdbc.parameter.Parameter.*;

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
    public void testWhere_map_illegalPropertyName() throws Exception {
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("illegal", 1);
        try {
            jdbcManager.from(Employee.class).where(m).getResultList();
            fail();
        } catch (PropertyNotFoundRuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testWhere_map_illegalPropertyName2() throws Exception {
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("department.illegal", 1);
        try {
            jdbcManager
                .from(Employee.class)
                .leftOuterJoin("department")
                .where(m)
                .getResultList();
            fail();
        } catch (PropertyNotFoundRuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testWhere_map_illegalPropertyName3() throws Exception {
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("illegal.illegal2", 1);
        try {
            jdbcManager
                .from(Employee.class)
                .leftOuterJoin("department")
                .where(m)
                .getResultList();
            fail();
        } catch (BaseJoinNotFoundRuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testWhere_simpleWhere_illegalPropertyName2() throws Exception {
        try {
            jdbcManager.from(Employee.class).leftOuterJoin("department").where(
                new SimpleWhere().eq("department.illegal", 1)).getResultList();
            fail();
        } catch (PropertyNotFoundRuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testWhere_simpleWhere_illegalPropertyName3() throws Exception {
        try {
            jdbcManager.from(Employee.class).leftOuterJoin("department").where(
                new SimpleWhere().eq("illegal.illegal2", 1)).getResultList();
            fail();
        } catch (BaseJoinNotFoundRuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testJoin_nest_condition() throws Exception {
        List<Department> list =
            jdbcManager
                .from(Department.class)
                .leftOuterJoin("employees")
                .leftOuterJoin("employees.address")
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
                .leftOuterJoin("department")
                .leftOuterJoin("address")
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
            jdbcManager.from(Department.class).leftOuterJoin("employees").leftOuterJoin(
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
                .leftOuterJoin("department")
                .leftOuterJoin("address")
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
    public void testJoin_illegalPropertyName() throws Exception {
        try {
            jdbcManager.from(Department.class).leftOuterJoin("illegal").getResultList();
            fail();
        } catch (PropertyNotFoundRuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testJoin_illegalPropertyName2() throws Exception {
        try {
            jdbcManager.from(Department.class).leftOuterJoin("employees").leftOuterJoin(
                "employees.illegal").getResultList();
            fail();
        } catch (PropertyNotFoundRuntimeException e) {
            System.out.println(e.getMessage());
        }
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
            jdbcManager.from(Department.class).leftOuterJoin("employees").where(
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
        if (!implementor.getDialect().supportsForUpdate(NORMAL, false)) {
            return;
        }
        jdbcManager.from(Employee.class).forUpdate().getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdate_leftOuterJoin() throws Exception {
        if (!implementor.getDialect().supportsForUpdate(NORMAL, false)
            || !implementor.getDialect().supportsOuterJoinForUpdate()) {
            return;
        }
        jdbcManager
            .from(Employee.class)
            .leftOuterJoin("department")
            .forUpdate()
            .getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdate_innerJoin() throws Exception {
        if (!implementor.getDialect().supportsForUpdate(NORMAL, false)) {
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
    public void testForUpdate_paging_UnsupportedOperationException()
            throws Exception {
        if (!implementor.getDialect().supportsForUpdate(NORMAL, false)) {
            return;
        }
        try {
            jdbcManager
                .from(Employee.class)
                .orderBy("employeeName")
                .offset(5)
                .limit(3)
                .forUpdate()
                .getResultList();
            fail();
        } catch (UnsupportedOperationException e) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdate_UnsupportedOperationException() throws Exception {
        if (implementor.getDialect().supportsForUpdate(NORMAL, false)) {
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
    public void testForUpdateWithTarget() throws Exception {
        if (!implementor.getDialect().supportsForUpdate(NORMAL, true)) {
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
    public void testForUpdateWithTarget_leftOuterJoin() throws Exception {
        if (!implementor.getDialect().supportsForUpdate(NORMAL, true)
            || !implementor.getDialect().supportsOuterJoinForUpdate()) {
            return;
        }
        jdbcManager.from(Employee.class).leftOuterJoin("department").forUpdate(
            "employeeName").getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateWithTarget_leftOuterJoin2() throws Exception {
        if (!implementor.getDialect().supportsForUpdate(NORMAL, true)
            || !implementor.getDialect().supportsOuterJoinForUpdate()) {
            return;
        }
        jdbcManager.from(Employee.class).leftOuterJoin("department").forUpdate(
            "department.location").getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateWithTarget_innerJoin() throws Exception {
        if (!implementor.getDialect().supportsForUpdate(NORMAL, true)) {
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
    public void testForUpdateWithTarget_innerJoin2() throws Exception {
        if (!implementor.getDialect().supportsForUpdate(NORMAL, true)) {
            return;
        }
        jdbcManager
            .from(Employee.class)
            .join("department", JoinType.INNER)
            .forUpdate("department.location")
            .getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateWithTarget_paging_UnsupportedOperationException()
            throws Exception {
        if (!implementor.getDialect().supportsForUpdate(NORMAL, true)) {
            return;
        }
        try {
            jdbcManager
                .from(Employee.class)
                .orderBy("employeeName")
                .offset(5)
                .limit(3)
                .forUpdate("employeeName")
                .getResultList();
            fail();
        } catch (UnsupportedOperationException e) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateWithTarget_UnsupportedOperationException()
            throws Exception {
        if (implementor.getDialect().supportsForUpdate(NORMAL, true)) {
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
    public void testForUpdateWithTargets() throws Exception {
        if (!implementor.getDialect().supportsForUpdate(NORMAL, true)) {
            return;
        }
        jdbcManager
            .from(Employee.class)
            .forUpdate("employeeName", "salary")
            .getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateWithTargets_leftOuterJoin() throws Exception {
        if (!implementor.getDialect().supportsForUpdate(NORMAL, true)
            || !implementor.getDialect().supportsOuterJoinForUpdate()) {
            return;
        }
        jdbcManager.from(Employee.class).leftOuterJoin("department").forUpdate(
            "employeeName",
            "department.location").getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateWithTargets_innerJoin() throws Exception {
        if (!implementor.getDialect().supportsForUpdate(NORMAL, true)) {
            return;
        }
        jdbcManager
            .from(Employee.class)
            .join("department", JoinType.INNER)
            .forUpdate("employeeName", "department.location")
            .getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateWithTargets_paging_UnsupportedOperationException()
            throws Exception {
        if (!implementor.getDialect().supportsForUpdate(NORMAL, true)) {
            return;
        }
        try {
            jdbcManager
                .from(Employee.class)
                .orderBy("employeeName")
                .offset(5)
                .limit(3)
                .forUpdate("employeeName", "salary")
                .getResultList();
            fail();
        } catch (UnsupportedOperationException e) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateWithTargets_UnsupportedOperationException()
            throws Exception {
        if (implementor.getDialect().supportsForUpdate(NORMAL, true)) {
            return;
        }
        try {
            jdbcManager
                .from(Employee.class)
                .forUpdate("employeeName", "salary");
            fail();
        } catch (UnsupportedOperationException e) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateWithTargets_illegalPropertyName() throws Exception {
        if (!implementor.getDialect().supportsForUpdate(NORMAL, true)) {
            return;
        }
        try {
            jdbcManager
                .from(Employee.class)
                .forUpdate("illegal")
                .getResultList();
            fail();
        } catch (PropertyNotFoundRuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateWithTargets_illegalPropertyName2()
            throws Exception {
        if (!implementor.getDialect().supportsForUpdate(NORMAL, true)) {
            return;
        }
        try {
            jdbcManager
                .from(Employee.class)
                .join("department", JoinType.INNER)
                .forUpdate("illegal.location")
                .getResultList();
            fail();
        } catch (BaseJoinNotFoundRuntimeException e) {
            System.out.println(e.getMessage());
        }

    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateWithTargets_illegalPropertyName3()
            throws Exception {
        if (!implementor.getDialect().supportsForUpdate(NORMAL, true)) {
            return;
        }
        try {
            jdbcManager
                .from(Employee.class)
                .join("department", JoinType.INNER)
                .forUpdate("department")
                .getResultList();
            fail();
        } catch (PropertyNotFoundRuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateNowait() throws Exception {
        if (!implementor.getDialect().supportsForUpdate(NOWAIT, false)) {
            return;
        }
        jdbcManager.from(Employee.class).forUpdateNowait().getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateNowait_leftOuterJoin() throws Exception {
        if (!implementor.getDialect().supportsForUpdate(NOWAIT, false)
            || !implementor.getDialect().supportsOuterJoinForUpdate()) {
            return;
        }
        jdbcManager
            .from(Employee.class)
            .leftOuterJoin("department")
            .forUpdateNowait()
            .getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateNowait_innerJoin() throws Exception {
        if (!implementor.getDialect().supportsForUpdate(NOWAIT, false)) {
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
    public void testForUpdateNowait_paging_UnsupportedOperationException()
            throws Exception {
        if (!implementor.getDialect().supportsForUpdate(NOWAIT, false)) {
            return;
        }
        try {
            jdbcManager
                .from(Employee.class)
                .orderBy("employeeName")
                .offset(5)
                .limit(3)
                .forUpdateNowait()
                .getResultList();
            fail();
        } catch (UnsupportedOperationException e) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateNowait_UnsupportedOperationException()
            throws Exception {
        if (implementor.getDialect().supportsForUpdate(NOWAIT, false)) {
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
    public void testForUpdateNowaitWithTarget() throws Exception {
        if (!implementor.getDialect().supportsForUpdate(NOWAIT, true)) {
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
    public void testForUpdateNowaitWithTarget_leftOuterJoin() throws Exception {
        if (!implementor.getDialect().supportsForUpdate(NOWAIT, true)
            || !implementor.getDialect().supportsOuterJoinForUpdate()) {
            return;
        }
        jdbcManager.from(Employee.class).leftOuterJoin("department").forUpdateNowait(
            "employeeName").getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateNowaitWithTarget_leftOuterJoin2() throws Exception {
        if (!implementor.getDialect().supportsForUpdate(NOWAIT, true)
            || !implementor.getDialect().supportsOuterJoinForUpdate()) {
            return;
        }
        jdbcManager.from(Employee.class).leftOuterJoin("department").forUpdateNowait(
            "department.location").getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateNowaitWithTarget_innerJoin() throws Exception {
        if (!implementor.getDialect().supportsForUpdate(NOWAIT, true)) {
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
    public void testForUpdateNowaitWithTarget_innerJoin2() throws Exception {
        if (!implementor.getDialect().supportsForUpdate(NOWAIT, true)) {
            return;
        }
        jdbcManager
            .from(Employee.class)
            .join("department", JoinType.INNER)
            .forUpdateNowait("department.location")
            .getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateNowaitWithTarget_paging_UnsupportedOperationException()
            throws Exception {
        if (!implementor.getDialect().supportsForUpdate(NOWAIT, true)) {
            return;
        }
        try {
            jdbcManager
                .from(Employee.class)
                .orderBy("employeeName")
                .offset(5)
                .limit(3)
                .forUpdateNowait("employeeName")
                .getResultList();
            fail();
        } catch (UnsupportedOperationException e) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateNowaitWithTarget_UnsupportedOperationException()
            throws Exception {
        if (implementor.getDialect().supportsForUpdate(NOWAIT, true)) {
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
    public void testForUpdateNowaitWithTargets() throws Exception {
        if (!implementor.getDialect().supportsForUpdate(NOWAIT, true)) {
            return;
        }
        jdbcManager.from(Employee.class).forUpdateNowait(
            "employeeName",
            "salary").getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateNowaitWithTargets_leftOuterJoin() throws Exception {
        if (!implementor.getDialect().supportsForUpdate(NOWAIT, true)
            || !implementor.getDialect().supportsOuterJoinForUpdate()) {
            return;
        }
        jdbcManager.from(Employee.class).leftOuterJoin("department").forUpdateNowait(
            "employeeName",
            "department.location").getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateNowaitWithTargets_innerJoin() throws Exception {
        if (!implementor.getDialect().supportsForUpdate(NOWAIT, true)) {
            return;
        }
        jdbcManager
            .from(Employee.class)
            .join("department", JoinType.INNER)
            .forUpdateNowait("employeeName", "department.location")
            .getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateNowaitWithTargets_pagingOffsetLimit()
            throws Exception {
        if (!implementor.getDialect().supportsForUpdate(NOWAIT, true)) {
            return;
        }
        try {
            jdbcManager
                .from(Employee.class)
                .orderBy("employeeName")
                .offset(5)
                .limit(3)
                .forUpdateNowait("employeeName", "salary")
                .getResultList();
            fail();
        } catch (UnsupportedOperationException e) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateNowaitWithTargets_UnsupportedOperationException()
            throws Exception {
        if (implementor.getDialect().supportsForUpdate(NOWAIT, true)) {
            return;
        }
        try {
            jdbcManager.from(Employee.class).forUpdateNowait(
                "employeeName",
                "salary");
            fail();
        } catch (UnsupportedOperationException e) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateWait() throws Exception {
        if (!implementor.getDialect().supportsForUpdate(WAIT, false)) {
            return;
        }
        jdbcManager.from(Employee.class).forUpdateWait(1).getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateWait_leftOuterJoin() throws Exception {
        if (!implementor.getDialect().supportsForUpdate(WAIT, false)
            || !implementor.getDialect().supportsOuterJoinForUpdate()) {
            return;
        }
        jdbcManager
            .from(Employee.class)
            .leftOuterJoin("department")
            .forUpdateWait(1)
            .getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateWait_innerJoin() throws Exception {
        if (!implementor.getDialect().supportsForUpdate(WAIT, false)) {
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
    public void testForUpdateWait_paging_UnsupportedOperationException()
            throws Exception {
        if (!implementor.getDialect().supportsForUpdate(WAIT, false)) {
            return;
        }
        try {
            jdbcManager
                .from(Employee.class)
                .orderBy("employeeName")
                .offset(5)
                .limit(3)
                .forUpdateWait(1)
                .getResultList();
            fail();
        } catch (UnsupportedOperationException e) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateWait_UnsupportedOperationException()
            throws Exception {
        if (implementor.getDialect().supportsForUpdate(WAIT, false)) {
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
    public void testForUpdateWaitWithTarget() throws Exception {
        if (!implementor.getDialect().supportsForUpdate(WAIT, true)) {
            return;
        }
        jdbcManager
            .from(Employee.class)
            .forUpdateWait(1, "employeeName")
            .getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateWaitWithTarget_leftOuterJoin() throws Exception {
        if (!implementor.getDialect().supportsForUpdate(WAIT, true)) {
            return;
        }
        jdbcManager.from(Employee.class).leftOuterJoin("department").forUpdateWait(
            1,
            "employeeName").getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateWaitWithTarget_leftOuterJoin2() throws Exception {
        if (!implementor.getDialect().supportsForUpdate(WAIT, true)) {
            return;
        }
        jdbcManager.from(Employee.class).leftOuterJoin("department").forUpdateWait(
            1,
            "department.location").getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateWaitWithTarget_innerJoin() throws Exception {
        if (!implementor.getDialect().supportsForUpdate(WAIT, true)) {
            return;
        }
        jdbcManager
            .from(Employee.class)
            .join("department", JoinType.INNER)
            .forUpdateWait(1, "employeeName")
            .getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateWaitWithTarget_innerJoin2() throws Exception {
        if (!implementor.getDialect().supportsForUpdate(WAIT, true)) {
            return;
        }
        jdbcManager
            .from(Employee.class)
            .join("department", JoinType.INNER)
            .forUpdateWait(1, "department.location")
            .getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateWaitWithTarget_paging_UnsupportedOperationException()
            throws Exception {
        if (!implementor.getDialect().supportsForUpdate(WAIT, true)) {
            return;
        }
        try {
            jdbcManager
                .from(Employee.class)
                .orderBy("employeeName")
                .offset(5)
                .limit(3)
                .forUpdateWait(1, "employeeName")
                .getResultList();
            fail();
        } catch (UnsupportedOperationException e) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateWaitWithTarget_UnsupportedOperationException()
            throws Exception {
        if (implementor.getDialect().supportsForUpdate(WAIT, true)) {
            return;
        }
        try {
            jdbcManager.from(Employee.class).forUpdateWait(1, "employeeName");
            fail();
        } catch (UnsupportedOperationException e) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateWaitWithTargets() throws Exception {
        if (!implementor.getDialect().supportsForUpdate(WAIT, true)) {
            return;
        }
        jdbcManager.from(Employee.class).forUpdateWait(
            1,
            "employeeName",
            "salary").getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateWaitWithTargets_leftOuterJoin() throws Exception {
        if (!implementor.getDialect().supportsForUpdate(WAIT, true)) {
            return;
        }
        jdbcManager.from(Employee.class).leftOuterJoin("department").forUpdateWait(
            1,
            "employeeName",
            "department.location").getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateWaitWithTargets_innerJoin() throws Exception {
        if (!implementor.getDialect().supportsForUpdate(WAIT, true)) {
            return;
        }
        jdbcManager
            .from(Employee.class)
            .join("department", JoinType.INNER)
            .forUpdateWait(1, "employeeName", "department.location")
            .getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateWaitWithTargets_paging_UnsupportedOperationException()
            throws Exception {
        if (!implementor.getDialect().supportsForUpdate(WAIT, true)) {
            return;
        }
        try {
            jdbcManager
                .from(Employee.class)
                .orderBy("employeeName")
                .offset(5)
                .limit(3)
                .forUpdateWait(1, "employeeName", "salary")
                .getResultList();
            fail();
        } catch (UnsupportedOperationException e) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateWaitWithTargets_UnsupportedOperationException()
            throws Exception {
        if (implementor.getDialect().supportsForUpdate(WAIT, true)) {
            return;
        }
        try {
            jdbcManager.from(Employee.class).forUpdateWait(
                1,
                "employeeName",
                "salary");
            fail();
        } catch (UnsupportedOperationException e) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testTemporalType() throws Exception {
        Tense tense =
            jdbcManager.from(Tense.class).where("id = ?", 1).getSingleResult();
        assertEquals(tense.sqlDate.getTime(), tense.calDate.getTimeInMillis());
        assertEquals(tense.sqlDate.getTime(), tense.dateDate.getTime());
        assertEquals(tense.sqlTime.getTime(), tense.calTime.getTimeInMillis());
        assertEquals(tense.sqlTime.getTime(), tense.dateTime.getTime());
        assertEquals(tense.sqlTimestamp.getTime(), tense.calTimestamp
            .getTimeInMillis());
        assertEquals(tense.sqlTimestamp.getTime(), tense.dateTimestamp
            .getTime());
    }

    /**
     * 
     * @throws Exception
     */
    public void testTemporalTypeDate_criteria() throws Exception {
        Date date =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .parse("2005-02-14 12:11:10");
        Tense tense =
            jdbcManager
                .from(Tense.class)
                .where("dateDate = ?", date(date))
                .getSingleResult();
        assertNotNull(tense);
        tense =
            jdbcManager
                .from(Tense.class)
                .where("dateTime = ?", time(date))
                .getSingleResult();
        assertNotNull(tense);
        tense =
            jdbcManager.from(Tense.class).where(
                "dateTimestamp = ?",
                timestamp(date)).getSingleResult();
        assertNotNull(tense);
    }

    /**
     * 
     * @throws Exception
     */
    public void testTemporalTypeCalendar_criteria() throws Exception {
        Date date =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .parse("2005-02-14 12:11:10");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Tense tense =
            jdbcManager
                .from(Tense.class)
                .where("calDate = ?", date(calendar))
                .getSingleResult();
        assertNotNull(tense);
        tense =
            jdbcManager
                .from(Tense.class)
                .where("calTime = ?", time(calendar))
                .getSingleResult();
        assertNotNull(tense);
        tense =
            jdbcManager.from(Tense.class).where(
                "calTimestamp = ?",
                timestamp(calendar)).getSingleResult();
        assertNotNull(tense);
    }

    /**
     * 
     * @throws Exception
     */
    public void testTemporalTypeSql_criteria() throws Exception {
        long time =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(
                "2005-02-14 12:11:10").getTime();
        Tense tense =
            jdbcManager.from(Tense.class).where(
                "sqlDate = ?",
                new java.sql.Date(time)).getSingleResult();
        assertNotNull(tense);
        tense =
            jdbcManager.from(Tense.class).where(
                "sqlTime = ?",
                Time.valueOf("12:11:10")).getSingleResult();
        assertNotNull(tense);
        tense =
            jdbcManager.from(Tense.class).where(
                "sqlTimestamp = ?",
                new Timestamp(time)).getSingleResult();
        assertNotNull(tense);
    }

    /**
     * 
     * @throws Exception
     */
    public void testTemporalTypeDate_simpleWhere() throws Exception {
        Date date =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .parse("2005-02-14 12:11:10");
        Tense tense =
            jdbcManager.from(Tense.class).where(
                new SimpleWhere().eq("dateDate", date)).getSingleResult();
        assertNotNull(tense);
        tense =
            jdbcManager.from(Tense.class).where(
                new SimpleWhere().eq("dateTime", date)).getSingleResult();
        assertNotNull(tense);
        tense =
            jdbcManager.from(Tense.class).where(
                new SimpleWhere().eq("dateTimestamp", date)).getSingleResult();
        assertNotNull(tense);
    }

    /**
     * 
     * @throws Exception
     */
    public void testTemporalTypeCalendar_simpleWhere() throws Exception {
        Date date =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .parse("2005-02-14 12:11:10");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Tense tense =
            jdbcManager.from(Tense.class).where(
                new SimpleWhere().eq("calDate", calendar)).getSingleResult();
        assertNotNull(tense);
        tense =
            jdbcManager.from(Tense.class).where(
                new SimpleWhere().eq("calTime", calendar)).getSingleResult();
        assertNotNull(tense);
        tense =
            jdbcManager
                .from(Tense.class)
                .where(new SimpleWhere().eq("calTimestamp", calendar))
                .getSingleResult();
        assertNotNull(tense);
    }

    /**
     * 
     * @throws Exception
     */
    public void testTemporalTypeSql_simpleWhere() throws Exception {
        long time =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(
                "2005-02-14 12:11:10").getTime();
        Tense tense =
            jdbcManager
                .from(Tense.class)
                .where(new SimpleWhere().eq("sqlDate", new java.sql.Date(time)))
                .getSingleResult();
        assertNotNull(tense);
        tense =
            jdbcManager
                .from(Tense.class)
                .where(
                    new SimpleWhere().eq("sqlTime", Time.valueOf("12:11:10")))
                .getSingleResult();
        assertNotNull(tense);
        tense =
            jdbcManager
                .from(Tense.class)
                .where(
                    new SimpleWhere().eq("sqlTimestamp", new Timestamp(time)))
                .getSingleResult();
        assertNotNull(tense);
    }

    /**
     * 
     * @throws Exception
     */
    public void testId() throws Exception {
        Employee employee =
            jdbcManager.from(Employee.class).id(10).getSingleResult();
        assertNotNull(employee);
        assertEquals("TURNER", employee.employeeName);
    }

    /**
     * 
     * @throws Exception
     */
    public void testId_IllegalSize() throws Exception {
        try {
            jdbcManager.from(Employee.class).id(10, 11).getSingleResult();
        } catch (IllegalIdPropertySizeRuntimeException e) {
            System.out.println(e);
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testId_composite() throws Exception {
        CompKeyEmployee employee =
            jdbcManager
                .from(CompKeyEmployee.class)
                .id(10, 10)
                .getSingleResult();
        assertNotNull(employee);
        assertEquals("TURNER", employee.employeeName);
    }

    /**
     * 
     * @throws Exception
     */
    public void testVersion() throws Exception {
        Employee employee =
            jdbcManager
                .from(Employee.class)
                .id(10)
                .version(1)
                .getSingleResult();
        assertNotNull(employee);
        assertEquals("TURNER", employee.employeeName);
    }

    /**
     * 
     * @throws Exception
     */
    public void testVersion_UnsupportedOperationException() throws Exception {
        try {
            jdbcManager.from(Employee.class).version(1).getSingleResult();
            fail();
        } catch (UnsupportedOperationException e) {
        }
    }
}
