/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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
package org.seasar.framework.util;

import org.seasar.framework.unit.S2FrameworkTestCase;

/**
 * @author higa
 *
 */
public class TokenizerTest extends S2FrameworkTestCase {

    /**
     * @throws Exception
     */
    public void testEOF() throws Exception {
        Tokenizer tokenizer = new Tokenizer("");
        assertEquals(Tokenizer.TT_EOF, tokenizer.nextToken());
        assertEquals(Tokenizer.TT_EOF, tokenizer.nextToken());
    }

    /**
     * @throws Exception
     */
    public void testWhitespace() throws Exception {
        Tokenizer tokenizer = new Tokenizer("\t       \n");
        assertEquals(Tokenizer.TT_EOF, tokenizer.nextToken());
    }

    /**
     * @throws Exception
     */
    public void testHyphen() throws Exception {
        Tokenizer tokenizer = new Tokenizer("       - ");
        assertEquals('-', tokenizer.nextToken());
        assertEquals(Tokenizer.TT_EOF, tokenizer.nextToken());
    }

    /**
     * @throws Exception
     */
    public void pend_testDot() throws Exception {
        Tokenizer tokenizer = new Tokenizer("abc.hoge");
        assertEquals(Tokenizer.TT_WORD, tokenizer.nextToken());
        assertEquals("abc.hoge", tokenizer.getStringValue());
        assertEquals(Tokenizer.TT_EOF, tokenizer.nextToken());
    }
}