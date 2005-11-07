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

import org.seasar.framework.container.AutoBindingDef;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.ComponentDeployer;
import org.seasar.framework.container.ConstructorAssembler;
import org.seasar.framework.container.MethodAssembler;
import org.seasar.framework.container.PropertyAssembler;
import org.seasar.framework.container.assembler.AssemblerFactory;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.StringUtil;

/**
 * @author higa
 * 
 */
public abstract class AbstractComponentDeployer implements ComponentDeployer {
    private ComponentDef componentDef_;

    private ConstructorAssembler constructorAssembler_;

    private PropertyAssembler propertyAssembler_;

    private MethodAssembler initMethodAssembler_;

    private MethodAssembler destroyMethodAssembler_;

    public AbstractComponentDeployer(ComponentDef componentDef) {
        componentDef_ = componentDef;
        setupAssembler();
    }

    protected final ComponentDef getComponentDef() {
        return componentDef_;
    }

    protected final ConstructorAssembler getConstructorAssembler() {
        return constructorAssembler_;
    }

    protected final PropertyAssembler getPropertyAssembler() {
        return propertyAssembler_;
    }

    protected final MethodAssembler getInitMethodAssembler() {
        return initMethodAssembler_;
    }

    protected final MethodAssembler getDestroyMethodAssembler() {
        return destroyMethodAssembler_;
    }

    protected void setupAssembler() {
        AutoBindingDef autoBindingDef = componentDef_.getAutoBindingDef();
        constructorAssembler_ = autoBindingDef
                .createConstructorAssembler(componentDef_);
        propertyAssembler_ = autoBindingDef
                .createPropertyAssembler(componentDef_);
        initMethodAssembler_ = AssemblerFactory
                .createInitMethodAssembler(componentDef_);
        destroyMethodAssembler_ = AssemblerFactory
                .createDestroyMethodAssembler(componentDef_);
    }

    protected String getComponentName() {
        String componentName = componentDef_.getComponentName();
        if (componentName == null) {
            componentName = ClassUtil.getShortClassName(componentDef_
                    .getComponentClass());
            componentName = StringUtil.decapitalize(componentName);
        }
        return componentName;
    }
}