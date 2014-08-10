/*
 * Copyright 2004-2014 the Seasar Foundation and the Others.
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
package org.seasar.extension.jdbc.impl;

import java.math.BigDecimal;

import org.seasar.extension.jdbc.SqlLog;
import org.seasar.extension.jdbc.SqlLogRegistry;
import org.seasar.extension.jdbc.SqlLogRegistryLocator;
import org.seasar.extension.unit.S2TestCase;

/**
 * @author koichik
 * 
 */
public class BasicHandlerTest extends S2TestCase {

    protected void setUp() throws Exception {
        super.setUp();
        include("j2ee.dicon");
    }

    /**
     * 
     * @throws Exception
     */
    public void testLogSql() throws Exception {
        final String sql = "update emp set ename = ?, comm = ? where empno = ?";
        Object[] args = new Object[] { "hoge", new BigDecimal("100.5"),
                new Integer(7788) };
        Class[] argTypes = new Class[] { String.class, BigDecimal.class,
                Integer.class };
        BasicHandler handler = new BasicHandler(getDataSource(), sql);
        assertTrue(handler.getLoggerClass() == BasicHandler.class);
        handler.logSql(args, argTypes);

        assertSqlLog(sql, args, argTypes);
        handler.setLoggerClass(BasicHandlerTest.class);
        handler.logSql(args, argTypes);
        assertSqlLog(sql, args, argTypes);
        assertTrue(handler.getLoggerClass() == BasicHandlerTest.class);
    }

    private void assertSqlLog(final String sql, Object[] args, Class[] argTypes) {
        SqlLogRegistry registry = SqlLogRegistryLocator.getInstance();
        SqlLog sqlLog = registry.getLast();
        assertEquals(sql, sqlLog.getRawSql());
        assertEquals(
                "update emp set ename = 'hoge', comm = 100.5 where empno = 7788",
                sqlLog.getCompleteSql());
        assertSame(args, sqlLog.getBindArgs());
        assertSame(argTypes, sqlLog.getBindArgTypes());
    }

}
