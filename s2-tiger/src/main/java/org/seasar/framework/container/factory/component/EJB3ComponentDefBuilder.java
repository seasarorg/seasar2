/*
 * Copyright 2004-2011 the Seasar Foundation and the Others.
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

import javax.ejb.Stateful;
import javax.ejb.Stateless;

import org.seasar.framework.container.AutoBindingDef;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.InstanceDef;
import org.seasar.framework.container.assembler.AutoBindingDefFactory;
import org.seasar.framework.container.deployer.InstanceDefFactory;
import org.seasar.framework.container.factory.AnnotationHandler;
import org.seasar.framework.container.factory.ComponentDefBuilder;
import org.seasar.framework.container.impl.ComponentDefImpl;
import org.seasar.framework.ejb.EJB3Desc;
import org.seasar.framework.ejb.EJB3DescFactory;
import org.seasar.framework.util.StringUtil;

/**
 * EJB3の{@link Stateless}、{@link Stateful}アノテーションを読み取り{@link ComponentDef}を作成するコンポーネントの実装クラスです。
 * 
 * @author koichik
 */
public class EJB3ComponentDefBuilder implements ComponentDefBuilder {

    /**
     * インスタンスを構築します。
     */
    public EJB3ComponentDefBuilder() {
    }

    public ComponentDef createComponentDef(
            final AnnotationHandler annotationHandler, Class<?> componentClass,
            InstanceDef defaultInstanceDef,
            AutoBindingDef defaultAutoBindingDef, boolean defaultExternalBinding) {
        final EJB3Desc ejb3Desc = EJB3DescFactory.getEJB3Desc(componentClass);
        if (ejb3Desc == null) {
            return null;
        }

        final ComponentDef componentDef = new ComponentDefImpl(componentClass);
        if (!StringUtil.isEmpty(ejb3Desc.getName())) {
            componentDef.setComponentName(ejb3Desc.getName());
        }
        componentDef.setInstanceDef(getInstanceDef(defaultInstanceDef));
        componentDef
                .setAutoBindingDef(getAutoBindingDef(defaultAutoBindingDef));
        componentDef.setExternalBinding(defaultExternalBinding);
        return componentDef;
    }

    /**
     * {@link InstanceDef インスタンス定義}を返します。
     * <p>
     * デフォルトのインスタンス定義が<code>null</code>の場合は{@link InstanceDefFactory#PROTOTYPE}を返します。
     * </p>
     * 
     * @param defaultInstanceDef
     *            デフォルトの{@link InstanceDef インスタンス定義}
     * @return {@link InstanceDef インスタンス定義}
     */
    protected InstanceDef getInstanceDef(final InstanceDef defaultInstanceDef) {
        return defaultInstanceDef != null ? defaultInstanceDef
                : InstanceDefFactory.PROTOTYPE;
    }

    /**
     * {@link AutoBindingDef 自動バインディング定義}を返します。
     * <p>
     * デフォルトのバインディング定義が<code>null</code>の場合は{@link AutoBindingDefFactory#SEMIAUTO}を返します。
     * </p>
     * 
     * @param defaultAutoBindingDef
     *            デフォルトの{@link AutoBindingDef 自動バインディング定義}
     * @return {@link AutoBindingDef 自動バインディング定義}
     */
    protected AutoBindingDef getAutoBindingDef(
            final AutoBindingDef defaultAutoBindingDef) {
        return defaultAutoBindingDef != null ? defaultAutoBindingDef
                : AutoBindingDefFactory.SEMIAUTO;
    }

}