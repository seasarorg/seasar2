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

import java.sql.SQLException;
import java.util.Date;

import javax.persistence.TemporalType;

import junit.framework.TestCase;

import org.seasar.extension.jdbc.SqlLog;
import org.seasar.extension.jdbc.SqlLogRegistry;
import org.seasar.extension.jdbc.SqlLogRegistryLocator;
import org.seasar.extension.jdbc.dialect.StandardDialect;
import org.seasar.extension.jdbc.entity.Aaa;
import org.seasar.extension.jdbc.entity.MyDto;
import org.seasar.extension.jdbc.handler.BeanResultSetHandler;
import org.seasar.extension.jdbc.manager.JdbcManagerImpl;
import org.seasar.extension.jdbc.manager.JdbcManagerImplementor;
import org.seasar.extension.jdbc.parameter.Parameter;
import org.seasar.extension.jdbc.types.ValueTypes;
import org.seasar.framework.convention.impl.PersistenceConventionImpl;
import org.seasar.framework.mock.sql.MockColumnMetaData;
import org.seasar.framework.mock.sql.MockDataSource;
import org.seasar.framework.mock.sql.MockPreparedStatement;
import org.seasar.framework.mock.sql.MockResultSet;
import org.seasar.framework.mock.sql.MockResultSetMetaData;
import org.seasar.framework.util.ArrayMap;

/**
 * @author higa
 * 
 */
public class AbsQueryTest extends TestCase {

    private JdbcManagerImpl manager;

    private Object bindVariable;

    private int parameterIndex;

    @Override
    protected void setUp() throws Exception {
        manager = new JdbcManagerImpl();
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
     * 
     */
    public void testLogSql() {
        String sql = "select * from aaa where id = ?";
        String completeSql = "select * from aaa where id = 1";
        MyQuery query = new MyQuery(manager);
        query.executedSql = sql;
        query.addParam(1);
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
    public void testLogSql_withArgs() {
        String sql = "select * from aaa where id = ?";
        String completeSql = "select * from aaa where id = 1";
        MyQuery query = new MyQuery(manager);
        query.prepareCallerClassAndMethodName("testLogSql_withArgs");
        query.logSql(sql, 1);
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
    public void testAddParam() {
        MyQuery query = new MyQuery(manager);
        Param param = query.addParam(1);
        assertEquals(1, param.value);
        assertEquals(Integer.class, param.paramClass);
        assertEquals(ValueTypes.INTEGER, param.valueType);
    }

    /**
     * 
     */
    public void testAddParam_temporal() {
        MyQuery query = new MyQuery(manager);
        Param param = query.addParam(Parameter.date(new Date(1)));
        assertEquals(new Date(1), param.value);
        assertEquals(Date.class, param.paramClass);
        assertEquals(ValueTypes.DATE_SQLDATE, param.valueType);
    }

    /**
     * 
     */
    public void testAddParam_clob() {
        MyQuery query = new MyQuery(manager);
        Param param = query.addParam(Parameter.lob("aaa"));
        assertEquals("aaa", param.value);
        assertEquals(String.class, param.paramClass);
        assertEquals(ValueTypes.CLOB, param.valueType);
    }

    /**
     * 
     */
    public void testAddParam_blob() {
        MyQuery query = new MyQuery(manager);
        Param param = query.addParam(Parameter.lob(new byte[10]));
        assertEquals(10, ((byte[]) param.value).length);
        assertEquals(byte[].class, param.paramClass);
        assertEquals(ValueTypes.BLOB, param.valueType);
    }

    /**
     * 
     */
    public void testAddParam_blob_serializable() {
        MyQuery query = new MyQuery(manager);
        MyDto myDto = new MyDto();
        Param param = query.addParam(Parameter.lob(myDto));
        assertEquals(myDto, param.value);
        assertEquals(MyDto.class, param.paramClass);
        assertEquals(ValueTypes.SERIALIZABLE_BLOB, param.valueType);
    }

    /**
     * 
     */
    public void testAddParam_serializable() {
        MyQuery query = new MyQuery(manager);
        MyDto myDto = new MyDto();
        Param param = query.addParam(myDto);
        assertEquals(myDto, param.value);
        assertEquals(MyDto.class, param.paramClass);
        assertEquals(ValueTypes.SERIALIZABLE_BYTE_ARRAY, param.valueType);
    }

    /**
     * 
     */
    public void testAddParam_null() {
        MyQuery query = new MyQuery(manager);
        try {
            query.addParam(null);
            fail();
        } catch (NullPointerException e) {
            System.out.println(e);
        }
    }

    /**
     * 
     */
    public void testAddParam_valueType() {
        MyQuery query = new MyQuery(manager);
        Param param = query.addParam(1, Integer.class, ValueTypes.INTEGER);
        assertEquals(1, query.getParamSize());
        assertEquals(1, param.value);
        assertEquals(Integer.class, param.paramClass);
        assertEquals(ValueTypes.INTEGER, param.valueType);
    }

    /**
     * @throws Exception
     * 
     */
    public void testPrepareInParams() throws Exception {
        MyQuery query = new MyQuery(manager);
        Param param = new Param();
        param.value = "aaa";
        param.paramClass = String.class;
        param.valueType = ValueTypes.STRING;
        query.paramList.add(param);
        MockPreparedStatement ps = new MockPreparedStatement(null, null) {

            @Override
            public void setString(int index, String x) throws SQLException {
                bindVariable = x;
                parameterIndex = index;
                super.setString(parameterIndex, x);
            }

        };
        query.prepareInParams(ps);
        assertEquals("aaa", bindVariable);
        assertEquals(1, parameterIndex);
    }

    /**
     * 
     */
    public void testResetParams() {
        MyQuery query = new MyQuery(manager);
        query.addParam(1);
        query.resetParams();
        assertEquals(0, query.paramList.size());
    }

    /**
     * 
     */
    public void testGetParamSize() {
        MyQuery query = new MyQuery(manager);
        query.addParam(1);
        assertEquals(1, query.getParamSize());
    }

    /**
     * 
     */
    public void testGetParam() {
        MyQuery query = new MyQuery(manager);
        Param param = query.addParam(1);
        assertSame(param, query.getParam(0));
    }

    /**
     * @throws Exception
     * 
     */
    public void testHandleResultSet() throws Exception {
        MyQuery query = new MyQuery(manager);
        MockResultSetMetaData rsMeta = new MockResultSetMetaData();
        MockColumnMetaData columnMeta = new MockColumnMetaData();
        columnMeta.setColumnLabel("ID");
        rsMeta.addColumnMetaData(columnMeta);
        columnMeta = new MockColumnMetaData();
        columnMeta.setColumnLabel("NAME");
        rsMeta.addColumnMetaData(columnMeta);
        MockResultSet rs = new MockResultSet(rsMeta);
        ArrayMap data = new ArrayMap();
        data.put("ID", "111");
        data.put("NAME", "222");
        rs.addRowData(data);
        BeanResultSetHandler handler = new BeanResultSetHandler(Aaa.class,
                manager.getDialect(), manager.getPersistenceConvention(),
                "select * from aaa");
        Object ret = query.handleResultSet(handler, rs);
        assertTrue(rs.isClosed());
        assertNotNull(ret);
    }

    /**
     * @throws Exception
     * 
     */
    public void testGetValueType() throws Exception {
        MyQuery query = new MyQuery(manager);
        assertEquals(ValueTypes.CLOB, query.getValueType(String.class, true,
                null));
        assertEquals(ValueTypes.INTEGER, query.getValueType(Integer.class,
                false, null));
        assertEquals(ValueTypes.DATE_TIME, query.getValueType(Date.class,
                false, TemporalType.TIME));
        assertEquals(ValueTypes.SERIALIZABLE_BYTE_ARRAY, query.getValueType(
                MyDto.class, false, null));
    }

    private static class MyQuery extends AbstractQuery<MyQuery> {

        /**
         * @param jdbcManager
         */
        public MyQuery(JdbcManagerImplementor jdbcManager) {
            super(jdbcManager);
        }

        @Override
        protected void prepare(String methodName) {
        }
    }
}
