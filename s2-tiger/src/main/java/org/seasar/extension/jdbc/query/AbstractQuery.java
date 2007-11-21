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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.TemporalType;

import org.seasar.extension.jdbc.ParamType;
import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.Query;
import org.seasar.extension.jdbc.ResultSetHandler;
import org.seasar.extension.jdbc.SqlLog;
import org.seasar.extension.jdbc.SqlLogRegistry;
import org.seasar.extension.jdbc.SqlLogRegistryLocator;
import org.seasar.extension.jdbc.SqlLogger;
import org.seasar.extension.jdbc.ValueType;
import org.seasar.extension.jdbc.impl.SqlLogImpl;
import org.seasar.extension.jdbc.manager.JdbcManagerImplementor;
import org.seasar.extension.jdbc.parameter.TemporalParameter;
import org.seasar.extension.jdbc.types.ValueTypes;
import org.seasar.extension.jdbc.util.BindVariableUtil;
import org.seasar.framework.exception.SQLRuntimeException;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.ResultSetUtil;

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
public abstract class AbstractQuery<S extends Query<S>> implements Query<S>,
        SqlLogger {

    /**
     * 内部的なJDBCマネージャです。
     */
    protected JdbcManagerImplementor jdbcManager;

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
     * パラメータのリストです。
     */
    protected List<Param> paramList = new ArrayList<Param>();

    /**
     * {@link AbstractQuery}を作成します。
     * 
     * @param jdbcManager
     *            内部的なJDBCマネージャ
     */
    public AbstractQuery(JdbcManagerImplementor jdbcManager) {
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

    public void logSql(String sql, Object... vars) {
        String completeSql = null;
        if (logger.isDebugEnabled()) {
            vars = getParamValues();
            completeSql = BindVariableUtil.getCompleteSql(sql, vars);
            logger.debug(completeSql);
        }
        SqlLogRegistry sqlLogRegistry = SqlLogRegistryLocator.getInstance();
        if (sqlLogRegistry != null) {
            if (completeSql == null) {
                vars = getParamValues();
                completeSql = BindVariableUtil.getCompleteSql(sql, vars);
            }
            SqlLog sqlLog = new SqlLogImpl(sql, completeSql, vars,
                    getParamClasses());
            sqlLogRegistry.add(sqlLog);
        }
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
        logSql(executedSql, getParamValues());
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
     * 内部的なJDBCマネージャを返します。
     * 
     * @return 内部的なJDBCマネージャ
     */
    public JdbcManagerImplementor getJdbcManager() {
        return jdbcManager;
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
     * パラメータの値の配列を返します。
     * 
     * @return パラメータの値の配列
     */
    public Object[] getParamValues() {
        Object[] ret = new Object[paramList.size()];
        for (int i = 0; i < paramList.size(); i++) {
            ret[i] = paramList.get(i).value;
        }
        return ret;
    }

    /**
     * パラメータの値のクラスの配列を返します。
     * 
     * @return パラメータの値のクラスの配列
     */
    public Class<?>[] getParamClasses() {
        Class<?>[] ret = new Class<?>[paramList.size()];
        for (int i = 0; i < paramList.size(); i++) {
            ret[i] = paramList.get(i).paramClass;
        }
        return ret;
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
     * パラメータを返します。
     * 
     * @param index
     *            インデックス
     * @return パラメータ
     */
    protected Param getParam(int index) {
        return paramList.get(index);
    }

    /**
     * パラメータの数を返します。
     * 
     * @return パラメータの数
     */
    protected int getParamSize() {
        return paramList.size();
    }

    /**
     * パラメータを追加します。
     * 
     * @param value
     *            パラメータの値
     * @return パラメータ
     */
    protected Param addParam(Object value) {
        if (value == null) {
            throw new NullPointerException("value");
        }
        return addParam(value, value.getClass());
    }

    /**
     * パラメータを追加します。
     * 
     * @param value
     *            パラメータの値
     * @param propertyMeta
     *            プロパティのメタデータ
     * @return パラメータ
     */
    protected Param addParam(Object value, PropertyMeta propertyMeta) {
        if (propertyMeta == null) {
            throw new NullPointerException("propertyMeta");
        }
        Class<?> propertyClass = propertyMeta.getPropertyClass();
        ValueType valueType = getValueType(propertyClass, propertyMeta.isLob(),
                propertyMeta.getTemporalType());
        return addParam(value, propertyClass, valueType);
    }

    /**
     * パラメータを追加します。
     * 
     * @param value
     *            パラメータの値
     * @param paramClass
     *            パラメータのクラス
     * @return パラメータ
     */
    protected Param addParam(Object value, Class<?> paramClass) {
        if (paramClass == null) {
            throw new NullPointerException("paramClass");
        }
        Param param = new Param();
        if (value != null && value instanceof TemporalParameter) {
            TemporalParameter parameter = (TemporalParameter) value;
            param.value = parameter.getValue();
            param.paramClass = parameter.getTemporalClass();
            param.valueType = getValueType(param.paramClass, false, parameter
                    .getTemporalType());
        } else {
            param.value = value;
            param.paramClass = paramClass;
            param.valueType = getValueType(param.paramClass, false, null);
        }
        paramList.add(param);
        return param;
    }

    /**
     * パラメータを追加します。
     * 
     * @param value
     *            パラメータの値
     * @param paramClass
     *            パラメータのクラス
     * @param valueType
     *            値タイプ
     * @return パラメータ
     */
    protected Param addParam(Object value, Class<?> paramClass,
            ValueType valueType) {
        Param param = new Param(value, paramClass);
        param.valueType = valueType;
        paramList.add(param);
        return param;
    }

    /**
     * 値タイプを返します。
     * 
     * @param paramClass
     *            パラメータのクラス
     * @param lob
     *            <code>LOB</code>かどうか
     * @param temporalType
     *            時制の種別
     * @return 値タイプ
     */
    protected ValueType getValueType(Class<?> paramClass, boolean lob,
            TemporalType temporalType) {
        if (lob && paramClass == String.class) {
            return ValueTypes.CLOB;
        }
        if (temporalType != null
                && (Date.class == paramClass || Calendar.class
                        .isAssignableFrom(paramClass))) {
            boolean date = Date.class == paramClass;
            switch (temporalType) {
            case DATE:
                return date ? ValueTypes.DATE_SQLDATE
                        : ValueTypes.CALENDAR_SQLDATE;
            case TIME:
                return date ? ValueTypes.DATE_TIME : ValueTypes.CALENDAR_TIME;
            case TIMESTAMP:
                return date ? ValueTypes.DATE_TIMESTAMP
                        : ValueTypes.CALENDAR_TIMESTAMP;
            }
        }
        return jdbcManager.getDialect().getValueType(paramClass);
    }

    /**
     * <code>IN</code>パラメータの準備をします。
     * 
     * @param ps
     *            準備されたステートメント
     */
    protected void prepareInParams(PreparedStatement ps) {
        int size = paramList.size();
        try {
            for (int i = 0; i < size; i++) {
                Param param = paramList.get(i);
                if (param.paramType != ParamType.OUT) {
                    param.valueType.bindValue(ps, i + 1, param.value);
                }
            }
        } catch (SQLException e) {
            throw new SQLRuntimeException(e);
        }
    }

    /**
     * パラメータをリセットします。
     */
    protected void resetParams() {
        paramList.clear();
    }

    /**
     * 結果セットを処理します。
     * 
     * @param handler
     *            結果セットハンドラ
     * @param rs
     *            結果セット
     * @return 処理結果
     * @throws SQLRuntimeException
     *             SQL例外が発生した場合。
     */
    protected Object handleResultSet(ResultSetHandler handler, ResultSet rs)
            throws SQLRuntimeException {
        Object ret = null;
        try {
            ret = handler.handle(rs);
        } catch (SQLException e) {
            throw new SQLRuntimeException(e);
        } finally {
            ResultSetUtil.close(rs);
        }
        return ret;
    }
}