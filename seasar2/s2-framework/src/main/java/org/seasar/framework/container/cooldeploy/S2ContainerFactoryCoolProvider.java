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
package org.seasar.framework.container.cooldeploy;

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.deployer.InstanceDefFactory;
import org.seasar.framework.container.factory.AnnotationHandler;
import org.seasar.framework.container.factory.AnnotationHandlerFactory;
import org.seasar.framework.container.factory.S2ContainerFactory.DefaultProvider;

/**
 * @author koichik
 * 
 */
public class S2ContainerFactoryCoolProvider extends DefaultProvider {

    protected static final String[] INCLUDE_PATHS = new String[] {
            "convention.dicon", "customizer.dicon", "creator.dicon" };

    protected AnnotationHandler handler = AnnotationHandlerFactory
            .getAnnotationHandler();

    public S2Container create(final String path) {
        final S2Container container = super.create(path);
        for (int i = 0; i < INCLUDE_PATHS.length; ++i) {
            if (!container.hasDescendant(INCLUDE_PATHS[i])) {
                include(container, INCLUDE_PATHS[i]);
            }
        }

        final ComponentDef cd = handler.createComponentDef(
                CoolComponentAutoRegister.class, InstanceDefFactory.SINGLETON);
        container.register(cd);

        return container;
    }

}
