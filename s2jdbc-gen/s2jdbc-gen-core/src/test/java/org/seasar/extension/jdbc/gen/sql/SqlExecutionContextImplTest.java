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
package org.seasar.extension.jdbc.gen.sql;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.junit.Test;
import org.seasar.extension.jdbc.gen.exception.SqlFailedException;
import org.seasar.framework.mock.sql.MockConnection;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class SqlExecutionContextImplTest {

    /**
     * 
     */
    @Test
    public void testGetStatement() {
        MockConnection conn = new MockConnection();
        SqlExecutionContextImpl context = new SqlExecutionContextImpl(conn,
                false);
        Statement statement = context.getStatement();
        assertNotNull(statement);
        assertSame(statement, context.getStatement());
        context.addException(new SqlFailedException(new SQLException(), "aaa",
                "bbb"));
        assertNotSame(statement, context.getStatement());
    }

    /**
     * 
     */
    @Test
    public void testAddException() {
        MockConnection conn = new MockConnection();
        SqlExecutionContextImpl context = new SqlExecutionContextImpl(conn,
                false);
        assertTrue(context.getExceptionList().isEmpty());
        SqlFailedException exception = new SqlFailedException(
                new SQLException(), "aaa", "bbb");
        context.addException(exception);
        List<SqlFailedException> list = context.getExceptionList();
        assertEquals(1, list.size());
    }

    /**
     * 
     */
    @Test
    public void testAddException_haltOnError() {
        MockConnection conn = new MockConnection();
        SqlExecutionContextImpl context = new SqlExecutionContextImpl(conn,
                true);
        assertTrue(context.getExceptionList().isEmpty());
        SqlFailedException exception = new SqlFailedException(
                new SQLException(), "aaa", "bbb");
        try {
            context.addException(exception);
            fail();
        } catch (SqlFailedException expected) {
        }
    }

    /**
     * 
     */
    @Test
    public void testDestroy() {
        MockConnection conn = new MockConnection();
        SqlExecutionContextImpl context = new SqlExecutionContextImpl(conn,
                false);
        assertNotNull(context.getStatement());
        assertNotNull(context.connection);
        assertNotNull(context.statement);
        context.destroy();
        assertNull(context.connection);
        assertNull(context.statement);
    }

}
