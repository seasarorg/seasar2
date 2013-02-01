/*
 * Copyright 2004-2013 the Seasar Foundation and the Others.
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
import org.seasar.framework.container.ComponentDeployer;
import org.seasar.framework.container.CyclicReferenceRuntimeException;

/**
 * singleton用の{@link ComponentDeployer}です。
 * 
 * @author higa
 * 
 */
public class SingletonComponentDeployer extends AbstractComponentDeployer {

    private Object component;

    private boolean instantiating = false;

    /**
     * {@link SingletonComponentDeployer}を作成します。
     * 
     * @param componentDef
     */
    public SingletonComponentDeployer(ComponentDef componentDef) {
        super(componentDef);
    }

    /**
     * @see org.seasar.framework.container.ComponentDeployer#deploy()
     */
    public Object deploy() {
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
            component = getConstructorAssembler().assemble();
        } finally {
            instantiating = false;
        }
        getPropertyAssembler().assemble(component);
        getInitMethodAssembler().assemble(component);
    }

    /**
     * @see org.seasar.framework.container.ComponentDeployer#init()
     */
    public void init() {
        deploy();
    }

    /**
     * @see org.seasar.framework.container.ComponentDeployer#destroy()
     */
    public void destroy() {
        if (component == null) {
            return;
        }
        getDestroyMethodAssembler().assemble(component);
        component = null;
    }
}