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
package org.seasar.extension.jdbc.it.sql.select;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.persistence.TemporalType;

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
public class SqlSelectValueTypeTest {

    private JdbcManager jdbcManager;

    /**
     * 
     * @throws Exception
     */
    public void testBean_temporalType() throws Exception {
        String sql = "SELECT * FROM TENSE WHERE ID = 1";
        Tense tense =
            jdbcManager.selectBySql(Tense.class, sql).getSingleResult();
        long date =
            new SimpleDateFormat("yyyy-MM-dd").parse("2005-02-14").getTime();
        long time =
            new SimpleDateFormat("HH:mm:ss").parse("12:11:10").getTime();
        long timestamp =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(
                "2005-02-14 12:11:10").getTime();
        assertNotNull(tense);
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
     * @throws Exception
     */
    public void testBean_temporalType_Calendar() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar
            .setTime(new SimpleDateFormat("yyyy-MM-dd").parse("2005-02-14"));
        Tense tense =
            jdbcManager.selectBySql(
                Tense.class,
                "SELECT * FROM TENSE WHERE CAL_DATE = ?",
                date(calendar)).getSingleResult();
        assertNotNull(tense);

        calendar.setTime(new SimpleDateFormat("HH:mm:ss").parse("12:11:10"));
        tense =
            jdbcManager.selectBySql(
                Tense.class,
                "SELECT * FROM TENSE WHERE CAL_TIME = ?",
                time(calendar)).getSingleResult();
        assertNotNull(tense);

        calendar.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            .parse("2005-02-14 12:11:10"));
        tense =
            jdbcManager.selectBySql(
                Tense.class,
                "SELECT * FROM TENSE WHERE CAL_TIMESTAMP = ?",
                timestamp(calendar)).getSingleResult();
        assertNotNull(tense);
    }

    /**
     * 
     * @throws Exception
     */
    public void testBean_temporalType_Date() throws Exception {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2005-02-14");
        Tense tense =
            jdbcManager.selectBySql(
                Tense.class,
                "SELECT * FROM TENSE WHERE DATE_DATE = ?",
                date(date)).getSingleResult();
        assertNotNull(tense);

        date = new SimpleDateFormat("HH:mm:ss").parse("12:11:10");
        tense =
            jdbcManager.selectBySql(
                Tense.class,
                "SELECT * FROM TENSE WHERE DATE_TIME = ?",
                time(date)).getSingleResult();
        assertNotNull(tense);

        date =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .parse("2005-02-14 12:11:10");
        tense =
            jdbcManager.selectBySql(
                Tense.class,
                "SELECT * FROM TENSE WHERE DATE_TIMESTAMP = ?",
                timestamp(date)).getSingleResult();
        assertNotNull(tense);
    }

    /**
     * 
     * @throws Exception
     */
    public void testMap_temporalType() throws Exception {
        String sql = "SELECT * FROM TENSE WHERE ID = 1";
        Map<?, ?> tense =
            jdbcManager.selectBySql(Map.class, sql).getSingleResult();
        assertNotNull(tense);
        assertNotNull(tense.get("calDate"));
        assertNotNull(tense.get("dateDate"));
        assertNotNull(tense.get("sqlDate"));
        assertNotNull(tense.get("calTime"));
        assertNotNull(tense.get("dateTime"));
        assertNotNull(tense.get("sqlTime"));
        assertNotNull(tense.get("calTimestamp"));
        assertNotNull(tense.get("dateTimestamp"));
        assertNotNull(tense.get("sqlTimestamp"));
    }

    /**
     * 
     * @throws Exception
     */
    public void testObject_temporalType() throws Exception {
        String sql = "SELECT CAL_TIMESTAMP FROM TENSE WHERE ID = 1";
        Calendar calTimestamp =
            jdbcManager.selectBySql(Calendar.class, sql).temporal(
                TemporalType.TIMESTAMP).getSingleResult();
        assertNotNull(calTimestamp);
        long time =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(
                "2005-02-14 12:11:10").getTime();
        assertEquals(time, calTimestamp.getTimeInMillis());
    }
}
