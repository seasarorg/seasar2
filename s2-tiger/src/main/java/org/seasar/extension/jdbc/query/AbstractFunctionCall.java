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

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.seasar.extension.jdbc.FunctionCall;
import org.seasar.extension.jdbc.JdbcContext;
import org.seasar.extension.jdbc.JdbcManagerImplementor;
import org.seasar.extension.jdbc.ParamType;
import org.seasar.extension.jdbc.ValueType;
import org.seasar.framework.exception.SQLRuntimeException;
import org.seasar.framework.util.PreparedStatementUtil;

/**
 * ファンクションを呼び出す抽象クラスです。
 * 
 * @author koichik
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

    public List<T> getResultList() {
        resultList = true;
        prepare("getResultList");
        logSql();
        return getResultListInternal();
    }

    public T getSingleResult() {
        resultList = false;
        prepare("getSingleResult");
        logSql();
        return getSingleResultInternal();
    }

    protected List<T> getResultListInternal() {
        final JdbcContext jdbcContext = jdbcManager.getJdbcContext();
        try {
            final CallableStatement cs = getCallableStatement(jdbcContext);
            if (PreparedStatementUtil.execute(cs)) {
                handleNonParamResultSets(cs);
            }
            final List<T> result = handleResultList(cs);
            handleOutParams(cs);
            return result;
        } finally {
            if (!jdbcContext.isTransactional()) {
                jdbcContext.destroy();
            }
        }
    }

    protected T getSingleResultInternal() {
        final JdbcContext jdbcContext = jdbcManager.getJdbcContext();
        try {
            final CallableStatement cs = getCallableStatement(jdbcContext);
            if (PreparedStatementUtil.execute(cs)) {
                handleNonParamResultSets(cs);
            }
            final T result = handleSingleResult(cs);
            handleOutParams(cs);
            return result;
        } finally {
            if (!jdbcContext.isTransactional()) {
                jdbcContext.destroy();
            }
        }
    }

    protected void prepareReturnParameter() {
        final ValueType valueType = getValueType(resultList ? List.class
                : resultClass, resultLob);
        final Param p = addParam(null, resultClass, valueType);
        p.paramType = ParamType.OUT;
    }

    @SuppressWarnings("unchecked")
    protected List<T> handleResultList(final CallableStatement cs) {
        try {
            final Param param = getParam(0);
            final ResultSet rs = ResultSet.class.cast(param.valueType.getValue(
                    cs, 1));
            return (List<T>) handleResultSet(param.field, rs);
        } catch (final SQLException e) {
            throw new SQLRuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    protected T handleSingleResult(final CallableStatement cs) {
        try {
            final Param param = getParam(0);
            return (T) param.valueType.getValue(cs, 1);
        } catch (final SQLException e) {
            throw new SQLRuntimeException(e);
        }
    }

}
