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
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.DestroyMethod;
import org.seasar.framework.container.annotation.tiger.InitMethod;
import org.seasar.framework.convention.NamingConvention;
import org.seasar.framework.env.Env;
import org.seasar.framework.jpa.PersistenceUnitContext;
import org.seasar.framework.jpa.PersistenceUnitManager;
import org.seasar.framework.jpa.PersistenceUnitProvider;
import org.seasar.framework.util.ClassUtil;

@Component
public class PersistenceUnitManagerImpl implements PersistenceUnitManager {

    public static final String DEFAULT_PERSISTENCE_UNIT_NAME = "persistenceUnit";

    public static final String PERSISTENCE_UNIT_NAME_SUFFIX = "PersistenceUnit";

    protected static final Context staticContext = new Context();

    protected Context context;

    protected boolean useStaticContext = Env.getValue().startsWith("ut");

    protected String defaultPersistenceUnitName = DEFAULT_PERSISTENCE_UNIT_NAME;

    protected String persistenceUnitNameSuffix = PERSISTENCE_UNIT_NAME_SUFFIX;

    protected S2Container container;

    protected PersistenceUnitProvider defaultUnitProvider;

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

    @Binding(bindingType = BindingType.MUST)
    public void setDefaultUnitProvider(
            final PersistenceUnitProvider defaultUnitProvider) {
        this.defaultUnitProvider = defaultUnitProvider;
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

    public EntityManagerFactory getEntityManagerFactory(final String unitName) {
        return getEntityManagerFactory(unitName, unitName);
    }

    public EntityManagerFactory getEntityManagerFactory(final String unitName,
            final PersistenceUnitProvider provider) {
        return getEntityManagerFactory(unitName, unitName, provider);
    }

    public EntityManagerFactory getEntityManagerFactory(
            final String abstractUnitName, final String concreteUnitName) {
        return getEntityManagerFactory(abstractUnitName, concreteUnitName,
                defaultUnitProvider);
    }

    public EntityManagerFactory getEntityManagerFactory(
            final String abstractUnitName, final String concreteUnitName,
            final PersistenceUnitProvider provider) {
        synchronized (context) {
            final EntityManagerFactory emf = context
                    .getEntityManagerFactory(concreteUnitName);
            if (emf != null) {
                return emf;
            }
            return createEntityManagerFactory(abstractUnitName,
                    concreteUnitName, provider);
        }
    }

    protected EntityManagerFactory createEntityManagerFactory(
            final String abstractUnitName, final String concreteUnitName,
            final PersistenceUnitProvider provider) {
        final EntityManagerFactory emf = provider.createEntityManagerFactory(
                abstractUnitName, concreteUnitName);
        if (emf != null) {
            context.addEntityManagerFactory(concreteUnitName, emf);
            return emf;
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
        final NamingConvention convention = NamingConvention.class
                .cast(container.getComponent(NamingConvention.class));
        if (convention == null) {
            return defaultPersistenceUnitName;
        }
        final String entityPackageName = convention.getEntityPackageName();
        final String resourcePath = ClassUtil.getResourcePath(entityClass);
        return getPersistenceUnitNameFromPackage(entityPackageName,
                resourcePath);
    }

    public String getPersistenceUnitName(final String mappingFile) {
        final NamingConvention convention = NamingConvention.class
                .cast(container.getComponent(NamingConvention.class));
        if (convention == null) {
            return defaultPersistenceUnitName;
        }
        final String entityPackageName = convention.getEntityPackageName();
        if (mappingFile.lastIndexOf("/" + entityPackageName + "/") > -1) {
            return getPersistenceUnitNameFromPackage(entityPackageName,
                    mappingFile);
        }
        final String daoPackageName = convention.getDaoPackageName();
        return getPersistenceUnitNameFromPackage(daoPackageName, mappingFile);
    }

    protected String getPersistenceUnitNameFromPackage(
            final String packageName, final String path) {
        final String key = "/" + packageName + "/";
        final int pos = path.lastIndexOf(key);
        if (pos < 0) {
            return defaultPersistenceUnitName;
        }
        final int pos2 = path.lastIndexOf('/');
        if (pos + key.length() - 1 == pos2) {
            return defaultPersistenceUnitName;
        }
        return path.substring(pos + key.length(), pos2)
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
