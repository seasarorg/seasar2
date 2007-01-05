/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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
package org.seasar.framework.container.factory.component;

import org.seasar.framework.container.AutoBindingDef;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.InstanceDef;
import org.seasar.framework.container.annotation.tiger.AutoBindingType;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
import org.seasar.framework.container.assembler.AutoBindingDefFactory;
import org.seasar.framework.container.deployer.InstanceDefFactory;
import org.seasar.framework.container.factory.AnnotationHandler;
import org.seasar.framework.container.factory.ComponentDefBuilder;
import org.seasar.framework.container.impl.ComponentDefImpl;
import org.seasar.framework.util.StringUtil;

public class PojoComponentDefBuilder implements ComponentDefBuilder {

    public PojoComponentDefBuilder() {
    }

    public ComponentDef createComponentDef(
            final AnnotationHandler annotationHandler,
            final Class<?> componentClass,
            final InstanceDef defaultInstanceDef,
            final AutoBindingDef defaultAutoBindingDef,
            final boolean defaultExternalBinding) {
        final Component component = componentClass
                .getAnnotation(Component.class);
        if (component == null) {
            return null;
        }

        final ComponentDef componentDef = new ComponentDefImpl(componentClass);
        if (!StringUtil.isEmpty(component.name())) {
            componentDef.setComponentName(component.name());
        }
        componentDef.setInstanceDef(getInstanceDef(component,
                defaultInstanceDef));
        componentDef.setAutoBindingDef(getAutoBindingDef(component,
                defaultAutoBindingDef));
        componentDef.setExternalBinding(component.externalBinding());
        return componentDef;
    }

    protected InstanceDef getInstanceDef(final Component component,
            final InstanceDef defaultInstanceDef) {
        final InstanceType instanceType = component.instance();
        if (instanceType == null || StringUtil.isEmpty(instanceType.getName())) {
            return defaultInstanceDef;
        }
        return InstanceDefFactory.getInstanceDef(instanceType.getName());
    }

    protected AutoBindingDef getAutoBindingDef(final Component component,
            final AutoBindingDef defaultAutoBindingDef) {
        final AutoBindingType autoBindingType = component.autoBinding();
        if (autoBindingType == null
                || StringUtil.isEmpty(autoBindingType.getName())) {
            return defaultAutoBindingDef;
        }
        return AutoBindingDefFactory.getAutoBindingDef(autoBindingType
                .getName());
    }
}
