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
import java.util.Arrays;

import org.junit.Test;
import org.seasar.extension.jdbc.gen.DataType;
import org.seasar.extension.jdbc.gen.JavaType;

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
    public void testDataType_decimal() throws Exception {
        DataType dataType = dialect.getDataType(Types.DECIMAL);
        assertEquals("number(10,5)", dataType.getDefinition(0, 10, 5));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testDataType_numeric() throws Exception {
        DataType dataType = dialect.getDataType(Types.NUMERIC);
        assertEquals("number(10,5)", dataType.getDefinition(0, 10, 5));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testDataType_varbinary() throws Exception {
        DataType dataType = dialect.getDataType(Types.VARBINARY);
        assertEquals("raw(2000)", dataType.getDefinition(2000, 0, 0));
        assertEquals("long raw", dataType.getDefinition(2001, 0, 0));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testDataType_varchar() throws Exception {
        DataType dataType = dialect.getDataType(Types.VARCHAR);
        assertEquals("varchar2(4000)", dataType.getDefinition(4000, 0, 0));
        assertEquals("long", dataType.getDefinition(4001, 0, 0));
    }

    @Test
    public void testJavaType_decimal() throws Exception {
        JavaType javaType = dialect.getJavaType(Types.DECIMAL);
        assertEquals(Integer.class, javaType
                .getJavaClass(8, 0, "NUMBER", false));
        assertEquals(BigDecimal.class, javaType.getJavaClass(8, 2, "NUMBER",
                false));
        assertEquals(BigDecimal.class, javaType.getJavaClass(11, 0, "NUMBER",
                false));
    }

    @Test
    public void testIsSqlBlockStartWords() throws Exception {
        assertTrue(dialect.isSqlBlockStartWords(Arrays.asList("create", "or",
                "replace", "procedure")));
        assertFalse(dialect.isSqlBlockStartWords(Arrays.asList("drop",
                "procedure")));
        assertTrue(dialect.isSqlBlockStartWords(Arrays.asList("begin")));
    }
}
