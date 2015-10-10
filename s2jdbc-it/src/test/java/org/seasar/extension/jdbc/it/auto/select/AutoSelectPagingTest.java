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

import java.util.List;

import org.junit.runner.RunWith;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.it.entity.Employee;
import org.seasar.framework.unit.Seasar2;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
@RunWith(Seasar2.class)
public class AutoSelectPagingTest {

    private JdbcManager jdbcManager;

    /**
     * 
     * @throws Exception
     */
    public void testPaging() throws Exception {
        List<Employee> list =
            jdbcManager
                .from(Employee.class)
                .orderBy("employeeId")
                .getResultList();
        assertEquals(14, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void testPaging_limitOnly() throws Exception {
        List<Employee> list =
            jdbcManager
                .from(Employee.class)
                .limit(3)
                .orderBy("employeeId")
                .getResultList();
        assertEquals(3, list.size());
        assertEquals(1, list.get(0).employeeId);
        assertEquals(3, list.get(2).employeeId);
    }

    /**
     * 
     * @throws Exception
     */
    public void testPaging_limitOnly_leftOuterJoin() throws Exception {
        List<Employee> list =
            jdbcManager
                .from(Employee.class)
                .leftOuterJoin("manager")
                .limit(3)
                .orderBy("employeeId")
                .getResultList();
        assertEquals(3, list.size());
        assertEquals(1, list.get(0).employeeId);
        assertEquals(3, list.get(2).employeeId);
    }

    /**
     * 
     * @throws Exception
     */
    public void testPaging_offset_limit() throws Exception {
        List<Employee> list =
            jdbcManager.from(Employee.class).offset(3).limit(5).orderBy(
                "employeeId").getResultList();
        assertEquals(5, list.size());
        assertEquals(4, list.get(0).employeeId);
        assertEquals(8, list.get(4).employeeId);
    }

    /**
     * 
     * @throws Exception
     */
    public void testPaging_offset_limit_leftOuterJoin() throws Exception {
        List<Employee> list =
            jdbcManager
                .from(Employee.class)
                .leftOuterJoin("manager")
                .offset(3)
                .limit(5)
                .orderBy("employeeId")
                .getResultList();
        assertEquals(5, list.size());
        assertEquals(4, list.get(0).employeeId);
        assertEquals(8, list.get(4).employeeId);
    }

    /**
     * 
     * @throws Exception
     */
    public void testPaging_offset_limitZero() throws Exception {
        List<Employee> list =
            jdbcManager.from(Employee.class).offset(3).limit(0).orderBy(
                "employeeId").getResultList();
        assertEquals(11, list.size());
        assertEquals(4, list.get(0).employeeId);
        assertEquals(14, list.get(10).employeeId);
    }

    /**
     * 
     * @throws Exception
     */
    public void testPaging_offset_limitZero_leftOuterJoin() throws Exception {
        List<Employee> list =
            jdbcManager
                .from(Employee.class)
                .leftOuterJoin("manager")
                .offset(3)
                .limit(0)
                .orderBy("employeeId")
                .getResultList();
        assertEquals(11, list.size());
        assertEquals(4, list.get(0).employeeId);
        assertEquals(14, list.get(10).employeeId);
    }

    /**
     * 
     * @throws Exception
     */
    public void testPaging_offsetOnly() throws Exception {
        List<Employee> list =
            jdbcManager
                .from(Employee.class)
                .offset(3)
                .orderBy("employeeId")
                .getResultList();
        assertEquals(11, list.size());
        assertEquals(4, list.get(0).employeeId);
        assertEquals(14, list.get(10).employeeId);
    }

    /**
     * 
     * @throws Exception
     */
    public void testPaging_offsetOnly_leftOuterJoin() throws Exception {
        List<Employee> list =
            jdbcManager
                .from(Employee.class)
                .leftOuterJoin("manager")
                .offset(3)
                .orderBy("employeeId")
                .getResultList();
        assertEquals(11, list.size());
        assertEquals(4, list.get(0).employeeId);
        assertEquals(14, list.get(10).employeeId);
    }

    /**
     * 
     * @throws Exception
     */
    public void testPaging_offsetZero_limit() throws Exception {
        List<Employee> list =
            jdbcManager.from(Employee.class).offset(0).limit(3).orderBy(
                "employeeId").getResultList();
        assertEquals(3, list.size());
        assertEquals(1, list.get(0).employeeId);
        assertEquals(3, list.get(2).employeeId);
    }

    /**
     * 
     * @throws Exception
     */
    public void testPaging_offsetZero_limit_leftOuterJoin() throws Exception {
        List<Employee> list =
            jdbcManager
                .from(Employee.class)
                .leftOuterJoin("manager")
                .offset(0)
                .limit(3)
                .orderBy("employeeId")
                .getResultList();
        assertEquals(3, list.size());
        assertEquals(1, list.get(0).employeeId);
        assertEquals(3, list.get(2).employeeId);
    }

    /**
     * 
     * @throws Exception
     */
    public void testPaging_offsetZero_limitZero() throws Exception {
        List<Employee> list =
            jdbcManager.from(Employee.class).offset(0).limit(0).orderBy(
                "employeeId").getResultList();
        assertEquals(14, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void testPaging_offsetZero_limitZero_leftOuterJoin()
            throws Exception {
        List<Employee> list =
            jdbcManager
                .from(Employee.class)
                .leftOuterJoin("manager")
                .offset(0)
                .limit(0)
                .orderBy("employeeId")
                .getResultList();
        assertEquals(14, list.size());
    }

}
