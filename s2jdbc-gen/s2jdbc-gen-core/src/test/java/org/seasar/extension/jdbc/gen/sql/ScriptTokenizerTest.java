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

import org.junit.Test;

import static org.junit.Assert.*;
import static org.seasar.extension.jdbc.gen.SqlScriptTokenizer.TokenType.*;

/**
 * @author taedium
 * 
 */
public class ScriptTokenizerTest {

    @Test
    public void testGetToken_endOfFragment() {
        SqlScriptTokenizerImpl tokenizer = new SqlScriptTokenizerImpl(';', "go");
        tokenizer.addFragment("aaa");
        assertEquals(SQL, tokenizer.nextToken());
        assertEquals("aaa", tokenizer.getToken());
        assertEquals(END_OF_FRAGMENT, tokenizer.nextToken());
        assertEquals("", tokenizer.getToken());
        assertEquals(END_OF_FRAGMENT, tokenizer.nextToken());
        assertEquals("", tokenizer.getToken());
    }

    @Test
    public void testGetToken_endOfFile() {
        SqlScriptTokenizerImpl tokenizer = new SqlScriptTokenizerImpl(';', "go");
        tokenizer.addFragment("aaa");
        assertEquals(SQL, tokenizer.nextToken());
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
        SqlScriptTokenizerImpl tokenizer = new SqlScriptTokenizerImpl(';', "go");
        tokenizer.addFragment("aaa -- bbb /* ; ");
        assertEquals(SQL, tokenizer.nextToken());
        assertEquals("aaa ", tokenizer.getToken());
        assertEquals(LINE_COMMENT, tokenizer.nextToken());
        assertEquals("-- bbb /* ; ", tokenizer.getToken());
        assertEquals(END_OF_FRAGMENT, tokenizer.nextToken());
    }

    @Test
    public void testGetToken_blockCommentInTwoFragments() {
        SqlScriptTokenizerImpl tokenizer = new SqlScriptTokenizerImpl(';', "go");
        tokenizer.addFragment("aaa/*b");
        assertEquals(SQL, tokenizer.nextToken());
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
        assertEquals(SQL, tokenizer.nextToken());
        assertEquals("ccc", tokenizer.getToken());
        assertEquals(END_OF_FRAGMENT, tokenizer.nextToken());
    }

    @Test
    public void testGetToken_blockCommentsInOneFragment() {
        SqlScriptTokenizerImpl tokenizer = new SqlScriptTokenizerImpl(';', "go");
        tokenizer.addFragment("aaa/*bbb*/ccc/*ddd*/");
        assertEquals(SQL, tokenizer.nextToken());
        assertEquals("aaa", tokenizer.getToken());
        assertEquals(START_OF_BLOCK_COMMENT, tokenizer.nextToken());
        assertEquals("/*", tokenizer.getToken());
        assertEquals(BLOCK_COMMENT, tokenizer.nextToken());
        assertEquals("bbb", tokenizer.getToken());
        assertEquals(END_OF_BLOCK_COMMENT, tokenizer.nextToken());
        assertEquals("*/", tokenizer.getToken());
        assertEquals(SQL, tokenizer.nextToken());
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
        SqlScriptTokenizerImpl tokenizer = new SqlScriptTokenizerImpl(';', "go");
        tokenizer.addFragment("select * from aaa; ");
        assertEquals(SQL, tokenizer.nextToken());
        assertEquals("select * from aaa", tokenizer.getToken());
        assertEquals(STATEMENT_DELIMITER, tokenizer.nextToken());
        assertEquals(";", tokenizer.getToken());
        assertEquals(SQL, tokenizer.nextToken());
        assertEquals(" ", tokenizer.getToken());
        assertEquals(END_OF_FRAGMENT, tokenizer.nextToken());
    }

    @Test
    public void testGetToken_blockDelimiter() throws Exception {
        SqlScriptTokenizerImpl tokenizer = new SqlScriptTokenizerImpl(';', "go");
        tokenizer.addFragment("aaa go");
        assertEquals(SQL, tokenizer.nextToken());
        assertEquals("aaa go", tokenizer.getToken());
        assertEquals(END_OF_FRAGMENT, tokenizer.nextToken());
        assertEquals("", tokenizer.getToken());
        tokenizer.addFragment("go ");
        assertEquals(BLOCK_DELIMITER, tokenizer.nextToken());
        assertEquals("go ", tokenizer.getToken());
        assertEquals(END_OF_FRAGMENT, tokenizer.nextToken());
    }

    @Test
    public void testGetToken_sql() throws Exception {
        SqlScriptTokenizerImpl tokenizer = new SqlScriptTokenizerImpl(';', "go");
        tokenizer.addFragment("select aaa,");
        assertEquals(SQL, tokenizer.nextToken());
        assertEquals("select aaa,", tokenizer.getToken());
        assertEquals(END_OF_FRAGMENT, tokenizer.nextToken());
        assertEquals("", tokenizer.getToken());
        tokenizer.addFragment("bbb");
        assertEquals(SQL, tokenizer.nextToken());
        assertEquals("bbb", tokenizer.getToken());
        assertEquals(END_OF_FRAGMENT, tokenizer.nextToken());
    }

    @Test
    public void testGetToken_quote() throws Exception {
        SqlScriptTokenizerImpl tokenizer = new SqlScriptTokenizerImpl(';', "go");
        tokenizer.addFragment("select 'aaa'");
        assertEquals(SQL, tokenizer.nextToken());
        assertEquals("select ", tokenizer.getToken());
        assertEquals(QUOTE, tokenizer.nextToken());
        assertEquals("'aaa'", tokenizer.getToken());
        assertEquals(END_OF_FRAGMENT, tokenizer.nextToken());
    }

    @Test
    public void testGetToken_quoteNotClosed() throws Exception {
        SqlScriptTokenizerImpl tokenizer = new SqlScriptTokenizerImpl(';', "go");
        tokenizer.addFragment("select 'aaa");
        assertEquals(SQL, tokenizer.nextToken());
        assertEquals("select ", tokenizer.getToken());
        assertEquals(QUOTE, tokenizer.nextToken());
        assertEquals("'aaa", tokenizer.getToken());
        assertEquals(END_OF_FRAGMENT, tokenizer.nextToken());
    }
}
