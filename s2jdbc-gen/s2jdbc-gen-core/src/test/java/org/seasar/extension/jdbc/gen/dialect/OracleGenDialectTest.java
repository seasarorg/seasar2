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
import org.seasar.extension.jdbc.gen.GenDialect.DbType;
import org.seasar.extension.jdbc.gen.GenDialect.JavaType;

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
    public void testDbType_decimal() throws Exception {
        DbType dbType = dialect.getDbType(Types.DECIMAL);
        assertEquals("number(10,5)", dbType.getDefinition(0, 10, 5));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testDbType_binary() throws Exception {
        DbType dbType = dialect.getDbType(Types.BINARY);
        assertEquals("raw(2000)", dbType.getDefinition(2000, 0, 0));
        assertEquals("long raw", dbType.getDefinition(2001, 0, 0));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testDbType_varchar() throws Exception {
        DbType dbType = dialect.getDbType(Types.VARCHAR);
        assertEquals("varchar2(4000)", dbType.getDefinition(4000, 0, 0));
        assertEquals("long", dbType.getDefinition(4001, 0, 0));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testDbType_bigint() throws Exception {
        DbType dbType = dialect.getDbType(Types.BIGINT);
        assertEquals("numeric(10,0)", dbType.getDefinition(0, 10, 0));
    }

    /**
     * 
     * @throws Exception
     */
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

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testIsSqlBlockStartWords() throws Exception {
        assertTrue(dialect.isSqlBlockStartWords(Arrays.asList("create", "or",
                "replace", "procedure")));
        assertFalse(dialect.isSqlBlockStartWords(Arrays.asList("drop",
                "procedure")));
        assertTrue(dialect.isSqlBlockStartWords(Arrays.asList("begin")));
    }
}
