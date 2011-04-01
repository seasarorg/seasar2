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
package org.seasar.framework.container;

import org.seasar.framework.container.factory.SingletonS2ContainerFactory;

/**
 * シングルトンのS2コンテナからキャストせずにコンポーネントを取得するためのユーティリティ・クラスです。
 * 
 * @author higa
 * @see SingletonS2ContainerFactory
 */
public abstract class SingletonS2Container {

    private SingletonS2Container() {
    }

    /**
     * 型を指定してシングルトンのS2コンテナからコンポーネントを取得します。
     * 
     * @param <T>
     *            コンポーネントの型
     * @param componentClass
     *            コンポーネントの型
     * @return コンポーネント
     * @see org.seasar.framework.container.S2Container#getComponent(Object)
     */
    @SuppressWarnings("unchecked")
    public static <T> T getComponent(final Class<T> componentClass) {
        return (T) SingletonS2ContainerFactory.getContainer().getComponent(
                componentClass);
    }

    /**
     * 名前を指定してシングルトンのS2コンテナからコンポーネントを取得します。
     * <p>
     * 戻り値の型は、このメソッドが呼び出されたコンテキストで求められている型となります。 指定されたコンポーネントが戻り値の型にキャストできない場合は、{@link ClassCastException}がスローされます。
     * </p>
     * 
     * @param <T>
     *            コンポーネントの型
     * @param componentName
     *            コンポーネント名
     * @return コンポーネント
     * @see org.seasar.framework.container.S2Container#getComponent(Object)
     */
    @SuppressWarnings("unchecked")
    public static <T> T getComponent(final String componentName) {
        return (T) SingletonS2ContainerFactory.getContainer().getComponent(
                componentName);
    }

}
