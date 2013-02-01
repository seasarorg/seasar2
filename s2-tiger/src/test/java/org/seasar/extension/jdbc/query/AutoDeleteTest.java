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

import javax.persistence.OptimisticLockException;

import junit.framework.TestCase;

import org.seasar.extension.jdbc.JdbcContext;
import org.seasar.extension.jdbc.SqlLog;
import org.seasar.extension.jdbc.SqlLogRegistry;
import org.seasar.extension.jdbc.SqlLogRegistryLocator;
import org.seasar.extension.jdbc.dialect.StandardDialect;
import org.seasar.extension.jdbc.entity.Eee;
import org.seasar.extension.jdbc.exception.QueryTwiceExecutionRuntimeException;
import org.seasar.extension.jdbc.manager.JdbcManagerImpl;
import org.seasar.extension.jdbc.meta.ColumnMetaFactoryImpl;
import org.seasar.extension.jdbc.meta.EntityMetaFactoryImpl;
import org.seasar.extension.jdbc.meta.PropertyMetaFactoryImpl;
import org.seasar.extension.jdbc.meta.TableMetaFactoryImpl;
import org.seasar.extension.jta.TransactionManagerImpl;
import org.seasar.extension.jta.TransactionSynchronizationRegistryImpl;
import org.seasar.framework.convention.impl.PersistenceConventionImpl;
import org.seasar.framework.mock.sql.MockDataSource;
import org.seasar.framework.mock.sql.MockPreparedStatement;

/**
 * @author koichik
 */
public class AutoDeleteTest extends TestCase {

    private JdbcManagerImpl manager;

    @Override
    protected void setUp() throws Exception {
        manager = new JdbcManagerImpl();
        manager.setSyncRegistry(new TransactionSynchronizationRegistryImpl(
                new TransactionManagerImpl()));
        manager.setDataSource(new MockDataSource());
        manager.setDialect(new StandardDialect());

        PersistenceConventionImpl convention = new PersistenceConventionImpl();
        EntityMetaFactoryImpl emFactory = new EntityMetaFactoryImpl();
        emFactory.setPersistenceConvention(convention);
        TableMetaFactoryImpl tableMetaFactory = new TableMetaFactoryImpl();
        tableMetaFactory.setPersistenceConvention(convention);
        emFactory.setTableMetaFactory(tableMetaFactory);

        PropertyMetaFactoryImpl pFactory = new PropertyMetaFactoryImpl();
        pFactory.setPersistenceConvention(convention);
        ColumnMetaFactoryImpl cmFactory = new ColumnMetaFactoryImpl();
        cmFactory.setPersistenceConvention(convention);
        pFactory.setColumnMetaFactory(cmFactory);
        emFactory.setPropertyMetaFactory(pFactory);
        emFactory.initialize();
        manager.setEntityMetaFactory(emFactory);
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
        AutoDeleteImpl<Eee> query = new AutoDeleteImpl<Eee>(manager, new Eee());
        assertSame(query, query.callerClass(getClass()));
        assertEquals(getClass(), query.callerClass);
    }

    /**
     * 
     */
    public void testCallerMethodName() {
        AutoDeleteImpl<Eee> query = new AutoDeleteImpl<Eee>(manager, new Eee());
        assertSame(query, query.callerMethodName("hoge"));
        assertEquals("hoge", query.callerMethodName);
    }

    /**
     * 
     */
    public void testQueryTimeout() {
        AutoDeleteImpl<Eee> query = new AutoDeleteImpl<Eee>(manager, new Eee());
        assertSame(query, query.queryTimeout(100));
        assertEquals(100, query.queryTimeout);
    }

    /**
     * 
     */
    public void testIgnoreVersion() {
        AutoDeleteImpl<Eee> query = new AutoDeleteImpl<Eee>(manager, new Eee());
        assertFalse(query.ignoreVersion);
        assertSame(query, query.ignoreVersion());
        assertTrue(query.ignoreVersion);
    }

    /**
     * 
     */
    public void testPrepareWhereClause() {
        Eee eee = new Eee();
        eee.id = 100;
        eee.name = "hoge";
        eee.version = 1L;
        AutoDeleteImpl<Eee> query = new AutoDeleteImpl<Eee>(manager, eee);
        query.prepare("execute");
        assertEquals(" where ID = ? and VERSION = ?", query.whereClause.toSql());
    }

    /**
     * 
     */
    public void testPrepareWhereClause_ignoreVersion() {
        Eee eee = new Eee();
        eee.id = 100;
        eee.name = "hoge";
        eee.version = 1L;
        AutoDeleteImpl<Eee> query = new AutoDeleteImpl<Eee>(manager, eee);
        query.ignoreVersion();
        query.prepare("execute");
        assertEquals(" where ID = ?", query.whereClause.toSql());
    }

    /**
     * 
     */
    public void testPrepareParams() {
        Eee eee = new Eee();
        eee.id = 100;
        eee.name = "hoge";
        eee.version = 1L;
        AutoDeleteImpl<Eee> query = new AutoDeleteImpl<Eee>(manager, eee);
        query.prepare("execute");
        assertEquals(2, query.getParamSize());
        assertEquals(new Integer(100), query.getParam(0).value);
        assertEquals(new Long(1L), query.getParam(1).value);
    }

    /**
     * 
     */
    public void testPrepareParams_ignoreVersion() {
        Eee eee = new Eee();
        eee.id = 100;
        eee.name = "hoge";
        eee.version = 1L;
        AutoDeleteImpl<Eee> query = new AutoDeleteImpl<Eee>(manager, eee);
        query.ignoreVersion();
        query.prepare("execute");
        assertEquals(1, query.getParamSize());
        assertEquals(new Integer(100), query.getParam(0).value);
    }

    /**
     * 
     */
    public void testPrepareSql() {
        Eee eee = new Eee();
        eee.id = 100;
        eee.name = "hoge";
        eee.version = 1L;
        AutoDeleteImpl<Eee> query = new AutoDeleteImpl<Eee>(manager, eee);
        query.prepare("execute");
        assertEquals("delete from EEE where ID = ? and VERSION = ?",
                query.executedSql);
    }

    /**
     * 
     */
    public void testPrepareSql_ignoreVersion() {
        Eee eee = new Eee();
        eee.id = 100;
        eee.name = "hoge";
        eee.version = 1L;
        AutoDeleteImpl<Eee> query = new AutoDeleteImpl<Eee>(manager, eee);
        query.ignoreVersion();
        query.prepare("execute");
        assertEquals("delete from EEE where ID = ?", query.executedSql);
    }

    /**
     * @throws Exception
     */
    public void testExecute() throws Exception {
        Eee eee = new Eee();
        eee.id = 100;
        eee.name = "hoge";
        eee.version = 1L;
        AutoDeleteImpl<Eee> query = new AutoDeleteImpl<Eee>(manager, eee) {

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
        assertEquals("delete from EEE where ID = 100 and VERSION = 1", sqlLog
                .getCompleteSql());

        try {
            query.execute();
            fail();
        } catch (QueryTwiceExecutionRuntimeException expected) {
        }
    }

    /**
     * @throws Exception
     */
    public void testExecute_ignoreVersion() throws Exception {
        Eee eee = new Eee();
        eee.id = 100;
        eee.name = "hoge";
        eee.version = 1L;
        AutoDeleteImpl<Eee> query = new AutoDeleteImpl<Eee>(manager, eee) {

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
        query.ignoreVersion();
        assertEquals(1, query.execute());
        SqlLog sqlLog = SqlLogRegistryLocator.getInstance().getLast();
        assertEquals("delete from EEE where ID = 100", sqlLog.getCompleteSql());
    }

    /**
     * @throws Exception
     */
    public void testOptimisticLock() throws Exception {
        Eee eee = new Eee();
        eee.id = 100;
        eee.name = "hoge";
        eee.version = 1L;
        AutoDeleteImpl<Eee> query = new AutoDeleteImpl<Eee>(manager, eee) {

            @Override
            protected PreparedStatement getPreparedStatement(
                    JdbcContext jdbcContext) {
                MockPreparedStatement ps = new MockPreparedStatement(null, null) {

                    @Override
                    public int executeUpdate() throws SQLException {
                        return 0;
                    }
                };
                return ps;
            }

        };
        try {
            query.execute();
            fail();
        } catch (OptimisticLockException expected) {
            expected.printStackTrace();
            assertSame(eee, expected.getEntity());
        }
    }

    /**
     * @throws Exception
     */
    public void testOptimisticLock_ignoreVersion() throws Exception {
        Eee eee = new Eee();
        eee.id = 100;
        eee.name = "hoge";
        eee.version = 1L;
        AutoDeleteImpl<Eee> query = new AutoDeleteImpl<Eee>(manager, eee) {

            @Override
            protected PreparedStatement getPreparedStatement(
                    JdbcContext jdbcContext) {
                MockPreparedStatement ps = new MockPreparedStatement(null, null) {

                    @Override
                    public int executeUpdate() throws SQLException {
                        return 0;
                    }
                };
                return ps;
            }

        };
        query.ignoreVersion();
        assertEquals(0, query.execute());
    }

    /**
     * @throws Exception
     */
    public void testOptimisticLock_suppressOptimisticLockException()
            throws Exception {
        Eee eee = new Eee();
        eee.id = 100;
        eee.name = "hoge";
        eee.version = 1L;
        AutoDeleteImpl<Eee> query = new AutoDeleteImpl<Eee>(manager, eee) {

            @Override
            protected PreparedStatement getPreparedStatement(
                    JdbcContext jdbcContext) {
                MockPreparedStatement ps = new MockPreparedStatement(null, null) {

                    @Override
                    public int executeUpdate() throws SQLException {
                        return 0;
                    }
                };
                return ps;
            }

        };
        query.suppresOptimisticLockException();
        assertEquals(0, query.execute());
    }

}
