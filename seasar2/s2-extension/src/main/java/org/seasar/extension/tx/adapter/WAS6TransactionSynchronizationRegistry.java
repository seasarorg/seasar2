/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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

import javax.transaction.Synchronization;
import javax.transaction.TransactionSynchronizationRegistry;

import com.ibm.wsspi.uow.UOWManager;

/**
 * WebSphere version 6 (6.0.2.19以降または6.1.0.9以降) が提供するUOW APIを使用する、
 * {@link TransactionSynchronizationRegistry}の実装です。
 * 
 * @author koichik
 * @since 2.4.18
 */
public class WAS6TransactionSynchronizationRegistry implements
        TransactionSynchronizationRegistry {

    /** UOW API の提供するトランザクションマネージャ */
    protected UOWManager uowManager;

    /**
     * インスタンスを構築します。
     * 
     * @param uowManager
     *            UOW API の提供するトランザクションマネージャ
     */
    public WAS6TransactionSynchronizationRegistry(final UOWManager uowManager) {
        this.uowManager = uowManager;
    }

    public Object getResource(final Object key) {
        return uowManager.getResource(key);
    }

    public boolean getRollbackOnly() {
        return uowManager.getRollbackOnly();
    }

    public Object getTransactionKey() {
        return new Long(uowManager.getLocalUOWId());
    }

    public int getTransactionStatus() {
        return uowManager.getUOWStatus();
    }

    public void putResource(final Object key, final Object value) {
        uowManager.putResource(key, value);
    }

    public void registerInterposedSynchronization(final Synchronization sync) {
        uowManager.registerInterposedSynchronization(sync);
    }

    public void setRollbackOnly() {
        uowManager.setRollbackOnly();
    }

}
