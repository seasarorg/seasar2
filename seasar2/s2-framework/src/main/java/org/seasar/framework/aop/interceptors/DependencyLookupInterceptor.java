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
package org.seasar.framework.aop.interceptors;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInvocation;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.ComponentNotFoundRuntimeException;
import org.seasar.framework.container.ContainerConstants;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.assembler.AbstractBindingTypeDef;
import org.seasar.framework.container.util.BindingUtil;
import org.seasar.framework.message.MessageFormatter;
import org.seasar.framework.util.StringUtil;

/**
 * getterメソッドに適用されて、S2コンテナからルックアップしたコンポーネントを返すインターセプタです。
 * 
 * @author koichik
 * @see AbstractBindingTypeDef
 */
public class DependencyLookupInterceptor extends AbstractInterceptor {

    private static final long serialVersionUID = 1L;

    public Object invoke(final MethodInvocation invocation) throws Throwable {
        final Method method = invocation.getMethod();
        final String methodName = method.getName();
        final Class propType = method.getReturnType();
        if (!methodName.startsWith("get")
                || method.getParameterTypes().length != 0 || propType == null) {
            throw new IllegalStateException(MessageFormatter.getSimpleMessage(
                    "ESSR0103", new Object[] { method }));
        }

        final S2Container container = getComponentDef(invocation)
                .getContainer();
        final String propName = StringUtil
                .decapitalize(methodName.substring(3));
        // Seasar2.4の型による自動バインディング相当のルックアップ
        if (container.hasComponentDef(propType)) {
            final ComponentDef cd = container.getComponentDef(propType);
            if (isAutoBindable(propName, propType, cd)) {
                return cd.getComponent();
            }
        }
        // 名前による自動バインディング相当のルックアップ
        if (container.hasComponentDef(propName)) {
            final ComponentDef cd = container.getComponentDef(propName);
            final Object component = cd.getComponent();
            if (propType.isInstance(component)) {
                return component;
            }
        }
        // 型による自動バインディング相当のルックアップ
        if (BindingUtil.isAutoBindable(propType)) {
            if (container.hasComponentDef(propType)) {
                final ComponentDef cd = container.getComponentDef(propType);
                return cd.getComponent();
            }
        }
        // 配列の要素型による自動バインディング相当のルックアップ
        if (BindingUtil.isAutoBindableArray(propType)) {
            Class clazz = propType.getComponentType();
            Object[] components = container.findAllComponents(clazz);
            if (components.length > 0) {
                return components;
            }
        }
        throw new ComponentNotFoundRuntimeException(propName);
    }

    /**
     * プロパティにコンポーネントを自動バインディング可能なら<code>true</code>を返します。
     * 
     * @param propertyName
     *            プロパティ名
     * @param propertyType
     *            プロパティの型
     * @param cd
     *            コンポーネント定義
     * @return プロパティにコンポーネントを自動バインディング可能なら<code>true</code>
     */
    protected boolean isAutoBindable(final String propertyName,
            final Class propertyType, final ComponentDef cd) {
        return cd.getComponentName() != null
                && (cd.getComponentName().equalsIgnoreCase(propertyName) || StringUtil
                        .endsWithIgnoreCase(cd.getComponentName(),
                                ContainerConstants.PACKAGE_SEP + propertyName));
    }

}
