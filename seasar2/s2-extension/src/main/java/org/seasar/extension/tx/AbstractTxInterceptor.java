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
package org.seasar.extension.tx;

import java.util.ArrayList;
import java.util.List;

import org.aopalliance.intercept.MethodInterceptor;

/**
 * 宣言的トランザクションのための抽象クラスです。
 * 
 * @author higa
 * @author koichik
 */
public abstract class AbstractTxInterceptor implements MethodInterceptor {

    /** トランザクションマネージャへのアダプタ */
    protected TransactionManagerAdapter transactionManagerAdapter;

    /** {@link TxRule}の{@link List} */
    protected final List txRules = new ArrayList();

    /**
     * インスタンスを構築します。
     */
    public AbstractTxInterceptor() {
    }

    /**
     * トランザクションマネージャへのアダプタを設定します。
     * 
     * @param transactionManagerAdapter
     *            トランザクションマネージャへのアダプタ
     */
    public void setTransactionControl(
            final TransactionManagerAdapter transactionManagerAdapter) {
        this.transactionManagerAdapter = transactionManagerAdapter;
    }

    /**
     * 指定の例外が発生した場合に、トランザクションをロールバックするようにマークしないルールを追加します。
     * 
     * @param exceptionClass
     *            例外クラス
     */
    public void addCommitRule(final Class exceptionClass) {
        txRules.add(new TxRule(exceptionClass, true));
    }

    /**
     * 指定の例外が発生した場合に、トランザクションをロールバックするようにマークするルールを追加します。
     * 
     * @param exceptionClass
     *            例外クラス
     */
    public void addRollbackRule(final Class exceptionClass) {
        txRules.add(new TxRule(exceptionClass, false));
    }

}
