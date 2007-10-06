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

import java.sql.SQLException;

import junit.framework.TestCase;

import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.SqlLog;
import org.seasar.extension.jdbc.SqlLogRegistry;
import org.seasar.extension.jdbc.SqlLogRegistryLocator;
import org.seasar.extension.jdbc.dialect.StandardDialect;
import org.seasar.extension.jdbc.exception.NullBindVariableRuntimeException;
import org.seasar.extension.jdbc.manager.JdbcManagerImpl;
import org.seasar.framework.mock.sql.MockDataSource;
import org.seasar.framework.mock.sql.MockPreparedStatement;

/**
 * @author higa
 * 
 */
public class AbstractQueryTest extends TestCase {

    private JdbcManagerImpl manager;

    private Object bindVariable;

    private int parameterIndex;

    @Override
    protected void setUp() throws Exception {
        manager = new JdbcManagerImpl();
        manager.setDataSource(new MockDataSource());
        manager.setDialect(new StandardDialect());

    }

    @Override
    protected void tearDown() throws Exception {
        SqlLogRegistry regisry = SqlLogRegistryLocator.getInstance();
        regisry.clear();
    }

    /**
     * 
     */
    public void testLogSql() {
        String sql = "select * from aaa where id = ?";
        String completeSql = "select * from aaa where id = 1";
        MyQuery query = new MyQuery(manager);
        query.executedSql = sql;
        query.bindVariableList.add(1);
        query.bindVariableClassList.add(Integer.class);
        query.prepareCallerClassAndMethodName("testLogSql");
        query.logSql();
        SqlLogRegistry registry = SqlLogRegistryLocator.getInstance();
        SqlLog log = registry.getLast();
        assertEquals(sql, log.getRawSql());
        assertEquals(completeSql, log.getCompleteSql());
        assertEquals(1, log.getBindArgs().length);
        assertEquals(new Integer(1), log.getBindArgs()[0]);
        assertEquals(1, log.getBindArgTypes().length);
        assertEquals(Integer.class, log.getBindArgTypes()[0]);
    }

    /**
     * 
     */
    public void testPrepareCallerClassAndMethodName() {
        MyQuery query = new MyQuery(manager);
        query.prepareCallerClassAndMethodName("hoge");
        assertEquals(MyQuery.class, query.callerClass);
        assertNotNull(query.logger);
        assertEquals("hoge", query.callerMethodName);
    }

    /**
     * 
     */
    public void testPrepareCallerClass_preSetup() {
        MyQuery query = new MyQuery(manager);
        query.callerClass = getClass();
        query.callerMethodName = "foo";
        query.prepareCallerClassAndMethodName("hoge");
        assertEquals(getClass(), query.callerClass);
        assertEquals("foo", query.callerMethodName);
    }

    /**
     * 
     */
    public void testPrepareBindVariableClassList() {
        MyQuery query = new MyQuery(manager);
        query.bindVariableList.add(1);
        query.prepareBindVariableClassList();
        assertEquals(1, query.bindVariableClassList.size());
        assertEquals(Integer.class, query.bindVariableClassList.get(0));
    }

    /**
     * 
     */
    public void testPrepareBindVariableClassList_nullBindVariable() {
        MyQuery query = new MyQuery(manager);
        query.bindVariableList.add(null);
        query.prepareCallerClassAndMethodName("hoge");
        try {
            query.prepareBindVariableClassList();
            fail();
        } catch (NullBindVariableRuntimeException e) {
            System.out.println(e);
        }
    }

    /**
     * @throws Exception
     * 
     */
    public void testPrepareBindVariables() throws Exception {
        MyQuery query = new MyQuery(manager);
        query.bindVariableList.add("aaa");
        query.bindVariableClassList.add(String.class);
        MockPreparedStatement ps = new MockPreparedStatement(null, null) {

            @Override
            public void setString(int index, String x) throws SQLException {
                bindVariable = x;
                parameterIndex = index;
                super.setString(parameterIndex, x);
            }

        };
        query.prepareBindVariables(ps);
        assertEquals("aaa", bindVariable);
        assertEquals(1, parameterIndex);
    }

    private static class MyQuery extends AbstractQuery {

        /**
         * @param jdbcManager
         */
        public MyQuery(JdbcManager jdbcManager) {
            super(jdbcManager);
        }

        @Override
        protected void prepare(String methodName) {
        }
    }
}
