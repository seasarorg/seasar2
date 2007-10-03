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

import java.util.Arrays;

import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.SqlSelect;
import org.seasar.extension.jdbc.exception.NullBindVariableRuntimeException;

/**
 * {@link SqlSelect}の実装クラスです。
 * 
 * @author higa
 * @param <T>
 *            戻り値のベースの型です。
 * 
 */
public class SqlSelectImpl<T> extends AbstractSqlSelect<T> implements
        SqlSelect<T> {

    /**
     * 空のパラメータです。
     */
    protected static final Object[] EMPTY_PARAMETERS = new Object[0];

    /**
     * SQLです。
     */
    protected String sql;

    /**
     * {@link SqlSelectImpl}を作成します。
     * 
     * @param jdbcManager
     *            JDBCマネージャ
     * @param baseClass
     *            ベースクラス
     * @param sql
     *            SQL
     * @see #SqlSelectImpl(JdbcManager, Class, String, Object[])
     */
    public SqlSelectImpl(JdbcManager jdbcManager, Class<T> baseClass, String sql) {
        this(jdbcManager, baseClass, sql, EMPTY_PARAMETERS);
    }

    /**
     * {@link SqlSelectImpl}を作成します。
     * 
     * @param jdbcManager
     *            JDBCマネージャ
     * @param baseClass
     *            ベースクラス
     * @param sql
     *            SQL
     * @param parameters
     *            パラメータの配列です。
     */
    public SqlSelectImpl(JdbcManager jdbcManager, Class<T> baseClass,
            String sql, Object... parameters) {
        super(jdbcManager, baseClass);
        if (sql == null) {
            throw new NullPointerException("sql");
        }
        this.sql = sql;
        if (parameters == null) {
            throw new NullPointerException("parameters");
        }
        bindVariableList.addAll(Arrays.asList(parameters));
    }

    public SqlSelect<T> callerClass(Class<?> callerClass) {
        this.callerClass = callerClass;
        return this;
    }

    public SqlSelect<T> callerMethodName(String callerMethodName) {
        this.callerMethodName = callerMethodName;
        return this;
    }

    public SqlSelect<T> maxRows(int maxRows) {
        this.maxRows = maxRows;
        return this;
    }

    public SqlSelect<T> fetchSize(int fetchSize) {
        this.fetchSize = fetchSize;
        return this;
    }

    public SqlSelect<T> queryTimeout(int queryTimeout) {
        this.queryTimeout = queryTimeout;
        return this;
    }

    public SqlSelect<T> limit(int limit) {
        this.limit = limit;
        return this;
    }

    public SqlSelect<T> offset(int offset) {
        this.offset = offset;
        return this;
    }

    @Override
    protected void prepare(String methodName) {
        prepareCallerClassAndMethodName(methodName);
        prepareParameters();
        executedSql = convertLimitSql(sql);
    }

    /**
     * パラメータを準備します。
     * 
     * @throws NullBindVariableRuntimeException
     *             パラメータの値が<code>null</code>の場合
     */
    protected void prepareParameters() throws NullBindVariableRuntimeException {
        int size = bindVariableList.size();
        for (int i = 0; i < size; i++) {
            Object var = bindVariableList.get(i);
            if (var == null) {
                logger.log("ESSR0709", new Object[] { callerClass.getName(),
                        callerMethodName });
                logger.log("ESSR0732", new Object[] { sql });
                throw new NullBindVariableRuntimeException();
            }
            bindVariableClassList.add(var.getClass());
        }
    }

    /**
     * SQLを返します。
     * 
     * @return SQL
     */
    public String getSql() {
        return sql;
    }
}