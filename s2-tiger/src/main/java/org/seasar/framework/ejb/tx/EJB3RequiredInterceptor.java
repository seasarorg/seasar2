package org.seasar.framework.ejb.tx;

import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;

import org.aopalliance.intercept.MethodInvocation;

public class EJB3RequiredInterceptor extends AbstractEJB3TxInterceptor {

    public EJB3RequiredInterceptor(final TransactionManager transactionManager) {
        super(transactionManager);
    }

    public Object invoke(final MethodInvocation invocation) throws Throwable {
        final boolean began = beginIfNecessary();
        try {
            final Object result = invocation.proceed();
            if (began) {
                commit();
            }
            return result;
        } catch (final Throwable t) {
            if (began) {
                if (isRollingBack(t)) {
                    rollback();
                } else {
                    commit();
                }
            }
            throw t;
        }
    }

    protected boolean beginIfNecessary() throws SystemException,
            NotSupportedException {
        if (hasTransaction()) {
            return false;
        }
        begin();
        return true;
    }
}
