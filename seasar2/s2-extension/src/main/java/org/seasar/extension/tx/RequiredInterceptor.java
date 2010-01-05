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

import org.aopalliance.intercept.MethodInvocation;

/**
 * トランザクションを要求するメソッドのためのインターセプタです。
 * <p>
 * このインターセプタが適用されたメソッドが呼び出された際にトランザクションが開始されていない場合は、 トランザクションが開始されます。
 * すでにトランザクションが開始されていた場合は、 そのトランザクションを引き継ぎます。
 * </p>
 * <p>
 * トランザクションを開始した場合でも引き継いだ場合でも、このインターセプタが適用されたメソッドが例外をスローした場合は、
 * 例外の種類に応じてトランザクションがロールバックされるようにマークします。デフォルトでは、全ての例外に対してロールバックされるようにマークします。
 * この設定は {@link #addCommitRule(Class)} および {@link #addRollbackRule(Class)}
 * によって変更することができます。
 * </p>
 * <p>
 * トランザクションを開始した場合、 このインターセプタが適用されたメソッドが終了 (正常終了した場合および例外をスローした場合の両方)
 * すると、開始したトランザクションを完了 (コミットまたはロールバック) します。
 * トランザクションがロールバックするようにマークされていれば、トランザクションをロールバックします。 そうでなければ、トランザクションをコミットします。
 * </p>
 * 
 * @author higa
 */
public class RequiredInterceptor extends AbstractTxInterceptor {

    /**
     * インスタンスを構築します。
     * 
     */
    public RequiredInterceptor() {
    }

    public Object invoke(final MethodInvocation invocation) throws Throwable {
        return transactionManagerAdapter
                .required(new DefaultTransactionCallback(invocation, txRules));
    }

}
