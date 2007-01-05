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
 * @author koichik
 */
public class GenericS2ContainerInitializer {

    protected String containerConfigPath;

    protected String configPath;

    protected ExternalContext externalContext;

    protected ExternalContextComponentDefRegister externalContextComponentDefRegister;

    public GenericS2ContainerInitializer() {
        this(new GenericExternalContext(),
                new GenericExternalContextComponentDefRegister());
    }

    public GenericS2ContainerInitializer(
            ExternalContext externalContext,
            ExternalContextComponentDefRegister externalContextComponentDefRegister) {
        this.externalContext = externalContext;
        this.externalContextComponentDefRegister = externalContextComponentDefRegister;
    }

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

    protected boolean isAlreadyInitialized() {
        return SingletonS2ContainerFactory.hasContainer();
    }

    public void setConfigPath(final String configPath) {
        this.configPath = configPath;
    }

}
