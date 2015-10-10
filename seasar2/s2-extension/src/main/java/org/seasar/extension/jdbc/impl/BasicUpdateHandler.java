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
package org.seasar.extension.jdbc.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.sql.DataSource;

import org.seasar.extension.jdbc.StatementFactory;
import org.seasar.extension.jdbc.UpdateHandler;
import org.seasar.extension.jdbc.util.ConnectionUtil;
import org.seasar.framework.exception.SQLRuntimeException;
import org.seasar.framework.util.PreparedStatementUtil;
import org.seasar.framework.util.StatementUtil;

/**
 * {@link UpdateHandler}の基本的な実装クラスです。
 * 
 * @author higa
 * 
 */
public class BasicUpdateHandler extends BasicHandler implements UpdateHandler {

    /**
     * {@link BasicUpdateHandler}を作成します。
     */
    public BasicUpdateHandler() {
    }

    /**
     * {@link BasicUpdateHandler}を作成します。
     * 
     * @param dataSource
     *            データソース
     * @param sql
     *            SQL
     */
    public BasicUpdateHandler(DataSource dataSource, String sql) {
        super(dataSource, sql);
    }

    /**
     * {@link BasicUpdateHandler}を作成します。
     * 
     * @param dataSource
     *            データソース
     * @param sql
     *            SQL
     * @param statementFactory
     *            ステートメントファクトリ
     */
    public BasicUpdateHandler(DataSource dataSource, String sql,
            StatementFactory statementFactory) {

        super(dataSource, sql, statementFactory);
    }

    public int execute(Object[] args) throws SQLRuntimeException {
        return execute(args, getArgTypes(args));
    }

    public int execute(Object[] args, Class[] argTypes)
            throws SQLRuntimeException {
        Connection connection = getConnection();
        try {
            return execute(connection, args, argTypes);
        } finally {
            ConnectionUtil.close(connection);
        }
    }

    /**
     * SQL文を実行します。
     * 
     * @param connection
     *            コネクション
     * @param args
     *            引数
     * @param argTypes
     *            引数の型
     * @return 更新した行数
     */
    public int execute(Connection connection, Object[] args, Class[] argTypes) {
        logSql(args, argTypes);
        PreparedStatement ps = prepareStatement(connection);
        try {
            bindArgs(ps, args, argTypes);
            return PreparedStatementUtil.executeUpdate(ps);
        } finally {
            StatementUtil.close(ps);
        }
    }
}