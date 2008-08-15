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
package org.seasar.extension.jdbc.gen.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;
import javax.transaction.UserTransaction;

import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.gen.Command;
import org.seasar.extension.jdbc.gen.GenDialect;
import org.seasar.extension.jdbc.gen.SqlExecutionContext;
import org.seasar.extension.jdbc.gen.SqlFileExecutor;
import org.seasar.extension.jdbc.gen.SqlUnitExecutor;
import org.seasar.extension.jdbc.gen.dialect.GenDialectManager;
import org.seasar.extension.jdbc.gen.exception.RequiredPropertyEmptyRuntimeException;
import org.seasar.extension.jdbc.gen.sql.SqlFileExecutorImpl;
import org.seasar.extension.jdbc.gen.sql.SqlUnitExecutorImpl;
import org.seasar.extension.jdbc.gen.util.SingletonS2ContainerFactorySupport;
import org.seasar.extension.jdbc.manager.JdbcManagerImplementor;
import org.seasar.framework.container.SingletonS2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.exception.SRuntimeException;
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

    /** 設定ファイルのパス */
    protected String configPath = "s2jdbc.dicon";

    /** 環境名 */
    protected String env = "ut";

    /** エラー発生時に処理を中止する場合{@code true} */
    protected boolean haltOnError = false;

    /** {@link JdbcManager}のコンポーネント名 */
    protected String jdbcManagerName = "jdbcManager";

    /** SQLファイルのエンコーディング */
    protected String sqlFileEncoding = "UTF-8";

    /** 実行するSQLファイルのリスト */
    protected List<File> sqlFileList = new ArrayList<File>();

    /** SQLステートメントの区切り文字 */
    protected char statementDelimiter = ';';

    /** すべてのSQLを単一のトランザクションで実行する場合{@code true}、そうでない場合{@code false} */
    protected boolean transactional = false;

    /** {@link SingletonS2ContainerFactory}のサポート */
    protected SingletonS2ContainerFactorySupport containerFactorySupport;

    /** データソース */
    protected DataSource dataSource;

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
     * 設定ファイルのパスを返します。
     * 
     * @return 設定ファイルのパス
     */
    public String getConfigPath() {
        return configPath;
    }

    /**
     * 設定ファイルのパスを設定します。
     * 
     * @param configPath
     *            設定ファイルのパス
     */
    public void setConfigPath(String configPath) {
        this.configPath = configPath;
    }

    /**
     * 環境名を返します。
     * 
     * @return 環境名
     */
    public String getEnv() {
        return env;
    }

    /**
     * 環境名を設定します。
     * 
     * @param env
     *            環境名
     */
    public void setEnv(String env) {
        this.env = env;
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
     * {@link JdbcManager}のコンポーネント名を返します。
     * 
     * @return {@link JdbcManager}のコンポーネント名
     */
    public String getJdbcManagerName() {
        return jdbcManagerName;
    }

    /**
     * {@link JdbcManager}のコンポーネント名を設定します。
     * 
     * @param jdbcManagerName
     *            {@link JdbcManager}のコンポーネント名
     */
    public void setJdbcManagerName(String jdbcManagerName) {
        this.jdbcManagerName = jdbcManagerName;
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
        containerFactorySupport = new SingletonS2ContainerFactorySupport(
                configPath, env);
        containerFactorySupport.init();

        JdbcManagerImplementor jdbcManager = SingletonS2Container
                .getComponent(jdbcManagerName);
        dataSource = jdbcManager.getDataSource();
        dialect = GenDialectManager.getGenDialect(jdbcManager.getDialect());
        if (transactional) {
            userTransaction = SingletonS2Container
                    .getComponent(UserTransaction.class);
        }
        sqlFileExecutor = createSqlFileExecutor();
        sqlUnitExecutor = createSqlUnitExecutor();

        logger.log("DS2JDBCGen0005", new Object[] { dialect.getClass()
                .getName() });
    }

    @Override
    protected void doExecute() {
        try {
            if (transactional) {
                userTransaction.begin();
            }
            executeSqlFileList();
            if (transactional) {
                userTransaction.commit();
            }
        } catch (Throwable t) {
            if (transactional) {
                try {
                    userTransaction.rollback();
                } catch (Throwable th) {
                    logger.log(th);
                }
            }
            throw new SRuntimeException("ES2JDBCGen0002", new Object[] { t }, t);
        }
    }

    @Override
    protected void doDestroy() {
        if (containerFactorySupport != null) {
            containerFactorySupport.destory();
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
        return new SqlFileExecutorImpl(dialect, sqlFileEncoding,
                statementDelimiter, blockDelimiter);
    }

    /**
     * {@link SqlUnitExecutor}の実装を返します。
     * 
     * @return {@link SqlUnitExecutor}の実装
     */
    protected SqlUnitExecutor createSqlUnitExecutor() {
        return new SqlUnitExecutorImpl(dataSource, haltOnError);
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }

}
