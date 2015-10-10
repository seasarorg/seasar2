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
package org.seasar.extension.jdbc.gen.internal.dialect;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;

import javax.persistence.GenerationType;

import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.ValueType;
import org.seasar.extension.jdbc.dialect.PostgreDialect.BlobImpl;
import org.seasar.extension.jdbc.gen.internal.sqltype.AbstractSqlType;
import org.seasar.extension.jdbc.gen.internal.sqltype.BigIntType;
import org.seasar.extension.jdbc.gen.internal.sqltype.BinaryType;
import org.seasar.extension.jdbc.gen.internal.sqltype.BooleanType;
import org.seasar.extension.jdbc.gen.internal.sqltype.DecimalType;
import org.seasar.extension.jdbc.gen.internal.sqltype.DoubleType;
import org.seasar.extension.jdbc.gen.internal.sqltype.FloatType;
import org.seasar.extension.jdbc.gen.internal.sqltype.IntegerType;
import org.seasar.extension.jdbc.gen.internal.sqltype.VarcharType;
import org.seasar.extension.jdbc.gen.provider.ValueTypeProvider;
import org.seasar.extension.jdbc.gen.sqltype.SqlType;
import org.seasar.framework.util.Base64Util;

/**
 * PostgreSQLの方言を扱うクラスです。
 * 
 * @author taedium
 */
public class PostgreGenDialect extends StandardGenDialect {

    /** テーブルが見つからないことを示すSQLステート */
    protected static String TABLE_NOT_FOUND_SQL_STATE = "42P01";

    /** カラムが見つからないことを示すSQLステート */
    protected static String COLUMN_NOT_FOUND_SQL_STATE = "42703";

    /** シーケンスが見つからないことを示すSQLステート */
    protected static String SEQUENCE_NOT_FOUND_SQL_STATE = "42P01";

    /**
     * インスタンスを構築します。
     */
    public PostgreGenDialect() {
        sqlTypeMap.put(Types.BIGINT, new BigIntType() {

            @Override
            public String getDataType(int length, int precision, int scale,
                    boolean identity) {
                return identity ? "bigserial" : "bigint";
            }
        });
        sqlTypeMap.put(Types.BINARY, new BinaryType("bytea"));
        sqlTypeMap.put(Types.BOOLEAN, new BooleanType("bool"));
        sqlTypeMap.put(Types.BLOB, new PostgreBlobType("oid"));
        sqlTypeMap.put(Types.CLOB, new VarcharType("text"));
        sqlTypeMap.put(Types.DECIMAL, new DecimalType("decimal($p,$s)"));
        sqlTypeMap.put(Types.DOUBLE, new DoubleType("float8"));
        sqlTypeMap.put(Types.FLOAT, new FloatType("float4"));
        sqlTypeMap.put(Types.INTEGER, new IntegerType() {

            @Override
            public String getDataType(int length, int precision, int scale,
                    boolean identity) {
                return identity ? "serial" : "integer";
            }

        });

        columnTypeMap.put("bigint", PostgreColumnType.BIGINT);
        columnTypeMap.put("bigserial", PostgreColumnType.BIGSERIAL);
        columnTypeMap.put("bit", PostgreColumnType.BIT);
        columnTypeMap.put("bool", PostgreColumnType.BOOL);
        columnTypeMap.put("bpchar", PostgreColumnType.BPCHAR);
        columnTypeMap.put("bytea", PostgreColumnType.BYTEA);
        columnTypeMap.put("float4", PostgreColumnType.FLOAT4);
        columnTypeMap.put("float8", PostgreColumnType.FLOAT8);
        columnTypeMap.put("int2", PostgreColumnType.INT2);
        columnTypeMap.put("int4", PostgreColumnType.INT4);
        columnTypeMap.put("int8", PostgreColumnType.INT8);
        columnTypeMap.put("money", PostgreColumnType.MONEY);
        columnTypeMap.put("numeric", PostgreColumnType.NUMERIC);
        columnTypeMap.put("oid", PostgreColumnType.OID);
        columnTypeMap.put("serial", PostgreColumnType.SERIAL);
        columnTypeMap.put("text", PostgreColumnType.TEXT);
        columnTypeMap.put("timestamptz", PostgreColumnType.TIMESTAMPTZ);
        columnTypeMap.put("timetz", PostgreColumnType.TIMETZ);
        columnTypeMap.put("varbit", PostgreColumnType.VARBIT);
        columnTypeMap.put("varchar", PostgreColumnType.VARCHAR);
    }

    @Override
    public String getName() {
        return "postgre";
    }

    @Override
    public SqlType getSqlType(ValueTypeProvider valueTypeProvider,
            PropertyMeta propertyMeta) {
        if (propertyMeta.isLob()) {
            if (propertyMeta.getPropertyClass() == String.class) {
                return getSqlTypeInternal(Types.CLOB);
            }
        }
        ValueType valueType = valueTypeProvider.provide(propertyMeta);
        return getSqlTypeInternal(valueType.getSqlType());
    }

    @Override
    public String getDefaultSchemaName(String userName) {
        return null;
    }

    @Override
    public GenerationType getDefaultGenerationType() {
        return GenerationType.IDENTITY;
    }

    @Override
    public boolean supportsSequence() {
        return true;
    }

    @Override
    public String getSequenceDefinitionFragment(String dataType,
            long initialValue, int allocationSize) {
        return "increment by " + allocationSize + " start with " + initialValue;
    }

    @Override
    public String getIdentityColumnDefinition() {
        return "not null";
    }

    @Override
    public boolean isTableNotFound(Throwable throwable) {
        String sqlState = getSQLState(throwable);
        return TABLE_NOT_FOUND_SQL_STATE.equals(sqlState);
    }

    @Override
    public boolean isColumnNotFound(Throwable throwable) {
        String sqlState = getSQLState(throwable);
        return COLUMN_NOT_FOUND_SQL_STATE.equals(sqlState);
    }

    @Override
    public boolean isSequenceNotFound(Throwable throwable) {
        String sqlState = getSQLState(throwable);
        return SEQUENCE_NOT_FOUND_SQL_STATE.equals(sqlState);
    }

    @Override
    public SqlBlockContext createSqlBlockContext() {
        return new PostgreSqlBlockContext();
    }

    @Override
    public boolean supportsIdentityInsert() {
        return true;
    }

    @Override
    public boolean supportsIdentity() {
        return true;
    }

    @Override
    public String getSequenceNextValString(String sequenceName,
            int allocationSize) {
        return "select nextval('" + sequenceName + "')";
    }

    @Override
    public boolean supportsCommentInCreateTable() {
        return false;
    }

    @Override
    public boolean supportsCommentOn() {
        return true;
    }

    /**
     * PostgreSQL用の{@link ColumnType}の実装です。
     * 
     * @author taedium
     */
    public static class PostgreColumnType extends StandardColumnType {

        private static PostgreColumnType BIT = new PostgreColumnType("bit($l)",
                byte[].class);

        private static PostgreColumnType BOOL = new PostgreColumnType("bool",
                Boolean.class);

        private static PostgreColumnType INT8 = new PostgreColumnType("int8",
                Long.class);

        private static PostgreColumnType BIGSERIAL = new PostgreColumnType(
                "bigserial", Long.class);

        private static PostgreColumnType BIGINT = new PostgreColumnType("oid",
                byte[].class, true);

        private static PostgreColumnType BYTEA = new PostgreColumnType("bytea",
                byte[].class);

        private static PostgreColumnType BPCHAR = new PostgreColumnType(
                "char($l)", String.class);

        private static PostgreColumnType NUMERIC = new PostgreColumnType(
                "decimal($p,$s)", BigDecimal.class) {

            @Override
            public String getColumnDefinition(int length, int precision,
                    int scale, String defaultValue) {
                precision = precision > 1000 ? 1000 : precision;
                return super.getColumnDefinition(length, precision, scale,
                        defaultValue);
            }

        };

        private static PostgreColumnType INT4 = new PostgreColumnType("int4",
                Integer.class);

        private static PostgreColumnType SERIAL = new PostgreColumnType(
                "serial", Integer.class);

        private static PostgreColumnType INT2 = new PostgreColumnType("int2",
                Short.class);

        private static PostgreColumnType FLOAT4 = new PostgreColumnType(
                "float4", Float.class);

        private static PostgreColumnType FLOAT8 = new PostgreColumnType(
                "float8", Double.class);

        private static PostgreColumnType MONEY = new PostgreColumnType("money",
                Float.class);

        private static PostgreColumnType OID = new PostgreColumnType("oid",
                byte[].class, true);

        private static PostgreColumnType TEXT = new PostgreColumnType("text",
                String.class, true);

        private static PostgreColumnType TIMETZ = new PostgreColumnType(
                "timetz", Time.class);

        private static PostgreColumnType TIMESTAMPTZ = new PostgreColumnType(
                "timestamptz", Timestamp.class);

        private static PostgreColumnType VARBIT = new PostgreColumnType(
                "varbit", byte[].class);

        private static PostgreColumnType VARCHAR = new PostgreColumnType(
                "varchar($l)", String.class) {

            @Override
            public String getColumnDefinition(int length, int precision,
                    int scale, String defaultValue) {
                length = length > 10485760 ? 10485760 : length;
                return super.getColumnDefinition(length, precision, scale,
                        defaultValue);
            }
        };

        /**
         * インスタンスを構築します。
         * 
         * @param dataType
         *            データ型
         * @param attributeClass
         *            属性のクラス
         */
        public PostgreColumnType(String dataType, Class<?> attributeClass) {
            super(dataType, attributeClass);
        }

        /**
         * インスタンスを構築します。
         * 
         * @param dataType
         *            データ型
         * @param attributeClass
         *            属性のクラス
         * @param lob
         *            LOBの場合{@code true}
         */
        public PostgreColumnType(String dataType, Class<?> attributeClass,
                boolean lob) {
            super(dataType, attributeClass, lob);
        }

    }

    /**
     * {@link StandardColumnType}の実装クラスです。
     * 
     * @author taedium
     */
    public static class PostgreSqlBlockContext implements SqlBlockContext {

        /** ブロックの内側の場合{@code true} */
        protected boolean inSqlBlock;

        public void addKeyword(String keyword) {
            if ("$$".equals(keyword)) {
                inSqlBlock = !inSqlBlock;
            }
        }

        public boolean isInSqlBlock() {
            return inSqlBlock;
        }
    }

    /**
     * PostgreSQLのBLOB型です。
     * 
     * @author taedium
     */
    public static class PostgreBlobType extends AbstractSqlType {

        /** 空のバイト配列 */
        protected static byte[] EMPTY_BYTES = new byte[] {};

        /**
         * インスタンスを構築します。
         * 
         * @param dataType
         *            データ型
         */
        public PostgreBlobType(String dataType) {
            super(dataType);
        }

        public void bindValue(PreparedStatement ps, int index, String value)
                throws SQLException {
            if (value == null) {
                ps.setNull(index, Types.BLOB);
            } else if (value.length() == 0) {
                ps.setBlob(index, new BlobImpl(EMPTY_BYTES));
            } else {
                byte[] bytes = Base64Util.decode(value);
                ps.setBlob(index, new BlobImpl(bytes));
            }
        }

        public String getValue(ResultSet resultSet, int index)
                throws SQLException {
            Blob blob = resultSet.getBlob(index);
            if (blob == null) {
                return null;
            }
            final long length = blob.length();
            if (length == 0) {
                return Base64Util.encode(EMPTY_BYTES);
            }
            if (length > Integer.MAX_VALUE) {
                throw new ArrayIndexOutOfBoundsException();
            }
            return Base64Util.encode(blob.getBytes(1L, (int) length));
        }
    }
}
