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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;

import junit.framework.TestCase;

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.ComponentDeployer;
import org.seasar.framework.container.PropertyAssembler;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.assembler.AssemblerFactory;
import org.seasar.framework.container.assembler.ManualOnlyPropertyAssembler;
import org.seasar.framework.container.deployer.ComponentDeployerFactory;
import org.seasar.framework.container.deployer.SingletonComponentDeployer;
import org.seasar.framework.container.impl.S2ContainerBehavior;
import org.seasar.framework.container.impl.S2ContainerImpl;
import org.seasar.framework.exception.IORuntimeException;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.ResourceUtil;
import org.xml.sax.SAXException;

/**
 * @author koichik
 */
public class S2ContainerFactoryTest extends TestCase {

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
        S2ContainerFactory.configurationContainer = null;
        S2ContainerFactory
                .setProvider(new S2ContainerFactory.DefaultProvider());
        S2ContainerFactory.setDefaultBuilder(new XmlS2ContainerBuilder());
        S2ContainerBehavior
                .setProvider(new S2ContainerBehavior.DefaultProvider());
        ComponentDeployerFactory
                .setProvider(new ComponentDeployerFactory.DefaultProvider());
        AssemblerFactory.setProvider(new AssemblerFactory.DefaultProvider());
    }

    public void testCircularInclude() throws Exception {
        try {
            S2ContainerFactory.create(getClass().getName().replace('.', '/')
                    + ".CircularA.dicon");
            fail("1");
        } catch (Throwable e) {
            while (e != null) {
                if (e instanceof CircularIncludeRuntimeException) {
                    return;
                }
                e = (e instanceof SAXException) ? ((SAXException) e)
                        .getException() : e.getCause();
            }
            fail("2");
        }
    }

    public void testCustomizeContainerFactory() throws Exception {
        configure("ContainerFactory.dicon");
        S2Container container = S2ContainerFactory.create("notExists.dicon");
        assertNotNull("1", container);
    }

    public void testCustomizePathResolver() throws Exception {
        configure("PathResolver.dicon");
        S2Container container = S2ContainerFactory.create("notExists.dicon");
        assertNotNull("1", container);
    }

    public void testCustomizeContainerBuilder() throws Exception {
        configure("ContainerBuilder.dicon");
        S2Container container = S2ContainerFactory.create(getClass().getName()
                .replace('.', '/')
                + ".app.properties");
        container.init();
        assertNotNull("1", container.getComponent("list"));
        assertNotNull("2", container.getComponent("map"));
    }

    public void testCustomizeResourceResolver() throws Exception {
        configure("ResourceResolver.dicon");
        S2Container container = S2ContainerFactory.create("hoge.dicon");
        container.init();
        assertNotNull("1", container.getComponent("list"));
        assertNotNull("2", container.getComponent("map"));
    }

    public void testCustomizeContainerBehavior() throws Exception {
        configure("ContainerBehavior.dicon");
        S2Container container = S2ContainerFactory.create(getClass().getName()
                .replace('.', '/')
                + ".dicon");
        container.init();
        assertNull("1", container.getComponent("notFound"));
    }

    public void testCustomizeComponentDeployerFactory() throws Exception {
        configure("ComponetDeployerFactory.dicon");
        S2Container container = S2ContainerFactory.create(getClass().getName()
                .replace('.', '/')
                + ".foo.dicon");
        container.init();
        Bar bar = (Bar) container.getComponent("bar");
        assertSame("1", bar, container.getComponent("bar"));
    }

    public void testCustomizeAssemblerFactory() throws Exception {
        configure("AssemblerFactory.dicon");
        S2Container container = S2ContainerFactory.create(getClass().getName()
                .replace('.', '/')
                + ".foo.dicon");
        container.init();
        Baz baz = (Baz) container.getComponent("baz");
        assertNull("1", baz.getFoo());
    }

    public void testCustomizeClassLoader() throws Exception {
        configure("ClassLoader.dicon");
        S2Container container = S2ContainerFactory.create(getClass().getName()
                .replace('.', '/')
                + ".foo.dicon");
        container.init();
        Object baz = container.getComponent("baz");
        ClassLoader cl = baz.getClass().getClassLoader();
        assertTrue("1", cl instanceof ChildFirstClassLoader);
        assertEquals("2", Thread.currentThread().getContextClassLoader(), cl
                .getParent());
    }

    public void testDestroy() throws Exception {
        configure("ContainerFactory.dicon");
        assertNotNull("1", S2ContainerFactory.configurationContainer);
        S2ContainerFactory.destroy();
        assertNull("2", S2ContainerFactory.configurationContainer);
    }

    public void configure(String name) throws Exception {
        String path = getClass().getName().replace('.', '/') + "." + name;
        S2ContainerFactory.configure(path);
    }

    public static class EmptyContainerFactory extends
            S2ContainerFactory.DefaultProvider {
        public S2Container create(String path) {
            return new S2ContainerImpl();
        }
    }

    public static class FixedPathResolver extends SimplePathResolver {
        public String resolvePath(String context, String path) {
            return S2ContainerFactoryTest.class.getName().replace('.', '/')
                    + ".PathResolver.dicon";
        }
    }

    public static class PropertyBuilder extends AbstractS2ContainerBuilder {
        public S2Container build(String path) {
            try {
                S2Container container = new S2ContainerImpl();
                Properties props = new Properties();
                props.load(ResourceUtil.getResourceAsStream(path));
                for (Iterator it = props.keySet().iterator(); it.hasNext();) {
                    String name = (String) it.next();
                    container.register(ClassUtil.forName(props
                            .getProperty(name)), name);
                }
                return container;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public S2Container include(S2Container parent, String path) {
            throw new UnsupportedOperationException();
        }
    }

    public static class StringResourceResolver implements ResourceResolver {
        protected String definition;

        public String getDefinition() {
            return this.definition;
        }

        public void setDefinition(String definition) {
            this.definition = definition;
        }

        public InputStream getInputStream(String path) {
            return new ByteArrayInputStream(definition.getBytes());
        }
    }

    public static class UnthrowExceptionBehavior extends
            S2ContainerBehavior.DefaultProvider {
        public ComponentDef acquireFromGetComponentDef(S2Container container,
                Object key) {
            return getComponentDef(container, key);
        }
    }

    public static class PrototypeToSingletonDeployerFactory extends
            ComponentDeployerFactory.DefaultProvider {
        public ComponentDeployer createPrototypeComponentDeployer(
                ComponentDef cd) {
            return new SingletonComponentDeployer(cd);
        }
    }

    public static class AutoToManualOnlyAssemblerFactory extends
            AssemblerFactory.DefaultProvider {
        public PropertyAssembler createAutoPropertyAssembler(ComponentDef cd) {
            return new ManualOnlyPropertyAssembler(cd);
        }
    }

    public static interface Foo {
    }

    public static class Bar implements Foo {
    }

    public static class Baz {
        Foo foo;

        public Foo getFoo() {
            return this.foo;
        }

        public void setFoo(Foo foo) {
            this.foo = foo;
        }
    }

    public static class ChildFirstClassLoader extends ClassLoader {
        public ChildFirstClassLoader(ClassLoader parent) {
            super(parent);
        }

        protected synchronized Class loadClass(String name, boolean resolve)
                throws ClassNotFoundException {
            if (!name.equals(Baz.class.getName())) {
                return super.loadClass(name, resolve);
            }

            InputStream is = getParent().getResourceAsStream(
                    name.replace('.', '/') + ".class");
            if (is == null) {
                throw new ClassNotFoundException(name);
            }
            try {
                byte[] bytes = new byte[is.available()];
                is.read(bytes);
                Class clazz = defineClass(name, bytes, 0, bytes.length);
                if (resolve) {
                    resolveClass(clazz);
                }
                return clazz;
            } catch (IOException e) {
                throw new IORuntimeException(e);
            }
        }
    }
}
