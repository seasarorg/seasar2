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

import org.seasar.extension.jdbc.SqlSelect;
import org.seasar.extension.jdbc.manager.JdbcManagerImplementor;

/**
 * {@link SqlSelect}の実装クラスです。
 * 
 * @author higa
 * @param <T>
 *            戻り値のベースの型です。
 */
public class SqlSelectImpl<T> extends AbstractSqlSelect<T, SqlSelect<T>>
        implements SqlSelect<T> {

    /**
     * SQLです。
     */
    protected String sql;

    /**
     * {@link SqlSelectImpl}を作成します。
     * 
     * @param jdbcManager
     *            内部的なJDBCマネージャ
     * @param baseClass
     *            ベースクラス
     * @param sql
     *            SQL
     * @param params
     *            パラメータの配列です。
     */
    public SqlSelectImpl(JdbcManagerImplementor jdbcManager,
            Class<T> baseClass, String sql, Object... params) {
        super(jdbcManager, baseClass);
        if (sql == null) {
            throw new NullPointerException("sql");
        }
        this.sql = sql;
        if (params == null) {
            throw new NullPointerException("parameters");
        }
        for (Object o : params) {
            addParam(o);
        }
    }

    @Override
    protected void prepare(String methodName) {
        prepareCallerClassAndMethodName(methodName);
        prepareSql();
    }

    /**
     * SQLを準備します。
     */
    protected void prepareSql() {
        if (count) {
            executedSql = convertGetCountSql(sql);
        } else {
            executedSql = convertLimitSql(sql);
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