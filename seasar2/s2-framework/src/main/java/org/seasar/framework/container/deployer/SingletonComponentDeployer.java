/*
 * Copyright 2004-2005 the Seasar Foundation and the Others.
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

    private Object component_;
    
    private Object hotswapTarget_;
    
    private Hotswap hotswap_;

    private boolean instantiating_ = false;

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
        if (component_ == null) {
            assemble();
        }
        return component_;
    }

    public void injectDependency(Object component) {
        throw new UnsupportedOperationException("injectDependency");
    }

    private void assemble() {
        if (instantiating_) {
            throw new CyclicReferenceRuntimeException(getComponentDef()
                    .getComponentClass());
        }
        instantiating_ = true;
        try {
            Object o = getConstructorAssembler().assemble();
            if (hotswap_ != null) {
                hotswapTarget_ = o;
                if (component_ == null) {
                    component_ = HotswapProxy.create(getComponentDef()
                            .getComponentClass(), this, Thread.currentThread().getContextClassLoader());
                }
            } else {
                component_ = o;
            }
        } finally {
            instantiating_ = false;
        }
        getPropertyAssembler().assemble(getTarget());
        getInitMethodAssembler().assemble(getTarget());
    }
    
    protected Object getTarget() {
        return hotswapTarget_ != null ? hotswapTarget_ : component_;
    }

    /**
     * @see org.seasar.framework.container.ComponentDeployer#init()
     */
    public void init() {
        hotswap_ = getComponentDef().getHotswap();
        if (hotswap_ != null && !isAppliedHotswap()) {
            hotswap_ = null;
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
        if (component_ == null) {
            return;
        }
        getDestroyMethodAssembler().assemble(getTarget());
        component_ = null;
        hotswapTarget_ = null;
    }

    public synchronized Object updateTarget() {
        if (hotswap_.isModified()) {
            assemble();
        }
        return hotswapTarget_;
    }
    
    
}
