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
package org.seasar.extension.jta;

import javax.transaction.InvalidTransactionException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;
import javax.transaction.TransactionSynchronizationRegistry;
import javax.transaction.UserTransaction;

/**
 * 機能が限定された{@link TransactionManager}の実装クラスです。
 * <p>
 * このトランザクションマネージャは、{@link UserTransaction}と{@link TransactionSynchronizationRegistry}を利用して実装しています。
 * そのため、以下の機能がサポートされません。
 * </p>
 * <ul>
 * <li>{@link TransactionManager#resume(Transaction)}</li>
 * <li>{@link TransactionManager#suspend()}</li>
 * </ul>
 * 
 * @author koichik
 */
public class RestrictedTransactionManagerImpl extends
        AbstractTransactionManagerImpl {

    /** ユーザトランザクション */
    protected UserTransaction userTransaction;

    /** トランザクションシンクロナイゼーションレジストリ */
    protected TransactionSynchronizationRegistry synchronizationRegistry;

    /**
     * インスタンスを構築します。
     */
    public RestrictedTransactionManagerImpl() {
    }

    /**
     * インスタンスを構築します。
     * 
     * @param userTransaction
     *            ユーザトランザクション
     * @param synchronizationRegistry
     *            トランザクションシンクロナイゼーションレジストリ
     */
    public RestrictedTransactionManagerImpl(UserTransaction userTransaction,
            TransactionSynchronizationRegistry synchronizationRegistry) {
        this.userTransaction = userTransaction;
        this.synchronizationRegistry = synchronizationRegistry;
    }

    /**
     * ユーザトランザクションを設定します。
     * 
     * @param userTransaction
     *            ユーザトランザクション
     */
    public void setUserTransaction(final UserTransaction userTransaction) {
        this.userTransaction = userTransaction;
    }

    /**
     * トランザクションシンクロナイゼーションレジストリを設定します。
     * 
     * @param synchronizationRegistry
     *            トランザクションシンクロナイゼーションレジストリ
     */
    public void setSynchronizationRegistry(
            final TransactionSynchronizationRegistry synchronizationRegistry) {
        this.synchronizationRegistry = synchronizationRegistry;
    }

    public void setTransactionTimeout(final int seconds) throws SystemException {
        userTransaction.setTransactionTimeout(seconds);
    }

    public void resume(final Transaction tx) throws IllegalStateException,
            InvalidTransactionException, SystemException {
        throw new UnsupportedOperationException("resume");
    }

    public Transaction suspend() throws SystemException {
        throw new UnsupportedOperationException("suspend");
    }

    protected ExtendedTransaction createTransaction() {
        return new RestrictedTransactionImpl(userTransaction,
                synchronizationRegistry);
    }

}
