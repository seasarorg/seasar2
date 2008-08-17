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
package org.seasar.extension.jdbc.gen.internal.dialect;

import java.sql.Types;

import org.junit.Test;
import org.seasar.extension.jdbc.gen.exception.UnsupportedSqlTypeRuntimeException;
import org.seasar.extension.jdbc.gen.sqltype.SqlType;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class StandardGenDialectTest {

    private StandardGenDialect dialect = new StandardGenDialect();

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetSqlType_char() throws Exception {
        SqlType type = dialect.getSqlType(Types.CHAR);
        assertEquals("char(1)", type.getColumnDefinition(10, 0, 0, false));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetSqlType_varchar() throws Exception {
        SqlType type = dialect.getSqlType(Types.VARCHAR);
        assertEquals("varchar(10)", type.getColumnDefinition(10, 0, 0, false));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetSqlType_unsupported() throws Exception {
        try {
            dialect.getSqlType(Types.OTHER);
            fail();
        } catch (UnsupportedSqlTypeRuntimeException expected) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetColumnType_unknown() throws Exception {
        assertNull(dialect.getColumnType("hoge"));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testQuote() throws Exception {
        assertEquals("\"aaa\"", dialect.quote("aaa"));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testUnquote() throws Exception {
        assertEquals("aaa", dialect.unquote("\"aaa\""));
    }

}
