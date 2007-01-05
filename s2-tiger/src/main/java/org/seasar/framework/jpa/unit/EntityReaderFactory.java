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
package org.seasar.framework.jpa.unit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.seasar.framework.util.Disposable;
import org.seasar.framework.util.DisposableUtil;

/**
 * 
 * @author taedium
 */
public class EntityReaderFactory {

    private static boolean initialized;

    protected static final List<EntityReaderProvider> providers = Collections
            .synchronizedList(new ArrayList<EntityReaderProvider>());

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
        initialized = false;
    }

    public static void clear() {
        providers.clear();
        initialized = true;
    }

    public static void addProvider(final EntityReaderProvider provider) {
        initialize();
        providers.add(provider);
    }

    public static void removeProvider(final EntityReaderProvider provider) {
        initialize();
        providers.remove(provider);
    }

    public static EntityReader getEntityReader(final Object entity) {
        initialize();
        for (final EntityReaderProvider provider : providers) {
            EntityReader reader = provider.createEntityReader(entity);
            if (reader != null) {
                return reader;
            }
        }
        return null;
    }

    public static EntityReader getEntityReader(final Collection<?> entities) {
        initialize();
        for (final EntityReaderProvider provider : providers) {
            EntityReader reader = provider.createEntityReader(entities);
            if (reader != null) {
                return reader;
            }
        }
        return null;
    }
}
