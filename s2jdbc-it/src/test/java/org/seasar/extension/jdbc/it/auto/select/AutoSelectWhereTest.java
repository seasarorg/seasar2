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
package org.seasar.extension.jdbc.it.auto.select;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.exception.BaseJoinNotFoundRuntimeException;
import org.seasar.extension.jdbc.exception.IllegalIdPropertySizeRuntimeException;
import org.seasar.extension.jdbc.exception.PropertyNotFoundRuntimeException;
import org.seasar.extension.jdbc.it.condition.EmployeeCondition;
import org.seasar.extension.jdbc.it.entity.CompKeyEmployee;
import org.seasar.extension.jdbc.it.entity.Employee;
import org.seasar.extension.jdbc.where.ComplexWhere;
import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.unit.Seasar2;

import static org.junit.Assert.*;
import static org.seasar.extension.jdbc.it.name.EmployeeNames.*;
import static org.seasar.extension.jdbc.operation.Operations.*;

/**
 * @author taedium
 * 
 */
@RunWith(Seasar2.class)
public class AutoSelectWhereTest {

    private JdbcManager jdbcManager;

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
    public void testWhere_contains_names() throws Exception {
        List<Employee> list =
            jdbcManager.from(Employee.class).where(
                contains(employeeName(), "LL")).getResultList();
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
    public void testWhere_ends_names() throws Exception {
        List<Employee> list =
            jdbcManager
                .from(Employee.class)
                .where(ends(employeeName(), "S"))
                .getResultList();
        assertEquals(3, list.size());
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
    public void testWhere_eq_names() throws Exception {
        List<Employee> list =
            jdbcManager
                .from(Employee.class)
                .where(eq(employeeName(), "SMITH"))
                .orderBy(asc(employeeId()))
                .getResultList();
        assertEquals(1, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void testWhere_eq_excludesWhitespace() throws Exception {
        List<Employee> list =
            jdbcManager
                .from(Employee.class)
                .where(
                    new SimpleWhere().excludesWhitespace().eq(
                        "employeeName",
                        "  "))
                .getResultList();
        assertEquals(14, list.size());
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
    public void testWhere_ge_le_names() throws Exception {
        List<Employee> list =
            jdbcManager
                .from(Employee.class)
                .where(
                    and(ge(salary(), new BigDecimal("1100")), le(
                        salary(),
                        new BigDecimal("2000"))))
                .orderBy(asc(employeeId()))
                .getResultList();
        assertEquals(6, list.size());
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
    public void testWhere_gt_lt_names() throws Exception {
        List<Employee> list =
            jdbcManager.from(Employee.class).where(
                and(gt(salary(), new BigDecimal("1100")), lt(
                    salary(),
                    new BigDecimal("2000")))).getResultList();
        assertEquals(5, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void testWhere_in_array() throws Exception {
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
    public void testWhere_in_list() throws Exception {
        List<Employee> list =
            jdbcManager.from(Employee.class).where(
                new SimpleWhere().in("employeeNo", Arrays.asList(
                    7654,
                    7900,
                    7934))).getResultList();
        assertEquals(3, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void testWhere_in_condition_array() throws Exception {
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
    public void testWhere_in_condition_list() throws Exception {
        List<Employee> list =
            jdbcManager.from(Employee.class).where(
                new EmployeeCondition().employeeNo.in(Arrays.asList(
                    7654,
                    7900,
                    7934))).getResultList();
        assertEquals(3, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void testWhere_in_names_array() throws Exception {
        List<Employee> list =
            jdbcManager.from(Employee.class).where(
                in(employeeNo(), 7654, 7900, 7934)).getResultList();
        assertEquals(3, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void testWhere_in_names_list() throws Exception {
        List<Employee> list =
            jdbcManager
                .from(Employee.class)
                .where(in(employeeNo(), Arrays.asList(7654, 7900, 7934)))
                .getResultList();
        assertEquals(3, list.size());
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
    public void testWhere_isNotNull_names() throws Exception {
        List<Employee> list =
            jdbcManager
                .from(Employee.class)
                .where(isNotNull(managerId()))
                .orderBy(asc(employeeId()))
                .getResultList();
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
    public void testWhere_isNull_names() throws Exception {
        List<Employee> list =
            jdbcManager
                .from(Employee.class)
                .where(isNull(managerId()))
                .getResultList();
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
    public void testWhere_like_names() throws Exception {
        List<Employee> list =
            jdbcManager
                .from(Employee.class)
                .where(like(employeeName(), "S%"))
                .getResultList();
        assertEquals(2, list.size());
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
            jdbcManager.from(Employee.class).leftOuterJoin("department").where(
                m).getResultList();
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
            jdbcManager.from(Employee.class).leftOuterJoin("department").where(
                m).getResultList();
            fail();
        } catch (BaseJoinNotFoundRuntimeException e) {
            System.out.println(e.getMessage());
        }
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
    public void testWhere_ne_names() throws Exception {
        List<Employee> list =
            jdbcManager
                .from(Employee.class)
                .where(ne(employeeName(), "SMITH"))
                .orderBy(asc(employeeId()))
                .getResultList();
        assertEquals(13, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void testWhere_notIn_array() throws Exception {
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
    public void testWhere_notIn_list() throws Exception {
        List<Employee> list =
            jdbcManager.from(Employee.class).where(
                new SimpleWhere().notIn("employeeNo", Arrays.asList(
                    7654,
                    7900,
                    7934))).getResultList();
        assertEquals(11, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void testWhere_notIn_condition_array() throws Exception {
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
    public void testWhere_notIn_condition_list() throws Exception {
        List<Employee> list =
            jdbcManager.from(Employee.class).where(
                new EmployeeCondition().employeeNo.notIn(Arrays.asList(
                    7654,
                    7900,
                    7934))).getResultList();
        assertEquals(11, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void testWhere_notIn_names_array() throws Exception {
        List<Employee> list =
            jdbcManager.from(Employee.class).where(
                notIn(employeeNo(), 7654, 7900, 7934)).getResultList();
        assertEquals(11, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void testWhere_notIn_names_list() throws Exception {
        List<Employee> list =
            jdbcManager
                .from(Employee.class)
                .where(notIn(employeeNo(), Arrays.asList(7654, 7900, 7934)))
                .getResultList();
        assertEquals(11, list.size());
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
    public void testWhere_or_excludesWhitespace() throws Exception {
        List<Employee> list =
            jdbcManager
                .from(Employee.class)
                .where(
                    new ComplexWhere().excludesWhitespace().eq(
                        "employeeName",
                        "").or().ge("salary", 3000).eq("employeeName", ""))
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
    public void testWhere_starts_names() throws Exception {
        List<Employee> list =
            jdbcManager
                .from(Employee.class)
                .where(starts(employeeName(), "S"))
                .getResultList();
        assertEquals(2, list.size());
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
    @Test(expected = IllegalIdPropertySizeRuntimeException.class)
    public void testId_IllegalSize() throws Exception {
        jdbcManager.from(Employee.class).id(10, 11).getSingleResult();
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
