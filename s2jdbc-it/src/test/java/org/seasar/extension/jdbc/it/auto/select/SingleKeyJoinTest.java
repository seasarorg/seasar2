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
import java.util.List;

import org.junit.runner.RunWith;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.exception.PropertyNotFoundRuntimeException;
import org.seasar.extension.jdbc.it.condition.DepartmentCondition;
import org.seasar.extension.jdbc.it.condition.EmployeeCondition;
import org.seasar.extension.jdbc.it.entity.Department;
import org.seasar.extension.jdbc.it.entity.Employee;
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
public class SingleKeyJoinTest {

    private JdbcManager jdbcManager;

    /**
     * 
     * @throws Exception
     */
    public void testJoin_nest() throws Exception {
        List<Department> list =
            jdbcManager
                .from(Department.class)
                .leftOuterJoin("employees")
                .leftOuterJoin("employees.address")
                .orderBy("departmentId")
                .getResultList();
        assertEquals(4, list.size());
        assertNotNull(list.get(0).employees);
        assertNotNull(list.get(0).employees.get(0).address);
    }

    /**
     * 
     * @throws Exception
     */
    public void testJoin_nest_simpleWhere() throws Exception {
        List<Department> list =
            jdbcManager
                .from(Department.class)
                .leftOuterJoin("employees")
                .leftOuterJoin("employees.address")
                .where(new SimpleWhere().eq("employees.addressId", 3))
                .getResultList();
        assertEquals(1, list.size());
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
                .innerJoin("manager")
                .leftOuterJoin("department")
                .leftOuterJoin("address")
                .getResultList();
        assertEquals(13, list.size());
        assertNotNull(list.get(0).department);
        assertNotNull(list.get(0).manager);
        assertNotNull(list.get(0).address);
    }

    /**
     * 
     * @throws Exception
     */
    public void testJoin_star_simpleWhere() throws Exception {
        List<Employee> list =
            jdbcManager
                .from(Employee.class)
                .innerJoin("manager")
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
    public void testJoin_star_condition() throws Exception {
        List<Employee> list =
            jdbcManager
                .from(Employee.class)
                .innerJoin("manager")
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
    public void testJoin_star_names() throws Exception {
        List<Employee> list =
            jdbcManager
                .from(Employee.class)
                .innerJoin(manager())
                .leftOuterJoin(department())
                .leftOuterJoin(address())
                .where(
                    ge(salary(), new BigDecimal("2000")),
                    eq(department().departmentName(), "RESEARCH"),
                    starts(address().street(), "STREET"))
                .getResultList();
        assertEquals(3, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void testJoin_illegalPropertyName() throws Exception {
        try {
            jdbcManager
                .from(Department.class)
                .leftOuterJoin("illegal")
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
    public void testJoin_illegalPropertyName2() throws Exception {
        try {
            jdbcManager
                .from(Department.class)
                .leftOuterJoin("employees")
                .leftOuterJoin("employees.illegal")
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
    public void testJoin_condition() throws Exception {
        List<Employee> list =
            jdbcManager.from(Employee.class).innerJoin(
                "department",
                "managerId = ?",
                9).where("salary > ?", new BigDecimal(2000)).getResultList();
        assertEquals(3, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void testJoin_names() throws Exception {
        List<Employee> list =
            jdbcManager
                .from(Employee.class)
                .innerJoin(department(), eq(managerId(), 9))
                .where(gt(salary(), new BigDecimal(2000)))
                .getResultList();
        assertEquals(3, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void testJoin_condition_where() throws Exception {
        List<Employee> list =
            jdbcManager
                .from(Employee.class)
                .innerJoin("department", new SimpleWhere().eq("managerId", 9))
                .where(new SimpleWhere().gt("salary", new BigDecimal(2000)))
                .getResultList();
        assertEquals(3, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void testJoin_condition_where_names() throws Exception {
        List<Employee> list =
            jdbcManager
                .from(Employee.class)
                .innerJoin(department(), eq(managerId(), 9))
                .where(gt(salary(), new BigDecimal(2000)))
                .getResultList();
        assertEquals(3, list.size());
    }

}
