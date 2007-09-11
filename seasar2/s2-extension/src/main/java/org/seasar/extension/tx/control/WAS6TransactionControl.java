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
package org.seasar.extension.tx.control;

import org.seasar.extension.tx.TransactionCallback;
import org.seasar.extension.tx.TransactionControl;
import org.seasar.framework.exception.SIllegalStateException;

import com.ibm.websphere.uow.UOWSynchronizationRegistry;
import com.ibm.wsspi.uow.UOWAction;
import com.ibm.wsspi.uow.UOWManager;

/**
 * WebSphere version 6 (6.0.2.19以降または6.1.0.9以降) が提供するUOW APIを使用してトランザクションを制御する、
 * {@link TransacstionControl}の実装です。
 * 
 * @author koichik
 */
public class WAS6TransactionControl implements TransactionControl {

    /** グローバルトランザクションを示します */
    protected boolean GLOBAL_TX = true;

    /** ローカルトランザクションを示します */
    protected boolean LOCAL_TX = false;

    /** 既存のトランザクションがあれば参加することを示します */
    protected boolean JOIN_TX = true;

    /** 新規のトランザクションを開始することを示します */
    protected boolean NEW_TX = true;

    /** <coce>uowManager</code>プロパティのバインディング定義です。 */
    public static final String uowManager_BINDING = "bindingType=must";

    /** UOW SPI の提供するトランザクションマネージャ */
    protected UOWManager uowManager;

    public Object required(final TransactionCallback callback) throws Throwable {
        return execute(callback, GLOBAL_TX, JOIN_TX);
    }

    public Object requiresNew(final TransactionCallback callback)
            throws Throwable {
        return execute(callback, GLOBAL_TX, NEW_TX);
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
        return execute(callback, LOCAL_TX, NEW_TX);
    }

    public Object never(final TransactionCallback callback) throws Throwable {
        if (hasTransaction()) {
            throw new SIllegalStateException("ESSR0317", null);
        }
        return callback.execute(this);
    }

    public void setRollbackOnly() {
        uowManager.setRollbackOnly();
    }

    public boolean isCommitted() {
        return uowManager.getUOWStatus() == UOWSynchronizationRegistry.UOW_STATUS_COMMITTED;
    }

    /**
     * @param callback
     *            トランザクションコールバック
     * @param globalTransaction
     *            {@link #GLOBAL_TX}または{@link #LOCAL_TX}
     * @param joinTransaction
     *            {@link #JOIN_TX}または{@link #NEW_TX}
     * @return トランザクションコールバックの戻り値
     * @throws Throwable
     *             トランザクションコールバックが例外をスローした場合
     */
    protected Object execute(final TransactionCallback callback,
            final boolean globalTransaction, final boolean joinTransaction)
            throws Throwable {
        final int type = globalTransaction ? UOWSynchronizationRegistry.UOW_TYPE_GLOBAL_TRANSACTION
                : UOWSynchronizationRegistry.UOW_TYPE_LOCAL_TRANSACTION;
        final UOWActionImpl action = new UOWActionImpl(callback);
        uowManager.runUnderUOW(type, joinTransaction, action);
        return action.getResult();
    }

    /**
     * 現在のスレッド上でトランザクションが開始されている場合は<code>true</code>を、それ以外の場合は<code>false</code>を返します。
     * 
     * @return 現在のスレッド上でトランザクションが開始されている場合は<code>true</code>
     */
    protected boolean hasTransaction() {
        return uowManager.getUOWStatus() != UOWSynchronizationRegistry.UOW_STATUS_NONE;
    }

    /**
     * <code>UOWManager</code>が制御するトランザクション中に実行するアクションの実装です。
     * 
     * @author koichik
     */
    public class UOWActionImpl implements UOWAction {

        /** トランザクションコールバック */
        protected TransactionCallback callback;

        /** トランザクションコールバックの戻り値 */
        protected Object result;

        /**
         * インスタンスを構築します。
         * 
         * @param callback
         *            トランザクションコールバック
         */
        public UOWActionImpl(final TransactionCallback callback) {
            this.callback = callback;
        }

        /**
         * <code>UOWManager</code>から呼び出されます。
         */
        public void run() throws Exception {
            try {
                result = callback.execute(WAS6TransactionControl.this);
            } catch (final Exception e) {
                throw e;
            } catch (final Error e) {
                throw e;
            } catch (final Throwable e) {
                throw new Exception(e);
            }
        }

        /**
         * トランザクションコールバックの戻り値を返します。
         * 
         * @return トランザクションコールバックの戻り値
         */
        public Object getResult() {
            return result;
        }

    }
}
