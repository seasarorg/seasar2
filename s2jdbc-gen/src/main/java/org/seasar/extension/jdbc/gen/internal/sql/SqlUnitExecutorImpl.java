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
package org.seasar.extension.jdbc.gen.internal.sql;

import javax.sql.DataSource;
import javax.transaction.Status;
import javax.transaction.UserTransaction;

import org.seasar.extension.jdbc.gen.internal.exception.TransactionRuntimeException;
import org.seasar.extension.jdbc.gen.sql.SqlExecutionContext;
import org.seasar.extension.jdbc.gen.sql.SqlUnitExecutor;
import org.seasar.framework.log.Logger;

/**
 * {@link SqlUnitExecutor}の実装クラスです。
 * 
 * @author taedium
 */
public class SqlUnitExecutorImpl implements SqlUnitExecutor {

    /** ロガー */
    protected static Logger logger = Logger
            .getLogger(SqlUnitExecutorImpl.class);

    /** データソース */
    protected DataSource dataSource;

    /** ユーザトランザクション */
    protected UserTransaction userTransaction;

    /** エラー発生時に処理を即座に中断する場合{@code true}、中断しない場合{@code false} */
    protected boolean haltOnError;

    /**
     * インスタンスを構築します。
     * 
     * @param dataSource
     *            データソース
     * @param userTransaction
     *            ユーザートランザクション、トランザクションを使用しない場合{@code null}
     * @param haltOnError
     *            エラー発生時に処理を即座に中断する場合{@code true}、中断しない場合{@code false}
     */
    public SqlUnitExecutorImpl(DataSource dataSource,
            UserTransaction userTransaction, boolean haltOnError) {
        if (dataSource == null) {
            throw new NullPointerException("dataSource");
        }
        this.dataSource = dataSource;
        this.userTransaction = userTransaction;
        this.haltOnError = haltOnError;
    }

    public void execute(Callback callback) {
        boolean began = begin();
        boolean commit = true;
        try {
            executeInternal(callback);
        } catch (RuntimeException e) {
            commit = false;
            throw e;
        } finally {
            if (began) {
                end(commit);
            }
        }
    }

    /**
     * 内部的に実行します。
     * 
     * @param callback
     *            コールバック
     */
    protected void executeInternal(Callback callback) {
        SqlExecutionContext context = createSqlExecutionContext();
        try {
            callback.execute(context);
        } finally {
            context.destroy();
        }
    }

    /**
     * トランザクションを開始します。
     * 
     * @return トランザクションが開始された場合{@code true}
     */
    protected boolean begin() {
        if (userTransaction == null) {
            return false;
        }
        if (!hasTransaction()) {
            try {
                userTransaction.begin();
            } catch (Exception e) {
                throw new TransactionRuntimeException(e);
            }
            return true;
        }
        return false;
    }

    /**
     * 現在のスレッド上でトランザクションがアクティブな場合は<code>true</code>を、それ以外の場合は<code>false</code>
     * を返します。
     * 
     * @return 現在のスレッド上でトランザクションがアクティブな場合は<code>true</code>
     */
    protected boolean hasTransaction() {
        if (userTransaction == null) {
            return false;
        }
        int status = getTransactionStatus();
        return status != Status.STATUS_NO_TRANSACTION
                && status != Status.STATUS_UNKNOWN;
    }

    /**
     * トランザクションを終了します。
     * 
     * @param commit
     *            コミットする場合{@code true}
     */
    protected void end(boolean commit) {
        if (userTransaction == null) {
            return;
        }
        if (commit && getTransactionStatus() == Status.STATUS_ACTIVE) {
            try {
                userTransaction.commit();
            } catch (Exception e) {
                throw new TransactionRuntimeException(e);
            }
        } else {
            try {
                userTransaction.rollback();
            } catch (Exception e) {
                throw new TransactionRuntimeException(e);
            }
        }
    }

    /**
     * トランザクションのステータスを返します。
     * 
     * @return トランザクションのステータス
     */
    protected int getTransactionStatus() {
        try {
            return userTransaction.getStatus();
        } catch (Exception e) {
            throw new TransactionRuntimeException(e);
        }
    }

    /**
     * {@link SqlExecutionContext}の実装クラスを作成します。
     * 
     * @return {@link SqlExecutionContext}の実装クラス
     */
    protected SqlExecutionContext createSqlExecutionContext() {
        return new SqlExecutionContextImpl(dataSource, userTransaction == null,
                haltOnError);
    }

}
