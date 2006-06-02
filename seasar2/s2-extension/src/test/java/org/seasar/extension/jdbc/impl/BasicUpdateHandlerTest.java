/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
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

import java.util.Date;

import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.exception.SQLRuntimeException;
import org.seasar.framework.exception.SSQLException;

public class BasicUpdateHandlerTest extends S2TestCase {

    public void testExecuteTx() throws Exception {
        String sql = "update emp set ename = ?, comm = ? where empno = ?";
        BasicUpdateHandler handler = new BasicUpdateHandler(getDataSource(),
                sql);
        int ret = handler.execute(new Object[] { "SCOTT", null,
                new Integer(7788) });
        assertEquals("1", 1, ret);
    }

    public void testExceptionByBrokenSqlTx() throws Exception {
        String sql = "pdate emp set ename = ?, comm = ? where empno = ?";
        BasicUpdateHandler handler = new BasicUpdateHandler(getDataSource(),
                sql);
        try {
            handler.execute(new Object[] { "SCOTT", null, new Integer(7788) });
            fail();
        } catch (SQLRuntimeException e) {
            assertTrue(e.getMessage(), e.getMessage().indexOf(sql) > -1);
            final SSQLException cause = (SSQLException) e.getCause();
            assertEquals(sql, cause.getSql());
        }
    }

    public void testExceptionByWrongDataTypeTx() throws Exception {
        final String sql = "update emp set comm = ?";
        BasicUpdateHandler handler = new BasicUpdateHandler(getDataSource(),
                sql);
        try {
            handler.execute(new Object[] { new Date() });
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
