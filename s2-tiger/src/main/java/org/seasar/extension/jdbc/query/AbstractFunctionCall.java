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
package org.seasar.extension.jdbc.query;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.persistence.TemporalType;

import org.seasar.extension.jdbc.FunctionCall;
import org.seasar.extension.jdbc.JdbcContext;
import org.seasar.extension.jdbc.ParamType;
import org.seasar.extension.jdbc.ValueType;
import org.seasar.extension.jdbc.manager.JdbcManagerImplementor;
import org.seasar.framework.exception.SQLRuntimeException;
import org.seasar.framework.util.PreparedStatementUtil;

/**
 * ファンクションを呼び出す抽象クラスです。
 * 
 * @author koichik
 * @param <T>
 *            ファンクションの戻り値の型。戻り値が結果セットの場合は<code>List</code>の要素の型
 * @param <S>
 *            <code>FunctionCall</code>のサブタイプです。
 */
public abstract class AbstractFunctionCall<T, S extends FunctionCall<T, S>>
        extends AbstractModuleCall<S> implements FunctionCall<T, S> {

    /** 戻り値のクラス */
    protected Class<T> resultClass;

    /** 戻り値型がLOBなら<code>true</code> */
    protected boolean resultLob;

    /** 戻り値が<code>List</code>なら<code>true</code> */
    protected boolean resultList;

    /** 戻り値の時制の種別 */
    protected TemporalType resultTemporalType;

    /**
     * インスタンスを構築します。
     * 
     * @param jdbcManager
     *            内部的なJDBCマネージャ
     * @param resultClass
     *            戻り値のクラス
     */
    public AbstractFunctionCall(final JdbcManagerImplementor jdbcManager,
            final Class<T> resultClass) {
        super(jdbcManager, true);
        this.resultClass = resultClass;
    }

    @SuppressWarnings("unchecked")
    public S lob() {
        resultLob = true;
        return (S) this;
    }

    @SuppressWarnings("unchecked")
    public S temporal(TemporalType temporalType) {
        this.resultTemporalType = temporalType;
        return (S) this;
    }

    public T getSingleResult() {
        resultList = false;
        prepare("getSingleResult");
        logSql();
        try {
            return getSingleResultInternal();
        } finally {
            completed();
        }
    }

    public List<T> getResultList() {
        resultList = true;
        prepare("getResultList");
        logSql();
        try {
            return getResultListInternal();
        } finally {
            completed();
        }
    }

    /**
     * ストアドファンクションを呼び出し、その戻り値を返します。
     * 
     * @return ストアドファンクションの戻り値
     */
    protected T getSingleResultInternal() {
        final JdbcContext jdbcContext = jdbcManager.getJdbcContext();
        try {
            final CallableStatement cs = getCallableStatement(jdbcContext);
            final boolean resultSetGettable = PreparedStatementUtil.execute(cs);
            handleNonParamResultSets(cs, resultSetGettable);
            final T result = handleSingleResult(cs);
            handleOutParams(cs);
            return result;
        } finally {
            if (!jdbcContext.isTransactional()) {
                jdbcContext.destroy();
            }
            completed();
        }
    }

    /**
     * 結果セットを返すストアドファンクションを呼び出し、その戻り値を返します。
     * 
     * @return ストアドファンクションの戻り値
     */
    protected List<T> getResultListInternal() {
        final JdbcContext jdbcContext = jdbcManager.getJdbcContext();
        try {
            final CallableStatement cs = getCallableStatement(jdbcContext);
            final boolean resultSetGettable = PreparedStatementUtil.execute(cs);
            handleNonParamResultSets(cs, resultSetGettable);
            final List<T> result = handleResultList(cs);
            handleOutParams(cs);
            return result;
        } finally {
            if (!jdbcContext.isTransactional()) {
                jdbcContext.destroy();
            }
        }
    }

    /**
     * ストアドファンクションの戻り値を受け取る<code>OUT</code>パラメータを準備します。
     */
    protected void prepareReturnParameter() {
        final ValueType valueType = getValueType(resultList ? List.class
                : resultClass, resultLob, resultTemporalType);
        final Param p = addParam(null, resultClass, valueType);
        p.paramType = ParamType.OUT;
    }

    /**
     * ストアドファンクションの戻り値を処理して、結果を返します。
     * 
     * @param cs
     *            呼び出し可能なステートメント
     * @return ストアドファンクションの戻り値を処理した結果
     */
    @SuppressWarnings("unchecked")
    protected T handleSingleResult(final CallableStatement cs) {
        try {
            final Param param = getParam(0);
            return (T) param.valueType.getValue(cs, 1);
        } catch (final SQLException e) {
            throw new SQLRuntimeException(e);
        }
    }

    /**
     * ストアドファンクションの戻り値を処理して、結果のリストを返します。
     * 
     * @param cs
     *            呼び出し可能なステートメント
     * @return ストアドファンクションの戻り値を処理した結果のリスト
     */
    @SuppressWarnings("unchecked")
    protected List<T> handleResultList(final CallableStatement cs) {
        try {
            final Param param = getParam(0);
            final ResultSet rs = ResultSet.class.cast(param.valueType.getValue(
                    cs, 1));
            return (List<T>) handleResultList(resultClass, rs);
        } catch (final SQLException e) {
            throw new SQLRuntimeException(e);
        }
    }

}
