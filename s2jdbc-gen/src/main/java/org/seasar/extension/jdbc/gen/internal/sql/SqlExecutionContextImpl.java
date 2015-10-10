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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.sql.DataSource;

import org.seasar.extension.jdbc.gen.sql.SqlExecutionContext;
import org.seasar.extension.jdbc.util.ConnectionUtil;
import org.seasar.extension.jdbc.util.DataSourceUtil;
import org.seasar.framework.exception.SQLRuntimeException;
import org.seasar.framework.log.Logger;

/**
 * {@link SqlExecutionContext}の実装クラスです。
 * 
 * @author taedium
 */
public class SqlExecutionContextImpl implements SqlExecutionContext {

    /** ロガー */
    protected static final Logger logger = Logger
            .getLogger(SqlExecutionContextImpl.class);

    /** {@link RuntimeException}のリスト */
    protected List<RuntimeException> exceptionList = new ArrayList<RuntimeException>();

    /** データソース */
    protected DataSource dataSource;

    /** コネクション */
    protected Connection connection;

    /** エラー発生時に処理を即座に中断する場合{@code true}、中断しない場合{@code false} */
    protected boolean haltOnError;

    /** ステートメント */
    protected Statement statement;

    /** 準備されたステートメント */
    protected PreparedStatement preparedStatement;

    /** ローカルトランザクションの場合{@code true} */
    protected boolean localTx;

    /** SQLの実行に失敗した場合{@code true} */
    protected boolean failed;

    /** このコンテキストを開始した場合{@code true} */
    protected boolean begun;

    /**
     * インスタンスを構築します。
     * 
     * @param dataSource
     *            データソース
     * @param localTx
     *            ローカルトランザクションの場合 {@code true}
     * @param haltOnError
     *            エラー発生時に処理を即座に中断する場合{@code true}、中断しない場合{@code false}
     */
    public SqlExecutionContextImpl(DataSource dataSource, boolean localTx,
            boolean haltOnError) {
        if (dataSource == null) {
            throw new NullPointerException("dataSource");
        }
        this.dataSource = dataSource;
        this.localTx = localTx;
        this.haltOnError = haltOnError;
        openConnection();
    }

    public boolean isHaltOnError() {
        return haltOnError;
    }

    public void setHaltOnError(boolean haltOnError) {
        this.haltOnError = haltOnError;
    }

    public Statement getStatement() {
        assertBegun();
        assertConnectionNotNull();
        statement = ConnectionUtil.createStatement(connection);
        return statement;
    }

    public PreparedStatement getPreparedStatement(String sql) {
        assertBegun();
        assertConnectionNotNull();
        preparedStatement = ConnectionUtil.prepareStatement(connection, sql);
        return preparedStatement;
    }

    public List<RuntimeException> getExceptionList() {
        return Collections.unmodifiableList(exceptionList);
    }

    public void addException(RuntimeException exception) {
        assertBegun();
        assertConnectionNotNull();
        failed = true;
        if (haltOnError) {
            throw exception;
        }
        logger.log("DS2JDBCGen0020", new Object[] { exception });
        exceptionList.add(exception);
    }

    public void notifyException() {
        assertBegun();
        assertConnectionNotNull();
        failed = true;
    }

    public void destroy() {
        assertNotBegun();
        exceptionList.clear();
        closeConnection();
    }

    public void begin() {
        assertNotBegun();
        begun = true;
        assertConnectionNotNull();
        if (localTx) {
            try {
                connection.setAutoCommit(false);
            } catch (SQLException e) {
                throw new SQLRuntimeException(e);
            }
        }
    }

    public void end() {
        assertBegun();
        begun = false;
        assertConnectionNotNull();
        closeStatements();
        if (localTx) {
            if (failed) {
                try {
                    rollbackLocalTxInternal();
                } catch (SQLException e) {
                    closeConnection();
                    throw new SQLRuntimeException(e);
                }
            } else {
                try {
                    commitLocalTxInternal();
                } catch (SQLException e) {
                    closeConnection();
                    throw new SQLRuntimeException(e);
                }
            }
            try {
                if (!connection.isClosed()) {
                    connection.setAutoCommit(true);
                }
            } catch (SQLException e) {
                closeConnection();
                throw new SQLRuntimeException(e);
            }
        }
        if (failed) {
            closeConnection();
            openConnection();
        }
        failed = false;
    }

    public void commitLocalTx() {
        if (localTx) {
            try {
                commitLocalTxInternal();
            } catch (SQLException e) {
                throw new SQLRuntimeException(e);
            }
        }
    }

    /**
     * 内部的にローカルトランザクションをロールバックします。
     * 
     * @throws SQLException
     */
    protected void rollbackLocalTxInternal() throws SQLException {
        if (!connection.isClosed()) {
            connection.rollback();
        }
    }

    /**
     * 内部的にローカルトランザクションをコミットします。
     * 
     * @throws SQLException
     */
    protected void commitLocalTxInternal() throws SQLException {
        if (!connection.isClosed()) {
            connection.commit();
        }
    }

    /**
     * ステートメントをクローズします。
     */
    protected void closeStatements() {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException ignore) {
                logger.log(ignore);
            }
            statement = null;
        }
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException ignore) {
                logger.log(ignore);
            }
            preparedStatement = null;
        }
    }

    /**
     * コネクションをクローズします。
     */
    protected void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ignore) {
                logger.log(ignore);
            }
            connection = null;
        }
    }

    /**
     * コネクションをオープンします。
     */
    protected void openConnection() {
        connection = DataSourceUtil.getConnection(dataSource);
    }

    /**
     * コネクションがnullでないことをアサートします。
     */
    protected void assertConnectionNotNull() {
        if (connection == null) {
            throw new AssertionError("connection must be opened.");
        }
    }

    /**
     * このコンテキストが開始されていることをアサートします。
     */
    protected void assertBegun() {
        if (!begun) {
            throw new AssertionError("this context must has been begun.");
        }
    }

    /**
     * このコンテキストが開始されていないことをアサートします。
     */
    protected void assertNotBegun() {
        if (begun) {
            throw new AssertionError("this context must not has been begun.");
        }
    }

}
