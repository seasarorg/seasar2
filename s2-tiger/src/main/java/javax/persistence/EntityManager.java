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
package javax.persistence;

public interface EntityManager {
    public void persist(Object entity);

    public <T> T merge(T entity);

    public void remove(Object entity);

    public <T> T find(Class<T> entityClass, Object primaryKey);

    public <T> T getReference(Class<T> entityClass, Object primaryKey);

    public void flush();

    public void setFlushMode(FlushModeType flushMode);

    public FlushModeType getFlushMode();

    public void lock(Object entity, LockModeType lockMode);

    public void refresh(Object entity);

    public void clear();

    public boolean contains(Object entity);

    public Query createQuery(String qlString);

    public Query createNamedQuery(String name);

    public Query createNativeQuery(String sqlString);

    @SuppressWarnings("unchecked")
    public Query createNativeQuery(String sqlString, Class resultClass);

    public Query createNativeQuery(String sqlString, String resultSetMapping);

    public void joinTransaction();

    public Object getDelegate();

    public void close();

    public boolean isOpen();

    public EntityTransaction getTransaction();
}