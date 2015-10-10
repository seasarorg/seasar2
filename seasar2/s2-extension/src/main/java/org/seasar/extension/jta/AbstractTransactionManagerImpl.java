/*
 * Copyright 2004-2015 the Seasar Foundation and the Others.
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

import org.seasar.framework.exception.SIllegalStateException;
import org.seasar.framework.exception.SNotSupportedException;
import org.seasar.framework.util.TransactionUtil;

/**
 * {@link javax.transaction.TransactionManager}を実装する抽象クラスです。
 * 
 * @author higa
 */
public abstract class AbstractTransactionManagerImpl implements
        TransactionManager {

    /** スレッドに関連づけられているトランザクション */
    protected final ThreadLocal threadAttachTx = new ThreadLocal();

    /**
     * <code>AbstractTransactionManagerImpl</code>のインスタンスを構築します。
     */
    public AbstractTransactionManagerImpl() {
    }

    public void begin() throws NotSupportedException, SystemException {
        ExtendedTransaction tx = getCurrent();
        if (tx != null) {
            throw new SNotSupportedException("ESSR0316", null);
        }
        tx = attachNewTransaction();
        tx.begin();
    }

    public void commit() throws RollbackException, HeuristicMixedException,
            HeuristicRollbackException, SecurityException,
            IllegalStateException, SystemException {

        final ExtendedTransaction tx = getCurrent();
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
        final ExtendedTransaction tx = getCurrent();
        if (tx == null) {
            throw new SIllegalStateException("ESSR0311", null);
        }
        try {
            tx.suspend();
        } finally {
            setCurrent(null);
        }
        return tx;
    }

    public void resume(final Transaction resumeTx)
            throws InvalidTransactionException, IllegalStateException,
            SystemException {

        final ExtendedTransaction tx = getCurrent();
        if (tx != null) {
            throw new SIllegalStateException("ESSR0317", null);
        }
        ((ExtendedTransaction) resumeTx).resume();
        setCurrent((ExtendedTransaction) resumeTx);
    }

    public void rollback() throws IllegalStateException, SecurityException,
            SystemException {

        final ExtendedTransaction tx = getCurrent();
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

        final ExtendedTransaction tx = getCurrent();
        if (tx == null) {
            throw new SIllegalStateException("ESSR0311", null);
        }
        tx.setRollbackOnly();
    }

    public void setTransactionTimeout(final int timeout) throws SystemException {
    }

    public int getStatus() {
        final ExtendedTransaction tx = getCurrent();
        if (tx != null) {
            return TransactionUtil.getStatus(tx);
        }
        return Status.STATUS_NO_TRANSACTION;
    }

    public Transaction getTransaction() {
        return getCurrent();
    }

    /**
     * 現在のスレッドに関連づけられているトランザクションを返します。
     * 
     * @return 現在のスレッドに関連づけられているトランザクション
     */
    protected ExtendedTransaction getCurrent() {
        final ExtendedTransaction tx = (ExtendedTransaction) threadAttachTx.get();
        if (tx != null
                && TransactionUtil.getStatus(tx) == Status.STATUS_NO_TRANSACTION) {
            setCurrent(null);
            return null;
        }
        return tx;
    }

    /**
     * トランザクションを現在のスレッドに関連づけます。
     * 
     * @param current
     *            現在のスレッドに関連づけるトランザクション
     */
    protected void setCurrent(final ExtendedTransaction current) {
        threadAttachTx.set(current);
    }

    /**
     * 新しいトランザクションを作成して現在のスレッドに関連づけます。
     * 
     * @return 現在のスレッドに関連づけられたトランザクション
     */
    protected ExtendedTransaction attachNewTransaction() {
        ExtendedTransaction tx = (ExtendedTransaction) threadAttachTx.get();
        if (tx == null) {
            tx = createTransaction();
            setCurrent(tx);
        }
        return tx;
    }

    /**
     * トランザクションを作成して返します。
     * 
     * @return トランザクション
     */
    protected abstract ExtendedTransaction createTransaction();

}