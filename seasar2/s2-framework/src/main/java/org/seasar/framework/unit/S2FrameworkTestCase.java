/*
 * Copyright 2004-2005 the Seasar Foundation and the Others.
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
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Servlet;
import javax.sql.DataSource;
import javax.transaction.TransactionManager;

import junit.framework.TestCase;

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.ContainerConstants;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.S2ContainerFactory;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.container.impl.S2ContainerImpl;
import org.seasar.framework.exception.EmptyRuntimeException;
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
import org.seasar.framework.util.ConnectionUtil;
import org.seasar.framework.util.DataSourceUtil;
import org.seasar.framework.util.FieldUtil;
import org.seasar.framework.util.MethodUtil;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.framework.util.StringUtil;

/**
 * @author higa
 */
public abstract class S2FrameworkTestCase extends TestCase {

    private static final String DATASOURCE_NAME = "j2ee"
            + ContainerConstants.NS_SEP + "dataSource";

    private S2Container container_;

    private Servlet servlet_;

    private MockServletConfig servletConfig_;

    private MockServletContext servletContext_;

    private MockHttpServletRequest request_;

    private MockHttpServletResponse response_;

    private DataSource dataSource_;

    private Connection connection_;

    private DatabaseMetaData dbMetaData_;

    private List bindedFields_;

    public S2FrameworkTestCase() {
    }

    public S2FrameworkTestCase(String name) {
        super(name);
    }

    public S2Container getContainer() {
        return container_;
    }

    public Object getComponent(String componentName) {
        return container_.getComponent(componentName);
    }

    public Object getComponent(Class componentClass) {
        return container_.getComponent(componentClass);
    }

    public ComponentDef getComponentDef(String componentName) {
        return container_.getComponentDef(componentName);
    }

    public ComponentDef getComponentDef(Class componentClass) {
        return container_.getComponentDef(componentClass);
    }

    public void register(Class componentClass) {
        container_.register(componentClass);
    }

    public void register(Class componentClass, String componentName) {
        container_.register(componentClass, componentName);
    }

    public void register(Object component) {
        container_.register(component);
    }

    public void register(Object component, String componentName) {
        container_.register(component, componentName);
    }

    public void register(ComponentDef componentDef) {
        container_.register(componentDef);
    }

    public void include(String path) {
        S2ContainerFactory.include(container_, convertPath(path));
    }

    protected String convertPath(String path) {
        if (ResourceUtil.getResourceNoException(path) != null) {
            return path;
        }
        String prefix = getClass().getName().replace('.', '/').replaceFirst(
                "/[^/]+$", "");
        return prefix + "/" + path;
    }

    public DataSource getDataSource() {
        if (dataSource_ == null) {
            throw new EmptyRuntimeException("dataSource");
        }
        return dataSource_;
    }

    public Connection getConnection() {
        if (connection_ != null) {
            return connection_;
        }
        connection_ = DataSourceUtil.getConnection(getDataSource());
        return connection_;
    }

    public DatabaseMetaData getDatabaseMetaData() {
        if (dbMetaData_ != null) {
            return dbMetaData_;
        }
        dbMetaData_ = ConnectionUtil.getMetaData(getConnection());
        return dbMetaData_;
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
                container_.init();
                try {
                    setupDataSource();
                    try {
                        setUpAfterContainerInit();
                        bindFields();
                        setUpAfterBindFields();
                        try {
                            runTestTx();
                        } finally {
                            tearDownBeforeUnbindFields();
                            unbindFields();
                        }
                        tearDownBeforeContainerDestroy();
                    } finally {
                        tearDownDataSource();
                    }
                } finally {
                    tearDownContainer();
                }
            } finally {
                tearDownForEachTestMethod();
            }

        } finally {
            for (int i = 0; i < 5; ++i) {
                System.runFinalization();
                System.gc();
            }
            tearDown();
        }
    }

    protected void setUpContainer() throws Throwable {
        container_ = new S2ContainerImpl();
        servletContext_ = new MockServletContextImpl("s2jsf-example");
        request_ = servletContext_.createRequest("/hello.html");
        response_ = new MockHttpServletResponseImpl(request_);
        servletConfig_ = new MockServletConfigImpl();
        servletConfig_.setServletContext(servletContext_);
        servlet_ = new MockServlet();
        servlet_.init(servletConfig_);
        container_.setServletContext(servletContext_);
        container_.setRequest(request_);
        container_.setResponse(response_);
        SingletonS2ContainerFactory.setContainer(container_);
    }

    protected void tearDownContainer() throws Throwable {
        container_.destroy();
        SingletonS2ContainerFactory.setContainer(null);
        container_ = null;
        servletContext_ = null;
        request_ = null;
        response_ = null;
        servletConfig_ = null;
        servlet_ = null;
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
        return servlet_;
    }

    protected void setServlet(Servlet servlet) {
        servlet_ = servlet;
    }

    protected MockServletConfig getServletConfig() {
        return servletConfig_;
    }

    protected void setServletConfig(MockServletConfig servletConfig) {
        servletConfig_ = servletConfig;
    }

    protected MockServletContext getServletContext() {
        return servletContext_;
    }

    protected void setServletContext(MockServletContext servletContext) {
        servletContext_ = servletContext;
    }

    protected MockHttpServletRequest getRequest() {
        return request_;
    }

    protected void setRequest(MockHttpServletRequest request) {
        request_ = request;
    }

    protected MockHttpServletResponse getResponse() {
        return response_;
    }

    protected void setResponse(MockHttpServletResponse response) {
        response_ = response;
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
        bindedFields_ = new ArrayList();
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
                bindedFields_.add(field);
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

    protected void unbindFields() {
        for (int i = 0; i < bindedFields_.size(); ++i) {
            Field field = (Field) bindedFields_.get(i);
            try {
                field.set(this, null);
            } catch (IllegalArgumentException e) {
                System.err.println(e);
            } catch (IllegalAccessException e) {
                System.err.println(e);
            }
        }
    }

    protected void runTestTx() throws Throwable {
        TransactionManager tm = null;
        if (needTransaction()) {
            try {
                tm = (TransactionManager) getComponent(TransactionManager.class);
                tm.begin();
            } catch (Throwable t) {
                System.err.println(t);
            }
        }
        try {
            runTest();
        } finally {
            if (tm != null) {
                tm.rollback();
            }
        }
    }

    protected boolean needTransaction() {
        return getName().endsWith("Tx");
    }

    protected void setupDataSource() {
        try {
            if (container_.hasComponentDef(DATASOURCE_NAME)) {
                dataSource_ = (DataSource) container_
                        .getComponent(DATASOURCE_NAME);
            } else if (container_.hasComponentDef(DataSource.class)) {
                dataSource_ = (DataSource) container_
                        .getComponent(DataSource.class);
            }
        } catch (Throwable t) {
            System.err.println(t);
        }
    }

    protected void tearDownDataSource() {
        dbMetaData_ = null;
        if (connection_ != null) {
            ConnectionUtil.close(connection_);
            connection_ = null;
        }
        dataSource_ = null;
    }
}