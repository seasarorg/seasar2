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

import java.rmi.RemoteException;

import javax.ejb.ApplicationException;

import org.seasar.extension.tx.AbstractTxInterceptor;

/**
 * EJB3準拠の宣言的トランザクションをサポートするインターセプタの抽象クラスです。
 * 
 * @author koichik
 */
public abstract class AbstractEJB3TxInterceptor extends AbstractTxInterceptor {

    /**
     * インスタンスを構築します。
     * 
     */
    public AbstractEJB3TxInterceptor() {
    }

    /**
     * EJB3仕様に従い、 発生した例外によってトランザクションをロールバックしなくてはならない場合は<code>true</code>を、
     * それ以外の場合は<code>false</code>を返します。
     * 
     * @param throwable
     *            発生した例外
     * @return 発生した例外によってトランザクションをロールバックしなくてはならない場合は<code>true</code>
     */
    protected static boolean isRollingBack(final Throwable throwable) {
        final Class<? extends Throwable> exceptionClass = throwable.getClass();
        final ApplicationException annotation = exceptionClass
                .getAnnotation(ApplicationException.class);
        if (annotation != null) {
            return annotation.rollback();
        }
        if (throwable instanceof RemoteException) {
            return true;
        }
        if (throwable instanceof RuntimeException) {
            return true;
        }
        if (throwable instanceof Exception) {
            return false;
        }
        return true;
    }

}
