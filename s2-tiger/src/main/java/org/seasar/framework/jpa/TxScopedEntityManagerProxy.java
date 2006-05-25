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
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Query;
import javax.persistence.TransactionRequiredException;
import javax.transaction.Synchronization;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.TransactionManagerUtil;
import org.seasar.framework.util.TransactionUtil;

/**
 * コンテナ管理{@link javax.persistence.EntityManager}の実装です。
 * <p>
 * このクラスは次のタイプの{@link javax.persistence.EntityManager}となります。
 * <ul>
 * <li>コンテナ管理 (container-managed entity manager)</li>
 * <li>JTAトランザクション (JTA entity manager)</li>
 * <li>トランザクションスコープ (transaction-scoped persistence contexts)</li>
 * <ul>
 * </p>
 * 
 * @author koichik
 */
@Component
public class TxScopedEntityManagerProxy implements EntityManager {
    private static final Logger logger = Logger
            .getLogger(TxScopedEntityManagerProxy.class);

    private TransactionManager tm;

    private EntityManagerFactory emf;

    private PersistenceUnitManager pum;

    /**
     * インスタンスを構築します。
     */
    public TxScopedEntityManagerProxy() {
    }

    @Binding(bindingType = BindingType.MUST)
    public void setTransactionManager(final TransactionManager tm) {
        this.tm = tm;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setEntityManagerFactory(final EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setPersistenceUnitManager(final PersistenceUnitManager pum) {
        this.pum = pum;
    }

    protected boolean isTxActive() {
        return TransactionManagerUtil.isActive(tm);
    }

    protected void assertTxActive() {
        if (!isTxActive()) {
            throw new TransactionRequiredException();
        }
    }

    protected EntityManager getEntityManager() {
        if (!isTxActive()) {
            return emf.createEntityManager();
        }
        final EntityManager em = getTxBoundEntityManager();
        if (em != null) {
            return em;
        }
        return createEntityManager();
    }

    protected EntityManager getTxBoundEntityManager() {
        final ConcurrentMap<Transaction, EntityManager> context = pum
                .getEmfContext(emf);
        if (context == null) {
            return null;
        }
        final Transaction tx = TransactionManagerUtil.getTransaction(tm);
        return context.get(tx);
    }

    protected EntityManager createEntityManager() {
        final EntityManager em = emf.createEntityManager();
        final Transaction tx = TransactionManagerUtil.getTransaction(tm);
        TransactionUtil.registerSynchronization(tx, new Synchronization() {
            public void afterCompletion(int status) {
                final ConcurrentMap<Transaction, EntityManager> context = pum
                        .getEmfContext(emf);
                context.remove(tx);
                try {
                    em.close();
                } catch (final Throwable t) {
                    logger.log("", new Object[] {}, t);
                }
            }

            public void beforeCompletion() {
            }
        });

        final ConcurrentMap<Transaction, EntityManager> context = pum
                .getEmfContext(emf);
        context.put(tx, em);
        return em;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.persistence.EntityManager#clear()
     */
    public void clear() {
        if (isTxActive()) {
            final EntityManager em = getEntityManager();
            em.clear();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.persistence.EntityManager#close()
     */
    public void close() {
        throw new IllegalStateException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.persistence.EntityManager#contains(java.lang.Object)
     */
    public boolean contains(final Object entity) {
        final boolean mustClose = !isTxActive();
        final EntityManager em = getEntityManager();
        try {
            return em.contains(entity);
        } finally {
            if (mustClose) {
                em.close();
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.persistence.EntityManager#createNamedQuery(java.lang.String)
     */
    public Query createNamedQuery(final String name) {
        final boolean mustClose = !isTxActive();
        final EntityManager em = getEntityManager();
        try {
            return em.createNamedQuery(name);
        } finally {
            if (mustClose) {
                em.close();
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.persistence.EntityManager#createNativeQuery(java.lang.String)
     */
    public Query createNativeQuery(final String sqlString) {
        final boolean mustClose = !isTxActive();
        final EntityManager em = getEntityManager();
        try {
            return em.createNativeQuery(sqlString);
        } finally {
            if (mustClose) {
                em.close();
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.persistence.EntityManager#createNativeQuery(java.lang.String,
     *      java.lang.Class)
     */
    public Query createNativeQuery(final String sqlString,
            final Class resultClass) {
        final boolean mustClose = !isTxActive();
        final EntityManager em = getEntityManager();
        try {
            return em.createNativeQuery(sqlString, resultClass);
        } finally {
            if (mustClose) {
                em.close();
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.persistence.EntityManager#createNativeQuery(java.lang.String,
     *      java.lang.String)
     */
    public Query createNativeQuery(final String sqlString,
            final String resultSetMapping) {
        final boolean mustClose = !isTxActive();
        final EntityManager em = getEntityManager();
        try {
            return em.createNativeQuery(sqlString, resultSetMapping);
        } finally {
            if (mustClose) {
                em.close();
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.persistence.EntityManager#createQuery(java.lang.String)
     */
    public Query createQuery(final String qlString) {
        final boolean mustClose = !isTxActive();
        final EntityManager em = getEntityManager();
        try {
            return em.createQuery(qlString);
        } finally {
            if (mustClose) {
                em.close();
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.persistence.EntityManager#find(java.lang.Class,
     *      java.lang.Object)
     */
    public <T> T find(final Class<T> entityClass, final Object primaryKey) {
        final boolean mustClose = !isTxActive();
        final EntityManager em = getEntityManager();
        try {
            return em.find(entityClass, primaryKey);
        } finally {
            if (mustClose) {
                em.close();
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.persistence.EntityManager#flush()
     */
    public void flush() {
        if (isTxActive()) {
            final EntityManager em = getEntityManager();
            em.flush();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.persistence.EntityManager#getDelegate()
     */
    public Object getDelegate() {
        final boolean mustClose = !isTxActive();
        final EntityManager em = getEntityManager();
        try {
            return em.getDelegate();
        } finally {
            if (mustClose) {
                em.close();
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.persistence.EntityManager#getFlushMode()
     */
    public FlushModeType getFlushMode() {
        final boolean mustClose = !isTxActive();
        final EntityManager em = getEntityManager();
        try {
            return em.getFlushMode();
        } finally {
            if (mustClose) {
                em.close();
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.persistence.EntityManager#getReference(java.lang.Class,
     *      java.lang.Object)
     */
    public <T> T getReference(final Class<T> entityClass,
            final Object primaryKey) {
        final boolean mustClose = !isTxActive();
        final EntityManager em = getEntityManager();
        try {
            return em.getReference(entityClass, primaryKey);
        } finally {
            if (mustClose) {
                em.close();
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.persistence.EntityManager#getTransaction()
     */
    public EntityTransaction getTransaction() {
        throw new IllegalStateException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.persistence.EntityManager#isOpen()
     */
    public boolean isOpen() {
        final boolean mustClose = !isTxActive();
        final EntityManager em = getEntityManager();
        try {
            return em.isOpen();
        } finally {
            if (mustClose) {
                em.close();
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.persistence.EntityManager#joinTransaction()
     */
    public void joinTransaction() {
        assertTxActive();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.persistence.EntityManager#lock(java.lang.Object,
     *      javax.persistence.LockModeType)
     */
    public void lock(Object entity, LockModeType lockMode) {
        assertTxActive();
        final EntityManager em = getEntityManager();
        em.lock(entity, lockMode);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.persistence.EntityManager#merge(T)
     */
    public <T> T merge(T entity) {
        assertTxActive();
        final EntityManager em = getEntityManager();
        return em.merge(entity);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.persistence.EntityManager#persist(java.lang.Object)
     */
    public void persist(Object entity) {
        assertTxActive();
        final EntityManager em = getEntityManager();
        em.persist(entity);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.persistence.EntityManager#refresh(java.lang.Object)
     */
    public void refresh(Object entity) {
        assertTxActive();
        final EntityManager em = getEntityManager();
        em.refresh(entity);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.persistence.EntityManager#remove(java.lang.Object)
     */
    public void remove(Object entity) {
        assertTxActive();
        final EntityManager em = getEntityManager();
        em.remove(entity);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.persistence.EntityManager#setFlushMode(javax.persistence.FlushModeType)
     */
    public void setFlushMode(FlushModeType flushMode) {
        if (isTxActive()) {
            final EntityManager em = getEntityManager();
            em.setFlushMode(flushMode);
        }
    }
}
