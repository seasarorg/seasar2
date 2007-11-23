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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import org.seasar.extension.jdbc.ValueType;
import org.seasar.framework.exception.SSQLException;

/**
 * Binary用の抽象 {@link ValueType}です。
 * 
 * @author higa
 */
public abstract class AbstractBinaryType extends AbstractValueType {

    /**
     * インスタンスを構築します。
     */
    public AbstractBinaryType() {
        super(Types.BINARY);
    }

    public void bindValue(final PreparedStatement ps, final int index,
            final Object value) throws SQLException {
        if (value == null) {
            setNull(ps, index);
        } else if (value instanceof byte[]) {
            final byte[] ba = (byte[]) value;
            final InputStream in = new ByteArrayInputStream(ba);
            ps.setBinaryStream(index, in, ba.length);
        } else {
            ps.setObject(index, value);
        }
    }

    public void bindValue(final CallableStatement cs,
            final String parameterName, final Object value) throws SQLException {
        if (value == null) {
            setNull(cs, parameterName);
        } else if (value instanceof byte[]) {
            final byte[] ba = (byte[]) value;
            final InputStream in = new ByteArrayInputStream(ba);
            cs.setBinaryStream(parameterName, in, ba.length);
        } else {
            cs.setObject(parameterName, value);
        }
    }

    /**
     * {@link Blob}をバイト配列に変換します。
     * 
     * @param blob
     *            BLOB
     * @return バイト配列
     * @throws SQLException
     *             SQL例外が発生した場合
     */
    protected byte[] toByteArray(final Blob blob) throws SQLException {
        if (blob == null) {
            return null;
        }
        final long l = blob.length();
        if (Integer.MAX_VALUE < l) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return blob.getBytes(1, (int) l);
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
            oos.close();
            return baos.toByteArray();
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
            ois.close();
            return ois.readObject();
        } catch (final Exception e) {
            throw new SSQLException("ESSR0017", new Object[] { e }, e);
        }
    }

}
