/*
 * Copyright 2004-2013 the Seasar Foundation and the Others.
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
package org.seasar.framework.unit.impl;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.seasar.framework.container.ExternalContext;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.annotation.tiger.DestroyMethod;
import org.seasar.framework.container.annotation.tiger.InitMethod;
import org.seasar.framework.container.deployer.ComponentDeployerFactory;
import org.seasar.framework.container.deployer.ExternalComponentDeployerProvider;
import org.seasar.framework.container.external.servlet.HttpServletExternalContext;
import org.seasar.framework.container.external.servlet.HttpServletExternalContextComponentDefRegister;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.container.servlet.S2ContainerServlet;
import org.seasar.framework.message.MessageResourceBundleFactory;
import org.seasar.framework.mock.servlet.MockHttpServletRequest;
import org.seasar.framework.mock.servlet.MockHttpServletResponse;
import org.seasar.framework.mock.servlet.MockHttpServletResponseImpl;
import org.seasar.framework.mock.servlet.MockServletConfig;
import org.seasar.framework.mock.servlet.MockServletConfigImpl;
import org.seasar.framework.mock.servlet.MockServletContext;
import org.seasar.framework.mock.servlet.MockServletContextImpl;

/**
 * テスト内でServlet、JTA、EJB3のAPIを利用することが可能なテストコンテキストです。
 * <p>
 * このクラスはサーブレットAPIのモックをサポートします。
 * </p>
 * 
 * @author taedium
 */
public class InternalTestContextImpl extends SimpleInternalTestContext {

    /** {@link ServletContext}のモック */
    protected MockServletContext servletContext;

    /** サーブレット */
    protected Servlet servlet;

    /** {@link ServletConfig}のモック */
    protected MockServletConfig servletConfig;

    /** {@link HttpServletRequest}のモック */
    protected MockHttpServletRequest request;

    /** {@link HttpServletResponse}のモック */
    protected MockHttpServletResponse response;

    /**
     * サーブレットコンテキストを設定します。
     * 
     * @param servletContext
     *            サーブレットコンテキスト
     */
    @Binding(bindingType = BindingType.MAY)
    public void setServletContext(MockServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @InitMethod
    @Override
    public void init() throws Throwable {
        if (servletContext == null) {
            servletContext = new MockServletContextImpl("s2-example");
        }
        request = servletContext.createRequest("/hello.html");
        response = new MockHttpServletResponseImpl(request);
        servletConfig = new MockServletConfigImpl();
        servletConfig.setServletContext(servletContext);
        servlet = new S2ContainerServlet();
        servlet.init(servletConfig);
        ExternalContext externalContext = new HttpServletExternalContext();
        externalContext.setApplication(servletContext);
        externalContext.setRequest(request);
        externalContext.setResponse(response);
        container.setExternalContext(externalContext);
        container
                .setExternalContextComponentDefRegister(new HttpServletExternalContextComponentDefRegister());
        ComponentDeployerFactory
                .setProvider(new ExternalComponentDeployerProvider());
        super.init();
    }

    @DestroyMethod
    @Override
    public void destroy() {
        ComponentDeployerFactory
                .setProvider(new ComponentDeployerFactory.DefaultProvider());
        SingletonS2ContainerFactory.setContainer(null);
        S2ContainerServlet.clearInstance();
        MessageResourceBundleFactory.clear();
        servletContext = null;
        request = null;
        response = null;
        servletConfig = null;
        servlet = null;
        super.destroy();
    }

    /**
     * インスタンスを構築します。
     */
    public InternalTestContextImpl() {
        setEjb3Enabled(true);
        setJtaEnabled(true);
    }
}
