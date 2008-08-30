/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
import java.sql.Types;
import java.util.Date;

import javax.persistence.GenerationType;
import javax.persistence.TemporalType;

import org.seasar.extension.jdbc.gen.internal.sqltype.BigIntType;
import org.seasar.extension.jdbc.gen.internal.sqltype.BinaryType;
import org.seasar.extension.jdbc.gen.internal.sqltype.BlobType;
import org.seasar.extension.jdbc.gen.internal.sqltype.BooleanType;
import org.seasar.extension.jdbc.gen.internal.sqltype.ClobType;
import org.seasar.extension.jdbc.gen.internal.sqltype.DecimalType;
import org.seasar.extension.jdbc.gen.internal.sqltype.DoubleType;
import org.seasar.extension.jdbc.gen.internal.sqltype.FloatType;
import org.seasar.extension.jdbc.gen.internal.sqltype.IntegerType;

/**
 * PostgreSQLの方言を扱うクラスです。
 * 
 * @author taedium
 */
public class PostgreGenDialect extends StandardGenDialect {

    /** テーブルが見つからないことを示すSQLステート */
    protected static String TABLE_NOT_FOUND_SQL_STATE = "42P01";

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
        sqlTypeMap.put(Types.BLOB, new BlobType("oid"));
        sqlTypeMap.put(Types.CLOB, new ClobType("text"));
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
        columnTypeMap.put("serial", PostgreColumnType.SERIAL);
        columnTypeMap.put("text", PostgreColumnType.TEXT);
        columnTypeMap.put("timestamptz", PostgreColumnType.TIMESTAMPTZ);
        columnTypeMap.put("timetz", PostgreColumnType.TIMETZ);
        columnTypeMap.put("varbit", PostgreColumnType.VARBIT);
        columnTypeMap.put("varchar", PostgreColumnType.VARCHAR);
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
    public String getSequenceDefinitionFragment(String dataType, int initValue,
            int allocationSize) {
        return dataType + " start with " + allocationSize + " increment by "
                + initValue;
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

        private static PostgreColumnType TEXT = new PostgreColumnType("text",
                String.class, true);

        private static PostgreColumnType TIMETZ = new PostgreColumnType(
                "timetz", Date.class, TemporalType.TIME);

        private static PostgreColumnType TIMESTAMPTZ = new PostgreColumnType(
                "timestamptz", Date.class, TemporalType.TIMESTAMP);

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
            super(dataType, attributeClass, lob, null);
        }

        /**
         * インスタンスを構築します。
         * 
         * @param dataType
         *            データ型
         * @param attributeClass
         *            属性のクラス
         * @param temporalType
         *            時制型
         */
        public PostgreColumnType(String dataType, Class<?> attributeClass,
                TemporalType temporalType) {
            super(dataType, attributeClass, false, temporalType);
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
}
