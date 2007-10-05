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
import org.seasar.extension.jdbc.it.entity.Address;
import org.seasar.extension.jdbc.it.entity.Employee;
import org.seasar.extension.unit.S2TestCase;

/**
 * @author taedium
 * 
 */
public class OneToOneTest extends S2TestCase {

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
    public void test_fetch_leftOuterJoin_owner_to_inverse() throws Exception {
        List<Employee> list = jdbcManager.from(Employee.class).join("address")
                .getResultList();
        assertEquals(14, list.size());
        for (Employee e : list) {
            assertNotNull(e.address);
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void test_leftOuterJoin_owner_to_inverse() throws Exception {
        List<Employee> list = jdbcManager.from(Employee.class).join("address",
                false).getResultList();
        assertEquals(14, list.size());
        for (Employee e : list) {
            assertNull(e.address);
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void test_fetch_innerJoin_owner_to_inverse() throws Exception {
        List<Employee> list = jdbcManager.from(Employee.class).join("address",
                JoinType.INNER).getResultList();
        assertEquals(14, list.size());
        for (Employee e : list) {
            assertNotNull(e.address);
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void test_innerJoin_owner_to_inverse() throws Exception {
        List<Employee> list = jdbcManager.from(Employee.class).join("address",
                JoinType.INNER, false).getResultList();
        assertEquals(14, list.size());
        for (Employee e : list) {
            assertNull(e.address);
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void test_fetch_leftOuterJoin_inverse_to_owner() throws Exception {
        List<Address> list = jdbcManager.from(Address.class).join("employee")
                .getResultList();
        assertEquals(14, list.size());
        for (Address e : list) {
            assertNotNull(e.employee);
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void test_leftOuterJoin_inverse_to_owner() throws Exception {
        List<Address> list = jdbcManager.from(Address.class).join("employee",
                false).getResultList();
        assertEquals(14, list.size());
        for (Address e : list) {
            assertNull(e.employee);
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void test_fetch_innerJoin_inverse_to_owner() throws Exception {
        List<Address> list = jdbcManager.from(Address.class).join("employee",
                JoinType.INNER).getResultList();
        assertEquals(14, list.size());
        for (Address e : list) {
            assertNotNull(e.employee);
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void test_innerJoin_inverse_to_owner() throws Exception {
        List<Address> list = jdbcManager.from(Address.class).join("employee",
                JoinType.INNER, false).getResultList();
        assertEquals(14, list.size());
        for (Address e : list) {
            assertNull(e.employee);
        }
    }
}
