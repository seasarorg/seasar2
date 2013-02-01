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
package org.seasar.extension.jdbc.dialect;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.types.ValueTypes;
import org.seasar.framework.exception.SQLRuntimeException;

/**
 * @author higa
 * 
 */
public class PostgreDialectTest extends TestCase {

    private PostgreDialect dialect = new PostgreDialect();

    /** */
    public String stringField;

    /** */
    public byte[] bytesField;

    /** */
    public Serializable serializableField;

    /** */
    public List<?> listField;

    /** */
    public ArrayList<?> arrayListField;

    /**
     * @throws Exception
     */
    public void testConvertLimitSql_limitOnly() throws Exception {
        String sql = "select * from emp order by id";
        String expected = sql + " limit 5";
        assertEquals(expected, dialect.convertLimitSql(sql, 0, 5));

    }

    /**
     * @throws Exception
     */
    public void testConvertLimitSql_offsetOnly() throws Exception {
        String sql = "select * from emp order by id";
        String expected = sql + " offset 5";
        assertEquals(expected, dialect.convertLimitSql(sql, 5, 0));

    }

    /**
     * @throws Exception
     */
    public void testConvertLimitSql_offsetLimit() throws Exception {
        String sql = "select e.* from emp e order by id";
        String expected = sql + " limit 10 offset 5";
        assertEquals(expected, dialect.convertLimitSql(sql, 5, 10));

    }

    /**
     * @throws Exception
     */
    public void testGetValueType() throws Exception {
        assertEquals(ValueTypes.STRING, dialect.getValueType(
                String.class,
                true, null));
        assertEquals(PostgreDialect.BLOB_TYPE, dialect.getValueType(
                byte[].class, true, null));
        assertEquals(PostgreDialect.SERIALIZABLE_BLOB_TYPE, dialect
                .getValueType(Serializable.class, true, null));
        assertEquals(ValueTypes.POSTGRE_RESULT_SET, dialect.getValueType(
                List.class, false, null));
        assertEquals(ValueTypes.POSTGRE_RESULT_SET, dialect.getValueType(
                ArrayList.class, false, null));
    }

    /**
     * @throws Exception
     */
    public void testGetValueType_propertyMeta() throws Exception {
        PropertyMeta pm = new PropertyMeta();
        pm.setField(getClass().getField("stringField"));
        pm.setLob(true);
        assertEquals(ValueTypes.STRING, dialect.getValueType(pm));

        pm = new PropertyMeta();
        pm.setField(getClass().getField("bytesField"));
        pm.setLob(true);
        assertEquals(PostgreDialect.BLOB_TYPE, dialect.getValueType(pm));

        pm = new PropertyMeta();
        pm.setField(getClass().getField("serializableField"));
        pm.setLob(true);
        assertEquals(PostgreDialect.SERIALIZABLE_BLOB_TYPE, dialect
                .getValueType(pm));

        pm = new PropertyMeta();
        pm.setField(getClass().getField("listField"));
        assertEquals(ValueTypes.POSTGRE_RESULT_SET, dialect.getValueType(pm));

        pm = new PropertyMeta();
        pm.setField(getClass().getField("arrayListField"));
        assertEquals(ValueTypes.POSTGRE_RESULT_SET, dialect.getValueType(pm));
    }

    /**
     * @throws Exception
     */
    public void testNeedsParameterForResultSet() throws Exception {
        assertTrue(dialect.needsParameterForResultSet());
    }

    /**
     * @throws Exception
     */
    public void testIsUniqueConstraintViolation() throws Exception {
        assertTrue(dialect
                .isUniqueConstraintViolation(new Exception(
                        new SQLRuntimeException(SQLException.class
                                .cast(new SQLException("foo", "XXX")
                                        .initCause(new SQLException("bar",
                                                "23505")))))));
        assertFalse(dialect
                .isUniqueConstraintViolation(new Exception(
                        new SQLRuntimeException(SQLException.class
                                .cast(new SQLException("foo", "XXX")
                                        .initCause(new SQLException("bar",
                                                "23000")))))));
        assertFalse(dialect.isUniqueConstraintViolation(new Exception(
                new RuntimeException())));
    }
}
