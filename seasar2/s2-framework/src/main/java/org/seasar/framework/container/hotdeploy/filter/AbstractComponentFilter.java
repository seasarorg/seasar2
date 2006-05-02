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
package org.seasar.framework.container.hotdeploy.filter;

import org.seasar.framework.container.AutoBindingDef;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.InstanceDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.AnnotationHandler;
import org.seasar.framework.container.factory.AnnotationHandlerFactory;
import org.seasar.framework.container.hotdeploy.ComponentFilter;
import org.seasar.framework.container.hotdeploy.ComponentFilterContainer;

public abstract class AbstractComponentFilter implements ComponentFilter {

    public final String instanceDef_BINDING = "bindingType=may";
    
    private InstanceDef instanceDef;
    
    public final String autoBindingDef_BINDING = "bindingType=may";
    
    private AutoBindingDef autoBindingDef;
    
    private ComponentFilterContainer componentFilterContainer;
    
    private String nameSuffix;
    
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

    public ComponentFilterContainer getComponentFilterContainer() {
        return componentFilterContainer;
    }

    public void setComponentFilterContainer(
            ComponentFilterContainer componentFilterContainer) {
        this.componentFilterContainer = componentFilterContainer;
    }
    
    public String getRootPackageName() {
        return componentFilterContainer.getRootPackageName();
    }
    
    public String getNameSuffix() {
        return nameSuffix;
    }

    public void setNameSuffix(String nameSuffix) {
        this.nameSuffix = nameSuffix;
    }

    public ComponentDef createComponentDef(S2Container container, Class clazz) {
        if (!isTarget(clazz)) {
            return null;
        }
        return doCreateComponentDef(container, clazz);
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

    protected ComponentDef doCreateComponentDef(S2Container container, Class clazz) {
        Class targetClass = getTargetClass(clazz);
        AnnotationHandler handler = AnnotationHandlerFactory.getAnnotationHandler();
        return handler.createComponentDef(targetClass, instanceDef, autoBindingDef);
    }
    
    public ComponentDef createComponentDef(S2Container container, String componentName) {
        if (!isTarget(componentName)) {
            return null;
        }
        return doCreateComponentDef(container, componentName);
    }
    
    protected boolean isTarget(String componentName) {
        if (nameSuffix != null) {
            return componentName.endsWith(nameSuffix);
        }
        return true;
    }

    protected ComponentDef doCreateComponentDef(S2Container container, String componentName) {
        return doCreateComponentDef(container, getTargetClass(componentName));
    }
    
    protected abstract Class getTargetClass(Class clazz);
    
    protected abstract Class getTargetClass(String componentName);
    
    protected void concatName(StringBuffer sb, String name) {
        if (name != null) {
            if (sb.length() > 0) {
                sb.append('.');
            }
            sb.append(name);
        }
    }
}