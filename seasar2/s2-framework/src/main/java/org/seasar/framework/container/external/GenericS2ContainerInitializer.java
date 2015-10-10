/*
 * Copyright 2004-2015 the Seasar Foundation and the Others.
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
package org.seasar.framework.container.external;

import org.seasar.framework.container.ExternalContext;
import org.seasar.framework.container.ExternalContextComponentDefRegister;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.deployer.ComponentDeployerFactory;
import org.seasar.framework.container.deployer.ExternalComponentDeployerProvider;
import org.seasar.framework.container.factory.S2ContainerFactory;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.util.StringUtil;

/**
 * Genericな {@link S2Container}の初期化クラスです。
 * 
 * @author koichik
 */
public class GenericS2ContainerInitializer {

    /**
     * {@link S2Container}用の設定パスです。
     */
    protected String containerConfigPath;

    /**
     * アプリケーション用の設定パスです。
     */
    protected String configPath;

    /**
     * {@link ExternalContext}です。
     */
    protected ExternalContext externalContext;

    /**
     * {@link ExternalContextComponentDefRegister}です。
     */
    protected ExternalContextComponentDefRegister externalContextComponentDefRegister;

    /**
     * {@link GenericS2ContainerInitializer}を作成します。
     */
    public GenericS2ContainerInitializer() {
        this(new GenericExternalContext(),
                new GenericExternalContextComponentDefRegister());
    }

    /**
     * {@link GenericS2ContainerInitializer}を作成します。
     * 
     * @param externalContext
     * @param externalContextComponentDefRegister
     */
    public GenericS2ContainerInitializer(
            ExternalContext externalContext,
            ExternalContextComponentDefRegister externalContextComponentDefRegister) {
        this.externalContext = externalContext;
        this.externalContextComponentDefRegister = externalContextComponentDefRegister;
    }

    /**
     * 初期化します。
     * 
     * @return {@link S2Container}
     */
    public S2Container initialize() {
        if (isAlreadyInitialized()) {
            return SingletonS2ContainerFactory.getContainer();
        }
        if (!StringUtil.isEmpty(containerConfigPath)) {
            S2ContainerFactory.configure(containerConfigPath);
        }
        if (!StringUtil.isEmpty(configPath)) {
            SingletonS2ContainerFactory.setConfigPath(configPath);
        }
        if (ComponentDeployerFactory.getProvider() instanceof ComponentDeployerFactory.DefaultProvider) {
            ComponentDeployerFactory
                    .setProvider(new ExternalComponentDeployerProvider());
        }
        SingletonS2ContainerFactory.setExternalContext(externalContext);
        SingletonS2ContainerFactory
                .setExternalContextComponentDefRegister(externalContextComponentDefRegister);
        SingletonS2ContainerFactory.init();

        return SingletonS2ContainerFactory.getContainer();
    }

    /**
     * 初期化されているかどうか返します。
     * 
     * @return 初期化されているかどうか
     */
    protected boolean isAlreadyInitialized() {
        return SingletonS2ContainerFactory.hasContainer();
    }

    /**
     * 設定パスを設定します。
     * 
     * @param configPath
     */
    public void setConfigPath(final String configPath) {
        this.configPath = configPath;
    }

}
