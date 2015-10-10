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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Status;
import javax.transaction.Synchronization;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;
import javax.transaction.TransactionSynchronizationRegistry;

import org.seasar.framework.exception.SIllegalStateException;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.TransactionManagerUtil;
import org.seasar.framework.util.TransactionUtil;

/**
 * {@link TransactionSynchronizationRegistry}の実装クラスです。
 * <p>
 * J2EE1.4準拠のAPサーバが提供するJTA実装など、 S2JTA以外のJTA実装と組み合わせて使用することもできます。 その場合、
 * {@link TransactionSynchronizationRegistry#registerInterposedSynchronization(Synchronization)}が規定する{@link Synchronization}呼び出しの順序は満たされません。
 * </p>
 * 
 * @author nakamura
 */
public class TransactionSynchronizationRegistryImpl implements
        TransactionSynchronizationRegistry {

    private static final Logger logger = Logger
            .getLogger(TransactionSynchronizationRegistryImpl.class);

    private TransactionManager tm;

    private final Map transactionContexts = Collections
            .synchronizedMap(new HashMap());

    /**
     * インスタンスを構築します。
     */
    public TransactionSynchronizationRegistryImpl() {
    }

    /**
     * インスタンスを構築します。
     * 
     * @param tm
     *            トランザクションマネージャ
     */
    public TransactionSynchronizationRegistryImpl(final TransactionManager tm) {
        this.tm = tm;
    }

    /**
     * トランザクションマネージャを設定します。
     * 
     * @param tm
     *            トランザクションマネージャ
     */
    public void setTransactionManager(final TransactionManager tm) {
        this.tm = tm;
    }

    public void putResource(final Object key, final Object value) {
        if (key == null) {
            throw new NullPointerException("key");
        }
        assertActive();
        getContext().putResource(key, value);
    }

    public Object getResource(final Object key) {
        if (key == null) {
            throw new NullPointerException("key");
        }
        assertActive();
        return getContext().getResource(key);
    }

    public void setRollbackOnly() {
        assertActive();
        TransactionManagerUtil.setRollbackOnly(tm);
    }

    public boolean getRollbackOnly() {
        assertActive();
        switch (getTransactionStatus()) {
        case Status.STATUS_MARKED_ROLLBACK:
        case Status.STATUS_ROLLING_BACK:
            return true;
        }
        return false;
    }

    public Object getTransactionKey() {
        if (!isActive()) {
            return null;
        }
        return getTransaction();
    }

    public int getTransactionStatus() {
        return TransactionManagerUtil.getStatus(tm);
    }

    public void registerInterposedSynchronization(final Synchronization sync) {
        assertActive();
        getContext().registerInterposedSynchronization(sync);
    }

    /**
     * トランザクションを返します。
     * 
     * @return トランザクション
     */
    protected Transaction getTransaction() {
        return TransactionManagerUtil.getTransaction(tm);
    }

    /**
     * トランザクションがアクティブであることを表明します。
     * 
     * @throws IllegalStateException
     *             アクティブでない場合
     */
    protected void assertActive() throws IllegalStateException {
        if (!isActive()) {
            throw new SIllegalStateException("ESSR0311");
        }
    }

    /**
     * トランザクションがアクティブかどうかを返します。
     * 
     * @return トランザクションがアクティブかどうか
     */
    protected boolean isActive() {
        return TransactionManagerUtil.isActive(tm);
    }

    /**
     * 現在のトランザクションに関連づけられた{@link SynchronizationRegisterImpl コンテキスト情報}を返します。
     * 
     * @return 現在のトランザクションに関連づけられた{@link SynchronizationRegisterImpl コンテキスト情報}
     */
    protected SynchronizationRegister getContext() {
        final Transaction tx = getTransaction();
        if (tx instanceof SynchronizationRegister) {
            return (SynchronizationRegister) tx;
        }
        SynchronizationRegisterImpl context = (SynchronizationRegisterImpl) transactionContexts
                .get(tx);
        if (context == null) {
            context = new SynchronizationRegisterImpl(tx);
            TransactionUtil.registerSynchronization(tx, context);
            transactionContexts.put(tx, context);
        }
        return context;
    }

    /**
     * トランザクションに関連づけられたコンテキスト情報を表すクラスです。
     * 
     * @author koichik
     */
    public class SynchronizationRegisterImpl implements
            SynchronizationRegister, Synchronization {

        private final Transaction tx;

        private final List interposedSynchronizations = new ArrayList();

        private final Map resourceMap = new HashMap();

        /**
         * インスタンスを構築します。
         * 
         * @param tx
         *            トランザクション
         */
        public SynchronizationRegisterImpl(final Transaction tx) {
            this.tx = tx;
        }

        public void registerInterposedSynchronization(final Synchronization sync)
                throws IllegalStateException {
            interposedSynchronizations.add(sync);
        }

        public void putResource(final Object key, final Object value)
                throws IllegalStateException {
            resourceMap.put(key, value);
        }

        public Object getResource(final Object key)
                throws IllegalStateException {
            return resourceMap.get(key);
        }

        public void beforeCompletion() {
            for (int i = 0; i < interposedSynchronizations.size(); ++i) {
                final Synchronization sync = (Synchronization) interposedSynchronizations
                        .get(i);
                sync.beforeCompletion();
            }
        }

        public void afterCompletion(final int status) {
            for (int i = 0; i < interposedSynchronizations.size(); ++i) {
                final Synchronization sync = (Synchronization) interposedSynchronizations
                        .get(i);
                try {
                    sync.afterCompletion(status);
                } catch (final Throwable t) {
                    logger.log(t);
                }
            }
            transactionContexts.remove(tx);
        }

    }

}
