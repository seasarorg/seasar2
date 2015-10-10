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

import java.math.BigDecimal;
import java.util.Map;

import org.junit.runner.RunWith;
import org.seasar.extension.jdbc.IterationCallback;
import org.seasar.extension.jdbc.IterationContext;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.it.entity.Employee;
import org.seasar.framework.unit.Seasar2;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
@RunWith(Seasar2.class)
public class SqlSelectIterationCallbackTest {

    private static final String SQL =
        "SELECT * FROM EMPLOYEE ORDER BY EMPLOYEE_ID";

    private static final String SQL2 =
        "SELECT SALARY, EMPLOYEE_ID FROM EMPLOYEE ORDER BY EMPLOYEE_ID";

    private JdbcManager jdbcManager;

    private IterationCallback<Employee, BigDecimal> beanSalarySumCallBack =
        new IterationCallback<Employee, BigDecimal>() {

            BigDecimal temp = BigDecimal.ZERO;

            public BigDecimal iterate(Employee entity, IterationContext context) {
                if (entity.salary != null) {
                    temp = temp.add(entity.salary);
                }
                return temp;
            }
        };

    @SuppressWarnings("unchecked")
    private IterationCallback<Map, BigDecimal> mapSalarySumCallBack =
        new IterationCallback<Map, BigDecimal>() {

            BigDecimal temp = BigDecimal.ZERO;

            public BigDecimal iterate(Map entity, IterationContext context) {
                BigDecimal salary = (BigDecimal) entity.get("salary");
                if (salary != null) {
                    temp = temp.add(salary);
                }
                return temp;
            }
        };

    private IterationCallback<BigDecimal, BigDecimal> objectSalarySumCallBack =
        new IterationCallback<BigDecimal, BigDecimal>() {

            BigDecimal temp = BigDecimal.ZERO;

            public BigDecimal iterate(BigDecimal salary,
                    IterationContext context) {
                if (salary != null) {
                    temp = temp.add(salary);
                }
                return temp;
            }
        };

    /**
     * 
     * @throws Exception
     */
    public void testBean() throws Exception {
        BigDecimal sum =
            jdbcManager.selectBySql(Employee.class, SQL).iterate(
                beanSalarySumCallBack);
        assertTrue(new BigDecimal(29025).compareTo(sum) == 0);
    }

    /**
     * 
     * @throws Exception
     */
    public void testBean_limitOnly() throws Exception {
        BigDecimal sum =
            jdbcManager.selectBySql(Employee.class, SQL).limit(3).iterate(
                beanSalarySumCallBack);
        assertTrue(new BigDecimal(3650).compareTo(sum) == 0);
    }

    /**
     * 
     * @throws Exception
     */
    public void testBean_offset_limit() throws Exception {
        BigDecimal sum =
            jdbcManager
                .selectBySql(Employee.class, SQL)
                .offset(3)
                .limit(5)
                .iterate(beanSalarySumCallBack);
        assertTrue(new BigDecimal(12525).compareTo(sum) == 0);
    }

    /**
     * 
     * @throws Exception
     */
    public void testBean_offsetOnly() throws Exception {
        BigDecimal sum =
            jdbcManager.selectBySql(Employee.class, SQL).offset(3).iterate(
                beanSalarySumCallBack);
        assertTrue(new BigDecimal(25375).compareTo(sum) == 0);
    }

    /**
     * 
     * @throws Exception
     */
    public void testMap() throws Exception {
        BigDecimal sum =
            jdbcManager.selectBySql(Map.class, SQL).iterate(
                mapSalarySumCallBack);
        assertTrue(new BigDecimal(29025).compareTo(sum) == 0);
    }

    /**
     * 
     * @throws Exception
     */
    public void testMap_limitOnly() throws Exception {
        BigDecimal sum =
            jdbcManager.selectBySql(Map.class, SQL).limit(3).iterate(
                mapSalarySumCallBack);
        assertTrue(new BigDecimal(3650).compareTo(sum) == 0);
    }

    /**
     * 
     * @throws Exception
     */
    public void testMap_offset_limit() throws Exception {
        BigDecimal sum =
            jdbcManager.selectBySql(Map.class, SQL).offset(3).limit(5).iterate(
                mapSalarySumCallBack);
        assertTrue(new BigDecimal(12525).compareTo(sum) == 0);
    }

    /**
     * 
     * @throws Exception
     */
    public void testMap_offsetOnly() throws Exception {
        BigDecimal sum =
            jdbcManager.selectBySql(Map.class, SQL).offset(3).iterate(
                mapSalarySumCallBack);
        assertTrue(new BigDecimal(25375).compareTo(sum) == 0);
    }

    /**
     * 
     * @throws Exception
     */
    public void testObject() throws Exception {
        BigDecimal sum =
            jdbcManager.selectBySql(BigDecimal.class, SQL2).iterate(
                objectSalarySumCallBack);
        assertTrue(new BigDecimal(29025).compareTo(sum) == 0);
    }

    /**
     * 
     * @throws Exception
     */
    public void testObject_limitOnly() throws Exception {
        BigDecimal sum =
            jdbcManager.selectBySql(BigDecimal.class, SQL2).limit(3).iterate(
                objectSalarySumCallBack);
        assertTrue(new BigDecimal(3650).compareTo(sum) == 0);
    }

    /**
     * 
     * @throws Exception
     */
    public void testObject_offset_limit() throws Exception {
        BigDecimal sum =
            jdbcManager
                .selectBySql(BigDecimal.class, SQL2)
                .offset(3)
                .limit(5)
                .iterate(objectSalarySumCallBack);
        assertTrue(new BigDecimal(12525).compareTo(sum) == 0);
    }

    /**
     * 
     * @throws Exception
     */
    public void testObject_offsetOnly() throws Exception {
        BigDecimal sum =
            jdbcManager.selectBySql(BigDecimal.class, SQL2).offset(3).iterate(
                objectSalarySumCallBack);
        assertTrue(new BigDecimal(25375).compareTo(sum) == 0);
    }
}
