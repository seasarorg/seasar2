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

import org.seasar.extension.jdbc.JdbcContext;
import org.seasar.extension.jdbc.SqlUpdate;
import org.seasar.extension.jdbc.exception.IllegalParamSizeRuntimeException;
import org.seasar.extension.jdbc.exception.SEntityExistsException;
import org.seasar.extension.jdbc.manager.JdbcManagerImplementor;
import org.seasar.framework.util.PreparedStatementUtil;
import org.seasar.framework.util.StatementUtil;

/**
 * {@link SqlUpdate}の実装クラスです。
 * 
 * @author higa
 * 
 */
public class SqlUpdateImpl extends AbstractQuery<SqlUpdate> implements
        SqlUpdate {

    private static final Object[] EMPTY_PARAMS = new Object[0];

    /**
     * パラメータのクラスの配列です。
     */
    protected Class<?>[] paramClasses;

    /**
     * パラメータの配列です。
     */
    protected Object[] params = EMPTY_PARAMS;

    /**
     * {@link SqlUpdateImpl}を作成します。
     * 
     * @param jdbcManager
     *            内部的なJDBCマネージャ
     * @param sql
     *            SQL
     * @param paramClasses
     *            パラメータのクラスの配列です。
     */
    public SqlUpdateImpl(JdbcManagerImplementor jdbcManager, String sql,
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

    public SqlUpdate params(Object... params) {
        this.params = params;
        return this;
    }

    public int execute() {
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
    protected int executeInternal() {
        if (params.length != paramClasses.length) {
            logger.log("ESSR0709", new Object[] { callerClass.getName(),
                    callerMethodName });
            throw new IllegalParamSizeRuntimeException(params.length,
                    paramClasses.length);
        }
        for (int j = 0; j < params.length; j++) {
            addParam(params[j], paramClasses[j]);
        }
        logSql();
        int ret = 0;
        JdbcContext jdbcContext = jdbcManager.getJdbcContext();
        try {
            PreparedStatement ps = getPreparedStatement(jdbcContext);
            ret = PreparedStatementUtil.executeUpdate(ps);
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
        if (queryTimeout > 0) {
            StatementUtil.setQueryTimeout(ps, queryTimeout);
        }
        prepareInParams(ps);
        return ps;
    }

    @Override
    protected void prepare(String methodName) {
        prepareCallerClassAndMethodName(methodName);
    }
}