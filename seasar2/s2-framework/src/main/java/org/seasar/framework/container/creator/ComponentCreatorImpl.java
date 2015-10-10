/*
 * Copyright 2004-2015 the Seasar Foundation and the Others.
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
 * {@link org.seasar.framework.container.ComponentCreator}の汎用的な実装です。
 * <p>
 * このクラスによって作られるコンポーネント定義に含める{@link InstanceDef インスタンス定義}、
 * {@link AutoBindingDef 自動バインディング定義}、
 * {@link org.seasar.framework.container.ExternalContext 外部バインディング}の有効/無効が設定できます。
 * インターフェースや抽象クラスをコンポーネント定義作成の対象とする場合、
 * AOP(AspectCustomzier)で実装クラスが作られるようにする必要があります。
 * </p>
 * <p>
 * このクラスがコンポーネント定義を作成するべきかどうかは、 コンポーネント名のサフィックスで判断しています。 コンポーネント名が{@link #setNameSuffix(String)}で設定したサフィックスに該当した場合のみ、
 * コンポーネント定義を作成します。
 * </p>
 * 
 * @author higa
 * @author jundu
 */
public class ComponentCreatorImpl implements ComponentCreator {

    private NamingConvention namingConvention;

    /**
     * プロパティ<code>instanceDef</code>のためのBindingアノテーションの定義です。
     */
    public static final String instanceDef_BINDING = "bindingType=may";

    private InstanceDef instanceDef;

    /**
     * プロパティ<code>autoBindingDef</code>のためのBindingアノテーションの定義です。
     */
    public static final String autoBindingDef_BINDING = "bindingType=may";

    private AutoBindingDef autoBindingDef;

    /**
     * プロパティ<code>externalBinding</code>のためのBindingアノテーションの定義です。
     */
    public static final String externalBinding_BINDING = "bindingType=may";

    private boolean externalBinding = false;

    /**
     * プロパティ<code>enableInterface</code>のためのBindingアノテーションの定義です。
     */
    public static final String enableInterface_BINDING = "bindingType=may";

    private boolean enableInterface = false;

    /**
     * プロパティ<code>enableAbstract</code>のためのBindingアノテーションの定義です。
     */
    public static final String enableAbstract_BINDING = "bindingType=may";

    private boolean enableAbstract = false;

    private String nameSuffix;

    private ComponentCustomizer customizer;

    /**
     * 指定された{@link NamingConvention 命名規約}に従った<code>ComponentCreatorImpl</code>を構築します。
     * 
     * @param namingConvention
     *            命名規約
     */
    public ComponentCreatorImpl(NamingConvention namingConvention) {
        if (namingConvention == null) {
            throw new EmptyRuntimeException("namingConvetion");
        }
        this.namingConvention = namingConvention;
    }

    /**
     * {@link NamingConvention 命名規約}を返します。
     * 
     * @return 命名規約
     */
    public NamingConvention getNamingConvention() {
        return namingConvention;
    }

    /**
     * {@link InstanceDef インスタンス定義}を返します。
     * 
     * @return インスタンス定義
     */
    public InstanceDef getInstanceDef() {
        return instanceDef;
    }

    /**
     * {@link InstanceDef インスタンス定義}を設定します。
     * 
     * @param instanceDef
     *            インスタンス定義
     */
    public void setInstanceDef(InstanceDef instanceDef) {
        this.instanceDef = instanceDef;
    }

    /**
     * {@link AutoBindingDef 自動バインディング定義}を返します。
     * 
     * @return 自動バインディング定義
     */
    public AutoBindingDef getAutoBindingDef() {
        return autoBindingDef;
    }

    /**
     * {@link AutoBindingDef 自動バインディング定義}を設定します。
     * 
     * @param autoBindingDef
     *            自動バインディング定義
     */
    public void setAutoBindingDef(AutoBindingDef autoBindingDef) {
        this.autoBindingDef = autoBindingDef;
    }

    /**
     * {@link org.seasar.framework.container.ExternalContext 外部バインディング}が有効かどうかを返します。
     * 
     * @return 外部バインディングが有効な場合<code>true</code>、 それ以外の場合<code>false</code>を返す
     */
    public boolean isExternalBinding() {
        return externalBinding;
    }

    /**
     * 外{@link org.seasar.framework.container.ExternalContext 外部バインディング}を有効にするかどうかを設定します。
     * 
     * @param externalBinding
     *            外部バインディングを有効にする場合は<code>true</code>、 それ以外の場合<code>false</code>を指定する
     */
    public void setExternalBinding(boolean externalBinding) {
        this.externalBinding = externalBinding;
    }

    /**
     * インターフェースを対象にするかどうかを返します。
     * 
     * @return インターフェースを対象にする場合<code>true</code>、 それ以外の場合<code>false</code>を返す
     */
    public boolean isEnableInterface() {
        return enableInterface;
    }

    /**
     * インターフェースを対象にするかどうかを設定します。
     * 
     * @param enableInterface
     *            インターフェースを対象にする場合<code>true</code>、 それ以外の場合<code>false</code>を指定する
     */
    public void setEnableInterface(boolean enableInterface) {
        this.enableInterface = enableInterface;
    }

    /**
     * 抽象クラスを対象にするかどうかを返します。
     * 
     * @return 抽象クラスを対象にする場合<code>true</code>、 それ以外の場合<code>false</code>を返す
     */
    public boolean isEnableAbstract() {
        return enableAbstract;
    }

    /**
     * 抽象クラスを対象にするかどうかを設定します。
     * 
     * @param enableAbstract
     *            抽象クラスを対象とする場合<code>true</code>、 それ以外の場合<code>false</code>を指定する
     */
    public void setEnableAbstract(boolean enableAbstract) {
        this.enableAbstract = enableAbstract;
    }

    /**
     * コンポーネント名のサフィックスを返します。
     * 
     * @return 名前のサフィックス
     */
    public String getNameSuffix() {
        return nameSuffix;
    }

    /**
     * コンポーネント名のサフィックスを設定します。
     * 
     * @param nameSuffix
     *            名前のサフィックス
     */
    public void setNameSuffix(String nameSuffix) {
        this.nameSuffix = nameSuffix;
    }

    /**
     * {@link ComponentCustomizer コンポーネント定義カスタマイザ}を返します。
     * 
     * @return コンポーネント定義カスタマイザ
     */
    protected ComponentCustomizer getCustomizer() {
        return customizer;
    }

    /**
     * {@link ComponentCustomizer コンポーネント定義カスタマイザ}を設定します。
     * 
     * @param customizer
     *            コンポーネント定義カスタマイザ
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
     * 指定されたコンポーネント名が、 対象となるコンポーネント名かどうかを返します。
     * 
     * @param componentName
     *            コンポーネント名
     * @return 対象となるコンポーネント名の場合<code>true</code>、 それ以外の場合<code>false</code>
     */
    public boolean isTargetComponentName(String componentName) {
        return componentName.endsWith(nameSuffix);
    }

    /**
     * 指定された{@link ComponentDef コンポーネント定義}を、
     * {@link ComponentCustomizer コンポーネント定義カスタマイザ}を使ってカスタマイズします。
     * 
     * @param componentDef
     *            コンポーネント定義
     */
    protected void customize(ComponentDef componentDef) {
        if (customizer != null) {
            customizer.customize(componentDef);
        }
    }
}