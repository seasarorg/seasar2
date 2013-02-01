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
public class WhereClauseTest extends TestCase {

    /**
     * 
     */
    public void testAddAndSql() {
        WhereClause whereClause = new WhereClause();
        assertEquals(30, whereClause.addAndSql("T1_.BBB_ID = _T2.BBB_ID"));
        assertEquals(" where T1_.BBB_ID = _T2.BBB_ID", whereClause.toSql());
        whereClause.addAndSql("T1_.BBB_ID = ?");
        assertEquals(" where T1_.BBB_ID = _T2.BBB_ID and T1_.BBB_ID = ?",
                whereClause.toSql());
    }

    /**
     * 
     */
    public void testAddSql() {
        WhereClause whereClause = new WhereClause();
        whereClause.addAndSql("T1_.BBB_ID = _T2.BBB_ID");
        assertEquals(" where T1_.BBB_ID = _T2.BBB_ID", whereClause.toSql());
        whereClause.addAndSql("(");
        assertEquals(" where T1_.BBB_ID = _T2.BBB_ID and (", whereClause
                .toSql());
        whereClause.addSql("T1_.BBB_ID = 1 or T1_.BBB_ID = 2");
        assertEquals(
                " where T1_.BBB_ID = _T2.BBB_ID and (T1_.BBB_ID = 1 or T1_.BBB_ID = 2",
                whereClause.toSql());
        whereClause.addSql(")");
        assertEquals(
                " where T1_.BBB_ID = _T2.BBB_ID and (T1_.BBB_ID = 1 or T1_.BBB_ID = 2)",
                whereClause.toSql());
    }

    /**
     * 
     */
    public void testRemoveSql() {
        WhereClause whereClause = new WhereClause();
        int length = whereClause.addAndSql("(");
        assertEquals(" where (", whereClause.toSql());
        whereClause.removeSql(length);
        assertEquals("", whereClause.toSql());
        assertEquals(0, whereClause.getLength());
    }
}
