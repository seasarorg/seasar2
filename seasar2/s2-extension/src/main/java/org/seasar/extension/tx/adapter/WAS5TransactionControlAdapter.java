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
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import org.seasar.extension.tx.TransactionCallback;
import org.seasar.extension.tx.TransactionCordinator;
import org.seasar.extension.tx.TransactionManagerAdapter;
import org.seasar.framework.exception.SIllegalStateException;

/**
 * WebSphere version 5.0.2以降が提供するTransaction SPIを使用してトランザクションを制御する、
 * {@link TransacstionManagerAdapter}の実装です。
 * 
 * @author koichik
 * @version 2.4.18
 */
public class WAS5TransactionControlAdapter implements TransactionManagerAdapter {

    /** REQUIREDトランザクション属性のためのフラグを示します */
    protected static final boolean[] REQUIRED = new boolean[] { false, false };

    /** REQUIRES_NEWトランザクション属性のためのフラグを示します */
    protected static final boolean[] REQUIRES_NEW = new boolean[] { false, true };

    /** NOT_SUPPORTEDトランザクション属性のためのフラグを示します */
    protected static final boolean[] NOT_SUPPORTED = new boolean[] { true,
            false };

    /** 現在のスレッドに関連づけられている{@link TransactionCordinator} */
    protected static final ThreadLocal currentCordinator = new ThreadLocal();

    /** 何もしない{@link TransactionCordinator}の実装 */
    protected static final TransactionCordinator NULL_CORDINATOR = new TransactionCordinator() {
        public void setRollbackOnly() {
        }
    };

    /** <coce>transactionControl</code>プロパティのバインディング定義です。 */
    public static final String transactionControl_BINDING = "bindingType=must";

    /** WAS5 Transaction APIの提供するトランザクションコントロール */
    protected TransactionControl transactionControl;

    /**
     * インスタンスを構築します。
     */
    public WAS5TransactionControlAdapter() {
    }

    /**
     * WAS5 Transaction APIの提供するトランザクションコントロールを設定します。
     * 
     * @param transactionControl
     *            WAS5 Transaction APIの提供するトランザクションコントロール
     */
    public void setTransactionControl(
            final TransactionControl transactionControl) {
        this.transactionControl = transactionControl;
    }

    public Object required(final TransactionCallback callback) throws Throwable {
        if (hasTransaction()) {
            return callback.execute(getCurrentCordinator());
        }
        return execute(callback, REQUIRED);
    }

    public Object requiresNew(final TransactionCallback callback)
            throws Throwable {
        return execute(callback, REQUIRES_NEW);
    }

    public Object mandatory(final TransactionCallback callback)
            throws Throwable {
        if (!hasTransaction()) {
            throw new SIllegalStateException("ESSR0311", null);
        }
        return callback.execute(getCurrentCordinator());
    }

    public Object notSupported(final TransactionCallback callback)
            throws Throwable {
        return execute(callback, NOT_SUPPORTED);
    }

    public Object never(final TransactionCallback callback) throws Throwable {
        if (hasTransaction()) {
            throw new SIllegalStateException("ESSR0317", null);
        }
        return callback.execute(NULL_CORDINATOR);
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
    protected Object execute(final TransactionCallback callback,
            final boolean[] txAttribute) throws Throwable {
        final TransactionCordinatorImpl action = new TransactionCordinatorImpl(
                callback);
        return action.run(txAttribute[0], txAttribute[1]);
    }

    /**
     * 現在のスレッド上でトランザクションがアクティブな場合は<code>true</code>を、 それ以外の場合は<code>false</code>を返します。
     * 
     * @return 現在のスレッド上でトランザクションがアクティブな場合は<code>true</code>
     */
    protected boolean hasTransaction() {
        return currentCordinator.get() != null;
    }

    /**
     * 現在のスレッドに関連づけられている{@link TransactionCordinator}を返します。
     * 
     * @return 現在のスレッドに関連づけられている{@link TransactionCordinator}
     */
    protected TransactionCordinator getCurrentCordinator() {
        return (TransactionCordinator) currentCordinator.get();
    }

    /**
     * 現在のスレッドに{@link TransactionCordinator}を関連づけます。
     * 
     * @param cordinator
     *            新たに現在のスレッド上に関連づけられた{@link TransactionCordinator}
     * @return 現在のスレッドから切り離された{@link TransactionCordinator}
     */
    protected TransactionCordinator enter(final TransactionCordinator cordinator) {
        final TransactionCordinator current = getCurrentCordinator();
        currentCordinator.set(cordinator);
        return current;
    }

    /**
     * 現在のスレッドから{@link TransactionCordinator}を切り離し、以前に関連付けられていた<code>TransactionControl</code>を関連づけます。
     * 
     * @param suspended
     *            以前に現在のスレッドに関連づけられた{@link TransactionCordinator}
     */
    protected void leave(final TransactionCordinator suspended) {
        currentCordinator.set(suspended);
    }

    /**
     * <code>TransactionControl</code>が制御するトランザクション中に実行されるアクションです。
     * 
     * @author koichik
     */
    public class TransactionCordinatorImpl implements TransactionCordinator {

        /** トランザクションコールバック */
        protected TransactionCallback callback;

        /** トランザクションをロールバックするようマークされていれば<code>true</code> */
        protected boolean rollbackOnly;

        /**
         * インスタンスを構築します。
         * 
         * @param callback
         *            トランザクションコールバック
         */
        public TransactionCordinatorImpl(final TransactionCallback callback) {
            this.callback = callback;
        }

        /**
         * <code>UOWManager</code>から呼び出されます。
         * 
         * @param forceLocalTx
         *            新たにローカルトランザクションを開始する場合に<code>true</code>
         * @param forceGlobalTx
         *            新たにグローバルトランザクションを開始する場合に<code>true</code>
         * @return トランザクションコールバックの戻り値
         * @throws Throwable
         *             トランザクションコールバックが例外をスローした場合
         */
        public Object run(final boolean forceLocalTx,
                final boolean forceGlobalTx) throws Throwable {
            final TransactionCordinator suspended = enter(forceLocalTx ? null
                    : this);
            try {
                final TxHandle txHandle = transactionControl.preinvoke(
                        forceLocalTx, forceGlobalTx);
                try {
                    return callback.execute(this);
                } finally {
                    if (!rollbackOnly) {
                        transactionControl.postinvoke(txHandle);
                    } else {
                        transactionControl.handleException(txHandle);
                    }
                }
            } finally {
                leave(suspended);
            }
        }

        public void setRollbackOnly() {
            this.rollbackOnly = true;
        }

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

}
