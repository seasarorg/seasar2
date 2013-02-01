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
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.persistence.EntityExistsException;
import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import junit.framework.TestCase;

import org.seasar.extension.jdbc.JdbcContext;
import org.seasar.extension.jdbc.SqlLog;
import org.seasar.extension.jdbc.SqlLogRegistry;
import org.seasar.extension.jdbc.SqlLogRegistryLocator;
import org.seasar.extension.jdbc.dialect.StandardDialect;
import org.seasar.extension.jdbc.exception.QueryTwiceExecutionRuntimeException;
import org.seasar.extension.jdbc.manager.JdbcManagerImpl;
import org.seasar.extension.jdbc.parameter.Parameter;
import org.seasar.extension.jdbc.types.ValueTypes;
import org.seasar.extension.jta.TransactionManagerImpl;
import org.seasar.extension.jta.TransactionSynchronizationRegistryImpl;
import org.seasar.extension.sql.cache.NodeCache;
import org.seasar.framework.exception.ResourceNotFoundRuntimeException;
import org.seasar.framework.mock.sql.MockDataSource;
import org.seasar.framework.mock.sql.MockPreparedStatement;
import org.seasar.framework.util.tiger.CollectionsUtil;

import static org.seasar.extension.jdbc.parameter.Parameter.*;

/**
 * @author taedium
 * 
 */
public class SqlFileUpdateImplTest extends TestCase {

    private static final String PATH_SIMPLE = SqlFileUpdateImplTest.class
            .getName()
            + "_update_simpleType";

    private static final String PATH_DTO = SqlFileUpdateImplTest.class
            .getName()
            + "_update_dto";

    private JdbcManagerImpl manager;

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
        NodeCache.clear();
        manager = null;
    }

    /**
     * 
     */
    public void testCallerClass() {
        SqlFileUpdateImpl query = new SqlFileUpdateImpl(manager, "aaa.sql");
        assertSame(query, query.callerClass(getClass()));
        assertEquals(getClass(), query.callerClass);
    }

    /**
     * 
     */
    public void testCallerMethodName() {
        SqlFileUpdateImpl query = new SqlFileUpdateImpl(manager, "aaa.sql");
        assertSame(query, query.callerMethodName("hoge"));
        assertEquals("hoge", query.callerMethodName);
    }

    /**
     * 
     */
    public void testQueryTimeout() {
        SqlFileUpdateImpl query = new SqlFileUpdateImpl(manager, "aaa.sql");
        assertSame(query, query.queryTimeout(100));
        assertEquals(100, query.queryTimeout);
    }

    /**
     * 
     */
    public void testPrepareNode() {
        SqlFileUpdateImpl query = new SqlFileUpdateImpl(manager, PATH_SIMPLE);
        query.prepareCallerClassAndMethodName("execute");
        query.prepareNode();
        assertNotNull(query.node);
    }

    /**
     * 
     */
    public void testPrepareNode_resourceNotFound() {
        SqlFileUpdateImpl query = new SqlFileUpdateImpl(manager, "xxx");
        query.prepareCallerClassAndMethodName("execute");
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
        SqlFileUpdateImpl query = new SqlFileUpdateImpl(manager, PATH_SIMPLE,
                "foo");
        query.prepareCallerClassAndMethodName("execute");
        query.prepareNode();
        query.prepareParameter();
        assertEquals("update aaa set name = ? where id = 1", query.sqlContext
                .getSql());
        assertEquals(1, query.getParamSize());
        assertEquals("foo", query.getParam(0).value);
        assertEquals(String.class, query.getParam(0).paramClass);
    }

    /**
     * 
     */
    public void testPrepareParameter_simpleType_clob() {
        SqlFileUpdateImpl query = new SqlFileUpdateImpl(manager, PATH_SIMPLE,
                lob("hoge"));
        query.prepareCallerClassAndMethodName("execute");
        query.prepareNode();
        query.prepareParameter();
        assertEquals("update aaa set name = ? where id = 1", query.sqlContext
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
        SqlFileUpdateImpl query = new SqlFileUpdateImpl(manager, PATH_SIMPLE,
                time(date));
        query.prepareCallerClassAndMethodName("execute");
        query.prepareNode();
        query.prepareParameter();
        assertEquals("update aaa set name = ? where id = 1", query.sqlContext
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
    public void testPrepareParameter_simpleType_bindNull() {
        SqlFileUpdateImpl query = new SqlFileUpdateImpl(manager, PATH_SIMPLE,
                null);
        query.prepareCallerClassAndMethodName("execute");
        query.prepareNode();
        try {
            query.prepareParameter();
            fail();
        } catch (NullPointerException ignore) {
        }
    }

    /**
     * 
     */
    public void testPrepareParameter_dto() {
        MyDto dto = new MyDto();
        dto.id = 1;
        dto.name = "foo";
        SqlFileUpdateImpl query = new SqlFileUpdateImpl(manager, PATH_DTO, dto);
        query.prepareCallerClassAndMethodName("getResultList");
        query.prepareNode();
        query.prepareParameter();
        assertEquals("update aaa set name = ? where id = ?", query.sqlContext
                .getSql());
        assertEquals(2, query.getParamSize());
        assertEquals("foo", query.getParam(0).value);
        assertEquals(1, query.getParam(1).value);
        assertEquals(String.class, query.getParam(0).paramClass);
        assertEquals(Integer.class, query.getParam(1).paramClass);
    }

    /**
     * 
     */
    public void testPrepareParameter_dto_clob() {
        MyDto2 dto = new MyDto2();
        dto.id = 1;
        dto.name = "hoge";
        SqlFileUpdateImpl query = new SqlFileUpdateImpl(manager, PATH_DTO, dto);
        query.prepareCallerClassAndMethodName("getResultList");
        query.prepareNode();
        query.prepareParameter();
        assertEquals("update aaa set name = ? where id = ?", query.sqlContext
                .getSql());
        assertEquals(2, query.getParamSize());
        assertEquals("hoge", query.getParam(0).value);
        assertEquals(1, query.getParam(1).value);
        assertEquals(String.class, query.getParam(0).paramClass);
        assertEquals(ValueTypes.CLOB, query.getParam(0).valueType);
        assertEquals(Integer.class, query.getParam(1).paramClass);
        assertEquals(ValueTypes.INTEGER, query.getParam(1).valueType);
    }

    /**
     * 
     * @throws Exception
     */
    public void testPrepareParameter_dto_date() throws Exception {
        MyDto3 dto = new MyDto3();
        dto.id = 1;
        dto.name = new SimpleDateFormat("HH:mm:ss").parse("12:11:10");
        SqlFileUpdateImpl query = new SqlFileUpdateImpl(manager, PATH_DTO, dto);
        query.prepareCallerClassAndMethodName("getResultList");
        query.prepareNode();
        query.prepareParameter();
        assertEquals("update aaa set name = ? where id = ?", query.sqlContext
                .getSql());
        assertEquals(2, query.getParamSize());
        assertEquals(new SimpleDateFormat("HH:mm:ss").parse("12:11:10"), query
                .getParam(0).value);
        assertEquals(1, query.getParam(1).value);
        assertEquals(Date.class, query.getParam(0).paramClass);
        assertEquals(ValueTypes.DATE_TIME, query.getParam(0).valueType);
        assertEquals(Integer.class, query.getParam(1).paramClass);
        assertEquals(ValueTypes.INTEGER, query.getParam(1).valueType);
    }

    /**
     * 
     */
    public void testPrepareParameter_map() {
        Map<String, Object> map = CollectionsUtil.newHashMap();
        map.put("id", 1);
        map.put("name", "foo");
        SqlFileUpdateImpl query = new SqlFileUpdateImpl(manager, PATH_DTO, map);
        query.prepareCallerClassAndMethodName("getResultList");
        query.prepareNode();
        query.prepareParameter();
        assertEquals("update aaa set name = ? where id = ?", query.sqlContext
                .getSql());
        assertEquals(2, query.getParamSize());
        assertEquals("foo", query.getParam(0).value);
        assertEquals(1, query.getParam(1).value);
        assertEquals(String.class, query.getParam(0).paramClass);
        assertEquals(Integer.class, query.getParam(1).paramClass);
    }

    /**
     * 
     */
    public void testPrepareParameter_map_clob() {
        Map<String, Object> map = CollectionsUtil.newHashMap();
        map.put("id", 1);
        map.put("name", Parameter.lob("hoge"));
        SqlFileUpdateImpl query = new SqlFileUpdateImpl(manager, PATH_DTO, map);
        query.prepareCallerClassAndMethodName("getResultList");
        query.prepareNode();
        query.prepareParameter();
        assertEquals("update aaa set name = ? where id = ?", query.sqlContext
                .getSql());
        assertEquals(2, query.getParamSize());
        assertEquals("hoge", query.getParam(0).value);
        assertEquals(1, query.getParam(1).value);
        assertEquals(String.class, query.getParam(0).paramClass);
        assertEquals(ValueTypes.CLOB, query.getParam(0).valueType);
        assertEquals(Integer.class, query.getParam(1).paramClass);
        assertEquals(ValueTypes.INTEGER, query.getParam(1).valueType);
    }

    /**
     * 
     * @throws Exception
     */
    public void testPrepareParameter_map_date() throws Exception {
        Map<String, Object> map = CollectionsUtil.newHashMap();
        map.put("id", 1);
        map.put("name", Parameter.time(new SimpleDateFormat("HH:mm:ss")
                .parse("12:11:10")));
        SqlFileUpdateImpl query = new SqlFileUpdateImpl(manager, PATH_DTO, map);
        query.prepareCallerClassAndMethodName("getResultList");
        query.prepareNode();
        query.prepareParameter();
        assertEquals("update aaa set name = ? where id = ?", query.sqlContext
                .getSql());
        assertEquals(2, query.getParamSize());
        assertEquals(new SimpleDateFormat("HH:mm:ss").parse("12:11:10"), query
                .getParam(0).value);
        assertEquals(1, query.getParam(1).value);
        assertEquals(Date.class, query.getParam(0).paramClass);
        assertEquals(ValueTypes.DATE_TIME, query.getParam(0).valueType);
        assertEquals(Integer.class, query.getParam(1).paramClass);
        assertEquals(ValueTypes.INTEGER, query.getParam(1).valueType);
    }

    /**
     * @throws Exception
     * 
     */
    public void testExecute() throws Exception {
        SqlFileUpdateImpl query = new SqlFileUpdateImpl(manager, PATH_SIMPLE,
                "foo") {

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
        assertEquals(1, query.execute());
        SqlLog sqlLog = SqlLogRegistryLocator.getInstance().getLast();
        assertEquals("update aaa set name = 'foo' where id = 1", sqlLog
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
        SqlFileUpdateImpl query = new SqlFileUpdateImpl(manager, PATH_SIMPLE,
                "foo") {

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
            query.execute();
            fail();
        } catch (EntityExistsException expected) {
            expected.printStackTrace();
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
        public String name;

    }

    private static class MyDto2 {

        /**
         * 
         */
        public Integer id;

        /**
         * 
         */
        @Lob
        public String name;

    }

    private static class MyDto3 {

        /**
         * 
         */
        public Integer id;

        /**
         * 
         */
        @Temporal(TemporalType.TIME)
        public Date name;

    }
}
