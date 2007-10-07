/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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

import junit.framework.TestCase;

import org.seasar.extension.jdbc.JdbcContext;
import org.seasar.extension.jdbc.SqlLog;
import org.seasar.extension.jdbc.SqlLogRegistry;
import org.seasar.extension.jdbc.SqlLogRegistryLocator;
import org.seasar.extension.jdbc.dialect.StandardDialect;
import org.seasar.extension.jdbc.manager.JdbcManagerImpl;
import org.seasar.extension.jta.TransactionManagerImpl;
import org.seasar.extension.jta.TransactionSynchronizationRegistryImpl;
import org.seasar.framework.mock.sql.MockDataSource;
import org.seasar.framework.mock.sql.MockPreparedStatement;

/**
 * @author higa
 * 
 */
public class SqlBatchUpdateImplTest extends TestCase {

    private JdbcManagerImpl manager;

    private int batchSize;

    private boolean executedBatch;

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
     * @throws Exception
     * 
     */
    public void testGetPreparedStatement() throws Exception {
        SqlUpdateImpl query = new SqlUpdateImpl(manager,
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
    public void testExecute() throws Exception {
        String sql = "update aaa set name = ? where id = ?";
        SqlBatchUpdateImpl query = new SqlBatchUpdateImpl(manager, sql,
                String.class, Integer.class) {

            @Override
            protected PreparedStatement getPreparedStatement(
                    JdbcContext jdbcContext) {
                MockPreparedStatement ps = new MockPreparedStatement(null, null) {

                    @Override
                    public void addBatch() throws SQLException {
                        batchSize++;
                        super.addBatch();
                    }

                    @Override
                    public int[] executeBatch() throws SQLException {
                        executedBatch = true;
                        return new int[] { 1, 1 };
                    }
                };
                return ps;
            }

            @Override
            protected void prepareBindVariables(PreparedStatement ps) {
                preparedBindVariables = true;
                super.prepareBindVariables(ps);
            }

        };
        int[] ret = query.params("hoge", 1).params("hoge2", 2).executeBatch();
        assertEquals(2, ret.length);
        assertTrue(executedBatch);
        assertEquals(2, batchSize);
        assertTrue(preparedBindVariables);
        SqlLogRegistry registry = SqlLogRegistryLocator.getInstance();
        assertEquals(2, registry.getSize());
        SqlLog sqlLog = registry.get(0);
        assertEquals("update aaa set name = 'hoge' where id = 1", sqlLog
                .getCompleteSql());
        sqlLog = registry.get(1);
        assertEquals("update aaa set name = 'hoge2' where id = 2", sqlLog
                .getCompleteSql());
    }
}
