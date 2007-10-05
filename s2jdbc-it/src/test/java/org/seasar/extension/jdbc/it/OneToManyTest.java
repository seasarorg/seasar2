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
package org.seasar.extension.jdbc.it;

import java.util.List;

import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.JoinType;
import org.seasar.extension.jdbc.it.entity.Department;
import org.seasar.extension.unit.S2TestCase;

/**
 * @author taedium
 * 
 */
public class OneToManyTest extends S2TestCase {

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
    public void test_fetch_leftOuterJoin() throws Exception {
        List<Department> list = jdbcManager.from(Department.class).join(
                "employees").getResultList();
        assertEquals(4, list.size());
        assertNotNull(list.get(0).employees);
        assertNotNull(list.get(1).employees);
        assertNotNull(list.get(2).employees);
        assertNotNull(list.get(3).employees);
    }

    /**
     * 
     * @throws Exception
     */
    public void test_leftOuterJoin() throws Exception {
        List<Department> list = jdbcManager.from(Department.class).join(
                "employees", false).getResultList();
        assertEquals(4, list.size());
        assertNull(list.get(0).employees);
        assertNull(list.get(1).employees);
        assertNull(list.get(2).employees);
        assertNull(list.get(3).employees);
    }

    /**
     * 
     * @throws Exception
     */
    public void test_fetch_innerJoin() throws Exception {
        List<Department> list = jdbcManager.from(Department.class).join(
                "employees", JoinType.INNER).getResultList();
        assertEquals(3, list.size());
        assertNotNull(list.get(0).employees);
        assertNotNull(list.get(1).employees);
        assertNotNull(list.get(2).employees);
    }

    /**
     * 
     * @throws Exception
     */
    public void test_innerJoin() throws Exception {
        List<Department> list = jdbcManager.from(Department.class).join(
                "employees", JoinType.INNER, false).getResultList();
        assertEquals(3, list.size());
        assertNull(list.get(0).employees);
        assertNull(list.get(1).employees);
        assertNull(list.get(2).employees);
    }
}
