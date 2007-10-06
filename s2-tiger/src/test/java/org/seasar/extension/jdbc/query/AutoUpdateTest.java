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
import java.util.Map;

import junit.framework.TestCase;

import org.seasar.extension.jdbc.JdbcContext;
import org.seasar.extension.jdbc.SqlLog;
import org.seasar.extension.jdbc.SqlLogRegistry;
import org.seasar.extension.jdbc.SqlLogRegistryLocator;
import org.seasar.extension.jdbc.dialect.StandardDialect;
import org.seasar.extension.jdbc.entity.Aaa;
import org.seasar.extension.jdbc.entity.Eee;
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
import org.seasar.framework.util.tiger.CollectionsUtil;

/**
 * @author koichik
 */
public class AutoUpdateTest extends TestCase {

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
    public void testIncludeVersion() {
        AutoUpdateImpl<Eee> query = new AutoUpdateImpl<Eee>(manager, new Eee());
        assertFalse(query.includeVersion);
        assertSame(query, query.includeVersion());
        assertTrue(query.includeVersion);
    }

    /**
     * 
     */
    public void testExcludeNull() {
        AutoUpdateImpl<Eee> query = new AutoUpdateImpl<Eee>(manager, new Eee());
        assertFalse(query.excludeNull);
        assertSame(query, query.excludeNull());
        assertTrue(query.excludeNull);
    }

    /**
     * 
     */
    public void testInclude() {
        AutoUpdateImpl<Eee> query = new AutoUpdateImpl<Eee>(manager, new Eee());
        assertTrue(query.excludeProperties.isEmpty());

        assertSame(query, query.include("name"));
        assertEquals(1, query.includeProperties.size());
        assertTrue(query.includeProperties.contains("name"));
        assertFalse(query.includeProperties.contains("id"));
        assertFalse(query.includeProperties.contains("version"));

        assertSame(query, query.include("id", "version"));
        assertEquals(3, query.includeProperties.size());
        assertTrue(query.includeProperties.contains("name"));
        assertTrue(query.includeProperties.contains("id"));
        assertTrue(query.includeProperties.contains("version"));
    }

    /**
     * 
     */
    public void testExclude() {
        AutoUpdateImpl<Eee> query = new AutoUpdateImpl<Eee>(manager, new Eee());
        assertTrue(query.excludeProperties.isEmpty());

        assertSame(query, query.exclude("name"));
        assertEquals(1, query.excludeProperties.size());
        assertTrue(query.excludeProperties.contains("name"));
        assertFalse(query.excludeProperties.contains("id"));
        assertFalse(query.excludeProperties.contains("version"));

        assertSame(query, query.exclude("id", "version"));
        assertEquals(3, query.excludeProperties.size());
        assertTrue(query.excludeProperties.contains("name"));
        assertTrue(query.excludeProperties.contains("id"));
        assertTrue(query.excludeProperties.contains("version"));
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
        assertEquals(5, query.beforeStates.size());
        assertEquals(new Integer(100), query.beforeStates.get("id"));
        assertEquals("hoge", query.beforeStates.get("name"));
        assertNull(query.beforeStates.get("bbbId"));
        assertNull(query.beforeStates.get("bbb"));
        assertEquals(new Long(1), query.beforeStates.get("version"));
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
    public void testPrepareTarget() {
        Eee eee = new Eee();
        eee.id = 100;
        eee.name = "hoge";
        eee.version = 1L;
        AutoUpdateImpl<Eee> query = new AutoUpdateImpl<Eee>(manager, eee);
        query.prepareTargetProperties();
        assertEquals(2, query.targetProperties.size());
        assertEquals("name", query.targetProperties.get(0).getName());
        assertEquals("fffId", query.targetProperties.get(1).getName());
    }

    /**
     * 
     */
    public void testPrepareTarget_includeVersion() {
        Eee eee = new Eee();
        eee.id = 100;
        eee.name = "hoge";
        eee.version = 1L;
        AutoUpdateImpl<Eee> query = new AutoUpdateImpl<Eee>(manager, eee);
        query.includeVersion();
        query.prepareTargetProperties();
        assertEquals(3, query.targetProperties.size());
        assertEquals("name", query.targetProperties.get(0).getName());
        assertEquals("fffId", query.targetProperties.get(1).getName());
        assertEquals("version", query.targetProperties.get(2).getName());
    }

    /**
     * 
     */
    public void testPrepareTarget_excludeNull() {
        Eee eee = new Eee();
        eee.id = 100;
        eee.name = "hoge";
        eee.version = 1L;
        AutoUpdateImpl<Eee> query = new AutoUpdateImpl<Eee>(manager, eee);
        query.excludeNull();
        query.prepareTargetProperties();
        assertEquals(1, query.targetProperties.size());
        assertEquals("name", query.targetProperties.get(0).getName());
    }

    /**
     * 
     */
    public void testPrepareTarget_include() {
        Eee eee = new Eee();
        eee.id = 100;
        eee.name = "hoge";
        eee.version = 1L;
        AutoUpdateImpl<Eee> query = new AutoUpdateImpl<Eee>(manager, eee);
        query.include("name");
        query.prepareTargetProperties();
        assertEquals(1, query.targetProperties.size());
        assertEquals("name", query.targetProperties.get(0).getName());
    }

    /**
     * 
     */
    public void testPrepareTarget_exclude() {
        Eee eee = new Eee();
        eee.id = 100;
        eee.name = "hoge";
        eee.version = 1L;
        AutoUpdateImpl<Eee> query = new AutoUpdateImpl<Eee>(manager, eee);
        query.exclude("name");
        query.prepareTargetProperties();
        assertEquals(1, query.targetProperties.size());
        assertEquals("fffId", query.targetProperties.get(0).getName());
    }

    /**
     * 
     */
    public void testPrepareTarget_includeAndExclude() {
        Eee eee = new Eee();
        eee.id = 100;
        eee.name = "hoge";
        eee.version = 1L;
        AutoUpdateImpl<Eee> query = new AutoUpdateImpl<Eee>(manager, eee);
        query.include("name", "fffId").exclude("fffId");
        query.prepareTargetProperties();
        assertEquals(1, query.targetProperties.size());
        assertEquals("name", query.targetProperties.get(0).getName());
    }

    /**
     * 
     */
    public void testPrepareTarget_includeAndExcludeNull() {
        Eee eee = new Eee();
        eee.id = 100;
        eee.name = "hoge";
        eee.version = 1L;
        AutoUpdateImpl<Eee> query = new AutoUpdateImpl<Eee>(manager, eee);
        query.include("name", "fffId").excludeNull();
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
    public void testPrepareSet() {
        Eee eee = new Eee();
        eee.id = 100;
        eee.name = "hoge";
        eee.version = 1L;
        AutoUpdateImpl<Eee> query = new AutoUpdateImpl<Eee>(manager, eee);
        query.prepare("execute");
        assertEquals(" set NAME = ?, FFF_ID = ?, VERSION = VERSION + 1",
                query.setClause.toSql());
    }

    /**
     * 
     */
    public void testPrepareSet_includeVersion() {
        Eee eee = new Eee();
        eee.id = 100;
        eee.name = "hoge";
        eee.version = 1L;
        AutoUpdateImpl<Eee> query = new AutoUpdateImpl<Eee>(manager, eee);
        query.includeVersion();
        query.prepare("execute");
        assertEquals(" set NAME = ?, FFF_ID = ?, VERSION = ?", query.setClause
                .toSql());
    }

    /**
     * 
     */
    public void testPrepareWhere() {
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
    public void testPrepareWhere_incldueVersion() {
        Eee eee = new Eee();
        eee.id = 100;
        eee.name = "hoge";
        eee.version = 1L;
        AutoUpdateImpl<Eee> query = new AutoUpdateImpl<Eee>(manager, eee);
        query.includeVersion();
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
        assertEquals(4, query.bindVariableList.size());
        assertEquals("hoge", query.bindVariableList.get(0));
        assertNull(query.bindVariableList.get(1));
        assertEquals(new Integer(100), query.bindVariableList.get(2));
        assertEquals(new Long(1L), query.bindVariableList.get(3));
    }

    /**
     * 
     */
    public void testPrepareParams_includeVersion() {
        Eee eee = new Eee();
        eee.id = 100;
        eee.name = "hoge";
        eee.version = 1L;
        AutoUpdateImpl<Eee> query = new AutoUpdateImpl<Eee>(manager, eee);
        query.includeVersion();
        query.prepare("execute");
        assertEquals(4, query.bindVariableList.size());
        assertEquals("hoge", query.bindVariableList.get(0));
        assertNull(query.bindVariableList.get(1));
        assertEquals(new Long(1L), query.bindVariableList.get(2));
        assertEquals(new Integer(100), query.bindVariableList.get(3));
    }

    /**
     * @throws Exception
     */
    public void testPrepareParams_excludeNull() throws Exception {
        Eee eee = new Eee();
        eee.id = 100;
        eee.name = "hoge";
        eee.version = 1L;
        AutoUpdateImpl<Eee> query = new AutoUpdateImpl<Eee>(manager, eee);
        query.excludeNull();
        query.prepare("execute");
        assertEquals(3, query.bindVariableList.size());
        assertEquals("hoge", query.bindVariableList.get(0));
        assertEquals(new Integer(100), query.bindVariableList.get(1));
        assertEquals(new Long(1L), query.bindVariableList.get(2));
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
                "update EEE set NAME = ?, FFF_ID = ?, VERSION = VERSION + 1 where ID = ? and VERSION = ?",
                query.executedSql);
    }

    /**
     * 
     */
    public void testPrepareSql_includeVersion() {
        Eee eee = new Eee();
        eee.id = 100;
        eee.name = "hoge";
        eee.version = 1L;
        AutoUpdateImpl<Eee> query = new AutoUpdateImpl<Eee>(manager, eee);
        query.includeVersion();
        query.prepare("execute");
        assertEquals(
                "update EEE set NAME = ?, FFF_ID = ?, VERSION = ? where ID = ?",
                query.executedSql);
    }

    /**
     * 
     */
    public void testPrepareSql_excludeNull() {
        Eee eee = new Eee();
        eee.id = 100;
        eee.name = "hoge";
        eee.version = 1L;
        AutoUpdateImpl<Eee> query = new AutoUpdateImpl<Eee>(manager, eee);
        query.excludeNull();
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
                "update EEE set NAME = 'hoge', FFF_ID = null, VERSION = VERSION + 1 where ID = 100 and VERSION = 1",
                sqlLog.getCompleteSql());
    }

    /**
     * @throws Exception
     */
    public void testExecute_includeVersion() throws Exception {
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
        query.includeVersion();
        assertEquals(1, query.execute());
        SqlLog sqlLog = SqlLogRegistryLocator.getInstance().getLast();
        assertEquals(
                "update EEE set NAME = 'hoge', FFF_ID = null, VERSION = 1 where ID = 100",
                sqlLog.getCompleteSql());
    }

    /**
     * @throws Exception
     */
    public void testExecute_excludeNull() throws Exception {
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
        query.excludeNull();
        assertEquals(1, query.execute());
        SqlLog sqlLog = SqlLogRegistryLocator.getInstance().getLast();
        assertEquals(
                "update EEE set NAME = 'hoge', VERSION = VERSION + 1 where ID = 100 and VERSION = 1",
                sqlLog.getCompleteSql());
    }

}
