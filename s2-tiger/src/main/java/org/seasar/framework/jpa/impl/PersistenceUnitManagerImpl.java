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
package org.seasar.framework.jpa.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.DestroyMethod;
import org.seasar.framework.container.annotation.tiger.InitMethod;
import org.seasar.framework.convention.NamingConvention;
import org.seasar.framework.jpa.PersistenceUnitContext;
import org.seasar.framework.jpa.PersistenceUnitManager;
import org.seasar.framework.jpa.PersistenceUnitProvider;
import org.seasar.framework.util.tiger.CollectionsUtil;

@Component
public class PersistenceUnitManagerImpl implements PersistenceUnitManager {

    public static final String DEFAULT_PERSISTENCE_UNIT_NAME = "persistenceUnit";

    public static final String PERSISTENCE_UNIT_NAME_SUFFIX = "PersistenceUnit";

    protected static final Context staticContext = new Context();

    protected Context context;

    protected boolean useStaticContext;

    protected String defaultPersistenceUnitName = DEFAULT_PERSISTENCE_UNIT_NAME;

    protected String persistenceUnitNameSuffix = PERSISTENCE_UNIT_NAME_SUFFIX;

    protected S2Container container;

    protected List<PersistenceUnitProvider> providers = CollectionsUtil
            .newArrayList();

    public PersistenceUnitManagerImpl() {
    }

    @Binding(bindingType = BindingType.MAY)
    public void setUseStaticContext(final boolean useStaticContext) {
        this.useStaticContext = useStaticContext;
    }

    @Binding(bindingType = BindingType.MAY)
    public void setDefaultPersistenceUnitName(
            final String defaultPersistenceUnitName) {
        this.defaultPersistenceUnitName = defaultPersistenceUnitName;
    }

    @Binding(bindingType = BindingType.MAY)
    public void setPersistenceUnitNameSuffix(
            final String persistenceUnitNameSuffix) {
        this.persistenceUnitNameSuffix = persistenceUnitNameSuffix;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setContainer(final S2Container container) {
        this.container = container.getRoot();
    }

    @InitMethod
    public void open() {
        context = useStaticContext ? staticContext : new Context();
    }

    @DestroyMethod
    public void close() {
        synchronized (context) {
            if (!useStaticContext) {
                context.close();
            }
        }
    }

    public void addProvider(final PersistenceUnitProvider provider) {
        providers.add(provider);
    }

    public void removeProvider(final PersistenceUnitProvider provider) {
        providers.remove(provider);
    }

    public EntityManagerFactory getEntityManagerFactory(final String unitName) {
        synchronized (context) {
            final EntityManagerFactory emf = context
                    .getEntityManagerFactory(unitName);
            if (emf != null) {
                return emf;
            }
            return createEntityManagerFactory(unitName);
        }
    }

    protected EntityManagerFactory createEntityManagerFactory(
            final String unitName) {
        for (final PersistenceUnitProvider provider : providers) {
            final EntityManagerFactory emf = provider
                    .createEntityManagerFactory(unitName);
            if (emf != null) {
                context.addEntityManagerFactory(unitName, emf);
                return emf;
            }
        }
        return null;
    }

    public PersistenceUnitContext getPersistenceUnitContext(
            final EntityManagerFactory emf) {
        synchronized (context) {
            return context.getPersistenceUnitContext(emf);
        }
    }

    public String getPersistenceUnitName(final Class<?> entityClass) {
        return getPersistenceUnitName(entityClass.getName().replace('.', '/'));
    }

    public String getPersistenceUnitName(final String mappingFile) {
        final NamingConvention convention = NamingConvention.class
                .cast(container.getComponent(NamingConvention.class));
        if (convention == null) {
            return defaultPersistenceUnitName;
        }
        final String entityPackageName = convention.getEntityPackageName();
        final String key = "/" + entityPackageName + "/";
        final int pos = mappingFile.lastIndexOf(key);
        if (pos < 0) {
            return defaultPersistenceUnitName;
        }
        final int pos2 = mappingFile.lastIndexOf('/');
        if (pos + key.length() - 1 == pos2) {
            return defaultPersistenceUnitName;
        }
        return mappingFile.substring(pos + key.length(), pos2)
                + persistenceUnitNameSuffix;
    }

    public static class Context {

        protected final Map<String, EntityManagerFactory> persistenceUnits = new HashMap<String, EntityManagerFactory>();

        protected final Map<EntityManagerFactory, PersistenceUnitContext> persistenceUnitContexts = new HashMap<EntityManagerFactory, PersistenceUnitContext>();

        public EntityManagerFactory getEntityManagerFactory(
                final String unitName) {
            return persistenceUnits.get(unitName);
        }

        public void addEntityManagerFactory(final String unitName,
                final EntityManagerFactory emf) {
            persistenceUnits.put(unitName, emf);
            persistenceUnitContexts.put(emf, new PersistenceUnitContextImpl());
        }

        public PersistenceUnitContext getPersistenceUnitContext(
                final EntityManagerFactory emf) {
            return persistenceUnitContexts.get(emf);
        }

        public void close() {
            for (final EntityManagerFactory emf : persistenceUnits.values()) {
                emf.close();
            }
            persistenceUnits.clear();
            persistenceUnitContexts.clear();
        }

    }

}
