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

import junit.framework.TestCase;

import org.seasar.extension.jdbc.exception.OrderByNotFoundRuntimeException;
import org.seasar.framework.exception.SQLRuntimeException;

/**
 * @author higa
 * 
 */
public class Mssql2005DialectTest extends TestCase {

    private Mssql2005Dialect dialect = new Mssql2005Dialect();

    /**
     * @throws Exception
     */
    public void testConvertLimitSql_limitOnly() throws Exception {
        String sql = "select * from emp order by id";
        String expected = "select top 5 * from emp order by id";
        assertEquals(expected, dialect.convertLimitSql(sql, 0, 5));

    }

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
    public void testConvertLimitSql_offsetLimit_notFoundOrderBy()
            throws Exception {
        String sql = "select * from emp";
        try {
            dialect.convertLimitSql(sql, 5, 10);
            fail();
        } catch (OrderByNotFoundRuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @throws Exception
     */
    public void testIsUniqueConstraintViolation() throws Exception {
        assertTrue(dialect.isUniqueConstraintViolation(new Exception(
                new SQLRuntimeException(SQLException.class
                        .cast(new SQLException("foo", "XXX")
                                .initCause(new SQLException("bar", "23000",
                                        2627)))))));
        assertFalse(dialect
                .isUniqueConstraintViolation(new Exception(
                        new SQLRuntimeException(SQLException.class
                                .cast(new SQLException("foo", "XXX")
                                        .initCause(new SQLException("bar",
                                                "23000")))))));
        assertFalse(dialect.isUniqueConstraintViolation(new Exception(
                new RuntimeException())));
    }
}
