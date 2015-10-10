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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import javax.persistence.TemporalType;

import org.junit.runner.RunWith;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.it.entity.Tense;
import org.seasar.extension.jdbc.it.sqlfile.select.SqlFileSelectWhereTest.Param2;
import org.seasar.extension.jdbc.it.sqlfile.select.SqlFileSelectWhereTest.Param3;
import org.seasar.framework.unit.Seasar2;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
@RunWith(Seasar2.class)
public class SqlFileSelectValueTypeTest {

    private JdbcManager jdbcManager;

    /**
     * 
     * @throws Exception
     */
    public void testBean_temporalType() throws Exception {
        String path =
            getClass().getName().replace(".", "/") + "_temporalType.sql";
        Tense tense =
            jdbcManager.selectBySqlFile(Tense.class, path).getSingleResult();
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
        Param2 param = new Param2();
        param.calDate = Calendar.getInstance();
        param.calDate.setTime(new SimpleDateFormat("yyyy-MM-dd")
            .parse("2005-02-14"));
        param.calTime = Calendar.getInstance();
        param.calTime.setTime(new SimpleDateFormat("HH:mm:ss")
            .parse("12:11:10"));
        param.calTimestamp = Calendar.getInstance();
        param.calTimestamp.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            .parse("2005-02-14 12:11:10"));

        String path =
            getClass().getName().replace(".", "/")
                + "_temporalType_Calendar1.sql";
        Tense tense =
            jdbcManager
                .selectBySqlFile(Tense.class, path, param)
                .getSingleResult();
        assertNotNull(tense);

        path =
            getClass().getName().replace(".", "/")
                + "_temporalType_Calendar2.sql";
        tense =
            jdbcManager
                .selectBySqlFile(Tense.class, path, param)
                .getSingleResult();
        assertNotNull(tense);

        path =
            getClass().getName().replace(".", "/")
                + "_temporalType_Calendar3.sql";
        tense =
            jdbcManager
                .selectBySqlFile(Tense.class, path, param)
                .getSingleResult();
        assertNotNull(tense);
    }

    /**
     * 
     * @throws Exception
     */
    public void testBean_temporalType_Date() throws Exception {
        Param3 param = new Param3();
        param.dateDate = new SimpleDateFormat("yyyy-MM-dd").parse("2005-02-14");
        param.dateTime = new SimpleDateFormat("HH:mm:ss").parse("12:11:10");
        param.dateTimestamp =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .parse("2005-02-14 12:11:10");

        String path =
            getClass().getName().replace(".", "/") + "_temporalType_Date1.sql";
        Tense tense =
            jdbcManager
                .selectBySqlFile(Tense.class, path, param)
                .getSingleResult();
        assertNotNull(tense);

        path =
            getClass().getName().replace(".", "/") + "_temporalType_Date2.sql";
        tense =
            jdbcManager
                .selectBySqlFile(Tense.class, path, param)
                .getSingleResult();
        assertNotNull(tense);

        path =
            getClass().getName().replace(".", "/") + "_temporalType_Date3.sql";
        tense =
            jdbcManager
                .selectBySqlFile(Tense.class, path, param)
                .getSingleResult();
        assertNotNull(tense);
    }

    /**
     * 
     * @throws Exception
     */
    public void testMap_temporalType() throws Exception {
        String path =
            getClass().getName().replace(".", "/") + "_temporalType.sql";
        Map<?, ?> tense =
            jdbcManager.selectBySqlFile(Map.class, path).getSingleResult();
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
        String path =
            getClass().getName().replace(".", "/") + "_Object_temporalType.sql";
        Calendar calTimestamp =
            jdbcManager.selectBySqlFile(Calendar.class, path).temporal(
                TemporalType.TIMESTAMP).getSingleResult();
        assertNotNull(calTimestamp);
        long time =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(
                "2005-02-14 12:11:10").getTime();
        assertEquals(time, calTimestamp.getTimeInMillis());
    }
}
