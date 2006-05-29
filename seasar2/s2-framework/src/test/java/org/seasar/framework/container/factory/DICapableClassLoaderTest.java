/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
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
package org.seasar.framework.container.factory;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.seasar.framework.container.S2Container;

public class DICapableClassLoaderTest extends TestCase {

    public DICapableClassLoaderTest() {
    }

    public DICapableClassLoaderTest(String name) {
        super(name);
    }

    public void testExceptClass() throws Exception {
        DICapableClassLoader loader = new DICapableClassLoader(getClass()
                .getClassLoader());

        S2Container container = S2ContainerFactory.create(getClass().getName()
                .replace('.', '/')
                + "1.dicon", loader);
        loader.setContainer(container);
        container.init();

        assertNotNull("1", container.getComponent("date"));
        assertNotNull("2", container.getComponent("text"));
        assertNotNull("3", container.getComponent("hoge"));
    }

    public void testAppliedClass() throws Exception {
        DICapableClassLoader loader = new DICapableClassLoader(getClass()
                .getClassLoader());
        loader.addClassPattern(getClass().getPackage().getName(),
                "DICapableClassLoaderTest.*");

        S2Container container = S2ContainerFactory.create(getClass().getName()
                .replace('.', '/')
                + "2.dicon", loader);
        loader.setContainer(container);
        container.init();

        Runnable foo = (Runnable) container.getComponent("foo");
        foo.run();
    }

    public static class Hoge {
    }

    public static class Foo extends Assert implements Runnable {
        public void run() {
            Bar bar = new Bar();
            assertSame("Foo:1", this, bar.getFoo());
            assertEquals("Foo:2", "bar", bar.getName());
        }
    }

    public static class Bar {
        public static final String INJECT_DEPENDENCY = "bar";

        private Foo foo;

        private String name;

        public Foo getFoo() {
            return foo;
        }

        public void setFoo(Foo foo) {
            this.foo = foo;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
