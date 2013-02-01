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

import java.lang.reflect.Field;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import junit.framework.TestCase;

import org.seasar.extension.jdbc.JdbcContext;
import org.seasar.extension.jdbc.ParamType;
import org.seasar.extension.jdbc.SqlLog;
import org.seasar.extension.jdbc.SqlLogRegistry;
import org.seasar.extension.jdbc.SqlLogRegistryLocator;
import org.seasar.extension.jdbc.dialect.OracleDialect;
import org.seasar.extension.jdbc.dialect.StandardDialect;
import org.seasar.extension.jdbc.entity.Aaa;
import org.seasar.extension.jdbc.exception.FieldNotGenericsRuntimeException;
import org.seasar.extension.jdbc.manager.JdbcManagerImpl;
import org.seasar.extension.jdbc.manager.JdbcManagerImplementor;
import org.seasar.extension.jta.TransactionManagerImpl;
import org.seasar.extension.jta.TransactionSynchronizationRegistryImpl;
import org.seasar.framework.convention.impl.PersistenceConventionImpl;
import org.seasar.framework.mock.sql.MockCallableStatement;
import org.seasar.framework.mock.sql.MockColumnMetaData;
import org.seasar.framework.mock.sql.MockDataSource;
import org.seasar.framework.mock.sql.MockResultSet;
import org.seasar.framework.mock.sql.MockResultSetMetaData;
import org.seasar.framework.util.ArrayMap;

/**
 * @author higa
 * 
 */
public class AbsProcedureCallTest extends TestCase {

    private JdbcManagerImpl manager;

    private boolean setupedCallableStatement;

    private boolean preparedInParams;

    private boolean preparedOutParams;

    private int paramIndex;

    private int paramSqlType;

    private int registeredCount;

    @Override
    protected void setUp() throws Exception {
        manager = new JdbcManagerImpl();
        manager.setSyncRegistry(new TransactionSynchronizationRegistryImpl(
                new TransactionManagerImpl()));
        manager.setDataSource(new MockDataSource());
        manager.setDialect(new StandardDialect());
        manager.setPersistenceConvention(new PersistenceConventionImpl());
    }

    @Override
    protected void tearDown() throws Exception {
        SqlLogRegistry regisry = SqlLogRegistryLocator.getInstance();
        regisry.clear();
    }

    /**
     * @throws Exception
     * 
     */
    public void testSetupCallableStatement() throws Exception {
        MyCall query = new MyCall(manager) {

            @Override
            protected void prepareOutParams(CallableStatement cs) {
                preparedOutParams = true;
                super.prepareOutParams(cs);
            }

            @Override
            protected void prepareInParams(PreparedStatement ps) {
                preparedInParams = true;
                super.prepareInParams(ps);
            }

        };
        query.fetchSize = 10;
        query.maxRows = 20;
        query.queryTimeout = 30;
        MockCallableStatement cs = new MockCallableStatement(null, null);
        query.setupCallableStatement(cs);
        assertEquals(10, cs.getFetchSize());
        assertEquals(20, cs.getMaxRows());
        assertEquals(30, cs.getQueryTimeout());
        assertTrue(preparedInParams);
        assertTrue(preparedOutParams);
    }

    /**
     * @throws Exception
     * 
     */
    public void testGetCallableStatement() throws Exception {
        MyCall query = new MyCall(manager) {

            @Override
            protected void setupCallableStatement(CallableStatement cs) {
                setupedCallableStatement = true;
                super.setupCallableStatement(cs);
            }
        };
        JdbcContext jdbcContext = manager.getJdbcContext();
        CallableStatement cs = query.getCallableStatement(jdbcContext);
        assertNotNull(cs);
        assertTrue(setupedCallableStatement);
    }

    /**
     * @throws Exception
     * 
     */
    public void testMaxRows() throws Exception {
        MyCall query = new MyCall(manager);
        assertSame(query, query.maxRows(100));
        assertEquals(100, query.maxRows);
    }

    /**
     * @throws Exception
     * 
     */
    public void testFetchSize() throws Exception {
        MyCall query = new MyCall(manager);
        assertSame(query, query.fetchSize(10));
        assertEquals(10, query.fetchSize);
    }

    /**
     * @throws Exception
     * 
     */
    public void testPrepareOutParams() throws Exception {
        MyCall query = new MyCall(manager);
        MockCallableStatement cs = new MockCallableStatement(null, null) {

            @Override
            public void registerOutParameter(int parameterIndex, int sqlType)
                    throws SQLException {
                paramIndex = parameterIndex;
                paramSqlType = sqlType;
                registeredCount++;
                super.registerOutParameter(parameterIndex, sqlType);
            }

        };
        Param param = query.addParam(1);
        param = query.addParam("hoge", String.class);
        param.paramType = ParamType.OUT;
        query.prepareOutParams(cs);
        assertEquals(2, paramIndex);
        assertEquals(Types.VARCHAR, paramSqlType);
        assertEquals(1, registeredCount);
    }

    /**
     * @throws Exception
     * 
     */
    public void testHandleOutParams_nonParam_resultSet() throws Exception {
        MyCall query = new MyCall(manager);
        MockCallableStatement cs = new MockCallableStatement(null, null) {

            @Override
            public ResultSet getResultSet() throws SQLException {
                MockResultSetMetaData rsMeta = new MockResultSetMetaData();
                MockColumnMetaData columnMeta = new MockColumnMetaData();
                columnMeta.setColumnLabel("ID");
                rsMeta.addColumnMetaData(columnMeta);
                columnMeta = new MockColumnMetaData();
                columnMeta.setColumnLabel("NAME");
                rsMeta.addColumnMetaData(columnMeta);
                MockResultSet rs = new MockResultSet(rsMeta);
                ArrayMap data = new ArrayMap();
                data.put("ID", 1);
                data.put("NAME", "aaa");
                rs.addRowData(data);
                return rs;
            }

        };
        Field field = MyDto.class.getDeclaredField("aaaList_OUT");
        field.setAccessible(true);
        query.addNonParam(field);
        MyDto dto = new MyDto();
        query.parameter = dto;
        query.handleNonParamResultSets(cs, true);
        query.handleOutParams(cs);
        assertNotNull(dto.aaaList_OUT);
        assertEquals(1, dto.aaaList_OUT.size());
        Aaa aaa = dto.aaaList_OUT.get(0);
        assertEquals(new Integer(1), aaa.id);
        assertEquals("aaa", aaa.name);
    }

    /**
     * @throws Exception
     * 
     */
    public void testHandleOutParams_outParam_resultSet() throws Exception {
        manager.setDialect(new OracleDialect());
        MyCall query = new MyCall(manager);
        MockCallableStatement cs = new MockCallableStatement(null, null) {

            @Override
            public Object getObject(int parameterIndex) throws SQLException {
                MockResultSetMetaData rsMeta = new MockResultSetMetaData();
                MockColumnMetaData columnMeta = new MockColumnMetaData();
                columnMeta.setColumnLabel("ID");
                rsMeta.addColumnMetaData(columnMeta);
                columnMeta = new MockColumnMetaData();
                columnMeta.setColumnLabel("NAME");
                rsMeta.addColumnMetaData(columnMeta);
                MockResultSet rs = new MockResultSet(rsMeta);
                ArrayMap data = new ArrayMap();
                data.put("ID", 1);
                data.put("NAME", "aaa");
                rs.addRowData(data);
                return rs;
            }
        };
        Field field = MyDto.class.getDeclaredField("aaaList_OUT");
        field.setAccessible(true);
        Param param = query.addParam(null, field.getType());
        param.paramType = ParamType.OUT;
        param.field = field;
        MyDto dto = new MyDto();
        query.parameter = dto;
        query.handleOutParams(cs);
        assertNotNull(dto.aaaList_OUT);
        assertEquals(1, dto.aaaList_OUT.size());
        Aaa aaa = dto.aaaList_OUT.get(0);
        assertEquals(new Integer(1), aaa.id);
        assertEquals("aaa", aaa.name);
    }

    /**
     * @throws Exception
     * 
     */
    public void testHandleOutParams_resultSet_notGenerics() throws Exception {
        manager.setDialect(new OracleDialect());
        MyCall query = new MyCall(manager);
        MockCallableStatement cs = new MockCallableStatement(null, null) {

            @Override
            public Object getObject(int parameterIndex) throws SQLException {
                MockResultSetMetaData rsMeta = new MockResultSetMetaData();
                MockColumnMetaData columnMeta = new MockColumnMetaData();
                columnMeta.setColumnLabel("ID");
                rsMeta.addColumnMetaData(columnMeta);
                columnMeta = new MockColumnMetaData();
                columnMeta.setColumnLabel("NAME");
                rsMeta.addColumnMetaData(columnMeta);
                MockResultSet rs = new MockResultSet(rsMeta);
                ArrayMap data = new ArrayMap();
                data.put("ID", 1);
                data.put("NAME", "aaa");
                rs.addRowData(data);
                return rs;
            }
        };
        Field field = BadDto.class.getDeclaredField("aaaList_OUT");
        field.setAccessible(true);
        Param param = query.addParam(null, field.getType());
        param.paramType = ParamType.OUT;
        param.field = field;
        BadDto dto = new BadDto();
        query.parameter = dto;
        query.prepare("call");
        try {
            query.handleOutParams(cs);
            fail();
        } catch (FieldNotGenericsRuntimeException e) {
            System.out.println(e);
            assertEquals(field, e.getField());
        }
    }

    /**
     * @throws Exception
     * 
     */
    public void testHandleOutParams_notResultSet() throws Exception {
        MyCall query = new MyCall(manager);
        MockCallableStatement cs = new MockCallableStatement(null, null) {

            @Override
            public String getString(int parameterIndex) throws SQLException {
                return "aaa";
            }

        };
        Field field = MyDto2.class.getDeclaredField("hoge_OUT");
        field.setAccessible(true);
        Param param = query.addParam(null, field.getType());
        param.paramType = ParamType.OUT;
        param.field = field;
        MyDto2 dto = new MyDto2();
        query.parameter = dto;
        query.handleOutParams(cs);
        assertEquals("aaa", dto.hoge_OUT);
    }

    /**
     * @throws Exception
     * 
     */
    public void testCall() throws Exception {
        MyCall query = new MyCall(manager) {

            @Override
            protected CallableStatement getCallableStatement(
                    JdbcContext jdbcContext) {
                MockCallableStatement cs = new MockCallableStatement(null, null) {

                    @Override
                    public String getString(int parameterIndex)
                            throws SQLException {
                        return "aaa";
                    }
                };
                return cs;
            }

        };

        Field field = MyDto2.class.getDeclaredField("hoge_OUT");
        field.setAccessible(true);
        Param param = query.addParam(null, field.getType());
        param.paramType = ParamType.OUT;
        param.field = field;
        MyDto2 dto = new MyDto2();
        query.parameter = dto;
        query.executedSql = "{? = call myfunc()}";
        query.execute();
        assertEquals("aaa", dto.hoge_OUT);
        SqlLog sqlLog = SqlLogRegistryLocator.getInstance().getLast();
        assertEquals("{null = call myfunc()}", sqlLog.getCompleteSql());
    }

    /**
     * @throws Exception
     * 
     */
    public void testNonParam() throws Exception {
        MyCall query = new MyCall(manager);
        Field field = MyDto.class.getDeclaredField("aaaList_OUT");
        field.setAccessible(true);
        Param param = query.addNonParam(field);
        assertNotNull(param);
        assertEquals(1, query.getNonParamSize());
        assertSame(param, query.getNonParam(0));
        assertSame(field, param.field);
    }

    private static class MyCall extends AbstractProcedureCall<MyCall> {

        /**
         * @param jdbcManager
         * 
         */
        public MyCall(JdbcManagerImplementor jdbcManager) {
            super(jdbcManager);
        }

        @Override
        protected void prepare(String methodName) {
            prepareCallerClassAndMethodName(methodName);
        }
    }

    private static final class MyDto {

        /**
         * 
         */
        public List<Aaa> aaaList_OUT;
    }

    private static final class BadDto {

        /**
         * 
         */
        @SuppressWarnings("unchecked")
        public List aaaList_OUT;
    }

    private static final class MyDto2 {

        /**
         * 
         */
        public String hoge_OUT;
    }
}
