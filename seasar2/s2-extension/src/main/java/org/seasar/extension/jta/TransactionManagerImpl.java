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
package org.seasar.extension.jta;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.InvalidTransactionException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;
import javax.transaction.xa.XAException;

import org.seasar.framework.exception.SIllegalStateException;
import org.seasar.framework.exception.SNotSupportedException;
import org.seasar.framework.exception.SSystemException;

/**
 * {@link javax.transaction.TransactionManager}の実装クラスです。
 * 
 * @author higa
 */
public class TransactionManagerImpl implements TransactionManager {

    private final ThreadLocal threadAttachTx = new ThreadLocal();

    /**
     * <code>TransactionManagerImpl</code>のインスタンスを構築します。
     */
    public TransactionManagerImpl() {
    }

    public void begin() throws NotSupportedException, SystemException {
        TransactionImpl tx = getCurrent();
        if (tx != null) {
            throw new SNotSupportedException("ESSR0316", null);
        }
        tx = attachTransaction();
        tx.begin();
    }

    public void commit() throws RollbackException, HeuristicMixedException,
            HeuristicRollbackException, SecurityException,
            IllegalStateException, SystemException {

        final TransactionImpl tx = getCurrent();
        if (tx == null) {
            throw new SIllegalStateException("ESSR0311", null);
        }
        try {
            tx.commit();
        } finally {
            setCurrent(null);
        }
    }

    public Transaction suspend() throws SystemException {
        final TransactionImpl tx = getCurrent();
        if (tx == null) {
            throw new SIllegalStateException("ESSR0311", null);
        }
        try {
            tx.suspend();
        } catch (final XAException ex) {
            throw new SSystemException("ESSR0363", new Object[] { ex }, ex);
        } finally {
            setCurrent(null);
        }
        return tx;
    }

    public void resume(final Transaction resumeTx)
            throws InvalidTransactionException, IllegalStateException,
            SystemException {

        final TransactionImpl tx = getCurrent();
        if (tx != null) {
            throw new SIllegalStateException("ESSR0317", null);
        }
        try {
            ((TransactionImpl) resumeTx).resume();
        } catch (final XAException ex) {
            throw new SSystemException("ESSR0364", new Object[] { ex }, ex);
        }
        setCurrent((TransactionImpl) resumeTx);
    }

    public void rollback() throws IllegalStateException, SecurityException,
            SystemException {

        final TransactionImpl tx = getCurrent();
        if (tx == null) {
            throw new SIllegalStateException("ESSR0311", null);
        }
        try {
            tx.rollback();
        } finally {
            setCurrent(null);
        }
    }

    public void setRollbackOnly() throws IllegalStateException, SystemException {

        final Transaction tx = getTransaction();
        if (tx == null) {
            throw new SIllegalStateException("ESSR0311", null);
        }
        tx.setRollbackOnly();
    }

    public void setTransactionTimeout(final int timeout) throws SystemException {
    }

    public int getStatus() {
        final TransactionImpl tx = getCurrent();
        if (tx != null) {
            return tx.getStatus();
        }
        return Status.STATUS_NO_TRANSACTION;
    }

    public Transaction getTransaction() {
        return getCurrent();
    }

    private TransactionImpl getCurrent() {
        final TransactionImpl tx = (TransactionImpl) threadAttachTx.get();
        if (tx != null && tx.getStatus() == Status.STATUS_NO_TRANSACTION) {
            setCurrent(null);
            return null;
        }
        return tx;
    }

    private void setCurrent(final TransactionImpl current) {
        threadAttachTx.set(current);
    }

    private TransactionImpl attachTransaction() {
        TransactionImpl tx = (TransactionImpl) threadAttachTx.get();
        if (tx == null) {
            tx = new TransactionImpl();
            setCurrent(tx);
        }
        return tx;
    }
}