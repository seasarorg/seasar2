/*
 * Copyright 2004-2014 the Seasar Foundation and the Others.
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
package org.seasar.framework.container.util;

import java.util.Collection;
import java.util.Map;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.ContainerConstants;

/**
 * コンポーネントとコンポーネントを結びつけるためのユーティリティクラスです。
 * 
 * @author higa
 * 
 */
public class BindingUtil implements ContainerConstants {

    /**
     * インスタンスを構築します。
     */
    protected BindingUtil() {
    }

    /**
     * 自動バインディング可能かどうか返します。
     * 
     * @param clazz
     * @return 自動バインディング可能かどうか
     */
    public static final boolean isAutoBindable(Class clazz) {
        return clazz.isInterface() && !Collection.class.isAssignableFrom(clazz)
                && !Map.class.isAssignableFrom(clazz);
    }

    /**
     * 自動バインディング可能かどうか返します。
     * 
     * @param clazz
     * @return 自動バインディング可能かどうか
     */
    public static final boolean isAutoBindableArray(Class clazz) {
        return clazz.isArray() && clazz.getComponentType().isInterface();
    }

    /**
     * 自動バインディング可能かどうか返します。
     * 
     * @param classes
     * @return 自動バインディング可能かどうか
     * @see #isAutoBindable(Class)
     */
    public static final boolean isAutoBindable(Class[] classes) {
        for (int i = 0; i < classes.length; ++i) {
            if (!isAutoBindable(classes[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * {@link BeanDesc}を返します。
     * 
     * @param componentDef
     * @param component
     * @return {@link BeanDesc}
     */
    public static BeanDesc getBeanDesc(ComponentDef componentDef,
            Object component) {
        return BeanDescFactory.getBeanDesc(getComponentClass(componentDef,
                component));
    }

    /**
     * コンポーネントのクラスを返します。AOPでクラスが拡張されている場合は、拡張前のクラスが返されます。
     * 
     * @param componentDef
     * @param component
     * @return コンポーネントのクラス
     */
    public static Class getComponentClass(ComponentDef componentDef,
            Object component) {
        Class clazz = componentDef.getConcreteClass();
        if (clazz != null) {
            return clazz;
        }
        return component.getClass();
    }
}