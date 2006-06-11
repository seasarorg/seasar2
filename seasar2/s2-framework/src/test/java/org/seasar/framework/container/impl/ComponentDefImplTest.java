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
package org.seasar.framework.container.impl;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashMap;

import junit.framework.TestCase;

import org.seasar.framework.aop.interceptors.TraceInterceptor;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.InitMethodDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.ognl.OgnlExpression;

/**
 * @author higa
 * 
 */
public class ComponentDefImplTest extends TestCase {

    public void testGetComponentForType3() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(A.class);
        container.register(cd);
        container.register(B.class);
        A a = (A) container.getComponent(A.class);
        assertEquals("1", "B", a.getHogeName());
        assertSame("2", a, container.getComponent(A.class));
    }

    public void testGetComponentForType2() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(A2.class);
        container.register(cd);
        container.register(B.class);
        A2 a2 = (A2) container.getComponent(A2.class);
        assertEquals("1", "B", a2.getHogeName());
    }

    public void testGetComponentForArgDef() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(BigDecimal.class, "num");
        cd.addArgDef(new ArgDefImpl("123"));
        container.register(cd);
        assertEquals("1", new BigDecimal(123), container.getComponent("num"));
    }

    public void testGetComponentForProperyDef() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(A2.class);
        cd.addPropertyDef(new PropertyDefImpl("hoge", new B()));
        container.register(cd);
        A2 a2 = (A2) container.getComponent(A2.class);
        assertEquals("1", "B", a2.getHogeName());
    }

    public void testGetComponentForMethodDef() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(HashMap.class, "myMap");
        InitMethodDef md = new InitMethodDefImpl("put");
        md.addArgDef(new ArgDefImpl("aaa"));
        md.addArgDef(new ArgDefImpl("hoge"));
        cd.addInitMethodDef(md);
        container.register(cd);
        HashMap myMap = (HashMap) container.getComponent("myMap");
        assertEquals("1", "hoge", myMap.get("aaa"));
    }

    public void testGetComponentForAspectDef() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(A.class);
        cd.addAspectDef(new AspectDefImpl(new TraceInterceptor()));
        container.register(cd);
        container.register(B.class);
        A a = (A) container.getComponent(A.class);
        assertEquals("1", "B", a.getHogeName());
    }

    public void testGetComponentForExpression() throws Exception {
        S2Container container = new S2ContainerImpl();
        container.register(Object.class, "obj");
        ComponentDefImpl cd = new ComponentDefImpl(null, "hash");
        cd.setExpression(new OgnlExpression("obj.hashCode()"));
        container.register(cd);
        assertNotNull("1", container.getComponent("hash"));
    }

    public void testCyclicReference() throws Exception {
        S2Container container = new S2ContainerImpl();
        container.register(A2.class);
        container.register(C.class);
        A2 a2 = (A2) container.getComponent(A2.class);
        C c = (C) container.getComponent(C.class);
        assertEquals("1", "C", a2.getHogeName());
        assertEquals("1", "C", c.getHogeName());
    }

    public void testInit() throws Exception {
        ComponentDef cd = new ComponentDefImpl(D.class);
        cd.addInitMethodDef(new InitMethodDefImpl("init"));
        cd.init();
        D d = (D) cd.getComponent();
        assertEquals("1", true, d.isInited());
    }

    public void testDestroy() throws Exception {
        ComponentDef cd = new ComponentDefImpl(D.class);
        cd.addDestroyMethodDef(new DestroyMethodDefImpl("destroy"));
        D d = (D) cd.getComponent();
        cd.destroy();
        assertEquals("1", true, d.isDestroyed());
    }

    public void testGetConcreteClass() throws Exception {
        final ClassLoader loader1 = Thread.currentThread()
                .getContextClassLoader();
        ClassLoader loader2 = new ClassLoader(loader1) {
            public Class loadClass(String name) throws ClassNotFoundException {
                if (!name.equals(Foo.class.getName())) {
                    return super.loadClass(name);
                }
                try {
                    InputStream is = loader1.getResourceAsStream(name.replace(
                            '.', '/')
                            + ".class");
                    byte[] bytes = new byte[is.available()];
                    is.read(bytes, 0, bytes.length);
                    return defineClass(name, bytes, 0, bytes.length);
                } catch (IOException e) {
                    throw new ClassNotFoundException(name, e);
                }
            }

        };
        Thread.currentThread().setContextClassLoader(loader2);
        S2Container container;
        try {
            container = new S2ContainerImpl();
            Class fooClass = loader2.loadClass(Foo.class.getName());
            ComponentDef cd = new ComponentDefImpl(fooClass, "foo");
            cd.addAspectDef(new AspectDefImpl(new TraceInterceptor()));
            container.register(cd);
        } finally {
            Thread.currentThread().setContextClassLoader(loader1);
        }

        ComponentDefImpl cd = (ComponentDefImpl) container
                .getComponentDef("foo");
        Class concreteClass = cd.getConcreteClass();
        assertTrue("1", concreteClass.getName().startsWith(
                Foo.class.getName() + "$$"));
        assertEquals("2", loader2, concreteClass.getClassLoader());
        Class superClass = concreteClass.getInterfaces()[0];
        assertEquals("3", Foo.class.getName(), superClass.getName());
        assertEquals("4", loader2, superClass.getClassLoader());
    }

    public interface Foo {
        public String getHogeName();
    }

    public static class FooImpl implements Foo {

        public String getHogeName() {
            return "hoge";
        }
    }

    public static class A {

        private Hoge hoge_;

        public A(Hoge hoge) {
            hoge_ = hoge;
        }

        public String getHogeName() {
            return hoge_.getName();
        }
    }

    public static class A2 implements Foo {

        private Hoge hoge_;

        public void setHoge(Hoge hoge) {
            hoge_ = hoge;
        }

        public String getHogeName() {
            return hoge_.getName();
        }
    }

    public interface Hoge {

        public String getName();
    }

    public static class B implements Hoge {

        public String getName() {
            return "B";
        }
    }

    public static class C implements Hoge {

        private Foo foo_;

        public void setFoo(Foo foo) {
            foo_ = foo;
        }

        public String getName() {
            return "C";
        }

        public String getHogeName() {
            return foo_.getHogeName();
        }
    }

    public static class D {

        private boolean inited_ = false;

        private boolean destroyed_ = false;

        public boolean isInited() {
            return inited_;
        }

        public boolean isDestroyed() {
            return destroyed_;
        }

        public void init() {
            inited_ = true;
        }

        public void destroy() {
            destroyed_ = true;
        }
    }
}
