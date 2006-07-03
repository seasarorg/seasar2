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
import org.seasar.framework.container.autoregister.ComponentCustomizer;
import org.seasar.framework.container.factory.AnnotationHandler;
import org.seasar.framework.container.factory.AnnotationHandlerFactory;
import org.seasar.framework.container.hotdeploy.OndemandCreator;
import org.seasar.framework.container.hotdeploy.OndemandS2Container;
import org.seasar.framework.convention.NamingConvention;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.ResourceUtil;

public abstract class AbstractOndemandCreator implements OndemandCreator {

    private NamingConvention namingConvention;

    public static final String instanceDef_BINDING = "bindingType=may";

    private InstanceDef instanceDef;

    public static final String autoBindingDef_BINDING = "bindingType=may";

    private AutoBindingDef autoBindingDef;

    private boolean externalBinding = false;

    private String nameSuffix;

    private List customizers = new ArrayList();

    public AbstractOndemandCreator(NamingConvention namingConvention) {
        if (namingConvention == null) {
            throw new EmptyRuntimeException("namingConvetion");
        }
        this.namingConvention = namingConvention;
    }

    public NamingConvention getNamingConvention() {
        return namingConvention;
    }

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

    public boolean isExternalBinding() {
        return externalBinding;
    }

    public void setExternalBinding(boolean externalBinding) {
        this.externalBinding = externalBinding;
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

    public ComponentCustomizer getCustomizer(int index) {
        return (ComponentCustomizer) customizers.get(index);
    }

    public void addCustomizer(ComponentCustomizer customizer) {
        customizers.add(customizer);
    }

    public boolean loadComponentDef(OndemandS2Container container,
            String subsystemPackageName, Class clazz) {
        if (!isTarget(subsystemPackageName, clazz)) {
            return false;
        }
        Class targetClass = getTargetClass(clazz);
        ComponentDef cd = container.getComponentDef(targetClass);
        if (cd != null) {
            return true;
        }
        AnnotationHandler handler = AnnotationHandlerFactory
                .getAnnotationHandler();
        cd = handler.createComponentDef(targetClass, instanceDef,
                autoBindingDef, externalBinding);
        customize(cd);
        if (cd.getComponentName() == null) {
            cd.setComponentName(composeComponentName(clazz.getName()));
        }
        container.register(cd);
        cd.init();
        return true;
    }

    protected boolean isTarget(String subsystemPackageName, Class clazz) {
        String className = clazz.getName();
        if (!isTargetMiddlePackage(subsystemPackageName, className)) {
            return false;
        }
        return isTarget(className);
    }

    protected abstract boolean isTargetMiddlePackage(
            String subsystemPackageName, String className);

    protected String composeComponentName(String className) {
        return getNamingConvention().fromClassNameToComponentName(className);
    }

    public ComponentDef getComponentDef(OndemandS2Container container,
            String subsystemPackageName, Class clazz) {
        if (!isTarget(subsystemPackageName, clazz)) {
            return null;
        }
        Class targetClass = getTargetClass(clazz);
        return container.getComponentDef(targetClass);
    }

    public ComponentDef getComponentDef(OndemandS2Container container,
            String subsystemPackageName, String componentName) {
        if (!isTarget(componentName)) {
            return null;
        }
        Class targetClass = getTargetClass(subsystemPackageName, componentName);
        return container.getComponentDef(targetClass);
    }

    protected boolean isTarget(String componentName) {
        if (nameSuffix != null) {
            return componentName.endsWith(nameSuffix);
        }
        return true;
    }

    protected Class getTargetClass(Class clazz) {
        if (!clazz.isInterface()) {
            return clazz;
        }
        String packageName = ClassUtil.getPackageName(clazz);
        String targetClassName = packageName + "." + getNamingConvention().getImplementationPackageName()
                + "." + ClassUtil.getShortClassName(clazz)
                + getNamingConvention().getImplementationSuffix();
        if (ResourceUtil.getResourceAsFileNoException(ClassUtil
                .getResourcePath(targetClassName)) != null) {
            return ClassUtil.forName(targetClassName);
        }
        return clazz;
    }

    protected abstract Class getTargetClass(String subsystemPackageName,
            String componentName);

    protected void customize(ComponentDef componentDef) {
        for (int i = 0; i < getCustomizerSize(); ++i) {
            ComponentCustomizer customizer = getCustomizer(i);
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