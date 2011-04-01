/*
 * Copyright 2004-2011 the Seasar Foundation and the Others.
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

import org.seasar.framework.container.ClassUnmatchRuntimeException;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.ComponentDeployer;

/**
 * outer用の{@link ComponentDeployer}です。
 * 
 * @author higa
 * 
 */
public class OuterComponentDeployer extends AbstractComponentDeployer {

    /**
     * {@link OuterComponentDeployer}を作成します。
     * 
     * @param componentDef
     */
    public OuterComponentDeployer(ComponentDef componentDef) {
        super(componentDef);
    }

    /**
     * @see org.seasar.framework.container.ComponentDeployer#deploy()
     */
    public Object deploy() {
        throw new UnsupportedOperationException("deploy");
    }

    public void injectDependency(Object outerComponent) {
        checkComponentClass(outerComponent);
        getPropertyAssembler().assemble(outerComponent);
        getInitMethodAssembler().assemble(outerComponent);
    }

    private void checkComponentClass(Object outerComponent)
            throws ClassUnmatchRuntimeException {

        Class componentClass = getComponentDef().getComponentClass();
        if (componentClass == null) {
            return;
        }
        if (!componentClass.isInstance(outerComponent)) {
            throw new ClassUnmatchRuntimeException(componentClass,
                    outerComponent.getClass());
        }
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
