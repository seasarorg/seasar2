/*
 * Copyright 2004-2013 the Seasar Foundation and the Others.
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
package org.seasar.extension.jdbc;

import junit.framework.TestCase;

/**
 * @author higa
 * 
 */
public class SelectClauseTest extends TestCase {

    /**
     * 
     */
    public void testAddSql() {
        SelectClause selectClause = new SelectClause();
        selectClause.addSql("T1_", "AAA_ID");
        assertEquals("T1_.AAA_ID as C1_", selectClause.toSql());
        assertEquals("C1_", selectClause.getColumnAlias("T1_", "AAA_ID"));
        selectClause.addSql("T1_", "AAA_NAME");
        assertEquals("T1_.AAA_ID as C1_, T1_.AAA_NAME as C2_", selectClause.toSql());
        assertEquals("C1_", selectClause.getColumnAlias("T1_", "AAA_ID"));
        assertEquals("C2_", selectClause.getColumnAlias("T1_", "AAA_NAME"));
    }

    /**
     * 
     */
    public void testAddSql2() {
        SelectClause selectClause = new SelectClause();
        selectClause.addSql("count(*)");
        assertEquals("count(*)", selectClause.toSql());
        selectClause.addSql("max(a)");
        assertEquals("count(*), max(a)", selectClause.toSql());
    }

    /**
     * 
     */
    public void testGetLength() {
        SelectClause selectClause = new SelectClause();
        assertEquals(0, selectClause.getLength());
        selectClause.addSql("T1_", "AAA_ID");
        assertEquals("T1_.AAA_ID as C1_".length(), selectClause.getLength());
    }

}
