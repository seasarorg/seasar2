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
package org.seasar.framework.container.impl;

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.ComponentNotFoundRuntimeException;
import org.seasar.framework.container.S2Container;

/**
 * 
 * @author koichik
 */
public final class S2ContainerBehavior {

    private static Provider provider = new DefaultProvider();

    private S2ContainerBehavior() {
    }

    public static Provider getProvider() {
        return provider;
    }

    public static void setProvider(final Provider p) {
        provider = p;
    }

    public static ComponentDef acquireFromGetComponent(S2Container container,
            final Object key) {
        return getProvider().acquireFromGetComponent(container, key);
    }

    public static ComponentDef acquireFromGetComponentDef(
            S2Container container, final Object key) {
        return getProvider().acquireFromGetComponentDef(container, key);
    }

    public static ComponentDef acquireFromHasComponentDef(
            S2Container container, final Object key) {
        return getProvider().acquireFromHasComponentDef(container, key);
    }

    public static ComponentDef acquireFromInjectDependency(
            S2Container container, final Object key) {
        return getProvider().acquireFromInjectDependency(container, key);
    }

    /**
     * 
     * @author koichik
     */
    public interface Provider {

        ComponentDef acquireFromGetComponent(S2Container container,
                final Object key);

        ComponentDef acquireFromGetComponentDef(S2Container container,
                final Object key);

        ComponentDef acquireFromHasComponentDef(S2Container container,
                final Object key);

        ComponentDef acquireFromInjectDependency(S2Container container,
                final Object key);
    }

    /**
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

        protected ComponentDef getComponentDef(final S2Container container,
                final Object key) {
            return ((S2ContainerImpl) container).internalGetComponentDef(key);
        }
    }
}