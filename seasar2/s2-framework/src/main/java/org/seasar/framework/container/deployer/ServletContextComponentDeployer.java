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
package org.seasar.framework.container.deployer;

import javax.servlet.ServletContext;

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.ExternalContext;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.hotswap.Hotswap;

/**
 * @author higa
 * 
 */
public class ServletContextComponentDeployer extends AbstractComponentDeployer {

    /**
     * @param componentDef
     */
    public ServletContextComponentDeployer(ComponentDef componentDef) {
        super(componentDef);
    }

    /**
     * @see org.seasar.framework.container.ComponentDeployer#deploy()
     */
    public Object deploy() {
        ComponentDef cd = getComponentDef();
        ServletContext servletContext = null;
        ExternalContext extCtx = cd.getContainer().getRoot().getExternalContext();
        if (extCtx != null && extCtx.getApplication() instanceof ServletContext) {
            servletContext = (ServletContext) extCtx.getApplication();
        }
        if (servletContext == null) {
            throw new EmptyRuntimeException("servletContext");
        }
        String componentName = getComponentName();
        Object component = null;
        Hotswap hotswap = cd.getHotswap();
        if (hotswap == null || !hotswap.isModified()) {
            component = servletContext.getAttribute(componentName);
            if (component != null) {
                return component;
            }
        }
        component = getConstructorAssembler().assemble();
        servletContext.setAttribute(componentName, component);
        getPropertyAssembler().assemble(component);
        getInitMethodAssembler().assemble(component);
        return component;
    }

    public void injectDependency(Object component) {
        throw new UnsupportedOperationException("injectDependency");
    }

    /**
     * @see org.seasar.framework.container.ComponentDeployer#init()
     */
    public void init() {
    }

    /**
     * @see org.seasar.framework.container.ComponentDeployer#destroy()
     */
    public void destroy() {
    }
}