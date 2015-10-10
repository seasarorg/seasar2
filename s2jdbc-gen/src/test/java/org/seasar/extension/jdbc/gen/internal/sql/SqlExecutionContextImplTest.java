/*
 * Copyright 2004-2015 the Seasar Foundation and the Others.
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
package org.seasar.extension.jdbc.gen.internal.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Test;
import org.seasar.extension.jdbc.gen.exception.SqlFailedRuntimeException;
import org.seasar.framework.mock.sql.MockDataSource;

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
        SqlExecutionContextImpl context = new SqlExecutionContextImpl(
                new MockDataSource(), true, false);
        context.begin();
        Statement statement = context.getStatement();
        assertNotNull(statement);
        PreparedStatement preparedStatement = context.getPreparedStatement("");
        assertNotNull(preparedStatement);
        context.end();
        context.destroy();
    }

    /**
     * 
     */
    @Test
    public void testAddException() {
        SqlExecutionContextImpl context = new SqlExecutionContextImpl(
                new MockDataSource(), true, false);

        Connection connection = context.connection;
        context.begin();
        assertEquals(0, context.getExceptionList().size());
        context.addException(new SqlFailedRuntimeException(new SQLException(),
                "aaa", 1, "bbb"));
        context.end();
        assertNotSame(connection, context.connection);
        assertEquals(1, context.getExceptionList().size());
        context.destroy();
        assertEquals(0, context.getExceptionList().size());
    }

    /**
     * 
     */
    @Test
    public void testAddException_haltOnError() {
        SqlExecutionContextImpl context = new SqlExecutionContextImpl(
                new MockDataSource(), true, true);
        context.begin();
        SqlFailedRuntimeException exception = new SqlFailedRuntimeException(
                new SQLException(), "aaa", 1, "bbb");
        try {
            context.addException(exception);
            fail();
        } catch (SqlFailedRuntimeException expected) {
        }
        context.end();
        context.destroy();
    }

    /**
     * 
     */
    @Test
    public void testBeginEndDestroy() {
        SqlExecutionContextImpl context = new SqlExecutionContextImpl(
                new MockDataSource(), true, false);
        context.begin();
        assertNotNull(context.getStatement());
        assertNotNull(context.connection);
        assertNotNull(context.statement);
        assertTrue(context.begun);
        context.end();
        assertNotNull(context.connection);
        assertNull(context.statement);
        assertFalse(context.begun);
        context.destroy();
        assertNull(context.connection);
        assertNull(context.statement);
        assertFalse(context.begun);
    }

}
