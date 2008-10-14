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
import org.seasar.extension.jdbc.gen.dialect.GenDialect.SqlBlockContext;
import org.seasar.extension.jdbc.gen.sqltype.SqlType;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class PostgreGenDialectTest {

    private PostgreGenDialect dialect = new PostgreGenDialect();

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetSqlType_integer() throws Exception {
        SqlType type = dialect.getSqlType(Types.INTEGER);
        assertEquals("serial", type.getDataType(10, 0, 0, true));
        assertEquals("integer", type.getDataType(10, 0, 0, false));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetSqlType_bigint() throws Exception {
        SqlType type = dialect.getSqlType(Types.BIGINT);
        assertEquals("bigserial", type.getDataType(10, 0, 0, true));
        assertEquals("bigint", type.getDataType(10, 0, 0, false));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetSqlType_clob() throws Exception {
        SqlType type = dialect.getSqlType(Types.CLOB);
        assertEquals("oid", type.getDataType(10, 0, 0, false));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testCreateSqlBlockContext() throws Exception {
        SqlBlockContext context = dialect.createSqlBlockContext();
        assertFalse(context.isInSqlBlock());
        context.addKeyword("$$");
        assertTrue(context.isInSqlBlock());
        context.addKeyword("$$");
        assertFalse(context.isInSqlBlock());
    }
}
