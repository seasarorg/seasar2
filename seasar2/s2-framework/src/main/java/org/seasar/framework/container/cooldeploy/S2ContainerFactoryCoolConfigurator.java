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
package org.seasar.framework.container.cooldeploy;

import org.seasar.framework.container.ExternalContext;
import org.seasar.framework.container.ExternalContextComponentDefRegister;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.PathResolver;
import org.seasar.framework.container.factory.S2ContainerFactory.DefaultConfigurator;
import org.seasar.framework.container.factory.S2ContainerFactory.Provider;

/**
 * COOL deploy用の
 * {@link org.seasar.framework.container.factory.S2ContainerFactory.Configurator}です。
 * 
 * @author koichik
 * @see org.seasar.framework.container.factory.S2ContainerFactory.DefaultConfigurator
 * 
 */
public class S2ContainerFactoryCoolConfigurator extends DefaultConfigurator {

    protected Provider createProvider(S2Container configurationContainer) {
        S2ContainerFactoryCoolProvider provider = new S2ContainerFactoryCoolProvider();
        if (configurationContainer.hasComponentDef(PathResolver.class)) {
            provider.setPathResolver((PathResolver) configurationContainer
                    .getComponent(PathResolver.class));
        }
        if (configurationContainer.hasComponentDef(ExternalContext.class)) {
            provider
                    .setExternalContext((ExternalContext) configurationContainer
                            .getComponent(ExternalContext.class));
        }
        if (configurationContainer
                .hasComponentDef(ExternalContextComponentDefRegister.class)) {
            provider
                    .setExternalContextComponentDefRegister((ExternalContextComponentDefRegister) configurationContainer
                            .getComponent(ExternalContextComponentDefRegister.class));
        }
        return provider;
    }

}
