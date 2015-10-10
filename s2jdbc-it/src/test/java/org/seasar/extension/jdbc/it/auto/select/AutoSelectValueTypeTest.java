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

import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.runner.RunWith;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.it.entity.Authority;
import org.seasar.extension.jdbc.it.entity.AuthorityType;
import org.seasar.extension.jdbc.it.entity.Job;
import org.seasar.extension.jdbc.it.entity.JobType;
import org.seasar.extension.jdbc.it.entity.Tense;
import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.unit.Seasar2;

import static org.junit.Assert.*;
import static org.seasar.extension.jdbc.parameter.Parameter.*;

/**
 * @author taedium
 * 
 */
@RunWith(Seasar2.class)
public class AutoSelectValueTypeTest {

    private JdbcManager jdbcManager;

    /**
     * 
     * @throws Exception
     */
    public void testTemporalType() throws Exception {
        Tense tense =
            jdbcManager.from(Tense.class).where("id = ?", 1).getSingleResult();
        assertEquals(tense.sqlDate.getTime(), tense.calDate.getTimeInMillis());
        assertEquals(tense.sqlDate.getTime(), tense.dateDate.getTime());
        assertEquals(tense.sqlTime.getTime(), tense.calTime.getTimeInMillis());
        assertEquals(tense.sqlTime.getTime(), tense.dateTime.getTime());
        assertEquals(tense.sqlTimestamp.getTime(), tense.calTimestamp
            .getTimeInMillis());
        assertEquals(tense.sqlTimestamp.getTime(), tense.dateTimestamp
            .getTime());
    }

    /**
     * 
     * @throws Exception
     */
    public void testTemporalTypeCalendar_criteria() throws Exception {
        Date date =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .parse("2005-02-14 12:11:10");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Tense tense =
            jdbcManager
                .from(Tense.class)
                .where("calDate = ?", date(calendar))
                .getSingleResult();
        assertNotNull(tense);
        tense =
            jdbcManager
                .from(Tense.class)
                .where("calTime = ?", time(calendar))
                .getSingleResult();
        assertNotNull(tense);
        tense =
            jdbcManager.from(Tense.class).where(
                "calTimestamp = ?",
                timestamp(calendar)).getSingleResult();
        assertNotNull(tense);
    }

    /**
     * 
     * @throws Exception
     */
    public void testTemporalTypeCalendar_simpleWhere() throws Exception {
        Date date =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .parse("2005-02-14 12:11:10");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Tense tense =
            jdbcManager.from(Tense.class).where(
                new SimpleWhere().eq("calDate", calendar)).getSingleResult();
        assertNotNull(tense);
        tense =
            jdbcManager.from(Tense.class).where(
                new SimpleWhere().eq("calTime", calendar)).getSingleResult();
        assertNotNull(tense);
        tense =
            jdbcManager
                .from(Tense.class)
                .where(new SimpleWhere().eq("calTimestamp", calendar))
                .getSingleResult();
        assertNotNull(tense);
    }

    /**
     * 
     * @throws Exception
     */
    public void testTemporalTypeDate_criteria() throws Exception {
        Date date =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .parse("2005-02-14 12:11:10");
        Tense tense =
            jdbcManager
                .from(Tense.class)
                .where("dateDate = ?", date(date))
                .getSingleResult();
        assertNotNull(tense);
        tense =
            jdbcManager
                .from(Tense.class)
                .where("dateTime = ?", time(date))
                .getSingleResult();
        assertNotNull(tense);
        tense =
            jdbcManager.from(Tense.class).where(
                "dateTimestamp = ?",
                timestamp(date)).getSingleResult();
        assertNotNull(tense);
    }

    /**
     * 
     * @throws Exception
     */
    public void testTemporalTypeDate_simpleWhere() throws Exception {
        Date date =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .parse("2005-02-14 12:11:10");
        Tense tense =
            jdbcManager.from(Tense.class).where(
                new SimpleWhere().eq("dateDate", date)).getSingleResult();
        assertNotNull(tense);
        tense =
            jdbcManager.from(Tense.class).where(
                new SimpleWhere().eq("dateTime", date)).getSingleResult();
        assertNotNull(tense);
        tense =
            jdbcManager.from(Tense.class).where(
                new SimpleWhere().eq("dateTimestamp", date)).getSingleResult();
        assertNotNull(tense);
    }

    /**
     * 
     * @throws Exception
     */
    public void testTemporalTypeSql_criteria() throws Exception {
        long time =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(
                "2005-02-14 12:11:10").getTime();
        Tense tense =
            jdbcManager.from(Tense.class).where(
                "sqlDate = ?",
                new java.sql.Date(time)).getSingleResult();
        assertNotNull(tense);
        tense =
            jdbcManager.from(Tense.class).where(
                "sqlTime = ?",
                Time.valueOf("12:11:10")).getSingleResult();
        assertNotNull(tense);
        tense =
            jdbcManager.from(Tense.class).where(
                "sqlTimestamp = ?",
                new Timestamp(time)).getSingleResult();
        assertNotNull(tense);
    }

    /**
     * 
     * @throws Exception
     */
    public void testTemporalTypeSql_simpleWhere() throws Exception {
        long time =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(
                "2005-02-14 12:11:10").getTime();
        Tense tense =
            jdbcManager
                .from(Tense.class)
                .where(new SimpleWhere().eq("sqlDate", new java.sql.Date(time)))
                .getSingleResult();
        assertNotNull(tense);
        tense =
            jdbcManager
                .from(Tense.class)
                .where(
                    new SimpleWhere().eq("sqlTime", Time.valueOf("12:11:10")))
                .getSingleResult();
        assertNotNull(tense);
        tense =
            jdbcManager
                .from(Tense.class)
                .where(
                    new SimpleWhere().eq("sqlTimestamp", new Timestamp(time)))
                .getSingleResult();
        assertNotNull(tense);
    }

    /**
     * 
     * @throws Exception
     */
    public void testUserDefineValueType() throws Exception {
        Authority authority =
            jdbcManager.from(Authority.class).id(3).getSingleResult();
        assertEquals(30, authority.authorityType.value());
    }

    /**
     * 
     * @throws Exception
     */
    public void testUserDefineValueType_criteria() throws Exception {
        Authority authority =
            jdbcManager.from(Authority.class).where(
                "authorityType = ?",
                AuthorityType.valueOf(20)).getSingleResult();
        assertEquals(2, authority.id);
    }

    /**
     * 
     * @throws Exception
     */
    public void testUserDefineValueType_simpleWhere() throws Exception {
        Authority authority =
            jdbcManager
                .from(Authority.class)
                .where(
                    new SimpleWhere().eq("authorityType", AuthorityType
                        .valueOf(20)))
                .getSingleResult();
        assertEquals(2, authority.id);
    }

    /**
     * 
     * @throws Exception
     */
    public void testEnumType() throws Exception {
        Job job = jdbcManager.from(Job.class).id(3).getSingleResult();
        assertEquals(JobType.PRESIDENT, job.jobType);
    }

    /**
     * 
     * @throws Exception
     */
    public void testEnumType_criteria() throws Exception {
        Job job =
            jdbcManager
                .from(Job.class)
                .where("jobType = ?", JobType.MANAGER)
                .getSingleResult();
        assertEquals(2, job.id);
    }

    /**
     * 
     * @throws Exception
     */
    public void testEnumType_simpleWhere() throws Exception {
        Job job =
            jdbcManager
                .from(Job.class)
                .where(new SimpleWhere().eq("jobType", JobType.MANAGER))
                .getSingleResult();
        assertEquals(2, job.id);
    }

}
