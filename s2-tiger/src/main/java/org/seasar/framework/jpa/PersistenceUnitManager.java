package org.seasar.framework.jpa;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.Transaction;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.DestroyMethod;

@Component
public class PersistenceUnitManager {
    private final Map<String, EntityManagerFactory> persistenceUnits = new HashMap<String, EntityManagerFactory>();

    private final Map<EntityManagerFactory, ConcurrentMap<Transaction, EntityManager>> emfContexts = new HashMap<EntityManagerFactory, ConcurrentMap<Transaction, EntityManager>>();

    public PersistenceUnitManager() {
    }

    @DestroyMethod
    public void close() {
        for (final Map.Entry<String, EntityManagerFactory> entry : persistenceUnits
                .entrySet()) {
            entry.getValue().close();
        }
    }

    public synchronized EntityManagerFactory getEntityManagerFactory(
            final String unitName) {
        final EntityManagerFactory emf = persistenceUnits.get(unitName);
        if (emf != null) {
            return emf;
        }
        return createEntityManagerFactory(unitName);
    }

    EntityManagerFactory createEntityManagerFactory(final String unitName) {
        final EntityManagerFactory emf = Persistence
                .createEntityManagerFactory(unitName);
        persistenceUnits.put(unitName, emf);
        emfContexts.put(emf,
                new ConcurrentHashMap<Transaction, EntityManager>());
        return emf;
    }

    public ConcurrentMap<Transaction, EntityManager> getEmfContext(
            final EntityManagerFactory emf) {
        return emfContexts.get(emf);
    }
}
