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
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.Synchronization;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;
import javax.transaction.TransactionSynchronizationRegistry;
import javax.transaction.UserTransaction;
import javax.transaction.xa.XAResource;

/**
 * 機能が限定された{@link Transaction}の実装クラスです。
 * <p>
 * このトランザクションは、{@link UserTransaction}と{@link TransactionSynchronizationRegistry}を利用して実装しています。
 * そのため、以下の機能がサポートされません。
 * </p>
 * <ul>
 * <li>{@link TransactionManager#resume(Transaction)}</li>
 * <li>{@link TransactionManager#suspend()}</li>
 * </ul>
 * 
 * @author koichik
 */
public class RestrictedTransactionImpl implements Transaction {

    /** ユーザトランザクション */
    protected UserTransaction userTransaction;

    /** トランザクションシンクロナイゼーションレジストリ */
    protected TransactionSynchronizationRegistry synchronizationRegistry;

    /**
     * トランザクションのインスタンスを構築します。
     * 
     * @param userTransaction
     *            ユーザトランザクション
     * @param synchronizationRegistry
     *            トランザクションシンクロナイゼーションレジストリ
     */
    public RestrictedTransactionImpl(final UserTransaction userTransaction,
            final TransactionSynchronizationRegistry synchronizationRegistry) {
        this.userTransaction = userTransaction;
        this.synchronizationRegistry = synchronizationRegistry;
    }

    /**
     * トランザクションを開始します。
     * 
     * @throws SystemException
     * @throws NotSupportedException
     * 
     */
    public void begin() throws NotSupportedException, SystemException {
        userTransaction.begin();
    }

    public void commit() throws HeuristicMixedException,
            HeuristicRollbackException, RollbackException, SecurityException,
            SystemException {
        userTransaction.commit();
    }

    public boolean delistResource(final XAResource xaRes, final int flag)
            throws IllegalStateException, SystemException {
        throw new UnsupportedOperationException("delistResource");
    }

    public boolean enlistResource(final XAResource xaRes)
            throws IllegalStateException, RollbackException, SystemException {
        throw new UnsupportedOperationException("enlistResource");
    }

    public int getStatus() throws SystemException {
        return userTransaction.getStatus();
    }

    public void registerSynchronization(final Synchronization sync)
            throws IllegalStateException, RollbackException, SystemException {
        synchronizationRegistry.registerInterposedSynchronization(sync);
    }

    public void rollback() throws IllegalStateException, SystemException {
        userTransaction.rollback();
    }

    public void setRollbackOnly() throws IllegalStateException, SystemException {
        userTransaction.setRollbackOnly();
    }

}
