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

import org.junit.runner.RunWith;
import org.seasar.extension.jdbc.IterationCallback;
import org.seasar.extension.jdbc.IterationContext;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.it.entity.Department;
import org.seasar.extension.jdbc.it.entity.Employee;
import org.seasar.extension.jdbc.it.entity.NoId;
import org.seasar.framework.unit.Seasar2;

import static junit.framework.Assert.*;

/**
 * @author taedium
 * 
 */
@RunWith(Seasar2.class)
public class AutoSelectIterationCallbackTest {

    private JdbcManager jdbcManager;

    private IterationCallback<Employee, BigDecimal> salarySumCallback =
        new IterationCallback<Employee, BigDecimal>() {

            BigDecimal temp = BigDecimal.ZERO;

            public BigDecimal iterate(Employee entity, IterationContext context) {
                if (entity.salary != null) {
                    temp = temp.add(entity.salary);
                }
                return temp;
            }
        };

    /**
     * 
     * @throws Exception
     */
    public void testSingleEntity() throws Exception {
        BigDecimal sum =
            jdbcManager.from(Employee.class).iterate(salarySumCallback);
        assertTrue(new BigDecimal(29025).compareTo(sum) == 0);
    }

    /**
     * 
     * @throws Exception
     */
    public void testSingleEntity_limitOnly() throws Exception {
        BigDecimal sum =
            jdbcManager
                .from(Employee.class)
                .limit(3)
                .orderBy("employeeId")
                .iterate(salarySumCallback);
        assertTrue(new BigDecimal(3650).compareTo(sum) == 0);
    }

    /**
     * 
     * @throws Exception
     */
    public void testSingleEntity_offset_limit() throws Exception {
        BigDecimal sum =
            jdbcManager.from(Employee.class).offset(3).limit(5).orderBy(
                "employeeId").iterate(salarySumCallback);
        assertTrue(new BigDecimal(12525).compareTo(sum) == 0);
    }

    /**
     * 
     * @throws Exception
     */
    public void testSingleEntity_offset_limitZero() throws Exception {
        BigDecimal sum =
            jdbcManager.from(Employee.class).offset(3).limit(0).orderBy(
                "employeeId").iterate(salarySumCallback);
        assertTrue(new BigDecimal(25375).compareTo(sum) == 0);
    }

    /**
     * 
     * @throws Exception
     */
    public void testSingleEntity_offsetOnly() throws Exception {
        BigDecimal sum =
            jdbcManager
                .from(Employee.class)
                .offset(3)
                .orderBy("employeeId")
                .iterate(salarySumCallback);
        assertTrue(new BigDecimal(25375).compareTo(sum) == 0);
    }

    /**
     * 
     * @throws Exception
     */
    public void testSingleEntity_offsetZero_limit() throws Exception {
        BigDecimal sum =
            jdbcManager.from(Employee.class).offset(0).limit(3).orderBy(
                "employeeId").iterate(salarySumCallback);
        assertTrue(new BigDecimal(3650).compareTo(sum) == 0);
    }

    /**
     * 
     * @throws Exception
     */
    public void testSingleEntity_offsetZero_limitZero() throws Exception {
        BigDecimal sum =
            jdbcManager.from(Employee.class).offset(0).limit(0).orderBy(
                "employeeId").iterate(salarySumCallback);
        assertTrue(new BigDecimal(29025).compareTo(sum) == 0);
    }

    /**
     * 
     * @throws Exception
     */
    public void testNoId() throws Exception {
        int result =
            jdbcManager.from(NoId.class).iterate(
                new IterationCallback<NoId, Integer>() {

                    private int count;

                    public Integer iterate(NoId entity, IterationContext context) {
                        return ++count;
                    }
                });
        assertEquals(2, result);
    }

    /**
     * 
     * @throws Exception
     */
    public void testManyToOne() throws Exception {
        BigDecimal sum =
            jdbcManager
                .from(Employee.class)
                .leftOuterJoin("department")
                .iterate(new IterationCallback<Employee, BigDecimal>() {

                    BigDecimal temp = BigDecimal.ZERO;

                    public BigDecimal iterate(Employee entity,
                            IterationContext context) {
                        if ("SALES".equals(entity.department.departmentName)) {
                            if (entity.salary != null) {
                                temp = temp.add(entity.salary);
                            }
                        }
                        return temp;
                    }
                });
        assertTrue(new BigDecimal(9400).compareTo(sum) == 0);
    }

    /**
     * 
     * @throws Exception
     */
    public void testOneToMany() throws Exception {
        BigDecimal sum =
            jdbcManager
                .from(Department.class)
                .leftOuterJoin("employees")
                .orderBy("departmentId")
                .iterate(new IterationCallback<Department, BigDecimal>() {

                    BigDecimal temp = BigDecimal.ZERO;

                    public BigDecimal iterate(Department entity,
                            IterationContext context) {
                        for (Employee e : entity.employees) {
                            if (e.salary != null) {
                                temp = temp.add(e.salary);
                            }
                        }
                        return temp;
                    }
                });
        assertTrue(new BigDecimal(29025).compareTo(sum) == 0);
    }

    /**
     * 
     * @throws Exception
     */
    public void testManyToOne_innerJoin_emptyResult() throws Exception {
        jdbcManager.updateBySql("DELETE FROM EMPLOYEE").execute();
        assertEquals(0, jdbcManager.from(Employee.class).getCount());

        Integer count =
            jdbcManager.from(Employee.class).innerJoin("department").iterate(
                new IterationCallback<Employee, Integer>() {

                    int i;

                    public Integer iterate(Employee entity,
                            IterationContext context) {
                        i++;
                        return i;
                    }
                });
        assertNull(count);
    }

    /**
     * 
     * @throws Exception
     */
    public void testOneToMany_innerJoin_emptyResult() throws Exception {
        jdbcManager.updateBySql("DELETE FROM EMPLOYEE").execute();
        jdbcManager.updateBySql("DELETE FROM DEPARTMENT").execute();
        assertEquals(0, jdbcManager.from(Department.class).getCount());

        Integer count =
            jdbcManager.from(Department.class).innerJoin("employees").iterate(
                new IterationCallback<Department, Integer>() {

                    int i;

                    public Integer iterate(Department entity,
                            IterationContext context) {
                        i++;
                        return i;
                    }
                });
        assertNull(count);
    }
}
