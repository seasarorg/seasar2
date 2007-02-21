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
package org.seasar.framework.ejb.tx;

import javax.transaction.SystemException;
import javax.transaction.Transaction;

import org.aopalliance.intercept.MethodInvocation;

/**
 * 新しいトランザクションを要求するメソッドのためのインターセプタです。
 * <p>
 * このインターセプタが適用されたメソッドが呼び出された際に、新しいトランザクションが開始されます。 メソッドが終了 (例外をスローした場合も)
 * した後、開始したトランザクションは完了 (コミットまたはロールバック) されます。<br>
 * メソッドが呼び出された際に、既にトランザクションが開始されていた場合、そのトランザクションは中断されます。
 * 中断されたトランザクションは、メソッドが終了した後に再開されます。
 * </p>
 * 
 * @author koichik
 */
public class EJB3RequiresNewInterceptor extends AbstractEJB3TxInterceptor {

    /**
     * インスタンスを構築します。
     * 
     */
    public EJB3RequiresNewInterceptor() {
    }

    public Object invoke(final MethodInvocation invocation) throws Throwable {
        final Transaction tx = suspendIfNecessary();
        try {
            begin();
            try {
                final Object result = invocation.proceed();
                end();
                return result;
            } catch (final Throwable t) {
                if (isRollingBack(t)) {
                    rollback();
                } else {
                    end();
                }
                throw t;
            }
        } finally {
            if (tx != null) {
                resume(tx);
            }
        }
    }

    /**
     * トランザクションが開始されていれば中断します。
     * 
     * @return 中断されたトランザクション
     * @throws SystemException
     *             トランザクションマネージャで例外が発生した場合にスローされます
     */
    protected Transaction suspendIfNecessary() throws SystemException {
        if (!hasTransaction()) {
            return null;
        }
        return suspend();
    }

}
