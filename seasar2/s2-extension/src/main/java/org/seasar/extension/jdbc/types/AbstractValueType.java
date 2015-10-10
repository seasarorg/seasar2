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
package org.seasar.extension.jdbc.types;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.seasar.extension.jdbc.ValueType;

/**
 * {@link ValueType}の抽象クラスです。
 * 
 * @author taedium
 */
public abstract class AbstractValueType implements ValueType {

    /** JDBCのSQL型 */
    private int sqlType;

    /**
     * インスタンスを構築します。
     * 
     * @param sqlType
     *            JDBCのSQL型
     */
    public AbstractValueType(int sqlType) {
        this.sqlType = sqlType;
    }

    /**
     * SQLの<code>NULL</code>を設定します。
     * 
     * @param ps
     *            準備された文
     * @param index
     *            位置
     * @throws SQLException
     *             SQL例外が発生した場合
     */
    protected void setNull(PreparedStatement ps, int index) throws SQLException {
        ps.setNull(index, sqlType);
    }

    /**
     * SQLの<code>NULL</code>を設定します。
     * 
     * @param cs
     *            ストアドプロシージャを表す文
     * @param parameterName
     *            パラメータ名
     * @throws SQLException
     *             SQL例外が発生した場合
     */
    protected void setNull(CallableStatement cs, String parameterName)
            throws SQLException {
        cs.setNull(parameterName, sqlType);
    }

    public void registerOutParameter(CallableStatement cs, int index)
            throws SQLException {

        cs.registerOutParameter(index, sqlType);
    }

    public void registerOutParameter(CallableStatement cs, String parameterName)
            throws SQLException {

        cs.registerOutParameter(parameterName, sqlType);
    }

    public int getSqlType() {
        return sqlType;
    }
}
