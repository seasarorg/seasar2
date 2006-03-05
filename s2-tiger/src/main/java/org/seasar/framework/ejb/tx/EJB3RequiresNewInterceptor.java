package org.seasar.framework.ejb.tx;

import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

import org.aopalliance.intercept.MethodInvocation;

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
                commit();
                return result;
            } catch (final Throwable t) {
                if (isRollingBack(t)) {
                    rollback();
                } else {
                    commit();
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
