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

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import junit.framework.TestCase;

import org.seasar.extension.jdbc.JdbcContext;
import org.seasar.extension.jdbc.ParamType;
import org.seasar.extension.jdbc.SqlLog;
import org.seasar.extension.jdbc.SqlLogRegistry;
import org.seasar.extension.jdbc.SqlLogRegistryLocator;
import org.seasar.extension.jdbc.annotation.InOut;
import org.seasar.extension.jdbc.annotation.Out;
import org.seasar.extension.jdbc.annotation.ResultSet;
import org.seasar.extension.jdbc.dialect.StandardDialect;
import org.seasar.extension.jdbc.entity.Aaa;
import org.seasar.extension.jdbc.exception.QueryTwiceExecutionRuntimeException;
import org.seasar.extension.jdbc.manager.JdbcManagerImpl;
import org.seasar.extension.jdbc.types.ValueTypes;
import org.seasar.extension.jta.TransactionManagerImpl;
import org.seasar.extension.jta.TransactionSynchronizationRegistryImpl;
import org.seasar.framework.mock.sql.MockCallableStatement;
import org.seasar.framework.mock.sql.MockDataSource;

import static org.seasar.extension.jdbc.parameter.Parameter.*;

/**
 * @author koichik
 */
public class SqlFunctionCallImplTest extends TestCase {

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
     */
    public void testPrepareReturnParameter_simpleType() throws Exception {
        SqlFunctionCallImpl<Integer> query = new SqlFunctionCallImpl<Integer>(
                manager, Integer.class, "{? = call hoge()}");
        query.prepareReturnParameter();
        query.prepareParameter();
        assertEquals(1, query.getParamSize());
        assertNull(query.getParam(0).value);
        assertEquals(Integer.class, query.getParam(0).paramClass);
        assertEquals(ValueTypes.INTEGER, query.getParam(0).valueType);
    }

    /**
     * @throws Exception
     */
    public void testPrepareReturnParameter_simpleList() throws Exception {
        SqlFunctionCallImpl<Integer> query = new SqlFunctionCallImpl<Integer>(
                manager, Integer.class, "{? = call hoge()}");
        query.resultList = true;
        query.prepareReturnParameter();
        query.prepareParameter();
        assertEquals(1, query.getParamSize());
        assertNull(query.getParam(0).value);
        assertEquals(Integer.class, query.getParam(0).paramClass);
        assertEquals(ValueTypes.OBJECT, query.getParam(0).valueType);
    }

    /**
     * @throws Exception
     */
    public void testPrepareReturnParameter_dtoList() throws Exception {
        SqlFunctionCallImpl<MyDto3> query = new SqlFunctionCallImpl<MyDto3>(
                manager, MyDto3.class, "{? = call hoge()}");
        query.resultList = true;
        query.prepareReturnParameter();
        query.prepareParameter();
        assertEquals(1, query.getParamSize());
        assertNull(query.getParam(0).value);
        assertEquals(MyDto3.class, query.getParam(0).paramClass);
        assertEquals(ValueTypes.OBJECT, query.getParam(0).valueType);
    }

    /**
     * @throws Exception
     */
    public void testPrepareParameter_simpleType() throws Exception {
        SqlFunctionCallImpl<String> query = new SqlFunctionCallImpl<String>(
                manager, String.class, "{? = call hoge(?)}", 1);
        query.prepareReturnParameter();
        query.prepareParameter();
        assertEquals(2, query.getParamSize());
        assertNull(query.getParam(0).value);
        assertEquals(String.class, query.getParam(0).paramClass);
        assertEquals(ValueTypes.STRING, query.getParam(0).valueType);

        assertEquals(1, query.getParam(1).value);
        assertEquals(Integer.class, query.getParam(1).paramClass);
        assertEquals(ValueTypes.INTEGER, query.getParam(1).valueType);
    }

    /**
     * @throws Exception
     */
    public void testPrepareParameter_simpleType_clob() throws Exception {
        SqlFunctionCallImpl<String> query = new SqlFunctionCallImpl<String>(
                manager, String.class, "{? = call hoge(?)}", lob("hoge"));
        query.prepareReturnParameter();
        query.prepareParameter();
        assertEquals(2, query.getParamSize());
        assertNull(query.getParam(0).value);
        assertEquals(String.class, query.getParam(0).paramClass);
        assertEquals(ValueTypes.STRING, query.getParam(0).valueType);

        assertEquals("hoge", query.getParam(1).value);
        assertEquals(String.class, query.getParam(1).paramClass);
        assertEquals(ValueTypes.CLOB, query.getParam(1).valueType);
    }

    /**
     * @throws Exception
     */
    public void testPrepareParameter_simpleType_date() throws Exception {
        Date date = new SimpleDateFormat("HH:mm:ss").parse("12:11:10");
        SqlFunctionCallImpl<String> query = new SqlFunctionCallImpl<String>(
                manager, String.class, "{? = call hoge(?)}", time(date));
        query.prepareReturnParameter();
        query.prepareParameter();
        assertEquals(2, query.getParamSize());
        assertNull(query.getParam(0).value);
        assertEquals(String.class, query.getParam(0).paramClass);
        assertEquals(ValueTypes.STRING, query.getParam(0).valueType);

        assertEquals(date, query.getParam(1).value);
        assertEquals(Date.class, query.getParam(1).paramClass);
        assertEquals(ValueTypes.DATE_TIME, query.getParam(1).valueType);
    }

    /**
     * @throws Exception
     */
    public void testPrepareParameter_dto() throws Exception {
        MyDto dto = new MyDto();
        dto.arg1 = "aaa";
        dto.arg2 = "bbb";
        SqlFunctionCallImpl<Integer> query = new SqlFunctionCallImpl<Integer>(
                manager, Integer.class, "{? = call hoge(?, ?, ?)}", dto);
        query.prepareReturnParameter();
        query.prepareParameter();
        assertEquals(4, query.getParamSize());
        assertEquals(null, query.getParam(0).value);
        assertEquals(Integer.class, query.getParam(0).paramClass);
        assertEquals(ValueTypes.INTEGER, query.getParam(0).valueType);
        assertEquals(ParamType.OUT, query.getParam(0).paramType);
        assertNull(query.getParam(0).field);

        assertEquals("aaa", query.getParam(1).value);
        assertEquals(String.class, query.getParam(1).paramClass);
        assertEquals(ValueTypes.STRING, query.getParam(1).valueType);
        assertEquals(ParamType.IN_OUT, query.getParam(1).paramType);
        assertEquals(MyDto.class.getDeclaredField("arg1"),
                query.getParam(1).field);

        assertEquals("bbb", query.getParam(2).value);
        assertEquals(String.class, query.getParam(2).paramClass);
        assertEquals(ValueTypes.STRING, query.getParam(2).valueType);
        assertEquals(ParamType.IN, query.getParam(2).paramType);
        assertNull(query.getParam(2).field);

        assertEquals(null, query.getParam(3).value);
        assertEquals(String.class, query.getParam(3).paramClass);
        assertEquals(ValueTypes.STRING, query.getParam(3).valueType);
        assertEquals(ParamType.OUT, query.getParam(3).paramType);
        assertEquals(MyDto.class.getDeclaredField("arg3"),
                query.getParam(3).field);
    }

    /**
     * @throws Exception
     */
    public void testPrepareParameter_resultSet() throws Exception {
        MyDto2 dto = new MyDto2();
        SqlFunctionCallImpl<Integer> query = new SqlFunctionCallImpl<Integer>(
                manager, Integer.class, "{? = call hoge(?)}", dto);
        query.prepareReturnParameter();
        query.prepareParameter();
        assertEquals(1, query.getParamSize());
        assertEquals(null, query.getParam(0).value);
        assertEquals(Integer.class, query.getParam(0).paramClass);
        assertEquals(ValueTypes.INTEGER, query.getParam(0).valueType);
        assertEquals(ParamType.OUT, query.getParam(0).paramType);
        assertNull(query.getParam(0).field);

        assertEquals(1, query.getNonParamSize());
        assertEquals(ParamType.OUT, query.getNonParam(0).paramType);
        assertEquals(MyDto2.class.getDeclaredField("arg1"), query
                .getNonParam(0).field);
    }

    /**
     * @throws Exception
     */
    public void testPrepareParameter_dto_clob() throws Exception {
        MyDto3 dto = new MyDto3();
        dto.largeName = "aaa";
        SqlFunctionCallImpl<Integer> query = new SqlFunctionCallImpl<Integer>(
                manager, Integer.class, "{? = call hoge(?)}", dto);
        query.prepareReturnParameter();
        query.prepareParameter();
        assertEquals(2, query.getParamSize());
        assertEquals(null, query.getParam(0).value);
        assertEquals(Integer.class, query.getParam(0).paramClass);
        assertEquals(ValueTypes.INTEGER, query.getParam(0).valueType);
        assertEquals(ParamType.OUT, query.getParam(0).paramType);
        assertNull(query.getParam(0).field);

        assertEquals("aaa", query.getParam(1).value);
        assertEquals(ParamType.IN, query.getParam(1).paramType);
        assertEquals(ValueTypes.CLOB, query.getParam(1).valueType);
    }

    /**
     * @throws Exception
     */
    public void testPrepareParameter_dto_date() throws Exception {
        MyDto4 dto = new MyDto4();
        dto.date = new SimpleDateFormat("HH:mm:ss").parse("12:11:10");
        SqlFunctionCallImpl<Integer> query = new SqlFunctionCallImpl<Integer>(
                manager, Integer.class, "{? = call hoge(?)}", dto);
        query.prepareReturnParameter();
        query.prepareParameter();
        assertEquals(2, query.getParamSize());
        assertEquals(null, query.getParam(0).value);
        assertEquals(Integer.class, query.getParam(0).paramClass);
        assertEquals(ValueTypes.INTEGER, query.getParam(0).valueType);
        assertEquals(ParamType.OUT, query.getParam(0).paramType);
        assertNull(query.getParam(0).field);

        assertEquals(new SimpleDateFormat("HH:mm:ss").parse("12:11:10"), query
                .getParam(1).value);
        assertEquals(ParamType.IN, query.getParam(1).paramType);
        assertEquals(ValueTypes.DATE_TIME, query.getParam(1).valueType);
    }

    /**
     * @throws Exception
     */
    public void testCall() throws Exception {
        MyDto dto = new MyDto();
        dto.arg1 = "aaa";
        dto.arg2 = "bbb";
        SqlFunctionCallImpl<String> query = new SqlFunctionCallImpl<String>(
                manager, String.class, "{? = call hoge(?, ?, ?)}", dto) {

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

        String result = query.getSingleResult();
        assertEquals("aaa1", result);
        assertEquals("aaa2", dto.arg1);
        assertEquals("aaa4", dto.arg3);
        SqlLog sqlLog = SqlLogRegistryLocator.getInstance().getLast();
        assertEquals("{null = call hoge('aaa', 'bbb', null)}", sqlLog
                .getCompleteSql());

        try {
            query.getSingleResult();
            fail();
        } catch (QueryTwiceExecutionRuntimeException expected) {
        }
    }

    private static final class MyDto {

        /** */
        @InOut
        public String arg1;

        /** */
        public String arg2;

        /** */
        @Out
        public String arg3;

    }

    private static final class MyDto2 {

        /** */
        @ResultSet
        public List<Aaa> arg1;

    }

    private static final class MyDto3 {

        /** */
        @Lob
        public String largeName;

    }

    private static final class MyDto4 {

        /** */
        @Temporal(TemporalType.TIME)
        public Date date;

    }
}
