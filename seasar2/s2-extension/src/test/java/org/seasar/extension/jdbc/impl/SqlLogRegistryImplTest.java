/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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
package org.seasar.extension.jdbc.impl;

import org.seasar.extension.jdbc.SqlLog;
import org.seasar.extension.unit.S2TestCase;

/**
 * @author taedium
 * 
 */
public class SqlLogRegistryImplTest extends S2TestCase {

    private String rawSql = "select * from emp where empno = ?";

    private String completeSql = "select * from emp where empno = 10";

    private Object[] bindArgs = new Object[] { new Integer(10) };

    private Object[] bindArgTypes = new Object[] { Integer.class };

    private SqlLog sqlLog = new SqlLogImpl(rawSql, completeSql, bindArgs,
            bindArgTypes);

    private SqlLog sqlLog2 = new SqlLogImpl(rawSql, completeSql, bindArgs,
            bindArgTypes);

    private SqlLog sqlLog3 = new SqlLogImpl(rawSql, completeSql, bindArgs,
            bindArgTypes);

    private SqlLog sqlLog4 = new SqlLogImpl(rawSql, completeSql, bindArgs,
            bindArgTypes);

    /**
     * 
     * @throws Exception
     */
    public void testGet() throws Exception {
        SqlLogRegistryImpl registry = new SqlLogRegistryImpl(3);
        registry.add(sqlLog);
        registry.add(sqlLog2);
        assertSame(sqlLog, registry.get(0));
        assertSame(sqlLog2, registry.get(1));
    }

    /**
     * 
     * @throws Exception
     */
    public void testGetLast() throws Exception {
        SqlLogRegistryImpl registry = new SqlLogRegistryImpl(3);
        assertNull(registry.getLast());
        registry.add(sqlLog);
        assertSame(sqlLog, registry.getLast());
        registry.add(sqlLog2);
        assertSame(sqlLog2, registry.getLast());
    }

    /**
     * 
     * @throws Exception
     */
    public void testGetSize() throws Exception {
        SqlLogRegistryImpl registry = new SqlLogRegistryImpl(2);
        registry.add(sqlLog);
        registry.add(sqlLog2);
        registry.add(sqlLog3);
        registry.add(sqlLog4);
        assertEquals(2, registry.getSize());
        assertSame(sqlLog3, registry.get(0));
        assertSame(sqlLog4, registry.get(1));
    }

}
