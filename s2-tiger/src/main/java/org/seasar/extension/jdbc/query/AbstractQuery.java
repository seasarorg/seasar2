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

import java.util.ArrayList;
import java.util.List;

import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.SqlLog;
import org.seasar.extension.jdbc.SqlLogRegistry;
import org.seasar.extension.jdbc.SqlLogRegistryLocator;
import org.seasar.extension.jdbc.impl.SqlLogImpl;
import org.seasar.extension.jdbc.util.BindVariableUtil;
import org.seasar.framework.log.Logger;

/**
 * @author higa
 * 
 */
/**
 * クエリの抽象クラスです。
 * 
 * @author higa
 * 
 */
public abstract class AbstractQuery {

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
     * バインド変数のクラスの配列を返します。
     * 
     * @return バインド変数のクラスの配列
     */
    public Class<?>[] getBindVariableClasses() {
        return bindVariableClassList.toArray(new Class[bindVariableClassList
                .size()]);
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
     * 実行されるSQLを返します。
     * 
     * @return 実行されるSQL
     */
    public String getExecutedSql() {
        return executedSql;
    }

    /**
     * JDBCマネージャを返します。
     * 
     * @return JDBCマネージャ
     */
    public JdbcManager getJdbcManager() {
        return jdbcManager;
    }
}