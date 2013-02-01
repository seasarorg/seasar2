/*
 * Copyright 2004-2013 the Seasar Foundation and the Others.
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

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Query;
import javax.persistence.TransactionRequiredException;
import javax.transaction.Status;
import javax.transaction.Synchronization;
import javax.transaction.TransactionSynchronizationRegistry;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.jpa.PersistenceUnitManager;
import org.seasar.framework.log.Logger;

/**
 * コンテナ管理{@link javax.persistence.EntityManager}の実装です。
 * <p>
 * このクラスは次のタイプの{@link javax.persistence.EntityManager}となります。
 * <ul>
 * <li>コンテナ管理 (container-managed entity manager)</li>
 * <li>JTAトランザクション (JTA entity manager)</li>
 * <li>トランザクションスコープ (transaction-scoped persistence contexts)</li>
 * </ul>
 * </p>
 * 
 * @author koichik
 */
@Component
public class TxScopedEntityManagerProxy implements EntityManager {

    private static final Logger logger = Logger
            .getLogger(TxScopedEntityManagerProxy.class);

    /** トランザクションシンクロナイゼーションレジストリ */
    @Binding(bindingType = BindingType.MUST)
    protected TransactionSynchronizationRegistry tsr;

    /** 永続マネージャファクトリ */
    @Binding(bindingType = BindingType.MUST)
    protected EntityManagerFactory emf;

    /** 永続ユニットマネージャ */
    @Binding(bindingType = BindingType.MUST)
    protected PersistenceUnitManager pum;

    /**
     * インスタンスを構築します。
     */
    public TxScopedEntityManagerProxy() {
    }

    /**
     * トランザクションが活動中の場合<code>true</code>を返します。
     * 
     * @return トランザクションが活動中の場合<code>true</code>、そうでない場合<code>false</code>
     */
    protected boolean isTxActive() {
        return tsr.getTransactionStatus() != Status.STATUS_NO_TRANSACTION;
    }

    /**
     * トランザクションが活動中であることをアサートします。
     * 
     * @exception TransactionRequiredException
     *                トランザクションが活動中でない場合
     */
    protected void assertTxActive() {
        if (!isTxActive()) {
            throw new TransactionRequiredException();
        }
    }

    /**
     * エンティティマネージャを返します。
     * 
     * @return エンティティマネージャ
     */
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

    /**
     * トランザクションに関連付けられたエンティティマネージャを返します。
     * 
     * @return トランザクションに関連付けられたエンティティマネージャ
     */
    protected EntityManager getTxBoundEntityManager() {
        if (!isTxActive()) {
            return null;
        }
        return EntityManager.class.cast(tsr.getResource(emf));
    }

    /**
     * エンティティマネージャを作成します。
     * 
     * @return エンティティマネージャ
     */
    protected EntityManager createEntityManager() {
        final EntityManager em = emf.createEntityManager();
        tsr.putResource(emf, em);
        tsr.registerInterposedSynchronization(new Synchronization() {

            public void beforeCompletion() {
            }

            public void afterCompletion(final int status) {
                try {
                    em.close();
                } catch (final Throwable t) {
                    logger.log("ESSR0017", new Object[] { t }, t);
                }
            }
        });
        return em;
    }

    public void clear() {
        if (isTxActive()) {
            final EntityManager em = getEntityManager();
            em.clear();
        }
    }

    public void close() {
        throw new IllegalStateException();
    }

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

    @SuppressWarnings("unchecked")
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

    public void flush() {
        if (isTxActive()) {
            final EntityManager em = getEntityManager();
            em.flush();
        }
    }

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

    public EntityTransaction getTransaction() {
        throw new IllegalStateException();
    }

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

    public void joinTransaction() {
        assertTxActive();
    }

    public void lock(final Object entity, final LockModeType lockMode) {
        assertTxActive();
        final EntityManager em = getEntityManager();
        em.lock(entity, lockMode);
    }

    public <T> T merge(final T entity) {
        assertTxActive();
        final EntityManager em = getEntityManager();
        return em.merge(entity);
    }

    public void persist(final Object entity) {
        assertTxActive();
        final EntityManager em = getEntityManager();
        em.persist(entity);
    }

    public void refresh(final Object entity) {
        assertTxActive();
        final EntityManager em = getEntityManager();
        em.refresh(entity);
    }

    public void remove(final Object entity) {
        assertTxActive();
        final EntityManager em = getEntityManager();
        em.remove(entity);
    }

    public void setFlushMode(final FlushModeType flushMode) {
        if (isTxActive()) {
            final EntityManager em = getEntityManager();
            em.setFlushMode(flushMode);
        }
    }

}
