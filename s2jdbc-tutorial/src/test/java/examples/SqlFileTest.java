/*
 * Copyright 2004-2013 the Seasar Foundation and the Others.
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
import examples.dto.SelectWithDepartmentDto;

/**
 * @author higa
 * 
 */
public class SqlFileTest extends S2TestCase {

    private static final String SQL_FILE =
        "META-INF/sql/examples/entity/Employee/selectWithDepartment.sql";

    private JdbcManager jdbcManager;

    protected void setUp() throws Exception {
        include("app.dicon");
    }

    /**
     * @throws Exception
     */
    public void testSqlFile() throws Exception {
        SelectWithDepartmentDto dto = new SelectWithDepartmentDto();
        dto.salaryMin = 1200;
        dto.salaryMax = 1800;
        List<EmployeeDto> results =
            jdbcManager
                .selectBySqlFile(EmployeeDto.class, SQL_FILE, dto)
                .getResultList();
        for (EmployeeDto e : results) {
            System.out
                .println(e.name + " " + e.salary + " " + e.departmentName);
        }
    }
}