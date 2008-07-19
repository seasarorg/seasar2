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
import org.seasar.extension.jdbc.gen.GenDialect.DbType;

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
    public void testGetDbType_binary() throws Exception {
        DbType dbType = dialect.getDbType(Types.BINARY);
        assertEquals("tinyblob", dbType.getDefinition(255, 0, 0));
        assertEquals("blob", dbType.getDefinition(256, 0, 0));
        assertEquals("blob", dbType.getDefinition(65535, 0, 0));
        assertEquals("mediumblob", dbType.getDefinition(65536, 0, 0));
        assertEquals("mediumblob", dbType.getDefinition(16777215, 0, 0));
        assertEquals("longblob", dbType.getDefinition(16777216, 0, 0));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetDbType_blob() throws Exception {
        DbType dbType = dialect.getDbType(Types.BLOB);
        assertEquals("tinyblob", dbType.getDefinition(255, 0, 0));
        assertEquals("blob", dbType.getDefinition(256, 0, 0));
        assertEquals("blob", dbType.getDefinition(65535, 0, 0));
        assertEquals("mediumblob", dbType.getDefinition(65536, 0, 0));
        assertEquals("mediumblob", dbType.getDefinition(16777215, 0, 0));
        assertEquals("longblob", dbType.getDefinition(16777216, 0, 0));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetDbType_clob() throws Exception {
        DbType dbType = dialect.getDbType(Types.CLOB);
        assertEquals("tinytext", dbType.getDefinition(255, 0, 0));
        assertEquals("text", dbType.getDefinition(256, 0, 0));
        assertEquals("text", dbType.getDefinition(65535, 0, 0));
        assertEquals("mediumtext", dbType.getDefinition(65536, 0, 0));
        assertEquals("mediumtext", dbType.getDefinition(16777215, 0, 0));
        assertEquals("longtext", dbType.getDefinition(16777216, 0, 0));
    }
}
