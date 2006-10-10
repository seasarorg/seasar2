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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

import org.seasar.framework.util.Disposable;
import org.seasar.framework.util.DisposableUtil;
import org.seasar.framework.util.tiger.CollectionsUtil;

/**
 * @author koichik
 * 
 */
public class EntityDescFactory {

    private static boolean initialized;

    protected static final List<EntityDescProvider> providers = Collections
            .synchronizedList(new ArrayList<EntityDescProvider>());

    @SuppressWarnings("unchecked")
    protected static final ConcurrentMap<Class<?>, EntityDesc> entityDescs = CollectionsUtil
            .newConcurrentHashMap();

    static {
        initialize();
    }

    public static void initialize() {
        if (initialized) {
            return;
        }
        DisposableUtil.add(new Disposable() {
            public void dispose() {
                clear();
            }
        });
        initialized = true;
    }

    public static void clear() {
        entityDescs.clear();
        initialized = false;
    }

    public static void addProvider(final EntityDescProvider provider) {
        initialize();
        providers.add(provider);
    }

    public static void removeProvider(final EntityDescProvider provider) {
        initialize();
        providers.remove(provider);
    }

    @SuppressWarnings("unchecked")
    public static <T> EntityDesc<T> getEntityDesc(final Class<T> entityClass) {
        initialize();
        final EntityDesc<T> entityDesc = entityDescs.get(entityClass);
        if (entityDesc != null) {
            return entityDesc;
        }
        return createEntityDesc(entityClass);
    }

    @SuppressWarnings("unchecked")
    protected static <T> EntityDesc<T> createEntityDesc(
            final Class<T> entityClass) {
        for (final EntityDescProvider provider : providers) {
            final EntityDesc<T> entityDesc = provider
                    .createEntityDesc(entityClass);
            if (entityDesc != null) {
                return CollectionsUtil.putIfAbsent(entityDescs, entityClass,
                        entityDesc);
            }
        }
        return null;
    }

}
