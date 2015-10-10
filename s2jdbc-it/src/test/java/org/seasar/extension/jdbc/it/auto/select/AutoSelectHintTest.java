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
import org.seasar.extension.jdbc.it.entity.Department;
import org.seasar.extension.jdbc.it.entity.Employee;
import org.seasar.framework.unit.Seasar2;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
@RunWith(Seasar2.class)
public class AutoSelectHintTest {

    private JdbcManager jdbcManager;

    /**
     * 
     * @throws Exception
     */
    public void testHint() throws Exception {
        List<Employee> list =
            jdbcManager
                .from(Employee.class)
                .where("employeeName like 'S%'")
                .orderBy("employeeName")
                .hint("index (Employee IX_EMPLOYEE_1)")
                .getResultList();
        assertEquals(2, list.size());
        assertEquals("SCOTT", list.get(0).employeeName);
        assertEquals("SMITH", list.get(1).employeeName);
    }

    /**
     * 
     * @throws Exception
     */
    public void testHint_withJoin() throws Exception {
        List<Department> list =
            jdbcManager
                .from(Department.class)
                .innerJoin("employees")
                .where("employees.employeeName like 'S%'")
                .orderBy("departmentId")
                .hint("index (employees IX_EMPLOYEE_1)")
                .getResultList();
        assertEquals(1, list.size());
        assertEquals("RESEARCH", list.get(0).departmentName);
    }

}
