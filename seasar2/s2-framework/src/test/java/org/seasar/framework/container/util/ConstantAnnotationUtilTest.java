package org.seasar.framework.container.util;

import java.lang.reflect.Field;
import java.util.Map;

import junit.framework.TestCase;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.container.util.ConstantAnnotationUtil.MyTokenizer;

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

        map = ConstantAnnotationUtil.convertExpressionToMap("hoge=9999.99");
        assertEquals("9999.99", map.get("hoge"));

        map = ConstantAnnotationUtil.convertExpressionToMap("hoge=-9999.99");
        assertEquals("-9999.99", map.get("hoge"));
    }

    public void testEOF() throws Exception {
        MyTokenizer tokenizer = new MyTokenizer("");
        assertEquals(MyTokenizer.TT_EOF, tokenizer.nextToken());
        assertEquals(MyTokenizer.TT_EOF, tokenizer.nextToken());
    }

    public void testWhitespace() throws Exception {
        MyTokenizer tokenizer = new MyTokenizer("\t       \n");
        assertEquals(MyTokenizer.TT_EOF, tokenizer.nextToken());
    }

    public void fixme_testHyphen() throws Exception {
        MyTokenizer tokenizer = new MyTokenizer("       - ");
        assertEquals('-', tokenizer.nextToken());
        assertEquals(MyTokenizer.TT_EOF, tokenizer.nextToken());
    }

    public void pend_testDot() throws Exception {
        MyTokenizer tokenizer = new MyTokenizer(".hoge");
        assertEquals('.', tokenizer.nextToken());
        assertEquals(MyTokenizer.TT_WORD, tokenizer.nextToken());
        assertEquals("hoge", tokenizer.getStringValue());
    }

    public void testOrdinary() throws Exception {
        MyTokenizer tokenizer = new MyTokenizer(" hoge= 123");
        assertEquals(MyTokenizer.TT_WORD, tokenizer.nextToken());
        assertEquals("hoge", tokenizer.getStringValue());
        assertEquals(MyTokenizer.TT_EQUAL, tokenizer.nextToken());
        assertEquals(MyTokenizer.TT_WORD, tokenizer.nextToken());
        assertEquals("123", tokenizer.getStringValue());
        assertEquals(MyTokenizer.TT_EOF, tokenizer.nextToken());
    }

    public void testOrdinaryDouble() throws Exception {
        MyTokenizer tokenizer = new MyTokenizer(" hoge= 9999.99");
        assertEquals(MyTokenizer.TT_WORD, tokenizer.nextToken());
        assertEquals("hoge", tokenizer.getStringValue());
        assertEquals(MyTokenizer.TT_EQUAL, tokenizer.nextToken());
        assertEquals(MyTokenizer.TT_WORD, tokenizer.nextToken());
        assertEquals("9999.99", tokenizer.getStringValue());
        assertEquals(MyTokenizer.TT_EOF, tokenizer.nextToken());
    }

    public void testQuote() throws Exception {
        MyTokenizer tokenizer = new MyTokenizer("hoge=',=abc', aaa=bbb");
        assertEquals(MyTokenizer.TT_WORD, tokenizer.nextToken());
        assertEquals("hoge", tokenizer.getStringValue());
        assertEquals(MyTokenizer.TT_EQUAL, tokenizer.nextToken());
        assertEquals(MyTokenizer.TT_QUOTE, tokenizer.nextToken());
        assertEquals(",=abc", tokenizer.getStringValue());
        assertEquals(MyTokenizer.TT_COMMA, tokenizer.nextToken());
        assertEquals(MyTokenizer.TT_WORD, tokenizer.nextToken());
        assertEquals("aaa", tokenizer.getStringValue());
        assertEquals(MyTokenizer.TT_EQUAL, tokenizer.nextToken());
        assertEquals(MyTokenizer.TT_WORD, tokenizer.nextToken());
        assertEquals("bbb", tokenizer.getStringValue());
        assertEquals(MyTokenizer.TT_EOF, tokenizer.nextToken());
    }

    public void testIsConstantAnnotation() throws Exception {
        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(Hoge.class);
        Field f1 = beanDesc.getField("A");
        assertFalse(ConstantAnnotationUtil.isConstantAnnotation(f1));

        Field f2 = beanDesc.getField("B");
        assertFalse(ConstantAnnotationUtil.isConstantAnnotation(f2));

        Field f3 = beanDesc.getField("C");
        assertFalse(ConstantAnnotationUtil.isConstantAnnotation(f3));

        Field f4 = beanDesc.getField("D");
        assertFalse(ConstantAnnotationUtil.isConstantAnnotation(f4));

        Field f5 = beanDesc.getField("F");
        assertTrue(ConstantAnnotationUtil.isConstantAnnotation(f5));
    }

    public static class Hoge {
        public static String A = "A";

        protected static final String B = "B";

        public String C = "C";

        public static final int D = 1;

        public static final String F = "F";
    }

}