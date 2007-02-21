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
package org.seasar.extension.tx;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.InvalidTransactionException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

import org.aopalliance.intercept.MethodInterceptor;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.exception.SIllegalArgumentException;

/**
 * 宣言的トランザクションのための抽象クラスです。
 * 
 * @author higa
 */
public abstract class AbstractTxInterceptor implements MethodInterceptor {

    /** <coce>userTransaction</code>プロパティのバインディング定義です。 */
    public static final String userTransaction_BINDING = "bindingType=must";

    /** <coce>transactionManager</code>プロパティのバインディング定義です。 */
    public static final String transactionManager_BINDING = "bindingType=may";

    private UserTransaction userTransaction;

    private TransactionManager transactionManager;

    private final List txRules = new ArrayList();

    /**
     * インスタンスを構築します。
     */
    public AbstractTxInterceptor() {
    }

    /**
     * {@link javax.transaction.UserTransaction ユーザトランザクション}を返します。
     * 
     * @return {@link javax.transaction.UserTransaction ユーザトランザクション}
     */
    public final UserTransaction getUserTransaction() {
        return userTransaction;
    }

    /**
     * {@link javax.transaction.UserTransaction ユーザトランザクション}を設定します。
     * 
     * @param userTransaction
     *            {@link javax.transaction.UserTransaction ユーザトランザクション}
     */
    public void setUserTransaction(final UserTransaction userTransaction) {
        this.userTransaction = userTransaction;
    }

    /**
     * {@link javax.transaction.TransactionManager トランザクションマネージャ}を返します。
     * 
     * @return {@link javax.transaction.TransactionManager トランザクションマネージャ}
     */
    public TransactionManager getTransactionManager() {
        return transactionManager;
    }

    /**
     * {@link javax.transaction.TransactionManager トランザクションマネージャ}を設定します。
     * 
     * @param transactionManager
     *            {@link javax.transaction.TransactionManager トランザクションマネージャ}
     */
    public void setTransactionManager(
            final TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    /**
     * 現在のスレッド上でトランザクションがアクティブな場合は<code>true</code>を、それ以外の場合は<code>false</code>を返します。
     * 
     * @return 現在のスレッド上でトランザクションがアクティブな場合は<code>true</code>
     * @throws SystemException
     *             トランザクションマネージャで例外が発生した場合にスローされます
     * @see javax.transaction.UserTransaction#getStatus()
     */
    public boolean hasTransaction() throws SystemException {
        return userTransaction.getStatus() != Status.STATUS_NO_TRANSACTION;
    }

    /**
     * トランザクションを開始します。
     * 
     * @throws NotSupportedException
     *             トランザクションマネージャで例外が発生した場合にスローされます
     * @throws SystemException
     *             トランザクションマネージャで例外が発生した場合にスローされます
     * @see javax.transaction.UserTransaction#begin()
     */
    public void begin() throws NotSupportedException, SystemException {
        userTransaction.begin();
    }

    /**
     * トランザクションをコミットまたはロールバックします。
     * <p>
     * 現在のスレッドに関連づけられているトランザクションがアクティブな場合は、 トランザクションをコミットします。
     * それ以外の場合はトランザクションをロールバックします。
     * </p>
     * 
     * @throws SecurityException
     *             トランザクションマネージャで例外が発生した場合にスローされます
     * @throws IllegalStateException
     *             トランザクションマネージャで例外が発生した場合にスローされます
     * @throws RollbackException
     *             トランザクションマネージャで例外が発生した場合にスローされます
     * @throws HeuristicMixedException
     *             トランザクションマネージャで例外が発生した場合にスローされます
     * @throws HeuristicRollbackException
     *             トランザクションマネージャで例外が発生した場合にスローされます
     * @throws SystemException
     *             トランザクションマネージャで例外が発生した場合にスローされます
     * @see javax.transaction.UserTransaction#commit()
     * @see javax.transaction.UserTransaction#rollback()
     */
    public void end() throws SecurityException, IllegalStateException,
            RollbackException, HeuristicMixedException,
            HeuristicRollbackException, SystemException {

        if (userTransaction.getStatus() == Status.STATUS_ACTIVE) {
            userTransaction.commit();
        } else {
            userTransaction.rollback();
        }
    }

    /**
     * トランザクションをロールバックします。
     * 
     * @throws IllegalStateException
     *             トランザクションマネージャで例外が発生した場合にスローされます
     * @throws SecurityException
     *             トランザクションマネージャで例外が発生した場合にスローされます
     * @throws SystemException
     *             トランザクションマネージャで例外が発生した場合にスローされます
     * @see javax.transaction.UserTransaction#rollback()
     */
    public void rollback() throws IllegalStateException, SecurityException,
            SystemException {

        if (hasTransaction()) {
            userTransaction.rollback();
        }
    }

    /**
     * トランザクションを中断します。
     * <p>
     * このメソッドが呼び出されるには、
     * {@link javax.transaction.TransactionManager トランザクションマネージャ}が
     * 設定されていなくてはなりません。
     * </p>
     * 
     * @return 中断された{@link javax.transaction.Transaction トランザクション}
     * @throws SystemException
     *             トランザクションマネージャで例外が発生した場合にスローされます
     * @see javax.transaction.TransactionManager#suspend()
     */
    public Transaction suspend() throws SystemException {
        if (transactionManager == null) {
            throw new EmptyRuntimeException("transactionManager");
        }
        return transactionManager.suspend();
    }

    /**
     * トランザクションを再開します。
     * <p>
     * このメソッドが呼び出されるには、
     * {@link javax.transaction.TransactionManager トランザクションマネージャ}が
     * 設定されていなくてはなりません。
     * </p>
     * 
     * @param transaction
     *            再開する{@link javax.transaction.Transaction トランザクション}
     * @throws InvalidTransactionException
     *             トランザクションマネージャで例外が発生した場合にスローされます
     * @throws IllegalStateException
     *             トランザクションマネージャで例外が発生した場合にスローされます
     * @throws SystemException
     *             トランザクションマネージャで例外が発生した場合にスローされます
     * @see javax.transaction.TransactionManager#resume(Transaction)
     */
    public void resume(final Transaction transaction)
            throws InvalidTransactionException, IllegalStateException,
            SystemException {
        if (transactionManager == null) {
            throw new EmptyRuntimeException("transactionManager");
        }
        transactionManager.resume(transaction);
    }

    /**
     * 例外が発生した場合にトランザクションをコミットまたはロールバックします。
     * <p>
     * 発生した例外がコミットまたはロールバックルールに登録されている場合はルールに従ってトランザクションをコミットまたはロールバックします。
     * ルールに登録されていない場合はトランザクションをロールバックします。
     * </p>
     * 
     * @param throwable
     *            発生した例外
     * @return トランザクションがコミットした場合は<code>true</code>、それ以外の場合は<code>false</code>
     * @throws SecurityException
     *             トランザクションマネージャで例外が発生した場合にスローされます
     * @throws IllegalStateException
     *             トランザクションマネージャで例外が発生した場合にスローされます
     * @throws RollbackException
     *             トランザクションマネージャで例外が発生した場合にスローされます
     * @throws HeuristicMixedException
     *             トランザクションマネージャで例外が発生した場合にスローされます
     * @throws HeuristicRollbackException
     *             トランザクションマネージャで例外が発生した場合にスローされます
     * @throws SystemException
     *             トランザクションマネージャで例外が発生した場合にスローされます
     */
    public boolean complete(final Throwable throwable)
            throws SecurityException, IllegalStateException, RollbackException,
            HeuristicMixedException, HeuristicRollbackException,
            SystemException {
        for (int i = 0; i < txRules.size(); ++i) {
            final TxRule rule = (TxRule) txRules.get(i);
            if (rule.isAssignableFrom(throwable)) {
                return rule.complete();
            }
        }
        rollback();
        return false;
    }

    /**
     * 例外が発生した場合にトランザクションをコミットするルールを追加します。
     * 
     * @param exceptionClass
     *            例外クラス
     */
    public void addCommitRule(final Class exceptionClass) {
        txRules.add(new TxRule(exceptionClass, true));
    }

    /**
     * 例外が発生した場合にトランザクションをロールバックするルールを追加します。
     * 
     * @param exceptionClass
     *            例外クラス
     */
    public void addRollbackRule(final Class exceptionClass) {
        txRules.add(new TxRule(exceptionClass, false));
    }

    /**
     * 例外が発生した場合にトランザクションをコミットするかロールバックするかのルールを表現します。
     * 
     * @author koichik
     */
    private class TxRule {
        private Class exceptionClass;

        private boolean commit;

        /**
         * インスタンスを構築します。
         * 
         * @param exceptionClass
         *            例外クラス
         * @param commit
         *            コミットする場合は<code>true</code>、それ以外の場合は<code>false</code>
         */
        public TxRule(final Class exceptionClass, final boolean commit) {
            if (!Throwable.class.isAssignableFrom(exceptionClass)) {
                throw new SIllegalArgumentException("ESSR0365",
                        new Object[] { exceptionClass.getName() });
            }
            this.exceptionClass = exceptionClass;
            this.commit = commit;
        }

        /**
         * 例外がこのルールに適合する場合は<code>true</code>、それ以外の場合は<code>false</code>を返します。
         * 
         * @param t
         *            例外
         * @return 例外がこのルールに適合する場合は<code>true</code>
         */
        public boolean isAssignableFrom(final Throwable t) {
            return exceptionClass.isAssignableFrom(t.getClass());
        }

        /**
         * ルールに従ってトランザクションをコミットまたはロールバックします。
         * 
         * @return トランザクションをコミットした場合は<code>true</code>、それ以外の場合は<code>false</code>
         * @throws RollbackException
         *             トランザクションマネージャで例外が発生した場合にスローされます
         * @throws HeuristicMixedException
         *             トランザクションマネージャで例外が発生した場合にスローされます
         * @throws HeuristicRollbackException
         *             トランザクションマネージャで例外が発生した場合にスローされます
         * @throws SystemException
         *             トランザクションマネージャで例外が発生した場合にスローされます
         */
        public boolean complete() throws RollbackException,
                HeuristicMixedException, HeuristicRollbackException,
                SystemException {
            if (commit) {
                end();
            } else {
                rollback();
            }
            return commit;
        }
    }

}
