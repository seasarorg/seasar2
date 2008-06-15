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

import java.sql.Types;

import org.junit.Test;
import org.seasar.extension.jdbc.gen.DataType;

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
        DataType sqlType = dialect.getDataType(Types.DECIMAL);
        assertEquals("number(10,5)", sqlType.getDefinition(0, 10, 5));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testDataType_numeric() throws Exception {
        DataType sqlType = dialect.getDataType(Types.NUMERIC);
        assertEquals("number(10,5)", sqlType.getDefinition(0, 10, 5));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testDataType_varbinary() throws Exception {
        DataType sqlType = dialect.getDataType(Types.VARBINARY);
        assertEquals("row(2000)", sqlType.getDefinition(2000, 0, 0));
        assertEquals("long row", sqlType.getDefinition(2001, 0, 0));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testDataType_varchar() throws Exception {
        DataType sqlType = dialect.getDataType(Types.VARCHAR);
        assertEquals("varchar2(4000)", sqlType.getDefinition(4000, 0, 0));
        assertEquals("long", sqlType.getDefinition(4001, 0, 0));
    }

}
