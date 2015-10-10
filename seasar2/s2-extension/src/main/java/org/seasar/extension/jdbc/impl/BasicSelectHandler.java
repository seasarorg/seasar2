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
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.seasar.extension.jdbc.ResultSetFactory;
import org.seasar.extension.jdbc.ResultSetHandler;
import org.seasar.extension.jdbc.SelectHandler;
import org.seasar.extension.jdbc.StatementFactory;
import org.seasar.extension.jdbc.util.ConnectionUtil;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.exception.SQLRuntimeException;
import org.seasar.framework.util.ResultSetUtil;
import org.seasar.framework.util.StatementUtil;

/**
 * {@link SelectHandler}の基本的な実装クラスです。
 * 
 * @author higa
 * 
 */
public class BasicSelectHandler extends BasicHandler implements SelectHandler {

    private ResultSetFactory resultSetFactory = BasicResultSetFactory.INSTANCE;

    private ResultSetHandler resultSetHandler;

    private int fetchSize = 100;

    private int maxRows = -1;

    /**
     * {@link BasicSelectHandler}を作成します。
     */
    public BasicSelectHandler() {
    }

    /**
     * {@link BasicSelectHandler}を作成します。
     * 
     * @param dataSource
     *            データソース
     * @param sql
     *            SQL
     * @param resultSetHandler
     *            結果セットハンドラ
     */
    public BasicSelectHandler(DataSource dataSource, String sql,
            ResultSetHandler resultSetHandler) {

        this(dataSource, sql, resultSetHandler, BasicStatementFactory.INSTANCE,
                BasicResultSetFactory.INSTANCE);
    }

    /**
     * {@link BasicSelectHandler}を作成します。
     * 
     * @param dataSource
     *            データソース
     * @param sql
     *            SQL
     * @param resultSetHandler
     *            結果セットハンドラ
     * @param statementFactory
     *            ステートメントファクトリ
     * @param resultSetFactory
     *            結果セットファクトリ
     */
    public BasicSelectHandler(DataSource dataSource, String sql,
            ResultSetHandler resultSetHandler,
            StatementFactory statementFactory, ResultSetFactory resultSetFactory) {

        setDataSource(dataSource);
        setSql(sql);
        setResultSetHandler(resultSetHandler);
        setStatementFactory(statementFactory);
        setResultSetFactory(resultSetFactory);
    }

    /**
     * 結果セットファクトリを返します。
     * 
     * @return 結果セットファクトリ
     */
    public ResultSetFactory getResultSetFactory() {
        return resultSetFactory;
    }

    /**
     * 結果セットファクトリを設定します。
     * 
     * @param resultSetFactory
     *            結果セットファクトリ
     */
    public void setResultSetFactory(ResultSetFactory resultSetFactory) {
        this.resultSetFactory = resultSetFactory;
    }

    /**
     * 結果セットハンドラを返します。
     * 
     * @return 結果セットハンドラ
     */
    public ResultSetHandler getResultSetHandler() {
        return resultSetHandler;
    }

    /**
     * 結果セットハンドラを設定します。
     * 
     * @param resultSetHandler
     *            結果セットハンドラ
     */
    public void setResultSetHandler(ResultSetHandler resultSetHandler) {
        this.resultSetHandler = resultSetHandler;
    }

    /**
     * フェッチ数を返します。
     * 
     * @return フェッチ数
     */
    public int getFetchSize() {
        return fetchSize;
    }

    /**
     * フェッチ数を設定します。
     * 
     * @param fetchSize
     *            フェッチ数
     */
    public void setFetchSize(int fetchSize) {
        this.fetchSize = fetchSize;
    }

    /**
     * 最大行数を返します。
     * 
     * @return 最大行数
     */
    public int getMaxRows() {
        return maxRows;
    }

    /**
     * 最大行数を設定します。
     * 
     * @param maxRows
     *            最大行数
     */
    public void setMaxRows(int maxRows) {
        this.maxRows = maxRows;
    }

    public Object execute(Object[] args) throws SQLRuntimeException {
        return execute(args, getArgTypes(args));
    }

    public Object execute(Object[] args, Class[] argTypes)
            throws SQLRuntimeException {
        Connection con = getConnection();
        try {
            return execute(con, args, argTypes);
        } finally {
            ConnectionUtil.close(con);
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
     * @return 実行した結果
     * @throws SQLRuntimeException
     *             SQL例外が発生した場合
     */
    public Object execute(Connection connection, Object[] args, Class[] argTypes)
            throws SQLRuntimeException {
        logSql(args, argTypes);
        PreparedStatement ps = null;
        try {
            ps = prepareStatement(connection);
            bindArgs(ps, args, argTypes);
            return execute(ps);
        } catch (SQLException ex) {
            throw new SQLRuntimeException(ex);
        } finally {
            StatementUtil.close(ps);
        }
    }

    /**
     * 引数のセットアップを行ないます。
     * 
     * @param con
     *            コネクション
     * @param args
     *            引数
     * @return セットアップ後の引数
     */
    protected Object[] setup(Connection con, Object[] args) {
        return args;
    }

    protected PreparedStatement prepareStatement(Connection connection) {
        PreparedStatement ps = super.prepareStatement(connection);
        if (fetchSize > -1) {
            StatementUtil.setFetchSize(ps, fetchSize);
        }
        if (maxRows > -1) {
            StatementUtil.setMaxRows(ps, maxRows);
        }
        return ps;
    }

    /**
     * SQL文を実行します。
     * 
     * @param ps
     *            準備されたステートメント
     * @return 実行結果
     * @throws SQLException
     *             SQL例外が発生した場合
     */
    protected Object execute(PreparedStatement ps) throws SQLException {
        if (resultSetHandler == null) {
            throw new EmptyRuntimeException("resultSetHandler");
        }
        ResultSet resultSet = null;
        try {
            resultSet = createResultSet(ps);
            return resultSetHandler.handle(resultSet);
        } finally {
            ResultSetUtil.close(resultSet);
        }
    }

    /**
     * データベースメタデータによるセットアップを行ないます。
     * 
     * @param dbMetaData
     *            データベースメタデータ
     */
    protected void setupDatabaseMetaData(DatabaseMetaData dbMetaData) {
    }

    /**
     * 結果セットを作成します。
     * 
     * @param ps
     *            準備されたステートメント
     * @return 結果セット
     */
    protected ResultSet createResultSet(PreparedStatement ps) {
        return resultSetFactory.createResultSet(ps);
    }
}