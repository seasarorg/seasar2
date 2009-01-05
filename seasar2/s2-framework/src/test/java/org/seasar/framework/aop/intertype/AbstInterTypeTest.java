/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
package org.seasar.framework.aop.intertype;

import java.lang.reflect.Method;
import java.util.List;

import org.seasar.framework.unit.S2FrameworkTestCase;
import org.seasar.framework.util.ClassUtil;

/**
 * 
 */
public class AbstInterTypeTest extends S2FrameworkTestCase {

    /**
     * 
     */
    public AbstInterTypeTest() {
    }

    /**
     * @param name
     */
    public AbstInterTypeTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        include(ClassUtil.getShortClassName(AbstInterTypeTest.class)
                + ".dicon");
    }

    /**
     * @throws Exception
     */
    public void test() throws Exception {
        Runnable o = (Runnable) getComponent(List.class);
        o.run();
        assertEquals("1", "Hoge1", o.toString());

        Method m = o.getClass().getMethod("getFoo", null);
        assertSame("2", getComponent(Foo.class), m.invoke(o, null));
    }

    /**
     * 
     */
    public interface Foo {
    }

    /**
     * 
     */
    public static class FooImpl implements Foo {
    }

    /**
     * 
     */
    public static class TestInterType extends AbstractInterType {
        protected void introduce() {
            addInterface(Runnable.class);

            addField(String.class, "hoge");
            addMethod("setHoge", new Class[] { String.class }, "hoge = $1;");
            addMethod(String.class, "getHoge", "return hoge;");

            addMethod("public void run() {setHoge(\"Hoge\"); add(getHoge());}");
            addMethod(String.class, "toString",
                    "return getHoge() + Integer.toString(size());");

            addField(Foo.class, "foo");
            addMethod("setFoo", new Class[] { Foo.class }, "foo = $1;");
            addMethod(Foo.class, "getFoo", "return foo;");
        }
    }
}
