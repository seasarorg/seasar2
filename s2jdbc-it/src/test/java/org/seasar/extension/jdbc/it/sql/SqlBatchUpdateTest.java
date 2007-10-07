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
package org.seasar.extension.jdbc.it.sql;

import java.math.BigDecimal;
import java.util.List;

import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.it.entity.Employee;
import org.seasar.extension.unit.S2TestCase;

/**
 * @author taedium
 * 
 */
public class SqlBatchUpdateTest extends S2TestCase {

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
    public void test_no_parameterTx() throws Exception {
        String sql = "update Employee set salary = salary * 2 where employee_id = 1";
        int[] result = jdbcManager.updateBatchBySql(sql).params().params()
                .executeBatch();
        assertEquals(2, result.length);
        BigDecimal salary = jdbcManager.selectBySql(BigDecimal.class,
                "select salary from Employee where employee_id = 1")
                .getSingleResult();
        assertEquals(new BigDecimal(3200), salary);
    }

    /**
     * 
     * @throws Exception
     */
    public void test_parameterTx() throws Exception {
        String sql = "delete from Employee where department_Id = ? and salary > ?";
        int[] result = jdbcManager.updateBatchBySql(sql, int.class,
                BigDecimal.class).params(1, new BigDecimal(3000)).params(2,
                new BigDecimal(2000)).executeBatch();
        assertEquals(2, result.length);
        List<Employee> list = jdbcManager.selectBySql(Employee.class,
                "select * from Employee where employee_id in (4,8,9,13)")
                .getResultList();
        assertTrue(list.isEmpty());
    }
}
