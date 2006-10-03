package org.seasar.framework.util;

import org.seasar.framework.unit.S2FrameworkTestCase;

public class TokenizerTest extends S2FrameworkTestCase {

    public void setUp() throws Exception {
        Tokenizer.ordinaryChar('=');
        Tokenizer.ordinaryChar(',');
    }

    public void tearDown() throws Exception {
        Tokenizer.wordChar('=');
        Tokenizer.wordChar(',');
    }

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

    public void testOrdinary() throws Exception {
        Tokenizer tokenizer = new Tokenizer(" hoge= 123.45");
        assertEquals(Tokenizer.TT_WORD, tokenizer.nextToken());
        assertEquals("hoge", tokenizer.getStringValue());
        assertEquals('=', tokenizer.nextToken());
        assertEquals(Tokenizer.TT_WORD, tokenizer.nextToken());
        assertEquals("123.45", tokenizer.getStringValue());
        assertEquals(Tokenizer.TT_EOF, tokenizer.nextToken());
    }

    public void testQuote() throws Exception {
        Tokenizer tokenizer = new Tokenizer("hoge=',=abc', aaa=bbb");
        assertEquals(Tokenizer.TT_WORD, tokenizer.nextToken());
        assertEquals("hoge", tokenizer.getStringValue());
        assertEquals('=', tokenizer.nextToken());
        assertEquals(Tokenizer.TT_QUOTE, tokenizer.nextToken());
        assertEquals(",=abc", tokenizer.getStringValue());
        assertEquals(',', tokenizer.nextToken());
        assertEquals(Tokenizer.TT_WORD, tokenizer.nextToken());
        assertEquals("aaa", tokenizer.getStringValue());
        assertEquals('=', tokenizer.nextToken());
        assertEquals(Tokenizer.TT_WORD, tokenizer.nextToken());
        assertEquals("bbb", tokenizer.getStringValue());
        assertEquals(Tokenizer.TT_EOF, tokenizer.nextToken());
    }
}