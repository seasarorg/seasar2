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
package org.seasar.extension.jdbc.it.sqlfile;

import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.it.entity.Department;
import org.seasar.extension.unit.S2TestCase;

/**
 * @author taedium
 * 
 */
public class SqlFileUpdateTest extends S2TestCase {

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
    public void test_no_parameterTx() throws Exception {
        String path = getClass().getName().replace(".", "/") + "_no.sql";
        int result = jdbcManager.updateBySqlFile(path).execute();
        assertEquals(1, result);
        Department department = jdbcManager.selectBySql(Department.class,
                "select * from Department where department_id = 1")
                .getSingleResult();
        assertEquals(1, department.departmentId);
        assertEquals(10, department.departmentNo);
        assertEquals("ACCOUNTING", department.departmentName);
        assertEquals("hoge", department.location);
        assertEquals(1, department.version);
    }

    /**
     * 
     * @throws Exception
     */
    public void test_simple_parameterTx() throws Exception {
        String path = getClass().getName().replace(".", "/")
                + "_simpleType.sql";
        int result = jdbcManager.updateBySqlFile(path, 2).execute();
        assertEquals(1, result);
        Department department = jdbcManager.selectBySql(Department.class,
                "select * from Department where department_id = 2")
                .getSingleResult();
        assertEquals(2, department.departmentId);
        assertEquals(20, department.departmentNo);
        assertEquals("RESEARCH", department.departmentName);
        assertEquals("hoge", department.location);
        assertEquals(1, department.version);
    }

    /**
     * 
     * @throws Exception
     */
    public void test_dto_parameterTx() throws Exception {
        String path = getClass().getName().replace(".", "/") + "_dto.sql";
        MyDto myDto = new MyDto();
        myDto.departmentId = 2;
        myDto.location = "foo";
        int result = jdbcManager.updateBySqlFile(path, myDto).execute();
        assertEquals(1, result);
        Department department = jdbcManager.selectBySql(Department.class,
                "select * from Department where department_id = 2")
                .getSingleResult();
        assertEquals(2, department.departmentId);
        assertEquals(20, department.departmentNo);
        assertEquals("RESEARCH", department.departmentName);
        assertEquals("foo", department.location);
        assertEquals(1, department.version);
    }

    /**
     * 
     * @author taedium
     * 
     */
    public static class MyDto {

        /** */
        public int departmentId;

        /** */
        public String location;
    }
}
