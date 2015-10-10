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
package examples;

import java.util.List;

import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.unit.S2TestCase;

import examples.dto.EmployeeDto;

/**
 * @author higa
 * 
 */
public class SqlGetResultListTest extends S2TestCase {

    private static final String SELECT_EMPLOYEE_DTO =
        "select e.*, d.name as department_name"
            + " from employee e left outer join department d"
            + " on e.department_id = d.id"
            + " where d.id = ?";

    private JdbcManager jdbcManager;

    protected void setUp() throws Exception {
        include("app.dicon");
    }

    /**
     * @throws Exception
     */
    public void testSqlGetResultList() throws Exception {
        List<EmployeeDto> results =
            jdbcManager
                .selectBySql(EmployeeDto.class, SELECT_EMPLOYEE_DTO, 1)
                .getResultList();
        for (EmployeeDto e : results) {
            System.out.println(e.name + " " + e.departmentName);
        }
    }
}