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
import java.util.List;

import org.seasar.extension.jdbc.DbmsDialect;
import org.seasar.extension.jdbc.JdbcContext;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.ResultSetHandler;
import org.seasar.extension.jdbc.Select;
import org.seasar.extension.jdbc.ValueType;
import org.seasar.extension.jdbc.exception.SNonUniqueResultException;
import org.seasar.framework.exception.SQLRuntimeException;
import org.seasar.framework.util.PreparedStatementUtil;
import org.seasar.framework.util.ResultSetUtil;
import org.seasar.framework.util.StatementUtil;

/**
 * 検索の抽象クラスです。
 * 
 * @author higa
 * @param <T>
 *            戻り値のベースの型です。
 * 
 */
public abstract class AbstractSelect<T> extends AbstractQuery implements
        Select<T> {

    /**
     * ベースクラスです。
     */
    protected Class<T> baseClass;

    /**
     * 最大行数です。
     */
    protected int maxRows;

    /**
     * フェッチ数です。
     */
    protected int fetchSize;

    /**
     * オフセットです。
     */
    protected int offset;

    /**
     * リミットです。
     */
    protected int limit;

    /**
     * {@link AbstractSelect}を作成します。
     * 
     * @param jdbcManager
     *            JDBCマネージャ
     * @param baseClass
     *            ベースクラス
     */
    public AbstractSelect(JdbcManager jdbcManager, Class<T> baseClass) {
        super(jdbcManager);
        this.baseClass = baseClass;

    }

    /**
     * 必要ならページング用にSQLを変換します。
     * 
     * @param sql
     *            SQL
     * @return 変換後のSQL
     */
    protected String convertLimitSql(String sql) {
        DbmsDialect dialect = jdbcManager.getDialect();
        if (dialect.supportsLimit()
                && (limit > 0 || limit == 0 && offset > 0
                        && dialect.supportsOffsetWithoutLimit())) {
            return dialect.convertLimitSql(sql, offset, limit);
        }
        return sql;
    }

    public List<T> getResultList() {
        prepare("getResultList");
        logSql();
        return getResultListInternal();
    }

    public T getSingleResult() throws SNonUniqueResultException {
        prepare("getSingleResult");
        logSql();
        return getSingleResultInternal();
    }

    /**
     * 検索してベースオブジェクトのリストを返します。
     * 
     * @return ベースオブジェクトのリスト
     */
    @SuppressWarnings("unchecked")
    protected List<T> getResultListInternal() {
        ResultSetHandler handler = createResultListResultSetHandler();
        List<T> ret = null;
        JdbcContext jdbcContext = jdbcManager.getJdbcContext();
        try {
            ResultSet rs = createResultSet(jdbcContext);
            ret = (List<T>) handleResultSet(handler, rs);
        } finally {
            if (!jdbcContext.isTransactional()) {
                jdbcContext.destroy();
            }
        }
        return ret;
    }

    /**
     * 検索してベースオブジェクトを返します。
     * 
     * @return ベースオブジェクト
     */
    @SuppressWarnings("unchecked")
    protected T getSingleResultInternal() {
        ResultSetHandler handler = createSingleResultResultSetHandler();
        T ret = null;
        JdbcContext jdbcContext = jdbcManager.getJdbcContext();
        try {
            ResultSet rs = createResultSet(jdbcContext);
            ret = (T) handleResultSet(handler, rs);
        } finally {
            if (!jdbcContext.isTransactional()) {
                jdbcContext.destroy();
            }
        }
        return ret;
    }

    /**
     * 準備されたステートメントを返します。
     * 
     * @param jdbcContext
     *            JDBCコンテキスト
     * @return 準備されたステートメント
     */
    protected PreparedStatement getPreparedStatement(JdbcContext jdbcContext) {
        PreparedStatement ps = jdbcContext.getPreparedStatement(executedSql);
        setupPreparedStatement(ps);
        return ps;
    }

    /**
     * カーソルつきの準備されたステートメントを返します。
     * 
     * @param jdbcContext
     *            JDBCコンテキスト
     * @return 準備されたステートメント
     */
    protected PreparedStatement getCursorPreparedStatement(
            JdbcContext jdbcContext) {
        PreparedStatement ps = jdbcContext
                .getCursorPreparedStatement(executedSql);
        setupPreparedStatement(ps);
        return ps;
    }

    /**
     * 準備されたステートメントをセットアップします。
     * 
     * @param ps
     *            準備されたステートメント
     * @param bindVariables
     *            バインド変数の配列
     * @param bindVariableClasses
     *            バインド変数のクラスの配列
     * 
     */
    protected void setupPreparedStatement(PreparedStatement ps) {
        if (maxRows > 0) {
            StatementUtil.setMaxRows(ps, maxRows);
        }
        if (fetchSize > 0) {
            StatementUtil.setFetchSize(ps, fetchSize);
        }
        if (queryTimeout > 0) {
            StatementUtil.setQueryTimeout(ps, queryTimeout);
        }
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

    /**
     * リストを返す結果セットハンドラを作成します。
     * 
     * @return 結果セットハンドラ
     */
    protected abstract ResultSetHandler createResultListResultSetHandler();

    /**
     * 単独の値を返す結果セットハンドラを作成します。
     * 
     * @return 結果セットハンドラ
     */
    protected abstract ResultSetHandler createSingleResultResultSetHandler();

    /**
     * 結果セットを作成します。
     * 
     * @param jdbcContext
     *            JDBCコンテキスト
     * @return 結果セット
     */
    protected ResultSet createResultSet(JdbcContext jdbcContext) {
        DbmsDialect dialect = jdbcManager.getDialect();
        if (offset > 0) {
            if (dialect.supportsOffset()
                    && (limit > 0 || limit == 0
                            && dialect.supportsOffsetWithoutLimit())) {
                PreparedStatement ps = getPreparedStatement(jdbcContext);
                return PreparedStatementUtil.executeQuery(ps);
            } else if (dialect.supportsCursor()) {
                PreparedStatement ps = getCursorPreparedStatement(jdbcContext);
                ResultSet rs = PreparedStatementUtil.executeQuery(ps);
                ResultSetUtil.absolute(rs, offset);
                return rs;
            } else {
                PreparedStatement ps = getPreparedStatement(jdbcContext);
                ResultSet rs = PreparedStatementUtil.executeQuery(ps);
                for (int i = 0; i < offset; i++) {
                    if (!ResultSetUtil.next(rs)) {
                        break;
                    }
                }
                return rs;
            }
        }
        PreparedStatement ps = getPreparedStatement(jdbcContext);
        return PreparedStatementUtil.executeQuery(ps);
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

    /**
     * ベースクラスを返します。
     * 
     * @return ベースクラス
     */
    public Class<T> getBaseClass() {
        return baseClass;
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
     * リミットを返します。
     * 
     * @return リミット
     */
    public int getLimit() {
        return limit;
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
     * オフセットを返します。
     * 
     * @return オフセット
     */
    public int getOffset() {
        return offset;
    }
}