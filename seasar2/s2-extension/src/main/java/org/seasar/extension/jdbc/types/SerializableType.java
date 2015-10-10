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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.seasar.extension.jdbc.ValueType;
import org.seasar.extension.jdbc.util.BindVariableUtil;
import org.seasar.framework.exception.SSQLException;

/**
 * オブジェクトをシリアライズしたバイト配列用の {@link ValueType}です。
 * 
 * @author higa
 */
public class SerializableType extends BytesType {

    /**
     * インスタンスを構築します。
     * 
     * @param trait
     *            トレイト
     */
    public SerializableType(Trait trait) {
        super(trait);
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

    /**
     * オブジェクトをシリアライズしてバイト配列に変換します。
     * 
     * @param o
     *            オブジェクト
     * @return オブジェクトをシリアライズしたバイト配列
     * @throws SQLException
     *             SQL例外が発生した場合
     */
    protected byte[] serialize(final Object o) throws SQLException {
        if (o == null) {
            return null;
        }
        try {
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            final ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(o);
            try {
                return baos.toByteArray();
            } finally {
                oos.close();
            }
        } catch (final Exception e) {
            throw new SSQLException("ESSR0017", new Object[] { e }, e);
        }
    }

    /**
     * バイト配列をデシリアライズしてオブジェクトに変換します。
     * 
     * @param bytes
     *            バイト配列
     * @return バイト配列をデシリアライズしたオブジェクト
     * @throws SQLException
     *             SQL例外が発生した場合
     */
    protected Object deserialize(final Object bytes) throws SQLException {
        if (bytes == null) {
            return null;
        }
        try {
            final ByteArrayInputStream bais = new ByteArrayInputStream(
                    (byte[]) bytes);
            final ObjectInputStream ois = new ObjectInputStream(bais);
            try {
                return ois.readObject();
            } finally {
                ois.close();
            }
        } catch (final Exception e) {
            throw new SSQLException("ESSR0017", new Object[] { e }, e);
        }
    }

    public String toText(Object value) {
        if (value == null) {
            return BindVariableUtil.nullText();
        }
        return BindVariableUtil.toText(value);
    }
}
