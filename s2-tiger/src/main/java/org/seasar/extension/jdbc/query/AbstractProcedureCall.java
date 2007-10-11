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

import java.lang.reflect.Field;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.seasar.extension.jdbc.JdbcContext;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.ParamType;
import org.seasar.extension.jdbc.ProcedureCall;
import org.seasar.extension.jdbc.ResultSetHandler;
import org.seasar.extension.jdbc.exception.FieldNotGenericsRuntimeException;
import org.seasar.extension.jdbc.handler.BeanListResultSetHandler;
import org.seasar.framework.exception.SQLRuntimeException;
import org.seasar.framework.util.FieldUtil;
import org.seasar.framework.util.PreparedStatementUtil;
import org.seasar.framework.util.StatementUtil;
import org.seasar.framework.util.tiger.ReflectionUtil;

/**
 * プロシージャを呼び出す抽象クラスです。
 * 
 * @author higa
 * @param <S>
 *            <code>ProcedureCall</code>のサブタイプです。
 */
public abstract class AbstractProcedureCall<S extends ProcedureCall<S>> extends
        AbstractQuery<S> implements ProcedureCall<S> {

    /**
     * 最大行数です。
     */
    protected int maxRows;

    /**
     * フェッチ数です。
     */
    protected int fetchSize;

    /**
     * パラメータです。
     */
    protected Object parameter;

    /**
     * MS SQLServerのような結果セットを<code>OUT</code>パラメータにマッピングしない場合のパラメータのリストです。
     */
    protected List<Param> nonParamList = new ArrayList<Param>();

    /**
     * {@link AbstractProcedureCall}を作成します。
     * 
     * @param jdbcManager
     *            JDBCマネージャ
     */
    public AbstractProcedureCall(JdbcManager jdbcManager) {
        super(jdbcManager);
    }

    @SuppressWarnings("unchecked")
    public S maxRows(int maxRows) {
        this.maxRows = maxRows;
        return (S) this;
    }

    @SuppressWarnings("unchecked")
    public S fetchSize(int fetchSize) {
        this.fetchSize = fetchSize;
        return (S) this;
    }

    public void call() {
        prepare("call");
        logSql();
        JdbcContext jdbcContext = jdbcManager.getJdbcContext();
        try {
            CallableStatement cs = getCallableStatement(jdbcContext);
            if (PreparedStatementUtil.execute(cs)) {
                handleNonParamResultSets(cs);
            }
            handleOutParams(cs);
        } finally {
            if (!jdbcContext.isTransactional()) {
                jdbcContext.destroy();
            }
        }

    }

    /**
     * 呼び出し可能なステートメントを返します。
     * 
     * @param jdbcContext
     *            JDBCコンテキスト
     * @return 呼び出し可能なステートメント
     */
    protected CallableStatement getCallableStatement(JdbcContext jdbcContext) {
        CallableStatement ps = jdbcContext.getCallableStatement(executedSql);
        setupCallableStatement(ps);
        return ps;
    }

    /**
     * 呼び出し可能なステートメントをセットアップします。
     * 
     * @param cs
     *            呼び出し可能なステートメント
     */
    protected void setupCallableStatement(CallableStatement cs) {
        if (maxRows > 0) {
            StatementUtil.setMaxRows(cs, maxRows);
        }
        if (fetchSize > 0) {
            StatementUtil.setFetchSize(cs, fetchSize);
        }
        if (queryTimeout > 0) {
            StatementUtil.setQueryTimeout(cs, queryTimeout);
        }
        prepareInParams(cs);
        prepareOutParams(cs);
    }

    /**
     * <code>OUT</code>パラメータを準備します。
     * 
     * @param cs
     *            呼び出し可能なステートメント
     */
    protected void prepareOutParams(CallableStatement cs) {
        int size = getParamSize();
        try {
            for (int i = 0; i < size; i++) {
                Param param = getParam(i);
                if (param.paramType == ParamType.IN) {
                    continue;
                }
                param.valueType.registerOutParameter(cs, i + 1);
            }
        } catch (SQLException e) {
            throw new SQLRuntimeException(e);
        }
    }

    /**
     * <code>OUT</code>パラメータにマッピングされない1つ以上の結果セットを処理します。
     * 
     * @param cs
     *            呼び出し可能なステートメント
     */
    protected void handleNonParamResultSets(CallableStatement cs) {
        try {
            for (int i = 0; i < nonParamList.size(); i++) {
                Param param = nonParamList.get(i);
                Object value = handleResultSet(param.field, cs.getResultSet());
                FieldUtil.set(param.field, parameter, value);
                if (!cs.getMoreResults()) {
                    break;
                }
            }
        } catch (SQLException e) {
            throw new SQLRuntimeException(e);
        }
    }

    /**
     * <code>OUT</code>パラメータを処理します。
     * 
     * @param cs
     *            呼び出し可能なステートメント
     */
    protected void handleOutParams(CallableStatement cs) {
        try {
            for (int i = 0; i < getParamSize(); i++) {
                Param param = getParam(i);
                if (param.paramType == ParamType.IN) {
                    continue;
                }
                Object value = param.valueType.getValue(cs, i + 1);
                if (value instanceof ResultSet) {
                    value = handleResultSet(param.field, (ResultSet) value);
                }
                FieldUtil.set(param.field, parameter, value);
            }
        } catch (SQLException e) {
            throw new SQLRuntimeException(e);
        }
    }

    /**
     * 結果セットを処理します。
     * 
     * @param field
     *            フィールド
     * @param rs
     *            結果セット
     * @return 処理した結果
     */
    protected Object handleResultSet(Field field, ResultSet rs) {
        Class<?> baseClass = ReflectionUtil
                .getElementTypeOfListFromFieldType(field);
        if (baseClass == null) {
            logger.log("ESSR0709", new Object[] { callerClass.getName(),
                    callerMethodName });
            throw new FieldNotGenericsRuntimeException(field);
        }
        ResultSetHandler handler = new BeanListResultSetHandler(baseClass,
                jdbcManager.getDialect(), null);
        return handleResultSet(handler, rs);
    }

    /**
     * 直接パラメータでは指定しないパラメータの数を返します。
     * 
     * @return 直接パラメータでは指定しないパラメータの数
     */
    protected int getNonParamSize() {
        return nonParamList.size();
    }

    /**
     * 直接パラメータでは指定しないパラメータを返します。
     * 
     * @param index
     *            インデックス
     * @return 直接パラメータでは指定しないパラメータ
     */
    protected Param getNonParam(int index) {
        return nonParamList.get(index);
    }

    /**
     * 直接パラメータでは指定しないパラメータを追加します。
     * 
     * @param field
     *            フィールド
     * @return 追加されたパラメータ
     */
    protected Param addNonParam(Field field) {
        Param param = new Param();
        param.field = field;
        param.paramType = ParamType.OUT;
        nonParamList.add(param);
        return param;
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
     * 最大行数を返します。
     * 
     * @return 最大行数
     */
    public int getMaxRows() {
        return maxRows;
    }

    /**
     * パラメータを返します。
     * 
     * @return パラメータ
     */
    public Object getParameter() {
        return parameter;
    }
}