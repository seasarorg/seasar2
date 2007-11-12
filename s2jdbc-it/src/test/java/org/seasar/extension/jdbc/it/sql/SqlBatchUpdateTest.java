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

import javax.persistence.EntityExistsException;

import org.junit.runner.RunWith;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.it.entity.Employee;
import org.seasar.framework.unit.Seasar2;
import org.seasar.framework.unit.annotation.Prerequisite;

import static junit.framework.Assert.*;

/**
 * @author taedium
 * 
 */
@RunWith(Seasar2.class)
public class SqlBatchUpdateTest {

    private JdbcManager jdbcManager;

    /**
     * 
     * @throws Exception
     */
    public void testParameter_noneTx() throws Exception {
        String sql =
            "update Employee set salary = salary * 2 where employee_id = 1";
        int[] result =
            jdbcManager.updateBatchBySql(sql).params().params().execute();
        assertEquals(2, result.length);
    }

    /**
     * 
     * @throws Exception
     */
    public void testParameterTx() throws Exception {
        String sql =
            "delete from Employee where department_Id = ? and salary > ?";
        int[] result =
            jdbcManager
                .updateBatchBySql(sql, int.class, BigDecimal.class)
                .params(1, new BigDecimal(3000))
                .params(2, new BigDecimal(2000))
                .execute();
        assertEquals(2, result.length);
        List<Employee> list =
            jdbcManager
                .selectBySql(
                    Employee.class,
                    "select * from Employee where employee_id in (4,8,9,13)")
                .getResultList();
        assertTrue(list.isEmpty());
    }

    /**
     * 
     * @throws Exception
     */
    @Prerequisite("#ENV != 'hsqldb'")
    public void testEntityExistsException_insertTx() throws Exception {
        String sql =
            "insert into Department (department_id, department_no) values(?, ?)";
        try {
            jdbcManager.updateBatchBySql(sql, int.class, int.class).params(
                99,
                10).execute();
            fail();
        } catch (EntityExistsException e) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    @Prerequisite("#ENV != 'hsqldb'")
    public void testEntityExistsException_updateTx() throws Exception {
        String sql =
            "update Department set department_no = ? where department_id = ?";
        try {
            jdbcManager.updateBatchBySql(sql, int.class, int.class).params(
                20,
                1).execute();
            fail();
        } catch (EntityExistsException e) {
        }
    }
}
