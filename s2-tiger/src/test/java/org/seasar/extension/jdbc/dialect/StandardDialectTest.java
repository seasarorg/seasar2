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
package org.seasar.extension.jdbc.dialect;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.TemporalType;

import junit.framework.TestCase;

import org.seasar.extension.jdbc.FromClause;
import org.seasar.extension.jdbc.JoinColumnMeta;
import org.seasar.extension.jdbc.JoinType;
import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.entity.MyDto;
import org.seasar.extension.jdbc.types.ValueTypes;
import org.seasar.framework.exception.SQLRuntimeException;

/**
 * @author higa
 * 
 */
public class StandardDialectTest extends TestCase {

    private StandardDialect dialect = new StandardDialect();

    /** */
    public String stringField;

    /**
     * @throws Exception
     */
    public void testConvertLimitSqlByRowNumber_offsetLimit() throws Exception {
        String sql = "select * from emp order by id";
        String expected = "select * from ( select "
                + "temp_.*, row_number() over(order by id) as rownumber_ from ( select * from emp ) as temp_ ) as temp2_"
                + " where rownumber_ >= 6 and rownumber_ <= 15";
        assertEquals(expected, dialect.convertLimitSqlByRowNumber(sql, 5, 10));
    }

    /**
     * @throws Exception
     */
    public void testConvertLimitSqlByRowNumber_offsetLimit_tableAlias()
            throws Exception {
        String sql = "select * from emp T1_ order by T1_.id";
        String expected = "select * from ( select "
                + "temp_.*, row_number() over(order by temp_.id) as rownumber_ from ( select * from emp T1_ ) as temp_ ) as temp2_"
                + " where rownumber_ >= 6 and rownumber_ <= 15";
        assertEquals(expected, dialect.convertLimitSqlByRowNumber(sql, 5, 10));
    }

    /**
     * @throws Exception
     */
    public void testConvertLimitSqlByRowNumber_offsetOnly() throws Exception {
        String sql = "select * from emp order by id";
        String expected = "select * from ( select "
                + "temp_.*, row_number() over(order by id) as rownumber_ from ( select * from emp ) as temp_ ) as temp2_"
                + " where rownumber_ >= 6";
        assertEquals(expected, dialect.convertLimitSqlByRowNumber(sql, 5, 0));
    }

    /**
     * @throws Exception
     */
    public void testConvertOrderBy() throws Exception {
        assertEquals("order by id", dialect.convertOrderBy("order by id"));
        assertEquals("order by temp_.id", dialect
                .convertOrderBy("order by T1_.id"));
    }

    /**
     * 
     */
    public void testSetupJoin() {
        StandardDialect dialect = new StandardDialect();
        FromClause fromClause = new FromClause();
        fromClause.addSql("AAA", "_T1");
        List<JoinColumnMeta> joinColumnMetaList = new ArrayList<JoinColumnMeta>();
        joinColumnMetaList.add(new JoinColumnMeta("BBB_ID", "BBB_ID"));
        dialect.setupJoin(fromClause, null, JoinType.LEFT_OUTER, "BBB", "_T2",
                "_T1", "_T2", joinColumnMetaList, " with (updlock, rowlock)",
                null);
        assertEquals(
                " from AAA _T1 left outer join BBB _T2 with (updlock, rowlock) on _T1.BBB_ID = _T2.BBB_ID",
                fromClause.toSql());
    }

    /**
     * 
     */
    public void testSetupJoin_WithCondition() {
        StandardDialect dialect = new StandardDialect();
        FromClause fromClause = new FromClause();
        fromClause.addSql("AAA", "_T1");
        List<JoinColumnMeta> joinColumnMetaList = new ArrayList<JoinColumnMeta>();
        joinColumnMetaList.add(new JoinColumnMeta("BBB_ID", "BBB_ID"));
        dialect.setupJoin(fromClause, null, JoinType.LEFT_OUTER, "BBB", "_T2",
                "_T1", "_T2", joinColumnMetaList, " with (updlock, rowlock)",
                "_T1.XXX is null");
        assertEquals(
                " from AAA _T1 left outer join BBB _T2 with (updlock, rowlock) on _T1.BBB_ID = _T2.BBB_ID and _T1.XXX is null",
                fromClause.toSql());
    }

    /**
     * 
     */
    public void testIsUniqueConstraintViolation() {
        StandardDialect dialect = new StandardDialect();
        assertTrue(dialect
                .isUniqueConstraintViolation(new Exception(
                        new SQLRuntimeException(SQLException.class
                                .cast(new SQLException("foo", "XXX")
                                        .initCause(new SQLException("bar",
                                                "270000")))))));
        assertFalse(dialect
                .isUniqueConstraintViolation(new Exception(
                        new SQLRuntimeException(SQLException.class
                                .cast(new SQLException("foo", "XXX")
                                        .initCause(new SQLException("bar",
                                                "000000")))))));
        assertFalse(dialect.isUniqueConstraintViolation(new Exception(
                new RuntimeException())));
    }

    /**
     * 
     */
    public void testGetSQLState() {
        StandardDialect dialect = new StandardDialect();
        assertEquals("10", dialect.getSQLState(new Exception(
                new SQLRuntimeException(SQLException.class
                        .cast(new SQLException("hoge", "XXX")
                                .initCause(new SQLException("hoge", "10")))))));
        assertEquals("20", dialect.getSQLState(new Exception(
                new SQLRuntimeException(SQLException.class
                        .cast(new SQLException("hoge", "XXX")
                                .initCause(new SQLException("hoge", "20")
                                        .initCause(new RuntimeException())))))));
        assertNull(dialect.getSQLState(new Exception(new SQLRuntimeException(
                SQLException.class.cast(new SQLException("hoge")
                        .initCause(new SQLException("hoge")
                                .initCause(new RuntimeException())))))));
    }

    /**
     * 
     * @throws Exception
     */
    public void testGetValueType() throws Exception {
        assertEquals(ValueTypes.STRING, dialect.getValueType(String.class,
                false, null));
        assertEquals(ValueTypes.INTEGER, dialect.getValueType(Integer.class,
                false, null));
        assertEquals(ValueTypes.TIMESTAMP, dialect.getValueType(Date.class,
                false, null));
        assertEquals(ValueTypes.TIMESTAMP, dialect.getValueType(Calendar.class,
                false, null));
        assertEquals(ValueTypes.SERIALIZABLE_BYTE_ARRAY, dialect.getValueType(
                MyDto.class, false, null));
    }

    /**
     * 
     * @throws Exception
     */
    public void testGetValueType_lob() throws Exception {
        assertEquals(ValueTypes.CLOB, dialect.getValueType(String.class, true,
                null));
    }

    /**
     * 
     * @throws Exception
     */
    public void testGetValueType_temporalType() throws Exception {
        assertEquals(ValueTypes.DATE_SQLDATE, dialect.getValueType(Date.class,
                false, TemporalType.DATE));
        assertEquals(ValueTypes.DATE_TIME, dialect.getValueType(Date.class,
                false, TemporalType.TIME));
        assertEquals(ValueTypes.DATE_TIMESTAMP, dialect.getValueType(
                Date.class, false, TemporalType.TIMESTAMP));
        assertEquals(ValueTypes.CALENDAR_SQLDATE, dialect.getValueType(
                Calendar.class, false, TemporalType.DATE));
        assertEquals(ValueTypes.CALENDAR_TIME, dialect.getValueType(
                Calendar.class, false, TemporalType.TIME));
        assertEquals(ValueTypes.CALENDAR_TIMESTAMP, dialect.getValueType(
                Calendar.class, false, TemporalType.TIMESTAMP));
    }

    /**
     * @throws Exception
     */
    public void testGetValueType_propertyMeta() throws Exception {
        PropertyMeta pm = new PropertyMeta();
        pm.setField(getClass().getField("stringField"));
        pm.setValueType(ValueTypes.CLOB);
        assertEquals(ValueTypes.CLOB, dialect.getValueType(pm));
    }

    /**
     * @throws Exception
     */
    public void testConvertGetCountSql() throws Exception {
        String sql = "select * from emp";
        String expected = "select count(*) from ( select * from emp ) COUNT_";
        assertEquals(expected, dialect.convertGetCountSql(sql));
    }
}
