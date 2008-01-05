/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
 */
public abstract class AbstractComponentAutoRegister extends
        AbstractAutoRegister implements ClassHandler {

    /** クラスファイルの拡張子 */
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
     * @return AutoNaming
     */
    public AutoNaming getAutoNaming() {
        return autoNaming;
    }

    /**
     * AutoNamingを設定します。
     * 
     * @param autoNaming
     *            AutoNaming
     */
    public void setAutoNaming(AutoNaming autoNaming) {
        this.autoNaming = autoNaming;
    }

    /**
     * インスタンス定義を返します。
     * 
     * @return インスタンス定義
     */
    public InstanceDef getInstanceDef() {
        return instanceDef;
    }

    /**
     * インスタンス定義を設定します。
     * 
     * @param instanceDef
     *            インスタンス定義
     */
    public void setInstanceDef(InstanceDef instanceDef) {
        this.instanceDef = instanceDef;
    }

    /**
     * 自動バインディング定義を返します。
     * 
     * @return 自動バインディング定義
     */
    public AutoBindingDef getAutoBindingDef() {
        return autoBindingDef;
    }

    /**
     * 自動バインディング定義を設定します。
     * 
     * @param autoBindingDef
     *            自動バインディング定義
     */
    public void setAutoBindingDef(AutoBindingDef autoBindingDef) {
        this.autoBindingDef = autoBindingDef;
    }

    /**
     * 外部バインディングのデフォルト値を返します。
     * 
     * @return 外部バインディングのデフォルト値
     */
    public boolean isExternalBinding() {
        return externalBinding;
    }

    /**
     * 外部バインディングのデフォルト値を設定します。
     * 
     * @param externalBinding
     *            外部バインディングのデフォルト値
     */
    public void setExternalBinding(boolean externalBinding) {
        this.externalBinding = externalBinding;
    }

    /**
     * コンポーネントカスタマイザを返します。
     * 
     * @return コンポーネントカスタマイザ
     */
    public ComponentCustomizer getCustomizer() {
        return customizer;
    }

    /**
     * コンポーネントカスタマイザを設定します。
     * 
     * @param customizer
     *            コンポーネントカスタマイザ
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
                return;
            }
        }
    }

    /**
     * コンポーネント定義を作成してコンテナに登録します。
     * 
     * @param className
     *            コンポーネントのクラス
     */
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

    /**
     * コンポーネント定義をカスタマイズします。
     * 
     * @param componentDef
     *            コンポーネント定義
     */
    protected void customize(ComponentDef componentDef) {
        if (customizer != null) {
            customizer.customize(componentDef);
        }
    }

    /**
     * コンポーネントを検索する対象となるパッケージの配列を返します。
     * <p>
     * コンポーネントを検索する対象のパッケージは<code>ClassPattern</code>に設定されたパッケージ名から
     * 重複やサブパッケージを除いたものになります。 例えば<code>ClassPattern</code>に<code>aaa, aaa.bbb, bbb</code>が指定された場合、
     * <code>aaa.bbb</code>は<code>aaa</code>のサブパッケージなので取り除かれ、
     * <code>aaa, bbb</code>が検索対象のパッケージとなります。
     * </p>
     * 
     * @return コンポーネントを検索する対象となるパッケージの配列
     */
    protected String[] getTargetPackages() {
        final List result = new ArrayList();
        for (int i = 0; i < getClassPatternSize(); ++i) {
            final String packageName = getClassPattern(i).getPackageName();
            boolean append = true;
            for (int j = 0; j < result.size(); ++j) {
                final String root = (String) result.get(j);
                if (packageName.equals(root)) {
                    append = false;
                    break;
                } else if (packageName.startsWith(root)) {
                    append = false;
                    break;
                } else if (root.startsWith(packageName)) {
                    result.set(j, packageName);
                    append = false;
                    break;
                }
            }
            if (append) {
                result.add(packageName);
            }
        }
        return (String[]) result.toArray(new String[result.size()]);
    }

}
