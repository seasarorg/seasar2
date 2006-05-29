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
package org.seasar.framework.container.hotdeploy.creator;

import java.util.ArrayList;
import java.util.List;

import org.seasar.framework.container.AutoBindingDef;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.InstanceDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.autoregister.ComponentDefCustomizer;
import org.seasar.framework.container.factory.AnnotationHandler;
import org.seasar.framework.container.factory.AnnotationHandlerFactory;
import org.seasar.framework.container.hotdeploy.OndemandCreator;
import org.seasar.framework.container.hotdeploy.OndemandCreatorContainer;

public abstract class AbstractOndemandCreator implements OndemandCreator {

    public static final String instanceDef_BINDING = "bindingType=may";

    private InstanceDef instanceDef;

    public static final String autoBindingDef_BINDING = "bindingType=may";

    private AutoBindingDef autoBindingDef;

    private OndemandCreatorContainer ondemandCreatorContainer;

    private String nameSuffix;

    private List customizers = new ArrayList();

    public InstanceDef getInstanceDef() {
        return instanceDef;
    }

    public void setInstanceDef(InstanceDef instanceDef) {
        this.instanceDef = instanceDef;
    }

    public AutoBindingDef getAutoBindingDef() {
        return autoBindingDef;
    }

    public void setAutoBindingDef(AutoBindingDef autoBindingDef) {
        this.autoBindingDef = autoBindingDef;
    }

    public OndemandCreatorContainer getOndemandCreatorContainer() {
        return ondemandCreatorContainer;
    }

    public void setOndemandCreatorContainer(
            OndemandCreatorContainer ondemandCreatorContainer) {
        this.ondemandCreatorContainer = ondemandCreatorContainer;
    }

    public String getRootPackageName() {
        return ondemandCreatorContainer.getRootPackageName();
    }

    public String getNameSuffix() {
        return nameSuffix;
    }

    public void setNameSuffix(String nameSuffix) {
        this.nameSuffix = nameSuffix;
    }

    public int getCustomizerSize() {
        return customizers.size();
    }

    public ComponentDefCustomizer getCustomizer(int index) {
        return (ComponentDefCustomizer) customizers.get(index);
    }

    public void addCustomizer(ComponentDefCustomizer customizer) {
        customizers.add(customizer);
    }

    public boolean loadComponentDef(S2Container container, Class clazz) {
        if (!isTarget(clazz)) {
            return false;
        }
        Class targetClass = getTargetClass(clazz);
        ComponentDef cd = ondemandCreatorContainer.getComponentDef(targetClass);
        if (cd != null) {
            return true;
        }
        AnnotationHandler handler = AnnotationHandlerFactory
                .getAnnotationHandler();
        cd = handler.createComponentDef(targetClass, instanceDef,
                autoBindingDef);
        customize(cd);
        if (cd.getComponentName() == null) {
            cd.setComponentName(composeComponentName(clazz.getName()));
        }
        ondemandCreatorContainer.register(cd);
        cd.init();
        return true;
    }

    protected boolean isTarget(Class clazz) {
        String className = clazz.getName();
        if (!isTargetMiddlePackage(className)) {
            return false;
        }
        if (nameSuffix != null) {
            return className.endsWith(nameSuffix);
        }
        return true;
    }

    protected abstract boolean isTargetMiddlePackage(String className);

    protected abstract String composeComponentName(String className);

    public ComponentDef getComponentDef(S2Container container, Class clazz) {
        if (!isTarget(clazz)) {
            return null;
        }
        Class targetClass = getTargetClass(clazz);
        return ondemandCreatorContainer.getComponentDef(targetClass);
    }

    public ComponentDef getComponentDef(S2Container container,
            String componentName) {
        if (!isTarget(componentName)) {
            return null;
        }
        Class targetClass = getTargetClass(componentName);
        return ondemandCreatorContainer.getComponentDef(targetClass);
    }

    protected boolean isTarget(String componentName) {
        if (nameSuffix != null) {
            return componentName.endsWith(nameSuffix);
        }
        return true;
    }

    protected abstract Class getTargetClass(Class clazz);

    protected abstract Class getTargetClass(String componentName);

    protected void customize(ComponentDef componentDef) {
        for (int i = 0; i < getCustomizerSize(); ++i) {
            ComponentDefCustomizer customizer = getCustomizer(i);
            customizer.customize(componentDef);
        }
    }

    protected void concatName(StringBuffer sb, String name) {
        if (name != null) {
            if (sb.length() > 0) {
                sb.append('.');
            }
            sb.append(name);
        }
    }
}