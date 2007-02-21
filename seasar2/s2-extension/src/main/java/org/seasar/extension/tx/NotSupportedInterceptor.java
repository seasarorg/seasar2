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

import javax.transaction.Transaction;

import org.aopalliance.intercept.MethodInvocation;

/**
 * トランザクションをサポートしないメソッドのためのインターセプタです。
 * <p>
 * このインターセプタが適用されたメソッドが呼び出された際にトランザクションが開始されている場合は、トランザクションが中断されます。 メソッドが終了
 * (例外をスローした場合も) した後、中断したトランザクションは再開されます。
 * </p>
 * 
 * @author taichi S.
 */
public class NotSupportedInterceptor extends AbstractTxInterceptor {

    /** <coce>transactionManager</code>プロパティのバインディング定義です。 */
    public static final String transactionManager_BINDING = "bindingType=must";

    /**
     * インスタンスを構築します。
     * 
     */
    public NotSupportedInterceptor() {
    }

    public Object invoke(final MethodInvocation invocation) throws Throwable {
        final Transaction tx = hasTransaction() ? suspend() : null;
        try {
            return invocation.proceed();
        } finally {
            if (tx != null) {
                resume(tx);
            }
        }
    }

}
