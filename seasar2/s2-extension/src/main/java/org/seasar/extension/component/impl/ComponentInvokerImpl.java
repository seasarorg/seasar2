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
package org.seasar.extension.component.impl;

import org.seasar.extension.component.ComponentInvoker;
import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.hotdeploy.HotdeployUtil;
import org.seasar.framework.exception.InvocationTargetRuntimeException;

/**
 * 指定されたコンポーネントのメソッドを呼び出すコンポーネントの実装です。
 * <p>
 * このクラスはS2RMIなどから利用されることを意図しています。
 * </p>
 * 
 * @author Kenichiro Murata
 */
public class ComponentInvokerImpl implements ComponentInvoker {

    private S2Container container;

    /**
     * インスタンスを構築します。
     */
    public ComponentInvokerImpl() {
    }

    public Object invoke(final String componentName, final String methodName,
            final Object[] args) throws Throwable {

        final Object component = container.getRoot()
                .getComponent(componentName);
        final BeanDesc beanDesc = BeanDescFactory.getBeanDesc(component
                .getClass());
        try {
            if (HotdeployUtil.isHotdeploy()) {
                final Object[] newArgs = (Object[]) HotdeployUtil
                        .rebuildValue(args);
                return beanDesc.invoke(component, methodName, newArgs);
            }
            return beanDesc.invoke(component, methodName, args);
        } catch (final InvocationTargetRuntimeException e) {
            throw e.getCause();
        }
    }

    /**
     * S2コンテナを設定します。
     * 
     * @param container
     *            S2コンテナ
     */
    public void setContainer(final S2Container container) {
        this.container = container;
    }

}
