package org.seasar.framework.jpa.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.DestroyMethod;
import org.seasar.framework.container.annotation.tiger.InitMethod;
import org.seasar.framework.jpa.PersistenceUnitContext;
import org.seasar.framework.jpa.PersistenceUnitManager;
import org.seasar.framework.jpa.PersistenceUnitProvider;
import org.seasar.framework.util.tiger.CollectionsUtil;

@Component
public class PersistenceUnitManagerImpl implements PersistenceUnitManager {

    protected static final Context staticContext = new Context();

    protected Context context;

    protected boolean useStaticContext;

    protected List<PersistenceUnitProvider> providers = CollectionsUtil
            .newArrayList();

    public PersistenceUnitManagerImpl() {
    }

    @Binding(bindingType = BindingType.MAY)
    public void setUseStaticContext(final boolean useStaticContext) {
        this.useStaticContext = useStaticContext;
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
