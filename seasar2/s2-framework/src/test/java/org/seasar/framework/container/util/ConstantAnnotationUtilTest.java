package org.seasar.framework.container.util;

import java.util.Map;

import junit.framework.TestCase;

import org.seasar.framework.container.util.ConstantAnnotationUtil.Tokenizer;

public class ConstantAnnotationUtilTest extends TestCase {

    public void testConvertExpressionToMap() throws Exception {
        Map map = ConstantAnnotationUtil.convertExpressionToMap("required");
        assertEquals("required", map.get(null));

        map = ConstantAnnotationUtil.convertExpressionToMap("hoge=aaa");
        assertEquals("aaa", map.get("hoge"));

        map = ConstantAnnotationUtil
                .convertExpressionToMap("hoge=aaa, hoge2=bbb");
        assertEquals("aaa", map.get("hoge"));
        assertEquals("bbb", map.get("hoge2"));

        map = ConstantAnnotationUtil
                .convertExpressionToMap("hoge='aaa,=', hoge2=bbb");
        assertEquals("aaa,=", map.get("hoge"));
        assertEquals("bbb", map.get("hoge2"));
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

    public void testDot() throws Exception {
        Tokenizer tokenizer = new Tokenizer(".hoge");
        assertEquals("1", '.', tokenizer.nextToken());
        assertEquals("2", '.', tokenizer.getTokenType());
        assertEquals("3", Tokenizer.TT_WORD, tokenizer.nextToken());
        assertEquals("4", "hoge", tokenizer.getStringValue());
    }

    public void testOrdinary() throws Exception {
        Tokenizer tokenizer = new Tokenizer(" hoge= 123");
        assertEquals(Tokenizer.TT_WORD, tokenizer.nextToken());
        assertEquals("hoge", tokenizer.getStringValue());
        assertEquals(Tokenizer.TT_EQUAL, tokenizer.nextToken());
        assertEquals(Tokenizer.TT_WORD, tokenizer.nextToken());
        assertEquals("123", tokenizer.getStringValue());
        assertEquals(Tokenizer.TT_EOF, tokenizer.nextToken());
    }

    public void testQuote() throws Exception {
        Tokenizer tokenizer = new Tokenizer("hoge=',=abc', aaa=bbb");
        assertEquals(Tokenizer.TT_WORD, tokenizer.nextToken());
        assertEquals("hoge", tokenizer.getStringValue());
        assertEquals(Tokenizer.TT_EQUAL, tokenizer.nextToken());
        assertEquals(Tokenizer.TT_QUOTE, tokenizer.nextToken());
        assertEquals(",=abc", tokenizer.getStringValue());
        assertEquals(Tokenizer.TT_COMMA, tokenizer.nextToken());
        assertEquals(Tokenizer.TT_WORD, tokenizer.nextToken());
        assertEquals("aaa", tokenizer.getStringValue());
        assertEquals(Tokenizer.TT_EQUAL, tokenizer.nextToken());
        assertEquals(Tokenizer.TT_WORD, tokenizer.nextToken());
        assertEquals("bbb", tokenizer.getStringValue());
        assertEquals(Tokenizer.TT_EOF, tokenizer.nextToken());
    }
}