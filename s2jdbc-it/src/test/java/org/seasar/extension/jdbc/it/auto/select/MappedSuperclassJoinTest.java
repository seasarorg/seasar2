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
import org.seasar.extension.jdbc.it.entity.ConcreteDepartment;
import org.seasar.extension.jdbc.it.entity.ConcreteEmployee;
import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.unit.Seasar2;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
@RunWith(Seasar2.class)
public class MappedSuperclassJoinTest {

    private JdbcManager jdbcManager;

    /**
     * 
     * @throws Exception
     */
    public void testJoin_nest() throws Exception {
        List<ConcreteDepartment> list =
            jdbcManager.from(ConcreteDepartment.class).leftOuterJoin(
                "employees").leftOuterJoin("employees.address").orderBy(
                "departmentId").getResultList();
        assertEquals(4, list.size());
        assertNotNull(list.get(0).employees);
        assertNotNull(list.get(0).employees.get(0).address);
    }

    /**
     * 
     * @throws Exception
     */
    public void testJoin_star() throws Exception {
        List<ConcreteEmployee> list =
            jdbcManager
                .from(ConcreteEmployee.class)
                .innerJoin("manager")
                .leftOuterJoin("department")
                .leftOuterJoin("address")
                .orderBy("departmentId")
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
    public void testJoin_condition() throws Exception {
        List<ConcreteEmployee> list =
            jdbcManager.from(ConcreteEmployee.class).innerJoin(
                "department",
                "managerId = ?",
                9).where("salary > ?", new BigDecimal(2000)).getResultList();
        assertEquals(3, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void testJoin_condition_where() throws Exception {
        List<ConcreteEmployee> list =
            jdbcManager
                .from(ConcreteEmployee.class)
                .innerJoin("department", new SimpleWhere().eq("managerId", 9))
                .where(new SimpleWhere().gt("salary", new BigDecimal(2000)))
                .getResultList();
        assertEquals(3, list.size());
    }
}
