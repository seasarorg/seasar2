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
public class StandardGenDialectTest {

    private StandardGenDialect dialect = new StandardGenDialect();

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testDbType_char() throws Exception {
        DbType dbType = dialect.getDbType(Types.CHAR);
        assertEquals("char(10)", dbType.getDefinition(10, 0, 0));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testDbType_varbinary() throws Exception {
        DbType dbType = dialect.getDbType(Types.VARBINARY);
        assertEquals("varbinary(10)", dbType.getDefinition(10, 0, 0));
    }

    /**
     * 
     * @throws Exception
     */
    public void testDbType_varchar() throws Exception {
        DbType dbType = dialect.getDbType(Types.VARCHAR);
        assertEquals("varchar(10)", dbType.getDefinition(10, 0, 0));
    }

}
