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
import javax.transaction.Synchronization;
import javax.transaction.TransactionSynchronizationRegistry;

import org.seasar.extension.tx.adapter.WAS5TransactionManagerAdapter.TransactionContext;

/**
 * WebSphere version 5.0.2以降が提供するTransaction APIを使用してトランザクションを制御する、
 * {@link TransactionSynchronizationRegistry}の実装です。
 * 
 * @author koichik
 * @version 2.4.18
 */
public class WAS5TransactionSynchronizationRegistry implements
        TransactionSynchronizationRegistry {

    /** WAS5 <code>TransactionControl</code>へのアダプタ */
    protected final WAS5TransactionManagerAdapter adapter;

    /**
     * インスタンスを構築します。
     * 
     * @param adapter
     *            WAS5 <code>TransactionControl</code>へのアダプタ
     */
    public WAS5TransactionSynchronizationRegistry(
            final WAS5TransactionManagerAdapter adapter) {
        this.adapter = adapter;
    }

    public Object getResource(final Object key) {
        final TransactionContext context = adapter.getCurrentContext();
        if (context == null) {
            throw new IllegalStateException();
        }
        return context.getResource(key);
    }

    public boolean getRollbackOnly() {
        final TransactionContext context = adapter.getCurrentContext();
        if (context == null) {
            throw new IllegalStateException();
        }
        return context.getRollbackOnly();
    }

    public Object getTransactionKey() {
        return adapter.getCurrentContext();
    }

    public int getTransactionStatus() {
        final TransactionContext context = adapter.getCurrentContext();
        if (context == null) {
            return Status.STATUS_UNKNOWN;
        }
        return context.getStatus();
    }

    public void putResource(final Object key, final Object value) {
        final TransactionContext context = adapter.getCurrentContext();
        if (context == null) {
            throw new IllegalStateException();
        }
        context.putResource(key, value);
    }

    public void registerInterposedSynchronization(final Synchronization sync) {
        final TransactionContext context = adapter.getCurrentContext();
        if (context == null) {
            throw new IllegalStateException();
        }
        context.registerSynchronization(sync);
    }

    public void setRollbackOnly() {
        final TransactionContext context = adapter.getCurrentContext();
        if (context == null) {
            throw new IllegalStateException();
        }
        context.setRollbackOnly();
    }

}
