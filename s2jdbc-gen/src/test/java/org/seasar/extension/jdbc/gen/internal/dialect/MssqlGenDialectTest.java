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
package org.seasar.extension.jdbc.gen.internal.dialect;

import java.math.BigDecimal;
import java.sql.Types;

import org.junit.Test;
import org.seasar.extension.jdbc.gen.dialect.GenDialect.ColumnType;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class MssqlGenDialectTest {

    private MssqlGenDialect dialect = new MssqlGenDialect();

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetColumnType_int() throws Exception {
        ColumnType type = dialect.getColumnType("int identity", Types.OTHER);
        assertEquals("int", type.getColumnDefinition(0, 0, 0, null));
        assertEquals(Integer.class, type.getAttributeClass(0, 0, 0));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetColumnType_bigint() throws Exception {
        ColumnType type = dialect.getColumnType("bigint identity", Types.OTHER);
        assertEquals("bigint", type.getColumnDefinition(0, 0, 0, null));
        assertEquals(Long.class, type.getAttributeClass(0, 0, 0));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetColumnType_smallint() throws Exception {
        ColumnType type = dialect.getColumnType("smallint identity",
                Types.OTHER);
        assertEquals("smallint", type.getColumnDefinition(0, 0, 0, null));
        assertEquals(Short.class, type.getAttributeClass(0, 0, 0));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetColumnType_tinyint() throws Exception {
        ColumnType type = dialect
                .getColumnType("tinyint identity", Types.OTHER);
        assertEquals("tinyint", type.getColumnDefinition(0, 0, 0, null));
        assertEquals(Short.class, type.getAttributeClass(0, 0, 0));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetColumnType_decimal() throws Exception {
        ColumnType type = dialect
                .getColumnType("decimal identity", Types.OTHER);
        assertEquals("decimal(10,2)", type.getColumnDefinition(0, 10, 2, null));
        assertEquals(BigDecimal.class, type.getAttributeClass(0, 10, 2));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetColumnType_numeric() throws Exception {
        ColumnType type = dialect
                .getColumnType("numeric identity", Types.OTHER);
        assertEquals("numeric(10,2)", type.getColumnDefinition(0, 10, 2, null));
        assertEquals(BigDecimal.class, type.getAttributeClass(0, 10, 2));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetColumnType_nchar() throws Exception {
        ColumnType type = dialect.getColumnType("nchar", Types.OTHER);
        assertEquals("nchar(10)", type.getColumnDefinition(10, 0, 0, null));
        assertEquals(String.class, type.getAttributeClass(10, 0, 0));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetColumnType_ntext() throws Exception {
        ColumnType type = dialect.getColumnType("ntext", Types.OTHER);
        assertEquals("ntext", type.getColumnDefinition(10, 0, 0, null));
        assertEquals(String.class, type.getAttributeClass(10, 0, 0));
    }
}
