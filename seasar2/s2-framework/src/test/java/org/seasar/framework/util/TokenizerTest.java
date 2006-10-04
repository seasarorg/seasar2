package org.seasar.framework.util;

import org.seasar.framework.unit.S2FrameworkTestCase;

public class TokenizerTest extends S2FrameworkTestCase {

    public void testEOF() throws Exception {
        Tokenizer tokenizer = new Tokenizer("");
        assertEquals(Tokenizer.TT_EOF, tokenizer.nextToken());
        assertEquals(Tokenizer.TT_EOF, tokenizer.nextToken());
    }

    public void testWhitespace() throws Exception {
        Tokenizer tokenizer = new Tokenizer("\t       \n");
        assertEquals(Tokenizer.TT_EOF, tokenizer.nextToken());
    }

    public void testHyphen() throws Exception {
        Tokenizer tokenizer = new Tokenizer("       - ");
        assertEquals('-', tokenizer.nextToken());
        assertEquals(Tokenizer.TT_EOF, tokenizer.nextToken());
    }

    public void pend_testDot() throws Exception {
        Tokenizer tokenizer = new Tokenizer("abc.hoge");
        assertEquals(Tokenizer.TT_WORD, tokenizer.nextToken());
        assertEquals("abc.hoge", tokenizer.getStringValue());
        assertEquals(Tokenizer.TT_EOF, tokenizer.nextToken());
    }
}