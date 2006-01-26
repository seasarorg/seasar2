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
    
    private ComponentDef componentDef;

    private ConstructorAssembler constructorAssembler;

    private PropertyAssembler propertyAssembler;

    private MethodAssembler initMethodAssembler;

    private MethodAssembler destroyMethodAssembler;

    public AbstractComponentDeployer(ComponentDef componentDef) {
        this.componentDef = componentDef;
        setupAssembler();
    }

    protected final ComponentDef getComponentDef() {
        return componentDef;
    }

    protected final ConstructorAssembler getConstructorAssembler() {
        return constructorAssembler;
    }

    protected final PropertyAssembler getPropertyAssembler() {
        return propertyAssembler;
    }

    protected final MethodAssembler getInitMethodAssembler() {
        return initMethodAssembler;
    }

    protected final MethodAssembler getDestroyMethodAssembler() {
        return destroyMethodAssembler;
    }

    protected void setupAssembler() {
        AutoBindingDef autoBindingDef = componentDef.getAutoBindingDef();
        constructorAssembler = autoBindingDef
                .createConstructorAssembler(componentDef);
        propertyAssembler = autoBindingDef
                .createPropertyAssembler(componentDef);
        initMethodAssembler = AssemblerFactory
                .createInitMethodAssembler(componentDef);
        destroyMethodAssembler = AssemblerFactory
                .createDestroyMethodAssembler(componentDef);
    }

    protected String getComponentName() {
        String componentName = componentDef.getComponentName();
        if (componentName == null) {
            componentName = ClassUtil.getShortClassName(componentDef
                    .getComponentClass());
            componentName = StringUtil.decapitalize(componentName);
        }
        return componentName;
    }
}