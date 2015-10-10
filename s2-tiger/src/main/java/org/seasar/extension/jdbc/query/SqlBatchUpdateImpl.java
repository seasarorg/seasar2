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
import java.util.ArrayList;
import java.util.List;

import org.seasar.extension.jdbc.JdbcContext;
import org.seasar.extension.jdbc.SqlBatchUpdate;
import org.seasar.extension.jdbc.exception.IllegalParamSizeRuntimeException;
import org.seasar.extension.jdbc.exception.SEntityExistsException;
import org.seasar.extension.jdbc.manager.JdbcManagerImplementor;
import org.seasar.framework.util.PreparedStatementUtil;
import org.seasar.framework.util.StatementUtil;

/**
 * {@link SqlBatchUpdate}の実装クラスです。
 * 
 * @author higa
 * 
 */
public class SqlBatchUpdateImpl extends AbstractQuery<SqlBatchUpdate> implements
        SqlBatchUpdate {

    /** バッチサイズ */
    protected int batchSize;

    /**
     * パラメータのクラスの配列です。
     */
    protected Class<?>[] paramClasses;

    /**
     * パラメータの配列のリストです。
     */
    protected List<Object[]> paramsList = new ArrayList<Object[]>();

    /**
     * {@link SqlBatchUpdateImpl}を作成します。
     * 
     * @param jdbcManager
     *            内部的なJDBCマネージャ
     * @param sql
     *            SQL
     * @param paramClasses
     *            パラメータのクラスの配列です。
     */
    public SqlBatchUpdateImpl(JdbcManagerImplementor jdbcManager, String sql,
            Class<?>... paramClasses) {
        super(jdbcManager);
        if (sql == null) {
            throw new NullPointerException("sql");
        }
        this.executedSql = sql;
        if (paramClasses == null) {
            throw new NullPointerException("paramClasses");
        }
        this.paramClasses = paramClasses;
    }

    public SqlBatchUpdate batchSize(final int batchSize) {
        this.batchSize = batchSize;
        return this;
    }

    public SqlBatchUpdate params(Object... params) {
        paramsList.add(params);
        return this;
    }

    public int[] execute() {
        prepare("execute");
        try {
            return executeInternal();
        } catch (final RuntimeException e) {
            if (getJdbcManager().getDialect().isUniqueConstraintViolation(e)) {
                throw new SEntityExistsException(executedSql, e);
            }
            throw e;
        } finally {
            completed();
        }
    }

    /**
     * データベースの更新を実行します。
     * 
     * @return 更新した行数
     */
    protected int[] executeInternal() {
        final JdbcContext jdbcContext = jdbcManager.getJdbcContext();
        try {
            final PreparedStatement ps = getPreparedStatement(jdbcContext);
            final int batchSize = this.batchSize > 0 ? this.batchSize
                    : jdbcManager.getDialect().getDefaultBatchSize();
            final int size = paramsList.size();
            final int[] updateRows = new int[size];
            int pos = 0;
            for (int i = 0; i < size; ++i) {
                final Object[] params = paramsList.get(i);
                if (params.length != paramClasses.length) {
                    logger.log("ESSR0709", new Object[] {
                            callerClass.getName(), callerMethodName });
                    throw new IllegalParamSizeRuntimeException(params.length,
                            paramClasses.length);
                }
                for (int j = 0; j < params.length; j++) {
                    addParam(params[j], paramClasses[j]);
                }
                logSql();
                prepareInParams(ps);
                PreparedStatementUtil.addBatch(ps);
                resetParams();
                if (i == size - 1
                        || (batchSize > 0 && (i + 1) % batchSize == 0)) {
                    final int[] rows = PreparedStatementUtil.executeBatch(ps);
                    System.arraycopy(rows, 0, updateRows, pos, rows.length);
                    pos = i + 1;
                }
            }
            return updateRows;
        } finally {
            if (!jdbcContext.isTransactional()) {
                jdbcContext.destroy();
            }
        }
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
        if (queryTimeout > 0) {
            StatementUtil.setQueryTimeout(ps, queryTimeout);
        }
        return ps;
    }

    @Override
    protected void prepare(String methodName) {
        prepareCallerClassAndMethodName(methodName);
    }
}