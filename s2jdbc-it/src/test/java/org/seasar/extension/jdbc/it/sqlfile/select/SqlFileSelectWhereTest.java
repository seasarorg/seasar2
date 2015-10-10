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
package org.seasar.extension.jdbc.it.sqlfile.select;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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
public class SqlFileSelectWhereTest {

    private JdbcManager jdbcManager;

    /**
     * 
     * @throws Exception
     */
    public void testBean_parameter_dto() throws Exception {
        String path = getClass().getName().replace(".", "/") + "_dto.sql";
        Param param = new Param();
        param.departmentId = 3;
        param.salary = new BigDecimal(1000);
        param.orderBy = "employee_name";
        param.offset = 1;
        param.limit = 3;
        List<Employee> list =
            jdbcManager
                .selectBySqlFile(Employee.class, path, param)
                .getResultList();
        assertEquals(3, list.size());
        assertEquals("BLAKE", list.get(0).employeeName);
        assertEquals("MARTIN", list.get(1).employeeName);
        assertEquals("TURNER", list.get(2).employeeName);
    }

    /**
     * 
     * @throws Exception
     */
    public void testBean_parameter_none() throws Exception {
        String path = getClass().getName().replace(".", "/") + "_no.sql";
        List<Employee> list =
            jdbcManager.selectBySqlFile(Employee.class, path).getResultList();
        assertEquals(14, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void testBean_parameter_simpleType() throws Exception {
        String path =
            getClass().getName().replace(".", "/") + "_simpleType.sql";
        List<Employee> list =
            jdbcManager
                .selectBySqlFile(Employee.class, path, 3)
                .getResultList();
        assertEquals(6, list.size());
        assertEquals("ALLEN", list.get(0).employeeName);
        assertEquals("BLAKE", list.get(1).employeeName);
        assertEquals("JAMES", list.get(2).employeeName);
        assertEquals("MARTIN", list.get(3).employeeName);
        assertEquals("TURNER", list.get(4).employeeName);
        assertEquals("WARD", list.get(5).employeeName);
    }

    /**
     * 
     * @throws Exception
     */
    public void testMap_parameter_dto() throws Exception {
        String path = getClass().getName().replace(".", "/") + "_dto.sql";
        Param param = new Param();
        param.departmentId = 3;
        param.salary = new BigDecimal(1000);
        param.orderBy = "employee_name";
        param.offset = 1;
        param.limit = 3;
        @SuppressWarnings("unchecked")
        List<Map> list =
            jdbcManager.selectBySqlFile(Map.class, path, param).getResultList();
        assertEquals(3, list.size());
        assertEquals("BLAKE", list.get(0).get("employeeName"));
        assertEquals("MARTIN", list.get(1).get("employeeName"));
        assertEquals("TURNER", list.get(2).get("employeeName"));
    }

    /**
     * 
     * @throws Exception
     */
    public void testMap_parameter_none() throws Exception {
        String path = getClass().getName().replace(".", "/") + "_no.sql";
        @SuppressWarnings("unchecked")
        List<Map> list =
            jdbcManager.selectBySqlFile(Map.class, path).getResultList();
        assertEquals(14, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void testMap_parameter_simpleType() throws Exception {
        String path =
            getClass().getName().replace(".", "/") + "_simpleType.sql";
        @SuppressWarnings("unchecked")
        List<Map> list =
            jdbcManager.selectBySqlFile(Map.class, path, 3).getResultList();
        assertEquals(6, list.size());
        assertEquals("ALLEN", list.get(0).get("employeeName"));
        assertEquals("BLAKE", list.get(1).get("employeeName"));
        assertEquals("JAMES", list.get(2).get("employeeName"));
        assertEquals("MARTIN", list.get(3).get("employeeName"));
        assertEquals("TURNER", list.get(4).get("employeeName"));
        assertEquals("WARD", list.get(5).get("employeeName"));
    }

    /**
     * 
     * @throws Exception
     */
    public void testObject_parameter_dto() throws Exception {
        String path = getClass().getName().replace(".", "/") + "_dto2.sql";
        Param param = new Param();
        param.departmentId = 3;
        param.salary = new BigDecimal(1000);
        param.orderBy = "employee_name";
        param.offset = 1;
        param.limit = 3;
        List<Integer> list =
            jdbcManager
                .selectBySqlFile(Integer.class, path, param)
                .getResultList();
        assertEquals(3, list.size());
        assertEquals(6, list.get(0).intValue());
        assertEquals(5, list.get(1).intValue());
        assertEquals(10, list.get(2).intValue());
    }

    /**
     * 
     * @throws Exception
     */
    public void testObject_parameter_none() throws Exception {
        String path = getClass().getName().replace(".", "/") + "_no2.sql";
        List<Integer> list =
            jdbcManager.selectBySqlFile(Integer.class, path).getResultList();
        assertEquals(14, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void testObject_parameter_simpleType() throws Exception {
        String path =
            getClass().getName().replace(".", "/") + "_simpleType2.sql";
        List<Integer> list =
            jdbcManager.selectBySqlFile(Integer.class, path, 3).getResultList();
        assertEquals(6, list.size());
        assertEquals(2, list.get(0).intValue());
        assertEquals(6, list.get(1).intValue());
        assertEquals(12, list.get(2).intValue());
        assertEquals(5, list.get(3).intValue());
        assertEquals(10, list.get(4).intValue());
        assertEquals(3, list.get(5).intValue());
    }

    /**
     * 
     * @author taedium
     */
    public static class Param {

        /** */
        public int departmentId;

        /** */
        public BigDecimal salary;

        /** */
        public String orderBy;

        /** */
        public int offset;

        /** */
        public int limit;
    }

    /**
     * 
     * @author taedium
     */
    public static class Param2 {

        /** */
        @Temporal(TemporalType.DATE)
        public Calendar calDate;

        /** */
        @Temporal(TemporalType.TIME)
        public Calendar calTime;

        /** */
        @Temporal(TemporalType.TIMESTAMP)
        public Calendar calTimestamp;
    }

    /**
     * 
     * @author taedium
     */
    public static class Param3 {

        /** */
        @Temporal(TemporalType.DATE)
        public Date dateDate;

        /** */
        @Temporal(TemporalType.TIME)
        public Date dateTime;

        /** */
        @Temporal(TemporalType.TIMESTAMP)
        public Date dateTimestamp;
    }
}
