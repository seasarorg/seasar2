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
package org.seasar.extension.persistence;

import junit.framework.TestCase;

/**
 * @author higa
 * 
 */
public class SqlStatementTest extends TestCase {

    private SqlStatement stmt = new SqlStatement();

    /**
     * Test method for
     * {@link org.seasar.extension.persistence.SqlStatement#addSql(java.lang.String)}.
     */
    public void testAddSql() {
        String sql = "select * from emp";
        stmt.addSql(sql);
        assertEquals(sql, stmt.getSql());
    }

    /**
     * Test method for
     * {@link org.seasar.extension.persistence.SqlStatement#addArg(java.lang.Object)}.
     */
    public void testAddArg() {
        stmt.addArg("hoge");
        stmt.addArg(1);
        Object[] args = stmt.getArgs();
        assertEquals(2, args.length);
        assertEquals("hoge", args[0]);
        assertEquals(1, args[1]);
        Class[] argClasses = stmt.getArgClasses();
        assertEquals(2, argClasses.length);
        assertEquals(String.class, argClasses[0]);
        assertEquals(Integer.class, argClasses[1]);
    }

    /**
     * Test method for
     * {@link org.seasar.extension.persistence.SqlStatement#addNullArg(java.lang.Class)}.
     */
    public void testAddNullArg() {
        stmt.addArg("hoge");
        stmt.addNullArg(Integer.class);
        Object[] args = stmt.getArgs();
        assertEquals(2, args.length);
        assertEquals("hoge", args[0]);
        assertNull(args[1]);
        Class[] argClasses = stmt.getArgClasses();
        assertEquals(2, argClasses.length);
        assertEquals(String.class, argClasses[0]);
        assertEquals(Integer.class, argClasses[1]);
    }

}
