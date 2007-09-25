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

import javax.transaction.InvalidTransactionException;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.seasar.extension.tx.TransactionCallback;
import org.seasar.extension.tx.TransactionManagerAdapter;
import org.seasar.framework.exception.SIllegalStateException;
import org.seasar.framework.log.Logger;

/**
 * WebSphere version 5.0.2以降が提供するTransaction APIを使用してトランザクションを制御する、
 * {@link TransacstionManagerAdapter}の実装です。
 * 
 * @author koichik
 * @version 2.4.18
 */
public class WAS5TransactionManagerAdapter implements
        TransactionManagerAdapter, Status {

    /** REQUIREDトランザクション属性のためのフラグを示します */
    protected static final boolean[] REQUIRED = new boolean[] { false, false };

    /** REQUIRES_NEWトランザクション属性のためのフラグを示します */
    protected static final boolean[] REQUIRES_NEW = new boolean[] { false, true };

    /** NOT_SUPPORTEDトランザクション属性のためのフラグを示します */
    protected static final boolean[] NOT_SUPPORTED = new boolean[] { true,
            false };

    private static final Logger logger = Logger
            .getLogger(WAS5TransactionManagerAdapter.class);

    /** ユーザトランザクション */
    protected final UserTransaction userTransaction;

    /** WAS5 Transaction APIの提供するトランザクションコントロール */
    protected final TransactionControl transactionControl;

    /**
     * インスタンスを構築します。
     * 
     * @param userTransaction
     *            ユーザトランザクション
     * @param transactionControl
     *            WAS5 Transaction APIの提供するトランザクションコントロール
     */
    public WAS5TransactionManagerAdapter(final UserTransaction userTransaction,
            final TransactionControl transactionControl) {
        this.userTransaction = userTransaction;
        this.transactionControl = transactionControl;
    }

    public Object required(final TransactionCallback callback) throws Throwable {
        return executeCallback(callback, REQUIRED);
    }

    public Object requiresNew(final TransactionCallback callback)
            throws Throwable {
        return executeCallback(callback, REQUIRES_NEW);
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
        return executeCallback(callback, NOT_SUPPORTED);
    }

    public Object never(final TransactionCallback callback) throws Throwable {
        if (hasTransaction()) {
            throw new SIllegalStateException("ESSR0317", null);
        }
        return callback.execute(this);
    }

    public void setRollbackOnly() {
        try {
            if (hasTransaction()) {
                userTransaction.setRollbackOnly();
            }
        } catch (final Exception e) {
            logger.log("ESSR0017", new Object[] { e.getMessage() }, e);
        }
    }

    /**
     * トランザクション制御下でトランザクションコールバックを呼び出します。
     * 
     * @param callback
     *            トランザクションコールバック
     * @param txAttribute
     *            トランザクション属性
     * @return トランザクションコールバックの戻り値
     * @throws Throwable
     *             トランザクションコールバックが例外をスローした場合
     */
    protected Object executeCallback(final TransactionCallback callback,
            final boolean[] txAttribute) throws Throwable {
        final TxHandle txHandle = transactionControl.preinvoke(txAttribute[0],
                txAttribute[1]);
        try {
            return callback.execute(WAS5TransactionManagerAdapter.this);
        } finally {
            if (getStatus() == STATUS_ACTIVE) {
                transactionControl.postinvoke(txHandle);
            } else {
                transactionControl.handleException(txHandle);
            }
        }
    }

    /**
     * 現在のスレッド上でトランザクションがアクティブな場合は<code>true</code>を、 それ以外の場合は<code>false</code>を返します。
     * 
     * @return 現在のスレッド上でトランザクションがアクティブな場合は<code>true</code>
     * @throws SystemException
     *             ユーザトランザクションで例外が発生した場合にスローされます
     * @see javax.transaction.UserTransaction#getStatus()
     */
    protected boolean hasTransaction() throws SystemException {
        final int status = userTransaction.getStatus();
        return status != STATUS_NO_TRANSACTION && status != STATUS_UNKNOWN;
    }

    /**
     * トランザクションの状態を返します。
     * 
     * @return トランザクションの状態
     * @see UserTransaction#getStatus()
     */
    protected int getStatus() {
        try {
            return userTransaction.getStatus();
        } catch (final Throwable e) {
            return STATUS_UNKNOWN;
        }
    }

    public static interface ExtendedJTATransaction {
        byte[] getGlobalId();

        int getLocalId();

        void registerSynchronizationCallback(SynchronizationCallback sync)
                throws NotSupportedException;

        void registerSynchronizationCallbackForCurrentTran(
                SynchronizationCallback sync) throws NotSupportedException;

        void unRegisterSynchronizationCallback(SynchronizationCallback sync)
                throws CallbackNotRegisteredException;
    }

    public static interface TransactionControl {
        TxHandle preinvoke(boolean forceLocalTran, boolean forceGlobalTran)
                throws NotSupportedException, SystemException;

        void postinvoke(TxHandle status) throws InvalidTransactionException,
                SystemException;

        void handleException(TxHandle status);
    }

    public interface TxHandle {
    }

    public interface SynchronizationCallback {
        void beforeCompletion(int localId, byte[] globalId);

        void afterCompletion(int localId, byte[] globalId, boolean committed);
    }

    public static class NotSupportedException extends Exception {
    }

    public static class CallbackNotRegisteredException extends Exception {
    }

}
