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
package org.seasar.framework.jpa.metadata;

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

    public static EntityDesc getEntityDesc(final Class<?> entityClass) {
        initialize();
        final EntityDesc entityDesc = entityDescs.get(entityClass);
        if (entityDesc != null) {
            return entityDesc;
        }
        return createEntityDesc(entityClass);
    }

    protected static EntityDesc createEntityDesc(final Class<?> entityClass) {
        for (final EntityDescProvider provider : providers) {
            final EntityDesc entityDesc = provider
                    .createEntityDesc(entityClass);
            if (entityDesc != null) {
                return CollectionsUtil.putIfAbsent(entityDescs, entityClass,
                        entityDesc);
            }
        }
        return null;
    }

}
