package org.seasar.framework.container.assembler;

import junit.framework.TestCase;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.impl.S2ContainerImpl;

public class AccessTypePropertyTest extends TestCase {

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

    public static interface Foo {
    }

    public static class FooImpl implements Foo {
    }

    public static class Bar {
        private Foo foo;

        public Foo getFoo() {
            return foo;
        }

        public void setFoo(Foo foo) {
            this.foo = foo;
        }
    }

    public static class Baz {
        private Foo foo;
    }
}
