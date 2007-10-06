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
import java.util.Arrays;

import org.seasar.extension.jdbc.JdbcContext;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.SqlUpdate;
import org.seasar.framework.util.PreparedStatementUtil;
import org.seasar.framework.util.StatementUtil;

/**
 * {@link SqlUpdate}の実装クラスです。
 * 
 * @author higa
 * 
 */
public class SqlUpdateImpl extends AbstractQuery implements SqlUpdate {

    /**
     * {@link SqlUpdateImpl}を作成します。
     * 
     * @param jdbcManager
     *            JDBCマネージャ
     * @param sql
     *            SQL
     * @param paramClasses
     *            パラメータのクラスの配列です。
     */
    public SqlUpdateImpl(JdbcManager jdbcManager, String sql,
            Class<?>... paramClasses) {
        super(jdbcManager);
        if (sql == null) {
            throw new NullPointerException("sql");
        }
        this.executedSql = sql;
        if (paramClasses == null) {
            throw new NullPointerException("paramClasses");
        }
        bindVariableClassList.addAll(Arrays.asList(paramClasses));
    }

    public SqlUpdate callerClass(Class<?> callerClass) {
        this.callerClass = callerClass;
        return this;
    }

    public SqlUpdate callerMethodName(String callerMethodName) {
        this.callerMethodName = callerMethodName;
        return this;
    }

    public SqlUpdate queryTimeout(int queryTimeout) {
        this.queryTimeout = queryTimeout;
        return this;
    }

    public SqlUpdate params(Object... params) {
        bindVariableList.addAll(Arrays.asList(params));
        return this;
    }

    public int execute() {
        prepare("execute");
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
        prepareBindVariables(ps);
        return ps;
    }

    @Override
    protected void prepare(String methodName) {
        prepareCallerClassAndMethodName(methodName);
    }
}