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
package org.seasar.extension.jdbc.gen.dialect;

import java.math.BigDecimal;
import java.sql.Types;

import org.junit.Test;
import org.seasar.extension.jdbc.gen.GenDialect.ColumnType;
import org.seasar.extension.jdbc.gen.GenDialect.SqlBlockContext;
import org.seasar.extension.jdbc.gen.GenDialect.SqlType;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class OracleGenDialectTest {

    private OracleGenDialect dialect = new OracleGenDialect();

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetColumnType_number() throws Exception {
        ColumnType type = dialect.getColumnType("NUMBER");
        assertEquals("number(10,5)", type.getColumnDefinition(0, 10, 5));
        assertEquals(Integer.class, type.getAttributeClass(0, 8, 0));
        assertEquals(BigDecimal.class, type.getAttributeClass(0, 8, 2));
        assertEquals(Long.class, type.getAttributeClass(0, 11, 0));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetSqlType_binary() throws Exception {
        SqlType type = dialect.getSqlType(Types.BINARY);
        assertEquals("raw(2000)", type.getColumnDefinition(2000, 0, 0, false));
        assertEquals("blob", type.getColumnDefinition(2001, 0, 0, false));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetSqlType_varchar() throws Exception {
        SqlType type = dialect.getSqlType(Types.VARCHAR);
        assertEquals("varchar2(4000)", type.getColumnDefinition(4000, 0, 0,
                false));
        assertEquals("clob", type.getColumnDefinition(4001, 0, 0, false));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetSqlType_bigint() throws Exception {
        SqlType type = dialect.getSqlType(Types.BIGINT);
        assertEquals("number(10,0)", type.getColumnDefinition(0, 10, 0, false));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testCreateSqlBlockContext() throws Exception {
        SqlBlockContext context = dialect.createSqlBlockContext();
        context.addKeyword("create");
        context.addKeyword("or");
        context.addKeyword("replace");
        context.addKeyword("procedure");
        assertTrue(context.isInSqlBlock());

        context = dialect.createSqlBlockContext();
        context.addKeyword("drop");
        assertFalse(context.isInSqlBlock());

        context = dialect.createSqlBlockContext();
        context.addKeyword("begin");
        assertTrue(context.isInSqlBlock());
    }
}
