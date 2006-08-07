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
package org.seasar.framework.container.cooldeploy.creator;

import org.seasar.framework.container.AutoBindingDef;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.InstanceDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.autoregister.ComponentCustomizer;
import org.seasar.framework.container.cooldeploy.CoolCreator;
import org.seasar.framework.container.factory.AnnotationHandler;
import org.seasar.framework.container.factory.AnnotationHandlerFactory;
import org.seasar.framework.convention.NamingConvention;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.ResourceUtil;

public abstract class AbstractCoolCreator implements CoolCreator {

    private NamingConvention namingConvention;

    public static final String instanceDef_BINDING = "bindingType=may";

    private InstanceDef instanceDef;

    public static final String autoBindingDef_BINDING = "bindingType=may";

    private AutoBindingDef autoBindingDef;

    private boolean externalBinding = false;

    private String nameSuffix;

    private ComponentCustomizer customizer;

    private S2Container container;

    public AbstractCoolCreator(NamingConvention namingConvention) {
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

    protected ComponentCustomizer getCustomizer() {
        return customizer;
    }

    protected void setCustomizer(ComponentCustomizer customizer) {
        this.customizer = customizer;
    }

    public S2Container getContainer() {
        return container;
    }

    public void setContainer(S2Container container) {
        this.container = container;
    }

    public boolean loadComponentDef(String rootPackageName, Class clazz) {
        if (!isTargetClassName(rootPackageName, clazz.getName())) {
            return false;
        }
        Class targetClass = getTargetClass(clazz);
        if (targetClass == null) {
            return true;
        }
        AnnotationHandler handler = AnnotationHandlerFactory
                .getAnnotationHandler();
        ComponentDef cd = handler.createComponentDef(targetClass, instanceDef,
                autoBindingDef, externalBinding);
        if (cd.getComponentName() == null) {
            cd.setComponentName(composeComponentName(clazz.getName()));
        }
        handler.appendDI(cd);
        handler.appendAspect(cd);
        handler.appendInitMethod(cd);
        customize(cd);
        container.getRoot().register(cd);
        return true;
    }

    protected boolean isTargetClassName(String rootPackageName, String className) {
        return isTargetMiddlePackage(rootPackageName, className)
                && isAppliedSuffix(className);
    }

    protected abstract boolean isTargetMiddlePackage(String rootPackageName,
            String className);

    protected String composeComponentName(String className) {
        return getNamingConvention().fromClassNameToComponentName(className);
    }

    protected boolean isAppliedSuffix(String name) {
        if (nameSuffix == null) {
            return true;
        }
        if (name.endsWith(nameSuffix)) {
            return true;
        }
        if (name.endsWith(nameSuffix
                + getNamingConvention().getImplementationSuffix())) {
            return true;
        }
        return false;
    }

    protected Class getTargetClass(Class clazz) {
        if (!clazz.isInterface()) {
            return clazz;
        }
        String packageName = ClassUtil.getPackageName(clazz);
        String targetClassName = packageName + "."
                + getNamingConvention().getImplementationPackageName() + "."
                + ClassUtil.getShortClassName(clazz)
                + getNamingConvention().getImplementationSuffix();
        if (ResourceUtil.getResourceNoException(ClassUtil
                .getResourcePath(targetClassName)) != null) {
            return null;
        }
        return clazz;
    }

    protected void customize(ComponentDef componentDef) {
        if (customizer != null) {
            customizer.customize(componentDef);
        }
    }
}