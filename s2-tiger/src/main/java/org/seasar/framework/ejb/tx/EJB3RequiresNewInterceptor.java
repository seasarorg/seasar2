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
import javax.transaction.TransactionManager;

import org.aopalliance.intercept.MethodInvocation;

/**
 * @author koichik
 * 
 */
public class EJB3RequiresNewInterceptor extends AbstractEJB3TxInterceptor {

    public EJB3RequiresNewInterceptor(
            final TransactionManager transactionManager) {
        super(transactionManager);
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

    protected Transaction suspendIfNecessary() throws SystemException {
        if (!hasTransaction()) {
            return null;
        }
        return suspend();
    }
}
