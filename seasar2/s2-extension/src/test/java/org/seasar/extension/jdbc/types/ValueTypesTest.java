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
package org.seasar.extension.jdbc.types;

import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.HashMap;

import junit.framework.TestCase;

import org.seasar.extension.jdbc.ValueType;
import org.seasar.extension.jdbc.impl.ResultSetWrapper;

/**
 * 
 */
public class ValueTypesTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testGetValueType() throws Exception {
        assertEquals(ValueTypes.TIMESTAMP, ValueTypes
                .getValueType(GregorianCalendar.class));

    }

    /**
     * @throws Exception
     */
    public void testGetValueType_unknownClass() throws Exception {
        assertEquals(ValueTypes.OBJECT, ValueTypes.getValueType(getClass()));
    }

    /**
     * @throws Exception
     */
    public void testUserDefineType() throws Exception {
        ValueType valueType = ValueTypes.getValueType(Authority.class);
        assertNotNull(valueType);
        assertTrue(valueType instanceof UserDefineType);
        final ResultSet resultSet = new MockResultSet() {
            public Object getObject(int columnIndex) throws SQLException {
                return new Integer(2);
            }
        };
        final Authority value = (Authority) valueType.getValue(resultSet, 0);
        assertEquals(2, value.value());
    }

    /**
     * @throws Exception
     */
    public void testIsSimpleType() throws Exception {
        assertFalse(ValueTypes.isSimpleType(HashMap.class));
        assertTrue(ValueTypes.isSimpleType(byte[].class));
        assertTrue(ValueTypes.isSimpleType(InputStream.class));
    }

    private static class MockResultSet extends ResultSetWrapper {
        MockResultSet() {
            super(null);
        }
    }
}