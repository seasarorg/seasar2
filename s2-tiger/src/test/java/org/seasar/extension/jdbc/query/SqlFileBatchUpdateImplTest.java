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
import java.sql.SQLException;

import junit.framework.TestCase;

import org.seasar.extension.jdbc.JdbcContext;
import org.seasar.extension.jdbc.SqlLog;
import org.seasar.extension.jdbc.SqlLogRegistry;
import org.seasar.extension.jdbc.SqlLogRegistryLocator;
import org.seasar.extension.jdbc.dialect.StandardDialect;
import org.seasar.extension.jdbc.exception.IllegalParamTypeRuntimeException;
import org.seasar.extension.jdbc.manager.JdbcManagerImpl;
import org.seasar.extension.jta.TransactionManagerImpl;
import org.seasar.extension.jta.TransactionSynchronizationRegistryImpl;
import org.seasar.framework.exception.ResourceNotFoundRuntimeException;
import org.seasar.framework.mock.sql.MockDataSource;
import org.seasar.framework.mock.sql.MockPreparedStatement;

/**
 * @author taedium
 * 
 */
public class SqlFileBatchUpdateImplTest extends TestCase {

    private static final String PATH_SIMPLE = SqlFileUpdateImplTest.class
            .getName()
            + "_update_simpleType";

    private static final String PATH_DTO = SqlFileUpdateImplTest.class
            .getName()
            + "_update_dto";

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
        SqlFileBatchUpdateImpl query = new SqlFileBatchUpdateImpl(manager,
                "aaa.sql");
        assertSame(query, query.callerClass(getClass()));
        assertEquals(getClass(), query.callerClass);
    }

    /**
     * 
     */
    public void testCallerMethodName() {
        SqlFileBatchUpdateImpl query = new SqlFileBatchUpdateImpl(manager,
                "aaa.sql");
        assertSame(query, query.callerMethodName("hoge"));
        assertEquals("hoge", query.callerMethodName);
    }

    /**
     * 
     */
    public void testQueryTimeout() {
        SqlFileBatchUpdateImpl query = new SqlFileBatchUpdateImpl(manager,
                "aaa.sql");
        assertSame(query, query.queryTimeout(100));
        assertEquals(100, query.queryTimeout);
    }

    /**
     * 
     */
    public void testParam() {
        SqlFileBatchUpdateImpl query = new SqlFileBatchUpdateImpl(manager,
                "aaa.sql", String.class);
        query.param("hoge").param("hoge2");
        assertEquals(2, query.paramsList.size());
        Object param = query.paramsList.get(0);
        assertEquals("hoge", param);
        param = query.paramsList.get(1);
        assertEquals("hoge2", param);
    }

    /**
     * @throws Exception
     * 
     */
    public void testExecuteBatch() throws Exception {
        SqlFileBatchUpdateImpl query = new SqlFileBatchUpdateImpl(manager,
                PATH_SIMPLE, String.class) {

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
            protected void prepareInParams(PreparedStatement ps) {
                preparedBindVariables = true;
                super.prepareInParams(ps);
            }

        };
        int[] ret = query.param("foo").param("bar").executeBatch();
        assertEquals(2, ret.length);
        assertTrue(executedBatch);
        assertEquals(2, batchSize);
        assertTrue(preparedBindVariables);
        SqlLogRegistry registry = SqlLogRegistryLocator.getInstance();
        assertEquals(2, registry.getSize());
        SqlLog sqlLog = registry.get(0);
        assertEquals("update aaa set name = 'foo' where id = 1", sqlLog
                .getCompleteSql());
        sqlLog = registry.get(1);
        assertEquals("update aaa set name = 'bar' where id = 1", sqlLog
                .getCompleteSql());
    }

    /**
     * @throws Exception
     * 
     */
    public void testExecuteBatch_illegalParamType() throws Exception {
        SqlFileBatchUpdateImpl query = new SqlFileBatchUpdateImpl(manager,
                PATH_SIMPLE, String.class);
        try {
            query.param(1).executeBatch();
            fail();
        } catch (IllegalParamTypeRuntimeException e) {
            System.out.println(e);
            assertEquals(String.class, e.getExpectedType());
            assertEquals(Integer.class, e.getActualType());
        }
    }

    /**
     * @throws Exception
     * 
     */
    public void testExecuteBatch_illegalParamType_null() throws Exception {
        SqlFileBatchUpdateImpl query = new SqlFileBatchUpdateImpl(manager,
                PATH_SIMPLE);
        try {
            query.param(1).executeBatch();
            fail();
        } catch (IllegalParamTypeRuntimeException e) {
            System.out.println(e);
            assertEquals(null, e.getExpectedType());
            assertEquals(Integer.class, e.getActualType());
        }
    }

    /**
     * 
     */
    public void testPrepareNode() {
        SqlFileBatchUpdateImpl query = new SqlFileBatchUpdateImpl(manager,
                PATH_SIMPLE);
        query.prepareCallerClassAndMethodName("execute");
        query.prepareNode();
        assertNotNull(query.node);
    }

    /**
     * 
     */
    public void testPrepareNode_resourceNotFound() {
        SqlFileBatchUpdateImpl query = new SqlFileBatchUpdateImpl(manager,
                "xxx");
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
        SqlFileBatchUpdateImpl query = new SqlFileBatchUpdateImpl(manager,
                PATH_SIMPLE, String.class);
        query.prepareCallerClassAndMethodName("execute");
        query.prepareNode();
        query.prepareParameter("foo");
        assertEquals("update aaa set name = ? where id = 1", query.sqlContext
                .getSql());
        assertEquals(1, query.getParamSize());
        assertEquals("foo", query.getParam(0).value);
        assertEquals(String.class, query.getParam(0).paramClass);
        query.resetParams();
        query.prepareParameter("bar");
        assertEquals("update aaa set name = ? where id = 1", query.sqlContext
                .getSql());
        assertEquals(1, query.getParamSize());
        assertEquals("bar", query.getParam(0).value);
        assertEquals(String.class, query.getParam(0).paramClass);
        query.resetParams();
    }

    /**
     * 
     */
    public void testPrepareParameter_simpleType_bindNull() {
        SqlFileBatchUpdateImpl query = new SqlFileBatchUpdateImpl(manager,
                PATH_SIMPLE, String.class);
        query.prepareCallerClassAndMethodName("execute");
        query.prepareNode();
        query.prepareParameter(null);
        assertEquals("update aaa set name = ? where id = 1", query.sqlContext
                .getSql());
        assertEquals(1, query.getParamSize());
        assertNull(query.getParam(0).value);
        assertEquals(String.class, query.getParam(0).paramClass);
        query.resetParams();
        query.prepareParameter(null);
        assertEquals("update aaa set name = ? where id = 1", query.sqlContext
                .getSql());
        assertEquals(1, query.getParamSize());
        assertNull(query.getParam(0).value);
        assertEquals(String.class, query.getParam(0).paramClass);
        query.resetParams();
    }

    /**
     * 
     */
    public void testPrepareParameter_dto() {
        SqlFileBatchUpdateImpl query = new SqlFileBatchUpdateImpl(manager,
                PATH_DTO, MyDto.class);
        query.prepareCallerClassAndMethodName("getResultList");
        query.prepareNode();
        MyDto dto = new MyDto();
        dto.id = 1;
        dto.name = "foo";
        query.prepareParameter(dto);
        assertEquals("update aaa set name = ? where id = ?", query.sqlContext
                .getSql());
        assertEquals(2, query.getParamSize());
        assertEquals("foo", query.getParam(0).value);
        assertEquals(1, query.getParam(1).value);
        assertEquals(String.class, query.getParam(0).paramClass);
        assertEquals(Integer.class, query.getParam(1).paramClass);
        query.resetParams();
        dto = new MyDto();
        dto.id = 2;
        dto.name = "bar";
        query.prepareParameter(dto);
        assertEquals("update aaa set name = ? where id = ?", query.sqlContext
                .getSql());
        assertEquals(2, query.getParamSize());
        assertEquals("bar", query.getParam(0).value);
        assertEquals(2, query.getParam(1).value);
        assertEquals(String.class, query.getParam(0).paramClass);
        assertEquals(Integer.class, query.getParam(1).paramClass);
        query.resetParams();
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
}
