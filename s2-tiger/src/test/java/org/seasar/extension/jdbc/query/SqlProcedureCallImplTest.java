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

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import junit.framework.TestCase;

import org.seasar.extension.jdbc.JdbcContext;
import org.seasar.extension.jdbc.ParamType;
import org.seasar.extension.jdbc.SqlLog;
import org.seasar.extension.jdbc.SqlLogRegistry;
import org.seasar.extension.jdbc.SqlLogRegistryLocator;
import org.seasar.extension.jdbc.dialect.StandardDialect;
import org.seasar.extension.jdbc.entity.Aaa;
import org.seasar.extension.jdbc.manager.JdbcManagerImpl;
import org.seasar.extension.jdbc.types.ValueTypes;
import org.seasar.extension.jta.TransactionManagerImpl;
import org.seasar.extension.jta.TransactionSynchronizationRegistryImpl;
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
public class SqlProcedureCallImplTest extends TestCase {

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
    public void testPrepareParameter_simpleType() throws Exception {
        SqlProcedureCallImpl query = new SqlProcedureCallImpl(manager,
                "{call hoge(?)}", 1);
        query.prepareParameter();
        assertEquals(1, query.getParamSize());
        assertEquals(1, query.getParam(0).value);
        assertEquals(Integer.class, query.getParam(0).paramClass);
        assertEquals(ValueTypes.INTEGER, query.getParam(0).valueType);
    }

    /**
     * @throws Exception
     * 
     */
    public void testPrepareParameter_dto() throws Exception {
        MyDto dto = new MyDto();
        dto.arg1_IN_OUT = "aaa";
        dto.arg2 = "bbb";
        SqlProcedureCallImpl query = new SqlProcedureCallImpl(manager,
                "{call hoge(?)}", dto);
        query.prepareParameter();
        assertEquals(3, query.getParamSize());
        assertEquals(null, query.getParam(0).value);
        assertEquals(String.class, query.getParam(0).paramClass);
        assertEquals(ValueTypes.STRING, query.getParam(0).valueType);
        assertEquals(ParamType.OUT, query.getParam(0).paramType);
        assertEquals(MyDto.class.getDeclaredField("result_OUT"), query
                .getParam(0).field);

        assertEquals("aaa", query.getParam(1).value);
        assertEquals(String.class, query.getParam(1).paramClass);
        assertEquals(ValueTypes.STRING, query.getParam(1).valueType);
        assertEquals(ParamType.IN_OUT, query.getParam(1).paramType);
        assertEquals(MyDto.class.getDeclaredField("arg1_IN_OUT"), query
                .getParam(1).field);

        assertEquals("bbb", query.getParam(2).value);
        assertEquals(String.class, query.getParam(2).paramClass);
        assertEquals(ValueTypes.STRING, query.getParam(2).valueType);
        assertEquals(ParamType.IN, query.getParam(2).paramType);
        assertNull(query.getParam(2).field);
    }

    /**
     * @throws Exception
     * 
     */
    public void testPrepareParameter_resultSet() throws Exception {
        MyDto2 dto = new MyDto2();
        SqlProcedureCallImpl query = new SqlProcedureCallImpl(manager,
                "{call hoge(?)}", dto) {

            @Override
            protected CallableStatement getCallableStatement(
                    JdbcContext jdbcContext) {
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
                return cs;
            }

        };
        query.prepareParameter();
        assertEquals(0, query.getParamSize());
        assertEquals(1, query.getNonParamSize());
        assertEquals(ParamType.OUT, query.getNonParam(0).paramType);
        assertEquals(MyDto2.class.getDeclaredField("result_OUT"), query
                .getNonParam(0).field);
    }

    /**
     * @throws Exception
     * 
     */
    public void testCall() throws Exception {
        MyDto dto = new MyDto();
        dto.arg1_IN_OUT = "aaa";
        dto.arg2 = "bbb";
        SqlProcedureCallImpl query = new SqlProcedureCallImpl(manager,
                "{? = call hoge(?, ?)}", dto) {

            @Override
            protected CallableStatement getCallableStatement(
                    JdbcContext jdbcContext) {
                MockCallableStatement cs = new MockCallableStatement(null, null) {

                    @Override
                    public String getString(int parameterIndex)
                            throws SQLException {
                        return "aaa" + parameterIndex;
                    }
                };
                return cs;
            }
        };

        query.call();
        assertEquals("aaa1", dto.result_OUT);
        assertEquals("aaa2", dto.arg1_IN_OUT);
        SqlLog sqlLog = SqlLogRegistryLocator.getInstance().getLast();
        assertEquals("{null = call hoge('aaa', 'bbb')}", sqlLog
                .getCompleteSql());
    }

    private static final class MyDto {

        /**
         * 
         */
        public String result_OUT;

        /**
         * 
         */
        public String arg1_IN_OUT;

        /**
         * 
         */
        public String arg2;
    }

    private static final class MyDto2 {

        /**
         * 
         */
        public List<Aaa> result_OUT;
    }
}
