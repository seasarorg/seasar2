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
 * @author koichik
 * 
 */
public class DefaultTransactionCallback implements TransactionCallback {

    /** {@link MethodInvocation} */
    protected final MethodInvocation methodInvocation;

    /** トランザクションルールのリスト */
    protected final List txRules;

    /**
     * @param methodInvocation
     * @param txRules
     */
    public DefaultTransactionCallback(final MethodInvocation methodInvocation,
            final List txRules) {
        this.methodInvocation = methodInvocation;
        this.txRules = txRules;
    }

    public Object execute(final TransactionControl control) throws Throwable {
        try {
            return methodInvocation.proceed();
        } catch (final Throwable t) {
            applyTxRule(control, t);
            control.setRollbackOnly();
            throw t;
        }
    }

    /**
     * 発生した例外に応じたトランザクションルールが登録されていれば適用します。
     * 
     * @param control
     *            トランザクションコントロール
     * @param t
     *            例外
     * @throws Throwable
     *             トランザクションルールを適用した場合にスローされます。
     */
    protected void applyTxRule(final TransactionControl control,
            final Throwable t) throws Throwable {
        for (int i = 0; i < txRules.size(); ++i) {
            final TxRule rule = (TxRule) txRules.get(i);
            if (rule.isAssignableFrom(t)) {
                rule.complete(control);
                throw t;
            }
        }
    }
}
