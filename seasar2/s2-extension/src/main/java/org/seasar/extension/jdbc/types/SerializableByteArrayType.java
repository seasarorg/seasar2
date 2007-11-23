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
package org.seasar.extension.jdbc.types;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.seasar.extension.jdbc.ValueType;

/**
 * オブジェクトをシリアライズしたBLOB用の {@link ValueType}です。
 * 
 * @author higa
 */
public class SerializableByteArrayType extends ByteArrayType {

    /**
     * インスタンスを構築します。
     */
    public SerializableByteArrayType() {
    }

    public Object getValue(final ResultSet resultSet, final int index)
            throws SQLException {
        return deserialize(super.getValue(resultSet, index));
    }

    public Object getValue(final ResultSet resultSet, final String columnName)
            throws SQLException {
        return deserialize(super.getValue(resultSet, columnName));
    }

    public Object getValue(final CallableStatement cs, final int index)
            throws SQLException {
        return deserialize(super.getValue(cs, index));
    }

    public Object getValue(final CallableStatement cs,
            final String parameterName) throws SQLException {
        return deserialize(super.getValue(cs, parameterName));
    }

    public void bindValue(final PreparedStatement ps, final int index,
            final Object value) throws SQLException {
        super.bindValue(ps, index, serialize(value));
    }

    public void bindValue(final CallableStatement cs,
            final String parameterName, final Object value) throws SQLException {
        super.bindValue(cs, parameterName, serialize(value));
    }

}
