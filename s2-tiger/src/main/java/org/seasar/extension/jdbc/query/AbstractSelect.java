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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.TemporalType;

import org.seasar.extension.jdbc.DbmsDialect;
import org.seasar.extension.jdbc.IterationCallback;
import org.seasar.extension.jdbc.JdbcContext;
import org.seasar.extension.jdbc.ResultSetHandler;
import org.seasar.extension.jdbc.Select;
import org.seasar.extension.jdbc.StatementHandler;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.extension.jdbc.exception.SNonUniqueResultException;
import org.seasar.extension.jdbc.manager.JdbcManagerImplementor;
import org.seasar.framework.util.PreparedStatementUtil;
import org.seasar.framework.util.ResultSetUtil;
import org.seasar.framework.util.StatementUtil;

/**
 * 検索の抽象クラスです。
 * 
 * @author higa
 * @param <T>
 *            戻り値のベースの型です。
 * @param <S>
 *            <code>Select</code>のサブタイプです。
 */
public abstract class AbstractSelect<T, S extends Select<T, S>> extends
        AbstractQuery<S> implements Select<T, S> {

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
     * 検索結果がなかった場合に{@link NoResultException}をスローするなら<code>true</code>です。
     */
    protected boolean disallowNoResult;

    /**
     * 戻り値型がLOBなら<code>true</code>です。
     */
    protected boolean resultLob;

    /**
     * 戻り値の時制の種別です。
     */
    protected TemporalType resultTemporalType;

    /**
     * SELECT COUNT(*)～で行数を取得する場合に<code>true</code>
     */
    protected boolean count;

    /**
     * {@link AbstractSelect}を作成します。
     * 
     * @param jdbcManager
     *            内部的なJDBCマネージャ
     * @param baseClass
     *            ベースクラス
     */
    public AbstractSelect(JdbcManagerImplementor jdbcManager, Class<T> baseClass) {
        super(jdbcManager);
        this.baseClass = baseClass;

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

    @SuppressWarnings("unchecked")
    public S limit(int limit) {
        this.limit = limit;
        return (S) this;
    }

    @SuppressWarnings("unchecked")
    public S offset(int offset) {
        this.offset = offset;
        return (S) this;
    }

    @SuppressWarnings("unchecked")
    public S disallowNoResult() {
        this.disallowNoResult = true;
        return (S) this;
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
        try {
            return getResultListInternal();
        } finally {
            completed();
        }
    }

    public T getSingleResult() throws SNonUniqueResultException {
        prepare("getSingleResult");
        logSql();
        try {
            return getSingleResultInternal();
        } finally {
            completed();
        }
    }

    public <RESULT> RESULT iterate(IterationCallback<T, RESULT> callback) {
        prepare("iterate");
        logSql();
        try {
            return iterateInternal(callback);
        } finally {
            completed();
        }
    }

    /**
     * SQLが返す結果セットの行数を返します。
     * 
     * @return SQLが返す結果セットの行数
     */
    public long getCount() {
        count = true;
        prepare("getCount");
        logSql();
        try {
            return Long.class.cast(getSingleResultInternal());
        } finally {
            completed();
        }
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
            ret = (List<T>) processResultSet(jdbcContext, handler);
            if (disallowNoResult && ret.isEmpty()) {
                throw new SNoResultException(executedSql);
            }
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
            ret = (T) processResultSet(jdbcContext, handler);
            if (disallowNoResult && ret == null) {
                throw new SNoResultException(executedSql);
            }
        } finally {
            if (!jdbcContext.isTransactional()) {
                jdbcContext.destroy();
            }
        }
        return ret;
    }

    /**
     * 検索して反復した結果を返します。
     * 
     * @param <RESULT>
     *            反復コールバックの戻り値の型
     * @param callback
     *            反復コールバック
     * @return 反復コールバックの戻り値
     */
    @SuppressWarnings("unchecked")
    protected <RESULT> RESULT iterateInternal(
            final IterationCallback<T, RESULT> callback) {
        final ResultSetHandler handler = createIterateResultSetHandler(callback);
        final JdbcContext jdbcContext = jdbcManager.getJdbcContext();
        try {
            return (RESULT) processResultSet(jdbcContext, handler);
        } finally {
            if (!jdbcContext.isTransactional()) {
                jdbcContext.destroy();
            }
        }
    }

    /**
     * 準備されたステートメントを処理します。
     * 
     * @param jdbcContext
     *            JDBCコンテキスト
     * @param handler
     *            準備されたステートメントを処理するハンドラ
     * @return 準備されたステートメントを処理した結果
     */
    protected Object processPreparedStatement(final JdbcContext jdbcContext,
            final StatementHandler<Object, PreparedStatement> handler) {
        return jdbcContext.usingPreparedStatement(executedSql,
                new StatementHandler<Object, PreparedStatement>() {

                    public Object handle(final PreparedStatement ps) {
                        setupPreparedStatement(ps);
                        return handler.handle(ps);
                    }
                });
    }

    /**
     * カーソルつきの準備されたステートメントを処理します。
     * 
     * @param jdbcContext
     *            JDBCコンテキスト
     * @param handler
     *            準備されたステートメントを処理するハンドラ
     * @return 準備されたステートメントを処理した結果
     */
    protected Object processCursorPreparedStatement(
            final JdbcContext jdbcContext,
            final StatementHandler<Object, PreparedStatement> handler) {
        return jdbcContext.usingCursorPreparedStatement(executedSql,
                new StatementHandler<Object, PreparedStatement>() {

                    public Object handle(final PreparedStatement ps) {
                        setupPreparedStatement(ps);
                        return handler.handle(ps);
                    }
                });
    }

    /**
     * 準備されたステートメントをセットアップします。
     * 
     * @param ps
     *            準備されたステートメント
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
        prepareInParams(ps);
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
     * 反復する結果セットハンドラを作成します。
     * 
     * @param callback
     *            反復コールバック
     * @return 結果セットハンドラ
     */
    protected abstract ResultSetHandler createIterateResultSetHandler(
            final IterationCallback<T, ?> callback);

    /**
     * 結果セットを処理します。
     * 
     * @param jdbcContext
     *            JDBCコンテキスト
     * @param handler
     *            結果セットを処理するハンドラ
     * @return 結果セットを処理した結果
     */
    protected Object processResultSet(final JdbcContext jdbcContext,
            final ResultSetHandler handler) {
        final DbmsDialect dialect = jdbcManager.getDialect();
        if (offset > 0) {
            if (dialect.supportsOffset()
                    && (limit > 0 || limit == 0
                            && dialect.supportsOffsetWithoutLimit())) {
                return processPreparedStatement(jdbcContext,
                        new StatementHandler<Object, PreparedStatement>() {

                            public Object handle(final PreparedStatement ps) {
                                final ResultSet rs = PreparedStatementUtil
                                        .executeQuery(ps);
                                return handleResultSet(handler, rs);
                            }
                        });
            } else if (dialect.supportsCursor()) {
                return processCursorPreparedStatement(jdbcContext,
                        new StatementHandler<Object, PreparedStatement>() {

                            public Object handle(final PreparedStatement ps) {
                                final ResultSet rs = PreparedStatementUtil
                                        .executeQuery(ps);
                                ResultSetUtil.absolute(rs, offset);
                                return handleResultSet(handler, rs);
                            }
                        });
            } else {
                return processPreparedStatement(jdbcContext,
                        new StatementHandler<Object, PreparedStatement>() {

                            public Object handle(final PreparedStatement ps) {
                                final ResultSet rs = PreparedStatementUtil
                                        .executeQuery(ps);
                                for (int i = 0; i < offset; i++) {
                                    if (!ResultSetUtil.next(rs)) {
                                        break;
                                    }
                                }
                                return handleResultSet(handler, rs);
                            }
                        });
            }
        }
        return processPreparedStatement(jdbcContext,
                new StatementHandler<Object, PreparedStatement>() {

                    public Object handle(final PreparedStatement ps) {
                        final ResultSet rs = PreparedStatementUtil
                                .executeQuery(ps);
                        return handleResultSet(handler, rs);
                    }
                });
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