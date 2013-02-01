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
public class SqlBatchUpdateImplTest extends TestCase {

    private JdbcManagerImpl manager;

    private int addedBatch;

    private int executedBatch;

    private boolean preparedBindVariables;

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
        SqlBatchUpdateImpl query = new SqlBatchUpdateImpl(manager,
                "update aaa set name = ? where id = ?", String.class,
                Integer.class);
        assertSame(query, query.callerClass(getClass()));
        assertEquals(getClass(), query.callerClass);
    }

    /**
     * 
     */
    public void testCallerMethodName() {
        SqlBatchUpdateImpl query = new SqlBatchUpdateImpl(manager,
                "update aaa set name = ? where id = ?", String.class,
                Integer.class);
        assertSame(query, query.callerMethodName("hoge"));
        assertEquals("hoge", query.callerMethodName);
    }

    /**
     * 
     */
    public void testQueryTimeout() {
        SqlBatchUpdateImpl query = new SqlBatchUpdateImpl(manager,
                "update aaa set name = ? where id = ?", String.class,
                Integer.class);
        assertSame(query, query.queryTimeout(100));
        assertEquals(100, query.queryTimeout);
    }

    /**
     * 
     */
    public void testParams() {
        SqlBatchUpdateImpl query = new SqlBatchUpdateImpl(manager,
                "update aaa set name = ? where id = ?", String.class,
                Integer.class);
        query.params("hoge", 1).params("hoge2", 2);
        assertEquals(2, query.paramsList.size());
        Object[] params = query.paramsList.get(0);
        assertEquals("hoge", params[0]);
        assertEquals(1, params[1]);
        params = query.paramsList.get(1);
        assertEquals("hoge2", params[0]);
        assertEquals(2, params[1]);
    }

    /**
     * 
     * @throws Exception
     */
    public void testParams_lob() throws Exception {
        SqlBatchUpdateImpl query = new SqlBatchUpdateImpl(manager,
                "update aaa set bbb = ? where ccc = ? and ddd = ?",
                String.class, String.class, byte[].class) {

            @Override
            protected PreparedStatement getPreparedStatement(
                    JdbcContext jdbcContext) {
                return new MockPreparedStatement(null, null) {

                    @Override
                    public int[] executeBatch() throws SQLException {

                        return new int[] { 1 };
                    }
                };
            }

            @Override
            protected void resetParams() {
                assertEquals(3, getParamSize());
                assertEquals(ValueTypes.CLOB, getParam(0).valueType);
                assertEquals(ValueTypes.CLOB, getParam(1).valueType);
                assertEquals(ValueTypes.BLOB, getParam(2).valueType);
                super.resetParams();
            }
        };
        query.params(lob((String) null), lob("foo"), lob(new byte[] {}))
                .params(lob((String) null), lob("foo2"), lob(new byte[] {}))
                .execute();
    }

    /**
     * 
     * @throws Exception
     */
    public void testParams_valueType() throws Exception {
        SqlBatchUpdateImpl query = new SqlBatchUpdateImpl(manager,
                "update aaa set bbb = ? where ccc = ? and ddd = ?",
                String.class, String.class, Date.class) {

            @Override
            protected PreparedStatement getPreparedStatement(
                    JdbcContext jdbcContext) {
                return new MockPreparedStatement(null, null) {

                    @Override
                    public int[] executeBatch() throws SQLException {

                        return new int[] { 1 };
                    }
                };
            }

            @Override
            protected void resetParams() {
                assertEquals(3, getParamSize());
                assertEquals(ValueTypes.CALENDAR_TIME, getParam(0).valueType);
                assertEquals(ValueTypes.DATE_SQLDATE, getParam(1).valueType);
                assertEquals(ValueTypes.DATE_TIME, getParam(2).valueType);
                super.resetParams();
            }
        };
        query.params(time((Calendar) null), date(new Date()), time(new Date()))
                .params(time((Calendar) null), date(new Date()),
                        time(new Date())).execute();
    }

    /**
     * @throws Exception
     * 
     */
    public void testGetPreparedStatement() throws Exception {
        SqlBatchUpdateImpl query = new SqlBatchUpdateImpl(manager,
                "update aaa set name = ? where id = ?", String.class,
                Integer.class);
        query.queryTimeout = 30;
        JdbcContext jdbcContext = manager.getJdbcContext();
        PreparedStatement ps = query.getPreparedStatement(jdbcContext);
        assertEquals(ResultSet.TYPE_FORWARD_ONLY, ps.getResultSetType());
        assertEquals(30, ps.getQueryTimeout());
    }

    /**
     * @throws Exception
     * 
     */
    public void testExecuteBatch() throws Exception {
        String sql = "update aaa set name = ? where id = ?";
        SqlBatchUpdateImpl query = new SqlBatchUpdateImpl(manager, sql,
                String.class, Integer.class) {

            @Override
            protected PreparedStatement getPreparedStatement(
                    JdbcContext jdbcContext) {
                MockPreparedStatement ps = new MockPreparedStatement(null, null) {

                    @Override
                    public void addBatch() throws SQLException {
                        ++addedBatch;
                        super.addBatch();
                    }

                    @Override
                    public int[] executeBatch() throws SQLException {
                        ++executedBatch;
                        return new int[] { 1, 1 };
                    }
                };
                return ps;
            }

            @Override
            protected void prepareInParams(PreparedStatement ps) {
                preparedBindVariables = true;
                super.prepareInParams(ps);
            }

        };
        int[] ret = query.params("hoge", 1).params("hoge2", 2).execute();
        assertEquals(2, ret.length);
        assertEquals(1, executedBatch);
        assertEquals(2, addedBatch);
        assertTrue(preparedBindVariables);
        SqlLogRegistry registry = SqlLogRegistryLocator.getInstance();
        assertEquals(2, registry.getSize());
        SqlLog sqlLog = registry.get(0);
        assertEquals("update aaa set name = 'hoge' where id = 1", sqlLog
                .getCompleteSql());
        sqlLog = registry.get(1);
        assertEquals("update aaa set name = 'hoge2' where id = 2", sqlLog
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
    public void testExecuteBatch_entityExists() throws Exception {
        String sql = "insert into aaa (name) values (?)";
        SqlBatchUpdateImpl query = new SqlBatchUpdateImpl(manager, sql,
                String.class) {

            @Override
            protected PreparedStatement getPreparedStatement(
                    JdbcContext jdbcContext) {
                MockPreparedStatement ps = new MockPreparedStatement(null, null) {

                    @Override
                    public int[] executeBatch() throws SQLException {
                        throw new SQLException("hoge", "23");
                    }
                };
                return ps;
            }

            @Override
            protected void prepareInParams(PreparedStatement ps) {
                preparedBindVariables = true;
                super.prepareInParams(ps);
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
    public void testExecuteBatch_batchSize1() throws Exception {
        String sql = "update aaa set name = ? where id = ?";
        SqlBatchUpdateImpl query = new SqlBatchUpdateImpl(manager, sql,
                String.class, Integer.class) {

            @Override
            protected PreparedStatement getPreparedStatement(
                    JdbcContext jdbcContext) {
                MockPreparedStatement ps = new MockPreparedStatement(null, null) {

                    @Override
                    public void addBatch() throws SQLException {
                        ++addedBatch;
                        super.addBatch();
                    }

                    @Override
                    public int[] executeBatch() throws SQLException {
                        ++executedBatch;
                        return executedBatch == 1 ? new int[] { 1, 2 }
                                : new int[] { 3 };
                    }
                };
                return ps;
            }

            @Override
            protected void prepareInParams(PreparedStatement ps) {
                preparedBindVariables = true;
                super.prepareInParams(ps);
            }

        };
        int[] ret = query.params("hoge", 1).params("hoge2", 2).params("hoge3",
                3).batchSize(2).execute();
        assertEquals(3, addedBatch);
        assertEquals(2, executedBatch);
        assertEquals(3, ret.length);
        assertEquals(1, ret[0]);
        assertEquals(2, ret[1]);
        assertEquals(3, ret[2]);
        assertTrue(preparedBindVariables);
        SqlLogRegistry registry = SqlLogRegistryLocator.getInstance();
        assertEquals(3, registry.getSize());
        SqlLog sqlLog = registry.get(0);
        assertEquals("update aaa set name = 'hoge' where id = 1", sqlLog
                .getCompleteSql());
        sqlLog = registry.get(1);
        assertEquals("update aaa set name = 'hoge2' where id = 2", sqlLog
                .getCompleteSql());
        sqlLog = registry.get(2);
        assertEquals("update aaa set name = 'hoge3' where id = 3", sqlLog
                .getCompleteSql());
    }

    /**
     * @throws Exception
     * 
     */
    public void testExecuteBatch_batchSize2() throws Exception {
        String sql = "update aaa set name = ? where id = ?";
        SqlBatchUpdateImpl query = new SqlBatchUpdateImpl(manager, sql,
                String.class, Integer.class) {

            @Override
            protected PreparedStatement getPreparedStatement(
                    JdbcContext jdbcContext) {
                MockPreparedStatement ps = new MockPreparedStatement(null, null) {

                    @Override
                    public void addBatch() throws SQLException {
                        ++addedBatch;
                        super.addBatch();
                    }

                    @Override
                    public int[] executeBatch() throws SQLException {
                        ++executedBatch;
                        return executedBatch == 1 ? new int[] { 1, 2 }
                                : new int[] { 3, 4 };
                    }
                };
                return ps;
            }

            @Override
            protected void prepareInParams(PreparedStatement ps) {
                preparedBindVariables = true;
                super.prepareInParams(ps);
            }

        };
        int[] ret = query.params("hoge", 1).params("hoge2", 2).params("hoge3",
                3).params("hoge4", 4).batchSize(2).execute();
        assertEquals(4, addedBatch);
        assertEquals(2, executedBatch);
        assertEquals(4, ret.length);
        assertEquals(1, ret[0]);
        assertEquals(2, ret[1]);
        assertEquals(3, ret[2]);
        assertEquals(4, ret[3]);
        assertTrue(preparedBindVariables);
        SqlLogRegistry registry = SqlLogRegistryLocator.getInstance();
        assertEquals(3, registry.getSize());
        SqlLog sqlLog = registry.get(0);
        assertEquals("update aaa set name = 'hoge2' where id = 2", sqlLog
                .getCompleteSql());
        sqlLog = registry.get(1);
        assertEquals("update aaa set name = 'hoge3' where id = 3", sqlLog
                .getCompleteSql());
        sqlLog = registry.get(2);
        assertEquals("update aaa set name = 'hoge4' where id = 4", sqlLog
                .getCompleteSql());
    }

    /**
     * @throws Exception
     * 
     */
    public void testExecuteBatch_illegalParamSize() throws Exception {
        String sql = "update aaa set name = ? where id = ?";
        SqlBatchUpdateImpl query = new SqlBatchUpdateImpl(manager, sql,
                String.class, Integer.class);
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
