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
package org.seasar.extension.jdbc.dialect;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import javax.persistence.GenerationType;
import javax.persistence.TemporalType;

import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.SelectForUpdateType;
import org.seasar.extension.jdbc.ValueType;
import org.seasar.extension.jdbc.types.BytesType;
import org.seasar.extension.jdbc.types.SerializableType;
import org.seasar.extension.jdbc.types.ValueTypes;
import org.seasar.extension.jdbc.types.BytesType.Trait;
import org.seasar.framework.util.tiger.Pair;

/**
 * PostgreSQL用の方言をあつかうクラスです。
 * 
 * @author higa
 * 
 */
public class PostgreDialect extends StandardDialect {

    /**
     * 一意制約違反を表すSQLステート
     */
    protected static final String uniqueConstraintViolationCode = "23505";

    /**
     * BLOB用の値タイプです。
     */
    protected final static ValueType BLOB_TYPE = new BytesType(
            new PostgreTrait());

    /**
     * オブジェクトをシリアライズしたBLOB用の値タイプです。
     */
    public final static ValueType SERIALIZABLE_BLOB_TYPE = new SerializableType(
            new PostgreTrait());

    @Override
    public String getName() {
        return "postgre";
    }

    @Override
    public boolean supportsLimit() {
        return true;
    }

    @Override
    public boolean needsParameterForResultSet() {
        return true;
    }

    @Override
    public String convertLimitSql(String sql, int offset, int limit) {
        StringBuilder buf = new StringBuilder(sql.length() + 20);
        buf.append(sql);
        if (limit > 0) {
            buf.append(" limit ");
            buf.append(limit);
        }
        if (offset > 0) {
            buf.append(" offset ");
            buf.append(offset);
        }
        return buf.toString();
    }

    @Override
    public ValueType getValueType(PropertyMeta propertyMeta) {
        final Class<?> clazz = propertyMeta.getPropertyClass();
        if (propertyMeta.isLob()) {
            if (clazz == String.class) {
                return ValueTypes.STRING;
            } else if (clazz == byte[].class) {
                return BLOB_TYPE;
            } else if (Serializable.class.isAssignableFrom(clazz)) {
                return SERIALIZABLE_BLOB_TYPE;
            }
        }
        final ValueType valueType = getValueTypeInternal(clazz);
        if (valueType != null) {
            return valueType;
        }
        return super.getValueType(propertyMeta);
    }

    @Override
    public ValueType getValueType(Class<?> clazz, boolean lob,
            TemporalType temporalType) {
        if (lob) {
            if (clazz == String.class) {
                return ValueTypes.STRING;
            } else if (clazz == byte[].class) {
                return BLOB_TYPE;
            } else if (Serializable.class.isAssignableFrom(clazz)) {
                return SERIALIZABLE_BLOB_TYPE;
            }
        }
        return super.getValueType(clazz, lob, temporalType);
    }

    @Override
    protected ValueType getValueTypeInternal(Class<?> clazz) {
        if (List.class.isAssignableFrom(clazz)) {
            return ValueTypes.POSTGRE_RESULT_SET;
        }
        return null;
    }

    @Override
    public GenerationType getDefaultGenerationType() {
        return GenerationType.IDENTITY;
    }

    @Override
    public boolean supportsIdentity() {
        return true;
    }

    @Override
    public boolean supportsGetGeneratedKeys() {
        return false;
    }

    @Override
    public String getIdentitySelectString(final String tableName,
            final String columnName) {
        return new String(new StringBuilder(64).append("select currval('")
                .append(tableName).append('_').append(columnName).append(
                        "_seq')"));
    }

    @Override
    public boolean supportsSequence() {
        return true;
    }

    @Override
    public String getSequenceNextValString(final String sequenceName,
            final int allocationSize) {
        return "select nextval('" + sequenceName + "')";
    }

    @Override
    public boolean supportsForUpdate(final SelectForUpdateType type,
            boolean withTarget) {
        return type == SelectForUpdateType.NORMAL;
    }

    @Override
    public String getForUpdateString(final SelectForUpdateType type,
            final int waitSeconds, final Pair<String, String>... aliases) {
        final StringBuilder buf = new StringBuilder(100).append(" for update");
        if (aliases.length > 0) {
            buf.append(" of ");
            for (final Pair<String, String> alias : aliases) {
                buf.append(alias.getFirst()).append(", ");
            }
            buf.setLength(buf.length() - 2);
        }
        return new String(buf);
    }

    @Override
    public boolean supportsOuterJoinForUpdate() {
        return false;
    }

    @Override
    public boolean isUniqueConstraintViolation(Throwable t) {
        final String state = getSQLState(t);
        return uniqueConstraintViolationCode.equals(state);
    }

    /**
     * {@link Blob}を扱うトレイトです。
     * 
     * @author koichik
     */
    public static class PostgreTrait implements Trait {

        public int getSqlType() {
            return Types.BLOB;
        }

        public void set(final PreparedStatement ps, final int parameterIndex,
                final byte[] bytes) throws SQLException {
            ps.setBlob(parameterIndex, new BlobImpl(bytes));
        }

        public void set(final CallableStatement cs, final String parameterName,
                final byte[] bytes) throws SQLException {
            cs.setBytes(parameterName, bytes);
        }

        public byte[] get(final ResultSet rs, final int columnIndex)
                throws SQLException {
            return BytesType.toBytes(rs.getBlob(columnIndex));
        }

        public byte[] get(final ResultSet rs, final String columnName)
                throws SQLException {
            return BytesType.toBytes(rs.getBlob(columnName));
        }

        public byte[] get(final CallableStatement cs, final int columnIndex)
                throws SQLException {
            return BytesType.toBytes(cs.getBlob(columnIndex));
        }

        public byte[] get(final CallableStatement cs, final String columnName)
                throws SQLException {
            return BytesType.toBytes(cs.getBlob(columnName));
        }

    }

    /**
     * {@link Blob}の簡易実装クラスです。
     * 
     * @author koichik
     */
    public static class BlobImpl implements Blob {

        /** バイト列 */
        protected byte[] bytes;

        /**
         * インスタンスを構築します。
         * 
         * @param bytes
         *            バイト列
         */
        public BlobImpl(final byte[] bytes) {
            this.bytes = bytes;
        }

        public InputStream getBinaryStream() throws SQLException {
            return new ByteArrayInputStream(bytes);
        }

        public byte[] getBytes(final long pos, final int length)
                throws SQLException {
            if (length == bytes.length) {
                return bytes;
            }
            final byte[] result = new byte[length];
            System.arraycopy(bytes, 0, result, 0, length);
            return result;
        }

        public long length() throws SQLException {
            return bytes.length;
        }

        public long position(final Blob pattern, final long start)
                throws SQLException {
            throw new UnsupportedOperationException("position");
        }

        public long position(final byte[] pattern, final long start)
                throws SQLException {
            throw new UnsupportedOperationException("position");
        }

        public OutputStream setBinaryStream(final long pos) throws SQLException {
            throw new UnsupportedOperationException("setBinaryStream");
        }

        public int setBytes(final long pos, final byte[] bytes,
                final int offset, final int len) throws SQLException {
            throw new UnsupportedOperationException("setBytes");
        }

        public int setBytes(final long pos, final byte[] bytes)
                throws SQLException {
            throw new UnsupportedOperationException("setBytes");
        }

        public void truncate(final long len) throws SQLException {
            throw new UnsupportedOperationException("truncate");
        }

    }

}
