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
package org.seasar.extension.jdbc.util;

import junit.framework.TestCase;

/**
 * @author higa
 * 
 */
public class QueryTokenizerTest extends TestCase {

    /**
     * 
     */
    public void testNextToken() {
        QueryTokenizer tokenizer = new QueryTokenizer("aaa=?");
        assertEquals(QueryTokenizer.TT_WORD, tokenizer.nextToken());
        assertEquals("aaa", tokenizer.getToken());
        assertEquals(QueryTokenizer.TT_OTHER, tokenizer.nextToken());
        assertEquals("=?", tokenizer.getToken());
        assertEquals(QueryTokenizer.TT_EOF, tokenizer.nextToken());
    }

    /**
     * 
     */
    public void testNextToken_firstBlank() {
        QueryTokenizer tokenizer = new QueryTokenizer(" aaa=?");
        assertEquals(QueryTokenizer.TT_OTHER, tokenizer.nextToken());
        assertEquals(" ", tokenizer.getToken());
        assertEquals(QueryTokenizer.TT_WORD, tokenizer.nextToken());
        assertEquals("aaa", tokenizer.getToken());
        assertEquals(QueryTokenizer.TT_OTHER, tokenizer.nextToken());
        assertEquals("=?", tokenizer.getToken());
        assertEquals(QueryTokenizer.TT_EOF, tokenizer.nextToken());
    }

    /**
     * 
     */
    public void testNextToken_singleQuote() {
        QueryTokenizer tokenizer = new QueryTokenizer("aaa='xxx' and bbb='yyy'");
        assertEquals(QueryTokenizer.TT_WORD, tokenizer.nextToken());
        assertEquals("aaa", tokenizer.getToken());
        assertEquals(QueryTokenizer.TT_OTHER, tokenizer.nextToken());
        assertEquals("=", tokenizer.getToken());
        assertEquals(QueryTokenizer.TT_QUOTE, tokenizer.nextToken());
        assertEquals("'xxx'", tokenizer.getToken());
        assertEquals(QueryTokenizer.TT_OTHER, tokenizer.nextToken());
        assertEquals(" ", tokenizer.getToken());
        assertEquals(QueryTokenizer.TT_WORD, tokenizer.nextToken());
        assertEquals("and", tokenizer.getToken());
        assertEquals(QueryTokenizer.TT_OTHER, tokenizer.nextToken());
        assertEquals(" ", tokenizer.getToken());
        assertEquals(QueryTokenizer.TT_WORD, tokenizer.nextToken());
        assertEquals("bbb", tokenizer.getToken());
        assertEquals(QueryTokenizer.TT_OTHER, tokenizer.nextToken());
        assertEquals("=", tokenizer.getToken());
        assertEquals(QueryTokenizer.TT_QUOTE, tokenizer.nextToken());
        assertEquals("'yyy'", tokenizer.getToken());
        assertEquals(QueryTokenizer.TT_EOF, tokenizer.nextToken());
    }

    /**
     * 
     */
    public void testNextToken_singleQuote_first() {
        QueryTokenizer tokenizer = new QueryTokenizer("'xxx'='xxx'");
        assertEquals(QueryTokenizer.TT_QUOTE, tokenizer.nextToken());
        assertEquals("'xxx'", tokenizer.getToken());
        assertEquals(QueryTokenizer.TT_OTHER, tokenizer.nextToken());
        assertEquals("=", tokenizer.getToken());
        assertEquals(QueryTokenizer.TT_QUOTE, tokenizer.nextToken());
        assertEquals("'xxx'", tokenizer.getToken());
        assertEquals(QueryTokenizer.TT_EOF, tokenizer.nextToken());
    }

    /**
     * 
     */
    public void testNextToken_word() {
        QueryTokenizer tokenizer = new QueryTokenizer("aaa");
        assertEquals(QueryTokenizer.TT_WORD, tokenizer.nextToken());
        assertEquals("aaa", tokenizer.getToken());
        assertEquals(QueryTokenizer.TT_EOF, tokenizer.nextToken());
    }

    /**
     * 
     */
    public void testNextToken_comma() {
        QueryTokenizer tokenizer = new QueryTokenizer("aaa, bbb");
        assertEquals(QueryTokenizer.TT_WORD, tokenizer.nextToken());
        assertEquals("aaa", tokenizer.getToken());
        assertEquals(QueryTokenizer.TT_OTHER, tokenizer.nextToken());
        assertEquals(", ", tokenizer.getToken());
        assertEquals(QueryTokenizer.TT_WORD, tokenizer.nextToken());
        assertEquals("bbb", tokenizer.getToken());
        assertEquals(QueryTokenizer.TT_EOF, tokenizer.nextToken());
    }

}
