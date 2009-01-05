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
package org.seasar.extension.jdbc.dialect;

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
public class OracleDialectTest extends TestCase {

    private OracleDialect dialect = new OracleDialect();

    /** */
    public String stringField;

    /** */
    public boolean booleanField;

    /** */
    public List<?> listField;

    /** */
    public ArrayList<?> arrayListField;

    /**
     * @throws Exception
     */
    public void testConvertLimitSql_limitOnly() throws Exception {
        String sql = "select * from emp order by id for update";
        String expected = "select * from ( select * from emp order by id ) where rownum <= 5 for update";
        assertEquals(expected, dialect.convertLimitSql(sql, 0, 5));

    }

    /**
     * @throws Exception
     */
    public void testConvertLimitSql_offsetLimit() throws Exception {
        String sql = "select e.* from emp e order by id for update";
        String expected = "select * from ( select temp_.*, rownum rownumber_ from ( select e.* from emp e order by id ) temp_ where rownum <= 15 ) where rownumber_ > 5 for update";
        assertEquals(expected, dialect.convertLimitSql(sql, 5, 10));

    }

    /**
     * @throws Exception
     */
    public void testConvertLimitSql_offsetOnly() throws Exception {
        String sql = "select e.* from emp e order by id for update";
        String expected = "select * from ( select temp_.*, rownum rownumber_ from ( select e.* from emp e order by id ) temp_ ) where rownumber_ > 5 for update";
        assertEquals(expected, dialect.convertLimitSql(sql, 5, 0));

    }

    /**
     * @throws Exception
     */
    public void testGetValueType() throws Exception {
        assertEquals(ValueTypes.WAVE_DASH_STRING, dialect.getValueType(
                String.class, false, null));
        assertEquals(ValueTypes.BOOLEAN_INTEGER, dialect.getValueType(
                boolean.class, false, null));
        assertEquals(ValueTypes.ORACLE_RESULT_SET, dialect.getValueType(
                List.class, false, null));
        assertEquals(ValueTypes.ORACLE_RESULT_SET, dialect.getValueType(
                ArrayList.class, false, null));
    }

    /**
     * @throws Exception
     */
    public void testGetValueType_propertyMeta() throws Exception {
        PropertyMeta pm = new PropertyMeta();
        pm.setField(getClass().getField("stringField"));
        pm.setValueType(ValueTypes.STRING);
        assertEquals(ValueTypes.WAVE_DASH_STRING, dialect.getValueType(pm));

        pm.setField(getClass().getField("booleanField"));
        pm.setValueType(ValueTypes.BOOLEAN);
        assertEquals(ValueTypes.BOOLEAN_INTEGER, dialect.getValueType(pm));

        pm.setField(getClass().getField("listField"));
        assertEquals(ValueTypes.ORACLE_RESULT_SET, dialect.getValueType(pm));

        pm.setField(getClass().getField("arrayListField"));
        assertEquals(ValueTypes.ORACLE_RESULT_SET, dialect.getValueType(pm));
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
        assertTrue(dialect.isUniqueConstraintViolation(new Exception(
                new SQLRuntimeException(
                        SQLException.class
                                .cast(new SQLException("foo", "XXX")
                                        .initCause(new SQLException("bar",
                                                "23000", 1)))))));
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
