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
public class AutoInsertTest extends TestCase {

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
        AutoInsertImpl<Eee> query = new AutoInsertImpl<Eee>(manager, new Eee());
        assertSame(query, query.callerClass(getClass()));
        assertEquals(getClass(), query.callerClass);
    }

    /**
     * 
     */
    public void testCallerMethodName() {
        AutoInsertImpl<Eee> query = new AutoInsertImpl<Eee>(manager, new Eee());
        assertSame(query, query.callerMethodName("hoge"));
        assertEquals("hoge", query.callerMethodName);
    }

    /**
     * 
     */
    public void testQueryTimeout() {
        AutoInsertImpl<Eee> query = new AutoInsertImpl<Eee>(manager, new Eee());
        assertSame(query, query.queryTimeout(100));
        assertEquals(100, query.queryTimeout);
    }

    /**
     * 
     */
    public void testExcludesNull() {
        AutoInsertImpl<Eee> query = new AutoInsertImpl<Eee>(manager, new Eee());
        assertFalse(query.excludesNull);
        assertSame(query, query.excludesNull());
        assertTrue(query.excludesNull);
    }

    /**
     * 
     */
    public void testIncludes() {
        AutoInsertImpl<Eee> query = new AutoInsertImpl<Eee>(manager, new Eee());
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
        AutoInsertImpl<Eee> query = new AutoInsertImpl<Eee>(manager, new Eee());
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
        Eee eee = new Eee();
        eee.id = 100;
        eee.name = "hoge";
        eee.version = 1L;
        AutoInsertImpl<Eee> query = new AutoInsertImpl<Eee>(manager, eee);
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
    public void testPrepareTarget_identity() {
        manager.setDialect(new Db2Dialect());
        Fff fff = new Fff();
        fff.name = "hoge";
        fff.version = 1L;
        AutoInsertImpl<Fff> query = new AutoInsertImpl<Fff>(manager, fff);
        query.prepareTargetProperties();
        assertEquals(2, query.targetProperties.size());
        assertEquals("name", query.targetProperties.get(0).getName());
        assertEquals("version", query.targetProperties.get(1).getName());
    }

    /**
     * 
     */
    public void testPrepareTarget_identityInto() {
        manager.setDialect(new HsqlDialect());
        Fff fff = new Fff();
        fff.name = "hoge";
        fff.version = 1L;
        AutoInsertImpl<Fff> query = new AutoInsertImpl<Fff>(manager, fff);
        query.prepareTargetProperties();
        assertEquals(3, query.targetProperties.size());
        assertEquals("id", query.targetProperties.get(0).getName());
        assertEquals("name", query.targetProperties.get(1).getName());
        assertEquals("version", query.targetProperties.get(2).getName());
    }

    /**
     * 
     */
    public void testPrepareTarget_identityNotSupported() {
        manager.setDialect(new OracleDialect());
        Identity entity = new Identity();
        AutoInsertImpl<Identity> query = new AutoInsertImpl<Identity>(manager,
                entity);
        try {
            query.prepareTargetProperties();
            fail();
        } catch (IdentityGeneratorNotSupportedRuntimeException expected) {
            expected.printStackTrace();
        }
    }

    /**
     * 
     */
    public void testPrepareTarget_sequence() {
        manager.setDialect(new OracleDialect());
        Fff fff = new Fff();
        fff.name = "hoge";
        fff.version = 1L;
        AutoInsertImpl<Fff> query = new AutoInsertImpl<Fff>(manager, fff);
        query.prepareTargetProperties();
        assertEquals(3, query.targetProperties.size());
        assertEquals("id", query.targetProperties.get(0).getName());
        assertEquals("name", query.targetProperties.get(1).getName());
        assertEquals("version", query.targetProperties.get(2).getName());
    }

    /**
     * 
     */
    public void testPrepareTarget_sequenceNotSupported() {
        manager.setDialect(new MssqlDialect());
        Sequence entity = new Sequence();
        AutoInsertImpl<Sequence> query = new AutoInsertImpl<Sequence>(manager,
                entity);
        try {
            query.prepareTargetProperties();
            fail();
        } catch (SequenceGeneratorNotSupportedRuntimeException expected) {
            expected.printStackTrace();
        }
    }

    /**
     * 
     */
    public void testPrepareTarget_table() {
        manager.setDialect(new StandardDialect());
        Fff fff = new Fff();
        fff.name = "hoge";
        fff.version = 1L;
        AutoInsertImpl<Fff> query = new AutoInsertImpl<Fff>(manager, fff);
        query.prepareTargetProperties();
        assertEquals(3, query.targetProperties.size());
        assertEquals("id", query.targetProperties.get(0).getName());
        assertEquals("name", query.targetProperties.get(1).getName());
        assertEquals("version", query.targetProperties.get(2).getName());
    }

    /**
     * 
     */
    public void testPrepareTarget_excludesNull() {
        Eee eee = new Eee();
        eee.id = 100;
        eee.name = "hoge";
        eee.version = 1L;
        AutoInsertImpl<Eee> query = new AutoInsertImpl<Eee>(manager, eee);
        query.excludesNull();
        query.prepareTargetProperties();
        assertEquals(3, query.targetProperties.size());
        assertEquals("id", query.targetProperties.get(0).getName());
        assertEquals("name", query.targetProperties.get(1).getName());
        assertEquals("version", query.targetProperties.get(2).getName());
    }

    /**
     * 
     */
    public void testPrepareTarget_includes() {
        Eee eee = new Eee();
        eee.id = 100;
        eee.name = "hoge";
        eee.version = 1L;
        AutoInsertImpl<Eee> query = new AutoInsertImpl<Eee>(manager, eee);
        query.includes("id", "version");
        query.prepareTargetProperties();
        assertEquals(2, query.targetProperties.size());
        assertEquals("id", query.targetProperties.get(0).getName());
        assertEquals("version", query.targetProperties.get(1).getName());
    }

    /**
     * 
     */
    public void testPrepareTarget_excludes() {
        Eee eee = new Eee();
        eee.id = 100;
        eee.name = "hoge";
        eee.version = 1L;
        AutoInsertImpl<Eee> query = new AutoInsertImpl<Eee>(manager, eee);
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
        Eee eee = new Eee();
        eee.id = 100;
        eee.name = "hoge";
        eee.version = 1L;
        AutoInsertImpl<Eee> query = new AutoInsertImpl<Eee>(manager, eee);
        query.includes("id", "name", "fffId").excludes("fffId");
        query.prepareTargetProperties();
        assertEquals(2, query.targetProperties.size());
        assertEquals("id", query.targetProperties.get(0).getName());
        assertEquals("name", query.targetProperties.get(1).getName());
    }

    /**
     * 
     */
    public void testPrepareTarget_includesAndExcludesNull() {
        Eee eee = new Eee();
        eee.id = 100;
        eee.name = "hoge";
        eee.version = 1L;
        AutoInsertImpl<Eee> query = new AutoInsertImpl<Eee>(manager, eee);
        query.includes("id", "name", "fffId").excludesNull();
        query.prepareTargetProperties();
        assertEquals(2, query.targetProperties.size());
        assertEquals("id", query.targetProperties.get(0).getName());
        assertEquals("name", query.targetProperties.get(1).getName());
    }

    /**
     * 
     */
    public void testPrepareIntoClause() {
        Eee eee = new Eee();
        eee.id = 100;
        eee.name = "hoge";
        eee.version = 1L;
        AutoInsertImpl<Eee> query = new AutoInsertImpl<Eee>(manager, eee);
        query.prepare("execute");
        assertEquals(" (ID, NAME, LONG_TEXT, FFF_ID, VERSION)",
                query.intoClause.toSql());
    }

    /**
     * 
     */
    public void testPrepareIntoClause_identity() {
        manager.setDialect(new Db2Dialect());
        Fff fff = new Fff();
        fff.name = "hoge";
        fff.version = 1L;
        AutoInsertImpl<Fff> query = new AutoInsertImpl<Fff>(manager, fff);
        query.prepare("execute");
        assertEquals(" (NAME, VERSION)", query.intoClause.toSql());
    }

    /**
     * 
     */
    public void testPrepareIntoClause_identityInto() {
        manager.setDialect(new HsqlDialect());
        Fff fff = new Fff();
        fff.name = "hoge";
        fff.version = 1L;
        AutoInsertImpl<Fff> query = new AutoInsertImpl<Fff>(manager, fff);
        query.prepare("execute");
        assertEquals(" (ID, NAME, VERSION)", query.intoClause.toSql());
    }

    /**
     * 
     */
    public void testPrepareIntoClause_sequence() {
        manager.setDialect(new OracleDialect());
        Fff fff = new Fff();
        fff.name = "hoge";
        fff.version = 1L;
        AutoInsertImpl<Fff> query = new AutoInsertImpl<Fff>(manager, fff) {

            @Override
            protected Object getIdValue(PropertyMeta propertyMeta) {
                return 10L;
            }

        };
        query.prepare("execute");
        assertEquals(" (ID, NAME, VERSION)", query.intoClause.toSql());
    }

    /**
     * 
     */
    public void testPrepareIntoClause_table() {
        manager.setDialect(new StandardDialect());
        Fff fff = new Fff();
        fff.name = "hoge";
        fff.version = 1L;
        AutoInsertImpl<Fff> query = new AutoInsertImpl<Fff>(manager, fff) {

            @Override
            protected Object getIdValue(PropertyMeta propertyMeta) {
                return 10L;
            }

        };
        query.prepare("execute");
        assertEquals(" (ID, NAME, VERSION)", query.intoClause.toSql());
    }

    /**
     * 
     */
    public void testPrepareValuesClause() {
        Eee eee = new Eee();
        eee.id = 100;
        eee.name = "hoge";
        eee.version = 1L;
        AutoInsertImpl<Eee> query = new AutoInsertImpl<Eee>(manager, eee);
        query.prepare("execute");
        assertEquals(" values (?, ?, ?, ?, ?)", query.valuesClause.toSql());
    }

    /**
     * 
     */
    public void testPrepareValuesClause_identity() {
        manager.setDialect(new Db2Dialect());
        Fff fff = new Fff();
        fff.name = "hoge";
        fff.version = 1L;
        AutoInsertImpl<Fff> query = new AutoInsertImpl<Fff>(manager, fff);
        query.prepare("execute");
        assertEquals(" values (?, ?)", query.valuesClause.toSql());
    }

    /**
     * 
     */
    public void testPrepareValuesClause_identityInto() {
        manager.setDialect(new HsqlDialect());
        Fff fff = new Fff();
        fff.name = "hoge";
        fff.version = 1L;
        AutoInsertImpl<Fff> query = new AutoInsertImpl<Fff>(manager, fff);
        query.prepare("execute");
        assertEquals(" values (?, ?, ?)", query.valuesClause.toSql());
    }

    /**
     * 
     */
    public void testPrepareValuesClause_sequence() {
        manager.setDialect(new OracleDialect());
        Fff fff = new Fff();
        fff.name = "hoge";
        fff.version = 1L;
        AutoInsertImpl<Fff> query = new AutoInsertImpl<Fff>(manager, fff) {

            @Override
            protected Object getIdValue(PropertyMeta propertyMeta) {
                return 10L;
            }

        };
        query.prepare("execute");
        assertEquals(" values (?, ?, ?)", query.valuesClause.toSql());
    }

    /**
     * 
     */
    public void testPrepareValuesClause_table() {
        manager.setDialect(new StandardDialect());
        Fff fff = new Fff();
        fff.name = "hoge";
        fff.version = 1L;
        AutoInsertImpl<Fff> query = new AutoInsertImpl<Fff>(manager, fff) {

            @Override
            protected Object getIdValue(PropertyMeta propertyMeta) {
                return 10L;
            }

        };
        query.prepare("execute");
        assertEquals(" values (?, ?, ?)", query.valuesClause.toSql());
    }

    /**
     * 
     */
    public void testPrepareParams() {
        Eee eee = new Eee();
        eee.id = 100;
        eee.name = "hoge";
        AutoInsertImpl<Eee> query = new AutoInsertImpl<Eee>(manager, eee);
        query.prepare("execute");
        assertEquals(5, query.getParamSize());
        assertEquals(new Integer(100), query.getParam(0).value);
        assertEquals("hoge", query.getParam(1).value);
        assertNull(query.getParam(2).value);
        assertTrue(query.getParam(2).valueType instanceof StringClobType);
        assertNull(query.getParam(3).value);
        assertEquals(new Long(1L), query.getParam(4).value);
        assertEquals(new Long(1), eee.version);
    }

    /**
     * 
     */
    public void testPrepareParams_identity() {
        manager.setDialect(new Db2Dialect());
        Fff fff = new Fff();
        fff.name = "hoge";
        fff.version = 1L;
        AutoInsertImpl<Fff> query = new AutoInsertImpl<Fff>(manager, fff);
        query.prepare("execute");
        assertEquals(2, query.getParamSize());
        assertEquals("hoge", query.getParam(0).value);
        assertEquals(new Long(1L), query.getParam(1).value);
    }

    /**
     * 
     */
    public void testPrepareParams_identityInto() {
        manager.setDialect(new HsqlDialect());
        Fff fff = new Fff();
        fff.name = "hoge";
        fff.version = 1L;
        AutoInsertImpl<Fff> query = new AutoInsertImpl<Fff>(manager, fff);
        query.prepare("execute");
        assertEquals(3, query.getParamSize());
        assertNull(query.getParam(0).value);
        assertEquals("hoge", query.getParam(1).value);
        assertEquals(new Long(1L), query.getParam(2).value);
    }

    /**
     * 
     */
    public void testPrepareParams_sequence() {
        manager.setDialect(new OracleDialect());
        Fff fff = new Fff();
        fff.name = "hoge";
        fff.version = 1L;
        AutoInsertImpl<Fff> query = new AutoInsertImpl<Fff>(manager, fff) {

            @Override
            protected Object getIdValue(PropertyMeta propertyMeta) {
                return 10L;
            }

        };
        query.prepare("execute");
        assertEquals(3, query.getParamSize());
        assertEquals(new Long(10), query.getParam(0).value);
        assertEquals("hoge", query.getParam(1).value);
        assertEquals(new Long(1L), query.getParam(2).value);
    }

    /**
     * 
     */
    public void testPrepareParams_table() {
        manager.setDialect(new StandardDialect());
        Fff fff = new Fff();
        fff.name = "hoge";
        fff.version = 1L;
        AutoInsertImpl<Fff> query = new AutoInsertImpl<Fff>(manager, fff) {

            @Override
            protected Object getIdValue(PropertyMeta propertyMeta) {
                return 10L;
            }

        };
        query.prepare("execute");
        assertEquals(3, query.getParamSize());
        assertEquals(new Long(10), query.getParam(0).value);
        assertEquals("hoge", query.getParam(1).value);
        assertEquals(new Long(1L), query.getParam(2).value);
    }

    /**
     * @throws Exception
     */
    public void testPrepareParams_excludesNull() throws Exception {
        Eee eee = new Eee();
        eee.id = 100;
        eee.name = "hoge";
        eee.version = 1L;
        AutoInsertImpl<Eee> query = new AutoInsertImpl<Eee>(manager, eee);
        query.excludesNull();
        query.prepare("execute");
        assertEquals(3, query.getParamSize());
        assertEquals(new Integer(100), query.getParam(0).value);
        assertEquals("hoge", query.getParam(1).value);
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
        AutoInsertImpl<Eee> query = new AutoInsertImpl<Eee>(manager, eee);
        query.prepare("execute");
        assertEquals(
                "insert into EEE (ID, NAME, LONG_TEXT, FFF_ID, VERSION) values (?, ?, ?, ?, ?)",
                query.executedSql);
    }

    /**
     * 
     */
    public void testPrepareSql_identity() {
        manager.setDialect(new Db2Dialect());
        Fff fff = new Fff();
        fff.name = "hoge";
        fff.version = 1L;
        AutoInsertImpl<Fff> query = new AutoInsertImpl<Fff>(manager, fff);
        query.prepare("execute");
        assertEquals("insert into FFF (NAME, VERSION) values (?, ?)",
                query.executedSql);
    }

    /**
     * 
     */
    public void testPrepareSql_identityInto() {
        manager.setDialect(new HsqlDialect());
        Fff fff = new Fff();
        fff.name = "hoge";
        fff.version = 1L;
        AutoInsertImpl<Fff> query = new AutoInsertImpl<Fff>(manager, fff);
        query.prepare("execute");
        assertEquals("insert into FFF (ID, NAME, VERSION) values (?, ?, ?)",
                query.executedSql);
    }

    /**
     * 
     */
    public void testPrepareSql_sequence() {
        manager.setDialect(new OracleDialect());
        Fff fff = new Fff();
        fff.name = "hoge";
        fff.version = 1L;
        AutoInsertImpl<Fff> query = new AutoInsertImpl<Fff>(manager, fff) {

            @Override
            protected Object getIdValue(PropertyMeta propertyMeta) {
                return 10L;
            }

        };
        query.prepare("execute");
        assertEquals("insert into FFF (ID, NAME, VERSION) values (?, ?, ?)",
                query.executedSql);
    }

    /**
     * 
     */
    public void testPrepareSql_table() {
        manager.setDialect(new StandardDialect());
        Fff fff = new Fff();
        fff.name = "hoge";
        fff.version = 1L;
        AutoInsertImpl<Fff> query = new AutoInsertImpl<Fff>(manager, fff) {

            @Override
            protected Object getIdValue(PropertyMeta propertyMeta) {
                return 10L;
            }

        };
        query.prepare("execute");
        assertEquals("insert into FFF (ID, NAME, VERSION) values (?, ?, ?)",
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
        AutoInsertImpl<Eee> query = new AutoInsertImpl<Eee>(manager, eee);
        query.excludesNull();
        query.prepare("execute");
        assertEquals("insert into EEE (ID, NAME, VERSION) values (?, ?, ?)",
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
        AutoInsertImpl<Eee> query = new AutoInsertImpl<Eee>(manager, eee) {

            @Override
            protected PreparedStatement createPreparedStatement(
                    JdbcContext jdbcContext) {
                assertFalse(useGetGeneratedKeys);
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
                "insert into EEE (ID, NAME, LONG_TEXT, FFF_ID, VERSION) values (100, 'hoge', null, null, 1)",
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
        Fff fff = new Fff();
        fff.name = "hoge";
        fff.version = 1L;
        AutoInsertImpl<Fff> query = new AutoInsertImpl<Fff>(manager, fff) {

            @Override
            protected PreparedStatement createPreparedStatement(
                    JdbcContext jdbcContext) {
                assertTrue(useGetGeneratedKeys);
                MockPreparedStatement ps = new MockPreparedStatement(null, null) {

                    @Override
                    public int executeUpdate() throws SQLException {
                        return 1;
                    }
                };
                return ps;
            }

            @Override
            protected void postExecute(PreparedStatement ps) {
            }

        };
        assertEquals(1, query.execute());
        SqlLog sqlLog = SqlLogRegistryLocator.getInstance().getLast();
        assertEquals("insert into FFF (NAME, VERSION) values ('hoge', 1)",
                sqlLog.getCompleteSql());
    }

    /**
     * @throws Exception
     */
    public void testExecute_identityInto() throws Exception {
        manager.setDialect(new HsqlDialect());
        Fff fff = new Fff();
        fff.name = "hoge";
        fff.version = 1L;
        AutoInsertImpl<Fff> query = new AutoInsertImpl<Fff>(manager, fff) {

            @Override
            protected PreparedStatement createPreparedStatement(
                    JdbcContext jdbcContext) {
                assertFalse(useGetGeneratedKeys);
                MockPreparedStatement ps = new MockPreparedStatement(null, null) {

                    @Override
                    public int executeUpdate() throws SQLException {
                        return 1;
                    }
                };
                return ps;
            }

            @Override
            protected void postExecute(PreparedStatement ps) {
            }

        };
        assertEquals(1, query.execute());
        SqlLog sqlLog = SqlLogRegistryLocator.getInstance().getLast();
        assertEquals(
                "insert into FFF (ID, NAME, VERSION) values (null, 'hoge', 1)",
                sqlLog.getCompleteSql());
    }

    /**
     * @throws Exception
     */
    public void testExecute_sequence() throws Exception {
        manager.setDialect(new OracleDialect());
        Fff fff = new Fff();
        fff.name = "hoge";
        fff.version = 1L;
        AutoInsertImpl<Fff> query = new AutoInsertImpl<Fff>(manager, fff) {

            @Override
            protected PreparedStatement createPreparedStatement(
                    JdbcContext jdbcContext) {
                assertFalse(useGetGeneratedKeys);
                MockPreparedStatement ps = new MockPreparedStatement(null, null) {

                    @Override
                    public int executeUpdate() throws SQLException {
                        return 1;
                    }
                };
                return ps;
            }

            @Override
            protected Object getIdValue(PropertyMeta propertyMeta) {
                return 100L;
            }

        };
        assertEquals(1, query.execute());
        SqlLog sqlLog = SqlLogRegistryLocator.getInstance().getLast();
        assertEquals(
                "insert into FFF (ID, NAME, VERSION) values (100, 'hoge', 1)",
                sqlLog.getCompleteSql());
    }

    /**
     * @throws Exception
     */
    public void testExecute_table() throws Exception {
        manager.setDialect(new StandardDialect());
        Fff fff = new Fff();
        fff.name = "hoge";
        fff.version = 1L;
        AutoInsertImpl<Fff> query = new AutoInsertImpl<Fff>(manager, fff) {

            @Override
            protected PreparedStatement createPreparedStatement(
                    JdbcContext jdbcContext) {
                assertFalse(useGetGeneratedKeys);
                MockPreparedStatement ps = new MockPreparedStatement(null, null) {

                    @Override
                    public int executeUpdate() throws SQLException {
                        return 1;
                    }
                };
                return ps;
            }

            @Override
            protected Object getIdValue(PropertyMeta propertyMeta) {
                return 100L;
            }

        };
        assertEquals(1, query.execute());
        SqlLog sqlLog = SqlLogRegistryLocator.getInstance().getLast();
        assertEquals(
                "insert into FFF (ID, NAME, VERSION) values (100, 'hoge', 1)",
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
        AutoInsertImpl<Eee> query = new AutoInsertImpl<Eee>(manager, eee) {

            @Override
            protected PreparedStatement createPreparedStatement(
                    JdbcContext jdbcContext) {
                assertFalse(useGetGeneratedKeys);
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
                "insert into EEE (ID, NAME, VERSION) values (100, 'hoge', 1)",
                sqlLog.getCompleteSql());
    }

}
