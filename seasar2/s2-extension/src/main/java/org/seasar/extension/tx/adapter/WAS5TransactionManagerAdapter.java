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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.transaction.InvalidTransactionException;
import javax.transaction.NotSupportedException;
import javax.transaction.Status;
import javax.transaction.Synchronization;
import javax.transaction.SystemException;

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
public class WAS5TransactionManagerAdapter implements TransactionManagerAdapter {

    /** REQUIREDトランザクション属性のためのフラグを示します */
    protected static final boolean[] REQUIRED = new boolean[] { false, false };

    /** REQUIRES_NEWトランザクション属性のためのフラグを示します */
    protected static final boolean[] REQUIRES_NEW = new boolean[] { false, true };

    /** NOT_SUPPORTEDトランザクション属性のためのフラグを示します */
    protected static final boolean[] NOT_SUPPORTED = new boolean[] { true,
            false };

    private static final Logger logger = Logger
            .getLogger(WAS5TransactionManagerAdapter.class);

    /** 現在のスレッドに関連づけられている{@link TransactionContext} */
    protected final ThreadLocal currentContexts = new ThreadLocal();

    /** WAS5 Transaction APIの提供するトランザクションコントロール */
    protected final TransactionControl transactionControl;

    /**
     * インスタンスを構築します。
     * 
     * @param transactionControl
     *            WAS5 Transaction APIの提供するトランザクションコントロール
     */
    public WAS5TransactionManagerAdapter(
            final TransactionControl transactionControl) {
        this.transactionControl = transactionControl;
    }

    public Object required(final TransactionCallback callback) throws Throwable {
        if (hasTransaction()) {
            return callback.execute(this);
        }
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
        final TransactionContext context = getCurrentContext();
        if (context == null) {
            logger.log("ESSR0311", null);
            return;
        }
        context.setRollbackOnly();
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
        final TransactionContext action = new TransactionContext(callback);
        return action.run(txAttribute[0], txAttribute[1]);
    }

    /**
     * 現在のスレッド上でトランザクションがアクティブな場合は<code>true</code>を、 それ以外の場合は<code>false</code>を返します。
     * 
     * @return 現在のスレッド上でトランザクションがアクティブな場合は<code>true</code>
     */
    protected boolean hasTransaction() {
        return currentContexts.get() != null;
    }

    /**
     * 現在のスレッドに関連づけられている{@link TransactionContext}を返します。
     * 
     * @return 現在のスレッドに関連づけられている{@link TransactionContext}
     */
    protected TransactionContext getCurrentContext() {
        return (TransactionContext) currentContexts.get();
    }

    /**
     * 現在のスレッドに{@link TransactionContext}を関連づけます。
     * <p>
     * 現在のスレッドにすでに<code>TransactionalAction</code>が関連づけられている場合、 その<code>TransactionalAction</code>を切り離して返します。
     * </p>
     * 
     * @param context
     *            新たに現在のスレッド上に関連づけられる{@link TransactionContext}
     * @return 現在のスレッドから切り離された{@link TransactionContext}
     */
    protected TransactionContext enter(final TransactionContext context) {
        final TransactionContext current = getCurrentContext();
        currentContexts.set(context);
        return current;
    }

    /**
     * 現在のスレッドから{@link TransactionContext}を切り離し、以前に関連付けられていた<code>TransactionalAction</code>を関連づけます。
     * 
     * @param suspended
     *            以前に現在のスレッドに関連づけられた{@link TransactionContext}
     */
    protected void leave(final TransactionContext suspended) {
        currentContexts.set(suspended);
    }

    /**
     * <code>TransactionControl</code>が制御するトランザクション中のコンテキストを表すクラスです。
     * 
     * @author koichik
     */
    public class TransactionContext implements Status {

        /** トランザクションコールバック */
        protected TransactionCallback callback;

        /**
         * 現在のトランザクションの状態
         * 
         * @see Status
         */
        protected int status = STATUS_UNKNOWN;

        /** 現在のトランザクションに登録されている{@link Synchronization}のリスト */
        protected final List syncronizations = new ArrayList();

        /** トランザクションに関連づけられているリソースの{@link Mah} */
        protected final Map resources = new HashMap();

        /**
         * インスタンスを構築します。
         * 
         * @param callback
         *            トランザクションコールバック
         */
        public TransactionContext(final TransactionCallback callback) {
            this.callback = callback;
        }

        /**
         * トランザクション境界内で実行されるアクションです。
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
            final TransactionContext suspended = enter(forceLocalTx ? null
                    : this);
            try {
                final TxHandle txHandle = transactionControl.preinvoke(
                        forceLocalTx, forceGlobalTx);
                try {
                    status = STATUS_ACTIVE;
                    final Object result = callback
                            .execute(WAS5TransactionManagerAdapter.this);
                    beforeCompletion();
                    return result;
                } finally {
                    completion(txHandle);
                }
            } finally {
                leave(suspended);
            }
        }

        /**
         * トランザクションの状態を返します。
         * 
         * @return トランザクションの状態
         * @see Status
         */
        public int getStatus() {
            return status;
        }

        /**
         * トランザクションをロールバックするようマークされていれば<code>true</code>を返します。
         * 
         * @return トランザクションをロールバックするようマークされていれば<code>true</code>
         */
        public boolean getRollbackOnly() {
            return status == STATUS_MARKED_ROLLBACK;
        }

        /**
         * トランザクションをロールバックするようマークします。
         */
        public void setRollbackOnly() {
            status = STATUS_MARKED_ROLLBACK;
        }

        /**
         * {@link Synchronization}をトランザクションに関連づけます。
         * 
         * @param sync
         *            トランザクションに関連づけられる同期オブジェクト
         */
        public void registerSynchronization(final Synchronization sync) {
            syncronizations.add(sync);
        }

        /**
         * キーに関連づけられたリソースを返します。
         * 
         * @param key
         *            キー
         * @return キーに関連づけられたリソース
         * @throws NullPointerException
         *             キーが<code>null</code>の場合
         */
        public Object getResource(final Object key) {
            if (key == null) {
                throw new NullPointerException();
            }
            return resources.get(key);
        }

        /**
         * キーに値を関連づけます。
         * 
         * @param key
         *            キー
         * @param value
         *            値
         * @throws NullPointerException
         *             キーが<code>null</code>の場合
         */
        public void putResource(final Object key, final Object value) {
            if (key == null) {
                throw new NullPointerException();
            }
            resources.put(key, value);
        }

        /**
         * トランザクションがコミットされることを登録されている{@link Synchronization}に通知します。
         * 
         */
        protected void beforeCompletion() {
            if (status == STATUS_ACTIVE) {
                for (final Iterator it = syncronizations.iterator(); it
                        .hasNext();) {
                    final Synchronization sync = (Synchronization) it.next();
                    sync.beforeCompletion();
                }
            }
        }

        /**
         * @param txHandle
         * @throws InvalidTransactionException
         * @throws SystemException
         */
        protected void completion(final TxHandle txHandle)
                throws InvalidTransactionException, SystemException {
            try {
                if (status == STATUS_ACTIVE) {
                    status = STATUS_COMMITTING;
                    transactionControl.postinvoke(txHandle);
                    status = STATUS_COMMITTED;
                } else {
                    status = STATUS_ROLLING_BACK;
                    transactionControl.handleException(txHandle);
                }
            } finally {
                if (status != STATUS_COMMITTED) {
                    status = STATUS_ROLLEDBACK;
                }
                afterCompletion();
            }
        }

        /**
         * トランザクションが終了したことを登録されている{@link Synchronization}に通知します。
         * 
         */
        protected void afterCompletion() {
            for (final Iterator it = syncronizations.iterator(); it.hasNext();) {
                final Synchronization sync = (Synchronization) it.next();
                try {
                    sync.afterCompletion(status);
                } catch (final Throwable t) {
                    logger.log(t);
                }
            }
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
