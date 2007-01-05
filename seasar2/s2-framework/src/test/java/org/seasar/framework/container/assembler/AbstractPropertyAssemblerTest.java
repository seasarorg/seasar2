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

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.external.servlet.HttpServletExternalContext;
import org.seasar.framework.container.external.servlet.HttpServletExternalContextComponentDefRegister;
import org.seasar.framework.container.impl.S2ContainerImpl;
import org.seasar.framework.mock.servlet.MockHttpServletRequest;
import org.seasar.framework.mock.servlet.MockHttpServletRequestImpl;
import org.seasar.framework.mock.servlet.MockHttpServletResponse;
import org.seasar.framework.mock.servlet.MockHttpServletResponseImpl;
import org.seasar.framework.mock.servlet.MockServletContext;
import org.seasar.framework.mock.servlet.MockServletContextImpl;

/**
 * @author koichik
 * 
 */
public class AbstractPropertyAssemblerTest extends TestCase {

    S2Container container;

    HttpServletExternalContext externalContext;

    MockServletContext servletContext;

    MockHttpServletRequest request;

    MockHttpServletResponse response;

    BeanDesc beanDesc;

    PropertyDesc nameDesc;

    PropertyDesc ageDesc;

    PropertyDesc modelsDesc;

    PropertyDesc magazinesDesc;

    ComponentDef cd;

    TestPage testPage;

    TestPropertyAssembler assembler;

    Set set;

    protected void setUp() throws Exception {
        super.setUp();

        container = new S2ContainerImpl();

        externalContext = new HttpServletExternalContext();
        container.setExternalContext(externalContext);

        servletContext = new MockServletContextImpl("/seasar2");
        externalContext.setApplication(servletContext);

        request = new MockHttpServletRequestImpl(servletContext, "/sersar2");
        externalContext.setRequest(request);

        response = new MockHttpServletResponseImpl(request);
        externalContext.setResponse(response);

        container
                .setExternalContextComponentDefRegister(new HttpServletExternalContextComponentDefRegister());
        container.register(TestPage.class, "testPage");
        container.init();

        beanDesc = BeanDescFactory.getBeanDesc(TestPage.class);
        nameDesc = beanDesc.getPropertyDesc("name");
        ageDesc = beanDesc.getPropertyDesc("age");
        modelsDesc = beanDesc.getPropertyDesc("models");
        magazinesDesc = beanDesc.getPropertyDesc("magazines");

        cd = container.getComponentDef("testPage");
        testPage = (TestPage) cd.getComponent();
        assembler = new TestPropertyAssembler(cd);
        set = new HashSet();
    }

    public void testBindParameter() throws Exception {
        request.setParameter("name", "Hoge");
        request.setParameter("age", "30");
        request.setParameter("models", new String[] { "Yuri", "Nao", "Maki" });
        request.setParameter("magazines",
                new String[] { "CanCam", "JJ", "Ray" });
        assembler.bindExternally(beanDesc, cd, testPage, set);
        assertEquals(4, set.size());
        assertEquals("Hoge", testPage.name);
        assertEquals(30, testPage.age);
        assertEquals(3, testPage.models.length);
        assertEquals("Yuri", testPage.models[0]);
        assertEquals("Nao", testPage.models[1]);
        assertEquals("Maki", testPage.models[2]);
        assertEquals(3, testPage.magazines.size());
        assertEquals("CanCam", testPage.magazines.get(0));
        assertEquals("JJ", testPage.magazines.get(1));
        assertEquals("Ray", testPage.magazines.get(2));
    }

    public void testBindHeader() throws Exception {
        request.addHeader("name", "Hoge");
        request.addHeader("age", "30");
        request.addHeader("models", "Yuri");
        request.addHeader("models", "Nao");
        request.addHeader("models", "Maki");
        request.addHeader("magazines", "CanCam");
        request.addHeader("magazines", "JJ");
        request.addHeader("magazines", "Ray");
        assembler.bindExternally(beanDesc, cd, testPage, set);
        assertEquals(4, set.size());
        assertEquals("Hoge", testPage.name);
        assertEquals(30, testPage.age);
        assertEquals(3, testPage.models.length);
        assertEquals("Yuri", testPage.models[0]);
        assertEquals("Nao", testPage.models[1]);
        assertEquals("Maki", testPage.models[2]);
        assertEquals(3, testPage.magazines.size());
        assertEquals("CanCam", testPage.magazines.get(0));
        assertEquals("JJ", testPage.magazines.get(1));
        assertEquals("Ray", testPage.magazines.get(2));
    }

    public void testPriority() throws Exception {
        request.setParameter("name", "Foo");
        request.addHeader("name", "Bar");
        request.setParameter("age", "20");
        request.addHeader("age", "30");
        assembler.bindExternally(beanDesc, cd, testPage, set);
        assertEquals(2, set.size());
        assertEquals("Foo", testPage.name);
        assertEquals(20, testPage.age);

    }

    public void testExcept() throws Exception {
        request.setParameter("name", "null");
        assembler.bindExternally(beanDesc, cd, testPage, set);
        assertEquals(1, set.size());
        assertEquals("", testPage.name);
    }

    public void testTypeMismatch() throws Exception {
        request.setParameter("contract", "hoge");
        assembler.bindExternally(beanDesc, cd, testPage, set);
        assertEquals(0, set.size());
        assertEquals(null, testPage.contract);
    }

    public void testNotBind() throws Exception {
        assembler.bindExternally(beanDesc, cd, testPage, set);
        assertTrue(set.isEmpty());
        assertNull(testPage.name);
        assertEquals(0, testPage.age);
        assertNull(testPage.models);
        assertNull(testPage.magazines);
    }

    public static class TestPropertyAssembler extends AbstractPropertyAssembler {
        public TestPropertyAssembler(ComponentDef componentDef) {
            super(componentDef);
        }

        public void assemble(Object component) {
        }

        public void bindExternally(BeanDesc beanDesc,
                ComponentDef componentDef, Object component, Set names) {
            super.bindExternally(beanDesc, componentDef, component, names);
        }

    }

    public static class TestPage {
        String name;

        int age;

        String[] models;

        List magazines;

        Map contract;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String[] getModels() {
            return models;
        }

        public void setModels(String[] models) {
            this.models = models;
        }

        public List getMagazines() {
            return magazines;
        }

        public void setMagazines(List magazines) {
            this.magazines = magazines;
        }

        public Map getContract() {
            return contract;
        }

        public void setContract(Map contract) {
            this.contract = contract;
        }
    }

}
