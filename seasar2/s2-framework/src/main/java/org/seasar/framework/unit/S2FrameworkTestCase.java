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
package org.seasar.framework.unit;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import javax.servlet.Servlet;

import junit.framework.TestCase;

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.ExternalContext;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.deployer.ComponentDeployerFactory;
import org.seasar.framework.container.deployer.HttpServletComponentDeployerProvider;
import org.seasar.framework.container.factory.S2ContainerFactory;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.container.impl.HttpServletExternalContext;
import org.seasar.framework.container.impl.HttpServletExternalContextComponentDefRegister;
import org.seasar.framework.container.impl.S2ContainerImpl;
import org.seasar.framework.exception.NoSuchMethodRuntimeException;
import org.seasar.framework.mock.servlet.MockHttpServletRequest;
import org.seasar.framework.mock.servlet.MockHttpServletResponse;
import org.seasar.framework.mock.servlet.MockHttpServletResponseImpl;
import org.seasar.framework.mock.servlet.MockServlet;
import org.seasar.framework.mock.servlet.MockServletConfig;
import org.seasar.framework.mock.servlet.MockServletConfigImpl;
import org.seasar.framework.mock.servlet.MockServletContext;
import org.seasar.framework.mock.servlet.MockServletContextImpl;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.FieldUtil;
import org.seasar.framework.util.MethodUtil;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.framework.util.StringUtil;

/**
 * @author higa
 */
public abstract class S2FrameworkTestCase extends TestCase {

    private S2Container container;

    private Servlet servlet;

    private MockServletConfig servletConfig;

    private MockServletContext servletContext;

    private MockHttpServletRequest request;

    private MockHttpServletResponse response;

    public S2FrameworkTestCase() {
    }

    public S2FrameworkTestCase(String name) {
        super(name);
    }

    public S2Container getContainer() {
        return container;
    }

    public Object getComponent(String componentName) {
        return container.getComponent(componentName);
    }

    public Object getComponent(Class componentClass) {
        return container.getComponent(componentClass);
    }

    public ComponentDef getComponentDef(String componentName) {
        return container.getComponentDef(componentName);
    }

    public ComponentDef getComponentDef(Class componentClass) {
        return container.getComponentDef(componentClass);
    }

    public void register(Class componentClass) {
        container.register(componentClass);
    }

    public void register(Class componentClass, String componentName) {
        container.register(componentClass, componentName);
    }

    public void register(Object component) {
        container.register(component);
    }

    public void register(Object component, String componentName) {
        container.register(component, componentName);
    }

    public void register(ComponentDef componentDef) {
        container.register(componentDef);
    }

    public void include(String path) {
        S2ContainerFactory.include(container, convertPath(path));
    }

    protected String convertPath(String path) {
        if (ResourceUtil.getResourceNoException(path) != null) {
            return path;
        }
        String prefix = getClass().getName().replace('.', '/').replaceFirst(
                "/[^/]+$", "");
        return prefix + "/" + path;
    }

    /**
     * @see junit.framework.TestCase#runBare()
     */
    public void runBare() throws Throwable {
        setUpContainer();
        setUp();
        try {
            setUpForEachTestMethod();
            try {
                container.init();
                try {
                    setUpAfterContainerInit();
                    try {
                        bindFields();
                        setUpAfterBindFields();
                        try {
                            runTest();
                        } finally {
                            tearDownBeforeUnbindFields();
                            unbindFields();
                        }
                    } finally {
                        tearDownBeforeContainerDestroy();
                    }
                } finally {
                    container.destroy();
                }
            } finally {
                tearDownForEachTestMethod();
            }

        } finally {
            tearDown();
            tearDownContainer();
        }
    }

    protected void setUpContainer() throws Throwable {
        container = new S2ContainerImpl();
        if (servletContext == null) {
            servletContext = new MockServletContextImpl("s2-example");
        }
        request = servletContext.createRequest("/hello.html");
        response = new MockHttpServletResponseImpl(request);
        servletConfig = new MockServletConfigImpl();
        servletConfig.setServletContext(servletContext);
        servlet = new MockServlet();
        servlet.init(servletConfig);
        ExternalContext externalContext = new HttpServletExternalContext();
        externalContext.setApplication(servletContext);
        externalContext.setRequest(request);
        externalContext.setResponse(response);
        container.setExternalContext(externalContext);
        container.setExternalContextComponentDefRegister(
                new HttpServletExternalContextComponentDefRegister());
        SingletonS2ContainerFactory.setContainer(container);
        ComponentDeployerFactory.setProvider(new HttpServletComponentDeployerProvider());
    }

    protected void tearDownContainer() throws Throwable {
        ComponentDeployerFactory.setProvider(new ComponentDeployerFactory.DefaultProvider());
        SingletonS2ContainerFactory.setContainer(null);
        container = null;
        servletContext = null;
        request = null;
        response = null;
        servletConfig = null;
        servlet = null;
    }

    protected void setUpAfterContainerInit() throws Throwable {
    }

    protected void setUpAfterBindFields() throws Throwable {
    }

    protected void tearDownBeforeUnbindFields() throws Throwable {
    }

    protected void setUpForEachTestMethod() throws Throwable {
        String targetName = getTargetName();
        if (targetName.length() > 0) {
            invoke("setUp" + targetName);
        }
    }

    protected void tearDownBeforeContainerDestroy() throws Throwable {
    }

    protected void tearDownForEachTestMethod() throws Throwable {
        String targetName = getTargetName();
        if (targetName.length() > 0) {
            invoke("tearDown" + getTargetName());
        }
    }

    protected Servlet getServlet() {
        return servlet;
    }

    protected void setServlet(Servlet servlet) {
        this.servlet = servlet;
    }

    protected MockServletConfig getServletConfig() {
        return servletConfig;
    }

    protected void setServletConfig(MockServletConfig servletConfig) {
        this.servletConfig = servletConfig;
    }

    protected MockServletContext getServletContext() {
        return servletContext;
    }

    protected void setServletContext(MockServletContext servletContext) {
        this.servletContext = servletContext;
    }

    protected MockHttpServletRequest getRequest() {
        return request;
    }

    protected void setRequest(MockHttpServletRequest request) {
        this.request = request;
    }

    protected MockHttpServletResponse getResponse() {
        return response;
    }

    protected void setResponse(MockHttpServletResponse response) {
        this.response = response;
    }

    protected String getTargetName() {
        return getName().substring(4);
    }

    protected void invoke(String methodName) throws Throwable {
        try {
            Method method = ClassUtil.getMethod(getClass(), methodName, null);
            MethodUtil.invoke(method, this, null);
        } catch (NoSuchMethodRuntimeException ignore) {
        }
    }

    protected void bindFields() throws Throwable {
        for (Class clazz = getClass(); clazz != S2FrameworkTestCase.class
                && clazz != null; clazz = clazz.getSuperclass()) {

            Field[] fields = clazz.getDeclaredFields();
            for (int i = 0; i < fields.length; ++i) {
                bindField(fields[i]);
            }
        }
    }

    protected void bindField(Field field) {
        if (isAutoBindable(field)) {
            field.setAccessible(true);
            if (FieldUtil.get(field, this) != null) {
                return;
            }
            String name = normalizeName(field.getName());
            Object component = null;
            if (getContainer().hasComponentDef(name)) {
                Class componentClass = getComponentDef(name)
                        .getComponentClass();
                if (componentClass == null) {
                    component = getComponent(name);
                    if (component != null) {
                        componentClass = component.getClass();
                    }
                }
                if (componentClass != null
                        && field.getType().isAssignableFrom(componentClass)) {
                    if (component == null) {
                        component = getComponent(name);
                    }
                } else {
                    component = null;
                }
            }
            if (component == null
                    && getContainer().hasComponentDef(field.getType())) {
                component = getComponent(field.getType());
            }
            if (component != null) {
                FieldUtil.set(field, this, component);
            }
        }
    }

    protected String normalizeName(String name) {
        return StringUtil.replace(name, "_", "");
    }

    protected boolean isAutoBindable(Field field) {
        int modifiers = field.getModifiers();
        return !Modifier.isStatic(modifiers) && !Modifier.isFinal(modifiers)
                && !field.getType().isPrimitive();
    }

    /**
     * @deprecated
     */
    protected void unbindFields() {
    }

}
