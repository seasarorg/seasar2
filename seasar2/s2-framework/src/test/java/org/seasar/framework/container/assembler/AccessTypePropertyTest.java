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
package org.seasar.framework.container.assembler;

import junit.framework.TestCase;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.impl.S2ContainerImpl;

/**
 * @author koichik
 */
public class AccessTypePropertyTest extends TestCase {

    /**
     * @throws Exception
     */
    public void test() throws Exception {
        S2Container container = new S2ContainerImpl();
        container.register(FooImpl.class, "foo");
        container.register(Bar.class, "bar");
        container.register(Baz.class, "baz");
        container.init();

        Foo foo = (Foo) container.getComponent("foo");
        Bar bar = (Bar) container.getComponent("bar");
        Baz baz = (Baz) container.getComponent("baz");
        assertEquals(foo, bar.getFoo());
        assertNull(baz.foo);
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
            this.foo = foo;
        }
    }

    /**
     * 
     */
    public static class Baz {
        private Foo foo;
    }
}
