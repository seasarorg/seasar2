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
import java.sql.SQLException;

import javax.sql.DataSource;

import org.seasar.extension.jdbc.SqlLog;
import org.seasar.extension.jdbc.SqlLogRegistry;
import org.seasar.extension.jdbc.SqlLogRegistryLocator;
import org.seasar.extension.jdbc.StatementFactory;
import org.seasar.extension.jdbc.ValueType;
import org.seasar.extension.jdbc.types.ValueTypes;
import org.seasar.extension.jdbc.util.BindVariableUtil;
import org.seasar.extension.jdbc.util.DataSourceUtil;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.exception.SQLRuntimeException;
import org.seasar.framework.log.Logger;

/**
 * SQL文を実行するための基本的なクラスです。
 * 
 * @author higa
 * 
 */
public class BasicHandler {

    private DataSource dataSource;

    private String sql;

    private StatementFactory statementFactory = BasicStatementFactory.INSTANCE;

    /**
     * ログで使われるクラスです。
     */
    protected Class loggerClass = BasicHandler.class;

    /**
     * {@link BasicHandler}を作成します。
     */
    public BasicHandler() {
    }

    /**
     * {@link BasicHandler}を作成します。
     * 
     * @param ds
     *            データソース
     * @param sql
     *            SQL
     */
    public BasicHandler(DataSource ds, String sql) {
        this(ds, sql, BasicStatementFactory.INSTANCE);
    }

    /**
     * {@link BasicHandler}を作成します。
     * 
     * @param ds
     *            データソース
     * @param sql
     *            SQL
     * @param statementFactory
     *            ステートメントファクトリ
     */
    public BasicHandler(DataSource ds, String sql,
            StatementFactory statementFactory) {

        setDataSource(ds);
        setSql(sql);
        setStatementFactory(statementFactory);
    }

    /**
     * データソースを返します。
     * 
     * @return データソース
     */
    public DataSource getDataSource() {
        return dataSource;
    }

    /**
     * データソースを設定します。
     * 
     * @param dataSource
     *            データソース
     */
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * SQLを返します。
     * 
     * @return SQL
     */
    public String getSql() {
        return sql;
    }

    /**
     * SQLを設定します。
     * 
     * @param sql
     *            SQL
     */
    public void setSql(String sql) {
        this.sql = sql;
    }

    /**
     * ステートメントファクトリを返します。
     * 
     * @return ステートメントファクトリ
     */
    public StatementFactory getStatementFactory() {
        return statementFactory;
    }

    /**
     * ステートメントファクトリを設定します。
     * 
     * @param statementFactory
     *            ステートメントファクトリ
     */
    public void setStatementFactory(StatementFactory statementFactory) {
        this.statementFactory = statementFactory;
    }

    /**
     * コネクションを返します。
     * 
     * @return コネクション
     */
    protected Connection getConnection() {
        if (dataSource == null) {
            throw new EmptyRuntimeException("dataSource");
        }
        return DataSourceUtil.getConnection(dataSource);
    }

    /**
     * 準備されたステートメントを返します。
     * 
     * @param connection
     *            コネクション
     * @return 準備されたステートメント
     */
    protected PreparedStatement prepareStatement(Connection connection) {
        if (sql == null) {
            throw new EmptyRuntimeException("sql");
        }
        return statementFactory.createPreparedStatement(connection, sql);
    }

    /**
     * 引数をバインドします。
     * 
     * @param ps
     *            準備されたステートメント
     * @param args
     *            引数
     * @param argTypes
     *            引数のタイプ
     */
    protected void bindArgs(PreparedStatement ps, Object[] args,
            Class[] argTypes) {

        if (args == null) {
            return;
        }
        for (int i = 0; i < args.length; ++i) {
            ValueType valueType = getValueType(argTypes[i]);
            try {
                valueType.bindValue(ps, i + 1, args[i]);
            } catch (SQLException ex) {
                throw new SQLRuntimeException(ex);
            }
        }
    }

    /**
     * 引数の型を返します。
     * 
     * @param args
     *            引数
     * @return 引数の型
     */
    protected Class[] getArgTypes(Object[] args) {
        if (args == null) {
            return null;
        }
        Class[] argTypes = new Class[args.length];
        for (int i = 0; i < args.length; ++i) {
            Object arg = args[i];
            if (arg != null) {
                argTypes[i] = arg.getClass();
            }
        }
        return argTypes;
    }

    /**
     * 完全なSQL文を返します。
     * 
     * @param args
     *            引数
     * @return 完全なSQL文
     */
    protected String getCompleteSql(Object[] args) {
        return BindVariableUtil.getCompleteSql(sql, args);
    }

    /**
     * バインド変数を文字列として返します。
     * 
     * @param bindVariable
     *            バインド変数
     * @return バインド変数の文字列表現
     */
    protected String getBindVariableText(Object bindVariable) {
        return BindVariableUtil.getBindVariableText(bindVariable);
    }

    /**
     * S2JDBC用の値の型を返します。
     * 
     * @param clazz
     *            クラス
     * @return S2JDBC用の値の型
     */
    protected ValueType getValueType(Class clazz) {
        return ValueTypes.getValueType(clazz);
    }

    /**
     * SQLをログ出力します。
     * 
     * @param args
     *            SQLにバインドされる値の配列
     * @param argTypes
     *            SQLにバインドされる値の型の配列
     */
    protected void logSql(Object[] args, Class[] argTypes) {
        Logger logger = Logger.getLogger(loggerClass);
        SqlLogRegistry sqlLogRegistry = SqlLogRegistryLocator.getInstance();
        if (logger.isDebugEnabled() || sqlLogRegistry != null) {
            String completeSql = getCompleteSql(args);
            if (logger.isDebugEnabled()) {
                logger.debug(completeSql);
            }
            if (sqlLogRegistry != null) {
                SqlLog sqlLog = new SqlLogImpl(getSql(), completeSql, args,
                        argTypes);
                sqlLogRegistry.add(sqlLog);
            }
        }
    }

    /**
     * ログ用のクラスを返します。
     * 
     * @return ログ用のクラス
     */
    public Class getLoggerClass() {
        return loggerClass;
    }

    /**
     * ログ用のクラスを設定します。
     * 
     * @param loggerClass
     *            ログ用のクラス
     */
    public void setLoggerClass(Class loggerClass) {
        this.loggerClass = loggerClass;
    }
}