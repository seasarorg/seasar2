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

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

/**
 * @author higa
 * 
 */
public class FromClauseTest extends TestCase {

    /**
     * 
     */
    public void testAddSql() {
        FromClause fromClause = new FromClause();
        fromClause.addSql("AAA", "_T1");
        assertEquals(" from AAA _T1", fromClause.toSql());
    }

    /**
     * 
     */
    public void testAddSql_leftOuterJoin() {
        FromClause fromClause = new FromClause();
        fromClause.addSql("AAA", "_T1");
        List<JoinColumnMeta> joinColumnMetaList = new ArrayList<JoinColumnMeta>();
        joinColumnMetaList.add(new JoinColumnMeta("BBB_ID", "BBB_ID"));
        fromClause.addSql(JoinType.LEFT_OUTER, "BBB", "_T2", "_T1", "_T2",
                joinColumnMetaList, " with (updlock, rowlock)", null);
        assertEquals(
                " from AAA _T1 left outer join BBB _T2 with (updlock, rowlock) on _T1.BBB_ID = _T2.BBB_ID",
                fromClause.toSql());
    }

    /**
     * 
     */
    public void testAddSql_innerJoin() {
        FromClause fromClause = new FromClause();
        fromClause.addSql("AAA", "_T1");
        List<JoinColumnMeta> joinColumnMetaList = new ArrayList<JoinColumnMeta>();
        joinColumnMetaList.add(new JoinColumnMeta("BBB_ID", "BBB_ID"));
        fromClause.addSql(JoinType.INNER, "BBB", "_T2", "_T1", "_T2",
                joinColumnMetaList, " with (updlock, rowlock)", null);
        assertEquals(
                " from AAA _T1 inner join BBB _T2 with (updlock, rowlock) on _T1.BBB_ID = _T2.BBB_ID",
                fromClause.toSql());
    }

    /**
     * 
     */
    public void testAddSql_joinColumns() {
        FromClause fromClause = new FromClause();
        fromClause.addSql("AAA", "_T1");
        List<JoinColumnMeta> joinColumnMetaList = new ArrayList<JoinColumnMeta>();
        joinColumnMetaList.add(new JoinColumnMeta("BBB_ID", "BBB_ID"));
        joinColumnMetaList.add(new JoinColumnMeta("BBB_ID2", "BBB_ID2"));
        fromClause.addSql(JoinType.INNER, "BBB", "_T2", "_T1", "_T2",
                joinColumnMetaList, " with (updlock, rowlock)",
                "_T1.XXX is null and _T2.YYY is not null");
        assertEquals(
                " from AAA _T1 inner join BBB _T2 with (updlock, rowlock) on _T1.BBB_ID = _T2.BBB_ID and _T1.BBB_ID2 = _T2.BBB_ID2 and _T1.XXX is null and _T2.YYY is not null",
                fromClause.toSql());
    }
}