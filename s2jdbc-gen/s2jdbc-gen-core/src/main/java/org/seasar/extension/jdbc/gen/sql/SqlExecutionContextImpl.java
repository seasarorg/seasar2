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
package org.seasar.extension.jdbc.gen.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.seasar.extension.jdbc.gen.SqlExecutionContext;
import org.seasar.extension.jdbc.gen.util.StatementUtil;
import org.seasar.extension.jdbc.util.ConnectionUtil;
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

    /** コネクション */
    protected Connection connection;

    /** エラー発生時に処理を即座に中断する場合{@code true}、中断しない場合{@code false} */
    protected boolean haltOnError;

    /** ステートメント */
    protected Statement statement;

    protected PreparedStatement preparedStatement;

    /**
     * @param connection
     *            コネクション
     * @param haltOnError
     *            エラー発生時に処理を即座に中断する場合{@code true}、中断しない場合{@code false}
     */
    public SqlExecutionContextImpl(Connection connection, boolean haltOnError) {
        if (connection == null) {
            throw new NullPointerException("connection");
        }
        this.connection = connection;
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
        if (preparedStatement != null) {
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
        if (haltOnError) {
            throw exception;
        }
        exceptionList.add(exception);
    }

    public void notifyException() {
        closeStatements();
    }

    public void destroy() {
        if (connection == null) {
            return;
        }
        closeStatements();
        try {
            connection.close();
        } catch (SQLException e) {
            logger.log(e);
        }
        connection = null;
        exceptionList.clear();
    }

    protected void closeStatements() {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                logger.log(e);
            }
            statement = null;
        }
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                logger.log(e);
            }
            preparedStatement = null;
        }
    }
}
