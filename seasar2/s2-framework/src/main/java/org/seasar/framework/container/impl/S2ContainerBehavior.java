/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
package org.seasar.framework.container.impl;

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.ComponentNotFoundRuntimeException;
import org.seasar.framework.container.S2Container;

/**
 * {@link S2Container}の振る舞いをカスタマイズするためのクラスです。 {@link Provider}を取り替えることによってカスタマイズが可能です。
 * {@link Provider}はs2container.diconで設定します。
 * 
 * @author koichik
 */
public class S2ContainerBehavior {

    private static Provider provider = new DefaultProvider();

    private S2ContainerBehavior() {
    }

    /**
     * {@link Provider}を返します。
     * 
     * @return {@link Provider}
     */
    public static Provider getProvider() {
        return provider;
    }

    /**
     * {@link Provider}を設定します。
     * 
     * @param p
     */
    public static void setProvider(final Provider p) {
        provider = p;
    }

    /**
     * {@link S2Container#getComponent(Object)}のカスタマイズポイントです。
     * 
     * @param container
     * @param key
     * @return {@link ComponentDef}
     */
    public static ComponentDef acquireFromGetComponent(S2Container container,
            final Object key) {
        return getProvider().acquireFromGetComponent(container, key);
    }

    /**
     * {@link S2Container#getComponentDef(Object)}のカスタマイズポイントです。
     * 
     * @param container
     * @param key
     * @return {@link ComponentDef}
     */
    public static ComponentDef acquireFromGetComponentDef(
            S2Container container, final Object key) {
        return getProvider().acquireFromGetComponentDef(container, key);
    }

    /**
     * {@link S2Container#hasComponentDef(Object)}のカスタマイズポイントです。
     * 
     * @param container
     * @param key
     * @return {@link ComponentDef}
     */
    public static ComponentDef acquireFromHasComponentDef(
            S2Container container, final Object key) {
        return getProvider().acquireFromHasComponentDef(container, key);
    }

    /**
     * {@link S2Container#injectDependency(Object)}のカスタマイズポイントです。
     * 
     * @param container
     * @param key
     * @return
     */
    public static ComponentDef acquireFromInjectDependency(
            S2Container container, final Object key) {
        return getProvider().acquireFromInjectDependency(container, key);
    }

    /**
     * S2Containerの振る舞いをカスタマイズするためのインターフェースです。
     * 
     * @author koichik
     */
    public interface Provider {

        /**
         * {@link S2Container#getComponent(Object)}のカスタマイズポイントです。
         * 
         * @param container
         * @param key
         * @return {@link ComponentDef}
         */
        ComponentDef acquireFromGetComponent(S2Container container,
                final Object key);

        /**
         * {@link S2Container#getComponentDef(Object)}のカスタマイズポイントです。
         * 
         * @param container
         * @param key
         * @return {@link ComponentDef}
         */
        ComponentDef acquireFromGetComponentDef(S2Container container,
                final Object key);

        /**
         * {@link S2Container#hasComponentDef(Object)}のカスタマイズポイントです。
         * 
         * @param container
         * @param key
         * @return {@link ComponentDef}
         */
        ComponentDef acquireFromHasComponentDef(S2Container container,
                final Object key);

        /**
         * {@link S2Container#injectDependency(Object)}のカスタマイズポイントです。
         * 
         * @param container
         * @param key
         * @return {@link ComponentDef}
         */
        ComponentDef acquireFromInjectDependency(S2Container container,
                final Object key);
    }

    /**
     * デフォルトの
     * {@link org.seasar.framework.container.impl.S2ContainerBehavior.Provider}実装です。
     * 
     * @author koichik
     */
    public static class DefaultProvider implements Provider {
        public ComponentDef acquireFromGetComponent(
                final S2Container container, final Object key) {
            return acquireFromGetComponentDef(container, key);
        }

        public ComponentDef acquireFromGetComponentDef(
                final S2Container container, final Object key) {
            final ComponentDef cd = getComponentDef(container, key);
            if (cd == null) {
                throw new ComponentNotFoundRuntimeException(key);
            }
            return cd;
        }

        public ComponentDef acquireFromHasComponentDef(
                final S2Container container, final Object key) {
            return getComponentDef(container, key);
        }

        public ComponentDef acquireFromInjectDependency(
                final S2Container container, final Object key) {
            return acquireFromGetComponentDef(container, key);
        }

        /**
         * {@link ComponentDef}を返すときのデフォルトの振る舞いです。
         * 
         * @param container
         * @param key
         * @return {@link ComponentDef}
         */
        protected ComponentDef getComponentDef(final S2Container container,
                final Object key) {
            return ((S2ContainerImpl) container).internalGetComponentDef(key);
        }
    }
}