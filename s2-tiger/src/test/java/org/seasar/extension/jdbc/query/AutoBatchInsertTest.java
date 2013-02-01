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
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

import org.seasar.extension.jdbc.JdbcContext;
import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.SqlLog;
import org.seasar.extension.jdbc.SqlLogRegistry;
import org.seasar.extension.jdbc.SqlLogRegistryLocator;
import org.seasar.extension.jdbc.dialect.Db2Dialect;
import org.seasar.extension.jdbc.dialect.HsqlDialect;
import org.seasar.extension.jdbc.dialect.MssqlDialect;
import org.seasar.extension.jdbc.dialect.OracleDialect;
import org.seasar.extension.jdbc.dialect.StandardDialect;
import org.seasar.extension.jdbc.entity.Eee;
import org.seasar.extension.jdbc.entity.Fff;
import org.seasar.extension.jdbc.entity.Identity;
import org.seasar.extension.jdbc.entity.Sequence;
import org.seasar.extension.jdbc.exception.IdentityGeneratorNotSupportedRuntimeException;
import org.seasar.extension.jdbc.exception.QueryTwiceExecutionRuntimeException;
import org.seasar.extension.jdbc.exception.SequenceGeneratorNotSupportedRuntimeException;
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

/**
 * @author koichik
 */
public class AutoBatchInsertTest extends TestCase {

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
        AutoBatchInsertImpl<Eee> query = new AutoBatchInsertImpl<Eee>(manager,
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
        AutoBatchInsertImpl<Eee> query = new AutoBatchInsertImpl<Eee>(manager,
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
        AutoBatchInsertImpl<Eee> query = new AutoBatchInsertImpl<Eee>(manager,
                entities);
        assertSame(query, query.queryTimeout(100));
        assertEquals(100, query.queryTimeout);
    }

    /**
     * 
     */
    public void testIncludes() {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchInsertImpl<Eee> query = new AutoBatchInsertImpl<Eee>(manager,
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
        AutoBatchInsertImpl<Eee> query = new AutoBatchInsertImpl<Eee>(manager,
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
        AutoBatchInsertImpl<Eee> query = new AutoBatchInsertImpl<Eee>(manager,
                entities);
        query.prepareTargetProperties();
        assertEquals(5, query.targetProperties.size());
        assertEquals("id", query.targetProperties.get(0).getName());
        assertEquals("name", query.targetProperties.get(1).getName());
        assertEquals("longText", query.targetProperties.get(2).getName());
        assertEquals("fffId", query.targetProperties.get(3).getName());
        assertEquals("version", query.targetProperties.get(4).getName());
    }

    /**
     * 
     */
    public void testPrepareTarget_identityNotSupported() {
        manager.setDialect(new OracleDialect());
        List<Identity> entities = Arrays.asList(new Identity(), new Identity(),
                new Identity());
        AutoBatchInsertImpl<Identity> query = new AutoBatchInsertImpl<Identity>(
                manager, entities);
        try {
            query.prepareTargetProperties();
            fail();
        } catch (IdentityGeneratorNotSupportedRuntimeException expected) {
        }
    }

    /**
     * 
     */
    public void testPrepareTarget_sequenceNotSupported() {
        manager.setDialect(new MssqlDialect());
        List<Sequence> entities = Arrays.asList(new Sequence(), new Sequence(),
                new Sequence());
        AutoBatchInsertImpl<Sequence> query = new AutoBatchInsertImpl<Sequence>(
                manager, entities);
        try {
            query.prepareTargetProperties();
            fail();
        } catch (SequenceGeneratorNotSupportedRuntimeException expected) {
        }
    }

    /**
     * 
     */
    public void testPrepareTarget_includes() {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchInsertImpl<Eee> query = new AutoBatchInsertImpl<Eee>(manager,
                entities);
        query.includes("name");
        query.prepareTargetProperties();
        assertEquals(2, query.targetProperties.size());
        assertEquals("id", query.targetProperties.get(0).getName());
        assertEquals("name", query.targetProperties.get(1).getName());
    }

    /**
     * 
     */
    public void testPrepareTarget_excludes() {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchInsertImpl<Eee> query = new AutoBatchInsertImpl<Eee>(manager,
                entities);
        query.excludes("name");
        query.prepareTargetProperties();
        assertEquals(4, query.targetProperties.size());
        assertEquals("id", query.targetProperties.get(0).getName());
        assertEquals("longText", query.targetProperties.get(1).getName());
        assertEquals("fffId", query.targetProperties.get(2).getName());
        assertEquals("version", query.targetProperties.get(3).getName());
    }

    /**
     * 
     */
    public void testPrepareTarget_includesAndExcludes() {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchInsertImpl<Eee> query = new AutoBatchInsertImpl<Eee>(manager,
                entities);
        query.includes("name", "fffId").excludes("fffId");
        query.prepareTargetProperties();
        assertEquals(2, query.targetProperties.size());
        assertEquals("id", query.targetProperties.get(0).getName());
        assertEquals("name", query.targetProperties.get(1).getName());
    }

    /**
     * 
     */
    public void testPrepareIntClause() {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchInsertImpl<Eee> query = new AutoBatchInsertImpl<Eee>(manager,
                entities);
        query.prepare("execute");
        assertEquals(" (ID, NAME, LONG_TEXT, FFF_ID, VERSION)",
                query.intoClause.toSql());
    }

    /**
     * 
     */
    public void testPrepareValuesClause() {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchInsertImpl<Eee> query = new AutoBatchInsertImpl<Eee>(manager,
                entities);
        query.prepare("execute");
        assertEquals(" values (?, ?, ?, ?, ?)", query.valuesClause.toSql());
    }

    /**
     * 
     */
    public void testPrepareSql() {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchInsertImpl<Eee> query = new AutoBatchInsertImpl<Eee>(manager,
                entities);
        query.prepare("execute");
        assertEquals(
                "insert into EEE (ID, NAME, LONG_TEXT, FFF_ID, VERSION) values (?, ?, ?, ?, ?)",
                query.executedSql);
    }

    /**
     * 
     */
    public void testPrepareParams() {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchInsertImpl<Eee> query = new AutoBatchInsertImpl<Eee>(manager,
                entities);
        query.prepare("execute");

        query.prepareParams(entities.get(0));
        assertEquals(5, query.getParamSize());
        assertEquals(new Integer(1), query.getParam(0).value);
        assertEquals("foo", query.getParam(1).value);
        assertNull(query.getParam(2).value);
        assertTrue(query.getParam(2).valueType instanceof StringClobType);
        assertNull(query.getParam(3).value);
        assertEquals(new Long(1L), query.getParam(4).value);
        assertEquals(new Long(1L), entities.get(0).version);
        query.resetParams();

        query.prepareParams(entities.get(1));
        assertEquals(5, query.getParamSize());
        assertEquals(new Integer(2), query.getParam(0).value);
        assertEquals("bar", query.getParam(1).value);
        assertNull(query.getParam(2).value);
        assertTrue(query.getParam(2).valueType instanceof StringClobType);
        assertNull(query.getParam(3).value);
        assertEquals(new Long(1L), query.getParam(4).value);
        assertEquals(new Long(1L), entities.get(1).version);
        query.resetParams();

        query.prepareParams(entities.get(2));
        assertEquals(5, query.getParamSize());
        assertEquals(new Integer(3), query.getParam(0).value);
        assertEquals("baz", query.getParam(1).value);
        assertNull(query.getParam(2).value);
        assertTrue(query.getParam(2).valueType instanceof StringClobType);
        assertNull(query.getParam(3).value);
        assertEquals(new Long(1L), query.getParam(4).value);
        assertEquals(new Long(1L), entities.get(1).version);
    }

    /**
     * @throws Exception
     */
    public void testExecute() throws Exception {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchInsertImpl<Eee> query = new AutoBatchInsertImpl<Eee>(manager,
                entities) {

            @Override
            protected PreparedStatement createPreparedStatement(
                    JdbcContext jdbcContext) {
                assertFalse(useGetGeneratedKeys);
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
        int[] result = query.execute();
        assertEquals(3, addBatchCalled);
        assertEquals(3, result.length);
        SqlLog sqlLog = SqlLogRegistryLocator.getInstance().getLast();
        assertEquals(
                "insert into EEE (ID, NAME, LONG_TEXT, FFF_ID, VERSION) values (3, 'baz', null, null, 1)",
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
    public void testExecute_identity() throws Exception {
        manager.setDialect(new Db2Dialect());
        List<Fff> entities = Arrays.asList(new Fff("foo"), new Fff("bar"),
                new Fff("baz"));
        AutoBatchInsertImpl<Fff> query = new AutoBatchInsertImpl<Fff>(manager,
                entities) {

            @Override
            protected PreparedStatement createPreparedStatement(
                    JdbcContext jdbcContext) {
                assertTrue(useGetGeneratedKeys);
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

            @Override
            protected Object getIdValue(PropertyMeta propertyMeta, Fff entity) {
                return null;
            }

            @Override
            protected void postExecute(PreparedStatement ps, Fff entity) {
            }

        };
        int[] result = query.execute();
        assertEquals(0, addBatchCalled);
        assertEquals(3, result.length);
        SqlLog sqlLog = SqlLogRegistryLocator.getInstance().getLast();
        assertEquals("insert into FFF (NAME, VERSION) values ('baz', 1)",
                sqlLog.getCompleteSql());
    }

    /**
     * @throws Exception
     */
    public void testExecute_identityInto() throws Exception {
        manager.setDialect(new HsqlDialect());
        List<Fff> entities = Arrays.asList(new Fff("foo"), new Fff("bar"),
                new Fff("baz"));
        AutoBatchInsertImpl<Fff> query = new AutoBatchInsertImpl<Fff>(manager,
                entities) {

            @Override
            protected PreparedStatement createPreparedStatement(
                    JdbcContext jdbcContext) {
                assertFalse(useGetGeneratedKeys);
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

            @Override
            protected Object getIdValue(PropertyMeta propertyMeta, Fff entity) {
                return null;
            }

            @Override
            protected void postExecute(PreparedStatement ps, Fff entity) {
            }

        };
        int[] result = query.execute();
        assertEquals(0, addBatchCalled);
        assertEquals(3, result.length);
        SqlLog sqlLog = SqlLogRegistryLocator.getInstance().getLast();
        assertEquals(
                "insert into FFF (ID, NAME, VERSION) values (null, 'baz', 1)",
                sqlLog.getCompleteSql());
    }

    /**
     * @throws Exception
     */
    public void testExecute_sequence() throws Exception {
        manager.setDialect(new OracleDialect());
        List<Fff> entities = Arrays.asList(new Fff("foo"), new Fff("bar"),
                new Fff("baz"));
        AutoBatchInsertImpl<Fff> query = new AutoBatchInsertImpl<Fff>(manager,
                entities) {

            @Override
            protected PreparedStatement createPreparedStatement(
                    JdbcContext jdbcContext) {
                assertFalse(useGetGeneratedKeys);
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

            @Override
            protected Object getIdValue(PropertyMeta propertyMeta, Fff entity) {
                return 10L;
            }

            @Override
            protected void postExecute(PreparedStatement ps, Fff entity) {
            }

        };
        int[] result = query.execute();
        assertEquals(3, addBatchCalled);
        assertEquals(3, result.length);
        SqlLog sqlLog = SqlLogRegistryLocator.getInstance().getLast();
        assertEquals(
                "insert into FFF (ID, NAME, VERSION) values (10, 'baz', 1)",
                sqlLog.getCompleteSql());
    }

    /**
     * @throws Exception
     */
    public void testExecute_table() throws Exception {
        manager.setDialect(new StandardDialect());
        List<Fff> entities = Arrays.asList(new Fff("foo"), new Fff("bar"),
                new Fff("baz"));
        AutoBatchInsertImpl<Fff> query = new AutoBatchInsertImpl<Fff>(manager,
                entities) {

            @Override
            protected PreparedStatement createPreparedStatement(
                    JdbcContext jdbcContext) {
                assertFalse(useGetGeneratedKeys);
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

            @Override
            protected Object getIdValue(PropertyMeta propertyMeta, Fff entity) {
                return 10L;
            }

            @Override
            protected void postExecute(PreparedStatement ps, Fff entity) {
            }

        };
        int[] result = query.execute();
        assertEquals(3, addBatchCalled);
        assertEquals(3, result.length);
        SqlLog sqlLog = SqlLogRegistryLocator.getInstance().getLast();
        assertEquals(
                "insert into FFF (ID, NAME, VERSION) values (10, 'baz', 1)",
                sqlLog.getCompleteSql());
    }

}
