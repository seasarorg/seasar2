/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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
package org.seasar.extension.component.impl;

import org.seasar.extension.component.ComponentInvoker;
import org.seasar.framework.container.ComponentCreator;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.hotdeploy.HotdeployClassLoader;
import org.seasar.framework.container.hotdeploy.HotdeployUtil;
import org.seasar.framework.container.impl.ComponentDefImpl;
import org.seasar.framework.util.ClassUtil;

/**
 * SMART deploy時に{@link ComponentInvoker}のコンポーネント定義を作成する{@link ComponentCreator}です。
 * <p>
 * バージョン2.4.13以前は、HOT deploy時に引数を{@link HotdeployClassLoader}からロードされたクラスにするために
 * {@link ComponentInvoker}を{@link ClassCastException}からロードされなくてはならなかったために
 * このクラスが必要でした。 しかし、バージョン2.4.14からは{@link HotdeployUtil#rebuildValue(Object)}が使えるようになったため、
 * {@link ComponentInvoker}をSMART deploy対象とする必要が無くなったため，もうこのクラスは必要なくなりましたが、
 * 互換性に考慮して残してあります。
 * </p>
 * 
 * @author koichik
 */
public class ComponentInvokerCreator implements ComponentCreator {

    /** {@link ComponentInvoker}インターフェースのFQN */
    protected static final String COMPONENT_INVOKER_INTERFACE_NAME = "org.seasar.extension.component.ComponentInvoker";

    /** {@link ComponentInvoker}実装クラスのFQN */
    protected static final String COMPONENT_INVOKER_CLASS_NAME = "org.seasar.extension.component.impl.ComponentInvokerImpl";

    /** {@link ComponentInvoker}のコンポーネント名 */
    protected String componentInvokerName = "componentInvoker";

    /**
     * {@link ComponentInvoker}のコンポーネント名を設定します。
     * 
     * @param componentInvokerName
     *            {@link ComponentInvoker}のコンポーネント名
     */
    public void setComponentInvokerName(final String componentInvokerName) {
        this.componentInvokerName = componentInvokerName;
    }

    public ComponentDef createComponentDef(final Class clazz) {
        if (!clazz.getName().equals(COMPONENT_INVOKER_INTERFACE_NAME)
                && !clazz.getName().equals(COMPONENT_INVOKER_CLASS_NAME)) {
            return null;
        }
        final ComponentDef cd = new ComponentDefImpl(ClassUtil
                .forName(COMPONENT_INVOKER_CLASS_NAME), componentInvokerName);
        return cd;
    }

    public ComponentDef createComponentDef(final String componentName) {
        if (!componentName.equals(componentInvokerName)) {
            return null;
        }
        final ComponentDef cd = new ComponentDefImpl(ClassUtil
                .forName(COMPONENT_INVOKER_CLASS_NAME), componentInvokerName);
        return cd;
    }

}
