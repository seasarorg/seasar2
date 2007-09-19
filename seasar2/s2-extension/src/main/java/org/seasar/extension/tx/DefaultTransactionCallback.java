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
package org.seasar.extension.tx;

import java.util.List;

import org.aopalliance.intercept.MethodInvocation;

/**
 * トランザクションコールバックのデフォルト実装クラスです。
 * <p>
 * このクラスは、宣言的トランザクションを実現するインターセプタによってインスタンス化されて
 * {@link TransactionManagerAdapter}の実装クラスに渡され、トランザクション境界の中から コールバックされます。
 * コールバックされたメソッドの中から、 インターセプタが適用されているメソッド (宣言的トランザクションの対象となるメソッド) を呼び出します。
 * </p>
 * 
 * @author koichik
 * @version 2.4.18
 */
public class DefaultTransactionCallback implements TransactionCallback {

    /** インターセプタが適用されているメソッドを起動するための{@link MethodInvocation} */
    protected final MethodInvocation methodInvocation;

    /** トランザクションルールのリスト */
    protected final List txRules;

    /**
     * インスタンスを構築します。
     * 
     * @param methodInvocation
     *            インターセプタが適用されているメソッドを起動するための{@link MethodInvocation}
     * @param txRules
     *            例外が発生した場合にトランザクションをコミットするかロールバックするかを表現する{@link TxRule}のリスト
     */
    public DefaultTransactionCallback(final MethodInvocation methodInvocation,
            final List txRules) {
        this.methodInvocation = methodInvocation;
        this.txRules = txRules;
    }

    public Object execute(final TransactionCordinator cordinator)
            throws Throwable {
        try {
            return methodInvocation.proceed();
        } catch (final Throwable t) {
            applyTxRule(cordinator, t);
            throw t;
        }
    }

    /**
     * トランザクション処理中に発生した例外に応じたトランザクションルールが登録されていれば適用します。
     * <p>
     * トランザクション処理中に発生した例外に応じたトランザクションルールが登録されていない場合は、 トランザクションをロールバックするために{@link TransactionCordinator#setRollbackOnly()}を呼び出します。
     * </p>
     * 
     * @param cordinator
     *            トランザクションコーディネータ
     * @param t
     *            トランザクション処理中に例外
     */
    protected void applyTxRule(final TransactionCordinator cordinator,
            final Throwable t) {
        for (int i = 0; i < txRules.size(); ++i) {
            final TxRule rule = (TxRule) txRules.get(i);
            if (rule.isAssignableFrom(t)) {
                rule.complete(cordinator);
                return;
            }
        }
        cordinator.setRollbackOnly();
    }

}
