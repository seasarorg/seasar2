package org.seasar.framework.container.assembler;

import junit.framework.TestCase;

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.PropertyDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.impl.PropertyDefImpl;
import org.seasar.framework.container.impl.S2ContainerImpl;

public class AccessTypeFieldTest extends TestCase {

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

    public static interface Foo {
    }

    public static class FooImpl implements Foo {
    }

    public static class Bar {
        private Foo foo;
    }

    public static class Baz {
        private Foo foo;

        public Foo getFoo() {
            return foo;
        }

        public void setFoo(Foo foo) {
            throw new RuntimeException();
        }
    }
}
