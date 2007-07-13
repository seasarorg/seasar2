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

import javax.transaction.Status;
import javax.transaction.Synchronization;
import javax.transaction.TransactionManager;
import javax.transaction.TransactionSynchronizationRegistry;

import org.seasar.framework.exception.SIllegalStateException;
import org.seasar.framework.util.TransactionManagerUtil;

/**
 * {@link TransactionSynchronizationRegistry}の実装クラスです。
 * 
 * @author nakamura
 * 
 */
public class TransactionSynchronizationRegistryImpl implements
        TransactionSynchronizationRegistry {

    private TransactionManager tm;

    /**
     * トランザクションマネージャを設定します。
     * 
     * @param tm
     *            トランザクションマネージャ
     */
    public void setTransactionManager(TransactionManager tm) {
        this.tm = tm;
    }

    public void putResource(Object key, Object value) {
        if (key == null) {
            throw new NullPointerException("key");
        }
        assertActive();
        getTransaction().putResource(key, value);
    }

    public Object getResource(Object key) {
        if (key == null) {
            throw new NullPointerException("key");
        }
        assertActive();
        return getTransaction().getResource(key);
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

    public void registerInterposedSynchronization(Synchronization sync) {
        assertActive();
        getTransaction().registerInterposedSynchronization(sync);
    }

    /**
     * トランザクションを返します。
     * 
     * @return トランザクション
     */
    protected TransactionImpl getTransaction() {
        return (TransactionImpl) TransactionManagerUtil.getTransaction(tm);
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

}
