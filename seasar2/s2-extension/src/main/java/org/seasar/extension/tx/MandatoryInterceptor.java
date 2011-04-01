/*
 * Copyright 2004-2011 the Seasar Foundation and the Others.
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

import org.aopalliance.intercept.MethodInvocation;

/**
 * トランザクションが必須なメソッドのためのインターセプタです。
 * <p>
 * このインターセプタが適用されたメソッドが呼び出された際にトランザクションが開始されていない場合は、 例外
 * {@link java.lang.IllegalStateException}がスローされます。
 * </p>
 * <p>
 * このインターセプタが適用されたメソッドが例外をスローした場合は、例外の種類に応じてトランザクションがロールバックされるようにマークします。
 * デフォルトでは、全ての例外に対してロールバックされるようにマークします。この設定は {@link #addCommitRule(Class)} および
 * {@link #addRollbackRule(Class)} によって変更することができます。
 * </p>
 * 
 * @author higa
 */
public class MandatoryInterceptor extends AbstractTxInterceptor {

    /**
     * インスタンスを構築します。
     * 
     */
    public MandatoryInterceptor() {
    }

    public Object invoke(final MethodInvocation invocation) throws Throwable {
        return transactionManagerAdapter
                .mandatory(new DefaultTransactionCallback(invocation, txRules));
    }

}
