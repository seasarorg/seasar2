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
import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;

import junit.framework.TestCase;

import org.seasar.extension.jdbc.DbmsDialect;
import org.seasar.extension.jdbc.IterationCallback;
import org.seasar.extension.jdbc.IterationContext;
import org.seasar.extension.jdbc.JdbcContext;
import org.seasar.extension.jdbc.ResultSetHandler;
import org.seasar.extension.jdbc.SqlLogRegistry;
import org.seasar.extension.jdbc.SqlLogRegistryLocator;
import org.seasar.extension.jdbc.StatementHandler;
import org.seasar.extension.jdbc.dialect.Db2Dialect;
import org.seasar.extension.jdbc.dialect.MysqlDialect;
import org.seasar.extension.jdbc.dialect.PostgreDialect;
import org.seasar.extension.jdbc.dialect.StandardDialect;
import org.seasar.extension.jdbc.dto.AaaDto;
import org.seasar.extension.jdbc.entity.Aaa;
import org.seasar.extension.jdbc.handler.BeanIterationResultSetHandler;
import org.seasar.extension.jdbc.handler.BeanListResultSetHandler;
import org.seasar.extension.jdbc.handler.BeanResultSetHandler;
import org.seasar.extension.jdbc.manager.JdbcContextImpl;
import org.seasar.extension.jdbc.manager.JdbcManagerImpl;
import org.seasar.extension.jdbc.manager.JdbcManagerImplementor;
import org.seasar.extension.jta.TransactionManagerImpl;
import org.seasar.extension.jta.TransactionSynchronizationRegistryImpl;
import org.seasar.framework.convention.impl.PersistenceConventionImpl;
import org.seasar.framework.exception.SQLRuntimeException;
import org.seasar.framework.mock.sql.MockColumnMetaData;
import org.seasar.framework.mock.sql.MockDataSource;
import org.seasar.framework.mock.sql.MockPreparedStatement;
import org.seasar.framework.mock.sql.MockResultSet;
import org.seasar.framework.mock.sql.MockResultSetMetaData;
import org.seasar.framework.util.ArrayMap;

/**
 * @author higa
 * 
 */
public class AbsSelectTest extends TestCase {

    private JdbcManagerImpl manager;

    private JdbcContextImpl jdbcContext;

    @Override
    protected void setUp() throws Exception {
        manager = new JdbcManagerImpl() {

            @Override
            public JdbcContext getJdbcContext() {
                jdbcContext = (JdbcContextImpl) super.getJdbcContext();
                return jdbcContext;
            }

        };
        manager.setSyncRegistry(new TransactionSynchronizationRegistryImpl(
                new TransactionManagerImpl()));
        manager.setDataSource(new MockDataSource());
        manager.setDialect(new StandardDialect());
        manager.setPersistenceConvention(new PersistenceConventionImpl());

    }

    @Override
    protected void tearDown() throws Exception {
        SqlLogRegistry regisry = SqlLogRegistryLocator.getInstance();
        regisry.clear();
    }

    /**
     * @throws Exception
     * 
     */
    public void testMaxRows() throws Exception {
        MySelect<Aaa> query = new MySelect<Aaa>(manager, Aaa.class);
        assertSame(query, query.maxRows(100));
        assertEquals(100, query.maxRows);
    }

    /**
     * @throws Exception
     * 
     */
    public void testFetchSize() throws Exception {
        MySelect<Aaa> query = new MySelect<Aaa>(manager, Aaa.class);
        assertSame(query, query.fetchSize(10));
        assertEquals(10, query.fetchSize);
    }

    /**
     * @throws Exception
     * 
     */
    public void testLimit() throws Exception {
        MySelect<Aaa> query = new MySelect<Aaa>(manager, Aaa.class);
        assertSame(query, query.limit(9));
        assertEquals(9, query.limit);
    }

    /**
     * @throws Exception
     * 
     */
    public void testOffset() throws Exception {
        MySelect<Aaa> query = new MySelect<Aaa>(manager, Aaa.class);
        assertSame(query, query.offset(8));
        assertEquals(8, query.offset);
    }

    /**
     * @throws Exception
     * 
     */
    public void testDisallowNoResult() throws Exception {
        MySelect<Aaa> query = new MySelect<Aaa>(manager, Aaa.class);
        assertSame(query, query.disallowNoResult());
        assertTrue(query.disallowNoResult);
    }

    /**
     * @throws Exception
     * 
     */
    public void testSetupPreparedStatement() throws Exception {
        String sql = "select * from aaa where id = ?";
        MySelect<Aaa> query = new MySelect<Aaa>(manager, Aaa.class);
        query.fetchSize = 10;
        query.maxRows = 20;
        query.queryTimeout = 30;
        query.executedSql = sql;
        query.addParam("aaa");
        MockPreparedStatement ps = new MockPreparedStatement(null, sql);
        query.setupPreparedStatement(ps);
        assertEquals(ResultSet.TYPE_FORWARD_ONLY, ps.getResultSetType());
        assertEquals(10, ps.getFetchSize());
        assertEquals(20, ps.getMaxRows());
        assertEquals(30, ps.getQueryTimeout());
    }

    /**
     * @throws Exception
     * 
     */
    public void testGetPreparedStatement() throws Exception {
        String sql = "select * from aaa where id = ?";
        MySelect<Aaa> query = new MySelect<Aaa>(manager, Aaa.class);
        query.fetchSize = 10;
        query.maxRows = 20;
        query.queryTimeout = 30;
        query.executedSql = sql;
        JdbcContext jdbcContext = manager.getJdbcContext();
        query.processPreparedStatement(jdbcContext,
                new StatementHandler<Object, PreparedStatement>() {

                    public Object handle(PreparedStatement ps) {
                        try {
                            assertNotNull(ps);
                            assertEquals(ResultSet.TYPE_FORWARD_ONLY, ps
                                    .getResultSetType());
                            assertEquals(10, ps.getFetchSize());
                            assertEquals(20, ps.getMaxRows());
                            assertEquals(30, ps.getQueryTimeout());
                        } catch (SQLException e) {
                            throw new SQLRuntimeException(e);
                        }
                        return null;
                    }
                });
    }

    /**
     * @throws Exception
     * 
     */
    public void testGetCursorPreparedStatement() throws Exception {
        String sql = "select * from aaa where id = ?";
        MySelect<Aaa> query = new MySelect<Aaa>(manager, Aaa.class);
        query.fetchSize = 10;
        query.maxRows = 20;
        query.queryTimeout = 30;
        query.executedSql = sql;
        JdbcContext jdbcContext = manager.getJdbcContext();
        query.processCursorPreparedStatement(jdbcContext,
                new StatementHandler<Object, PreparedStatement>() {

                    public Object handle(PreparedStatement ps) {
                        try {
                            assertEquals(ResultSet.TYPE_SCROLL_INSENSITIVE, ps
                                    .getResultSetType());
                            assertEquals(10, ps.getFetchSize());
                            assertEquals(20, ps.getMaxRows());
                            assertEquals(30, ps.getQueryTimeout());
                        } catch (SQLException e) {
                            throw new SQLRuntimeException(e);
                        }
                        return null;
                    }
                });
    }

    /**
     * @throws Exception
     * 
     */
    public void testCreateResultSet_noPaging() throws Exception {
        MySelect<Aaa> query = new MySelect<Aaa>(manager, Aaa.class);
        JdbcContext jdbcContext = manager.getJdbcContext();
        query.processResultSet(jdbcContext, new ResultSetHandler() {

            public Object handle(ResultSet rs) throws SQLException {
                assertEquals(ResultSet.TYPE_FORWARD_ONLY, rs.getType());
                assertEquals(0, rs.getRow());
                return null;
            }

        });
    }

    /**
     * @throws Exception
     * 
     */
    public void testCreateResultSet_limit_supportOffset() throws Exception {
        manager.setDialect(new PostgreDialect());
        MySelect<Aaa> query = new MySelect<Aaa>(manager, Aaa.class);
        query.offset = 5;
        query.limit = 10;
        JdbcContext jdbcContext = manager.getJdbcContext();
        query.processResultSet(jdbcContext, new ResultSetHandler() {

            public Object handle(ResultSet rs) throws SQLException {
                assertEquals(ResultSet.TYPE_FORWARD_ONLY, rs.getType());
                assertEquals(0, rs.getRow());
                return null;
            }

        });
    }

    /**
     * @throws Exception
     * 
     */
    public void testCreateResultSet_limit_supportOffset_offsetOnly()
            throws Exception {
        manager.setDialect(new PostgreDialect());
        MySelect<Aaa> query = new MySelect<Aaa>(manager, Aaa.class);
        query.offset = 5;
        JdbcContext jdbcContext = manager.getJdbcContext();
        query.processResultSet(jdbcContext, new ResultSetHandler() {

            public Object handle(ResultSet rs) throws SQLException {
                assertEquals(ResultSet.TYPE_FORWARD_ONLY, rs.getType());
                assertEquals(0, rs.getRow());
                return null;
            }

        });
    }

    /**
     * @throws Exception
     * 
     */
    public void testCreateResultSet_limit_supportOffset_offsetOnly_notSupportOffsetWithoutLimit()
            throws Exception {
        manager.setDialect(new MysqlDialect());
        final MySelect<Aaa> query = new MySelect<Aaa>(manager, Aaa.class) {

            MockResultSet rs = new MockResultSet();

            @Override
            protected Object processCursorPreparedStatement(
                    final JdbcContext jdbcContext,
                    final StatementHandler<Object, PreparedStatement> handler) {
                MockPreparedStatement ps = new MockPreparedStatement(null, null) {

                    @Override
                    public ResultSet executeQuery() throws SQLException {
                        rs.addRowData(new ArrayMap());
                        rs.addRowData(new ArrayMap());
                        rs.setType(ResultSet.TYPE_SCROLL_INSENSITIVE);
                        return rs;
                    }

                };
                try {
                    rs.setFetchSize(fetchSize);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return handler.handle(ps);
            }

        };
        query.offset = 1;
        JdbcContext jdbcContext = manager.getJdbcContext();
        query.processResultSet(jdbcContext, new ResultSetHandler() {

            public Object handle(ResultSet rs) throws SQLException {
                assertEquals(ResultSet.TYPE_SCROLL_INSENSITIVE, rs.getType());
                assertEquals(1, rs.getRow());
                return null;
            }
        });
    }

    /**
     * @throws Exception
     * 
     */
    public void testCreateResultSet_notSupportOffset_cursorSupport()
            throws Exception {
        final MySelect<Aaa> query = new MySelect<Aaa>(manager, Aaa.class) {

            MockResultSet rs = new MockResultSet();

            @Override
            protected Object processCursorPreparedStatement(
                    final JdbcContext jdbcContext,
                    final StatementHandler<Object, PreparedStatement> handler) {
                MockPreparedStatement ps = new MockPreparedStatement(null, null) {

                    @Override
                    public ResultSet executeQuery() throws SQLException {
                        rs.addRowData(new ArrayMap());
                        rs.addRowData(new ArrayMap());
                        rs.setType(ResultSet.TYPE_SCROLL_INSENSITIVE);
                        return rs;
                    }

                };
                try {
                    rs.setFetchSize(fetchSize);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return handler.handle(ps);
            }

        };
        query.offset = 1;
        query.limit = 10;
        JdbcContext jdbcContext = manager.getJdbcContext();
        query.processResultSet(jdbcContext, new ResultSetHandler() {

            public Object handle(ResultSet rs) throws SQLException {
                assertEquals(ResultSet.TYPE_SCROLL_INSENSITIVE, rs.getType());
                assertEquals(1, rs.getRow());
                return null;
            }
        });
    }

    /**
     * 
     */
    @SuppressWarnings("unchecked")
    public void testGetResultListInternal() {
        MySelect<AaaDto> query = new MySelect<AaaDto>(manager, AaaDto.class) {

            @Override
            protected ResultSetHandler createResultListResultSetHandler() {
                DbmsDialect dialect = jdbcManager.getDialect();
                return new BeanListResultSetHandler(baseClass, dialect, manager
                        .getPersistenceConvention(), "select * from aaa");
            }

            @Override
            protected Object processResultSet(final JdbcContext jdbcContext,
                    final ResultSetHandler handler) {
                try {
                    MockResultSetMetaData rsMeta = new MockResultSetMetaData();
                    MockColumnMetaData columnMeta = new MockColumnMetaData();
                    columnMeta.setColumnLabel("FOO2");
                    rsMeta.addColumnMetaData(columnMeta);
                    columnMeta = new MockColumnMetaData();
                    columnMeta.setColumnLabel("AAA_BBB");
                    rsMeta.addColumnMetaData(columnMeta);
                    MockResultSet rs = new MockResultSet(rsMeta);
                    ArrayMap data = new ArrayMap();
                    data.put("FOO2", "111");
                    data.put("AAA_BBB", "222");
                    rs.addRowData(data);
                    return handler.handle(rs);
                } catch (SQLException e) {
                    throw new SQLRuntimeException(e);
                }
            }

        };
        List<AaaDto> ret = query.getResultListInternal();
        assertEquals(1, ret.size());
        AaaDto dto = ret.get(0);
        assertEquals("111", dto.foo);
        assertEquals("222", dto.aaaBbb);
        assertTrue(jdbcContext.idDestroyed());
    }

    /**
     * 
     */
    @SuppressWarnings("unchecked")
    public void testGetResultListInternal_noResult() {
        MySelect<AaaDto> query = new MySelect<AaaDto>(manager, AaaDto.class) {

            @Override
            protected ResultSetHandler createResultListResultSetHandler() {
                DbmsDialect dialect = jdbcManager.getDialect();
                return new BeanListResultSetHandler(baseClass, dialect, manager
                        .getPersistenceConvention(), "select * from aaa");
            }

            @Override
            protected Object processResultSet(final JdbcContext jdbcContext,
                    final ResultSetHandler handler) {
                try {
                    MockResultSetMetaData rsMeta = new MockResultSetMetaData();
                    MockColumnMetaData columnMeta = new MockColumnMetaData();
                    columnMeta.setColumnLabel("FOO2");
                    rsMeta.addColumnMetaData(columnMeta);
                    columnMeta = new MockColumnMetaData();
                    columnMeta.setColumnLabel("AAA_BBB");
                    rsMeta.addColumnMetaData(columnMeta);
                    MockResultSet rs = new MockResultSet(rsMeta);
                    return handler.handle(rs);
                } catch (SQLException e) {
                    throw new SQLRuntimeException(e);
                }
            }

        };
        List<AaaDto> ret = query.getResultListInternal();
        assertTrue(ret.isEmpty());
    }

    /**
     * 
     */
    @SuppressWarnings("unchecked")
    public void testGetResultListInternal_disallowNoResult() {
        MySelect<AaaDto> query = new MySelect<AaaDto>(manager, AaaDto.class) {

            @Override
            protected ResultSetHandler createResultListResultSetHandler() {
                DbmsDialect dialect = jdbcManager.getDialect();
                return new BeanListResultSetHandler(baseClass, dialect, manager
                        .getPersistenceConvention(), "select * from aaa");
            }

            @Override
            protected Object processResultSet(final JdbcContext jdbcContext,
                    final ResultSetHandler handler) {
                try {
                    MockResultSetMetaData rsMeta = new MockResultSetMetaData();
                    MockColumnMetaData columnMeta = new MockColumnMetaData();
                    columnMeta.setColumnLabel("FOO2");
                    rsMeta.addColumnMetaData(columnMeta);
                    columnMeta = new MockColumnMetaData();
                    columnMeta.setColumnLabel("AAA_BBB");
                    rsMeta.addColumnMetaData(columnMeta);
                    MockResultSet rs = new MockResultSet(rsMeta);
                    return handler.handle(rs);
                } catch (SQLException e) {
                    throw new SQLRuntimeException(e);
                }
            }

        };
        try {
            query.disallowNoResult().getResultListInternal();
            fail();
        } catch (NoResultException expected) {
        }
    }

    /**
     * 
     */
    public void testGetSingleResultInternal() {
        MySelect<AaaDto> query = new MySelect<AaaDto>(manager, AaaDto.class) {

            @Override
            protected ResultSetHandler createSingleResultResultSetHandler() {
                DbmsDialect dialect = jdbcManager.getDialect();
                return new BeanResultSetHandler(baseClass, dialect, manager
                        .getPersistenceConvention(), "select * from aaa");
            }

            @Override
            protected Object processResultSet(final JdbcContext jdbcContext,
                    final ResultSetHandler handler) {
                try {
                    MockResultSetMetaData rsMeta = new MockResultSetMetaData();
                    MockColumnMetaData columnMeta = new MockColumnMetaData();
                    columnMeta.setColumnLabel("FOO2");
                    rsMeta.addColumnMetaData(columnMeta);
                    columnMeta = new MockColumnMetaData();
                    columnMeta.setColumnLabel("AAA_BBB");
                    rsMeta.addColumnMetaData(columnMeta);
                    MockResultSet rs = new MockResultSet(rsMeta);
                    ArrayMap data = new ArrayMap();
                    data.put("FOO2", "111");
                    data.put("AAA_BBB", "222");
                    rs.addRowData(data);
                    return handler.handle(rs);
                } catch (SQLException e) {
                    throw new SQLRuntimeException(e);
                }
            }

        };
        AaaDto dto = query.getSingleResultInternal();
        assertEquals("111", dto.foo);
        assertEquals("222", dto.aaaBbb);
        assertTrue(jdbcContext.idDestroyed());
    }

    /**
     * 
     */
    public void testGetSingleResultInternal_noResult() {
        MySelect<AaaDto> query = new MySelect<AaaDto>(manager, AaaDto.class) {

            @Override
            protected ResultSetHandler createSingleResultResultSetHandler() {
                DbmsDialect dialect = jdbcManager.getDialect();
                return new BeanResultSetHandler(baseClass, dialect, manager
                        .getPersistenceConvention(), "select * from aaa");
            }

            @Override
            protected Object processResultSet(final JdbcContext jdbcContext,
                    final ResultSetHandler handler) {
                try {
                    MockResultSetMetaData rsMeta = new MockResultSetMetaData();
                    MockColumnMetaData columnMeta = new MockColumnMetaData();
                    columnMeta.setColumnLabel("FOO2");
                    rsMeta.addColumnMetaData(columnMeta);
                    columnMeta = new MockColumnMetaData();
                    columnMeta.setColumnLabel("AAA_BBB");
                    rsMeta.addColumnMetaData(columnMeta);
                    MockResultSet rs = new MockResultSet(rsMeta);
                    return handler.handle(rs);
                } catch (SQLException e) {
                    throw new SQLRuntimeException(e);
                }
            }

        };
        AaaDto dto = query.getSingleResultInternal();
        assertNull(dto);
    }

    /**
     * 
     */
    public void testGetSingleResultInternal_disallowNoResult() {
        MySelect<AaaDto> query = new MySelect<AaaDto>(manager, AaaDto.class) {

            @Override
            protected ResultSetHandler createSingleResultResultSetHandler() {
                DbmsDialect dialect = jdbcManager.getDialect();
                return new BeanResultSetHandler(baseClass, dialect, manager
                        .getPersistenceConvention(), "select * from aaa");
            }

            @Override
            protected Object processResultSet(final JdbcContext jdbcContext,
                    final ResultSetHandler handler) {
                try {
                    MockResultSetMetaData rsMeta = new MockResultSetMetaData();
                    MockColumnMetaData columnMeta = new MockColumnMetaData();
                    columnMeta.setColumnLabel("FOO2");
                    rsMeta.addColumnMetaData(columnMeta);
                    columnMeta = new MockColumnMetaData();
                    columnMeta.setColumnLabel("AAA_BBB");
                    rsMeta.addColumnMetaData(columnMeta);
                    MockResultSet rs = new MockResultSet(rsMeta);
                    return handler.handle(rs);
                } catch (SQLException e) {
                    throw new SQLRuntimeException(e);
                }
            }

        };
        try {
            query.disallowNoResult().getSingleResultInternal();
            fail();
        } catch (NoResultException expected) {
        }
    }

    /**
     * 
     */
    @SuppressWarnings("unchecked")
    public void testIterateInternal() {
        final List<AaaDto> list = new ArrayList<AaaDto>();
        MySelect<AaaDto> query = new MySelect<AaaDto>(manager, AaaDto.class) {

            @Override
            protected ResultSetHandler createIterateResultSetHandler(
                    IterationCallback callback) {
                DbmsDialect dialect = jdbcManager.getDialect();
                return new BeanIterationResultSetHandler(baseClass, dialect,
                        manager.getPersistenceConvention(),
                        "select * from aaa", 0, callback);
            }

            @Override
            protected Object processResultSet(final JdbcContext jdbcContext,
                    final ResultSetHandler handler) {
                try {
                    MockResultSetMetaData rsMeta = new MockResultSetMetaData();
                    MockColumnMetaData columnMeta = new MockColumnMetaData();
                    columnMeta.setColumnLabel("FOO2");
                    rsMeta.addColumnMetaData(columnMeta);
                    columnMeta = new MockColumnMetaData();
                    columnMeta.setColumnLabel("AAA_BBB");
                    rsMeta.addColumnMetaData(columnMeta);
                    MockResultSet rs = new MockResultSet(rsMeta);
                    ArrayMap data = new ArrayMap();
                    data.put("FOO2", "111");
                    data.put("AAA_BBB", "222");
                    rs.addRowData(data);
                    return handler.handle(rs);
                } catch (SQLException e) {
                    throw new SQLRuntimeException(e);
                }
            }

        };
        query.iterateInternal(new IterationCallback<AaaDto, Integer>() {

            public Integer iterate(AaaDto entity, IterationContext context) {
                list.add(entity);
                return null;
            }
        });
        assertEquals(1, list.size());
        AaaDto dto = list.get(0);
        assertEquals("111", dto.foo);
        assertEquals("222", dto.aaaBbb);
        assertTrue(jdbcContext.idDestroyed());
    }

    /**
     * 
     */
    @SuppressWarnings("unchecked")
    public void testIterateListInternal_noResult() {
        final List<AaaDto> list = new ArrayList<AaaDto>();
        MySelect<AaaDto> query = new MySelect<AaaDto>(manager, AaaDto.class) {

            @Override
            protected ResultSetHandler createIterateResultSetHandler(
                    IterationCallback callback) {
                DbmsDialect dialect = jdbcManager.getDialect();
                return new BeanIterationResultSetHandler(baseClass, dialect,
                        manager.getPersistenceConvention(),
                        "select * from aaa", 0, callback);
            }

            @Override
            protected Object processResultSet(final JdbcContext jdbcContext,
                    final ResultSetHandler handler) {
                try {
                    MockResultSetMetaData rsMeta = new MockResultSetMetaData();
                    MockColumnMetaData columnMeta = new MockColumnMetaData();
                    columnMeta.setColumnLabel("FOO2");
                    rsMeta.addColumnMetaData(columnMeta);
                    columnMeta = new MockColumnMetaData();
                    columnMeta.setColumnLabel("AAA_BBB");
                    rsMeta.addColumnMetaData(columnMeta);
                    MockResultSet rs = new MockResultSet(rsMeta);
                    return handler.handle(rs);
                } catch (SQLException e) {
                    throw new SQLRuntimeException(e);
                }
            }

        };
        query.iterateInternal(new IterationCallback<AaaDto, Integer>() {

            public Integer iterate(AaaDto entity, IterationContext context) {
                list.add(entity);
                return null;
            }
        });
        assertTrue(list.isEmpty());
    }

    /**
     * 
     */
    public void testConvertLimitSql() {
        manager.setDialect(new PostgreDialect());
        MySelect<Aaa> query = new MySelect<Aaa>(manager, Aaa.class);
        query.limit = 10;
        assertEquals("select * from aaa limit 10", query
                .convertLimitSql("select * from aaa"));
    }

    /**
     * 
     */
    public void testConvertLimitSql_limitOffsetZero_db2() {
        manager.setDialect(new Db2Dialect());
        MySelect<Aaa> query = new MySelect<Aaa>(manager, Aaa.class);
        assertEquals("select * from aaa", query
                .convertLimitSql("select * from aaa"));
    }

    /**
     * 
     */
    public void testConvertSql_supportOffsetWithoutLimit() {
        manager.setDialect(new PostgreDialect());
        MySelect<Aaa> query = new MySelect<Aaa>(manager, Aaa.class);
        query.offset = 5;
        assertEquals("select * from aaa offset 5", query
                .convertLimitSql("select * from aaa"));
    }

    /**
     * 
     */
    public void testConvertSql_notSupportLimit() {
        MySelect<Aaa> query = new MySelect<Aaa>(manager, Aaa.class);
        query.limit = 10;
        query.offset = 5;
        assertEquals("select * from aaa", query
                .convertLimitSql("select * from aaa"));
    }

    private static class MySelect<T> extends AbstractSelect<T, MySelect<T>> {

        /**
         * @param jdbcManager
         * @param baseClass
         * 
         */
        public MySelect(JdbcManagerImplementor jdbcManager, Class<T> baseClass) {
            super(jdbcManager, baseClass);
        }

        @Override
        protected ResultSetHandler createResultListResultSetHandler() {
            return null;
        }

        @Override
        protected ResultSetHandler createSingleResultResultSetHandler() {
            return null;
        }

        @Override
        protected ResultSetHandler createIterateResultSetHandler(
                IterationCallback<T, ?> callback) {
            return null;
        }

        @Override
        protected void prepare(String methodName) {
        }
    }
}
