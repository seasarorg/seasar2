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
package org.seasar.framework.ejb.tx;

import java.rmi.RemoteException;

import javax.ejb.ApplicationException;

import org.aopalliance.intercept.MethodInvocation;
import org.seasar.extension.tx.RequiresNewInterceptor;

/**
 * 新しいトランザクションを要求するメソッドのためのインターセプタです。
 * <p>
 * このインターセプタが適用されたメソッドが呼び出された際に、新しいトランザクションが開始されます。
 * 既にトランザクションが開始されていた場合、そのトランザクションは中断されます。 中断されたトランザクションは、メソッドが終了した後に再開されます。
 * </p>
 * <p>
 * このインターセプタが適用されたメソッドが例外をスローした場合は、例外の種類に応じてトランザクションがロールバックされるようにマークします。
 * </p>
 * <ul>
 * <li>例外が{@link ApplicationException}で注釈されていれば、その
 * {@link ApplicationException#rollback()}要素に従います。</li>
 * <li>例外が{@link RemoteException}およびそのサブクラスならロールバックされるようにマークします。</li>
 * <li>例外が{@link RuntimeException}およびそのサブクラスならロールバックされるようにマークします。</li>
 * <li>その他の例外の場合はロールバックされるようにマークしません。</li>
 * </ul>
 * </p> この設定は {@link #addCommitRule(Class)} および {@link #addRollbackRule(Class)}
 * によって変更することができます。 </p>
 * <p>
 * このインターセプタが適用されたメソッドがメソッドが終了 (正常終了した場合および例外をスローした場合の両方) すると、開始したトランザクションを完了
 * (コミットまたはロールバック) します。 トランザクションがロールバックするようにマークされていれば、トランザクションをロールバックします。
 * そうでなければ、トランザクションをコミットします。
 * </p>
 * 
 * @author koichik
 */
public class EJB3RequiresNewInterceptor extends RequiresNewInterceptor {

    /**
     * インスタンスを構築します。
     * 
     */
    public EJB3RequiresNewInterceptor() {
    }

    @Override
    public Object invoke(final MethodInvocation invocation) throws Throwable {
        return transactionManagerAdapter
                .requiresNew(new EJB3TransactionCallback(invocation, txRules));
    }

}
