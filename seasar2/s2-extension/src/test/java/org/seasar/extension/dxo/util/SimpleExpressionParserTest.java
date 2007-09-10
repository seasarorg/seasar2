/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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
package org.seasar.extension.dxo.util;

import java.util.Iterator;

import org.seasar.extension.dxo.util.SimpleExpression.ConversionRule;

import junit.framework.TestCase;

/**
 * @author koichik
 */
public class SimpleExpressionParserTest extends TestCase {

    /**
     * @throws Exception
     */
    public void test1() throws Exception {
        SimpleExpression exp = SimpleExpressionParser.parse("a:b");
        assertNotNull(exp);
        assertEquals(1, exp.conversionRuleList.size());
        Iterator pairs = exp.conversionRuleList.iterator();
        ConversionRule pair = (ConversionRule) pairs.next();
        assertEquals("a", pair.destProperty);
        assertEquals(1, pair.sourcePropertyList.size());
        Iterator values = pair.sourcePropertyList.iterator();
        assertEquals("b", values.next());
    }

    /**
     * @throws Exception
     */
    public void test2() throws Exception {
        SimpleExpression exp = SimpleExpressionParser.parse("a:b.c.d");
        assertNotNull(exp);
        assertEquals(1, exp.conversionRuleList.size());
        Iterator pairs = exp.conversionRuleList.iterator();
        ConversionRule pair = (ConversionRule) pairs.next();
        assertEquals("a", pair.destProperty);
        assertEquals(3, pair.sourcePropertyList.size());
        Iterator values = pair.sourcePropertyList.iterator();
        assertEquals("b", values.next());
        assertEquals("c", values.next());
        assertEquals("d", values.next());
    }

    /**
     * @throws Exception
     */
    public void test3() throws Exception {
        SimpleExpression exp = SimpleExpressionParser.parse("x:a,y:b,z:c");
        assertNotNull(exp);
        assertEquals(3, exp.conversionRuleList.size());
        Iterator pairs = exp.conversionRuleList.iterator();
        ConversionRule pair = (ConversionRule) pairs.next();
        assertEquals("x", pair.destProperty);
        assertEquals(1, pair.sourcePropertyList.size());
        Iterator values = pair.sourcePropertyList.iterator();
        assertEquals("a", values.next());

        pair = (ConversionRule) pairs.next();
        assertEquals("y", pair.destProperty);
        assertEquals(1, pair.sourcePropertyList.size());
        values = pair.sourcePropertyList.iterator();
        assertEquals("b", values.next());

        pair = (ConversionRule) pairs.next();
        assertEquals("z", pair.destProperty);
        assertEquals(1, pair.sourcePropertyList.size());
        values = pair.sourcePropertyList.iterator();
        assertEquals("c", values.next());
    }

    /**
     * @throws Exception
     */
    public void test4() throws Exception {
        SimpleExpression exp = SimpleExpressionParser
                .parse("xxx:aaa,yyy:bbb.ccc,zzz:ccc.ddd.eee");
        assertNotNull(exp);
        assertEquals(3, exp.conversionRuleList.size());
        Iterator pairs = exp.conversionRuleList.iterator();
        ConversionRule pair = (ConversionRule) pairs.next();
        assertEquals("xxx", pair.destProperty);
        assertEquals(1, pair.sourcePropertyList.size());
        Iterator values = pair.sourcePropertyList.iterator();
        assertEquals("aaa", values.next());

        pair = (ConversionRule) pairs.next();
        assertEquals("yyy", pair.destProperty);
        assertEquals(2, pair.sourcePropertyList.size());
        values = pair.sourcePropertyList.iterator();
        assertEquals("bbb", values.next());
        assertEquals("ccc", values.next());

        pair = (ConversionRule) pairs.next();
        assertEquals("zzz", pair.destProperty);
        assertEquals(3, pair.sourcePropertyList.size());
        values = pair.sourcePropertyList.iterator();
        assertEquals("ccc", values.next());
        assertEquals("ddd", values.next());
        assertEquals("eee", values.next());
    }

    /**
     * @throws Exception
     */
    public void test5() throws Exception {
        SimpleExpression exp = SimpleExpressionParser
                .parse(" xxx : aaa , yyy : bbb . ccc , zzz : ccc . ddd . eee ");
        assertNotNull(exp);
        assertEquals(3, exp.conversionRuleList.size());
        Iterator pairs = exp.conversionRuleList.iterator();
        ConversionRule pair = (ConversionRule) pairs.next();
        assertEquals("xxx", pair.destProperty);
        assertEquals(1, pair.sourcePropertyList.size());
        Iterator values = pair.sourcePropertyList.iterator();
        assertEquals("aaa", values.next());

        pair = (ConversionRule) pairs.next();
        assertEquals("yyy", pair.destProperty);
        assertEquals(2, pair.sourcePropertyList.size());
        values = pair.sourcePropertyList.iterator();
        assertEquals("bbb", values.next());
        assertEquals("ccc", values.next());

        pair = (ConversionRule) pairs.next();
        assertEquals("zzz", pair.destProperty);
        assertEquals(3, pair.sourcePropertyList.size());
        values = pair.sourcePropertyList.iterator();
        assertEquals("ccc", values.next());
        assertEquals("ddd", values.next());
        assertEquals("eee", values.next());
    }

    /**
     * @throws Exception
     */
    public void test6() throws Exception {
        assertNull(SimpleExpressionParser.parse("a::b"));
        assertNull(SimpleExpressionParser.parse("a:b;"));
        assertNull(SimpleExpressionParser.parse("a:b,"));
        assertNull(SimpleExpressionParser.parse("a:b,,b:c"));
        assertNull(SimpleExpressionParser.parse("a:b,b:c."));
        assertNull(SimpleExpressionParser.parse("a:b,b:c."));
    }

}
