/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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

import javax.persistence.EntityExistsException;

import org.junit.runner.RunWith;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.it.entity.Tense;
import org.seasar.framework.unit.Seasar2;

import static org.junit.Assert.*;
import static org.seasar.extension.jdbc.parameter.Parameter.*;

/**
 * @author taedium
 * 
 */
@RunWith(Seasar2.class)
public class SqlUpdateTest {

    private JdbcManager jdbcManager;

    /**
     * 
     * @throws Exception
     */
    public void testParameter_none() throws Exception {
        String sql = "delete from Employee";
        int actual = jdbcManager.updateBySql(sql).execute();
        assertEquals(14, actual);
    }

    /**
     * 
     * @throws Exception
     */
    public void testParameter() throws Exception {
        String sql =
            "delete from Employee where department_Id = ? and salary > ?";
        int actual =
            jdbcManager.updateBySql(sql, int.class, BigDecimal.class).params(
                3,
                new BigDecimal(1000)).execute();
        assertEquals(5, actual);
    }

    /**
     * 
     * @throws Exception
     */
    public void testEntityExistsException_insert() throws Exception {
        String sql =
            "insert into Department (department_id, department_no) values(?, ?)";
        try {
            jdbcManager
                .updateBySql(sql, int.class, int.class)
                .params(1, 50)
                .execute();
            fail();
        } catch (EntityExistsException e) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testEntityExistsException_update() throws Exception {
        String sql =
            "update Department set department_id = ? where department_id = ?";
        try {
            jdbcManager
                .updateBySql(sql, int.class, int.class)
                .params(1, 2)
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
            "update Tense set date_date = ?, date_time = ?, date_timestamp = ?, cal_date = ?, cal_time = ?, cal_timestamp = ?, sql_date = ?, sql_time = ?, sql_timestamp = ? where id = ?";
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

        jdbcManager.updateBySql(
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
                    "select date_date, date_time, date_timestamp, cal_date, cal_time, cal_timestamp, sql_date, sql_time, sql_timestamp from Tense where id = 1")
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
