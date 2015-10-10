/*
 * Copyright 2004-2015 the Seasar Foundation and the Others.
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
import javax.sql.DataSource;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;

/**
 * {@link EntityManager}のダミー実装クラスです。
 * 
 * @author koichik
 */
public class DummyEntityManager implements EntityManager {

    /** データソース */
    protected DataSource dataSource;

    /**
     * データソースを返します。
     * 
     * @return データソース
     */
    public DataSource getDataSource() {
        return dataSource;
    }

    /**
     * データソースを設定します。
     * 
     * @param dataSource
     *            データソース
     */
    @Binding(bindingType = BindingType.MUST)
    public void setDataSource(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void clear() {
    }

    public void close() {
    }

    public boolean contains(final Object entity) {
        throw new UnsupportedOperationException("contains");
    }

    public Query createNamedQuery(final String name) {
        throw new UnsupportedOperationException("createNamedQuery");
    }

    public Query createNativeQuery(final String sqlString) {
        throw new UnsupportedOperationException("createNativeQuery");
    }

    @SuppressWarnings("unchecked")
    public Query createNativeQuery(final String sqlString,
            final Class resultClass) {
        throw new UnsupportedOperationException("createNativeQuery");
    }

    public Query createNativeQuery(final String sqlString,
            final String resultSetMapping) {
        throw new UnsupportedOperationException("createNativeQuery");
    }

    public Query createQuery(final String qlString) {
        throw new UnsupportedOperationException("createQuery");
    }

    public <T> T find(final Class<T> entityClass, final Object primaryKey) {
        throw new UnsupportedOperationException("find");
    }

    public void flush() {
    }

    public Object getDelegate() {
        return this;
    }

    public FlushModeType getFlushMode() {
        throw new UnsupportedOperationException("getFlushMode");
    }

    public <T> T getReference(final Class<T> entityClass,
            final Object primaryKey) {
        throw new UnsupportedOperationException("getReference");
    }

    public EntityTransaction getTransaction() {
        throw new UnsupportedOperationException("getTransaction");
    }

    public boolean isOpen() {
        return true;
    }

    public void joinTransaction() {
    }

    public void lock(final Object entity, final LockModeType lockMode) {
        throw new UnsupportedOperationException("lock");
    }

    public <T> T merge(final T entity) {
        throw new UnsupportedOperationException("merge");
    }

    public void persist(final Object entity) {
        throw new UnsupportedOperationException("persist");
    }

    public void refresh(final Object entity) {
        throw new UnsupportedOperationException("refresh");
    }

    public void remove(final Object entity) {
        throw new UnsupportedOperationException("remove");
    }

    public void setFlushMode(final FlushModeType flushMode) {
        throw new UnsupportedOperationException("setFlushMode");
    }

}
