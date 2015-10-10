/*
 * Copyright 2004-2015 the Seasar Foundation and the Others.
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

import java.util.Map;

import junit.framework.TestCase;

/**
 * @author koichik
 */
public class SimpleExpressionTest extends TestCase {

    /**
     * @throws Exception
     */
    public void test1() throws Exception {
        Foo foo = new Foo();
        foo.bar = new Bar();
        foo.bar.baz = new Baz();
        foo.bar.baz.name = "hoge";
        SimpleExpression exp = SimpleExpressionParser.parse("a : bar.baz.name");
        Map map = exp.evaluate(foo);
        assertEquals(1, map.size());
        assertEquals("hoge", map.get("a"));
    }

    /**
     * @throws Exception
     */
    public void test2() throws Exception {
        Foo foo = new Foo();
        foo.bar = new Bar();
        foo.bar.baz = new Baz();
        SimpleExpression exp = SimpleExpressionParser
                .parse("result : bar.baz.name, dummy : null");
        Map map = exp.evaluate(foo);
        assertEquals(2, map.size());
        assertNull(map.get("result"));
        assertNull(map.get("dummy"));
    }

    /**
     * @throws Exception
     */
    public void test3() throws Exception {
        Foo foo = new Foo();
        foo.bar = new Bar();
        SimpleExpression exp = SimpleExpressionParser
                .parse("result : bar.baz.name");
        Map map = exp.evaluate(foo);
        assertEquals(1, map.size());
        assertNull(map.get("result"));
    }

    /**
     * @throws Exception
     */
    public void test4() throws Exception {
        Foo foo = new Foo();
        SimpleExpression exp = SimpleExpressionParser
                .parse("result : bar.baz.name");
        Map map = exp.evaluate(foo);
        assertEquals(1, map.size());
        assertNull(map.get("result"));
    }

    /**
     */
    public static class Foo {
        /** */
        public Bar bar;
    }

    /**
     */
    public static class Bar {
        /** */
        public Baz baz;
    }

    /**
     */
    public static class Baz {
        /** */
        public String name;
    }

}
