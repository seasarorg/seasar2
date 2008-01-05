/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.exception.SQLRuntimeException;
import org.seasar.framework.exception.SSQLException;

/**
 * @author higa
 * 
 */
public class BasicReturningRowsBatchHandlerTest extends S2TestCase {

    /**
     * @throws Exception
     */
    public void testExecuteTx() throws Exception {
        String sql = "update emp set ename = ?, comm = ? where empno = ?";
        BasicReturningRowsBatchHandler handler = new BasicReturningRowsBatchHandler(
                getDataSource(), sql, 2);
        List list = new ArrayList();
        list.add(new Object[] { "aaa", null, new Integer(7369) });
        list.add(new Object[] { "bbb", new Double(100.5), new Integer(7499) });
        list.add(new Object[] { "ccc", null, new Integer(7521) });
        int[] rows = handler.execute(list);
        assertEquals(1, rows[0]);
        assertEquals(1, rows[1]);
        assertEquals(1, rows[2]);
        assertEquals(3, rows.length);
        String sql2 = "select empno, ename, comm from emp where empno in (7369, 7499, 7521) order by empno";
        BasicSelectHandler handler2 = new BasicSelectHandler(getDataSource(),
                sql2, new MapListResultSetHandler());
        List ret2 = (List) handler2.execute((Object[]) null);
        Map rec = (Map) ret2.get(0);
        assertEquals("aaa", rec.get("ename"));
        rec = (Map) ret2.get(1);
        assertEquals("bbb", rec.get("ename"));
        rec = (Map) ret2.get(2);
        assertEquals("ccc", rec.get("ename"));
    }

    /**
     * @throws Exception
     */
    public void testExceptionByBrokenSqlTx() throws Exception {
        final String sql = "updat emp set ename = ?, comm = ? where empno = ?";
        BasicReturningRowsBatchHandler handler = new BasicReturningRowsBatchHandler(
                getDataSource(), sql, 2);
        List list = new ArrayList();
        list.add(new Object[] { "aaa", null, new Integer(7369) });
        try {
            handler.execute(list);
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
    public void testExceptionByWrongDataTypeTx() throws Exception {
        final String sql = "update emp set ename = ?, comm = ? where empno = ?";
        BasicReturningRowsBatchHandler handler = new BasicReturningRowsBatchHandler(
                getDataSource(), sql, 2);
        List list = new ArrayList();
        list.add(new Object[] { "aaa", new Date(), new Integer(7369) });
        try {
            handler.execute(list);
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
