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
import java.util.Map;

import javax.persistence.OptimisticLockException;

import junit.framework.TestCase;

import org.seasar.extension.jdbc.JdbcContext;
import org.seasar.extension.jdbc.SqlLog;
import org.seasar.extension.jdbc.SqlLogRegistry;
import org.seasar.extension.jdbc.SqlLogRegistryLocator;
import org.seasar.extension.jdbc.dialect.StandardDialect;
import org.seasar.extension.jdbc.entity.Aaa;
import org.seasar.extension.jdbc.entity.Eee;
import org.seasar.extension.jdbc.exception.QueryTwiceExecutionRuntimeException;
import org.seasar.extension.jdbc.manager.JdbcManagerImpl;
import org.seasar.extension.jdbc.meta.ColumnMetaFactoryImpl;
import org.seasar.extension.jdbc.meta.EntityMetaFactoryImpl;
import org.seasar.extension.jdbc.meta.PropertyMetaFactoryImpl;
import org.seasar.extension.jdbc.meta.TableMetaFactoryImpl;
import org.seasar.extension.jdbc.types.StringClobType;
import org.seasar.extension.jta.TransactionManagerImpl;
import org.seasar.extension.jta.TransactionSynchronizationRegistryImpl;
import org.seasar.framework.convention.impl.PersistenceConventionImpl;
import org.seasar.framework.mock.sql.MockDataSource;
import org.seasar.framework.mock.sql.MockPreparedStatement;
import org.seasar.framework.util.tiger.CollectionsUtil;

/**
 * @author koichik
 */
public class AutoUpdateImplTest extends TestCase {

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
        AutoUpdateImpl<Eee> query = new AutoUpdateImpl<Eee>(manager, new Eee());
        assertSame(query, query.callerClass(getClass()));
        assertEquals(getClass(), query.callerClass);
    }

    /**
     * 
     */
    public void testCallerMethodName() {
        AutoUpdateImpl<Eee> query = new AutoUpdateImpl<Eee>(manager, new Eee());
        assertSame(query, query.callerMethodName("hoge"));
        assertEquals("hoge", query.callerMethodName);
    }

    /**
     * 
     */
    public void testQueryTimeout() {
        AutoUpdateImpl<Eee> query = new AutoUpdateImpl<Eee>(manager, new Eee());
        assertSame(query, query.queryTimeout(100));
        assertEquals(100, query.queryTimeout);
    }

    /**
     * 
     */
    public void testIncludesVersion() {
        AutoUpdateImpl<Eee> query = new AutoUpdateImpl<Eee>(manager, new Eee());
        assertFalse(query.includeVersion);
        assertSame(query, query.includesVersion());
        assertTrue(query.includeVersion);
    }

    /**
     * 
     */
    public void testIncrementVersions() {
        Eee e = new Eee(1, "foo");
        AutoUpdateImpl<Eee> query = new AutoUpdateImpl<Eee>(manager, e);
        query.incrementVersion();
        assertEquals(new Long(1), e.version);
    }

    /**
     * 
     */
    public void testIncrementVersions_includeVersion() {
        Eee e = new Eee(1, "foo");
        AutoUpdateImpl<Eee> query = new AutoUpdateImpl<Eee>(manager, e);
        query.includesVersion();
        query.incrementVersion();
        assertEquals(new Long(0), e.version);
    }

    /**
     * 
     */
    public void testExcludesNull() {
        AutoUpdateImpl<Eee> query = new AutoUpdateImpl<Eee>(manager, new Eee());
        assertFalse(query.excludesNull);
        assertSame(query, query.excludesNull());
        assertTrue(query.excludesNull);
    }

    /**
     * 
     */
    public void testIncludes() {
        AutoUpdateImpl<Eee> query = new AutoUpdateImpl<Eee>(manager, new Eee());
        assertTrue(query.excludesProperties.isEmpty());

        assertSame(query, query.includes("name"));
        assertEquals(1, query.includesProperties.size());
        assertTrue(query.includesProperties.contains("name"));
        assertFalse(query.includesProperties.contains("id"));
        assertFalse(query.includesProperties.contains("version"));

        assertSame(query, query.includes("id", "version"));
        assertEquals(3, query.includesProperties.size());
        assertTrue(query.includesProperties.contains("name"));
        assertTrue(query.includesProperties.contains("id"));
        assertTrue(query.includesProperties.contains("version"));
    }

    /**
     * 
     */
    public void testExcludes() {
        AutoUpdateImpl<Eee> query = new AutoUpdateImpl<Eee>(manager, new Eee());
        assertTrue(query.excludesProperties.isEmpty());

        assertSame(query, query.excludes("name"));
        assertEquals(1, query.excludesProperties.size());
        assertTrue(query.excludesProperties.contains("name"));
        assertFalse(query.excludesProperties.contains("id"));
        assertFalse(query.excludesProperties.contains("version"));

        assertSame(query, query.excludes("id", "version"));
        assertEquals(3, query.excludesProperties.size());
        assertTrue(query.excludesProperties.contains("name"));
        assertTrue(query.excludesProperties.contains("id"));
        assertTrue(query.excludesProperties.contains("version"));
    }

    /**
     * 
     */
    public void testChangedFrom_entity() {
        Eee before = new Eee();
        before.id = 100;
        before.name = "hoge";
        before.version = 1L;
        AutoUpdateImpl<Eee> query = new AutoUpdateImpl<Eee>(manager, new Eee());
        assertSame(query, query.changedFrom(before));
        assertEquals(7, query.beforeStates.size());
        assertEquals(new Integer(100), query.beforeStates.get("id"));
        assertEquals("hoge", query.beforeStates.get("name"));
        assertNull(query.beforeStates.get("longText"));
        assertNull(query.beforeStates.get("bbbId"));
        assertNull(query.beforeStates.get("bbb"));
        assertEquals(new Long(1), query.beforeStates.get("version"));
        assertNull(query.beforeStates.get("lastUpdate"));
    }

    /**
     * 
     */
    public void testChangedFrom_entity_same() {
        Eee before = new Eee();
        before.id = 100;
        before.name = "hoge";
        before.version = 1L;
        AutoUpdateImpl<Eee> query = new AutoUpdateImpl<Eee>(manager, before);
        assertSame(query, query.changedFrom(before));
        assertEquals(7, query.beforeStates.size());
        assertEquals(new Integer(100), query.beforeStates.get("id"));
        assertEquals("hoge", query.beforeStates.get("name"));
        assertNull(query.beforeStates.get("longText"));
        assertNull(query.beforeStates.get("bbbId"));
        assertNull(query.beforeStates.get("bbb"));
        assertEquals(new Long(1), query.beforeStates.get("version"));
        assertNull(query.beforeStates.get("lastUpdate"));
        assertTrue(query.targetProperties.isEmpty());
        assertEquals(0, query.setClause.getLength());
    }

    /**
     * 
     */
    public void testChangedFrom_map() {
        Map<String, Object> before = CollectionsUtil.newHashMap();
        before.put("id", 100);
        before.put("name", "hoge");
        AutoUpdateImpl<Aaa> query = new AutoUpdateImpl<Aaa>(manager, new Aaa());
        assertSame(query, query.changedFrom(before));
        assertEquals(2, query.beforeStates.size());
        assertEquals(new Integer(100), query.beforeStates.get("id"));
        assertEquals("hoge", query.beforeStates.get("name"));
    }

    /**
     * 
     */
    public void testChangedFrom_map_same() {
        Map<String, Object> before = CollectionsUtil.newHashMap();
        before.put("id", 100);
        before.put("name", "hoge");
        Aaa aaa = new Aaa();
        aaa.id = 100;
        aaa.name = "hoge";
        AutoUpdateImpl<Aaa> query = new AutoUpdateImpl<Aaa>(manager, aaa);
        assertSame(query, query.changedFrom(before));
        assertEquals(2, query.beforeStates.size());
        assertEquals(new Integer(100), query.beforeStates.get("id"));
        assertEquals("hoge", query.beforeStates.get("name"));
        assertTrue(query.targetProperties.isEmpty());
        assertEquals(0, query.setClause.getLength());
    }

    /**
     * 
     */
    public void testPrepareTarget() {
        Eee eee = new Eee();
        eee.id = 100;
        eee.name = "hoge";
        eee.version = 1L;
        AutoUpdateImpl<Eee> query = new AutoUpdateImpl<Eee>(manager, eee);
        query.prepareTargetProperties();
        assertEquals(3, query.targetProperties.size());
        assertEquals("name", query.targetProperties.get(0).getName());
        assertEquals("longText", query.targetProperties.get(1).getName());
        assertEquals("fffId", query.targetProperties.get(2).getName());
    }

    /**
     * 
     */
    public void testPrepareTarget_includesVersion() {
        Eee eee = new Eee();
        eee.id = 100;
        eee.name = "hoge";
        eee.version = 1L;
        AutoUpdateImpl<Eee> query = new AutoUpdateImpl<Eee>(manager, eee);
        query.includesVersion();
        query.prepareTargetProperties();
        assertEquals(4, query.targetProperties.size());
        assertEquals("name", query.targetProperties.get(0).getName());
        assertEquals("longText", query.targetProperties.get(1).getName());
        assertEquals("fffId", query.targetProperties.get(2).getName());
        assertEquals("version", query.targetProperties.get(3).getName());
    }

    /**
     * 
     */
    public void testPrepareTarget_excludesNull() {
        Eee eee = new Eee();
        eee.id = 100;
        eee.name = "hoge";
        eee.version = 1L;
        AutoUpdateImpl<Eee> query = new AutoUpdateImpl<Eee>(manager, eee);
        query.excludesNull();
        query.prepareTargetProperties();
        assertEquals(1, query.targetProperties.size());
        assertEquals("name", query.targetProperties.get(0).getName());
    }

    /**
     * 
     */
    public void testPrepareTarget_includes() {
        Eee eee = new Eee();
        eee.id = 100;
        eee.name = "hoge";
        eee.version = 1L;
        AutoUpdateImpl<Eee> query = new AutoUpdateImpl<Eee>(manager, eee);
        query.includes("name");
        query.prepareTargetProperties();
        assertEquals(1, query.targetProperties.size());
        assertEquals("name", query.targetProperties.get(0).getName());
    }

    /**
     * 
     */
    public void testPrepareTarget_excludes() {
        Eee eee = new Eee();
        eee.id = 100;
        eee.name = "hoge";
        eee.version = 1L;
        AutoUpdateImpl<Eee> query = new AutoUpdateImpl<Eee>(manager, eee);
        query.excludes("name");
        query.prepareTargetProperties();
        assertEquals(2, query.targetProperties.size());
        assertEquals("longText", query.targetProperties.get(0).getName());
        assertEquals("fffId", query.targetProperties.get(1).getName());
    }

    /**
     * 
     */
    public void testPrepareTarget_includesAndExcludes() {
        Eee eee = new Eee();
        eee.id = 100;
        eee.name = "hoge";
        eee.version = 1L;
        AutoUpdateImpl<Eee> query = new AutoUpdateImpl<Eee>(manager, eee);
        query.includes("name", "fffId").excludes("fffId");
        query.prepareTargetProperties();
        assertEquals(1, query.targetProperties.size());
        assertEquals("name", query.targetProperties.get(0).getName());
    }

    /**
     * 
     */
    public void testPrepareTarget_includesAndExcludesNull() {
        Eee eee = new Eee();
        eee.id = 100;
        eee.name = "hoge";
        eee.version = 1L;
        AutoUpdateImpl<Eee> query = new AutoUpdateImpl<Eee>(manager, eee);
        query.includes("name", "fffId").excludesNull();
        query.prepareTargetProperties();
        assertEquals(1, query.targetProperties.size());
        assertEquals("name", query.targetProperties.get(0).getName());
    }

    /**
     * 
     */
    public void testPrepareTarget_changedFromEntity() {
        Eee eee = new Eee();
        eee.id = 100;
        eee.name = "foo";
        eee.version = 1L;
        Eee before = new Eee();
        before.id = 100;
        before.name = "bar";
        eee.version = 1L;
        AutoUpdateImpl<Eee> query = new AutoUpdateImpl<Eee>(manager, eee);
        query.changedFrom(before);
        query.prepareTargetProperties();
        assertEquals(1, query.targetProperties.size());
        assertEquals("name", query.targetProperties.get(0).getName());
    }

    /**
     * 
     */
    public void testPrepareTarget_changedFromMap() {
        Eee eee = new Eee();
        eee.id = 100;
        eee.name = "foo";
        eee.version = 1L;
        Map<String, Object> before = CollectionsUtil.newHashMap();
        before.put("id", 100);
        before.put("name", "hoge");
        AutoUpdateImpl<Eee> query = new AutoUpdateImpl<Eee>(manager, eee);
        query.changedFrom(before);
        query.prepareTargetProperties();
        assertEquals(1, query.targetProperties.size());
        assertEquals("name", query.targetProperties.get(0).getName());
    }

    /**
     * 
     */
    public void testPrepareSetClause() {
        Eee eee = new Eee();
        eee.id = 100;
        eee.name = "hoge";
        eee.version = 1L;
        AutoUpdateImpl<Eee> query = new AutoUpdateImpl<Eee>(manager, eee);
        query.prepare("execute");
        assertEquals(
                " set NAME = ?, LONG_TEXT = ?, FFF_ID = ?, VERSION = VERSION + 1",
                query.setClause.toSql());
    }

    /**
     * 
     */
    public void testPrepareSetClause_includesVersion() {
        Eee eee = new Eee();
        eee.id = 100;
        eee.name = "hoge";
        eee.version = 1L;
        AutoUpdateImpl<Eee> query = new AutoUpdateImpl<Eee>(manager, eee);
        query.includesVersion();
        query.prepare("execute");
        assertEquals(" set NAME = ?, LONG_TEXT = ?, FFF_ID = ?, VERSION = ?",
                query.setClause.toSql());
    }

    /**
     * 
     */
    public void testPrepareWhereClause() {
        Eee eee = new Eee();
        eee.id = 100;
        eee.name = "hoge";
        eee.version = 1L;
        AutoUpdateImpl<Eee> query = new AutoUpdateImpl<Eee>(manager, eee);
        query.prepare("execute");
        assertEquals(" where ID = ? and VERSION = ?", query.whereClause.toSql());
    }

    /**
     * 
     */
    public void testPrepareWhereClause_inclduesVersion() {
        Eee eee = new Eee();
        eee.id = 100;
        eee.name = "hoge";
        eee.version = 1L;
        AutoUpdateImpl<Eee> query = new AutoUpdateImpl<Eee>(manager, eee);
        query.includesVersion();
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
        AutoUpdateImpl<Eee> query = new AutoUpdateImpl<Eee>(manager, eee);
        query.prepare("execute");
        assertEquals(5, query.getParamSize());
        assertEquals("hoge", query.getParam(0).value);
        assertNull(query.getParam(1).value);
        assertTrue(query.getParam(1).valueType instanceof StringClobType);
        assertNull(query.getParam(2).value);
        assertEquals(new Integer(100), query.getParam(3).value);
        assertEquals(new Long(1L), query.getParam(4).value);
    }

    /**
     * 
     */
    public void testPrepareParams_includesVersion() {
        Eee eee = new Eee();
        eee.id = 100;
        eee.name = "hoge";
        eee.version = 1L;
        AutoUpdateImpl<Eee> query = new AutoUpdateImpl<Eee>(manager, eee);
        query.includesVersion();
        query.prepare("execute");
        assertEquals(5, query.getParamSize());
        assertEquals("hoge", query.getParam(0).value);
        assertNull(query.getParam(1).value);
        assertTrue(query.getParam(1).valueType instanceof StringClobType);
        assertNull(query.getParam(2).value);
        assertEquals(new Long(1L), query.getParam(3).value);
        assertEquals(new Integer(100), query.getParam(4).value);
    }

    /**
     * @throws Exception
     */
    public void testPrepareParams_excludesNull() throws Exception {
        Eee eee = new Eee();
        eee.id = 100;
        eee.name = "hoge";
        eee.version = 1L;
        AutoUpdateImpl<Eee> query = new AutoUpdateImpl<Eee>(manager, eee);
        query.excludesNull();
        query.prepare("execute");
        assertEquals(3, query.getParamSize());
        assertEquals("hoge", query.getParam(0).value);
        assertEquals(new Integer(100), query.getParam(1).value);
        assertEquals(new Long(1L), query.getParam(2).value);
    }

    /**
     * 
     */
    public void testPrepareSql() {
        Eee eee = new Eee();
        eee.id = 100;
        eee.name = "hoge";
        eee.version = 1L;
        AutoUpdateImpl<Eee> query = new AutoUpdateImpl<Eee>(manager, eee);
        query.prepare("execute");
        assertEquals(
                "update EEE set NAME = ?, LONG_TEXT = ?, FFF_ID = ?, VERSION = VERSION + 1 where ID = ? and VERSION = ?",
                query.executedSql);
    }

    /**
     * 
     */
    public void testPrepareSql_includesVersion() {
        Eee eee = new Eee();
        eee.id = 100;
        eee.name = "hoge";
        eee.version = 1L;
        AutoUpdateImpl<Eee> query = new AutoUpdateImpl<Eee>(manager, eee);
        query.includesVersion();
        query.prepare("execute");
        assertEquals(
                "update EEE set NAME = ?, LONG_TEXT = ?, FFF_ID = ?, VERSION = ? where ID = ?",
                query.executedSql);
    }

    /**
     * 
     */
    public void testPrepareSql_excludesNull() {
        Eee eee = new Eee();
        eee.id = 100;
        eee.name = "hoge";
        eee.version = 1L;
        AutoUpdateImpl<Eee> query = new AutoUpdateImpl<Eee>(manager, eee);
        query.excludesNull();
        query.prepare("execute");
        assertEquals(
                "update EEE set NAME = ?, VERSION = VERSION + 1 where ID = ? and VERSION = ?",
                query.executedSql);
    }

    /**
     * @throws Exception
     */
    public void testExecute() throws Exception {
        Eee eee = new Eee();
        eee.id = 100;
        eee.name = "hoge";
        eee.version = 1L;
        AutoUpdateImpl<Eee> query = new AutoUpdateImpl<Eee>(manager, eee) {

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
        assertEquals(
                "update EEE set NAME = 'hoge', LONG_TEXT = null, FFF_ID = null, VERSION = VERSION + 1 where ID = 100 and VERSION = 1",
                sqlLog.getCompleteSql());

        try {
            query.execute();
            fail();
        } catch (QueryTwiceExecutionRuntimeException expected) {
        }
    }

    /**
     * @throws Exception
     */
    public void testExecute_includesVersion() throws Exception {
        Eee eee = new Eee();
        eee.id = 100;
        eee.name = "hoge";
        eee.version = 1L;
        AutoUpdateImpl<Eee> query = new AutoUpdateImpl<Eee>(manager, eee) {

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
        query.includesVersion();
        assertEquals(1, query.execute());
        SqlLog sqlLog = SqlLogRegistryLocator.getInstance().getLast();
        assertEquals(
                "update EEE set NAME = 'hoge', LONG_TEXT = null, FFF_ID = null, VERSION = 1 where ID = 100",
                sqlLog.getCompleteSql());
    }

    /**
     * @throws Exception
     */
    public void testExecute_excludesNull() throws Exception {
        Eee eee = new Eee();
        eee.id = 100;
        eee.name = "hoge";
        eee.version = 1L;
        AutoUpdateImpl<Eee> query = new AutoUpdateImpl<Eee>(manager, eee) {

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
        query.excludesNull();
        assertEquals(1, query.execute());
        SqlLog sqlLog = SqlLogRegistryLocator.getInstance().getLast();
        assertEquals(
                "update EEE set NAME = 'hoge', VERSION = VERSION + 1 where ID = 100 and VERSION = 1",
                sqlLog.getCompleteSql());
    }

    /**
     * @throws Exception
     */
    public void testExecute_changedFrom_same() throws Exception {
        Eee eee = new Eee();
        eee.id = 100;
        eee.name = "hoge";
        eee.version = 1L;
        AutoUpdateImpl<Eee> query = new AutoUpdateImpl<Eee>(manager, eee);
        query.changedFrom(eee);
        assertEquals(0, query.execute());
        assertNull(SqlLogRegistryLocator.getInstance().getLast());

        try {
            query.execute();
            fail();
        } catch (QueryTwiceExecutionRuntimeException expected) {
        }
    }

    /**
     * @throws Exception
     */
    public void testOptimisticLock() throws Exception {
        Eee eee = new Eee();
        eee.id = 100;
        eee.name = "hoge";
        eee.version = 1L;
        AutoUpdateImpl<Eee> query = new AutoUpdateImpl<Eee>(manager, eee) {

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
    public void testOptimisticLock_includesVersion() throws Exception {
        Eee eee = new Eee();
        eee.id = 100;
        eee.name = "hoge";
        eee.version = 1L;
        AutoUpdateImpl<Eee> query = new AutoUpdateImpl<Eee>(manager, eee) {

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
        query.includesVersion();
        assertEquals(0, query.execute());
        assertEquals(new Long(1), eee.version);
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
        AutoUpdateImpl<Eee> query = new AutoUpdateImpl<Eee>(manager, eee) {

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
