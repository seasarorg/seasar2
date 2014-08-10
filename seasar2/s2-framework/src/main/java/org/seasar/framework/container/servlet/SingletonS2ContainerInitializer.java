/*
 * Copyright 2004-2014 the Seasar Foundation and the Others.
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
package org.seasar.framework.container.servlet;

import org.seasar.framework.container.deployer.ComponentDeployerFactory;
import org.seasar.framework.container.deployer.ExternalComponentDeployerProvider;
import org.seasar.framework.container.external.servlet.HttpServletExternalContext;
import org.seasar.framework.container.external.servlet.HttpServletExternalContextComponentDefRegister;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.util.StringUtil;

/**
 * {@link SingletonS2ContainerFactory}を初期化するためのクラスです。
 * 
 * @author manhole
 */
public class SingletonS2ContainerInitializer {

    private Object application;

    private String configPath;

    /**
     * 初期化します。
     */
    public void initialize() {
        if (isAlreadyInitialized()) {
            return;
        }
        if (!StringUtil.isEmpty(configPath)) {
            SingletonS2ContainerFactory.setConfigPath(configPath);
        }
        if (ComponentDeployerFactory.getProvider() instanceof ComponentDeployerFactory.DefaultProvider) {
            ComponentDeployerFactory
                    .setProvider(new ExternalComponentDeployerProvider());
        }
        HttpServletExternalContext extCtx = new HttpServletExternalContext();
        extCtx.setApplication(application);
        SingletonS2ContainerFactory.setExternalContext(extCtx);
        SingletonS2ContainerFactory
                .setExternalContextComponentDefRegister(new HttpServletExternalContextComponentDefRegister());
        SingletonS2ContainerFactory.init();
    }

    private boolean isAlreadyInitialized() {
        return SingletonS2ContainerFactory.hasContainer();
    }

    /**
     * アプリケーションオブジェクトを設定します。
     * 
     * @param application
     */
    public void setApplication(Object application) {
        this.application = application;
    }

    /**
     * 設定パスを設定します。
     * 
     * @param configPath
     */
    public void setConfigPath(String configPath) {
        this.configPath = configPath;
    }

}
