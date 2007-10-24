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

import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.JoinType;
import org.seasar.extension.jdbc.it.entity.Employee;
import org.seasar.extension.unit.S2TestCase;

/**
 * @author taedium
 * 
 */
public class ManyToOneTest extends S2TestCase {

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
    public void testLeftOuterJoin_fetch() throws Exception {
        List<Employee> list =
            jdbcManager.from(Employee.class).join("department").getResultList();
        assertEquals(14, list.size());
        for (Employee e : list) {
            assertNotNull(e);
            assertNotNull(e.department);
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testLeftOuterJoin() throws Exception {
        List<Employee> list =
            jdbcManager
                .from(Employee.class)
                .join("department", false)
                .getResultList();
        assertEquals(14, list.size());
        for (Employee e : list) {
            assertNotNull(e);
            assertNull(e.department);
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testInnerJoin_fetch() throws Exception {
        List<Employee> list =
            jdbcManager
                .from(Employee.class)
                .join("department", JoinType.INNER)
                .getResultList();
        assertEquals(14, list.size());
        for (Employee e : list) {
            assertNotNull(e);
            assertNotNull(e.department);
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testInnerJoin() throws Exception {
        List<Employee> list =
            jdbcManager.from(Employee.class).join(
                "department",
                JoinType.INNER,
                false).getResultList();
        assertEquals(14, list.size());
        for (Employee e : list) {
            assertNotNull(e);
            assertNull(e.department);
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testInnerJoin_fetch_self() throws Exception {
        List<Employee> list =
            jdbcManager
                .from(Employee.class)
                .join("manager", JoinType.INNER)
                .getResultList();
        assertEquals(13, list.size());
        for (Employee e : list) {
            assertNotNull(e);
            assertNotNull(e.manager);
        }
    }

}
