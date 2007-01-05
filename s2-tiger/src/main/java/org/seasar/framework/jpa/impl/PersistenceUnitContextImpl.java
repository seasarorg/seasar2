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

import java.util.concurrent.ConcurrentMap;

import javax.persistence.EntityManager;
import javax.transaction.Transaction;

import org.seasar.framework.jpa.PersistenceUnitContext;
import org.seasar.framework.util.tiger.CollectionsUtil;

/**
 * @author koichik
 */
public class PersistenceUnitContextImpl implements PersistenceUnitContext {

    protected ConcurrentMap<Transaction, EntityManager> entityManagers = CollectionsUtil
            .newConcurrentHashMap();

    public EntityManager getEntityManager(final Transaction tx) {
        return entityManagers.get(tx);
    }

    public void registerEntityManager(final Transaction tx,
            final EntityManager em) {
        entityManagers.put(tx, em);
    }

    public void unregisterEntityManager(final Transaction tx) {
        entityManagers.remove(tx);
    }

}
