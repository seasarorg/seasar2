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
import java.util.Arrays;
import java.util.List;

import javax.persistence.OptimisticLockException;

import junit.framework.TestCase;

import org.seasar.extension.jdbc.JdbcContext;
import org.seasar.extension.jdbc.SqlLog;
import org.seasar.extension.jdbc.SqlLogRegistry;
import org.seasar.extension.jdbc.SqlLogRegistryLocator;
import org.seasar.extension.jdbc.dialect.StandardDialect;
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

/**
 * @author koichik
 */
public class AutoBatchUpdateTest extends TestCase {

    private JdbcManagerImpl manager;

    private int addBatchCalled;

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
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchUpdateImpl<Eee> query = new AutoBatchUpdateImpl<Eee>(manager,
                entities);
        assertSame(query, query.callerClass(getClass()));
        assertEquals(getClass(), query.callerClass);
    }

    /**
     * 
     */
    public void testCallerMethodName() {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchUpdateImpl<Eee> query = new AutoBatchUpdateImpl<Eee>(manager,
                entities);
        assertSame(query, query.callerMethodName("hoge"));
        assertEquals("hoge", query.callerMethodName);
    }

    /**
     * 
     */
    public void testQueryTimeout() {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchUpdateImpl<Eee> query = new AutoBatchUpdateImpl<Eee>(manager,
                entities);
        assertSame(query, query.queryTimeout(100));
        assertEquals(100, query.queryTimeout);
    }

    /**
     * 
     */
    public void testIncludesVersion() {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchUpdateImpl<Eee> query = new AutoBatchUpdateImpl<Eee>(manager,
                entities);
        assertFalse(query.includeVersion);
        assertSame(query, query.includesVersion());
        assertTrue(query.includeVersion);
    }

    /**
     * 
     */
    public void testIncludes() {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchUpdateImpl<Eee> query = new AutoBatchUpdateImpl<Eee>(manager,
                entities);
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
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchUpdateImpl<Eee> query = new AutoBatchUpdateImpl<Eee>(manager,
                entities);
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
    public void testPrepareTarget() {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchUpdateImpl<Eee> query = new AutoBatchUpdateImpl<Eee>(manager,
                entities);
        query.prepareTargetProperties();
        assertEquals(2, query.targetProperties.size());
        assertEquals("name", query.targetProperties.get(0).getName());
        assertEquals("fffId", query.targetProperties.get(1).getName());
    }

    /**
     * 
     */
    public void testPrepareTarget_includesVersion() {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchUpdateImpl<Eee> query = new AutoBatchUpdateImpl<Eee>(manager,
                entities);
        query.includesVersion();
        query.prepareTargetProperties();
        assertEquals(3, query.targetProperties.size());
        assertEquals("name", query.targetProperties.get(0).getName());
        assertEquals("fffId", query.targetProperties.get(1).getName());
        assertEquals("version", query.targetProperties.get(2).getName());
    }

    /**
     * 
     */
    public void testPrepareTarget_includes() {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchUpdateImpl<Eee> query = new AutoBatchUpdateImpl<Eee>(manager,
                entities);
        query.includes("name");
        query.prepareTargetProperties();
        assertEquals(1, query.targetProperties.size());
        assertEquals("name", query.targetProperties.get(0).getName());
    }

    /**
     * 
     */
    public void testPrepareTarget_excludes() {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchUpdateImpl<Eee> query = new AutoBatchUpdateImpl<Eee>(manager,
                entities);
        query.excludes("name");
        query.prepareTargetProperties();
        assertEquals(1, query.targetProperties.size());
        assertEquals("fffId", query.targetProperties.get(0).getName());
    }

    /**
     * 
     */
    public void testPrepareTarget_includesAndExcludes() {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchUpdateImpl<Eee> query = new AutoBatchUpdateImpl<Eee>(manager,
                entities);
        query.includes("name", "fffId").excludes("fffId");
        query.prepareTargetProperties();
        assertEquals(1, query.targetProperties.size());
        assertEquals("name", query.targetProperties.get(0).getName());
    }

    /**
     * 
     */
    public void testPrepareSetClause() {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchUpdateImpl<Eee> query = new AutoBatchUpdateImpl<Eee>(manager,
                entities);
        query.prepare("execute");
        assertEquals(" set NAME = ?, FFF_ID = ?, VERSION = VERSION + 1",
                query.setClause.toSql());
    }

    /**
     * 
     */
    public void testPrepareSetClause_includesVersion() {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchUpdateImpl<Eee> query = new AutoBatchUpdateImpl<Eee>(manager,
                entities);
        query.includesVersion();
        query.prepare("execute");
        assertEquals(" set NAME = ?, FFF_ID = ?, VERSION = ?", query.setClause
                .toSql());
    }

    /**
     * 
     */
    public void testPrepareWhereClause() {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchUpdateImpl<Eee> query = new AutoBatchUpdateImpl<Eee>(manager,
                entities);
        query.prepare("execute");
        assertEquals(" where ID = ? and VERSION = ?", query.whereClause.toSql());
    }

    /**
     * 
     */
    public void testPrepareWhereClause_inclduesVersion() {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchUpdateImpl<Eee> query = new AutoBatchUpdateImpl<Eee>(manager,
                entities);
        query.includesVersion();
        query.prepare("execute");
        assertEquals(" where ID = ?", query.whereClause.toSql());
    }

    /**
     * 
     */
    public void testPrepareSql() {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchUpdateImpl<Eee> query = new AutoBatchUpdateImpl<Eee>(manager,
                entities);
        query.prepare("execute");
        assertEquals(
                "update EEE set NAME = ?, FFF_ID = ?, VERSION = VERSION + 1 where ID = ? and VERSION = ?",
                query.executedSql);
    }

    /**
     * 
     */
    public void testPrepareSql_includesVersion() {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchUpdateImpl<Eee> query = new AutoBatchUpdateImpl<Eee>(manager,
                entities);
        query.includesVersion();
        query.prepare("execute");
        assertEquals(
                "update EEE set NAME = ?, FFF_ID = ?, VERSION = ? where ID = ?",
                query.executedSql);
    }

    /**
     * 
     */
    public void testPrepareParams() {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchUpdateImpl<Eee> query = new AutoBatchUpdateImpl<Eee>(manager,
                entities);
        query.prepare("execute");

        query.prepareParams(entities.get(0));
        assertEquals(4, query.getParamSize());
        assertEquals("foo", query.getParam(0).value);
        assertNull(query.getParam(1).value);
        assertEquals(new Integer(1), query.getParam(2).value);
        assertEquals(new Long(0L), query.getParam(3).value);
        query.resetParams();

        query.prepareParams(entities.get(1));
        assertEquals(4, query.getParamSize());
        assertEquals("bar", query.getParam(0).value);
        assertNull(query.getParam(1).value);
        assertEquals(new Integer(2), query.getParam(2).value);
        assertEquals(new Long(0L), query.getParam(3).value);
        query.resetParams();

        query.prepareParams(entities.get(2));
        assertEquals(4, query.getParamSize());
        assertEquals("baz", query.getParam(0).value);
        assertNull(query.getParam(1).value);
        assertEquals(new Integer(3), query.getParam(2).value);
        assertEquals(new Long(0L), query.getParam(3).value);
    }

    /**
     * 
     */
    public void testPrepareParams_includesVersion() {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchUpdateImpl<Eee> query = new AutoBatchUpdateImpl<Eee>(manager,
                entities);
        query.includesVersion();
        query.prepare("execute");

        query.prepareParams(entities.get(0));
        assertEquals(4, query.getParamSize());
        assertEquals("foo", query.getParam(0).value);
        assertNull(query.getParam(1).value);
        assertEquals(new Long(0L), query.getParam(2).value);
        assertEquals(new Integer(1), query.getParam(3).value);
        query.resetParams();

        query.prepareParams(entities.get(1));
        assertEquals(4, query.getParamSize());
        assertEquals("bar", query.getParam(0).value);
        assertNull(query.getParam(1).value);
        assertEquals(new Long(0L), query.getParam(2).value);
        assertEquals(new Integer(2), query.getParam(3).value);
        query.resetParams();

        query.prepareParams(entities.get(2));
        assertEquals(4, query.getParamSize());
        assertEquals("baz", query.getParam(0).value);
        assertNull(query.getParam(1).value);
        assertEquals(new Long(0L), query.getParam(2).value);
        assertEquals(new Integer(3), query.getParam(3).value);
    }

    /**
     * @throws Exception
     */
    public void testExecute() throws Exception {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchUpdateImpl<Eee> query = new AutoBatchUpdateImpl<Eee>(manager,
                entities) {

            @Override
            protected PreparedStatement getPreparedStatement(
                    JdbcContext jdbcContext) {
                MockPreparedStatement ps = new MockPreparedStatement(null, null) {

                    @Override
                    public int[] executeBatch() throws SQLException {
                        return new int[] { 1, 1, 1 };
                    }

                    @Override
                    public void addBatch() throws SQLException {
                        ++addBatchCalled;
                    }
                };
                return ps;
            }

        };
        int[] result = query.executeBatch();
        assertEquals(3, addBatchCalled);
        assertEquals(3, result.length);
        SqlLog sqlLog = SqlLogRegistryLocator.getInstance().getLast();
        assertEquals(
                "update EEE set NAME = 'baz', FFF_ID = null, VERSION = VERSION + 1 where ID = 3 and VERSION = 0",
                sqlLog.getCompleteSql());
    }

    /**
     * @throws Exception
     */
    public void testExecute_includesVersion() throws Exception {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchUpdateImpl<Eee> query = new AutoBatchUpdateImpl<Eee>(manager,
                entities) {

            @Override
            protected PreparedStatement getPreparedStatement(
                    JdbcContext jdbcContext) {
                MockPreparedStatement ps = new MockPreparedStatement(null, null) {

                    @Override
                    public int[] executeBatch() throws SQLException {
                        return new int[] { 1, 1, 1 };
                    }

                    @Override
                    public void addBatch() throws SQLException {
                        ++addBatchCalled;
                    }
                };
                return ps;
            }

        };
        query.includesVersion();
        int[] result = query.executeBatch();
        assertEquals(3, addBatchCalled);
        assertEquals(3, result.length);
        SqlLog sqlLog = SqlLogRegistryLocator.getInstance().getLast();
        assertEquals(
                "update EEE set NAME = 'baz', FFF_ID = null, VERSION = 0 where ID = 3",
                sqlLog.getCompleteSql());
    }

    /**
     * @throws Exception
     */
    public void testOptimisticLock() throws Exception {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchUpdateImpl<Eee> query = new AutoBatchUpdateImpl<Eee>(manager,
                entities) {

            @Override
            protected PreparedStatement getPreparedStatement(
                    JdbcContext jdbcContext) {
                MockPreparedStatement ps = new MockPreparedStatement(null, null) {

                    @Override
                    public int[] executeBatch() throws SQLException {
                        return new int[] { 1, 1, 0 };
                    }

                    @Override
                    public void addBatch() throws SQLException {
                        ++addBatchCalled;
                    }
                };
                return ps;
            }

        };
        try {
            query.executeBatch();
            fail();
        } catch (OptimisticLockException expected) {
            expected.printStackTrace();
            assertSame(entities.get(2), expected.getEntity());
        }
    }

    /**
     * @throws Exception
     */
    public void testOptimisticLock_includesVersion() throws Exception {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchUpdateImpl<Eee> query = new AutoBatchUpdateImpl<Eee>(manager,
                entities) {

            @Override
            protected PreparedStatement getPreparedStatement(
                    JdbcContext jdbcContext) {
                MockPreparedStatement ps = new MockPreparedStatement(null, null) {

                    @Override
                    public int[] executeBatch() throws SQLException {
                        return new int[] { 1, 1, 0 };
                    }

                    @Override
                    public void addBatch() throws SQLException {
                        ++addBatchCalled;
                    }
                };
                return ps;
            }

        };
        query.includesVersion();
        int[] result = query.executeBatch();
        assertEquals(3, addBatchCalled);
        assertEquals(3, result.length);
        assertEquals(1, result[0]);
        assertEquals(1, result[1]);
        assertEquals(0, result[2]);
    }

}
