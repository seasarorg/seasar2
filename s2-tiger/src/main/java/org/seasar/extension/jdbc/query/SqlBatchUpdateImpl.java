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
import java.util.ArrayList;
import java.util.List;

import org.seasar.extension.jdbc.JdbcContext;
import org.seasar.extension.jdbc.SqlBatchUpdate;
import org.seasar.extension.jdbc.SqlUpdate;
import org.seasar.extension.jdbc.exception.IllegalParamSizeRuntimeException;
import org.seasar.extension.jdbc.manager.JdbcManagerImplementor;
import org.seasar.framework.util.PreparedStatementUtil;
import org.seasar.framework.util.StatementUtil;

/**
 * {@link SqlUpdate}の実装クラスです。
 * 
 * @author higa
 * 
 */
public class SqlBatchUpdateImpl extends AbstractQuery<SqlBatchUpdate> implements
        SqlBatchUpdate {

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
        for (Class<?> c : paramClasses) {
            addParam(null, c);
        }
    }

    public SqlBatchUpdate params(Object... params) {
        paramsList.add(params);
        return this;
    }

    public int[] execute() {
        prepare("executeBatch");
        int[] ret = null;
        JdbcContext jdbcContext = jdbcManager.getJdbcContext();
        try {
            PreparedStatement ps = getPreparedStatement(jdbcContext);
            for (int i = 0; i < paramsList.size(); ++i) {
                Object[] params = paramsList.get(i);
                if (params.length != paramList.size()) {
                    logger.log("ESSR0709", new Object[] {
                            callerClass.getName(), callerMethodName });
                    throw new IllegalParamSizeRuntimeException(params.length,
                            paramList.size());
                }
                for (int j = 0; j < params.length; j++) {
                    Param param = getParam(j);
                    param.value = params[j];
                }
                logSql();
                prepareInParams(ps);
                PreparedStatementUtil.addBatch(ps);
            }
            ret = PreparedStatementUtil.executeBatch(ps);
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
        return ps;
    }

    @Override
    protected void prepare(String methodName) {
        prepareCallerClassAndMethodName(methodName);
    }
}