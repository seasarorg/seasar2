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

import java.util.HashMap;
import java.util.Map;

import javax.transaction.Status;
import javax.transaction.Synchronization;
import javax.transaction.SystemException;
import javax.transaction.TransactionSynchronizationRegistry;
import javax.transaction.UserTransaction;

import org.seasar.extension.tx.adapter.WAS5TransactionManagerAdapter.ExtendedJTATransaction;
import org.seasar.extension.tx.adapter.WAS5TransactionManagerAdapter.NotSupportedException;
import org.seasar.extension.tx.adapter.WAS5TransactionManagerAdapter.SynchronizationCallback;

/**
 * WebSphere version 5.0.2以降が提供するTransaction APIを使用してトランザクションを制御する、
 * {@link TransactionSynchronizationRegistry}の実装です。
 * 
 * @author koichik
 * @version 2.4.18
 */
public class WAS5TransactionSynchronizationRegistry implements
        TransactionSynchronizationRegistry, SynchronizationCallback, Status {

    /** スレッド上のトランザクションに関連づけられたリソースを保持する{@link Map} */
    protected static final ThreadLocal resourcesMaps = new ThreadLocal() {

        protected Object initialValue() {
            return new HashMap();
        }

    };

    /** ユーザトランザクション */
    protected final UserTransaction userTransaction;

    /** WAS5 Transaction APIの提供する拡張JTAトランザクション */
    protected final ExtendedJTATransaction extendedTransaction;

    /**
     * インスタンスを構築します。
     * 
     * @param userTransaction
     *            ユーザトランザクション
     * @param extendedTransaction
     *            WAS5 Transaction APIの提供する拡張JTAトランザクション
     */
    public WAS5TransactionSynchronizationRegistry(
            final UserTransaction userTransaction,
            final ExtendedJTATransaction extendedTransaction) {
        this.userTransaction = userTransaction;
        this.extendedTransaction = extendedTransaction;
    }

    public Object getResource(final Object key) {
        if (key == null) {
            throw new NullPointerException("key");
        }
        return getResources().get(key);
    }

    public boolean getRollbackOnly() {
        try {
            final int status = userTransaction.getStatus();
            if (status == STATUS_NO_TRANSACTION) {
                throw new IllegalStateException();
            }
            return status == STATUS_MARKED_ROLLBACK;
        } catch (final SystemException e) {
            throw createIllegalStateException(e);
        }
    }

    public Object getTransactionKey() {
        return extendedTransaction.getGlobalId();
    }

    public int getTransactionStatus() {
        try {
            return userTransaction.getStatus();
        } catch (final SystemException e) {
            return STATUS_UNKNOWN;
        }
    }

    public void putResource(final Object key, final Object value) {
        if (key == null) {
            throw new NullPointerException("key");
        }
        getResources().put(key, value);
    }

    public void registerInterposedSynchronization(final Synchronization sync) {
        try {
            final int status = userTransaction.getStatus();
            if (status != STATUS_ACTIVE) {
                throw new IllegalStateException();
            }
            extendedTransaction
                    .registerSynchronizationCallbackForCurrentTran(new SynchronizationCallbackAdapter(
                            sync));
        } catch (final Exception e) {
            throw createIllegalStateException(e);
        }
    }

    public void setRollbackOnly() {
        try {
            userTransaction.setRollbackOnly();
        } catch (final SystemException e) {
            throw createIllegalStateException(e);
        }
    }

    public void beforeCompletion(final int localId, final byte[] globalId) {
    }

    public void afterCompletion(final int localId, final byte[] globalId,
            final boolean committed) {
        final Map resourcesMap = (Map) resourcesMaps.get();
        if (resourcesMap != null) {
            resourcesMap.remove(globalId);
        }
    }

    /**
     * 現在のトランザクションに関連づけられたリソースの{@link Map}を返します。
     * 
     * @return 現在のトランザクションに関連づけられたリソースの{@link Map}
     */
    protected Map getResources() {
        final byte[] globalId = extendedTransaction.getGlobalId();
        if (globalId == null) {
            throw new IllegalStateException();
        }
        final Map resourcesMap = (Map) resourcesMaps.get();
        if (resourcesMap.containsKey(globalId)) {
            return (Map) resourcesMap.get(globalId);
        }
        try {
            extendedTransaction
                    .registerSynchronizationCallbackForCurrentTran(this);
        } catch (final NotSupportedException e) {
            throw createIllegalStateException(e);
        }
        final Map resources = new HashMap();
        resourcesMap.put(globalId, resources);
        return resources;
    }

    /**
     * 原因となる例外を設定した{@link IllegalStateException}を作成して返します。
     * 
     * @param e
     *            原因となった例外
     * @return <code>e</code>を原因とする{@link IllegalStateException}
     */
    protected IllegalStateException createIllegalStateException(
            final Throwable e) {
        return (IllegalStateException) new IllegalStateException().initCause(e);
    }

    /**
     * JTAの{@link Synchronization}と、WAS5 Transaction APIの提供する<code>SynchronizationCallback</code>のアダプタです。
     * 
     * @author koichik
     * 
     */
    public static class SynchronizationCallbackAdapter implements
            SynchronizationCallback, Status {

        /** 本来の{@link Synchronization} */
        protected Synchronization sync;

        /**
         * @param sync
         *            本来の{@link Synchronization}
         */
        public SynchronizationCallbackAdapter(final Synchronization sync) {
            this.sync = sync;
        }

        public void beforeCompletion(final int localId, final byte[] globalId) {
            sync.beforeCompletion();
        }

        public void afterCompletion(final int localId, final byte[] globalId,
                final boolean committed) {
            sync.afterCompletion(committed ? STATUS_COMMITTED
                    : STATUS_ROLLEDBACK);
        }

    }

}
