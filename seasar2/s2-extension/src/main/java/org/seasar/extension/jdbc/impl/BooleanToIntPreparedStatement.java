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
package org.seasar.extension.jdbc.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Booleanをintに変換する {@link PreparedStatement}です。
 * 
 * @author higa
 * @author manhole
 */
public class BooleanToIntPreparedStatement extends PreparedStatementWrapper {

    /**
     * {@link BooleanToIntPreparedStatement}を作成します。
     * 
     * @param original
     *            オリジナル
     * @param sql
     *            SQL
     */
    public BooleanToIntPreparedStatement(PreparedStatement original, String sql) {
        super(original, sql);
    }

    public void setBoolean(int parameterIndex, boolean x) throws SQLException {
        setInt(parameterIndex, x ? 1 : 0);
    }

    public void setNull(int paramIndex, int sqlType, String typeName)
            throws SQLException {

        super.setNull(paramIndex, changeSqlTypeIfBoolean(sqlType), typeName);
    }

    public void setNull(int parameterIndex, int sqlType) throws SQLException {
        super.setNull(parameterIndex, changeSqlTypeIfBoolean(sqlType));
    }

    /**
     * SQLの型をbooleanの場合にintegerに変換します。
     * 
     * @param sqlType
     *            SQLの型
     * @return 変換結果
     */
    protected int changeSqlTypeIfBoolean(int sqlType) {
        return sqlType == Types.BOOLEAN ? Types.INTEGER : sqlType;
    }
}