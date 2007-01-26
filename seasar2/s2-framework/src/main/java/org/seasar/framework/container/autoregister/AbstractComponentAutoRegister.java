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
package org.seasar.framework.container.autoregister;

import org.seasar.framework.container.AutoBindingDef;
import org.seasar.framework.container.ComponentCustomizer;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.InstanceDef;
import org.seasar.framework.container.factory.AnnotationHandler;
import org.seasar.framework.container.factory.AnnotationHandlerFactory;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.ClassTraversal.ClassHandler;

/**
 * コンポーネントを自動登録するための抽象クラスです。
 * 
 * @author higa
 * 
 */
public abstract class AbstractComponentAutoRegister extends
        AbstractAutoRegister implements ClassHandler {

    protected static final String CLASS_SUFFIX = ".class";

    /**
     * BINDINGアノテーションの定義です。
     */
    public static final String autoNaming_BINDING = "bindingType=may";

    private AutoNaming autoNaming = new DefaultAutoNaming();

    /**
     * BINDINGアノテーションの定義です。
     */
    public static final String instanceDef_BINDING = "bindingType=may";

    private InstanceDef instanceDef;

    /**
     * BINDINGアノテーションの定義です。
     */
    public static final String autoBindingDef_BINDING = "bindingType=may";

    private AutoBindingDef autoBindingDef;

    private boolean externalBinding = false;

    /**
     * BINDINGアノテーションの定義です。
     */
    public static final String customizer_BINDING = "bindingType=may";

    private ComponentCustomizer customizer;

    /**
     * AutoNamingを返します。
     * 
     * @return
     */
    public AutoNaming getAutoNaming() {
        return autoNaming;
    }

    /**
     * AutoNamingを設定します。
     * 
     * @param autoNaming
     */
    public void setAutoNaming(AutoNaming autoNaming) {
        this.autoNaming = autoNaming;
    }

    /**
     * インスタンス定義を返します。
     * 
     * @return
     */
    public InstanceDef getInstanceDef() {
        return instanceDef;
    }

    /**
     * インスタンス定義を設定します。
     * 
     * @param instanceDef
     */
    public void setInstanceDef(InstanceDef instanceDef) {
        this.instanceDef = instanceDef;
    }

    /**
     * 自動バインディング定義を返します。
     * 
     * @return
     */
    public AutoBindingDef getAutoBindingDef() {
        return autoBindingDef;
    }

    /**
     * 自動バインディング定義を設定します。
     * 
     * @param autoBindingDef
     */
    public void setAutoBindingDef(AutoBindingDef autoBindingDef) {
        this.autoBindingDef = autoBindingDef;
    }

    /**
     * 外部バインディングのデフォルト値を返します。
     * 
     * @return
     */
    public boolean isExternalBinding() {
        return externalBinding;
    }

    /**
     * 外部バインディングのデフォルト値を設定します。
     * 
     * @param externalBinding
     */
    public void setExternalBinding(boolean externalBinding) {
        this.externalBinding = externalBinding;
    }

    /**
     * コンポーネントカスタマイザを返します。
     * 
     * @return
     */
    public ComponentCustomizer getCustomizer() {
        return customizer;
    }

    /**
     * コンポーネントカスタマイザを設定します。
     * 
     * @param customizer
     */
    public void setCustomizer(ComponentCustomizer customizer) {
        this.customizer = customizer;
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
                register(ClassUtil.concatName(packageName, shortClassName));
            }
        }
    }

    protected void register(final String className) {
        final AnnotationHandler annoHandler = AnnotationHandlerFactory
                .getAnnotationHandler();
        final ComponentDef cd = annoHandler.createComponentDef(className,
                instanceDef, autoBindingDef, externalBinding);
        if (cd.getComponentName() == null) {
            String[] names = ClassUtil.splitPackageAndShortClassName(className);
            cd.setComponentName(autoNaming.defineName(names[0], names[1]));
        }
        annoHandler.appendDI(cd);
        customize(cd);
        annoHandler.appendInitMethod(cd);
        annoHandler.appendDestroyMethod(cd);
        annoHandler.appendAspect(cd);
        annoHandler.appendInterType(cd);
        getContainer().register(cd);
    }

    protected void customize(ComponentDef componentDef) {
        if (customizer != null) {
            customizer.customize(componentDef);
        }
    }
}