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
package org.seasar.framework.container.assembler;

import junit.framework.TestCase;

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.PropertyDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.impl.PropertyDefImpl;
import org.seasar.framework.container.impl.S2ContainerImpl;

/**
 * @author koichik
 */
public class AccessTypeFieldTest extends TestCase {

    /**
     * @throws Exception
     */
    public void test() throws Exception {
        S2Container container = new S2ContainerImpl();
        container.register(FooImpl.class, "foo");
        container.register(Bar.class, "bar");
        ComponentDef cd1 = container.getComponentDef("bar");
        PropertyDef pd1 = new PropertyDefImpl("foo");
        pd1.setAccessTypeDef(AccessTypeDefFactory.FIELD);
        cd1.addPropertyDef(pd1);
        container.register(Baz.class, "baz");
        ComponentDef cd2 = container.getComponentDef("baz");
        PropertyDef pd2 = new PropertyDefImpl("foo");
        pd2.setAccessTypeDef(AccessTypeDefFactory.FIELD);
        cd2.addPropertyDef(pd2);
        container.init();

        Foo foo = (Foo) container.getComponent("foo");
        Bar bar = (Bar) container.getComponent("bar");
        Baz baz = (Baz) container.getComponent("baz");
        assertEquals(foo, bar.foo);
        assertEquals(foo, baz.getFoo());
    }

    /**
     *
     */
    public static interface Foo {
    }

    /**
     *
     */
    public static class FooImpl implements Foo {
    }

    /**
     *
     */
    public static class Bar {
        private Foo foo;
    }

    /**
     *
     */
    public static class Baz {
        private Foo foo;

        /**
         * @return
         */
        public Foo getFoo() {
            return foo;
        }

        /**
         * @param foo
         */
        public void setFoo(Foo foo) {
            throw new RuntimeException();
        }
    }
}
