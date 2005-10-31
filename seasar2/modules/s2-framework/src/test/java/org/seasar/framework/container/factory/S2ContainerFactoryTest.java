package org.seasar.framework.container.factory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
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
import org.seasar.framework.container.factory.AbstractS2ContainerBuilder;
import org.seasar.framework.container.factory.CircularIncludeRuntimeException;
import org.seasar.framework.container.factory.ResourceResolver;
import org.seasar.framework.container.factory.S2ContainerFactory;
import org.seasar.framework.container.factory.SimplePathResolver;
import org.seasar.framework.container.factory.XmlS2ContainerBuilder;
import org.seasar.framework.container.impl.S2ContainerBehavior;
import org.seasar.framework.container.impl.S2ContainerImpl;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.ResourceUtil;
import org.xml.sax.SAXException;

/**
 * @author koichik
 */
public class S2ContainerFactoryTest extends TestCase {
    protected Method configureMethod_;

    public S2ContainerFactoryTest() {
    }

    public S2ContainerFactoryTest(String name) {
        super(name);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(S2ContainerFactoryTest.class);
    }

    public void setUp() throws Exception {
        configureMethod_ = S2ContainerFactory.class.getDeclaredMethod("configure", new Class[0]);
        configureMethod_.setAccessible(true);
    }

    public void tearDown() throws Exception {
        S2ContainerFactory.setProvider(new S2ContainerFactory.DefaultProvider());
        S2ContainerFactory.setDefaultBuilder(new XmlS2ContainerBuilder());
        S2ContainerBehavior.setProvider(new S2ContainerBehavior.DefaultProvider());
        ComponentDeployerFactory.setProvider(new ComponentDeployerFactory.DefaultProvider());
        AssemblerFactory.setProvider(new AssemblerFactory.DefaultProvider());
    }

    public void testCircularInclude() throws Exception {
        try {
            S2ContainerFactory.create(getClass().getName().replace('.', '/') + ".CircularA.dicon");
            fail("1");
        }
        catch (Throwable e) {
            while (e != null) {
                if (e instanceof CircularIncludeRuntimeException) {
                    return;
                }
                e = (e instanceof SAXException) ? ((SAXException) e).getException() : e.getCause();
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
        S2Container container = S2ContainerFactory.create(getClass().getName().replace('.', '/')
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
        S2Container container = S2ContainerFactory.create(getClass().getName().replace('.', '/')
                + ".dicon");
        container.init();
        assertNull("1", container.getComponent("notFound"));
    }

    public void testCustomizeComponentDeployerFactory() throws Exception {
        configure("ComponetDeployerFactory.dicon");
        S2Container container = S2ContainerFactory.create(getClass().getName().replace('.', '/')
                + ".foo.dicon");
        container.init();
        Bar bar = (Bar) container.getComponent("bar");
        assertSame("1", bar, container.getComponent("bar"));
    }

    public void testCustomizeAssemblerFactory() throws Exception {
        configure("AssemblerFactory.dicon");
        S2Container container = S2ContainerFactory.create(getClass().getName().replace('.', '/')
                + ".foo.dicon");
        container.init();
        Baz baz = (Baz) container.getComponent("baz");
        assertNull("1", baz.getFoo());
    }

    public void configure(String name) throws Exception {
        String path = getClass().getName().replace('.', '/') + "." + name;
        System.setProperty(S2ContainerFactory.FACTORY_CONFIG_KEY, path);
        configureMethod_.invoke(null, null);
    }

    public static class EmptyContainerFactory extends S2ContainerFactory.DefaultProvider {
        public S2Container create(String path) {
            return new S2ContainerImpl();
        }
    }

    public static class FixedPathResolver extends SimplePathResolver {
        public String resolvePath(String context, String path) {
            return S2ContainerFactoryTest.class.getName().replace('.', '/') + ".PathResolver.dicon";
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
                    container.register(ClassUtil.forName(props.getProperty(name)), name);
                }
                return container;
            }
            catch (IOException e) {
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

    public static class UnthrowExceptionBehavior extends S2ContainerBehavior.DefaultProvider {
        public ComponentDef acquireFromGetComponentDef(S2Container container, Object key) {
            return getComponentDef(container, key);
        }
    }

    public static class PrototypeToSingletonDeployerFactory extends
            ComponentDeployerFactory.DefaultProvider {
        public ComponentDeployer createPrototypeComponentDeployer(ComponentDef cd) {
            return new SingletonComponentDeployer(cd);
        }
    }

    public static class AutoToManualOnlyAssemblerFactory extends AssemblerFactory.DefaultProvider {
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
}
