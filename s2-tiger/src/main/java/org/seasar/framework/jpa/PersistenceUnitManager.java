package org.seasar.framework.jpa;

import java.util.concurrent.ConcurrentMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.Transaction;

public interface PersistenceUnitManager {

    EntityManagerFactory getEntityManagerFactory(final String unitName);

    ConcurrentMap<Transaction, EntityManager> getEmfContext(
            final EntityManagerFactory emf);

}
