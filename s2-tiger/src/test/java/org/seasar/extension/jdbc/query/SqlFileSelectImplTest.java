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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import junit.framework.TestCase;

import org.seasar.extension.jdbc.JdbcContext;
import org.seasar.extension.jdbc.ResultSetHandler;
import org.seasar.extension.jdbc.SqlLog;
import org.seasar.extension.jdbc.SqlLogRegistry;
import org.seasar.extension.jdbc.SqlLogRegistryLocator;
import org.seasar.extension.jdbc.dialect.PostgreDialect;
import org.seasar.extension.jdbc.dialect.StandardDialect;
import org.seasar.extension.jdbc.entity.Aaa;
import org.seasar.extension.jdbc.exception.QueryTwiceExecutionRuntimeException;
import org.seasar.extension.jdbc.manager.JdbcManagerImpl;
import org.seasar.extension.jdbc.parameter.Parameter;
import org.seasar.extension.jdbc.types.ValueTypes;
import org.seasar.extension.jta.TransactionManagerImpl;
import org.seasar.extension.jta.TransactionSynchronizationRegistryImpl;
import org.seasar.extension.sql.cache.NodeCache;
import org.seasar.framework.convention.impl.PersistenceConventionImpl;
import org.seasar.framework.exception.ResourceNotFoundRuntimeException;
import org.seasar.framework.exception.SQLRuntimeException;
import org.seasar.framework.mock.sql.MockColumnMetaData;
import org.seasar.framework.mock.sql.MockDataSource;
import org.seasar.framework.mock.sql.MockResultSet;
import org.seasar.framework.mock.sql.MockResultSetMetaData;
import org.seasar.framework.util.ArrayMap;
import org.seasar.framework.util.tiger.CollectionsUtil;

import static org.seasar.extension.jdbc.parameter.Parameter.*;

/**
 * @author higa
 * 
 */
public class SqlFileSelectImplTest extends TestCase {

    private static final String PATH = SqlFileSelectImplTest.class.getName()
            + "_select";

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
        NodeCache.clear();
        manager = null;
    }

    /**
     * 
     */
    public void testCallerClass() {
        SqlFileSelectImpl<Aaa> query = new SqlFileSelectImpl<Aaa>(manager,
                Aaa.class, "aaa.sql");
        assertSame(query, query.callerClass(getClass()));
        assertEquals(getClass(), query.callerClass);
    }

    /**
     * 
     */
    public void testCallerMethodName() {
        SqlFileSelectImpl<Aaa> query = new SqlFileSelectImpl<Aaa>(manager,
                Aaa.class, "aaa.sql");
        assertSame(query, query.callerMethodName("hoge"));
        assertEquals("hoge", query.callerMethodName);
    }

    /**
     * 
     */
    public void testMaxRows() {
        SqlFileSelectImpl<Aaa> query = new SqlFileSelectImpl<Aaa>(manager,
                Aaa.class, "aaa.sql");
        assertSame(query, query.maxRows(100));
        assertEquals(100, query.maxRows);
    }

    /**
     * 
     */
    public void testFetchSize() {
        SqlFileSelectImpl<Aaa> query = new SqlFileSelectImpl<Aaa>(manager,
                Aaa.class, "aaa.sql");
        assertSame(query, query.fetchSize(100));
        assertEquals(100, query.fetchSize);
    }

    /**
     * 
     */
    public void testQueryTimeout() {
        SqlFileSelectImpl<Aaa> query = new SqlFileSelectImpl<Aaa>(manager,
                Aaa.class, "aaa.sql");
        assertSame(query, query.queryTimeout(100));
        assertEquals(100, query.queryTimeout);
    }

    /**
     * 
     */
    public void testLimit() {
        SqlFileSelectImpl<Aaa> query = new SqlFileSelectImpl<Aaa>(manager,
                Aaa.class, "aaa.sql");
        assertSame(query, query.limit(100));
        assertEquals(100, query.limit);
    }

    /**
     * 
     */
    public void testOffset() {
        SqlFileSelectImpl<Aaa> query = new SqlFileSelectImpl<Aaa>(manager,
                Aaa.class, "aaa.sql");
        assertSame(query, query.offset(100));
        assertEquals(100, query.offset);
    }

    /**
     * 
     */
    public void testPrepareNode() {
        SqlFileSelectImpl<Aaa> query = new SqlFileSelectImpl<Aaa>(manager,
                Aaa.class, PATH);
        query.prepareCallerClassAndMethodName("getResultList");
        query.prepareNode();
        assertNotNull(query.node);
    }

    /**
     * 
     */
    public void testPrepareNode_resourceNotFound() {
        SqlFileSelectImpl<Aaa> query = new SqlFileSelectImpl<Aaa>(manager,
                Aaa.class, "xxx");
        query.prepareCallerClassAndMethodName("getResultList");
        try {
            query.prepareNode();
            fail();
        } catch (ResourceNotFoundRuntimeException e) {
            System.out.println(e);
            assertEquals("xxx", e.getPath());
        }
    }

    /**
     * 
     */
    public void testPrepareParameter_simpleType() {
        SqlFileSelectImpl<Aaa> query = new SqlFileSelectImpl<Aaa>(manager,
                Aaa.class, PATH, 1);
        query.prepareCallerClassAndMethodName("getResultList");
        query.prepareNode();
        query.prepareParameter();
        assertEquals("select * from aaa where id = ?", query.sqlContext
                .getSql());
        assertEquals(1, query.getParamSize());
        assertEquals(1, query.getParam(0).value);
        assertEquals(Integer.class, query.getParam(0).paramClass);
    }

    /**
     * 
     */
    public void testPrepareParameter_simpleType_clob() {
        SqlFileSelectImpl<Aaa> query = new SqlFileSelectImpl<Aaa>(manager,
                Aaa.class, PATH, lob("hoge"));
        query.prepareCallerClassAndMethodName("getResultList");
        query.prepareNode();
        query.prepareParameter();
        assertEquals("select * from aaa where id = ?", query.sqlContext
                .getSql());
        assertEquals(1, query.getParamSize());
        assertEquals("hoge", query.getParam(0).value);
        assertEquals(String.class, query.getParam(0).paramClass);
        assertEquals(ValueTypes.CLOB, query.getParam(0).valueType);
    }

    /**
     * 
     * @throws Exception
     */
    public void testPrepareParameter_simpleType_date() throws Exception {
        Date date = new SimpleDateFormat("HH:mm:ss").parse("12:11:10");
        SqlFileSelectImpl<Aaa> query = new SqlFileSelectImpl<Aaa>(manager,
                Aaa.class, PATH, time(date));
        query.prepareCallerClassAndMethodName("getResultList");
        query.prepareNode();
        query.prepareParameter();
        assertEquals("select * from aaa where id = ?", query.sqlContext
                .getSql());
        assertEquals(1, query.getParamSize());
        assertEquals(new SimpleDateFormat("HH:mm:ss").parse("12:11:10"), query
                .getParam(0).value);
        assertEquals(Date.class, query.getParam(0).paramClass);
        assertEquals(ValueTypes.DATE_TIME, query.getParam(0).valueType);
    }

    /**
     * 
     */
    public void testPrepareParameter_dto() {
        MyDto dto = new MyDto();
        dto.id = 1;
        dto.offset = 5;
        dto.limit = 10;
        SqlFileSelectImpl<Aaa> query = new SqlFileSelectImpl<Aaa>(manager,
                Aaa.class, PATH, dto);
        query.prepareCallerClassAndMethodName("getResultList");
        query.prepareNode();
        query.prepareParameter();
        assertEquals("select * from aaa where id = ?", query.sqlContext
                .getSql());
        assertEquals(1, query.getParamSize());
        assertEquals(1, query.getParam(0).value);
        assertEquals(Integer.class, query.getParam(0).paramClass);
        assertEquals(10, query.limit);
        assertEquals(5, query.offset);
    }

    /**
     * 
     */
    public void testPrepareParameter_dto_clob() {
        MyDto2 dto = new MyDto2();
        dto.largeName = "hoge";
        SqlFileSelectImpl<Aaa> query = new SqlFileSelectImpl<Aaa>(manager,
                Aaa.class, PATH, dto);
        query.prepareCallerClassAndMethodName("getResultList");
        query.prepareNode();
        query.prepareParameter();
        assertEquals("select * from aaa where id = ?", query.sqlContext
                .getSql());
        assertEquals(1, query.getParamSize());
        assertEquals("hoge", query.getParam(0).value);
        assertEquals(String.class, query.getParam(0).paramClass);
        assertEquals(ValueTypes.CLOB, query.getParam(0).valueType);
    }

    /**
     * 
     * @throws Exception
     */
    public void testPrepareParameter_dto_date() throws Exception {
        MyDto3 dto = new MyDto3();
        dto.date = new SimpleDateFormat("HH:mm:ss").parse("12:11:10");
        SqlFileSelectImpl<Aaa> query = new SqlFileSelectImpl<Aaa>(manager,
                Aaa.class, PATH, dto);
        query.prepareCallerClassAndMethodName("getResultList");
        query.prepareNode();
        query.prepareParameter();
        assertEquals("select * from aaa where id = ?", query.sqlContext
                .getSql());
        assertEquals(1, query.getParamSize());
        assertEquals(new SimpleDateFormat("HH:mm:ss").parse("12:11:10"), query
                .getParam(0).value);
        assertEquals(Date.class, query.getParam(0).paramClass);
        assertEquals(ValueTypes.DATE_TIME, query.getParam(0).valueType);
    }

    /**
     * 
     */
    public void testPrepareParameter_map() {
        Map<String, Object> map = CollectionsUtil.newHashMap();
        map.put("id", 1);
        map.put("offset", 5);
        map.put("limit", 10);
        SqlFileSelectImpl<Aaa> query = new SqlFileSelectImpl<Aaa>(manager,
                Aaa.class, PATH, map);
        query.prepareCallerClassAndMethodName("getResultList");
        query.prepareNode();
        query.prepareParameter();
        assertEquals("select * from aaa where id = ?", query.sqlContext
                .getSql());
        assertEquals(1, query.getParamSize());
        assertEquals(1, query.getParam(0).value);
        assertEquals(Integer.class, query.getParam(0).paramClass);
        assertEquals(10, query.limit);
        assertEquals(5, query.offset);
    }

    /**
     * 
     */
    public void testPrepareParameter_map_clob() {
        Map<String, Object> map = CollectionsUtil.newHashMap();
        map.put("largeName", Parameter.lob("hoge"));
        SqlFileSelectImpl<Aaa> query = new SqlFileSelectImpl<Aaa>(manager,
                Aaa.class, PATH, map);
        query.prepareCallerClassAndMethodName("getResultList");
        query.prepareNode();
        query.prepareParameter();
        assertEquals("select * from aaa where id = ?", query.sqlContext
                .getSql());
        assertEquals(1, query.getParamSize());
        assertEquals("hoge", query.getParam(0).value);
        assertEquals(String.class, query.getParam(0).paramClass);
        assertEquals(ValueTypes.CLOB, query.getParam(0).valueType);
    }

    /**
     * 
     * @throws Exception
     */
    public void testPrepareParameter_map_date() throws Exception {
        Map<String, Object> map = CollectionsUtil.newHashMap();
        map.put("date", Parameter.time(new SimpleDateFormat("HH:mm:ss")
                .parse("12:11:10")));
        SqlFileSelectImpl<Aaa> query = new SqlFileSelectImpl<Aaa>(manager,
                Aaa.class, PATH, map);
        query.prepareCallerClassAndMethodName("getResultList");
        query.prepareNode();
        query.prepareParameter();
        assertEquals("select * from aaa where id = ?", query.sqlContext
                .getSql());
        assertEquals(1, query.getParamSize());
        assertEquals(new SimpleDateFormat("HH:mm:ss").parse("12:11:10"), query
                .getParam(0).value);
        assertEquals(Date.class, query.getParam(0).paramClass);
        assertEquals(ValueTypes.DATE_TIME, query.getParam(0).valueType);
    }

    /**
     * 
     */
    public void testPrepareSql_dto() {
        MyDto dto = new MyDto();
        dto.id = 1;
        dto.offset = 5;
        dto.limit = 10;
        manager.setDialect(new PostgreDialect());
        SqlFileSelectImpl<Aaa> query = new SqlFileSelectImpl<Aaa>(manager,
                Aaa.class, PATH, dto);
        query.prepareCallerClassAndMethodName("getResultList");
        query.prepareNode();
        query.prepareParameter();
        query.prepareSql();
        assertEquals("select * from aaa where id = ? limit 10 offset 5",
                query.executedSql);
    }

    /**
     * 
     */
    public void testPrepareSql_map() {
        Map<String, Object> map = CollectionsUtil.newHashMap();
        map.put("id", 1);
        map.put("offset", 5);
        map.put("limit", 10);
        manager.setDialect(new PostgreDialect());
        SqlFileSelectImpl<Aaa> query = new SqlFileSelectImpl<Aaa>(manager,
                Aaa.class, PATH, map);
        query.prepareCallerClassAndMethodName("getResultList");
        query.prepareNode();
        query.prepareParameter();
        query.prepareSql();
        assertEquals("select * from aaa where id = ? limit 10 offset 5",
                query.executedSql);
    }

    /**
     * 
     */
    public void testPrepareSql_getCount() {
        MyDto dto = new MyDto();
        dto.id = 1;
        dto.offset = 5;
        dto.limit = 10;
        manager.setDialect(new PostgreDialect());
        SqlFileSelectImpl<Long> query = new SqlFileSelectImpl<Long>(manager,
                Long.class, PATH, dto);
        query.count = true;
        query.prepareCallerClassAndMethodName("getCount");
        query.prepareNode();
        query.prepareParameter();
        query.prepareSql();
        assertEquals(
                "select count(*) from ( select * from aaa where id = ? ) COUNT_",
                query.executedSql);
    }

    /**
     * 
     */
    public void testGetResultList() {
        MyDto dto = new MyDto();
        dto.id = 1;
        dto.offset = 5;
        dto.limit = 10;
        manager.setDialect(new PostgreDialect());
        SqlFileSelectImpl<Aaa> query = new SqlFileSelectImpl<Aaa>(manager,
                Aaa.class, PATH, dto) {

            @Override
            protected Object processResultSet(final JdbcContext jdbcContext,
                    final ResultSetHandler handler) {
                try {
                    MockResultSetMetaData rsMeta = new MockResultSetMetaData();
                    MockColumnMetaData columnMeta = new MockColumnMetaData();
                    columnMeta.setColumnLabel("ID");
                    rsMeta.addColumnMetaData(columnMeta);
                    columnMeta = new MockColumnMetaData();
                    columnMeta.setColumnLabel("NAME");
                    rsMeta.addColumnMetaData(columnMeta);
                    columnMeta = new MockColumnMetaData();
                    columnMeta.setColumnLabel("BBB_ID");
                    rsMeta.addColumnMetaData(columnMeta);
                    MockResultSet rs = new MockResultSet(rsMeta);
                    ArrayMap data = new ArrayMap();
                    data.put("ID", 1);
                    data.put("NAME", "AAA");
                    data.put("BBB_ID", 2);
                    rs.addRowData(data);
                    return handler.handle(rs);
                } catch (SQLException e) {
                    throw new SQLRuntimeException(e);
                }
            }

        };
        List<Aaa> ret = query.getResultList();
        assertEquals(1, ret.size());
        Aaa aaa = ret.get(0);
        assertEquals(new Integer(1), aaa.id);
        assertEquals("AAA", aaa.name);
        assertEquals(new Integer(2), aaa.bbbId);
        SqlLog sqlLog = SqlLogRegistryLocator.getInstance().getLast();
        assertEquals("select * from aaa where id = 1 limit 10 offset 5", sqlLog
                .getCompleteSql());

        try {
            query.getResultList();
            fail();
        } catch (QueryTwiceExecutionRuntimeException expected) {
        }
    }

    /**
     * 
     */
    public void testGetSingleResult() {
        MyDto dto = new MyDto();
        dto.id = 1;
        dto.offset = 5;
        dto.limit = 10;
        manager.setDialect(new PostgreDialect());
        SqlFileSelectImpl<Aaa> query = new SqlFileSelectImpl<Aaa>(manager,
                Aaa.class, PATH, dto) {

            @Override
            protected Object processResultSet(final JdbcContext jdbcContext,
                    final ResultSetHandler handler) {
                try {
                    MockResultSetMetaData rsMeta = new MockResultSetMetaData();
                    MockColumnMetaData columnMeta = new MockColumnMetaData();
                    columnMeta.setColumnLabel("ID");
                    rsMeta.addColumnMetaData(columnMeta);
                    columnMeta = new MockColumnMetaData();
                    columnMeta.setColumnLabel("NAME");
                    rsMeta.addColumnMetaData(columnMeta);
                    columnMeta = new MockColumnMetaData();
                    columnMeta.setColumnLabel("BBB_ID");
                    rsMeta.addColumnMetaData(columnMeta);
                    MockResultSet rs = new MockResultSet(rsMeta);
                    ArrayMap data = new ArrayMap();
                    data.put("ID", 1);
                    data.put("NAME", "AAA");
                    data.put("BBB_ID", 2);
                    rs.addRowData(data);
                    return handler.handle(rs);
                } catch (SQLException e) {
                    throw new SQLRuntimeException(e);
                }
            }

        };
        Aaa aaa = query.getSingleResult();
        assertEquals(new Integer(1), aaa.id);
        assertEquals("AAA", aaa.name);
        assertEquals(new Integer(2), aaa.bbbId);
        SqlLog sqlLog = SqlLogRegistryLocator.getInstance().getLast();
        assertEquals("select * from aaa where id = 1 limit 10 offset 5", sqlLog
                .getCompleteSql());

        try {
            query.getSingleResult();
            fail();
        } catch (QueryTwiceExecutionRuntimeException expected) {
        }
    }

    private static class MyDto {

        /**
         * 
         */
        public Integer id;

        /**
         * 
         */
        public int limit;

        /**
         * 
         */
        public int offset;
    }

    private static class MyDto2 {

        /**
         * 
         */
        @Lob
        public String largeName;
    }

    private static class MyDto3 {

        /**
         * 
         */
        @Temporal(TemporalType.TIME)
        public Date date;
    }

}