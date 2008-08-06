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
import static org.seasar.extension.jdbc.gen.sql.DumpFileTokenizer.TokenType.*;

/**
 * @author taedium
 * 
 */
public class DumpFileTokenizerTest {

    private DumpFileTokenizer tokenizer = new DumpFileTokenizer(',');

    @Test
    public void testValue() throws Exception {
        String s = "aaa, bbb, \r\n";
        tokenizer.addChars(s.toCharArray(), s.length());
        assertEquals(VALUE, tokenizer.nextToken());
        assertEquals("aaa", tokenizer.getToken());
        assertEquals(DELIMETER, tokenizer.nextToken());
        assertEquals(",", tokenizer.getToken());
        assertEquals(VALUE, tokenizer.nextToken());
        assertEquals(" bbb", tokenizer.getToken());
        assertEquals(DELIMETER, tokenizer.nextToken());
        assertEquals(",", tokenizer.getToken());
        assertEquals(VALUE, tokenizer.nextToken());
        assertEquals(" ", tokenizer.getToken());
        assertEquals(END_OF_LINE, tokenizer.nextToken());
        assertEquals("\r\n", tokenizer.getToken());
        assertEquals(END_OF_BUFFER, tokenizer.nextToken());
        assertEquals("", tokenizer.getToken());
    }

    @Test
    public void testValue_escaped() throws Exception {
        String s = "\"a\"\"a,a\"\r\n";
        tokenizer.addChars(s.toCharArray(), s.length());
        assertEquals(VALUE, tokenizer.nextToken());
        assertEquals("\"a\"\"a,a\"", tokenizer.getToken());
        assertEquals(END_OF_LINE, tokenizer.nextToken());
        assertEquals("\r\n", tokenizer.getToken());
    }

    @Test
    public void testEndOfBuffer() throws Exception {
        String s = "aa";
        tokenizer.addChars(s.toCharArray(), s.length());
        assertEquals(END_OF_BUFFER, tokenizer.nextToken());
        assertEquals("aa", tokenizer.getToken());

        s = "a,bbb\r\n";
        tokenizer.addChars(s.toCharArray(), s.length());
        assertEquals(VALUE, tokenizer.nextToken());
        assertEquals("aaa", tokenizer.getToken());
        assertEquals(DELIMETER, tokenizer.nextToken());
        assertEquals(",", tokenizer.getToken());
        assertEquals(VALUE, tokenizer.nextToken());
        assertEquals("bbb", tokenizer.getToken());
    }

    @Test
    public void testNull() throws Exception {
        String s = "aaa,,bbb\r\n";
        tokenizer.addChars(s.toCharArray(), s.length());
        assertEquals(VALUE, tokenizer.nextToken());
        assertEquals("aaa", tokenizer.getToken());
        assertEquals(DELIMETER, tokenizer.nextToken());
        assertEquals(",", tokenizer.getToken());
        assertEquals(NULL, tokenizer.nextToken());
        assertEquals("", tokenizer.getToken());
        assertEquals(DELIMETER, tokenizer.nextToken());
        assertEquals(",", tokenizer.getToken());
        assertEquals(VALUE, tokenizer.nextToken());
        assertEquals("bbb", tokenizer.getToken());
        assertEquals(END_OF_LINE, tokenizer.nextToken());
        assertEquals("\r\n", tokenizer.getToken());
        assertEquals(END_OF_BUFFER, tokenizer.nextToken());
        assertEquals("", tokenizer.getToken());
    }

    @Test
    public void testNull_afterStartOfLine() throws Exception {
        String s = ",bbb\r\n";
        tokenizer.addChars(s.toCharArray(), s.length());
        assertEquals(NULL, tokenizer.nextToken());
        assertEquals("", tokenizer.getToken());
        assertEquals(DELIMETER, tokenizer.nextToken());
        assertEquals(",", tokenizer.getToken());
        assertEquals(VALUE, tokenizer.nextToken());
        assertEquals("bbb", tokenizer.getToken());
        assertEquals(END_OF_LINE, tokenizer.nextToken());
        assertEquals("\r\n", tokenizer.getToken());
        assertEquals(END_OF_BUFFER, tokenizer.nextToken());
        assertEquals("", tokenizer.getToken());
    }

    @Test
    public void testNull_BeforeEndOfLine() throws Exception {
        String s = "aaa,\r\n";
        tokenizer.addChars(s.toCharArray(), s.length());
        assertEquals(VALUE, tokenizer.nextToken());
        assertEquals("aaa", tokenizer.getToken());
        assertEquals(DELIMETER, tokenizer.nextToken());
        assertEquals(",", tokenizer.getToken());
        assertEquals(NULL, tokenizer.nextToken());
        assertEquals("", tokenizer.getToken());
        assertEquals(END_OF_LINE, tokenizer.nextToken());
        assertEquals("\r\n", tokenizer.getToken());
        assertEquals(END_OF_BUFFER, tokenizer.nextToken());
        assertEquals("", tokenizer.getToken());
    }

    @Test
    public void testEndOfLine() throws Exception {
        String s = "aaa\r\nbbb\r";
        tokenizer.addChars(s.toCharArray(), s.length());
        assertEquals(VALUE, tokenizer.nextToken());
        assertEquals("aaa", tokenizer.getToken());
        assertEquals(END_OF_LINE, tokenizer.nextToken());
        assertEquals("\r\n", tokenizer.getToken());
        assertEquals(END_OF_BUFFER, tokenizer.nextToken());
        assertEquals("bbb\r", tokenizer.getToken());

        s = "\nccc\r\n";
        tokenizer.addChars(s.toCharArray(), s.length());
        assertEquals(VALUE, tokenizer.nextToken());
        assertEquals("bbb", tokenizer.getToken());
        assertEquals(END_OF_LINE, tokenizer.nextToken());
        assertEquals("\r\n", tokenizer.getToken());
        assertEquals(VALUE, tokenizer.nextToken());
        assertEquals("ccc", tokenizer.getToken());
        assertEquals(END_OF_LINE, tokenizer.nextToken());
        assertEquals("\r\n", tokenizer.getToken());
    }

    @Test
    public void testEndOfLine_escaped() throws Exception {
        String s = "\"aaa\r\nbbb\"\r\n";
        tokenizer.addChars(s.toCharArray(), s.length());
        assertEquals(VALUE, tokenizer.nextToken());
        assertEquals("\"aaa\r\nbbb\"", tokenizer.getToken());
        assertEquals(END_OF_LINE, tokenizer.nextToken());
        assertEquals("\r\n", tokenizer.getToken());
    }
}
