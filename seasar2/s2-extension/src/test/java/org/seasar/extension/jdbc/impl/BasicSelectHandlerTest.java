/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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

import java.util.Map;

import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.exception.SQLRuntimeException;
import org.seasar.framework.exception.SSQLException;

/**
 * @author higa
 *
 */
public class BasicSelectHandlerTest extends S2TestCase {

    /**
     * @throws Exception
     */
    public void testExecute() throws Exception {
        String sql = "select * from emp where empno = ?";
        BasicSelectHandler handler = new BasicSelectHandler(getDataSource(),
                sql, new MapResultSetHandler());
        Map ret = (Map) handler.execute(new Object[] { new Integer(7788) });
        System.out.println(ret);
        assertNotNull("1", ret);
    }

    /**
     * @throws Exception
     */
    public void testExceptionByBrokenSql() throws Exception {
        final String sql = "selec * from emp";
        BasicSelectHandler handler = new BasicSelectHandler(getDataSource(),
                sql, new MapResultSetHandler());
        try {
            handler.execute(new Object[] {});
            fail();
        } catch (SQLRuntimeException e) {
            assertTrue(e.getMessage(), e.getMessage().indexOf(sql) > -1);
            final SSQLException cause = (SSQLException) e.getCause();
            assertEquals(sql, cause.getSql());
        }
    }

    /**
     * @throws Exception
     */
    public void testExceptionByInvalidTableName() throws Exception {
        final String sql = "select * from UNKNOWN";
        BasicSelectHandler handler = new BasicSelectHandler(getDataSource(),
                sql, new MapResultSetHandler());
        try {
            handler.execute(new Object[] {});
            fail();
        } catch (SQLRuntimeException e) {
            assertTrue(e.getMessage(), e.getMessage().indexOf(sql) > -1);
            final SSQLException cause = (SSQLException) e.getCause();
            assertEquals(sql, cause.getSql());
        }
    }

    public void setUp() {
        include("j2ee.dicon");
    }

}