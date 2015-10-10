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
package org.seasar.extension.sql.parser;

import junit.framework.TestCase;

import org.seasar.extension.sql.SqlTokenizer;
import org.seasar.extension.sql.TokenNotClosedRuntimeException;

/**
 * @author higa
 * 
 */
public class SqlTokenizerImplTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testNext() throws Exception {
        String sql = "SELECT * FROM emp";
        SqlTokenizer tokenizer = new SqlTokenizerImpl(sql);
        assertEquals("1", SqlTokenizer.SQL, tokenizer.next());
        assertEquals("2", sql, tokenizer.getToken());
        assertEquals("3", SqlTokenizer.EOF, tokenizer.next());
        assertEquals("4", null, tokenizer.getToken());
    }

    /**
     * @throws Exception
     */
    public void testCommentEndNotFound() throws Exception {
        String sql = "SELECT * FROM emp/*hoge";
        SqlTokenizer tokenizer = new SqlTokenizerImpl(sql);
        assertEquals("1", SqlTokenizer.SQL, tokenizer.next());
        assertEquals("2", "SELECT * FROM emp", tokenizer.getToken());
        try {
            tokenizer.next();
            fail("3");
        } catch (TokenNotClosedRuntimeException ex) {
            System.out.println(ex);
        }
    }

    /**
     * @throws Exception
     */
    public void testBindVariable() throws Exception {
        String sql = "SELECT * FROM emp WHERE job = /*job*/'CLER K' AND deptno = /*deptno*/20";
        SqlTokenizer tokenizer = new SqlTokenizerImpl(sql);
        assertEquals("1", SqlTokenizer.SQL, tokenizer.next());
        assertEquals("2", "SELECT * FROM emp WHERE job = ", tokenizer
                .getToken());
        assertEquals("3", SqlTokenizer.COMMENT, tokenizer.next());
        assertEquals("4", "job", tokenizer.getToken());
        tokenizer.skipToken();
        assertEquals("5", SqlTokenizer.SQL, tokenizer.next());
        assertEquals("6", " AND deptno = ", tokenizer.getToken());
        assertEquals("7", SqlTokenizer.COMMENT, tokenizer.next());
        assertEquals("8", "deptno", tokenizer.getToken());
        tokenizer.skipToken();
        assertEquals("9", SqlTokenizer.EOF, tokenizer.next());
    }

    /**
     * @throws Exception
     */
    public void testParseBindVariable2() throws Exception {
        String sql = "SELECT * FROM emp WHERE job = /*job*/'CLERK'/**/";
        SqlTokenizer tokenizer = new SqlTokenizerImpl(sql);
        assertEquals("1", SqlTokenizer.SQL, tokenizer.next());
        assertEquals("2", "SELECT * FROM emp WHERE job = ", tokenizer
                .getToken());
        assertEquals("3", SqlTokenizer.COMMENT, tokenizer.next());
        assertEquals("4", "job", tokenizer.getToken());
        tokenizer.skipToken();
        assertEquals("5", SqlTokenizer.COMMENT, tokenizer.next());
        assertEquals("6", "", tokenizer.getToken());
        assertEquals("7", SqlTokenizer.EOF, tokenizer.next());
    }

    /**
     * @throws Exception
     */
    public void testParseBindVariable3() throws Exception {
        String sql = "/*job*/'CLERK',";
        SqlTokenizer tokenizer = new SqlTokenizerImpl(sql);
        assertEquals("1", SqlTokenizer.COMMENT, tokenizer.next());
        assertEquals("2", "job", tokenizer.getToken());
        tokenizer.skipToken();
        assertEquals("3", SqlTokenizer.SQL, tokenizer.next());
        assertEquals("4", ",", tokenizer.getToken());
        assertEquals("5", SqlTokenizer.EOF, tokenizer.next());
    }

    /**
     * @throws Exception
     */
    public void testParseElse() throws Exception {
        String sql = "SELECT * FROM emp WHERE /*IF job != null*/job = /*job*/'CLERK'-- ELSE job is null/*END*/";
        SqlTokenizer tokenizer = new SqlTokenizerImpl(sql);
        assertEquals("1", SqlTokenizer.SQL, tokenizer.next());
        assertEquals("2", "SELECT * FROM emp WHERE ", tokenizer.getToken());
        assertEquals("3", SqlTokenizer.COMMENT, tokenizer.next());
        assertEquals("4", "IF job != null", tokenizer.getToken());
        assertEquals("5", SqlTokenizer.SQL, tokenizer.next());
        assertEquals("6", "job = ", tokenizer.getToken());
        assertEquals("7", SqlTokenizer.COMMENT, tokenizer.next());
        assertEquals("8", "job", tokenizer.getToken());
        tokenizer.skipToken();
        assertEquals("9", SqlTokenizer.ELSE, tokenizer.next());
        tokenizer.skipWhitespace();
        assertEquals("10", SqlTokenizer.SQL, tokenizer.next());
        assertEquals("11", "job is null", tokenizer.getToken());
        assertEquals("12", SqlTokenizer.COMMENT, tokenizer.next());
        assertEquals("13", "END", tokenizer.getToken());
        assertEquals("14", SqlTokenizer.EOF, tokenizer.next());
    }

    /**
     * @throws Exception
     */
    public void testParseElse2() throws Exception {
        String sql = "/*IF false*/aaa -- ELSE bbb = /*bbb*/123/*END*/";
        SqlTokenizer tokenizer = new SqlTokenizerImpl(sql);
        assertEquals("1", SqlTokenizer.COMMENT, tokenizer.next());
        assertEquals("2", "IF false", tokenizer.getToken());
        assertEquals("3", SqlTokenizer.SQL, tokenizer.next());
        assertEquals("4", "aaa ", tokenizer.getToken());
        assertEquals("5", SqlTokenizer.ELSE, tokenizer.next());
        tokenizer.skipWhitespace();
        assertEquals("6", SqlTokenizer.SQL, tokenizer.next());
        assertEquals("7", "bbb = ", tokenizer.getToken());
        assertEquals("8", SqlTokenizer.COMMENT, tokenizer.next());
        assertEquals("9", "bbb", tokenizer.getToken());
        tokenizer.skipToken();
        assertEquals("10", SqlTokenizer.COMMENT, tokenizer.next());
        assertEquals("11", "END", tokenizer.getToken());
        assertEquals("12", SqlTokenizer.EOF, tokenizer.next());
    }

    /**
     * @throws Exception
     */
    public void testAnd() throws Exception {
        String sql = " AND bbb";
        SqlTokenizer tokenizer = new SqlTokenizerImpl(sql);
        assertEquals("1", " ", tokenizer.skipWhitespace());
        assertEquals("2", "AND", tokenizer.skipToken());
        assertEquals("3", " AND", tokenizer.getBefore());
        assertEquals("3", " bbb", tokenizer.getAfter());
    }

    /**
     * @throws Exception
     */
    public void testBindVariable2() throws Exception {
        String sql = "? abc ? def ?";
        SqlTokenizer tokenizer = new SqlTokenizerImpl(sql);
        assertEquals("1", SqlTokenizer.BIND_VARIABLE, tokenizer.next());
        assertEquals("2", "$1", tokenizer.getToken());
        assertEquals("3", SqlTokenizer.SQL, tokenizer.next());
        assertEquals("4", " abc ", tokenizer.getToken());
        assertEquals("5", SqlTokenizer.BIND_VARIABLE, tokenizer.next());
        assertEquals("6", "$2", tokenizer.getToken());
        assertEquals("7", SqlTokenizer.SQL, tokenizer.next());
        assertEquals("8", " def ", tokenizer.getToken());
        assertEquals("9", SqlTokenizer.BIND_VARIABLE, tokenizer.next());
        assertEquals("10", "$3", tokenizer.getToken());
        assertEquals("11", SqlTokenizer.EOF, tokenizer.next());
    }

    /**
     * @throws Exception
     */
    public void testBindVariable3() throws Exception {
        String sql = "abc ? def";
        SqlTokenizer tokenizer = new SqlTokenizerImpl(sql);
        assertEquals("1", SqlTokenizer.SQL, tokenizer.next());
        assertEquals("2", "abc ", tokenizer.getToken());
        assertEquals("3", SqlTokenizer.BIND_VARIABLE, tokenizer.next());
        assertEquals("4", SqlTokenizer.SQL, tokenizer.next());
        assertEquals("5", " def", tokenizer.getToken());
        assertEquals("6", SqlTokenizer.EOF, tokenizer.next());
    }

    /**
     * @throws Exception
     */
    public void testBindVariable4() throws Exception {
        String sql = "/*IF false*/aaa--ELSE bbb = /*bbb*/123/*END*/";
        SqlTokenizer tokenizer = new SqlTokenizerImpl(sql);
        assertEquals("1", SqlTokenizer.COMMENT, tokenizer.next());
        assertEquals("2", "IF false", tokenizer.getToken());
        assertEquals("3", SqlTokenizer.SQL, tokenizer.next());
        assertEquals("4", "aaa", tokenizer.getToken());
        assertEquals("5", SqlTokenizer.ELSE, tokenizer.next());
        assertEquals("6", SqlTokenizer.SQL, tokenizer.next());
        assertEquals("7", " bbb = ", tokenizer.getToken());
        assertEquals("8", SqlTokenizer.COMMENT, tokenizer.next());
        assertEquals("9", "bbb", tokenizer.getToken());
    }

    /**
     * @throws Exception
     */
    public void testSkipTokenForParent() throws Exception {
        String sql = "INSERT INTO TABLE_NAME (ID) VALUES (/*id*/20)";
        SqlTokenizer tokenizer = new SqlTokenizerImpl(sql);
        assertEquals("1", SqlTokenizer.SQL, tokenizer.next());
        assertEquals("2", SqlTokenizer.COMMENT, tokenizer.next());
        assertEquals("3", "20", tokenizer.skipToken());
        assertEquals("4", SqlTokenizer.SQL, tokenizer.next());
        assertEquals("5", ")", tokenizer.getToken());
        assertEquals("6", SqlTokenizer.EOF, tokenizer.next());
    }
}