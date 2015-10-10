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
package org.seasar.extension.tx.adapter;

import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

import org.seasar.extension.tx.TransactionCallback;
import org.seasar.extension.tx.TransactionManagerAdapter;
import org.seasar.framework.exception.SIllegalStateException;
import org.seasar.framework.log.Logger;

/**
 * JTAの{@link TransactionManager}を使用してトランザクションを制御する、
 * {@link TransactionManagerAdapter}の実装です。
 * 
 * @author koichik
 * @since 2.4.18
 */
public class JTATransactionManagerAdapter implements TransactionManagerAdapter,
        Status {

    private static final Logger logger = Logger
            .getLogger(JTATransactionManagerAdapter.class);

    /** ユーザトランザクション */
    protected final UserTransaction userTransaction;

    /** トランザクションマネージャ */
    protected final TransactionManager transactionManager;

    /**
     * インスタンスを構築します。
     * 
     * @param userTransaction
     *            ユーザトランザクション
     * @param transactionManager
     *            トランザクションマネージャ
     */
    public JTATransactionManagerAdapter(final UserTransaction userTransaction,
            final TransactionManager transactionManager) {
        this.userTransaction = userTransaction;
        this.transactionManager = transactionManager;
    }

    public Object required(final TransactionCallback callback) throws Throwable {
        final boolean began = begin();
        try {
            return callback.execute(this);
        } finally {
            if (began) {
                end();
            }
        }
    }

    public Object requiresNew(final TransactionCallback callback)
            throws Throwable {
        final Transaction tx = suspend();
        try {
            begin();
            try {
                return callback.execute(this);
            } finally {
                end();
            }
        } finally {
            if (tx != null) {
                resume(tx);
            }
        }
    }

    public Object mandatory(final TransactionCallback callback)
            throws Throwable {
        if (!hasTransaction()) {
            throw new SIllegalStateException("ESSR0311", null);
        }
        return callback.execute(this);
    }

    public Object notSupported(final TransactionCallback callback)
            throws Throwable {
        final Transaction tx = suspend();
        try {
            return callback.execute(this);
        } finally {
            if (tx != null) {
                resume(tx);
            }
        }
    }

    public Object never(final TransactionCallback callback) throws Throwable {
        if (hasTransaction()) {
            throw new SIllegalStateException("ESSR0317", null);
        }
        return callback.execute(this);
    }

    public void setRollbackOnly() {
        try {
            if (userTransaction.getStatus() == STATUS_ACTIVE) {
                userTransaction.setRollbackOnly();
            }
        } catch (final Exception e) {
            logger.log("ESSR0017", new Object[] { e.getMessage() }, e);
        }
    }

    /**
     * 現在のスレッド上でトランザクションがアクティブな場合は<code>true</code>を、それ以外の場合は<code>false</code>を返します。
     * 
     * @return 現在のスレッド上でトランザクションがアクティブな場合は<code>true</code>
     * @throws SystemException
     *             トランザクションマネージャで例外が発生した場合にスローされます
     * @see javax.transaction.UserTransaction#getStatus()
     */
    protected boolean hasTransaction() throws SystemException {
        final int status = userTransaction.getStatus();
        return status != STATUS_NO_TRANSACTION && status != STATUS_UNKNOWN;
    }

    /**
     * トランザクションを開始します。
     * <p>
     * 新しいトランザクションを開始した場合は<code>true</code>、それ以外の場合は<code>false</code>を返します。
     * </p>
     * 
     * @return 新しいトランザクションを開始した場合は<code>true</code>
     * @throws Exception
     *             トランザクションマネージャで例外が発生した場合にスローされます
     * @see javax.transaction.TransactionManager#begin()
     */
    protected boolean begin() throws Exception {
        if (hasTransaction()) {
            return false;
        }
        userTransaction.begin();
        return true;
    }

    /**
     * トランザクションをコミットまたはロールバックします。
     * <p>
     * 現在のスレッドに関連づけられているトランザクションがアクティブな場合は、 トランザクションをコミットします。
     * それ以外の場合はトランザクションをロールバックします。
     * </p>
     * 
     * @throws Exception
     *             トランザクションマネージャで例外が発生した場合にスローされます
     * @see javax.transaction.TransactionManager#commit()
     * @see javax.transaction.TransactionManager#rollback()
     */
    protected void end() throws Exception {
        if (userTransaction.getStatus() == STATUS_ACTIVE) {
            userTransaction.commit();
        } else {
            userTransaction.rollback();
        }
    }

    /**
     * トランザクションを中断します。
     * <p>
     * 現在のスレッド上でトランザクションが開始されていなければ<code>null</code>を返します。
     * </p>
     * 
     * @return 中断された{@link javax.transaction.Transaction トランザクション}
     * @throws Exception
     *             トランザクションマネージャで例外が発生した場合にスローされます
     * @see javax.transaction.TransactionManager#suspend()
     */
    protected Transaction suspend() throws Exception {
        return hasTransaction() ? transactionManager.suspend() : null;
    }

    /**
     * トランザクションを再開します。
     * 
     * @param transaction
     *            再開する{@link javax.transaction.Transaction トランザクション}
     * @throws Exception
     *             トランザクションマネージャで例外が発生した場合にスローされます
     * @see javax.transaction.TransactionManager#resume(Transaction)
     */
    protected void resume(final Transaction transaction) throws Exception {
        transactionManager.resume(transaction);
    }

}
