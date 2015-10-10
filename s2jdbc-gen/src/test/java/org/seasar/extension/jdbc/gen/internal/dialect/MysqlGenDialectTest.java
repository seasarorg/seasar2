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

import java.sql.Types;

import org.junit.Test;
import org.seasar.extension.jdbc.gen.sqltype.SqlType;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class MysqlGenDialectTest {

    private MysqlGenDialect dialect = new MysqlGenDialect();

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetSqlType_blob() throws Exception {
        SqlType type = dialect.getSqlType(Types.BLOB);
        assertEquals("tinyblob", type.getDataType(255, 0, 0, false));
        assertEquals("blob", type.getDataType(256, 0, 0, false));
        assertEquals("blob", type.getDataType(65535, 0, 0, false));
        assertEquals("mediumblob", type.getDataType(65536, 0, 0, false));
        assertEquals("mediumblob", type.getDataType(16777215, 0, 0, false));
        assertEquals("longblob", type.getDataType(16777216, 0, 0, false));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetSqlType_clob() throws Exception {
        SqlType type = dialect.getSqlType(Types.CLOB);
        assertEquals("tinytext", type.getDataType(255, 0, 0, false));
        assertEquals("text", type.getDataType(256, 0, 0, false));
        assertEquals("text", type.getDataType(65535, 0, 0, false));
        assertEquals("mediumtext", type.getDataType(65536, 0, 0, false));
        assertEquals("mediumtext", type.getDataType(16777215, 0, 0, false));
        assertEquals("longtext", type.getDataType(16777216, 0, 0, false));
    }
}
