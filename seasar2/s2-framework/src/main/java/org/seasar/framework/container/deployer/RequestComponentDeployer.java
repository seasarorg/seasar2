/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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

import java.util.Map;

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.ComponentDeployer;
import org.seasar.framework.container.ExternalContext;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.log.Logger;

/**
 * request用の{@link ComponentDeployer}です。
 * 
 * @author higa
 * 
 */
public class RequestComponentDeployer extends AbstractComponentDeployer {

    private static Logger logger = Logger
            .getLogger(RequestComponentDeployer.class);

    /**
     * {@link RequestComponentDeployer}を作成します。
     * 
     * @param componentDef
     */
    public RequestComponentDeployer(ComponentDef componentDef) {
        super(componentDef);
    }

    /**
     * @see org.seasar.framework.container.ComponentDeployer#deploy()
     */
    public Object deploy() {
        ComponentDef cd = getComponentDef();
        ExternalContext extCtx = cd.getContainer().getRoot()
                .getExternalContext();
        if (extCtx == null) {
            RuntimeException re = new EmptyRuntimeException("externalContext");
            logger.log(re);
            throw re;
        }
        Map requestMap = extCtx.getRequestMap();
        String componentName = getComponentName();
        Object component = null;
        component = requestMap.get(componentName);
        if (component != null) {
            return component;
        }
        component = getConstructorAssembler().assemble();
        requestMap.put(componentName, component);
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