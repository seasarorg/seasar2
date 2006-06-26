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
package org.seasar.framework.container.autoregister;

import java.util.ArrayList;
import java.util.List;

import org.seasar.framework.container.AutoBindingDef;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.InstanceDef;
import org.seasar.framework.container.factory.AnnotationHandler;
import org.seasar.framework.container.factory.AnnotationHandlerFactory;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.ClassTraversal.ClassHandler;

/**
 * @author higa
 *
 */
public abstract class AbstractComponentAutoRegister extends
        AbstractAutoRegister implements ClassHandler {

    protected static final String CLASS_SUFFIX = ".class";

    private AutoNaming autoNaming = new DefaultAutoNaming();

    private InstanceDef instanceDef;

    private AutoBindingDef autoBindingDef;

    private boolean externalBinding = false;

    private List customizers = new ArrayList();

    public AutoNaming getAutoNaming() {
        return autoNaming;
    }

    public static final String autoNaming_BINDING = "bindingType=may";

    public void setAutoNaming(AutoNaming autoNaming) {
        this.autoNaming = autoNaming;
    }

    public InstanceDef getInstanceDef() {
        return instanceDef;
    }

    public static final String instanceDef_BINDING = "bindingType=may";

    public void setInstanceDef(InstanceDef instanceDef) {
        this.instanceDef = instanceDef;
    }

    public static final String autoBindingDef_BINDING = "bindingType=may";

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

    public int getCustomizerSize() {
        return customizers.size();
    }

    public ComponentCustomizer getCustomizer(int index) {
        return (ComponentCustomizer) customizers.get(index);
    }

    public void addCustomizer(ComponentCustomizer customizer) {
        customizers.add(customizer);
    }

    public void processClass(final String packageName,
            final String shortClassName) {
        if (isIgnore(packageName, shortClassName)) {
            return;
        }

        for (int i = 0; i < getClassPatternSize(); ++i) {
            final ClassPattern cp = getClassPattern(i);
            if (cp.isAppliedPackageName(packageName)
                    && cp.isAppliedShortClassName(shortClassName)) {
                register(packageName, shortClassName);
            }
        }
    }

    protected void register(final String packageName,
            final String shortClassName) {
        final AnnotationHandler annoHandler = AnnotationHandlerFactory
                .getAnnotationHandler();
        final String className = ClassUtil.concatName(packageName,
                shortClassName);
        final ComponentDef cd = annoHandler.createComponentDef(className,
                instanceDef, autoBindingDef, externalBinding);
        if (cd.getComponentName() == null && autoNaming != null) {
            cd.setComponentName(autoNaming.defineName(packageName,
                    shortClassName));
        }
        annoHandler.appendDI(cd);
        annoHandler.appendAspect(cd);
        annoHandler.appendInitMethod(cd);
        customize(cd);
        getContainer().register(cd);
    }

    protected void customize(ComponentDef componentDef) {
        for (int i = 0; i < getCustomizerSize(); ++i) {
            ComponentCustomizer customizer = getCustomizer(i);
            customizer.customize(componentDef);
        }
    }

}