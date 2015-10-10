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
package org.seasar.extension.jdbc.it.sql;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityExistsException;

import org.junit.runner.RunWith;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.it.entity.Employee;
import org.seasar.extension.jdbc.it.entity.Tense;
import org.seasar.framework.unit.Seasar2;
import org.seasar.framework.unit.annotation.Prerequisite;

import static junit.framework.Assert.*;

import static org.seasar.extension.jdbc.parameter.Parameter.*;

/**
 * @author taedium
 * 
 */
@RunWith(Seasar2.class)
public class SqlBatchUpdateTest {

    private JdbcManager jdbcManager;

    /**
     * 
     * @throws Exception
     */
    public void testParameter_none() throws Exception {
        String sql =
            "UPDATE EMPLOYEE SET SALARY = SALARY * 2 WHERE EMPLOYEE_ID = 1";
        int[] result =
            jdbcManager.updateBatchBySql(sql).params().params().execute();
        assertEquals(2, result.length);
    }

    /**
     * 
     * @throws Exception
     */
    public void testParameter() throws Exception {
        String sql =
            "DELETE FROM EMPLOYEE WHERE DEPARTMENT_ID = ? AND SALARY > ?";
        int[] result =
            jdbcManager
                .updateBatchBySql(sql, int.class, BigDecimal.class)
                .params(1, new BigDecimal(3000))
                .params(2, new BigDecimal(2000))
                .execute();
        assertEquals(2, result.length);
        List<Employee> list =
            jdbcManager
                .selectBySql(
                    Employee.class,
                    "SELECT * FROM EMPLOYEE WHERE EMPLOYEE_ID IN (4,8,9,13)")
                .getResultList();
        assertTrue(list.isEmpty());
    }

    /**
     * 
     * @throws Exception
     */
    @Prerequisite("#ENV != 'hsqldb'")
    public void testEntityExistsException_insert() throws Exception {
        String sql =
            "INSERT INTO DEPARTMENT (DEPARTMENT_ID, DEPARTMENT_NO) VALUES(?, ?)";
        try {
            jdbcManager.updateBatchBySql(sql, int.class, int.class).params(
                1,
                50).execute();
            fail();
        } catch (EntityExistsException e) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    @Prerequisite("#ENV != 'hsqldb'")
    public void testEntityExistsException_update() throws Exception {
        String sql =
            "UPDATE DEPARTMENT SET DEPARTMENT_ID = ? WHERE DEPARTMENT_ID = ?";
        try {
            jdbcManager
                .updateBatchBySql(sql, int.class, int.class)
                .params(1, 4)
                .execute();
            fail();
        } catch (EntityExistsException e) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testTemporalType() throws Exception {
        String sql =
            "UPDATE TENSE SET DATE_DATE = ?, DATE_TIME = ?, DATE_TIMESTAMP = ?, CAL_DATE = ?, CAL_TIME = ?, CAL_TIMESTAMP = ?, SQL_DATE = ?, SQL_TIME = ?, SQL_TIMESTAMP = ? WHERE ID = ?";
        long date =
            new SimpleDateFormat("yyyy-MM-dd").parse("2005-03-14").getTime();
        long time =
            new SimpleDateFormat("HH:mm:ss").parse("13:11:10").getTime();
        long timestamp =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(
                "2005-03-14 13:11:10").getTime();
        Calendar calDate = Calendar.getInstance();
        calDate.setTimeInMillis(date);
        Calendar calTime = Calendar.getInstance();
        calTime.setTimeInMillis(time);
        Calendar calTimestamp = Calendar.getInstance();
        calTimestamp.setTimeInMillis(timestamp);

        jdbcManager.updateBatchBySql(
            sql,
            Date.class,
            Date.class,
            Date.class,
            Calendar.class,
            Calendar.class,
            Calendar.class,
            java.sql.Date.class,
            Time.class,
            Timestamp.class,
            int.class).params(
            date(new Date(date)),
            time(new Date(time)),
            timestamp(new Date(timestamp)),
            calDate,
            calTime,
            calTimestamp,
            new java.sql.Date(date),
            new Time(time),
            new Timestamp(timestamp),
            1).execute();

        Tense tense =
            jdbcManager
                .selectBySql(
                    Tense.class,
                    "SELECT DATE_DATE, DATE_TIME, DATE_TIMESTAMP, CAL_DATE, CAL_TIME, CAL_TIMESTAMP, SQL_DATE, SQL_TIME, SQL_TIMESTAMP FROM TENSE WHERE ID = 1")
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
}
