package org.seasar.extension.jta;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

public class UserTransactionImpl implements UserTransaction {
    protected TransactionManager tm;

    public UserTransactionImpl(final TransactionManager tm) {
        this.tm = tm;
    }

    public void begin() throws NotSupportedException, SystemException {
        tm.begin();
    }

    public void commit() throws HeuristicMixedException,
            HeuristicRollbackException, IllegalStateException,
            RollbackException, SecurityException, SystemException {
        tm.commit();
    }

    public int getStatus() throws SystemException {
        return tm.getStatus();
    }

    public void rollback() throws IllegalStateException, SecurityException,
            SystemException {
        tm.rollback();
    }

    public void setRollbackOnly() throws IllegalStateException, SystemException {
        tm.setRollbackOnly();
    }

    public void setTransactionTimeout(final int timeout) throws SystemException {
        tm.setTransactionTimeout(timeout);
    }
}
