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

import java.util.Map;

import junit.framework.TestCase;

import org.seasar.extension.jdbc.ResultSetHandler;
import org.seasar.extension.jdbc.SqlLogRegistry;
import org.seasar.extension.jdbc.SqlLogRegistryLocator;
import org.seasar.extension.jdbc.dialect.PostgreDialect;
import org.seasar.extension.jdbc.dialect.StandardDialect;
import org.seasar.extension.jdbc.dto.AaaDto;
import org.seasar.extension.jdbc.entity.Aaa;
import org.seasar.extension.jdbc.handler.BeanListResultSetHandler;
import org.seasar.extension.jdbc.handler.BeanListSupportLimitResultSetHandler;
import org.seasar.extension.jdbc.handler.BeanResultSetHandler;
import org.seasar.extension.jdbc.handler.MapListResultSetHandler;
import org.seasar.extension.jdbc.handler.MapListSupportLimitResultSetHandler;
import org.seasar.extension.jdbc.handler.ObjectListResultSetHandler;
import org.seasar.extension.jdbc.handler.ObjectListSupportLimitResultSetHandler;
import org.seasar.extension.jdbc.handler.ObjectResultSetHandler;
import org.seasar.extension.jdbc.manager.JdbcManagerImpl;
import org.seasar.extension.jdbc.manager.JdbcManagerImplementor;
import org.seasar.extension.jta.TransactionManagerImpl;
import org.seasar.extension.jta.TransactionSynchronizationRegistryImpl;
import org.seasar.framework.mock.sql.MockDataSource;

/**
 * @author higa
 * 
 */
public class AbstractSqlSelectTest extends TestCase {

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
        manager = null;
    }

    /**
     * @throws Exception
     * 
     */
    public void testCreateResultListResultSetHandler() throws Exception {
        MySelect<Aaa> query = new MySelect<Aaa>(manager, Aaa.class);
        ResultSetHandler handler = query.createResultListResultSetHandler();
        assertEquals(BeanListResultSetHandler.class, handler.getClass());
    }

    /**
     * @throws Exception
     * 
     */
    public void testCreateResultListResultSetHandler_simpleType()
            throws Exception {
        MySelect<Integer> query = new MySelect<Integer>(manager, Integer.class);
        ResultSetHandler handler = query.createResultListResultSetHandler();
        assertEquals(ObjectListResultSetHandler.class, handler.getClass());
    }

    /**
     * @throws Exception
     * 
     */
    @SuppressWarnings("unchecked")
    public void testCreateResultListResultSetHandler_map() throws Exception {
        MySelect<Map> query = new MySelect<Map>(manager, Map.class);
        ResultSetHandler handler = query.createResultListResultSetHandler();
        assertEquals(MapListResultSetHandler.class, handler.getClass());
    }

    /**
     * @throws Exception
     * 
     */
    public void testCreateResultListResultSetHandler_limit_supportLimit()
            throws Exception {
        manager.setDialect(new PostgreDialect());
        MySelect<Aaa> query = new MySelect<Aaa>(manager, Aaa.class);
        query.limit = 10;
        ResultSetHandler handler = query.createResultListResultSetHandler();
        assertEquals(BeanListResultSetHandler.class, handler.getClass());
    }

    /**
     * @throws Exception
     * 
     */
    public void testCreateResultListResultSetHandler_limit_notSupportLimit()
            throws Exception {
        MySelect<Aaa> query = new MySelect<Aaa>(manager, Aaa.class);
        query.limit = 10;
        ResultSetHandler handler = query.createResultListResultSetHandler();
        assertEquals(BeanListSupportLimitResultSetHandler.class, handler
                .getClass());
    }

    /**
     * @throws Exception
     * 
     */
    public void testCreateResultListResultSetHandler_simpleType_limit_supportLimit()
            throws Exception {
        manager.setDialect(new PostgreDialect());
        MySelect<Integer> query = new MySelect<Integer>(manager, Integer.class);
        query.limit = 10;
        ResultSetHandler handler = query.createResultListResultSetHandler();
        assertEquals(ObjectListResultSetHandler.class, handler.getClass());
    }

    /**
     * @throws Exception
     * 
     */
    public void testCreateResultListResultSetHandler_simpleType_limit_notSupportLimit()
            throws Exception {
        MySelect<Integer> query = new MySelect<Integer>(manager, Integer.class);
        query.limit = 10;
        ResultSetHandler handler = query.createResultListResultSetHandler();
        assertEquals(ObjectListSupportLimitResultSetHandler.class, handler
                .getClass());
    }

    /**
     * @throws Exception
     * 
     */
    @SuppressWarnings("unchecked")
    public void testCreateResultListResultSetHandler_map_limit_supportLimit()
            throws Exception {
        manager.setDialect(new PostgreDialect());
        MySelect<Map> query = new MySelect<Map>(manager, Map.class);
        query.limit = 10;
        ResultSetHandler handler = query.createResultListResultSetHandler();
        assertEquals(MapListResultSetHandler.class, handler.getClass());
    }

    /**
     * @throws Exception
     * 
     */
    @SuppressWarnings("unchecked")
    public void testCreateResultListResultSetHandler_map_limit_notSupportLimit()
            throws Exception {
        MySelect<Map> query = new MySelect<Map>(manager, Map.class);
        query.limit = 10;
        ResultSetHandler handler = query.createResultListResultSetHandler();
        assertEquals(MapListSupportLimitResultSetHandler.class, handler
                .getClass());
    }

    /**
     * @throws Exception
     * 
     */
    public void testCreateSingleResultResultSetHandler() throws Exception {
        MySelect<AaaDto> query = new MySelect<AaaDto>(manager, AaaDto.class);
        ResultSetHandler handler = query.createSingleResultResultSetHandler();
        assertEquals(BeanResultSetHandler.class, handler.getClass());
    }

    /**
     * @throws Exception
     * 
     */
    public void testCreateSingleResultResultSetHandler_simpleType()
            throws Exception {
        MySelect<Integer> query = new MySelect<Integer>(manager, Integer.class);
        ResultSetHandler handler = query.createSingleResultResultSetHandler();
        assertEquals(ObjectResultSetHandler.class, handler.getClass());
    }

    private static class MySelect<T> extends AbstractSqlSelect<T, MySelect<T>> {

        /**
         * @param jdbcManager
         * @param baseClass
         */
        public MySelect(JdbcManagerImplementor jdbcManager, Class<T> baseClass) {
            super(jdbcManager, baseClass);
        }

        @Override
        protected void prepare(String methodName) {
        }

    }
}
