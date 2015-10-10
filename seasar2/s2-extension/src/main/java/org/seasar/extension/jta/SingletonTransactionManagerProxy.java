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
package org.seasar.extension.jta;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.InvalidTransactionException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

import org.seasar.framework.container.factory.SingletonS2ContainerFactory;

/**
 * {@link SingletonS2ContainerFactory}を利用したトランザクションマネージャのプロキシです。
 * 
 * @author koichik
 */
public class SingletonTransactionManagerProxy implements TransactionManager {

    public void begin() throws NotSupportedException, SystemException {
        getTransactionManager().begin();
    }

    public void commit() throws HeuristicMixedException,
            HeuristicRollbackException, IllegalStateException,
            RollbackException, SecurityException, SystemException {
        getTransactionManager().commit();
    }

    public int getStatus() throws SystemException {
        return getTransactionManager().getStatus();
    }

    public Transaction getTransaction() throws SystemException {
        return getTransactionManager().getTransaction();
    }

    public void resume(final Transaction tx) throws IllegalStateException,
            InvalidTransactionException, SystemException {
        getTransactionManager().resume(tx);
    }

    public void rollback() throws IllegalStateException, SecurityException,
            SystemException {
        getTransactionManager().rollback();
    }

    public void setRollbackOnly() throws IllegalStateException, SystemException {
        getTransactionManager().setRollbackOnly();
    }

    public void setTransactionTimeout(final int timeout) throws SystemException {
        getTransactionManager().setTransactionTimeout(timeout);
    }

    public Transaction suspend() throws SystemException {
        return getTransactionManager().suspend();
    }

    /**
     * トランザクションマネージャを返します。
     * 
     * @return トランザクションマネージャ
     */
    protected TransactionManager getTransactionManager() {
        return (TransactionManager) SingletonS2ContainerFactory.getContainer()
                .getComponent(TransactionManager.class);
    }

}
