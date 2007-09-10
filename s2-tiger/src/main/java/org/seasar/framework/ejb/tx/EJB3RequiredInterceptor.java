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

import org.aopalliance.intercept.MethodInvocation;
import org.seasar.extension.tx.RequiredInterceptor;

/**
 * トランザクションを要求するメソッドのためのインターセプタです。
 * <p>
 * このインターセプタが適用されたメソッドが呼び出された際にトランザクションが開始されていない場合は、トランザクションが開始されます。 メソッドが終了
 * (例外をスローした場合も) した後、開始したトランザクションは完了 (コミットまたはロールバック) されます。<br>
 * メソッドが呼び出された際に、既にトランザクションが開始されていた場合は何もしません。
 * </p>
 * 
 * @author koichik
 */
public class EJB3RequiredInterceptor extends RequiredInterceptor {

    /**
     * インスタンスを構築します。
     * 
     */
    public EJB3RequiredInterceptor() {
    }

    @Override
    public Object invoke(final MethodInvocation invocation) throws Throwable {
        return transactionControl.required(new EJB3TransactionCallback(
                invocation, txRules));
    }

}
