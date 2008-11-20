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
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.StatementUtil;

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

    /**
     * @param dataSource
     *            データソース
     * @param haltOnError
     *            エラー発生時に処理を即座に中断する場合{@code true}、中断しない場合{@code false}
     */
    public SqlExecutionContextImpl(DataSource dataSource, boolean haltOnError) {
        if (dataSource == null) {
            throw new NullPointerException("dataSource");
        }
        this.dataSource = dataSource;
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
        if (statement != null) {
            return statement;
        }
        statement = ConnectionUtil.createStatement(connection);
        return statement;
    }

    public PreparedStatement getPreparedStatement(String sql) {
        if (connection != null && preparedStatement != null) {
            StatementUtil.close(preparedStatement);
        }
        preparedStatement = ConnectionUtil.prepareStatement(connection, sql);
        return preparedStatement;
    }

    public List<RuntimeException> getExceptionList() {
        return Collections.unmodifiableList(exceptionList);
    }

    public void addException(RuntimeException exception) {
        closeStatements();
        closeConnection();
        if (haltOnError) {
            throw exception;
        }
        logger.log("DS2JDBCGen0020", new Object[] { exception });
        exceptionList.add(exception);
        openConnection();
    }

    public void notifyException() {
        closeStatements();
        closeConnection();
        openConnection();
    }

    public void destroy() {
        if (connection == null) {
            return;
        }
        closeStatements();
        closeConnection();
        exceptionList.clear();
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
}
