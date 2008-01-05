/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Query;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.jpa.EntityManagerProvider;
import org.seasar.framework.util.StringUtil;

/**
 * 複数の{@link EntityManager}から任意の{@link EntityManager}を選択し処理を委譲するプロキシです。
 * <p>
 * 委譲先の{@link EntityManager}は{@link EntityManagerProvider}から取得します。
 * </p>
 * 
 * @author koichik
 */
@Component
public class SelectableEntityManagerProxy implements EntityManager {

    /** {@link EntityManagerProvider}のインスタンス */
    @Binding(bindingType = BindingType.MUST)
    protected EntityManagerProvider entityManagerProvider;

    /**
     * インスタンスを構築します。
     */
    public SelectableEntityManagerProxy() {
    }

    /**
     * {@link EntityManager}のコンポーネントを返します。
     * 
     * @return {@link EntityManager}のコンポーネント
     */
    protected EntityManager getEntityManager() {
        final String prefix = entityManagerProvider
                .getSelectableEntityManagerPrefix();
        if (StringUtil.isEmpty(prefix)) {
            throw new EmptyRuntimeException("entityManagerName");
        }
        return entityManagerProvider.getEntityManger(prefix);
    }

    public void clear() {
        final EntityManager em = getEntityManager();
        em.clear();
    }

    public void close() {
        final EntityManager em = getEntityManager();
        em.close();
    }

    public boolean contains(final Object entity) {
        final EntityManager em = getEntityManager();
        return em.contains(entity);
    }

    public Query createNamedQuery(final String name) {
        final EntityManager em = getEntityManager();
        return em.createNamedQuery(name);
    }

    public Query createNativeQuery(final String sqlString) {
        final EntityManager em = getEntityManager();
        return em.createNativeQuery(sqlString);
    }

    @SuppressWarnings("unchecked")
    public Query createNativeQuery(final String sqlString,
            final Class resultClass) {
        final EntityManager em = getEntityManager();
        return em.createNativeQuery(sqlString, resultClass);
    }

    public Query createNativeQuery(final String sqlString,
            final String resultSetMapping) {
        final EntityManager em = getEntityManager();
        return em.createNativeQuery(sqlString, resultSetMapping);
    }

    public Query createQuery(final String qlString) {
        final EntityManager em = getEntityManager();
        return em.createQuery(qlString);
    }

    public <T> T find(final Class<T> entityClass, final Object primaryKey) {
        final EntityManager em = getEntityManager();
        return em.find(entityClass, primaryKey);
    }

    public void flush() {
        final EntityManager em = getEntityManager();
        em.flush();
    }

    public Object getDelegate() {
        final EntityManager em = getEntityManager();
        return em.getDelegate();
    }

    public FlushModeType getFlushMode() {
        final EntityManager em = getEntityManager();
        return em.getFlushMode();
    }

    public <T> T getReference(final Class<T> entityClass,
            final Object primaryKey) {
        final EntityManager em = getEntityManager();
        return em.getReference(entityClass, primaryKey);
    }

    public EntityTransaction getTransaction() {
        final EntityManager em = getEntityManager();
        return em.getTransaction();
    }

    public boolean isOpen() {
        final EntityManager em = getEntityManager();
        return em.isOpen();
    }

    public void joinTransaction() {
        final EntityManager em = getEntityManager();
        em.joinTransaction();
    }

    public void lock(final Object entity, final LockModeType lockMode) {
        final EntityManager em = getEntityManager();
        em.lock(entity, lockMode);
    }

    public <T> T merge(final T entity) {
        final EntityManager em = getEntityManager();
        return em.merge(entity);
    }

    public void persist(final Object entity) {
        final EntityManager em = getEntityManager();
        em.persist(entity);
    }

    public void refresh(final Object entity) {
        final EntityManager em = getEntityManager();
        em.refresh(entity);
    }

    public void remove(final Object entity) {
        final EntityManager em = getEntityManager();
        em.remove(entity);
    }

    public void setFlushMode(final FlushModeType flushMode) {
        final EntityManager em = getEntityManager();
        em.setFlushMode(flushMode);
    }

}
