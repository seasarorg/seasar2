package org.seasar.framework.aop.interceptors;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.seasar.framework.aop.interceptors.PrototypeDelegateInterceptor;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.deployer.InstanceDefFactory;
import org.seasar.framework.container.impl.ComponentDefImpl;
import org.seasar.framework.container.impl.S2ContainerImpl;

/**
 * @author koichik
 */
public class PrototypeDelegateInterceptorTest extends TestCase {
    private static List list = new ArrayList();
    private S2Container container;

    public PrototypeDelegateInterceptorTest() {
    }

    public PrototypeDelegateInterceptorTest(String name) {
        super(name);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(PrototypeDelegateInterceptorTest.class);
    }

    public void setUp() {
        list.clear();
        S2ContainerImpl containerImpl = new S2ContainerImpl();

        ComponentDefImpl singleton = new ComponentDefImpl(Target.class, "singleton");
        containerImpl.register(singleton);

        ComponentDefImpl prototype = new ComponentDefImpl(Target.class, "prototype");
        prototype.setInstanceDef(InstanceDefFactory.PROTOTYPE);
        containerImpl.register(prototype);

        containerImpl.init();
        container = containerImpl;
    }

    public void testSingleton() throws Exception {
        PrototypeDelegateInterceptor pdi = new PrototypeDelegateInterceptor(container);
        pdi.setTargetName("singleton");
        Hello proxy = (Hello) pdi.createProxy(Hello.class);
        proxy.foo();
        proxy.foo();
        assertEquals("1", 2, list.size());
        assertSame("2", list.get(0), list.get(1));
    }

    public void testPrototype() throws Exception {
        PrototypeDelegateInterceptor pdi = new PrototypeDelegateInterceptor(container);
        pdi.setTargetName("prototype");
        Hello proxy = (Hello) pdi.createProxy(Hello.class);
        proxy.foo();
        proxy.foo();
        assertEquals("1", 2, list.size());
        assertNotSame("2", list.get(0), list.get(1));
    }

    public void testMethodName() throws Exception {
        PrototypeDelegateInterceptor pdi = new PrototypeDelegateInterceptor(container);
        pdi.setTargetName("singleton");
        pdi.addMethodNameMap("bar", "foo");
        Hello proxy = (Hello) pdi.createProxy(Hello.class);
        proxy.bar();
        assertEquals("1", 1, list.size());
    }

    public static interface Hello {
        public void foo();

        public void bar();
    }

    public static class Target {
        public void foo() {
            list.add(this);
        }
    }
}