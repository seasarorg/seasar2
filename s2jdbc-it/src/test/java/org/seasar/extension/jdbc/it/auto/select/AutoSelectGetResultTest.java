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

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

import org.junit.runner.RunWith;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.it.entity.Department;
import org.seasar.extension.jdbc.it.entity.Employee;
import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.unit.Seasar2;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
@RunWith(Seasar2.class)
public class AutoSelectGetResultTest {

    private JdbcManager jdbcManager;

    /**
     * 
     * @throws Exception
     */
    public void testGetSingleResult() throws Exception {
        Employee employee =
            jdbcManager.from(Employee.class).where(
                new SimpleWhere().eq("employeeId", 1)).getSingleResult();
        assertNotNull(employee);
    }

    /**
     * 
     * @throws Exception
     */
    public void testGetSingleResult_NonUniqueResultException() throws Exception {
        try {
            jdbcManager.from(Employee.class).where(
                new SimpleWhere().eq("departmentId", 1)).getSingleResult();
            fail();
        } catch (NonUniqueResultException e) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testGetSingleResult_NoResultException() throws Exception {
        try {
            jdbcManager
                .from(Employee.class)
                .where(new SimpleWhere().eq("employeeId", 100))
                .disallowNoResult()
                .getSingleResult();
            fail();
        } catch (NoResultException e) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testGetSingleResult_null() throws Exception {
        Employee employee =
            jdbcManager.from(Employee.class).where(
                new SimpleWhere().eq("employeeId", 100)).getSingleResult();
        assertNull(employee);
    }

    /**
     * 
     * @throws Exception
     */
    public void testGetSingleResult_oneToMany() throws Exception {
        Department department =
            jdbcManager
                .from(Department.class)
                .leftOuterJoin("employees")
                .where(new SimpleWhere().eq("departmentId", 1))
                .getSingleResult();
        assertNotNull(department);
    }

}
