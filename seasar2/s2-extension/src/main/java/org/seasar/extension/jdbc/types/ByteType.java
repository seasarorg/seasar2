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
import java.sql.Types;

import org.seasar.extension.jdbc.ValueType;
import org.seasar.extension.jdbc.util.BindVariableUtil;
import org.seasar.framework.util.ByteConversionUtil;

/**
 * Byte用の {@link ValueType}です。
 * 
 * @author higa
 */
public class ByteType extends AbstractValueType {

    /**
     * インスタンスを構築します。
     */
    public ByteType() {
        super(Types.SMALLINT);
    }

    public Object getValue(final ResultSet resultSet, final int index)
            throws SQLException {
        return ByteConversionUtil.toByte(resultSet.getObject(index));
    }

    public Object getValue(final ResultSet resultSet, final String columnName)
            throws SQLException {
        return ByteConversionUtil.toByte(resultSet.getObject(columnName));
    }

    public Object getValue(final CallableStatement cs, final int index)
            throws SQLException {
        return ByteConversionUtil.toByte(cs.getObject(index));
    }

    public Object getValue(final CallableStatement cs,
            final String parameterName) throws SQLException {
        return ByteConversionUtil.toByte(cs.getObject(parameterName));
    }

    public void bindValue(final PreparedStatement ps, final int index,
            final Object value) throws SQLException {
        if (value == null) {
            setNull(ps, index);
        } else {
            ps.setByte(index, ByteConversionUtil.toPrimitiveByte(value));
        }
    }

    public void bindValue(final CallableStatement cs,
            final String parameterName, final Object value) throws SQLException {
        if (value == null) {
            setNull(cs, parameterName);
        } else {
            cs
                    .setByte(parameterName, ByteConversionUtil
                            .toPrimitiveByte(value));
        }
    }

    public String toText(Object value) {
        if (value == null) {
            return BindVariableUtil.nullText();
        }
        Byte var = ByteConversionUtil.toByte(value);
        return BindVariableUtil.toText(var);
    }
}
