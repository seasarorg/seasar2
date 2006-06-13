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

import java.util.Map;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.ExternalContext;
import org.seasar.framework.exception.EmptyRuntimeException;

/**
 * @author higa
 * 
 */
public class SessionComponentDeployer extends AbstractComponentDeployer {

    /**
     * @param componentDef
     */
    public SessionComponentDeployer(ComponentDef componentDef) {
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
            throw new EmptyRuntimeException("externalContext");
        }
        Map sessionMap = extCtx.getSessionMap();
        String componentName = getComponentName();
        Object old = sessionMap.get(componentName);
        if (old != null && old.getClass().equals(cd.getConcreteClass())) {
            return old;
        }
        Object component = getConstructorAssembler().assemble();
        if (old != null) {
            copyProperties(old, component);
        } else {
            getPropertyAssembler().assemble(component);
            getInitMethodAssembler().assemble(component);
        }
        sessionMap.put(componentName, component);
        return component;
    }
    
    protected void copyProperties(Object old, Object component) {
        BeanDesc oldBeanDesc = BeanDescFactory.getBeanDesc(old.getClass());
        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(component.getClass());
        for (int i = 0; i < beanDesc.getPropertyDescSize(); ++i) {
            PropertyDesc pd = beanDesc.getPropertyDesc(i);
            if (!pd.hasWriteMethod()) {
                continue;
            }
            PropertyDesc oldPd = oldBeanDesc.getPropertyDesc(pd.getPropertyName());
            if (!pd.hasReadMethod()) {
                continue;
            }
            pd.setValue(component, oldPd.getValue(old));
        }
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