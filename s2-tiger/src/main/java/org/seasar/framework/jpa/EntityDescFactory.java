/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
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
package org.seasar.framework.jpa;

import java.util.concurrent.ConcurrentMap;

import javax.persistence.EntityManager;

import org.seasar.framework.util.tiger.CollectionsUtil;

/**
 * @author koichik
 * 
 */
public class EntityDescFactory {
    protected static final ConcurrentMap<Object, ConcurrentMap<Class<?>, EntityDesc>> contexts = CollectionsUtil
            .newConcurrentHashMap();

    protected static final ConcurrentMap<Class<?>, EntityDescProvider> providers = CollectionsUtil
            .newConcurrentHashMap();

    public static EntityDesc getEntityDesc(final EntityManager em,
            final Class<?> entityClass) {
        final Object delegate = em.getDelegate();
        final EntityDescProvider provider = getProvider(delegate.getClass());
        if (provider == null) {
            throw new RuntimeException();// TODO
        }

        final Object contextKey = provider.getContextKey(em);
        final ConcurrentMap<Class<?>, EntityDesc> context = getContext(contextKey);
        EntityDesc entityDesc = context.get(entityClass);
        if (entityDesc != null) {
            return entityDesc;
        }

        entityDesc = provider.createEntityDesc(entityClass, contextKey);
        if (entityDesc != null) {
            return CollectionsUtil
                    .putIfAbsent(context, entityClass, entityDesc);
        }

        return null;
    }

    public static void addProvider(final Class<?> delegateClass,
            final EntityDescProvider provider) {
        providers.put(delegateClass, provider);
    }

    public static EntityDescProvider getProvider(final Class<?> delegateClass) {
        return providers.get(delegateClass);
    }

    protected static ConcurrentMap<Class<?>, EntityDesc> getContext(
            final Object contextKey) {
        ConcurrentMap<Class<?>, EntityDesc> context = contexts.get(contextKey);
        if (context != null) {
            return context;
        }

        context = CollectionsUtil.newConcurrentHashMap();
        return CollectionsUtil.putIfAbsent(contexts, contextKey, context);
    }
}
