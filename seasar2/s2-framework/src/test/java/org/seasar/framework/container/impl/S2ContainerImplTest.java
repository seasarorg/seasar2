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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import junit.framework.TestCase;

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.ContainerConstants;
import org.seasar.framework.container.Expression;
import org.seasar.framework.container.ExternalContext;
import org.seasar.framework.container.InitMethodDef;
import org.seasar.framework.container.PropertyDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.TooManyRegistrationRuntimeException;
import org.seasar.framework.container.deployer.InstanceDefFactory;
import org.seasar.framework.container.external.servlet.HttpServletExternalContext;
import org.seasar.framework.container.external.servlet.HttpServletExternalContextComponentDefRegister;
import org.seasar.framework.container.ognl.OgnlExpression;
import org.seasar.framework.mock.servlet.MockHttpServletResponseImpl;
import org.seasar.framework.mock.servlet.MockServletContextImpl;
import org.seasar.framework.util.ResourceUtil;

/**
 * @author higa
 * 
 */
public class S2ContainerImplTest extends TestCase {

    S2Container container0;

    S2Container container1;

    S2Container container2;

    S2Container container3;

    S2Container container4;

    protected void setUp() throws Exception {
        super.setUp();

        container0 = new S2ContainerImpl();
        container0.setPath("0");

        container1 = new S2ContainerImpl();
        container1.setPath("1");
        container1.register(HashMap.class);
        container0.include(container1);

        container2 = new S2ContainerImpl();
        container2.setPath("2");
        container2.register(HashMap.class);
        container2.register(HashMap.class);
        container1.include(container2);

        container3 = new S2ContainerImpl();
        container3.setPath("3");
        container3.register(HashMap.class);
        container3.register(HashMap.class);
        container3.register(HashMap.class);
        container2.include(container3);

        container4 = new S2ContainerImpl();
        container4.setPath("4");
        container4.register(HashMap.class);
        container4.register(HashMap.class);
        container4.register(HashMap.class);
        container4.register(HashMap.class);
        container3.include(container4);
    }

    public void testRegister() throws Exception {
        S2Container container = new S2ContainerImpl();
        container.register(A.class);
        container.register(B.class);
        container.register(B2.class);
        try {
            container.getComponent(A.class);
            fail("1");
        } catch (TooManyRegistrationRuntimeException ex) {
            System.out.println(ex);
            assertEquals("2", Hoge.class, ex.getKey());
            assertEquals("3", 2, ex.getComponentClasses().length);
            assertEquals("4", B.class, ex.getComponentClasses()[0]);
            assertEquals("5", B2.class, ex.getComponentClasses()[1]);
        }
    }

    public void testRegisterForAlreadyRegistration() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDef cd = new ComponentDefImpl(B.class, "B");
        ComponentDef cd2 = new ComponentDefImpl(B2.class, "B");
        container.register(cd);
        container.register(cd2);
        try {
            container.getComponent("B");
            fail("1");
        } catch (TooManyRegistrationRuntimeException ex) {
            System.out.println(ex);
            assertEquals("2", "B", ex.getKey());
            assertEquals("3", 2, ex.getComponentClasses().length);
            assertEquals("4", B.class, ex.getComponentClasses()[0]);
            assertEquals("5", B2.class, ex.getComponentClasses()[1]);
        }
    }

    public void testInclude() throws Exception {
        S2Container container = new S2ContainerImpl();
        container.register(A.class);
        S2Container container2 = new S2ContainerImpl();
        container2.register(B.class);
        container.include(container2);
        A a = (A) container.getComponent(A.class);
        assertEquals("1", "B", a.getHogeName());
        assertEquals("2", 1, container2.getParentSize());
        assertSame("3", container, container2.getParent(0));
    }

    public void testInclude2() throws Exception {
        S2Container root = new S2ContainerImpl();
        S2Container child = new S2ContainerImpl();
        child.setNamespace("aaa");
        child.register("hoge", "hoge");
        root.include(child);
        S2Container child2 = new S2ContainerImpl();
        child2.setNamespace("bbb");
        child2.register("hoge2", "hoge");
        S2Container grandchild = new S2ContainerImpl();
        grandchild.setNamespace("ccc");
        grandchild.register("hoge3", "hoge");
        child2.include(grandchild);
        root.include(child2);
        assertEquals("1", "hoge", child.getComponent("hoge"));
        assertEquals("2", "hoge3", grandchild.getComponent("hoge"));
        assertEquals("3", child, root.getComponent("aaa"));
        assertEquals("4", child2, root.getComponent("bbb"));
        assertEquals("5", "hoge", root.getComponent("aaa.hoge"));
        assertEquals("6", "hoge2", root.getComponent("bbb.hoge"));
        assertEquals("7", "hoge3", root.getComponent("ccc.hoge"));
        assertEquals("8", "hoge", child.getComponent("aaa.hoge"));
        assertEquals("9", false, child.hasComponentDef("bbb.hoge"));
        assertEquals("10", false, child.hasComponentDef("ccc.hoge"));
        assertEquals("11", "hoge2", child2.getComponent("hoge"));
        assertEquals("12", "hoge3", child2.getComponent("ccc.hoge"));
        assertEquals("13", 0, root.getComponentDefSize());
    }

    public void testInclude3() throws Exception {
        S2Container container = new S2ContainerImpl();
        S2Container child = new S2ContainerImpl();
        child.setPath("aaa.xml");
        S2Container grandchild = new S2ContainerImpl();
        grandchild.setPath("bbb.xml");
        grandchild.setNamespace("bbb");
        child.include(grandchild);
        container.include(child);
        container.include(grandchild);
        assertNotNull("1", container.getComponentDef("bbb"));
    }

    public void testInclude4() throws Exception {
        S2Container aaa = new S2ContainerImpl();
        aaa.setPath("aaa.xml");
        aaa.setNamespace("aaa");
        S2Container bbb = new S2ContainerImpl();
        bbb.setPath("bbb.xml");
        S2Container aaa2 = new S2ContainerImpl();
        aaa2.setPath("aaa.xml");
        aaa2.setNamespace("aaa");
        bbb.include(aaa2);
        aaa.include(bbb);
        assertNotNull("1", aaa.getComponentDef("aaa"));
    }

    public void testInclude5() throws Exception {
        S2Container container = new S2ContainerImpl();
        S2Container child = new S2ContainerImpl();
        child.setNamespace("aaa");
        S2Container child2 = new S2ContainerImpl();
        child2.setNamespace("aaa");
        container.include(child);
        container.include(child2);
        assertSame("1", child, container.getComponent("aaa"));
    }

    public void testInclude6() throws Exception {
        S2Container root = new S2ContainerImpl();
        S2Container aaa = new S2ContainerImpl();
        aaa.setPath("aaa.xml");
        aaa.setNamespace("aaa");
        S2Container bbb = new S2ContainerImpl();
        bbb.setPath("bbb.xml");
        bbb.setNamespace("bbb");
        bbb.register(new Date(), "date");
        aaa.include(bbb);
        root.include(aaa);
        assertNotNull("1", root.getComponentDef("bbb.date"));
    }

    public void testInclude7() throws Exception {
        S2Container root = new S2ContainerImpl();
        S2Container aaa = new S2ContainerImpl();
        aaa.setNamespace("aaa");
        S2Container bbb = new S2ContainerImpl();
        bbb.setNamespace("bbb");
        bbb.register(new Date(0), "date");
        S2Container ccc = new S2ContainerImpl();
        ccc.setNamespace("ccc");
        ccc.register(new Date(1), "date");
        bbb.include(ccc);
        aaa.include(bbb);
        root.include(aaa);
        assertEquals("1", new Date(1), root.getComponent("aaa.bbb.ccc.date"));
        assertEquals("2", new Date(0), root.getComponent("aaa.bbb.date"));
        assertEquals("3", new Date(0), root.getComponent("bbb.date"));
        assertEquals("4", new Date(1), root.getComponent("bbb.ccc.date"));
        assertEquals("5", new Date(1), root.getComponent("ccc.date"));
        assertEquals("6", new Date(0), root.getComponent("date"));
        assertEquals("7", "bbb", ((S2Container) root.getComponent("aaa.bbb"))
                .getNamespace());
        assertEquals("8", "bbb", ((S2Container) root.getComponent("bbb"))
                .getNamespace());
        assertEquals("9", "ccc", ((S2Container) root.getComponent("bbb.ccc"))
                .getNamespace());
        assertEquals("10", "ccc", ((S2Container) root.getComponent("ccc"))
                .getNamespace());
    }

    public void testRegisterAfterInclude() throws Exception {
        S2Container container = new S2ContainerImpl();
        S2Container container2 = new S2ContainerImpl();
        S2Container container3 = new S2ContainerImpl();
        container2.include(container3);
        container.include(container2);
        Date date = new Date(0);
        container3.register(date, "hoge");
        assertEquals("1", date, container.getComponent("hoge"));
        assertEquals("2", date, container.getComponent(Date.class));
    }

    public void testInitAndDestroy() throws Exception {
        S2Container container = new S2ContainerImpl();
        S2Container child = new S2ContainerImpl();
        List initList = new ArrayList();
        List destroyList = new ArrayList();
        ComponentDef componentDef = new ComponentDefImpl(C.class, "c1");
        componentDef.addInitMethodDef(new InitMethodDefImpl("init"));
        componentDef.addDestroyMethodDef(new DestroyMethodDefImpl("destroy"));
        componentDef.addArgDef(new ArgDefImpl("c1"));
        componentDef.addArgDef(new ArgDefImpl(initList));
        componentDef.addArgDef(new ArgDefImpl(destroyList));
        container.register(componentDef);

        componentDef = new ComponentDefImpl(C.class, "c2");
        componentDef.addInitMethodDef(new InitMethodDefImpl("init"));
        componentDef.addDestroyMethodDef(new DestroyMethodDefImpl("destroy"));
        componentDef.addArgDef(new ArgDefImpl("c2"));
        componentDef.addArgDef(new ArgDefImpl(initList));
        componentDef.addArgDef(new ArgDefImpl(destroyList));
        container.register(componentDef);

        componentDef = new ComponentDefImpl(C.class, "c3");
        componentDef.addInitMethodDef(new InitMethodDefImpl("init"));
        componentDef.addDestroyMethodDef(new DestroyMethodDefImpl("destroy"));
        componentDef.addArgDef(new ArgDefImpl("c3"));
        componentDef.addArgDef(new ArgDefImpl(initList));
        componentDef.addArgDef(new ArgDefImpl(destroyList));
        child.register(componentDef);
        container.include(child);

        container.init();
        assertEquals("1", 3, initList.size());
        assertEquals("2", "c3", initList.get(0));
        assertEquals("3", "c1", initList.get(1));
        assertEquals("4", "c2", initList.get(2));
        container.destroy();
        assertEquals("5", 3, destroyList.size());
        assertEquals("6", "c2", destroyList.get(0));
        assertEquals("7", "c1", destroyList.get(1));
        assertEquals("8", "c3", destroyList.get(2));
    }

    public void testContextClassLoaderWhenInit() throws Exception {
        final ClassLoader[] loader = new ClassLoader[1];
        S2Container container = new S2ContainerImpl();
        ComponentDef cd = new ComponentDefImpl(Runnable.class);
        cd.setExpression(new Expression() {
            public Object evaluate(S2Container container, Map context) {
                return new Runnable() {
                    public void run() {
                        loader[0] = Thread.currentThread()
                                .getContextClassLoader();
                    }
                };
            }
        });
        cd.addInitMethodDef(new InitMethodDefImpl("run"));
        container.register(cd);

        ClassLoader loader1 = Thread.currentThread().getContextClassLoader();
        ClassLoader loader2 = new URLClassLoader(new URL[0]);
        Thread.currentThread().setContextClassLoader(loader2);
        try {
            container.init();
            assertEquals("1", loader1, loader[0]);
            assertEquals("2", loader2, Thread.currentThread()
                    .getContextClassLoader());
        } finally {
            Thread.currentThread().setContextClassLoader(loader1);
        }
    }

    public void testContextClassLoaderWhenDestroy() throws Exception {
        final ClassLoader[] loader = new ClassLoader[1];
        S2Container container = new S2ContainerImpl();
        ComponentDef cd = new ComponentDefImpl(Runnable.class);
        cd.setExpression(new Expression() {
            public Object evaluate(S2Container container, Map context) {
                return new Runnable() {
                    public void run() {
                        loader[0] = Thread.currentThread()
                                .getContextClassLoader();
                    }
                };
            }
        });
        cd.addDestroyMethodDef(new DestroyMethodDefImpl("run"));
        container.register(cd);
        container.init();

        ClassLoader loader1 = Thread.currentThread().getContextClassLoader();
        ClassLoader loader2 = new URLClassLoader(new URL[0]);
        Thread.currentThread().setContextClassLoader(loader2);
        try {
            container.destroy();
            assertEquals("1", loader1, loader[0]);
            assertEquals("2", loader2, Thread.currentThread()
                    .getContextClassLoader());
        } finally {
            Thread.currentThread().setContextClassLoader(loader1);
        }
    }

    public void testInjectDependency() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDef componentDef = new ComponentDefImpl(HashMap.class, "hoge");
        componentDef.setInstanceDef(InstanceDefFactory.OUTER);
        InitMethodDef md = new InitMethodDefImpl("put");
        md.addArgDef(new ArgDefImpl("aaa"));
        md.addArgDef(new ArgDefImpl("111"));
        componentDef.addInitMethodDef(md);
        container.register(componentDef);

        HashMap map = new HashMap();
        container.injectDependency(map);
        assertEquals("1", "111", map.get("aaa"));

        HashMap map2 = new HashMap();
        container.injectDependency(map2, Map.class);
        assertEquals("2", "111", map2.get("aaa"));

        HashMap map3 = new HashMap();
        container.injectDependency(map3, "hoge");
        assertEquals("3", "111", map3.get("aaa"));
    }

    public void testSelf() throws Exception {
        S2Container container = new S2ContainerImpl();
        container.register(D.class);
        D d = (D) container.getComponent(D.class);
        assertSame("1", container, d.getContainer());
    }

    public void testSelf2() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDef cd = new ComponentDefImpl(D.class);
        PropertyDef pd = new PropertyDefImpl("container");
        pd.setExpression(new OgnlExpression(ContainerConstants.CONTAINER_NAME));
        cd.addPropertyDef(pd);
        container.register(cd);
        D d = (D) container.getComponent(D.class);
        assertSame("1", container, d.getContainer());
    }

    public void testConstructor() throws Exception {
        S2Container container = new S2ContainerImpl();
        assertEquals("1", 0, container.getComponentDefSize());
    }

    public void testNamespace() throws Exception {
        S2Container container = new S2ContainerImpl();
        container.setNamespace("aaa");
        container.register(String.class, "bbb");
        assertNotNull("1", container.getComponent("bbb"));
        assertNotNull("2", container.getComponent("aaa.bbb"));
    }

    public void testGetComponentDef() throws Exception {
        S2Container aaa = new S2ContainerImpl();
        aaa.setNamespace("aaa");
        S2Container bbb = new S2ContainerImpl();
        bbb.setNamespace("bbb");
        bbb.register(String.class, "hoge");
        aaa.include(bbb);
        assertNotNull("1", aaa.getComponentDef("bbb.hoge"));
        assertNotNull("2", bbb.getComponentDef("bbb.hoge"));
    }

    public void testGetComponentDef2() throws Exception {
        S2Container container = new S2ContainerImpl();
        container.register(FooImpl.class);
        Hoge hoge = (Hoge) container.getComponent(Hoge.class);
        assertEquals("1", "Foo", hoge.getName());
    }

    public void testFindComponents() throws Exception {
        Map[] found = (Map[]) container0.findComponents(Map.class);
        assertEquals(1, found.length);

        found = (Map[]) container1.findComponents(Map.class);
        assertEquals(1, found.length);

        found = (Map[]) container2.findComponents(Map.class);
        assertEquals(2, found.length);

        found = (Map[]) container3.findComponents(Map.class);
        assertEquals(3, found.length);

        found = (Map[]) container4.findComponents(Map.class);
        assertEquals(4, found.length);
    }

    public void testFindAllComponents() throws Exception {
        Map[] found = (Map[]) container0.findAllComponents(Map.class);
        assertEquals(10, found.length);

        found = (Map[]) container1.findAllComponents(Map.class);
        assertEquals(10, found.length);

        found = (Map[]) container2.findAllComponents(Map.class);
        assertEquals(9, found.length);

        found = (Map[]) container3.findAllComponents(Map.class);
        assertEquals(7, found.length);

        found = (Map[]) container4.findAllComponents(Map.class);
        assertEquals(4, found.length);
    }

    public void testFindLocalComponents() throws Exception {
        Map[] found = (Map[]) container0.findLocalComponents(Map.class);
        assertEquals(0, found.length);

        found = (Map[]) container1.findLocalComponents(Map.class);
        assertEquals(1, found.length);

        found = (Map[]) container2.findLocalComponents(Map.class);
        assertEquals(2, found.length);

        found = (Map[]) container3.findLocalComponents(Map.class);
        assertEquals(3, found.length);

        found = (Map[]) container4.findLocalComponents(Map.class);
        assertEquals(4, found.length);
    }

    public void testFindComponentDefs() throws Exception {
        ComponentDef[] found = container0.findComponentDefs(Map.class);
        assertEquals(1, found.length);

        found = container1.findComponentDefs(Map.class);
        assertEquals(1, found.length);

        found = container2.findComponentDefs(Map.class);
        assertEquals(2, found.length);

        found = container3.findComponentDefs(Map.class);
        assertEquals(3, found.length);

        found = container4.findComponentDefs(Map.class);
        assertEquals(4, found.length);
    }

    public void testFindAllComponentDefs() throws Exception {
        ComponentDef[] found = container0.findAllComponentDefs(Map.class);
        assertEquals(10, found.length);

        found = container1.findAllComponentDefs(Map.class);
        assertEquals(10, found.length);

        found = container2.findAllComponentDefs(Map.class);
        assertEquals(9, found.length);

        found = container3.findAllComponentDefs(Map.class);
        assertEquals(7, found.length);

        found = container4.findAllComponentDefs(Map.class);
        assertEquals(4, found.length);
    }

    public void testFindLocalComponentDefs() throws Exception {
        ComponentDef[] found = container0.findLocalComponentDefs(Map.class);
        assertEquals(0, found.length);

        found = container1.findLocalComponentDefs(Map.class);
        assertEquals(1, found.length);

        found = container2.findLocalComponentDefs(Map.class);
        assertEquals(2, found.length);

        found = container3.findLocalComponentDefs(Map.class);
        assertEquals(3, found.length);

        found = container4.findLocalComponentDefs(Map.class);
        assertEquals(4, found.length);
    }

    public void testRequest() throws Exception {
        S2Container container = new S2ContainerImpl();
        S2Container child = new S2ContainerImpl();
        child.setNamespace("aaa");
        child.register(RequestClient.class);
        container.include(child);
        MockServletContextImpl ctx = new MockServletContextImpl(
                "/s2jsf-example");
        HttpServletRequest request = ctx.createRequest("/hello.html");
        ExternalContext extCtx = new HttpServletExternalContext();
        extCtx.setRequest(request);
        container.setExternalContext(extCtx);
        container
                .setExternalContextComponentDefRegister(new HttpServletExternalContextComponentDefRegister());
        container.init();
        RequestClient client = (RequestClient) container
                .getComponent(RequestClient.class);
        assertNotNull("1", client.getRequest());
    }

    public void testSession() throws Exception {
        S2Container container = new S2ContainerImpl();
        S2Container child = new S2ContainerImpl();
        child.setNamespace("aaa");
        child.register(SessionClient.class);
        container.include(child);
        MockServletContextImpl ctx = new MockServletContextImpl(
                "/s2jsf-example");
        HttpServletRequest request = ctx.createRequest("/hello.html");
        ExternalContext extCtx = new HttpServletExternalContext();
        extCtx.setRequest(request);
        container.setExternalContext(extCtx);
        container
                .setExternalContextComponentDefRegister(new HttpServletExternalContextComponentDefRegister());
        container.init();
        SessionClient client = (SessionClient) container
                .getComponent(SessionClient.class);
        assertNotNull("1", client.getSession());
    }

    public void testResponse() throws Exception {
        S2Container container = new S2ContainerImpl();
        S2Container child = new S2ContainerImpl();
        child.setNamespace("aaa");
        child.register(ResponseClient.class);
        container.include(child);
        MockServletContextImpl ctx = new MockServletContextImpl(
                "/s2jsf-example");
        HttpServletRequest request = ctx.createRequest("/hello.html");
        HttpServletResponse response = new MockHttpServletResponseImpl(request);
        ExternalContext extCtx = new HttpServletExternalContext();
        extCtx.setResponse(response);
        container.setExternalContext(extCtx);
        container
                .setExternalContextComponentDefRegister(new HttpServletExternalContextComponentDefRegister());
        container.init();
        ResponseClient client = (ResponseClient) container
                .getComponent(ResponseClient.class);
        assertNotNull("1", client.getResponse());
    }

    public void testServletContext() throws Exception {
        S2Container container = new S2ContainerImpl();
        S2Container child = new S2ContainerImpl();
        child.setNamespace("aaa");
        child.register(ServletContextClient.class);
        container.include(child);
        MockServletContextImpl ctx = new MockServletContextImpl(
                "/s2jsf-example");
        ExternalContext extCtx = new HttpServletExternalContext();
        extCtx.setApplication(ctx);
        container.setExternalContext(extCtx);
        container
                .setExternalContextComponentDefRegister(new HttpServletExternalContextComponentDefRegister());
        container.init();
        ServletContextClient client = (ServletContextClient) container
                .getComponent(ServletContextClient.class);
        assertNotNull("1", client.getServletContext());
    }

    public void testOgnlClassResolvingWhileClassLoaderSpecified()
            throws Exception {
        S2Container container = new S2ContainerImpl() {
            public ClassLoader getClassLoader() {
                return null;
            }
        };
        ComponentDef componentDef = new ComponentDefImpl();
        componentDef.setComponentName("component");
        componentDef.setExpression(new OgnlExpression(
                "@org.seasar.framework.container.impl.S2ContainerImpl@class"));
        container.register(componentDef);
        Object obj = container.getComponent("component");
        assertNotNull("1", obj);
        assertEquals("2", S2ContainerImpl.class, obj);

        File componentClassFile = ResourceUtil
                .getResourceAsFile("org/seasar/framework/container/impl/classes/test/Component");
        File parentDir = componentClassFile.getParentFile();

        byte[] buf = new byte[4096];
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(componentClassFile);
            os = new FileOutputStream(new File(parentDir, "Component.class"));
            int len;
            while ((len = is.read(buf)) != -1) {
                os.write(buf, 0, len);
            }
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (Throwable t) {
                    ;
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (Throwable t) {
                    ;
                }
            }
        }
        File classesDir = parentDir.getParentFile();

        ClassLoader customCl = new URLClassLoader(new URL[] { classesDir
                .toURI().toURL() });
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(customCl);
            container = new S2ContainerImpl();
            componentDef = new ComponentDefImpl();
            componentDef.setComponentName("component");
            componentDef.setExpression(new OgnlExpression(
                    "@test.Component@class"));
            container.register(componentDef);
            obj = container.getComponent("component");
            assertNotNull("3", obj);
            assertSame("4", customCl, ((Class) obj).getClassLoader());
        } finally {
            Thread.currentThread().setContextClassLoader(cl);
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

    public interface Hoge {

        public String getName();
    }

    public interface Foo extends Hoge {
    }

    public static class B implements Hoge {

        public String getName() {
            return "B";
        }
    }

    public static class B2 implements Hoge {

        public String getName() {
            return "B2";
        }
    }

    public static class C {

        private String name_;

        private List initList_;

        private List destroyList_;

        public C(String name, List initList, List destoryList) {
            name_ = name;
            initList_ = initList;
            destroyList_ = destoryList;
        }

        public void init() {
            initList_.add(name_);
        }

        public void destroy() {
            destroyList_.add(name_);
        }
    }

    public static class D {

        private S2Container container_;

        public S2Container getContainer() {
            return container_;
        }

        public void setContainer(S2Container container) {
            container_ = container;
        }
    }

    public static class FooImpl implements Foo {
        public String getName() {
            return "Foo";
        }
    }

    public static class RequestClient {

        private HttpServletRequest request_;

        public HttpServletRequest getRequest() {
            return request_;
        }

        public void setRequest(HttpServletRequest request) {
            this.request_ = request;
        }
    }

    public static class SessionClient {

        private HttpSession session_;

        public HttpSession getSession() {
            return session_;
        }

        public void setSession(HttpSession session) {
            this.session_ = session;
        }
    }

    public static class ResponseClient {

        private HttpServletResponse response_;

        public HttpServletResponse getResponse() {
            return response_;
        }

        public void setResponse(HttpServletResponse response) {
            this.response_ = response;
        }
    }

    public static class ServletContextClient {

        private ServletContext servletContext_;

        public ServletContext getServletContext() {
            return servletContext_;
        }

        public void setServletContext(ServletContext servletContext) {
            servletContext_ = servletContext;
        }
    }

}
