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
package org.seasar.extension.jdbc.query;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.EntityExistsException;

import junit.framework.TestCase;

import org.seasar.extension.jdbc.JdbcContext;
import org.seasar.extension.jdbc.SqlLog;
import org.seasar.extension.jdbc.SqlLogRegistry;
import org.seasar.extension.jdbc.SqlLogRegistryLocator;
import org.seasar.extension.jdbc.dialect.StandardDialect;
import org.seasar.extension.jdbc.exception.IllegalParamSizeRuntimeException;
import org.seasar.extension.jdbc.exception.QueryTwiceExecutionRuntimeException;
import org.seasar.extension.jdbc.manager.JdbcManagerImpl;
import org.seasar.extension.jdbc.types.ValueTypes;
import org.seasar.extension.jta.TransactionManagerImpl;
import org.seasar.extension.jta.TransactionSynchronizationRegistryImpl;
import org.seasar.framework.mock.sql.MockDataSource;
import org.seasar.framework.mock.sql.MockPreparedStatement;

import static org.seasar.extension.jdbc.parameter.Parameter.*;

/**
 * @author higa
 * 
 */
public class SqlUpdateImplTest extends TestCase {

    private JdbcManagerImpl manager;

    private boolean preparedBindVariables = false;

    @Override
    protected void setUp() throws Exception {
        manager = new JdbcManagerImpl();
        manager.setSyncRegistry(new TransactionSynchronizationRegistryImpl(
                new TransactionManagerImpl()));
        manager.setDataSource(new MockDataSource());
        manager.setDialect(new StandardDialect());

    }

    @Override
    protected void tearDown() throws Exception {
        SqlLogRegistry regisry = SqlLogRegistryLocator.getInstance();
        regisry.clear();
        manager = null;
    }

    /**
     * 
     */
    public void testCallerClass() {
        SqlUpdateImpl query = new SqlUpdateImpl(manager,
                "update aaa set name = ? where id = ?", String.class,
                Integer.class);
        assertSame(query, query.callerClass(getClass()));
        assertEquals(getClass(), query.callerClass);
    }

    /**
     * 
     */
    public void testCallerMethodName() {
        SqlUpdateImpl query = new SqlUpdateImpl(manager,
                "update aaa set name = ? where id = ?", String.class,
                Integer.class);
        assertSame(query, query.callerMethodName("hoge"));
        assertEquals("hoge", query.callerMethodName);
    }

    /**
     * 
     */
    public void testQueryTimeout() {
        SqlUpdateImpl query = new SqlUpdateImpl(manager,
                "update aaa set name = ? where id = ?", String.class,
                Integer.class);
        assertSame(query, query.queryTimeout(100));
        assertEquals(100, query.queryTimeout);
    }

    /**
     * 
     */
    public void testParams() {
        SqlUpdateImpl query = new SqlUpdateImpl(manager,
                "update aaa set name = ? where id = ?", String.class,
                Integer.class);
        query.params("hoge", 1);
        assertEquals(2, query.params.length);
        assertEquals("hoge", query.params[0]);
        assertEquals(1, query.params[1]);
    }

    /**
     * 
     * @throws Exception
     */
    public void testParams_lob() throws Exception {
        String sql = "update aaa set bbb = ? where ccc = ? and ddd = ?";
        SqlUpdateImpl query = new SqlUpdateImpl(manager, sql, String.class,
                String.class, byte[].class) {

            @Override
            protected PreparedStatement getPreparedStatement(
                    JdbcContext jdbcContext) {
                return new MockPreparedStatement(null, null);
            }

        };
        query.params(lob((String) null), lob("hoge"), lob(new byte[] {}))
                .execute();
        assertEquals(3, query.getParamSize());
        assertEquals(ValueTypes.CLOB, query.getParam(0).valueType);
        assertEquals(ValueTypes.CLOB, query.getParam(1).valueType);
        assertEquals(ValueTypes.BLOB, query.getParam(2).valueType);
    }

    /**
     * 
     * @throws Exception
     */
    public void testParams_temporalType() throws Exception {
        String sql = "update aaa set bbb = ? where ccc = ? and ddd = ?";
        SqlUpdateImpl query = new SqlUpdateImpl(manager, sql, Calendar.class,
                Date.class, Date.class) {

            @Override
            protected PreparedStatement getPreparedStatement(
                    JdbcContext jdbcContext) {
                return new MockPreparedStatement(null, null);
            }

        };
        query.params(time((Calendar) null), date(new Date()), time(new Date()))
                .execute();
        assertEquals(3, query.getParamSize());
        assertEquals(ValueTypes.CALENDAR_TIME, query.getParam(0).valueType);
        assertEquals(ValueTypes.DATE_SQLDATE, query.getParam(1).valueType);
        assertEquals(ValueTypes.DATE_TIME, query.getParam(2).valueType);
    }

    /**
     * @throws Exception
     * 
     */
    public void testGetPreparedStatement() throws Exception {
        SqlUpdateImpl query = new SqlUpdateImpl(manager,
                "update aaa set name = ? where id = ?", String.class,
                Integer.class) {

            @Override
            protected void prepareInParams(PreparedStatement ps) {
                super.prepareInParams(ps);
                preparedBindVariables = true;
            }

        };
        query.queryTimeout = 30;
        JdbcContext jdbcContext = manager.getJdbcContext();
        PreparedStatement ps = query.getPreparedStatement(jdbcContext);
        assertEquals(ResultSet.TYPE_FORWARD_ONLY, ps.getResultSetType());
        assertEquals(30, ps.getQueryTimeout());
        assertTrue(preparedBindVariables);
    }

    /**
     * @throws Exception
     * 
     */
    public void testExecute() throws Exception {
        String sql = "update aaa set name = ? where id = ?";
        SqlUpdateImpl query = new SqlUpdateImpl(manager, sql, String.class,
                Integer.class) {

            @Override
            protected PreparedStatement getPreparedStatement(
                    JdbcContext jdbcContext) {
                MockPreparedStatement ps = new MockPreparedStatement(null, null) {

                    @Override
                    public int executeUpdate() throws SQLException {
                        return 1;
                    }
                };
                return ps;
            }

        };
        assertEquals(1, query.params("hoge", 1).execute());
        SqlLog sqlLog = SqlLogRegistryLocator.getInstance().getLast();
        assertEquals("update aaa set name = 'hoge' where id = 1", sqlLog
                .getCompleteSql());

        try {
            query.execute();
            fail();
        } catch (QueryTwiceExecutionRuntimeException expected) {
        }
    }

    /**
     * @throws Exception
     * 
     */
    public void testExecute_entityExists() throws Exception {
        String sql = "insert into aaa (name) values (?)";
        SqlUpdateImpl query = new SqlUpdateImpl(manager, sql, String.class) {

            @Override
            protected PreparedStatement getPreparedStatement(
                    JdbcContext jdbcContext) {
                MockPreparedStatement ps = new MockPreparedStatement(null, null) {

                    @Override
                    public int executeUpdate() throws SQLException {
                        throw new SQLException("hoge", "23");
                    }
                };
                return ps;
            }

        };
        try {
            query.params("hoge").params("hoge2").execute();
            fail();
        } catch (EntityExistsException expected) {
            expected.printStackTrace();
        }
    }

    /**
     * @throws Exception
     * 
     */
    public void testExecute_illegalParamSize() throws Exception {
        String sql = "update aaa set name = ? where id = ?";
        SqlUpdateImpl query = new SqlUpdateImpl(manager, sql, String.class,
                Integer.class);
        try {
            query.params("hoge").execute();
            fail();
        } catch (IllegalParamSizeRuntimeException e) {
            System.out.println(e);
            assertEquals(1, e.getParamSize());
            assertEquals(2, e.getParamClassSize());
        }
    }

}
