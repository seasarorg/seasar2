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
package org.seasar.extension.tx.adapter;

import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.seasar.extension.tx.TransactionCallback;
import org.seasar.extension.tx.TransactionManagerAdapter;
import org.seasar.framework.exception.SIllegalStateException;
import org.seasar.framework.log.Logger;

/**
 * JTAの{@link UserTransaction}を使用してトランザクションを制御する、
 * {@link TransactionManagerAdapter}の実装です。
 * 
 * @author koichik
 * @version 2.4.18
 */
public class JTAUserTransactionAdapter implements TransactionManagerAdapter {

    private static final Logger logger = Logger
            .getLogger(JTAUserTransactionAdapter.class);

    /** <coce>transactionManager</code>プロパティのバインディング定義です。 */
    public static final String userTransaction_BINDING = "bindingType=must";

    /** ユーザトランザクション */
    protected UserTransaction userTransaction;

    /**
     * ユーザトランザクションを設定します。
     * 
     * @param userTransaction
     *            ユーザトランザクション
     */
    public void setUserTransaction(final UserTransaction userTransaction) {
        this.userTransaction = userTransaction;
    }

    public Object required(final TransactionCallback callback) throws Throwable {
        final boolean began = begin();
        try {
            return callback.execute(this);
        } finally {
            if (began) {
                end();
            }
        }
    }

    public Object requiresNew(final TransactionCallback callback)
            throws Throwable {
        throw new UnsupportedOperationException("REQUIRES_NEW");
    }

    public Object mandatory(final TransactionCallback callback)
            throws Throwable {
        if (!hasTransaction()) {
            throw new SIllegalStateException("ESSR0311", null);
        }
        return callback.execute(this);
    }

    public Object notSupported(final TransactionCallback callback)
            throws Throwable {
        throw new UnsupportedOperationException("NOT_SUPPORTED");
    }

    public Object never(final TransactionCallback callback) throws Throwable {
        if (hasTransaction()) {
            throw new SIllegalStateException("ESSR0317", null);
        }
        return callback.execute(this);
    }

    public void setRollbackOnly() {
        try {
            userTransaction.setRollbackOnly();
        } catch (final Exception e) {
            logger.log("ESSR0017", new Object[] { e.getMessage() }, e);
        }
    }

    /**
     * 現在のスレッド上でトランザクションがアクティブな場合は<code>true</code>を、それ以外の場合は<code>false</code>を返します。
     * 
     * @return 現在のスレッド上でトランザクションがアクティブな場合は<code>true</code>
     * @throws SystemException
     *             ユーザトランザクションで例外が発生した場合にスローされます
     * @see javax.transaction.UserTransaction#getStatus()
     */
    protected boolean hasTransaction() throws SystemException {
        return userTransaction.getStatus() != Status.STATUS_NO_TRANSACTION;
    }

    /**
     * トランザクションを開始します。
     * <p>
     * トランザクションを開始した場合は<code>true</code>、それ以外の場合は<code>false</code>を返します。
     * </p>
     * 
     * @return トランザクションを開始した場合は<code>true</code>
     * @throws Exception
     *             ユーザトランザクションで例外が発生した場合にスローされます
     * @see javax.transaction.UserTransaction#begin()
     */
    protected boolean begin() throws Exception {
        if (hasTransaction()) {
            return false;
        }
        userTransaction.begin();
        return true;
    }

    /**
     * トランザクションをコミットまたはロールバックします。
     * <p>
     * 現在のスレッドに関連づけられているトランザクションがアクティブな場合は、 トランザクションをコミットします。
     * それ以外の場合はトランザクションをロールバックします。
     * </p>
     * 
     * @throws Exception
     *             ユーザトランザクションャで例外が発生した場合にスローされます
     * @see javax.transaction.UserTransaction#commit()
     * @see javax.transaction.UserTransaction#rollback()
     */
    protected void end() throws Exception {
        if (userTransaction.getStatus() == Status.STATUS_ACTIVE) {
            userTransaction.commit();
        } else {
            userTransaction.rollback();
        }
    }

}
