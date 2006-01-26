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

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.CyclicReferenceRuntimeException;
import org.seasar.framework.hotswap.Hotswap;
import org.seasar.framework.hotswap.HotswapProxy;
import org.seasar.framework.hotswap.HotswapTargetFactory;

/**
 * @author higa
 * 
 */
public class SingletonComponentDeployer extends AbstractComponentDeployer
    implements HotswapTargetFactory {

    private Object component;
    
    private Object hotswapTarget;
    
    private Hotswap hotswap;

    private boolean instantiating = false;

    /**
     * @param componentDef
     */
    public SingletonComponentDeployer(ComponentDef componentDef) {
        super(componentDef);
    }

    /**
     * @see org.seasar.framework.container.ComponentDeployer#deploy()
     */
    public synchronized Object deploy() {
        if (component == null) {
            assemble();
        }
        return component;
    }

    public void injectDependency(Object component) {
        throw new UnsupportedOperationException("injectDependency");
    }

    private void assemble() {
        if (instantiating) {
            throw new CyclicReferenceRuntimeException(getComponentDef()
                    .getComponentClass());
        }
        instantiating = true;
        try {
            Object o = getConstructorAssembler().assemble();
            if (hotswap != null) {
                hotswapTarget = o;
                if (component == null) {
                    component = HotswapProxy.create(getComponentDef()
                            .getComponentClass(), this, Thread.currentThread().getContextClassLoader());
                }
            } else {
                component = o;
            }
        } finally {
            instantiating = false;
        }
        getPropertyAssembler().assemble(getTarget());
        getInitMethodAssembler().assemble(getTarget());
    }
    
    protected Object getTarget() {
        return hotswapTarget != null ? hotswapTarget : component;
    }

    /**
     * @see org.seasar.framework.container.ComponentDeployer#init()
     */
    public void init() {
        hotswap = getComponentDef().getHotswap();
        if (hotswap != null && !isAppliedHotswap()) {
            hotswap = null;
        }
        deploy();
    }
    
    protected boolean isAppliedHotswap() {
        Class clazz = getComponentDef().getComponentClass();
        if (clazz == null) {
            return false;
        }
        Class[] itfs = clazz.getInterfaces();
        if (itfs == null) {
            return false;
        }
        for (int i = 0; i < itfs.length; ++i) {
            if (itfs[i].getMethods().length > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * @see org.seasar.framework.container.ComponentDeployer#destroy()
     */
    public void destroy() {
        if (component == null) {
            return;
        }
        getDestroyMethodAssembler().assemble(getTarget());
        component = null;
        hotswapTarget = null;
    }

    public synchronized Object updateTarget() {
        if (hotswap.isModified()) {
            assemble();
        }
        return hotswapTarget;
    }
    
    
}
