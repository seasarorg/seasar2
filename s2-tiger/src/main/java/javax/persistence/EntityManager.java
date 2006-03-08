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
    /**
     * Make an instance managed and persistent.
     * 
     * @param entity
     * @throws IllegalArgumentException
     *             if not an entity or entity is detached
     * @throws TransactionRequiredException
     *             if there is no transaction and the persistence context is of
     *             type PersistenceContextType.TRANSACTION
     */
    public void persist(Object entity);

    /**
     * Merge the state of the given entity into the current persistence context.
     * 
     * @param entity
     * @return the instance that the state was merged to
     * @throws IllegalArgumentException
     *             if instance is not an entity or is a removed entity
     * @throws TransactionRequiredException
     *             if there is no transaction and the persistence context is of
     *             type PersistenceContextType.TRANSACTION
     */
    public <T> T merge(T entity);

    /**
     * Remove the entity instance.
     * 
     * @param entity
     * @throws IllegalArgumentException
     *             if not an entity or if a detached entity
     * @throws TransactionRequiredException
     *             if there is no transaction and the persistence context is of
     *             type PersistenceContextType.TRANSACTION
     */
    public void remove(Object entity);

    /**
     * Find by primary key.
     * 
     * @param entityClass
     * @param primaryKey
     * @return the found entity instance or null if the entity does not exist
     * @throws IllegalArgumentException
     *             if the first argument does not denote an entity type or the
     *             second argument is not a valid type for that entity’s primary
     *             key
     */
    public <T> T find(Class<T> entityClass, Object primaryKey);

    /**
     * Get an instance, whose state may be lazily fetched. If the requested
     * instance does not exist in the database, throws EntityNotFoundException
     * when the instance state is first accessed. (The persistence provider
     * runtime is permitted to throw the EntityNotFoundException when
     * getReference is called.) The application should not expect that the
     * instance state will be available upon detachment, unless it was accessed
     * by the application while the entity manager was open.
     * 
     * @param entityClass
     * @param primaryKey
     * @return the found entity instance
     * @throws IllegalArgumentException
     *             if the first argument does not denote an entity type or the
     *             second argument is not a valid type for that entity’s primary
     *             key
     * @throws EntityNotFoundException
     *             if the entity state cannot be accessed
     */
    public <T> T getReference(Class<T> entityClass, Object primaryKey);

    /**
     * Synchronize the persistence context to the underlying database.
     * 
     * @throws TransactionRequiredException
     *             if there is no transaction
     * @throws PersistenceException
     *             if the flush fails
     */
    public void flush();

    /**
     * Set the flush mode that applies to all objects contained in the
     * persistence context.
     * 
     * @param flushMode
     */
    public void setFlushMode(FlushModeType flushMode);

    /**
     * Get the flush mode that applies to all objects contained in the
     * persistence context.
     * 
     * @return flushMode
     */
    public FlushModeType getFlushMode();

    /**
     * Set the lock mode for an entity object contained in the persistence
     * context.
     * 
     * @param entity
     * @param lockMode
     * @throws PersistenceException
     *             if an unsupported lock call is made
     * @throws IllegalArgumentException
     *             if the instance is not an entity or is a detached entity
     * @throws TransactionRequiredException
     *             if there is no transaction
     */
    public void lock(Object entity, LockModeType lockMode);

    /**
     * Refresh the state of the instance from the database, overwriting changes
     * made to the entity, if any.
     * 
     * @param entity
     * @throws IllegalArgumentException
     *             if not an entity or entity is not managed
     * @throws TransactionRequiredException
     *             if there is no transaction and the persistence context is of
     *             type PersistenceContextType.TRANSACTION
     * @throws EntityNotFoundException
     *             if the entity no longer exists in the database
     */
    public void refresh(Object entity);

    /**
     * Clear the persistence context, causing all managed entities to become
     * detached. Changes made to entities that have not been flushed to the
     * database will not be persisted.
     */
    public void clear();

    /**
     * Check if the instance belongs to the current persistence context.
     * 
     * @param entity
     * @return
     * @throws IllegalArgumentException
     *             if not an entity
     */
    public boolean contains(Object entity);

    /**
     * Create an instance of Query for executing an EJB QL statement.
     * 
     * @param ejbqlString
     *            an EJB QL query string
     * @return the new query instance
     * @throws IllegalArgumentException
     *             if query string is not valid
     */
    public Query createQuery(String ejbqlString);

    /**
     * Create an instance of Query for executing a named query (in EJB QL or
     * native SQL).
     * 
     * @param name
     *            the name of a query defined in metadata
     * @return the new query instance
     * @throws IllegalArgumentException
     *             if a query has not been defined with the given name
     */
    public Query createNamedQuery(String name);

    /**
     * Create an instance of Query for executing a native SQL statement, e.g.,
     * for update or delete.
     * 
     * @param sqlString
     *            a native SQL query string
     * @return the new query instance
     */
    public Query createNativeQuery(String sqlString);

    /**
     * Create an instance of Query for executing a native SQL query.
     * 
     * @param sqlString
     *            a native SQL query string
     * @param resultClass
     *            the class of the resulting instance(s)
     * @return the new query instance
     */
    public Query createNativeQuery(String sqlString, Class resultClass);

    /**
     * Create an instance of Query for executing a native SQL query.
     * 
     * @param sqlString
     *            a native SQL query string
     * @param resultSetMapping
     *            the name of the result set mapping
     * @return the new query instance
     */
    public Query createNativeQuery(String sqlString, String resultSetMapping);

    /**
     * Close an application-managed EntityManager. After an EntityManager has
     * been closed, all methods on the EntityManager instance will throw the
     * IllegalStateException except for isOpen, which will return false. This
     * method can only be called when the EntityManager is not associated with
     * an active transaction.
     * 
     * @throws IllegalStateException
     *             if the EntityManager is associated with an active transaction
     *             or if the EntityManager is container-managed.
     */
    public void close();

    /**
     * Determine whether the EntityManager is open.
     * 
     * @return true until the EntityManager has been closed.
     */
    public boolean isOpen();

    /**
     * Return the resource-level transaction object. The EntityTransaction
     * instance may be used serially to begin and commit multiple transactions.
     * 
     * @return EntityTransaction instance
     * @throws IllegalStateException
     *             if invoked on a JTA EntityManager or an EntityManager that
     *             has been closed.
     */
    public EntityTransaction getTransaction();
}