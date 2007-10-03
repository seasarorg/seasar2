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

import junit.framework.TestCase;

import org.seasar.extension.jdbc.SqlLogRegistry;
import org.seasar.extension.jdbc.SqlLogRegistryLocator;
import org.seasar.extension.jdbc.dialect.StandardDialect;
import org.seasar.extension.jdbc.entity.Aaa;
import org.seasar.extension.jdbc.manager.JdbcManagerImpl;
import org.seasar.extension.jta.TransactionManagerImpl;
import org.seasar.extension.jta.TransactionSynchronizationRegistryImpl;
import org.seasar.extension.sql.cache.NodeCache;
import org.seasar.framework.exception.ResourceNotFoundRuntimeException;
import org.seasar.framework.mock.sql.MockDataSource;

/**
 * @author higa
 * 
 */
public class SqlFileSelectImplTest extends TestCase {

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
                Aaa.class, getClass().getName() + "_select");
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
}