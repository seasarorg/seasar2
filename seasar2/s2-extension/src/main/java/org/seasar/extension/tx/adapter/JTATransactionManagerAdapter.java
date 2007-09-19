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
package org.seasar.extension.tx.adapter;

import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

import org.seasar.extension.tx.TransactionCallback;
import org.seasar.extension.tx.TransactionCordinator;
import org.seasar.extension.tx.TransactionManagerAdapter;
import org.seasar.framework.exception.SIllegalStateException;
import org.seasar.framework.log.Logger;

/**
 * JTAの{@link TransactionManager}を使用してトランザクションを制御する、
 * {@link TransactionManagerAdapter}の実装です。
 * 
 * @author koichik
 * @version 2.4.18
 */
public class JTATransactionManagerAdapter implements TransactionManagerAdapter,
        TransactionCordinator {

    private static final Logger logger = Logger
            .getLogger(JTATransactionManagerAdapter.class);

    /** <coce>transactionManager</code>プロパティのバインディング定義です。 */
    public static final String transactionManager_BINDING = "bindingType=must";

    /** トランザクションマネージャ */
    protected TransactionManager transactionManager;

    /**
     * トランザクションマネージャを設定します。
     * 
     * @param transactionManager
     *            トランザクションマネージャ
     */
    public void setTransactionManager(
            final TransactionManager transactionManager) {
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
        final Transaction tx = hasTransaction() ? suspend() : null;
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
        final Transaction tx = hasTransaction() ? suspend() : null;
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
            transactionManager.setRollbackOnly();
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
        return transactionManager.getStatus() != Status.STATUS_NO_TRANSACTION;
    }

    /**
     * トランザクションを開始します。
     * <p>
     * トランザクションを開始した場合は<code>true</code>、それ以外の場合は<code>false</code>を返します。
     * </p>
     * 
     * @return トランザクションを開始した場合は<code>true</code>
     * @throws Exception
     *             トランザクションマネージャで例外が発生した場合にスローされます
     * @see javax.transaction.TransactionManager#begin()
     */
    protected boolean begin() throws Exception {
        if (hasTransaction()) {
            return false;
        }
        transactionManager.begin();
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
        if (transactionManager.getStatus() == Status.STATUS_ACTIVE) {
            transactionManager.commit();
        } else {
            transactionManager.rollback();
        }
    }

    /**
     * トランザクションを中断します。
     * 
     * @return 中断された{@link javax.transaction.Transaction トランザクション}
     * @throws Exception
     *             トランザクションマネージャで例外が発生した場合にスローされます
     * @see javax.transaction.TransactionManager#suspend()
     */
    protected Transaction suspend() throws Exception {
        return transactionManager.suspend();
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
