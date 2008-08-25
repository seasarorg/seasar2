/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
package org.seasar.extension.jdbc.gen.internal.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.seasar.extension.jdbc.gen.command.Command;
import org.seasar.extension.jdbc.gen.dialect.GenDialect;
import org.seasar.extension.jdbc.gen.dialect.GenDialectRegistry;
import org.seasar.extension.jdbc.gen.internal.exception.RequiredPropertyEmptyRuntimeException;
import org.seasar.extension.jdbc.gen.sql.SqlExecutionContext;
import org.seasar.extension.jdbc.gen.sql.SqlFileExecutor;
import org.seasar.extension.jdbc.gen.sql.SqlUnitExecutor;
import org.seasar.framework.container.SingletonS2Container;
import org.seasar.framework.log.Logger;

/**
 * SQLを実行する{@link Command}の実装です。
 * <p>
 * このコマンドは、指定されたSQLファイルに記述された複数のSQLステートメントまたはSQLブロックをデータベースに対し実行します。SQLファイルには、1
 * 行コメントやブロックコメントを記述することもできます。コメントはSQLとは認識されず実行されません。
 * </p>
 * 
 * @author taedium
 */
public class ExecuteSqlCommand extends AbstractCommand {

    /** ロガー */
    protected static Logger logger = Logger.getLogger(ExecuteSqlCommand.class);

    /** SQLブロックの区切り文字 */
    protected String blockDelimiter = null;

    /** エラー発生時に処理を中止する場合{@code true} */
    protected boolean haltOnError = false;

    /** SQLファイルのエンコーディング */
    protected String sqlFileEncoding = "UTF-8";

    /** 実行するSQLファイルのリスト */
    protected List<File> sqlFileList = new ArrayList<File>();

    /** SQLステートメントの区切り文字 */
    protected char statementDelimiter = ';';

    /** すべてのSQLを単一のトランザクションで実行する場合{@code true}、そうでない場合{@code false} */
    protected boolean transactional = false;

    /** 方言 */
    protected GenDialect dialect;

    /** ユーザトランザクション */
    protected UserTransaction userTransaction;

    /** SQLファイルの実行者 */
    protected SqlFileExecutor sqlFileExecutor;

    /** SQLのひとまとまりの処理の実行者 */
    protected SqlUnitExecutor sqlUnitExecutor;

    /**
     * インスタンスを構築します。
     */
    public ExecuteSqlCommand() {
    }

    /**
     * SQLブロックの区切り文字を返します。
     * 
     * @return SQLブロックの区切り文字
     */
    public String getBlockDelimiter() {
        return blockDelimiter;
    }

    /**
     * SQLブロックの区切り文字を設定します。
     * 
     * @param blockDelimiter
     *            SQLブロックの区切り文字
     */
    public void setBlockDelimiter(String blockDelimiter) {
        this.blockDelimiter = blockDelimiter;
    }

    /**
     * エラー発生時に処理を中止する場合{@code true}を返します。
     * 
     * @return エラー発生時に処理を中止する場合{@code true}
     */
    public boolean isHaltOnError() {
        return haltOnError;
    }

    /**
     * エラー発生時に処理を中止する場合{@code true}を設定します。
     * 
     * @param haltOnError
     *            エラー発生時に処理を中止する場合{@code true}
     */
    public void setHaltOnError(boolean haltOnError) {
        this.haltOnError = haltOnError;
    }

    /**
     * SQLファイルのエンコーディングを返します。
     * 
     * @return SQLファイルのエンコーディング
     */
    public String getSqlFileEncoding() {
        return sqlFileEncoding;
    }

    /**
     * SQLファイルのエンコーディングを設定します。
     * 
     * @param sqlFileEncoding
     *            SQLファイルのエンコーディング
     */
    public void setSqlFileEncoding(String sqlFileEncoding) {
        this.sqlFileEncoding = sqlFileEncoding;
    }

    /**
     * 実行するSQLファイルのリストを返します。
     * 
     * @return 実行するSQLファイルのリスト
     */
    public List<File> getSqlFileList() {
        return sqlFileList;
    }

    /**
     * 実行するSQLファイルのリストを設定します。
     * 
     * @param sqlFileList
     *            実行するSQLファイルのリスト
     */
    public void setSqlFileList(List<File> sqlFileList) {
        this.sqlFileList = sqlFileList;
    }

    /**
     * すべてのSQLを単一のトランザクションで実行する場合{@code true}、そうでない場合{@code false}を返します。
     * 
     * @return すべてのSQLを単一のトランザクションで実行する場合{@code true}、そうでない場合{@code false}
     */
    public boolean isTransactional() {
        return transactional;
    }

    /**
     * すべてのSQLを単一のトランザクションで実行する場合{@code true}、そうでない場合{@code false}を設定します。
     * 
     * @param transactional
     *            すべてのSQLを単一のトランザクションで実行する場合{@code true}、そうでない場合{@code false}
     */
    public void setTransactional(boolean transactional) {
        this.transactional = transactional;
    }

    /**
     * SQLステートメントの区切り文字を返します。
     * 
     * @return SQLステートメントの区切り文字
     */
    public char getStatementDelimiter() {
        return statementDelimiter;
    }

    /**
     * SQLステートメントの区切り文字を設定します。
     * 
     * @param statementDelimiter
     *            SQLステートメントの区切り文字
     */
    public void setStatementDelimiter(char statementDelimiter) {
        this.statementDelimiter = statementDelimiter;
    }

    @Override
    protected void doValidate() {
        if (sqlFileList.isEmpty()) {
            throw new RequiredPropertyEmptyRuntimeException("sqlFileList");
        }
    }

    @Override
    protected void doInit() {
        dialect = GenDialectRegistry.getGenDialect(jdbcManager.getDialect());
        if (transactional) {
            userTransaction = SingletonS2Container
                    .getComponent(UserTransaction.class);
        }
        sqlFileExecutor = createSqlFileExecutor();
        sqlUnitExecutor = createSqlUnitExecutor();

        logRdbmsAndGenDialect(dialect);
    }

    @Override
    protected void doExecute() throws Throwable {
        boolean began = begin();
        try {
            executeSqlFileList();
        } finally {
            if (began) {
                end();
            }
        }
    }

    @Override
    protected void doDestroy() {
    }

    /**
     * トランザクションを開始します。
     * 
     * @return トランザクションが開始された場合{@code true}
     * @throws Exception
     *             トランザクションの開始で何らかの例外が発生した場合
     */
    protected boolean begin() throws Exception {
        if (transactional && !hasTransaction()) {
            userTransaction.begin();
            return true;
        }
        return false;
    }

    /**
     * 現在のスレッド上でトランザクションがアクティブな場合は<code>true</code>を、それ以外の場合は<code>false</code>
     * を返します。
     * 
     * @return 現在のスレッド上でトランザクションがアクティブな場合は<code>true</code>
     * @throws SystemException
     *             ユーザトランザクションで例外が発生した場合
     */
    protected boolean hasTransaction() throws SystemException {
        final int status = userTransaction.getStatus();
        return status != Status.STATUS_NO_TRANSACTION
                && status != Status.STATUS_UNKNOWN;
    }

    /**
     * トランザクションを終了します。
     * 
     * @throws Exception
     *             トランザクションの終了で何らかの例外が発生した場合
     */
    protected void end() throws Exception {
        if (transactional) {
            if (userTransaction.getStatus() == Status.STATUS_ACTIVE) {
                userTransaction.commit();
            } else {
                userTransaction.rollback();
            }
        }
    }

    /**
     * SQLファイルのリストを実行します。
     */
    protected void executeSqlFileList() {
        sqlUnitExecutor.execute(new SqlUnitExecutor.Callback() {

            public void execute(SqlExecutionContext context) {
                for (File sqlFile : sqlFileList) {
                    sqlFileExecutor.execute(context, sqlFile);
                }
            }
        });
    }

    /**
     * {@link SqlFileExecutor}の実装を返します。
     * 
     * @return {@link SqlFileExecutor}の実装
     */
    protected SqlFileExecutor createSqlFileExecutor() {
        return factory.createSqlFileExecutor(this, dialect, sqlFileEncoding,
                statementDelimiter, blockDelimiter);
    }

    /**
     * {@link SqlUnitExecutor}の実装を返します。
     * 
     * @return {@link SqlUnitExecutor}の実装
     */
    protected SqlUnitExecutor createSqlUnitExecutor() {
        return factory.createSqlUnitExecutor(this, jdbcManager.getDataSource(),
                haltOnError);
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }

}
