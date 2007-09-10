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
import java.util.List;

import javax.ejb.ApplicationException;

import org.aopalliance.intercept.MethodInvocation;
import org.seasar.extension.tx.DefaultTransactionCallback;
import org.seasar.extension.tx.TransactionControl;

/**
 * @author koichik
 * 
 */
public class EJB3TransactionCallback extends DefaultTransactionCallback {

    /**
     * @param methodInvocation
     * @param txRules
     */
    @SuppressWarnings("unchecked")
    public EJB3TransactionCallback(final MethodInvocation methodInvocation,
            final List txRules) {
        super(methodInvocation, txRules);
    }

    @Override
    protected void applyTxRule(final TransactionControl control,
            final Throwable t) throws Throwable {
        if (isRollingBack(t)) {
            control.setRollbackOnly();
            throw t;
        }
        super.applyTxRule(control, t);
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
