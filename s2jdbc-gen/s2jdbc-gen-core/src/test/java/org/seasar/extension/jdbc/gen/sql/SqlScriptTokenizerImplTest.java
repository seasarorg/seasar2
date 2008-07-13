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
package org.seasar.extension.jdbc.gen.sql;

import org.junit.Before;
import org.junit.Test;
import org.seasar.extension.jdbc.gen.dialect.OracleGenDialect;

import static org.junit.Assert.*;
import static org.seasar.extension.jdbc.gen.SqlScriptTokenizer.TokenType.*;

/**
 * @author taedium
 * 
 */
public class SqlScriptTokenizerImplTest {

    private SqlScriptTokenizerImpl tokenizer;

    @Before
    public void setUp() throws Exception {
        tokenizer = new SqlScriptTokenizerImpl(new OracleGenDialect(), ';');
    }

    @Test
    public void testGetToken_endOfFragment() {
        tokenizer.addFragment("aaa");
        assertEquals(WORD, tokenizer.nextToken());
        assertEquals("aaa", tokenizer.getToken());
        assertEquals(END_OF_FRAGMENT, tokenizer.nextToken());
        assertEquals("", tokenizer.getToken());
        assertEquals(END_OF_FRAGMENT, tokenizer.nextToken());
        assertEquals("", tokenizer.getToken());
    }

    @Test
    public void testGetToken_endOfFile() {
        tokenizer.addFragment("aaa");
        assertEquals(WORD, tokenizer.nextToken());
        assertEquals("aaa", tokenizer.getToken());
        assertEquals(END_OF_FRAGMENT, tokenizer.nextToken());
        assertEquals("", tokenizer.getToken());

        tokenizer.addFragment(null);
        assertEquals(END_OF_FILE, tokenizer.nextToken());
        assertNull(tokenizer.getToken());
        assertEquals(END_OF_FILE, tokenizer.nextToken());
        assertNull(tokenizer.getToken());
    }

    @Test
    public void testGetToken_lineComment() {
        tokenizer.addFragment("aaa -- bbb /* ; ");
        assertEquals(WORD, tokenizer.nextToken());
        assertEquals("aaa", tokenizer.getToken());
        assertEquals(OTHER, tokenizer.nextToken());
        assertEquals(" ", tokenizer.getToken());
        assertEquals(LINE_COMMENT, tokenizer.nextToken());
        assertEquals("-- bbb /* ; ", tokenizer.getToken());
        assertEquals(END_OF_FRAGMENT, tokenizer.nextToken());
    }

    @Test
    public void testGetToken_blockCommentInTwoFragments() {
        tokenizer.addFragment("aaa/*b");
        assertEquals(WORD, tokenizer.nextToken());
        assertEquals("aaa", tokenizer.getToken());
        assertEquals(START_OF_BLOCK_COMMENT, tokenizer.nextToken());
        assertEquals("/*", tokenizer.getToken());
        assertEquals(BLOCK_COMMENT, tokenizer.nextToken());
        assertEquals("b", tokenizer.getToken());
        assertEquals(END_OF_FRAGMENT, tokenizer.nextToken());
        assertEquals("", tokenizer.getToken());

        tokenizer.addFragment("bb*/ccc");
        assertEquals(BLOCK_COMMENT, tokenizer.nextToken());
        assertEquals("bb", tokenizer.getToken());
        assertEquals(END_OF_BLOCK_COMMENT, tokenizer.nextToken());
        assertEquals("*/", tokenizer.getToken());
        assertEquals(WORD, tokenizer.nextToken());
        assertEquals("ccc", tokenizer.getToken());
        assertEquals(END_OF_FRAGMENT, tokenizer.nextToken());
    }

    @Test
    public void testGetToken_blockCommentsInOneFragment() {
        tokenizer.addFragment("aaa/*bbb*/ccc/*ddd*/");
        assertEquals(WORD, tokenizer.nextToken());
        assertEquals("aaa", tokenizer.getToken());
        assertEquals(START_OF_BLOCK_COMMENT, tokenizer.nextToken());
        assertEquals("/*", tokenizer.getToken());
        assertEquals(BLOCK_COMMENT, tokenizer.nextToken());
        assertEquals("bbb", tokenizer.getToken());
        assertEquals(END_OF_BLOCK_COMMENT, tokenizer.nextToken());
        assertEquals("*/", tokenizer.getToken());
        assertEquals(WORD, tokenizer.nextToken());
        assertEquals("ccc", tokenizer.getToken());
        assertEquals(START_OF_BLOCK_COMMENT, tokenizer.nextToken());
        assertEquals("/*", tokenizer.getToken());
        assertEquals(BLOCK_COMMENT, tokenizer.nextToken());
        assertEquals("ddd", tokenizer.getToken());
        assertEquals(END_OF_BLOCK_COMMENT, tokenizer.nextToken());
        assertEquals("*/", tokenizer.getToken());
        assertEquals(END_OF_FRAGMENT, tokenizer.nextToken());
    }

    @Test
    public void testGetToken_statementDelimiter() throws Exception {
        tokenizer.addFragment("select * from aaa; ");
        assertEquals(WORD, tokenizer.nextToken());
        assertEquals("select", tokenizer.getToken());
        assertEquals(OTHER, tokenizer.nextToken());
        assertEquals(" * ", tokenizer.getToken());
        assertEquals(WORD, tokenizer.nextToken());
        assertEquals("from", tokenizer.getToken());
        assertEquals(OTHER, tokenizer.nextToken());
        assertEquals(" ", tokenizer.getToken());
        assertEquals(WORD, tokenizer.nextToken());
        assertEquals("aaa", tokenizer.getToken());
        assertEquals(STATEMENT_DELIMITER, tokenizer.nextToken());
        assertEquals(";", tokenizer.getToken());
        assertEquals(OTHER, tokenizer.nextToken());
        assertEquals(" ", tokenizer.getToken());
        assertEquals(END_OF_FRAGMENT, tokenizer.nextToken());
    }

    @Test
    public void testGetToken_blockDelimiter() throws Exception {
        tokenizer.addFragment("aaa go");
        assertEquals(WORD, tokenizer.nextToken());
        assertEquals("aaa", tokenizer.getToken());
        assertEquals(OTHER, tokenizer.nextToken());
        assertEquals(" ", tokenizer.getToken());
        assertEquals(WORD, tokenizer.nextToken());
        assertEquals("go", tokenizer.getToken());
        assertEquals(END_OF_FRAGMENT, tokenizer.nextToken());
        assertEquals("", tokenizer.getToken());

        tokenizer.addFragment("/ ");
        assertEquals(BLOCK_DELIMITER, tokenizer.nextToken());
        assertEquals("/ ", tokenizer.getToken());
        assertEquals(END_OF_FRAGMENT, tokenizer.nextToken());
    }

    @Test
    public void testGetToken_wordAndOther() throws Exception {
        tokenizer.addFragment("select,");
        assertEquals(WORD, tokenizer.nextToken());
        assertEquals("select", tokenizer.getToken());
        assertEquals(OTHER, tokenizer.nextToken());
        assertEquals(",", tokenizer.getToken());
        assertEquals(END_OF_FRAGMENT, tokenizer.nextToken());
        assertEquals("", tokenizer.getToken());
        tokenizer.addFragment("bbb");
        assertEquals(WORD, tokenizer.nextToken());
        assertEquals("bbb", tokenizer.getToken());
        assertEquals(END_OF_FRAGMENT, tokenizer.nextToken());
    }

    @Test
    public void testGetToken_quote() throws Exception {
        tokenizer.addFragment("'aaa'");
        assertEquals(QUOTE, tokenizer.nextToken());
        assertEquals("'aaa'", tokenizer.getToken());
        assertEquals(END_OF_FRAGMENT, tokenizer.nextToken());
    }

    @Test
    public void testGetToken_quoteNotClosed() throws Exception {
        tokenizer.addFragment("'aaa");
        assertEquals(QUOTE, tokenizer.nextToken());
        assertEquals("'aaa", tokenizer.getToken());
        assertEquals(END_OF_FRAGMENT, tokenizer.nextToken());
    }

    @Test
    public void test_sqlBlock() throws Exception {
        tokenizer.addFragment("begin aaa; end;");
        assertEquals(WORD, tokenizer.nextToken());
        assertEquals("begin", tokenizer.getToken());
        assertTrue(tokenizer.isInSqlBlock());
        assertEquals(OTHER, tokenizer.nextToken());
        assertEquals(" ", tokenizer.getToken());
        assertTrue(tokenizer.isInSqlBlock());
        assertEquals(WORD, tokenizer.nextToken());
        assertEquals("aaa", tokenizer.getToken());
        assertTrue(tokenizer.isInSqlBlock());
        assertEquals(STATEMENT_DELIMITER, tokenizer.nextToken());
        assertEquals(";", tokenizer.getToken());
        assertTrue(tokenizer.isInSqlBlock());
        assertEquals(OTHER, tokenizer.nextToken());
        assertEquals(" ", tokenizer.getToken());
        assertTrue(tokenizer.isInSqlBlock());
        assertEquals(WORD, tokenizer.nextToken());
        assertEquals("end", tokenizer.getToken());
        assertTrue(tokenizer.isInSqlBlock());
        assertEquals(STATEMENT_DELIMITER, tokenizer.nextToken());
        assertEquals(";", tokenizer.getToken());
        assertTrue(tokenizer.isInSqlBlock());
        assertEquals(END_OF_FRAGMENT, tokenizer.nextToken());
        assertEquals("", tokenizer.getToken());
        assertTrue(tokenizer.isInSqlBlock());

        tokenizer.addFragment("/");
        assertEquals(BLOCK_DELIMITER, tokenizer.nextToken());
        assertEquals("/", tokenizer.getToken());
        assertFalse(tokenizer.isInSqlBlock());
        assertEquals(END_OF_FRAGMENT, tokenizer.nextToken());
        assertEquals("", tokenizer.getToken());

        tokenizer.addFragment("aaa;");
        assertEquals(WORD, tokenizer.nextToken());
        assertEquals("aaa", tokenizer.getToken());
        assertEquals(STATEMENT_DELIMITER, tokenizer.nextToken());
        assertEquals(";", tokenizer.getToken());
    }
}
