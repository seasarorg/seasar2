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
package org.seasar.extension.jdbc.it.sqlfile;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.EntityExistsException;

import org.junit.runner.RunWith;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.it.entity.Department;
import org.seasar.extension.jdbc.it.entity.Tense;
import org.seasar.framework.unit.Seasar2;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
@RunWith(Seasar2.class)
public class SqlFileUpdateTest {

    private JdbcManager jdbcManager;

    /**
     * 
     * @throws Exception
     */
    public void testParameter_none() throws Exception {
        String path = getClass().getName().replace(".", "/") + "_no.sql";
        int result = jdbcManager.updateBySqlFile(path, null).execute();
        assertEquals(1, result);
        Department department =
            jdbcManager
                .selectBySql(
                    Department.class,
                    "select * from DEPARTMENT where DEPARTMENT_ID = 1")
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
    public void testParamter_simpleType() throws Exception {
        String path =
            getClass().getName().replace(".", "/") + "_simpleType.sql";
        int result = jdbcManager.updateBySqlFile(path, 2).execute();
        assertEquals(1, result);
        Department department =
            jdbcManager
                .selectBySql(
                    Department.class,
                    "select * from DEPARTMENT where DEPARTMENT_ID = 2")
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
    public void testParamter_dto() throws Exception {
        String path = getClass().getName().replace(".", "/") + "_dto.sql";
        MyDto myDto = new MyDto();
        myDto.departmentId = 2;
        myDto.location = "foo";
        int result = jdbcManager.updateBySqlFile(path, myDto).execute();
        assertEquals(1, result);
        Department department =
            jdbcManager
                .selectBySql(
                    Department.class,
                    "select * from DEPARTMENT where DEPARTMENT_ID = 2")
                .getSingleResult();
        assertEquals(2, department.departmentId);
        assertEquals(20, department.departmentNo);
        assertEquals("RESEARCH", department.departmentName);
        assertEquals("foo", department.location);
        assertEquals(1, department.version);
    }

    /**
     * 
     * @throws Exception
     */
    public void testEntityExistsException_insert() throws Exception {
        String path =
            getClass().getName().replace(".", "/")
                + "_EntityExistsException_insert.sql";
        MyDto2 dto = new MyDto2();
        dto.departmentId = 1;
        dto.departmentNo = 50;
        try {
            jdbcManager.updateBySqlFile(path, dto).execute();
            fail();
        } catch (EntityExistsException e) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testEntityExistsException_update() throws Exception {
        jdbcManager
            .updateBySql(
                "insert into DEPARTMENT (DEPARTMENT_ID, DEPARTMENT_NO) values (99, 99)")
            .execute();
        String path =
            getClass().getName().replace(".", "/")
                + "_EntityExistsException_update.sql";
        MyDto3 dto = new MyDto3();
        dto.departmentId = 1;
        dto.departmentId2 = 99;
        try {
            jdbcManager.updateBySqlFile(path, dto).execute();
            fail();
        } catch (EntityExistsException e) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testTemporalType() throws Exception {
        String path =
            getClass().getName().replace(".", "/") + "_temporalType.sql";
        long date =
            new SimpleDateFormat("yyyy-MM-dd").parse("2005-03-14").getTime();
        long time =
            new SimpleDateFormat("HH:mm:ss").parse("13:11:10").getTime();
        long timestamp =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(
                "2005-03-14 13:11:10").getTime();
        Tense tense = new Tense();
        tense.id = 1;
        tense.dateDate = new Date(date);
        tense.dateTime = new Date(time);
        tense.dateTimestamp = new Date(timestamp);
        tense.calDate = Calendar.getInstance();
        tense.calDate.setTimeInMillis(date);
        tense.calTime = Calendar.getInstance();
        tense.calTime.setTimeInMillis(time);
        tense.calTimestamp = Calendar.getInstance();
        tense.calTimestamp.setTimeInMillis(timestamp);
        tense.sqlDate = new java.sql.Date(date);
        tense.sqlTime = new Time(time);
        tense.sqlTimestamp = new Timestamp(timestamp);

        jdbcManager.updateBySqlFile(path, tense).execute();

        tense =
            jdbcManager
                .selectBySql(
                    Tense.class,
                    "select DATE_DATE, DATE_TIME, DATE_TIMESTAMP, CAL_DATE, CAL_TIME, CAL_TIMESTAMP, SQL_DATE, SQL_TIME, SQL_TIMESTAMP from TENSE where ID = 1")
                .getSingleResult();
        assertEquals(date, tense.calDate.getTimeInMillis());
        assertEquals(date, tense.dateDate.getTime());
        assertEquals(date, tense.sqlDate.getTime());
        assertEquals(time, tense.calTime.getTimeInMillis());
        assertEquals(time, tense.dateTime.getTime());
        assertEquals(time, tense.sqlTime.getTime());
        assertEquals(timestamp, tense.calTimestamp.getTimeInMillis());
        assertEquals(timestamp, tense.dateTimestamp.getTime());
        assertEquals(timestamp, tense.sqlTimestamp.getTime());
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

    /**
     * 
     * @author taedium
     * 
     */
    public static class MyDto2 {

        /** */
        public int departmentId;

        /** */
        public int departmentNo;
    }

    /**
     * 
     * @author taedium
     * 
     */
    public static class MyDto3 {

        /** */
        public int departmentId;

        /** */
        public int departmentId2;
    }
}
