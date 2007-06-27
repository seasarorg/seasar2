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
package org.seasar.framework.container.creator;

import java.lang.reflect.Modifier;

import org.seasar.framework.container.AutoBindingDef;
import org.seasar.framework.container.ComponentCreator;
import org.seasar.framework.container.ComponentCustomizer;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.InstanceDef;
import org.seasar.framework.container.factory.AnnotationHandler;
import org.seasar.framework.container.factory.AnnotationHandlerFactory;
import org.seasar.framework.convention.NamingConvention;
import org.seasar.framework.exception.EmptyRuntimeException;

/**
 * {@link ComponentCreator}の実装です。
 * 
 * @author higa
 * 
 */
public class ComponentCreatorImpl implements ComponentCreator {

    private NamingConvention namingConvention;

    /**
     * Bindingアノテーションの定義です。
     */
    public static final String instanceDef_BINDING = "bindingType=may";

    private InstanceDef instanceDef;

    /**
     * Bindingアノテーションの定義です。
     */
    public static final String autoBindingDef_BINDING = "bindingType=may";

    private AutoBindingDef autoBindingDef;

    /**
     * Bindingアノテーションの定義です。
     */
    public static final String externalBinding_BINDING = "bindingType=may";

    private boolean externalBinding = false;

    /**
     * Bindingアノテーションの定義です。
     */
    public static final String enableInterface_BINDING = "bindingType=may";

    private boolean enableInterface = false;

    /**
     * Bindingアノテーションの定義です。
     */
    public static final String enableAbstract_BINDING = "bindingType=may";

    private boolean enableAbstract = false;

    private String nameSuffix;

    private ComponentCustomizer customizer;

    /**
     * {@link ComponentCreatorImpl}を作成します。
     * 
     * @param namingConvention
     */
    public ComponentCreatorImpl(NamingConvention namingConvention) {
        if (namingConvention == null) {
            throw new EmptyRuntimeException("namingConvetion");
        }
        this.namingConvention = namingConvention;
    }

    /**
     * {@link NamingConvention}を返します。
     * 
     * @return {@link NamingConvention}
     */
    public NamingConvention getNamingConvention() {
        return namingConvention;
    }

    /**
     * {@link InstanceDef}を返します。
     * 
     * @return {@link InstanceDef}
     */
    public InstanceDef getInstanceDef() {
        return instanceDef;
    }

    /**
     * {@link InstanceDef}を設定します。
     * 
     * @param instanceDef
     */
    public void setInstanceDef(InstanceDef instanceDef) {
        this.instanceDef = instanceDef;
    }

    /**
     * {@link AutoBindingDef}を返します。
     * 
     * @return {@link AutoBindingDef}
     */
    public AutoBindingDef getAutoBindingDef() {
        return autoBindingDef;
    }

    /**
     * {@link AutoBindingDef}を設定します。
     * 
     * @param autoBindingDef
     */
    public void setAutoBindingDef(AutoBindingDef autoBindingDef) {
        this.autoBindingDef = autoBindingDef;
    }

    /**
     * デフォルトで外部バインディングをするかどうかを返します。
     * 
     * @return デフォルトで外部バインディングをするかどうか
     */
    public boolean isExternalBinding() {
        return externalBinding;
    }

    /**
     * デフォルトで外部バインディングをするかどうかを設定します。
     * 
     * @param externalBinding
     */
    public void setExternalBinding(boolean externalBinding) {
        this.externalBinding = externalBinding;
    }

    /**
     * インターフェースだけしかないクラスを可能にするかどうかを返します。インターフェースだけしかない場合は、AOP(AspectCustomzier)で実装クラスが作られるでしょう。
     * 
     * @return インターフェースだけしかないクラスを可能にするかどうか
     */
    public boolean isEnableInterface() {
        return enableInterface;
    }

    /**
     * インターフェースしかないクラスを可能にするかどうかを設定します。
     * 
     * @param enableInterface
     */
    public void setEnableInterface(boolean enableInterface) {
        this.enableInterface = enableInterface;
    }

    /**
     * 抽象クラスを可能にするかどうかを返します。抽象クラスの場合、AOP(AspectCustomzier)で実装クラスが作られるでしょう。
     * 
     * @return 抽象クラスを可能にするかどうか
     */
    public boolean isEnableAbstract() {
        return enableAbstract;
    }

    /**
     * 抽象クラスを可能にするかどうかを設定します。
     * 
     * @param enableAbstract
     */
    public void setEnableAbstract(boolean enableAbstract) {
        this.enableAbstract = enableAbstract;
    }

    /**
     * 名前のサフィックスを返します。
     * 
     * @return 名前のサフィックス
     */
    public String getNameSuffix() {
        return nameSuffix;
    }

    /**
     * 名前のサフィックスを設定します。
     * 
     * @param nameSuffix
     */
    public void setNameSuffix(String nameSuffix) {
        this.nameSuffix = nameSuffix;
    }

    /**
     * {@link ComponentCustomizer}を返します。
     * 
     * @return {@link ComponentCustomizer}
     */
    protected ComponentCustomizer getCustomizer() {
        return customizer;
    }

    /**
     * {@link ComponentCustomizer}を設定します。
     * 
     * @param customizer
     */
    protected void setCustomizer(ComponentCustomizer customizer) {
        this.customizer = customizer;
    }

    public ComponentDef createComponentDef(Class componentClass) {
        if (!namingConvention.isTargetClassName(componentClass.getName(),
                nameSuffix)) {
            return null;
        }
        Class targetClass = namingConvention.toCompleteClass(componentClass);
        if (targetClass.isInterface()) {
            if (!isEnableInterface()) {
                return null;
            }
        } else if (Modifier.isAbstract(targetClass.getModifiers())) {
            if (!isEnableAbstract()) {
                return null;
            }
        }
        AnnotationHandler handler = AnnotationHandlerFactory
                .getAnnotationHandler();
        ComponentDef cd = handler.createComponentDef(targetClass, instanceDef,
                autoBindingDef, externalBinding);
        if (cd.getComponentName() == null) {
            cd.setComponentName(namingConvention
                    .fromClassNameToComponentName(targetClass.getName()));
        }
        handler.appendDI(cd);
        customize(cd);
        handler.appendInitMethod(cd);
        handler.appendDestroyMethod(cd);
        handler.appendAspect(cd);
        handler.appendInterType(cd);
        return cd;
    }

    public ComponentDef createComponentDef(String componentName) {
        if (!isTargetComponentName(componentName)) {
            return null;
        }
        Class componentClass = namingConvention
                .fromComponentNameToClass(componentName);
        if (componentClass == null) {
            return null;
        }
        return createComponentDef(componentClass);
    }

    /**
     * 対象となるコンポーネント名かどうかを返します。
     * 
     * @param componentName
     * @return 対象となるコンポーネント名かどうか
     */
    public boolean isTargetComponentName(String componentName) {
        return componentName.endsWith(nameSuffix);
    }

    /**
     * {@link ComponentDef}をカスタマイズします。
     * 
     * @param componentDef
     */
    protected void customize(ComponentDef componentDef) {
        if (customizer != null) {
            customizer.customize(componentDef);
        }
    }
}