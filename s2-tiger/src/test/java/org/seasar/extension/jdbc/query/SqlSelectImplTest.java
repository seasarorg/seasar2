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

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import org.seasar.extension.jdbc.JdbcContext;
import org.seasar.extension.jdbc.ResultSetHandler;
import org.seasar.extension.jdbc.SqlLog;
import org.seasar.extension.jdbc.SqlLogRegistry;
import org.seasar.extension.jdbc.SqlLogRegistryLocator;
import org.seasar.extension.jdbc.dialect.PostgreDialect;
import org.seasar.extension.jdbc.dialect.StandardDialect;
import org.seasar.extension.jdbc.dto.AaaDto;
import org.seasar.extension.jdbc.entity.Aaa;
import org.seasar.extension.jdbc.exception.QueryTwiceExecutionRuntimeException;
import org.seasar.extension.jdbc.exception.SNonUniqueResultException;
import org.seasar.extension.jdbc.manager.JdbcManagerImpl;
import org.seasar.extension.jdbc.types.ValueTypes;
import org.seasar.extension.jta.TransactionManagerImpl;
import org.seasar.extension.jta.TransactionSynchronizationRegistryImpl;
import org.seasar.framework.convention.impl.PersistenceConventionImpl;
import org.seasar.framework.exception.SQLRuntimeException;
import org.seasar.framework.mock.sql.MockColumnMetaData;
import org.seasar.framework.mock.sql.MockDataSource;
import org.seasar.framework.mock.sql.MockResultSet;
import org.seasar.framework.mock.sql.MockResultSetMetaData;
import org.seasar.framework.util.ArrayMap;

import static org.seasar.extension.jdbc.parameter.Parameter.*;

/**
 * @author higa
 * 
 */
public class SqlSelectImplTest extends TestCase {

    private JdbcManagerImpl manager;

    @Override
    protected void setUp() throws Exception {
        manager = new JdbcManagerImpl();
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
        manager = null;
    }

    /**
     * 
     */
    public void testCallerClass() {
        SqlSelectImpl<Aaa> query = new SqlSelectImpl<Aaa>(manager, Aaa.class,
                "select * from aaa");
        assertSame(query, query.callerClass(getClass()));
        assertEquals(getClass(), query.callerClass);
    }

    /**
     * 
     */
    public void testCallerMethodName() {
        SqlSelectImpl<Aaa> query = new SqlSelectImpl<Aaa>(manager, Aaa.class,
                "select * from aaa");
        assertSame(query, query.callerMethodName("hoge"));
        assertEquals("hoge", query.callerMethodName);
    }

    /**
     * 
     */
    public void testMaxRows() {
        SqlSelectImpl<Aaa> query = new SqlSelectImpl<Aaa>(manager, Aaa.class,
                "select * from aaa");
        assertSame(query, query.maxRows(100));
        assertEquals(100, query.maxRows);
    }

    /**
     * 
     */
    public void testFetchSize() {
        SqlSelectImpl<Aaa> query = new SqlSelectImpl<Aaa>(manager, Aaa.class,
                "select * from aaa");
        assertSame(query, query.fetchSize(100));
        assertEquals(100, query.fetchSize);
    }

    /**
     * 
     */
    public void testQueryTimeout() {
        SqlSelectImpl<Aaa> query = new SqlSelectImpl<Aaa>(manager, Aaa.class,
                "select * from aaa");
        assertSame(query, query.queryTimeout(100));
        assertEquals(100, query.queryTimeout);
    }

    /**
     * 
     */
    public void testLimit() {
        SqlSelectImpl<Aaa> query = new SqlSelectImpl<Aaa>(manager, Aaa.class,
                "select * from aaa");
        assertSame(query, query.limit(100));
        assertEquals(100, query.limit);
    }

    /**
     * 
     */
    public void testOffset() {
        SqlSelectImpl<Aaa> query = new SqlSelectImpl<Aaa>(manager, Aaa.class,
                "select * from aaa");
        assertSame(query, query.offset(100));
        assertEquals(100, query.offset);
    }

    /**
     * 
     */
    public void testPrepare_executedSql() {
        manager.setDialect(new PostgreDialect());
        SqlSelectImpl<Aaa> query = new SqlSelectImpl<Aaa>(manager, Aaa.class,
                "select * from aaa");
        query.limit(10);
        query.prepare("getResultList");
        assertEquals("select * from aaa limit 10", query.executedSql);
    }

    /**
     * 
     */
    public void testConstructor_nullPointer() {
        try {
            new SqlSelectImpl<AaaDto>(manager, AaaDto.class,
                    "select foo2, aaa_bbb from hoge where aaa = ?",
                    (Object[]) null);
            fail();
        } catch (NullPointerException e) {
            System.out.println(e);
        }
    }

    /**
     * 
     */
    public void testPrepare_nullBindVariable2() {
        try {
            new SqlSelectImpl<AaaDto>(manager, AaaDto.class,
                    "select foo2, aaa_bbb from hoge where aaa = ?",
                    (Object) null);
            fail();
        } catch (NullPointerException e) {
            System.out.println(e);
        }
    }

    /**
     * 
     */
    public void testGetResultList() {
        SqlSelectImpl<AaaDto> query = new SqlSelectImpl<AaaDto>(manager,
                AaaDto.class, "select foo2, aaa_bbb from hoge") {

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
        List<AaaDto> ret = query.getResultList();
        assertEquals(1, ret.size());
        AaaDto dto = ret.get(0);
        assertEquals("111", dto.foo);
        assertEquals("222", dto.aaaBbb);
        SqlLog sqlLog = SqlLogRegistryLocator.getInstance().getLast();
        assertEquals("select foo2, aaa_bbb from hoge", sqlLog.getCompleteSql());

        try {
            query.getResultList();
            fail();
        } catch (QueryTwiceExecutionRuntimeException expected) {
        }
    }

    /**
     * 
     */
    public void testGetResultList_prepare() {
        SqlSelectImpl<AaaDto> query = new SqlSelectImpl<AaaDto>(manager,
                AaaDto.class, "select foo2, aaa_bbb from hoge") {

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
        query.getResultList();
        assertEquals("getResultList", query.getCallerMethodName());
    }

    /**
     * 
     */
    public void testGetResultList_paging() {
        manager.setDialect(new PostgreDialect());
        SqlSelectImpl<AaaDto> query = new SqlSelectImpl<AaaDto>(manager,
                AaaDto.class, "select foo2, aaa_bbb from hoge") {

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
        List<AaaDto> ret = query.limit(10).offset(5).getResultList();
        assertEquals(1, ret.size());
        AaaDto dto = ret.get(0);
        assertEquals("111", dto.foo);
        assertEquals("222", dto.aaaBbb);
        SqlLog sqlLog = SqlLogRegistryLocator.getInstance().getLast();
        assertEquals("select foo2, aaa_bbb from hoge limit 10 offset 5", sqlLog
                .getCompleteSql());
    }

    /**
     * 
     */
    public void testGetResultList_parameters() {
        SqlSelectImpl<AaaDto> query = new SqlSelectImpl<AaaDto>(manager,
                AaaDto.class,
                "select foo2, aaa_bbb from hoge where aaa = ? and bbb = ?",
                "111", "222") {

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
        List<AaaDto> ret = query.getResultList();
        assertEquals(1, ret.size());
        AaaDto dto = ret.get(0);
        assertEquals("111", dto.foo);
        assertEquals("222", dto.aaaBbb);
        SqlLog sqlLog = SqlLogRegistryLocator.getInstance().getLast();
        assertEquals(
                "select foo2, aaa_bbb from hoge where aaa = '111' and bbb = '222'",
                sqlLog.getCompleteSql());
    }

    /**
     * 
     */
    public void testGetResultList_parameters_paging() {
        manager.setDialect(new PostgreDialect());
        SqlSelectImpl<AaaDto> query = new SqlSelectImpl<AaaDto>(manager,
                AaaDto.class,
                "select foo2, aaa_bbb from hoge where aaa = ? and bbb = ?",
                "111", "222") {

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
        List<AaaDto> ret = query.limit(10).offset(5).getResultList();
        assertEquals(1, ret.size());
        AaaDto dto = ret.get(0);
        assertEquals("111", dto.foo);
        assertEquals("222", dto.aaaBbb);
        SqlLog sqlLog = SqlLogRegistryLocator.getInstance().getLast();
        assertEquals(
                "select foo2, aaa_bbb from hoge where aaa = '111' and bbb = '222' limit 10 offset 5",
                sqlLog.getCompleteSql());
    }

    /**
     * 
     */
    public void testGetSingleResult() {
        SqlSelectImpl<AaaDto> query = new SqlSelectImpl<AaaDto>(manager,
                AaaDto.class, "select foo2, aaa_bbb from hoge") {

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
        AaaDto dto = query.getSingleResult();
        assertNotNull(dto);
        assertEquals("111", dto.foo);
        assertEquals("222", dto.aaaBbb);
        SqlLog sqlLog = SqlLogRegistryLocator.getInstance().getLast();
        assertEquals("select foo2, aaa_bbb from hoge", sqlLog.getCompleteSql());

        try {
            query.getSingleResult();
            fail();
        } catch (QueryTwiceExecutionRuntimeException expected) {
        }
    }

    /**
     * 
     */
    public void testGetSingleResult_prepare() {
        SqlSelectImpl<AaaDto> query = new SqlSelectImpl<AaaDto>(manager,
                AaaDto.class, "select foo2, aaa_bbb from hoge") {

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
        query.getSingleResult();
        assertEquals("getSingleResult", query.getCallerMethodName());
    }

    /**
     * 
     */
    public void testGetSingleResult_paging() {
        manager.setDialect(new PostgreDialect());
        SqlSelectImpl<AaaDto> query = new SqlSelectImpl<AaaDto>(manager,
                AaaDto.class, "select foo2, aaa_bbb from hoge") {

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
        AaaDto dto = query.limit(10).offset(5).getSingleResult();
        assertNotNull(dto);
        assertEquals("111", dto.foo);
        assertEquals("222", dto.aaaBbb);
        SqlLog sqlLog = SqlLogRegistryLocator.getInstance().getLast();
        assertEquals("select foo2, aaa_bbb from hoge limit 10 offset 5", sqlLog
                .getCompleteSql());
    }

    /**
     * 
     */
    public void testGetSingleResult_simpleType() {
        SqlSelectImpl<Integer> query = new SqlSelectImpl<Integer>(manager,
                Integer.class, "select count(*) as cnt from aaa") {

            @Override
            protected Object processResultSet(final JdbcContext jdbcContext,
                    final ResultSetHandler handler) {
                try {
                    MockResultSetMetaData rsMeta = new MockResultSetMetaData();
                    MockColumnMetaData columnMeta = new MockColumnMetaData();
                    columnMeta.setColumnLabel("CNT");
                    rsMeta.addColumnMetaData(columnMeta);
                    MockResultSet rs = new MockResultSet(rsMeta);
                    ArrayMap data = new ArrayMap();
                    data.put("CNT", 5);
                    rs.addRowData(data);
                    return handler.handle(rs);
                } catch (SQLException e) {
                    throw new SQLRuntimeException(e);
                }
            }

        };
        Integer count = query.getSingleResult();
        assertEquals(5, count.intValue());
    }

    /**
     * 
     */
    public void testGetSingleResult_nodata() {
        SqlSelectImpl<AaaDto> query = new SqlSelectImpl<AaaDto>(manager,
                AaaDto.class, "select foo2, aaa_bbb from hoge") {

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
                    return handler.handle(new MockResultSet(rsMeta));
                } catch (SQLException e) {
                    throw new SQLRuntimeException(e);
                }
            }

        };
        AaaDto dto = query.getSingleResult();
        assertNull(dto);
        SqlLog sqlLog = SqlLogRegistryLocator.getInstance().getLast();
        assertEquals("select foo2, aaa_bbb from hoge", sqlLog.getCompleteSql());
    }

    /**
     * 
     */
    public void testGetSingleResult_nonunique() {
        SqlSelectImpl<AaaDto> query = new SqlSelectImpl<AaaDto>(manager,
                AaaDto.class, "select foo2, aaa_bbb from hoge where aaa = ?") {

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
                    data = new ArrayMap();
                    data.put("FOO2", "111x");
                    data.put("AAA_BBB", "222x");
                    rs.addRowData(data);
                    return handler.handle(rs);
                } catch (SQLException e) {
                    throw new SQLRuntimeException(e);
                }
            }

        };
        try {
            query.getSingleResult();
            fail();
        } catch (SNonUniqueResultException e) {
            System.out.println(e);
        }
    }

    /**
     * 
     */
    public void testGetSingleResult_parameters() {
        SqlSelectImpl<AaaDto> query = new SqlSelectImpl<AaaDto>(manager,
                AaaDto.class,
                "select foo2, aaa_bbb from hoge where aaa = ? and bbb = ?",
                "111", "222") {

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
        AaaDto dto = query.getSingleResult();
        assertNotNull(dto);
        assertEquals("111", dto.foo);
        assertEquals("222", dto.aaaBbb);
        SqlLog sqlLog = SqlLogRegistryLocator.getInstance().getLast();
        assertEquals(
                "select foo2, aaa_bbb from hoge where aaa = '111' and bbb = '222'",
                sqlLog.getCompleteSql());
    }

    /**
     * 
     */
    public void testGetSingleResult_parameters_paging() {
        manager.setDialect(new PostgreDialect());
        SqlSelectImpl<AaaDto> query = new SqlSelectImpl<AaaDto>(manager,
                AaaDto.class,
                "select foo2, aaa_bbb from hoge where aaa = ? and bbb = ?",
                "111", "222") {

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
        AaaDto dto = query.limit(10).offset(5).getSingleResult();
        assertNotNull(dto);
        assertEquals("111", dto.foo);
        assertEquals("222", dto.aaaBbb);
        SqlLog sqlLog = SqlLogRegistryLocator.getInstance().getLast();
        assertEquals(
                "select foo2, aaa_bbb from hoge where aaa = '111' and bbb = '222' limit 10 offset 5",
                sqlLog.getCompleteSql());
    }

    /**
     * 
     */
    public void testPrepareSql() {
        SqlSelectImpl<Aaa> query = new SqlSelectImpl<Aaa>(manager, Aaa.class,
                "select * from aaa");
        query.prepare("getResultList");
        query.prepareSql();
        assertNotNull(query.executedSql);
    }

    /**
     * 
     */
    public void testPrepareSql_getCount() {
        SqlSelectImpl<Long> query = new SqlSelectImpl<Long>(manager,
                Long.class, "select * from aaa");
        query.count = true;
        query.prepare("getResultList");
        query.prepareSql();
        assertEquals("select count(*) from ( select * from aaa ) COUNT_",
                query.executedSql);
    }

    /**
     * 
     * @throws Exception
     */
    public void testParams_valueType() throws Exception {
        SqlSelectImpl<Aaa> query = new SqlSelectImpl<Aaa>(manager, Aaa.class,
                "select * from aaa where bbb = ? and ccc = ? and ddd = ?",
                "hoge", lob("foo"), time(new Date()));
        assertEquals(3, query.getParamSize());
        assertEquals(ValueTypes.STRING, query.getParam(0).valueType);
        assertEquals(ValueTypes.CLOB, query.getParam(1).valueType);
        assertEquals(ValueTypes.DATE_TIME, query.getParam(2).valueType);
    }
}
