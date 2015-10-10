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
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.seasar.extension.jdbc.ValueType;
import org.seasar.extension.jdbc.util.BindVariableUtil;
import org.seasar.framework.exception.SSQLException;
import org.seasar.framework.util.InputStreamUtil;

/**
 * <code>byte[]</code>用の {@link ValueType}です。
 * 
 * @author koichik
 */
public class BytesType extends AbstractValueType {

    /** 空のバイト配列 */
    public static final byte[] EMPTY_BYTES = new byte[0];

    /** バイト配列を<code>getBytes()/setBytes()</code>で扱うトレイト */
    public static final Trait BYTES_TRAIT = new BytesTrait();

    /** バイト配列を<code>getBinaryStream()/setBinaryStream()</code>で扱うトレイト */
    public static final Trait STREAM_TRAIT = new StreamTrait();

    /** バイト配列を<code>getBlob()/setBytes()</code>で扱うトレイト */
    public static final Trait BLOB_TRAIT = new BlobTrait();

    /**
     * バイト配列を操作するためのトレイトです。
     */
    protected Trait trait;

    /**
     * インスタンスを構築します。
     * 
     * @param trait
     *            トレイト
     */
    public BytesType(final Trait trait) {
        super(trait.getSqlType());
        this.trait = trait;
    }

    public void bindValue(final PreparedStatement ps, final int index,
            final Object value) throws SQLException {
        if (value == null) {
            setNull(ps, index);
        } else if (value instanceof byte[]) {
            trait.set(ps, index, (byte[]) value);
        } else {
            ps.setObject(index, value);
        }
    }

    public void bindValue(final CallableStatement cs,
            final String parameterName, final Object value) throws SQLException {
        if (value == null) {
            setNull(cs, parameterName);
        } else if (value instanceof byte[]) {
            trait.set(cs, parameterName, (byte[]) value);
        } else {
            cs.setObject(parameterName, value);
        }
    }

    public Object getValue(final ResultSet resultSet, final int index)
            throws SQLException {
        return trait.get(resultSet, index);
    }

    public Object getValue(final ResultSet resultSet, final String columnName)
            throws SQLException {
        return trait.get(resultSet, columnName);
    }

    public Object getValue(final CallableStatement cs, final int index)
            throws SQLException {
        return trait.get(cs, index);
    }

    public Object getValue(final CallableStatement cs,
            final String parameterName) throws SQLException {
        return trait.get(cs, parameterName);
    }

    public String toText(Object value) {
        if (value == null) {
            return BindVariableUtil.nullText();
        } else if (value instanceof byte[]) {
            return BindVariableUtil.toText((byte[]) value);
        }
        return BindVariableUtil.toText(value);
    }

    /**
     * {@link InputStream}からバイト配列を取得して返します。
     * 
     * @param is
     *            入力ストリーム
     * @return バイト配列
     * @throws SQLException
     *             SQL例外が発生した場合
     */
    public static byte[] toBytes(final InputStream is) throws SQLException {
        try {
            final byte[] bytes = new byte[is.available()];
            is.read(bytes);
            return bytes;
        } catch (final IOException e) {
            throw new SSQLException("ESSR0040", new Object[] { e.getMessage() });
        }
    }

    /**
     * {@link Blob}からバイト配列を取得して返します。
     * 
     * @param blob
     *            BLOB
     * @return バイト配列
     * @throws SQLException
     *             SQL例外が発生した場合
     * @throws ArrayIndexOutOfBoundsException
     *             BLOBのデータ長が<code>int</code>型の最大長を越えている場合
     */
    public static byte[] toBytes(final Blob blob) throws SQLException {
        if (blob == null) {
            return null;
        }
        final long length = blob.length();
        if (length == 0) {
            return EMPTY_BYTES;
        }
        if (length > Integer.MAX_VALUE) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return blob.getBytes(1L, (int) length);
    }

    /**
     * 
     * @author koichik
     */
    public interface Trait {
        /**
         * JDBC型を返します。
         * 
         * @return JDBC型
         */
        int getSqlType();

        /**
         * {@link PreparedStatement}にバイト配列をパラメータとして設定します。
         * 
         * @param ps
         *            準備されたステートメント
         * @param parameterIndex
         *            パラメータのインデックス
         * @param bytes
         *            パラメータ値のバイト配列
         * @throws SQLException
         *             SQL例外が発生した場合
         */
        void set(PreparedStatement ps, int parameterIndex, byte[] bytes)
                throws SQLException;

        /**
         * {@link CallableStatement}にバイト配列を名前パラメータとして設定します。
         * 
         * @param cs
         *            呼び出し可能なステートメント
         * @param parameterName
         *            パラメータ名
         * @param bytes
         *            パラメータ値のバイト配列
         * @throws SQLException
         *             SQL例外が発生した場合
         */
        void set(CallableStatement cs, String parameterName, byte[] bytes)
                throws SQLException;

        /**
         * {@link ResultSet}からバイト配列を取得します。
         * 
         * @param rs
         *            結果セット
         * @param columnIndex
         *            カラムのインデックス
         * @return バイト配列
         * @throws SQLException
         *             SQL例外が発生した場合
         */
        byte[] get(ResultSet rs, int columnIndex) throws SQLException;

        /**
         * {@link ResultSet}からバイト配列を取得します。
         * 
         * @param rs
         *            結果セット
         * @param columnName
         *            カラムの名前
         * @return バイト配列
         * @throws SQLException
         *             SQL例外が発生した場合
         */
        byte[] get(ResultSet rs, String columnName) throws SQLException;

        /**
         * {@link CallableStatement}からバイト配列を取得します。
         * 
         * @param cs
         *            呼び出し可能なステートメント
         * @param columnIndex
         *            カラムのインデックス
         * @return バイト配列
         * @throws SQLException
         *             SQL例外が発生した場合
         */
        byte[] get(CallableStatement cs, int columnIndex) throws SQLException;

        /**
         * {@link CallableStatement}からバイト配列を取得します。
         * 
         * @param cs
         *            呼び出し可能なステートメント
         * @param columnName
         *            カラムの名前
         * @return バイト配列
         * @throws SQLException
         *             SQL例外が発生した場合
         */
        byte[] get(CallableStatement cs, String columnName) throws SQLException;

    }

    /**
     * バイト配列を<code>getBytes()/setBytes()</code>で扱うトレイトです。
     * 
     * @author koichik
     */
    public static class BytesTrait implements Trait {

        public int getSqlType() {
            return Types.BINARY;
        }

        public void set(final PreparedStatement ps, final int parameterIndex,
                final byte[] bytes) throws SQLException {
            ps.setBytes(parameterIndex, bytes);
        }

        public void set(final CallableStatement cs, final String parameterName,
                final byte[] bytes) throws SQLException {
            cs.setBytes(parameterName, bytes);
        }

        public byte[] get(final ResultSet rs, final int columnIndex)
                throws SQLException {
            return rs.getBytes(columnIndex);
        }

        public byte[] get(final ResultSet rs, final String columnName)
                throws SQLException {
            return rs.getBytes(columnName);
        }

        public byte[] get(final CallableStatement cs, final int columnIndex)
                throws SQLException {
            return cs.getBytes(columnIndex);
        }

        public byte[] get(final CallableStatement cs, final String columnName)
                throws SQLException {
            return cs.getBytes(columnName);
        }

    }

    /**
     * バイト配列を<code>getBinaryStream()/setBinaryStream()</code>で扱うトレイトです。
     * 
     * @author koichik
     */
    public static class StreamTrait implements Trait {

        public int getSqlType() {
            return Types.BINARY;
        }

        public void set(final PreparedStatement ps, final int parameterIndex,
                final byte[] bytes) throws SQLException {
            ps.setBinaryStream(parameterIndex, new ByteArrayInputStream(bytes),
                    bytes.length);
        }

        public void set(final CallableStatement cs, final String parameterName,
                final byte[] bytes) throws SQLException {
            cs.setBinaryStream(parameterName, new ByteArrayInputStream(bytes),
                    bytes.length);
        }

        public byte[] get(final ResultSet rs, final int columnIndex)
                throws SQLException {
            final InputStream is = rs.getBinaryStream(columnIndex);
            try {
                return toBytes(is);
            } finally {
                InputStreamUtil.close(is);
            }
        }

        public byte[] get(final ResultSet rs, final String columnName)
                throws SQLException {
            final InputStream is = rs.getBinaryStream(columnName);
            try {
                return toBytes(is);
            } finally {
                InputStreamUtil.close(is);
            }
        }

        public byte[] get(final CallableStatement cs, final int columnIndex)
                throws SQLException {
            return cs.getBytes(columnIndex);
        }

        public byte[] get(final CallableStatement cs, final String columnName)
                throws SQLException {
            return cs.getBytes(columnName);
        }

    }

    /**
     * バイト配列を<code>getBlob()/setBinaryStream()</code>で扱うトレイトです。
     * 
     * @author koichik
     */
    public static class BlobTrait implements Trait {

        public int getSqlType() {
            return Types.BLOB;
        }

        public void set(final PreparedStatement ps, final int parameterIndex,
                final byte[] bytes) throws SQLException {
            ps.setBinaryStream(parameterIndex, new ByteArrayInputStream(bytes),
                    bytes.length);
        }

        public void set(final CallableStatement cs, final String parameterName,
                final byte[] bytes) throws SQLException {
            cs.setBinaryStream(parameterName, new ByteArrayInputStream(bytes),
                    bytes.length);
        }

        public byte[] get(final ResultSet rs, final int columnIndex)
                throws SQLException {
            return toBytes(rs.getBlob(columnIndex));
        }

        public byte[] get(final ResultSet rs, final String columnName)
                throws SQLException {
            return toBytes(rs.getBlob(columnName));
        }

        public byte[] get(final CallableStatement cs, final int columnIndex)
                throws SQLException {
            return toBytes(cs.getBlob(columnIndex));
        }

        public byte[] get(final CallableStatement cs, final String columnName)
                throws SQLException {
            return toBytes(cs.getBlob(columnName));
        }

    }

}
