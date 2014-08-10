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
package org.seasar.extension.jdbc.types;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;

import junit.framework.TestCase;

import org.seasar.extension.jdbc.impl.PreparedStatementWrapper;
import org.seasar.extension.jdbc.impl.ResultSetWrapper;

/**
 * @author higa
 */
public class UserDefineTypeTest extends TestCase {

    private UserDefineType valueType;

    protected void setUp() throws Exception {
        Method valueOfMethod = Authority.class.getMethod("valueOf",
                new Class[] { int.class });
        Method valueMethod = Authority.class.getMethod("value", null);
        valueType = new UserDefineType(ValueTypes.INTEGER, valueOfMethod,
                valueMethod);
    }

    /**
     * @throws Exception
     */
    public void testGetValue_index() throws Exception {
        // ## Arrange ##
        final ResultSet resultSet = new MockResultSet() {
            public Object getObject(int columnIndex) throws SQLException {
                return new Integer(2);
            }
        };

        // ## Act ##
        final Authority value = (Authority) valueType.getValue(resultSet, 0);

        // ## Assert ##
        assertEquals(2, value.value());
    }

    /**
     * @throws Exception
     */
    public void testGetValue_name() throws Exception {
        // ## Arrange ##
        final ResultSet resultSet = new MockResultSet() {
            public Object getObject(String columnName) throws SQLException {
                return new Integer(2);
            }
        };

        // ## Act ##
        final Authority value = (Authority) valueType.getValue(resultSet,
                "auth");

        // ## Assert ##
        assertEquals(2, value.value());
    }

    /**
     * @throws Exception
     */
    public void testBindValue() throws Exception {
        // ## Arrange ##
        final MockStatement statement = new MockStatement();

        // ## Act ##
        valueType.bindValue(statement, 1, Authority.valueOf(2));
        // ## Assert ##
        assertEquals(statement.value, 2);
    }

    /**
     * 
     * @throws Exception
     */
    public void testToText() throws Exception {
        assertEquals("2", valueType.toText(Authority.valueOf(2)));
    }

    private static class MockResultSet extends ResultSetWrapper {
        MockResultSet() {
            super(null);
        }
    }

    private static class MockStatement extends PreparedStatementWrapper {
        private int value;

        MockStatement() {
            super(null, null);
        }

        public void setInt(int columnIndex, int v) throws SQLException {
            value = v;
        }
    }
}
