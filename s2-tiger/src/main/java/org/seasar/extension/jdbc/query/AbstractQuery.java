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
package org.seasar.extension.jdbc.query;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.seasar.extension.jdbc.DbmsDialect;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.Query;
import org.seasar.extension.jdbc.SqlLog;
import org.seasar.extension.jdbc.SqlLogRegistry;
import org.seasar.extension.jdbc.SqlLogRegistryLocator;
import org.seasar.extension.jdbc.ValueType;
import org.seasar.extension.jdbc.exception.NullBindVariableRuntimeException;
import org.seasar.extension.jdbc.impl.SqlLogImpl;
import org.seasar.extension.jdbc.util.BindVariableUtil;
import org.seasar.framework.exception.SQLRuntimeException;
import org.seasar.framework.log.Logger;

/**
 * @author higa
 * 
 */
/**
 * クエリの抽象クラスです。
 * 
 * @author higa
 * @param <S>
 *            <code>Query</code>のサブタイプです。
 */
public abstract class AbstractQuery<S extends Query<S>> implements Query<S> {

    /**
     * JDBCマネージャです。
     */
    protected JdbcManager jdbcManager;

    /**
     * クエリを呼び出すクラスです。
     */
    protected Class<?> callerClass;

    /**
     * クエリを呼び出すメソッド名です。
     */
    protected String callerMethodName;

    /**
     * クエリタイムアウトの秒数です。
     */
    protected int queryTimeout;

    /**
     * ログを出力するオブジェクトです。
     */
    protected Logger logger;

    /**
     * 実行されるSQL
     */
    protected String executedSql;

    /**
     * バインド変数のリストです。
     */
    protected List<Object> bindVariableList = new ArrayList<Object>();

    /**
     * バインド変数のクラスのリストです。
     */
    protected List<Class<?>> bindVariableClassList = new ArrayList<Class<?>>();

    /**
     * {@link AbstractQuery}を作成します。
     * 
     * @param jdbcManager
     *            JDBCマネージャ
     * @param baseClass
     *            ベースクラス
     */
    public AbstractQuery(JdbcManager jdbcManager) {
        this.jdbcManager = jdbcManager;
    }

    @SuppressWarnings("unchecked")
    public S callerClass(Class<?> callerClass) {
        this.callerClass = callerClass;
        return (S) this;
    }

    @SuppressWarnings("unchecked")
    public S callerMethodName(String callerMethodName) {
        this.callerMethodName = callerMethodName;
        return (S) this;
    }

    @SuppressWarnings("unchecked")
    public S queryTimeout(int queryTimeout) {
        this.queryTimeout = queryTimeout;
        return (S) this;
    }

    /**
     * クエリの準備をします。
     * 
     * @param methodName
     *            メソッド名
     */
    protected abstract void prepare(String methodName);

    /**
     * SQLをログに出力します。
     */
    protected void logSql() {
        String completeSql = null;
        Object[] vars = null;
        if (logger.isDebugEnabled()) {
            vars = getBindVariables();
            completeSql = BindVariableUtil.getCompleteSql(executedSql, vars);
            logger.debug(completeSql);
        }
        SqlLogRegistry sqlLogRegistry = SqlLogRegistryLocator.getInstance();
        if (sqlLogRegistry != null) {
            if (completeSql == null) {
                vars = getBindVariables();
                completeSql = BindVariableUtil
                        .getCompleteSql(executedSql, vars);
            }
            SqlLog sqlLog = new SqlLogImpl(executedSql, completeSql, vars,
                    getBindVariableClasses());
            sqlLogRegistry.add(sqlLog);
        }
    }

    /**
     * 検索を呼び出すクラスとメソッド名を準備します。
     * 
     * @param methodName
     *            メソッド名
     */
    protected void prepareCallerClassAndMethodName(String methodName) {
        if (callerClass == null) {
            callerClass = getClass();
        }
        logger = Logger.getLogger(callerClass);
        if (callerMethodName == null) {
            callerMethodName = methodName;
        }
    }

    /**
     * 実行されるSQLを返します。
     * 
     * @return 実行されるSQL
     */
    public String getExecutedSql() {
        return executedSql;
    }

    /**
     * バインド変数の配列を返します。
     * 
     * @return バインド変数の配列
     */
    public Object[] getBindVariables() {
        return bindVariableList.toArray(new Object[bindVariableList.size()]);
    }

    /**
     * バインド変数のクラスの配列を返します。
     * 
     * @return バインド変数のクラスの配列
     */
    public Class<?>[] getBindVariableClasses() {
        return bindVariableClassList.toArray(new Class[bindVariableClassList
                .size()]);
    }

    /**
     * クエリを呼び出すクラスを返します。
     * 
     * @return クエリを呼び出すクラス
     */
    public Class<?> getCallerClass() {
        return callerClass;
    }

    /**
     * クエリを呼び出すメソッド名を返します。
     * 
     * @return クエリを呼び出すメソッド名
     */
    public String getCallerMethodName() {
        return callerMethodName;
    }

    /**
     * クエリタイムアウトを返します。
     * 
     * @return クエリタイムアウト
     */
    public int getQueryTimeout() {
        return queryTimeout;
    }

    /**
     * JDBCマネージャを返します。
     * 
     * @return JDBCマネージャ
     */
    public JdbcManager getJdbcManager() {
        return jdbcManager;
    }

    /**
     * バインド変数のクラスのリストを準備します。
     * 
     * @throws NullBindVariableRuntimeException
     *             バインド変数の値が<code>null</code>の場合
     */
    protected void prepareBindVariableClassList()
            throws NullBindVariableRuntimeException {
        int size = bindVariableList.size();
        for (int i = 0; i < size; i++) {
            Object var = bindVariableList.get(i);
            if (var == null) {
                logger.log("ESSR0709", new Object[] { callerClass.getName(),
                        callerMethodName });
                throw new NullBindVariableRuntimeException();
            }
            bindVariableClassList.add(var.getClass());
        }
    }

    /**
     * バインド変数を準備します。
     * 
     * @param ps
     *            準備されたステートメント
     */
    protected void prepareBindVariables(PreparedStatement ps) {
        DbmsDialect dialect = jdbcManager.getDialect();
        int size = bindVariableList.size();
        for (int i = 0; i < size; i++) {
            ValueType valueType = dialect.getValueType(bindVariableClassList
                    .get(i));
            try {
                valueType.bindValue(ps, i + 1, bindVariableList.get(i));
            } catch (SQLException e) {
                throw new SQLRuntimeException(e);
            }
        }
    }
}