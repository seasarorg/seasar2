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
import java.sql.ResultSet;
import java.sql.SQLException;

import org.seasar.extension.jdbc.ValueType;
import org.seasar.extension.jdbc.util.BindVariableUtil;

/**
 * オラクルの結果セット用の {@link ValueType}です。
 * 
 * @author higa
 * 
 */
public class OracleResultSetType extends AbstractValueType {

    /**
     * カーソル用のタイプです。
     */
    protected static int CURSOR = -10;

    /**
     * インスタンスを構築します。
     */
    public OracleResultSetType() {
        super(CURSOR);
    }

    public Object getValue(ResultSet resultSet, int index) throws SQLException {
        throw new SQLException("not supported");
    }

    public Object getValue(ResultSet resultSet, String columnName)
            throws SQLException {
        throw new SQLException("not supported");
    }

    public Object getValue(CallableStatement cs, int index) throws SQLException {
        return cs.getObject(index);
    }

    public Object getValue(CallableStatement cs, String parameterName)
            throws SQLException {
        return cs.getObject(parameterName);
    }

    public void bindValue(PreparedStatement ps, int index, Object value)
            throws SQLException {
        throw new SQLException("not supported");
    }

    public void bindValue(CallableStatement cs, String parameterName,
            Object value) throws SQLException {
        throw new SQLException("not supported");
    }

    public String toText(Object value) {
        return BindVariableUtil.nullText();
    }

}