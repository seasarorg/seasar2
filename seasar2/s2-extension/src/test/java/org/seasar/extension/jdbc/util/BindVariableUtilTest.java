/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
package org.seasar.extension.jdbc.util;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;

import junit.framework.TestCase;

import org.seasar.extension.jdbc.IllegalBindArgSizeRuntimeException;
import org.seasar.extension.jdbc.ValueType;
import org.seasar.extension.jdbc.types.ValueTypes;

/**
 * @author higa
 * 
 */
public class BindVariableUtilTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testGetCompleteSql1() throws Exception {
        final String sql = "update emp set ename = ?, comm = ? where empno = ?";
        assertEquals(
                "update emp set ename = 'foo', comm = null where empno = 'bar'",
                BindVariableUtil.getCompleteSql(sql, new Object[] { "foo",
                        null, "bar" }));
    }

    /**
     * @throws Exception
     */
    public void testGetCompleteSql2() throws Exception {
        final String sql = "update emp set ename = ?, comm = '?' where empno = ?";
        assertEquals(
                "update emp set ename = 'foo', comm = '?' where empno = 'bar'",
                BindVariableUtil.getCompleteSql(sql, new Object[] { "foo",
                        "bar" }));
    }

    /**
     * @throws Exception
     */
    public void testGetCompleteSql3() throws Exception {
        final String sql = "update emp set ename = /* ? */, comm = ? where empno = ?/*?*/";
        assertEquals(
                "update emp set ename = /* ? */, comm = null where empno = 'bar'/*?*/",
                BindVariableUtil.getCompleteSql(sql,
                        new Object[] { null, "bar" }));
    }

    /**
     * @throws Exception
     */
    public void testGetCompleteSql4() throws Exception {
        final String sql = "update emp set ename = ?, comm = ? where empno = ?";
        assertEquals(
                "update emp set ename = 'foo', comm = null where empno = 'bar'",
                BindVariableUtil.getCompleteSql(sql, new Object[] { "foo",
                        null, "bar" }, new ValueType[] { ValueTypes.STRING,
                        ValueTypes.BIGDECIMAL, ValueTypes.STRING }));
    }

    /**
     * @throws Exception
     */
    public void testGetCompleteSql_exception() throws Exception {
        final String sql = "update emp set ename = ?, comm = ? where empno = ?";
        try {
            BindVariableUtil.getCompleteSql(sql, new Object[] { "foo", "bar" });
            fail();
        } catch (IllegalBindArgSizeRuntimeException e) {
        }
    }

    /**
     * @throws Exception
     */
    public void testToText_Number() throws Exception {
        BigDecimal value = new BigDecimal("123.456");
        String text = BindVariableUtil.toText(value);
        assertEquals("123.456", text);
    }

    /**
     * @throws Exception
     */
    public void testToText_Boolean() throws Exception {
        Boolean value = Boolean.TRUE;
        String text = BindVariableUtil.toText(value);
        assertEquals("'true'", text);
    }

    /**
     * @throws Exception
     */
    public void testToText_String() throws Exception {
        String value = "hoge";
        String text = BindVariableUtil.toText(value);
        assertEquals("'hoge'", text);
    }

    /**
     * @throws Exception
     */
    public void testToText_SqlDate() throws Exception {
        long time = Timestamp.valueOf("2007-11-29 13:14:15.123456789")
                .getTime();
        java.sql.Date value = new java.sql.Date(time);
        String text = BindVariableUtil.toText(value);
        assertEquals("'2007-11-29'", text);
    }

    /**
     * @throws Exception
     */
    public void testToText_Time() throws Exception {
        long time = Timestamp.valueOf("2007-11-29 13:14:15.123456789")
                .getTime();
        Time value = new Time(time);
        String text = BindVariableUtil.toText(value);
        assertEquals("'13:14:15.123'", text);
    }

    /**
     * @throws Exception
     */
    public void testToText_Time_zeroMilliSecond() throws Exception {
        long time = Timestamp.valueOf("2007-11-29 13:14:15").getTime();
        Time value = new Time(time);
        String text = BindVariableUtil.toText(value);
        assertEquals("'13:14:15'", text);
    }

    /**
     * @throws Exception
     */
    public void testToText_Timestamp() throws Exception {
        Timestamp value = Timestamp.valueOf("2007-11-29 13:14:15.123456789");
        String text = BindVariableUtil.toText(value);
        assertEquals("'2007-11-29 13:14:15.123456789'", text);
    }

    /**
     * @throws Exception
     */
    public void testToText_Timestamp_zeroMilliSecond() throws Exception {
        Timestamp value = Timestamp.valueOf("2007-11-29 13:14:15");
        String text = BindVariableUtil.toText(value);
        assertEquals("'2007-11-29 13:14:15'", text);
    }

    /**
     * @throws Exception
     */
    public void testToText_ByteArray() throws Exception {
        byte[] value = new byte[] { 1, 2, 3 };
        String text = BindVariableUtil.toText(value);
        assertEquals("'" + value + "(byteLength=3)'", text);
    }

    /**
     * @throws Exception
     */
    public void testToText_Object() throws Exception {
        Object value = new Object();
        String text = BindVariableUtil.toText(value);
        assertEquals("'" + value.toString() + "'", text);
    }

    /**
     * @throws Exception
     */
    public void testNullText() throws Exception {
        assertEquals("null", BindVariableUtil.nullText());
    }
}